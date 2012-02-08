package com.systex.sop.cvs.ui.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Timestamp;

import org.quartz.SchedulerException;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.schedular.CVSJob;
import com.systex.sop.cvs.schedular.CVSSchedularManager;
import com.systex.sop.cvs.ui.StartUI;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

public class StartUILogic {
	private StartUI ui;
	ServerSocket socket;

	public StartUILogic(StartUI ui) {
		this.ui = ui;
		checkSingleApp();
		initial();
	}
	
	private void checkSingleApp() {
		try {
			socket = new ServerSocket(PropReader.getPropertyInt("CVS.PORT"));
		} catch (IOException e) {
			CVSLog.getLogger().warn("系統重覆執行");
			System.exit(0);
		}
	}
	
	/** 初始化頁「自動同步」 **/
	private void initPageAutoSync() {
		ui.getCron_jTxtF().setText(PropReader.getProperty("CVS.CRONTAB"));
		ui.getAutoDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate()));
		ui.getAutoExec_jBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doAutoSyncExecute();
				} catch (SchedulerException e1) {
					CVSLog.getLogger().error(this, e1);
				}
			}
		});
	}
	
	/** 初始化頁「手動同步」 **/
	private void initPageManualSync() {
		ui.getManualDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate()));
		ui.getManualExec_jBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doManualSyncExecute();
			}
		});
		ui.getFullSync_jChkB().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ui.getFullSync_jChkB().isSelected()) {
					ui.getManualDate_jTxtF().setText("2000/01/01");
				}else{
					ui.getManualDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate()));
				}
			}
		});
	}

	private void initial() {
		initPageAutoSync();
		initPageManualSync();
	}
	
	/** 執行「自動同步」(啟動 / 停止) **/
	private void doAutoSyncExecute() throws SchedulerException {
		if (CVSSchedularManager.getInstance().isStandby()) {
			CVSSchedularManager.getInstance().start();
			ui.getAutoExec_jBtn().setText("停止");
			// 更新標題為同步中
			ui.getFrame().getFrameTitleBar().getTitle().setText(PropReader.getProperty("CVS.TITLE") + "  [自動同步中]");
		}else
		if (CVSSchedularManager.getInstance().isStarted()) {
			CVSSchedularManager.getInstance().suspend();
			ui.getAutoExec_jBtn().setText("啟動");
			// 標題更新回來
			ui.getFrame().getFrameTitleBar().getTitle().setText(PropReader.getProperty("CVS.TITLE"));
		}
	}
	
	/** 執行「手動同步」 **/
	private void doManualSyncExecute() {
		CVSJob job = new CVSJob();
		Timestamp date = null;
		try {
			date = TimestampHelper.convertToTimestamp2(ui.getManualDate_jTxtF().getText());
		}catch(Exception e){
			System.err.println("輸入的日期無效");
			return;
		}
		
		job.execute(date, ui.getFullSync_jChkB().isSelected());
	}
	
}
