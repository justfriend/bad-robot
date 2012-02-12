package com.systex.sop.cvs.schedular;

import java.sql.Timestamp;

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
	private LogFutureTask logFuture = null;
	private WriteFutureTask writeFuture = null;
	
	public static Timestamp getAutoSyncDate() {
		return new Timestamp(System.currentTimeMillis() - 86400000L);	// 自動同步需包含前一日 (避免漏掉前日尾段)
	}
	
	private void doSync(Timestamp date, boolean isFullSync, boolean isSyncLog, boolean isSyncWrite, boolean isUI) {
		String msg = null;
		
		/** 進行登入 **/
		Tbsoptcvslogin login = null;
		try {
			// 登入 CVS (直至登出前都不允許其他人作業)
			login = loginDAO.doLogin(HostnameUtil.getHostname());
			if (login != null) {
				msg = "登入失敗, 目前正在執行之使用者為" + login.getCreator();
				CVSLog.getLogger().error(msg);
				if (isUI) {
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}
				return;
			}
			TaskSyncResult.clearResult();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			if (isUI) StartUI.getInstance().getFrame().setMessage("登入發生異常...(" + e.getMessage() + ")");
			return;
		}
		
		/** 進行同步 **/
		try {
			StopWatch s = new StopWatch();
			s.start();
			
			// 清空資料表 (完全同步)
			if (isFullSync && isSyncWrite) {
				if (isUI) StartUI.getInstance().getFrame().setMessage("清空所有資料中...");
				try {
					commonDAO.executeSQL("truncate table tbsoptcvstag");
					commonDAO.executeSQL("truncate table tbsoptcvsver");
					commonDAO.executeSQL("truncate table tbsoptcvsmap");
					if (isUI) {
						msg = "所有資料已清完成";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}catch(Exception e) {
					if (isUI) {
						msg = "清空所有資料失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
					CVSLog.getLogger().error(this, e);
					return;
				}
			}
			
			// 取得紀錄檔
			if (isSyncLog) {
				logFuture = new LogFutureTask(true);
				if (isUI) StartUI.getInstance().getFrame().setMessage("同步紀錄檔中...");
				if (logFuture.execute(date)) {
					if (isUI) {
						msg = "同步紀錄檔完成";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}else{
					if (isUI) {
						msg = "同步紀錄檔失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}
			}
			
			// 寫入紀錄檔至資料庫
			if (isSyncWrite) {
				writeFuture = new WriteFutureTask(true);
				if (isUI) StartUI.getInstance().getFrame().setMessage("寫入紀錄檔中...");
				if (writeFuture.execute(date, isFullSync)) {
					if (isUI) {
						msg = "寫入紀錄檔完成";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}else{
					if (isUI) { 
						msg = "寫入紀錄檔失敗";
						StartUI.getInstance().getFrame().setMessage(msg);
						CxtMessageQueue.addCxtMessage(msg);
					}
				}
			}
			
			s.stop();
			if (isUI) {
				msg = StringUtil.concat("同步完成, 耗時：", CVSFunc.fxElapseTime(s.getTime()));
				StartUI.getInstance().getFrame().setMessage(msg);
				CxtMessageQueue.addCxtMessage(msg);
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			/** 進行登出 **/
			Tbsoptcvslogin logout = loginDAO.doLogout(HostnameUtil.getHostname());
			if (logout != null) {
				CVSLog.getLogger().error(msg);
				if (isUI) {
					msg = "登出失敗, 目前使用者為" + logout.getCreator();
					StartUI.getInstance().getFrame().setMessage(msg);
					CxtMessageQueue.addCxtMessage(msg);
				}
			}
		}
	}
	
	/**
	 * 進行同步處理 (手動/自動 )
	 */
	public void execute(final Timestamp date, final boolean isFullSync, final boolean isSyncLog, final boolean isSyncWrite, final boolean isUI) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doSync(date, isFullSync, isSyncLog, isSyncWrite, isUI);
			}
		}).start();
	}
	
	/**
	 * 立即終止一切同步
	 */
	public void shutdownNow() {
		if (logFuture != null) {
			if (! (logFuture.getService().isTerminated() || logFuture.getService().isShutdown()) ) {
				logFuture.getService().shutdownNow();
				CxtMessageQueue.addCxtMessage("Interrupt");
			}
		}
		
		if (writeFuture != null) {
			if (! (writeFuture.getService().isTerminated() || writeFuture.getService().isShutdown()) ) {
				writeFuture.getService().shutdownNow();
				CxtMessageQueue.addCxtMessage("Interrupt");
			}
		}
	}
	
	/**
	 * 進行同步處理 (自動排程)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		execute(getAutoSyncDate(), false, true, true, true);
	}

}
