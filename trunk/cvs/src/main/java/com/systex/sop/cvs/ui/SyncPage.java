package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.schedular.CVSJob;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.customize.other.ObservingTextField;
import com.systex.sop.cvs.ui.customize.other.SSSDatePicker;
import com.systex.sop.cvs.ui.logic.SyncPageLogic;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * 同步功能頁面
 * <P>
 * 1. 僅含畫面UI及元件事件 <BR>
 * 3. 事件之處理委派由--Logic來處理<BR>
 *
 */
@SuppressWarnings("serial")
public class SyncPage extends JPanel {
	private SSSJTextField autoCron_jTxtF;
	private SSSJTextField autoDate_jTxtF;
	private SSSJButton autoExec_jBtn;
	private ObservingTextField manualDate_jTxtF;
	private JTable syncResult_jTbl;
	private JCheckBox fullSync_jChkB;
	private SSSJButton manualExec_jBtn;
	private SyncPageLogic logic = new SyncPageLogic(this);
	
	private void initial() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);

		SSSJSplitPane sync_jSplit = new SSSJSplitPane();
		sync_jSplit.setBackground(new Color(127, 125, 123));
		sync_jSplit.setDividerLocation(130);
		sync_jSplit.setOneTouchExpandable(false);
		sync_jSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		scrollPane.setViewportView(sync_jSplit);
		
		SSSJTabbedPane sync_jTab = new SSSJTabbedPane();
		sync_jSplit.setLeftComponent(sync_jTab);

		JPanel autoSyncTab = new JPanel();
		autoSyncTab.setBackground(Color.WHITE);
		sync_jTab.addTab("自動同步", null, autoSyncTab, null);
		autoSyncTab.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("30dlu"), FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, }));

		SSSJLabel label = new SSSJLabel("日期");
		autoSyncTab.add(label, "2, 2, right, default");

		autoDate_jTxtF = new ObservingTextField();
		autoDate_jTxtF.setEditable(false);
		autoSyncTab.add(autoDate_jTxtF, "4, 2, fill, default");
		autoDate_jTxtF.setColumns(10);

		SSSJLabel label_1 = new SSSJLabel("排程");
		autoSyncTab.add(label_1, "2, 4, right, default");

		autoCron_jTxtF = new SSSJTextField();
		autoCron_jTxtF.setEditable(false);
		autoSyncTab.add(autoCron_jTxtF, "4, 4, fill, default");
		autoCron_jTxtF.setColumns(10);

		autoExec_jBtn = new SSSJButton(SSSJButton.ITEM_LITE);
		autoExec_jBtn.setBackground(Color.WHITE);
		autoExec_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 執行「自動同步」(啟動 / 停止)
				logic.doAutoSyncExecute();
			}
		});
		autoExec_jBtn.setText("啟動");
		autoSyncTab.add(autoExec_jBtn, "8, 4");

		JPanel manualSyncTab = new JPanel();
		manualSyncTab.setBackground(Color.WHITE);
		sync_jTab.addTab("手動同步", null, manualSyncTab, null);
		manualSyncTab.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:30dlu"), FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		SSSJLabel label_2 = new SSSJLabel("日期");
		manualSyncTab.add(label_2, "2, 2, right, default");

		manualDate_jTxtF = new ObservingTextField();
		manualDate_jTxtF.setEditable(false);
		manualSyncTab.add(manualDate_jTxtF, "4, 2, fill, default");
		manualDate_jTxtF.setColumns(10);

		JButton datePicker_jBtn = new JButton();
		datePicker_jBtn.setMargin(new Insets(0, 0, 0, 0));
		datePicker_jBtn.setBorder(null);
		datePicker_jBtn.setBackground(Color.WHITE);
		datePicker_jBtn.setIcon(new ImageIcon(StartUI.class.getResource("/resource/search-calendar.png")));
		datePicker_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SSSDatePicker dp = new SSSDatePicker(manualDate_jTxtF, Locale.TAIWAN);
				Date selectedDate = dp.parseDate(autoDate_jTxtF.getText());
				dp.setSelectedDate(selectedDate);
				dp.start(autoDate_jTxtF);
			}
		});
		manualSyncTab.add(datePicker_jBtn, "6, 2");

		SSSJLabel label_3 = new SSSJLabel();
		label_3.setText("範圍");
		manualSyncTab.add(label_3, "2, 4, right, default");

		fullSync_jChkB = new JCheckBox("完全同步");
		fullSync_jChkB.setBackground(Color.ORANGE);
		fullSync_jChkB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 切換「完整同步」按鈕
				logic.doSwitchFullSync();
			}
		});
		manualSyncTab.add(fullSync_jChkB, "4, 4");

		manualExec_jBtn = new SSSJButton(SSSJButton.ITEM_LITE);
		manualExec_jBtn.setBackground(Color.WHITE);
		manualExec_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 執行「手動同步」
				logic.doManualSyncExecute();
			}
		});
		manualExec_jBtn.setText("執行");
		manualSyncTab.add(manualExec_jBtn, "8, 4");

		JScrollPane syncResult_jScrP = new JScrollPane();
		syncResult_jScrP.setBackground(Color.ORANGE);
		syncResult_jScrP.setBorder(null);
		sync_jSplit.setRightComponent(syncResult_jScrP);

		syncResult_jTbl = new JTable();
		syncResult_jScrP.setViewportView(syncResult_jTbl);
	}
	
	private void initUI() {
		/** 初始化「自動同步」 **/
		getCron_jTxtF().setText(PropReader.getProperty("CVS.CRONTAB"));
		getAutoDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate()));
		
		/** 初始化「手動同步」 **/
		getManualDate_jTxtF().setText(TimestampHelper.convertToyyyyMMdd2(CVSJob.getAutoSyncDate())); 
	}

	/**
	 * Create the panel.
	 */
	public SyncPage() {
		initial();
		initUI();
	}

	public SSSJTextField getAutoDate_jTxtF() {
		return autoDate_jTxtF;
	}

	public SSSJButton getAutoExec_jBtn() {
		return autoExec_jBtn;
	}

	public SSSJTextField getCron_jTxtF() {
		return autoCron_jTxtF;
	}

	public JCheckBox getFullSync_jChkB() {
		return fullSync_jChkB;
	}

	public ObservingTextField getManualDate_jTxtF() {
		return manualDate_jTxtF;
	}

	public SSSJButton getManualExec_jBtn() {
		return manualExec_jBtn;
	}

	public JTable getTable() {
		return syncResult_jTbl;
	}

}
