package com.systex.sop.cvs.schedular;

import java.sql.Timestamp;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.systex.sop.cvs.dao.CommonDAO;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.task.LogFutureTask;
import com.systex.sop.cvs.task.TaskResult;
import com.systex.sop.cvs.task.WriteFutureTask;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.util.HostnameUtil;

public class CVSJob implements Job {
	private CommonDAO commonDAO = new CommonDAO();
	
	public static Timestamp getAutoSyncDate() {
		return new Timestamp(System.currentTimeMillis() - 86400000L);	// 自動同步需包含前一日 (避免漏掉前日尾段)
	}
	
	public void execute(final Timestamp date, final boolean isFullSync) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				TaskResult.clearResult();
				
				try {
					if (isFullSync) {
						StartUI.getInstance().getFrame().getMsgjL().setText("清空所有資料中...");
						try {
							commonDAO.executeSQL("truncate table tbsoptcvstag");
							commonDAO.executeSQL("truncate table tbsoptcvsver");
							commonDAO.executeSQL("truncate table tbsoptcvsmap");
							StartUI.getInstance().getFrame().getMsgjL().setText("所有資料已清完成");
						}catch(Exception e) {
							StartUI.getInstance().getFrame().getMsgjL().setText("清空所有資料失敗");
							CVSLog.getLogger().error(this, e);
							return;
						}
					}
					LogFutureTask logFuture = new LogFutureTask();
					StartUI.getInstance().getFrame().getMsgjL().setText("同步紀錄檔中...");
					if (logFuture.execute(date)) {
						StartUI.getInstance().getFrame().getMsgjL().setText("同步紀錄檔完成");
						WriteFutureTask writeFuture = new WriteFutureTask();
						StartUI.getInstance().getFrame().getMsgjL().setText("寫入紀錄檔中...");
						if (writeFuture.execute(HostnameUtil.getHostname(), date, isFullSync)) {
							StartUI.getInstance().getFrame().getMsgjL().setText("寫入紀錄檔完成 (同步完成)");
						}else{
							StartUI.getInstance().getFrame().getMsgjL().setText("寫入紀錄檔失敗");
						}
					}else{
						StartUI.getInstance().getFrame().getMsgjL().setText("同步紀錄檔失敗");
						StartUI.getInstance().getFrame().getMsgjL().setText("發生異常...");
					}
				}catch(Exception e){
					CVSLog.getLogger().error(this, e);
				}
			}
		}).start();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		execute(getAutoSyncDate(), false);
	}

}
