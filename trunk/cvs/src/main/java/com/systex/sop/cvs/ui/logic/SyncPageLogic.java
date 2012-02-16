package com.systex.sop.cvs.ui.logic;

import java.sql.Timestamp;

import javax.swing.JButton;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.schedular.CVSJob;
import com.systex.sop.cvs.schedular.CVSSchedularManager;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * 同步之邏輯處理及資料查詢
 * <P>
 *
 */
public class SyncPageLogic {
	private static CVSJob job = new CVSJob();
	
	/** 執行「自動同步」(啟動 / 停止) **/
	public void doAutoSyncExecute(JButton btn) {
		try {
			if (CVSSchedularManager.getInstance().isStandby()) {
				CVSSchedularManager.getInstance().start();
				btn.setText("停止");
				// 更新標題為同步中
				StartUI.getInstance().getFrame().getFrameTitleBar().getTitle().setText(
						StartUI.getInstance().getDefTitle() + "  [自動同步中]");
			}else
			if (CVSSchedularManager.getInstance().isStarted()) {
				CVSSchedularManager.getInstance().suspend();
				btn.setText("啟動");
				// 標題更新回來
				StartUI.getInstance().getFrame().getFrameTitleBar().getTitle().setText(
						StartUI.getInstance().getDefTitle() );
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}
	}
	
	/** 執行「手動同步之中斷」 **/
	public void doInterrupt() {
		if (job != null) {
			try {
				job.shutdownNow();
				StartUI.getInstance().getFrame().showMessageBox("中斷成功");
			}catch(Exception e){
				StartUI.getInstance().getFrame().showMessageBox("中斷失敗，請重新啟動程式");
			}
		}
	}
	
	/** 執行「手動同步」 **/
	public void doManualSyncExecute(boolean isSyncLog, boolean isSyncWrite, String dateStr, boolean isFullSync) {
		Timestamp date = null;
		try {
			date = TimestampHelper.convertToTimestamp2(dateStr);
		}catch(Exception e){
			String msg = "輸入的日期無效, 格式需為 (yyyy/MM/dd)";
			StartUI.getInstance().getFrame().showMessageBox(msg);
			return;
		}
		
		job.execute(date, isFullSync, isSyncLog, isSyncWrite);
	}
}
