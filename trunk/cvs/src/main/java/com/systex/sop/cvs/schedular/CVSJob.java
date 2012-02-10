package com.systex.sop.cvs.schedular;

import java.sql.Timestamp;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.systex.sop.cvs.dao.CVSLoginDAO;
import com.systex.sop.cvs.dao.CommonDAO;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.task.LogFutureTask;
import com.systex.sop.cvs.task.TaskResult;
import com.systex.sop.cvs.task.WriteFutureTask;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.util.HostnameUtil;

public class CVSJob implements Job {
	private CommonDAO commonDAO = new CommonDAO();
	private CVSLoginDAO loginDAO = new CVSLoginDAO();
	private LogFutureTask logFuture = null;
	private WriteFutureTask writeFuture = null;
	
	public static Timestamp getAutoSyncDate() {
		return new Timestamp(System.currentTimeMillis() - 86400000L);	// 自動同步需包含前一日 (避免漏掉前日尾段)
	}
	
	private void doSync(Timestamp date, boolean isFullSync) {
		
		/** 進行登入 **/
		Tbsoptcvslogin login = null;
		try {
			// 登入 CVS (直至登出前都不允許其他人作業)
			login = loginDAO.doLogin(HostnameUtil.getHostname());
			if (login != null) {
				String msg = "登入失敗, 目前正在執行之使用者為" + login.getCreator();
				CVSLog.getLogger().error(msg);
				StartUI.getInstance().getFrame().setMessage(msg);
				return;
			}
			TaskResult.clearResult();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			StartUI.getInstance().getFrame().setMessage("登入發生異常...(" + e.getMessage() + ")");
			return;
		}
		
		/** 進行同步 **/
		try {
			// 清空資料表 (完全同步)
			if (isFullSync) {
				StartUI.getInstance().getFrame().setMessage("清空所有資料中...");
				try {
					commonDAO.executeSQL("truncate table tbsoptcvstag");
					commonDAO.executeSQL("truncate table tbsoptcvsver");
					commonDAO.executeSQL("truncate table tbsoptcvsmap");
					StartUI.getInstance().getFrame().setMessage("所有資料已清完成");
				}catch(Exception e) {
					StartUI.getInstance().getFrame().setMessage("清空所有資料失敗");
					CVSLog.getLogger().error(this, e);
					return;
				}
			}
			
			// 取得紀錄檔
			logFuture = new LogFutureTask();
			StartUI.getInstance().getFrame().setMessage("同步紀錄檔中...");
			if (logFuture.execute(date)) {
				StartUI.getInstance().getFrame().setMessage("同步紀錄檔完成");
				
				// 寫入紀錄檔至資料庫
				writeFuture = new WriteFutureTask();
				StartUI.getInstance().getFrame().setMessage("寫入紀錄檔中...");
				if (writeFuture.execute(HostnameUtil.getHostname(), date, isFullSync)) {
					StartUI.getInstance().getFrame().setMessage("寫入紀錄檔完成 (同步完成)");
				}else{
					StartUI.getInstance().getFrame().setMessage("寫入紀錄檔失敗");
				}
			}else{
				StartUI.getInstance().getFrame().setMessage("同步紀錄檔失敗");
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			/** 進行登出 **/
			Tbsoptcvslogin logout = loginDAO.doLogout(HostnameUtil.getHostname());
			if (logout != null) {
				String msg = "登出失敗, 目前使用者為" + logout.getCreator();
				CVSLog.getLogger().error(msg);
				StartUI.getInstance().getFrame().setMessage(msg);
			}
		}
	}
	
	/**
	 * 進行同步處理 (手動/自動 )
	 */
	public void execute(final Timestamp date, final boolean isFullSync) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				doSync(date, isFullSync);
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
				StartUI.getInstance().getFrame().setCxtMessage("Interrupt");
			}
		}
		
		if (writeFuture != null) {
			if (! (writeFuture.getService().isTerminated() || writeFuture.getService().isShutdown()) ) {
				writeFuture.getService().shutdownNow();
				StartUI.getInstance().getFrame().setCxtMessage("Interrupt");
			}
		}
	}
	
	/**
	 * 進行同步處理 (自動排程)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		execute(getAutoSyncDate(), false);
	}

}
