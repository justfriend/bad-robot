package com.systex.sop.cvs.ui.logic;

import java.sql.Timestamp;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.schedular.CVSJob;
import com.systex.sop.cvs.schedular.CVSSchedularManager;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.ui.SyncPage;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * 同步功能頁面
 * <P>
 * 1. 僅含動作之處理 <BR>
 *
 */
public class SyncPageLogic {
	private SyncPage page;
	private CVSJob job = null;
	
	public SyncPageLogic(SyncPage page) {
		this.page = page;
	}
	
	/** 切換「完整同步」按鈕 **/
	public void doSwitchFullSync() {
		if (page.getFullSync_jChkB().isSelected()) {
			page.getManualDate_jTxtF().setText("2000/01/01");
		} else {
			page.getManualDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate()));
		}
	}
	
	/** 執行「自動同步」(啟動 / 停止) **/
	public void doAutoSyncExecute() {
		try {
			if (CVSSchedularManager.getInstance().isStandby()) {
				CVSSchedularManager.getInstance().start();
				page.getAutoExec_jBtn().setText("停止");
				// 更新標題為同步中
				StartUI.getInstance().getFrame().getFrameTitleBar().getTitle().setText(PropReader.getProperty("CVS.TITLE") + "  [自動同步中]");
			}else
			if (CVSSchedularManager.getInstance().isStarted()) {
				CVSSchedularManager.getInstance().suspend();
				page.getAutoExec_jBtn().setText("啟動");
				// 標題更新回來
				StartUI.getInstance().getFrame().getFrameTitleBar().getTitle().setText(PropReader.getProperty("CVS.TITLE"));
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}
	}
	
	/** 執行「手動同步」 **/
	public void doManualSyncExecute(boolean isSyncLog, boolean isSyncWrite) {
		if (job == null) job = new CVSJob();
		Timestamp date = null;
		try {
			date = TimestampHelper.convertToTimestamp2(page.getManualDate_jTxtF().getText());
		}catch(Exception e){
			System.err.println("輸入的日期無效");
			return;
		}
		
		job.execute(date, page.getFullSync_jChkB().isSelected(), isSyncLog, isSyncWrite, true);
	}
	
	/** 執行「手動同步之中斷」 **/
	public void doInterrupt() {
		if (job != null) job.shutdownNow();
	}
}
