package com.systex.sop.cvs.schedular;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.systex.sop.cvs.dao.CVSLoginDAO;
import com.systex.sop.cvs.dao.CommonDAO;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.message.CxtMessageQueue;
import com.systex.sop.cvs.task.LogFutureTask;
import com.systex.sop.cvs.task.TaskSyncResult;
import com.systex.sop.cvs.task.WriteFutureTask;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.util.HostnameUtil;
import com.systex.sop.cvs.util.StringUtil;

public class CVSJob implements Job {
	private CommonDAO commonDAO = new CommonDAO();
	private CVSLoginDAO loginDAO = new CVSLoginDAO();
	
	public static Timestamp getAutoSyncDate() {
		return new Timestamp(System.currentTimeMillis() - 86400000L);	// 自動同步需包含前一日 (避免漏掉前日尾段)
	}
	
	private void doSync(Timestamp date, boolean isFullSync, boolean isSyncLog, boolean isSyncWrite) {
		String msg = null;
		
		/** 進行登入 **/
		Tbsoptcvslogin login = null;
		try {
			// 登入 CVS (直至登出前都不允許其他人作業)
			login = loginDAO.doLogin(HostnameUtil.getHostname());
			if (login != null) {
				msg = "登入失敗, 當前使用者為" + login.getCreator();
				CVSLog.getLogger().error(msg);
				StartUI.getInstance().getFrame().setMessage(msg);
				CxtMessageQueue.addCxtMessage(msg);
				return;
			}
			TaskSyncResult.clearResult();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("登入發生異常...(" + e.getMessage() + ")");
			return;
		}

		/** 進行同步 **/
		try {
			StopWatch s = new StopWatch();
			s.start();
			
			// 取得紀錄檔
			if (isSyncLog) {
				LogFutureTask.getInstance().newService();
				StartUI.getInstance().getFrame().setMessage("同步紀錄檔中...");
				if (LogFutureTask.getInstance().execute(date, isFullSync)) {
					msg = "同步紀錄檔完成";
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}else{
					isSyncWrite = false;
					msg = "同步紀錄檔失敗";
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}
			}
			
			// 寫入紀錄檔至資料庫
			if (isSyncWrite) {
				
				// 清空資料表 (完全同步)
				if (isFullSync && isSyncWrite) {
					StartUI.getInstance().getFrame().setMessage("清空所有資料中...");
					try {
						commonDAO.executeSQL("truncate table tbsoptcvstag");
						commonDAO.executeSQL("truncate table tbsoptcvsver");
						commonDAO.executeSQL("truncate table tbsoptcvsmap");
						msg = "所有資料已清空";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}catch(Exception e) {
						msg = "清空資料失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
						CVSLog.getLogger().error(this, e);
						return;
					}
				}
				
				// 刪除TAG-INDEX
				if (isFullSync) {
					try {
						StartUI.getInstance().getFrame().setMessage("刪除TAG索引中...");
						commonDAO.executeSQL("drop index INX_TAG");
						msg = "刪除TAG索引完成";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}catch(Exception e){
						CVSLog.getLogger().warn(this, e);
						msg = "刪除TAG索引失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}
				
				WriteFutureTask.getInstance().newService();
				StartUI.getInstance().getFrame().setMessage("寫入紀錄檔中...");
				if (WriteFutureTask.getInstance().execute(date, isFullSync)) {
					msg = "寫入紀錄檔完成";
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}else{
					msg = "寫入紀錄檔失敗";
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}
				
				// 重建TAG-INDEX
				if (isFullSync) {
					try {
						StartUI.getInstance().getFrame().setMessage("重建TAG索引中...");
						commonDAO.executeSQL("create index INX_TAG on TBSOPTCVSTAG(M_SID) tablespace SOPA_INDEX");
						msg = "重建TAG索引完成";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}catch(Exception e){
						CVSLog.getLogger().warn(this, e);
						msg = "重建TAG索引失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}
			}
			
			s.stop();
			msg = StringUtil.concat("同步完成, 耗時：", CVSFunc.fxElapseTime(s.getTime()));
			StartUI.getInstance().getFrame().setMessage(msg);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			/** 進行登出 **/
			Tbsoptcvslogin logout = loginDAO.doLogout(HostnameUtil.getHostname());
			if (logout != null) {
				CVSLog.getLogger().error(msg);
				msg = "登出失敗, 目前使用者為" + logout.getCreator();
				StartUI.getInstance().getFrame().setMessage(msg);
				CxtMessageQueue.addCxtMessage(msg);
			}
		}
	}
	
	/**
	 * 進行同步處理 (手動/自動 )
	 */
	public void execute(final Timestamp date, final boolean isFullSync, final boolean isSyncLog, final boolean isSyncWrite) {
		doSync(date, isFullSync, isSyncLog, isSyncWrite);
	}
	
	/**
	 * 立即終止一切同步
	 */
	public void shutdownNow() {
		if (!(LogFutureTask.getInstance().getService().isTerminated() ||
			  LogFutureTask.getInstance().getService().isShutdown()) )
		{
			LogFutureTask.getInstance().getService().shutdown();
			try {
				if (!LogFutureTask.getInstance().getService().awaitTermination(10, TimeUnit.SECONDS)) {
					LogFutureTask.getInstance().getService().shutdownNow();
					if (!LogFutureTask.getInstance().getService().awaitTermination(60, TimeUnit.SECONDS)) {
						throw new RuntimeException("Service did not terminate");
					}
					CxtMessageQueue.addCxtMessage("Interrupt");
				}
			}catch(InterruptedException e){
				LogFutureTask.getInstance().getService().shutdownNow();
			}
		}
		
		if (!(WriteFutureTask.getInstance().getService().isTerminated() ||
			  WriteFutureTask.getInstance().getService().isShutdown()) ) {
			WriteFutureTask.getInstance().getService().shutdown();
			try {
				if (!WriteFutureTask.getInstance().getService().awaitTermination(5, TimeUnit.SECONDS)) {
					WriteFutureTask.getInstance().getService().shutdownNow();
					if (!WriteFutureTask.getInstance().getService().awaitTermination(5, TimeUnit.SECONDS)) {
						throw new RuntimeException("Service did not terminate");
					}
					CxtMessageQueue.addCxtMessage("Interrupt");
				}
			}catch(InterruptedException e){
				WriteFutureTask.getInstance().getService().shutdownNow();
			}
		}
	}
	
	/**
	 * 進行同步處理 (自動排程)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		execute(getAutoSyncDate(), false, true, true);
	}

}
