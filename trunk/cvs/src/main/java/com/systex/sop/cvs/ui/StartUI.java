package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.comp.SSSFrame;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.customize.other.ObservingTextField;
import com.systex.sop.cvs.ui.customize.other.SSSDatePicker;
import com.systex.sop.cvs.ui.logic.StartUILogic;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.ScreenSize;
import java.awt.Insets;

public class StartUI {
	private static StartUI instance;

	public static StartUI getInstance() {
		if (instance == null) {
			instance = new StartUI();
		}
		return instance;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartUI window = StartUI.getInstance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private SSSJTextField autoCron_jTxtF;
	private SSSJTextField autoDate_jTxtF;
	private SSSJButton autoExec_jBtn;
	private SSSFrame frame;
	private ObservingTextField manualDate_jTxtF;
	private Dimension size = new Dimension(1024, 768);
	private JTable table;
	private SSSJButton manualExec_jBtn;
	private JCheckBox fullSync_jChkB;

	/**
	 * Create the application.
	 */
	private StartUI() {
		initialize();
		new StartUILogic(this);
	}

	public SSSJButton getAutoExec_jBtn() {
		return autoExec_jBtn;
	}

	public SSSJTextField getCron_jTxtF() {
		return autoCron_jTxtF;
	}

	public SSSJTextField getAutoDate_jTxtF() {
		return autoDate_jTxtF;
	}

	public SSSFrame getFrame() {
		return frame;
	}

	public JTable getTable() {
		return table;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		Icon titleIcon = new ImageIcon(getClass().getResource("/resource/symbol.png"));
		Image frameIcon = null;
		try {
			frameIcon = ImageIO.read(getClass().getResource("/resource/symbolLarge.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame = new SSSFrame(frameIcon, titleIcon, PropReader.getProperty("CVS.TITLE"), true, true, true);
		frame.setBounds(0, ScreenSize.getAdjustScreenHeight(size.height), 800, 538);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		UIManager.put("SplitPane.dividerSize", 3);
		SSSJSplitPane splitPane = new SSSJSplitPane();
		splitPane.setDividerLocation(150);
		splitPane.setOneTouchExpandable(false);
		frame.getPanel().add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		splitPane.setLeftComponent(scrollPane);

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.WHITE);
		scrollPane.setViewportView(menuPanel);
		menuPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				RowSpec.decode("25dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

		SSSJButton button = new SSSJButton(SSSJButton.SUBJECT);
		button.setText("伺服端功能");
		menuPanel.add(button, "1, 1, default, fill");

		SSSJButton button_1 = new SSSJButton(SSSJButton.TITLE);
		button_1.setText("同步功能");
		menuPanel.add(button_1, "1, 3");

		SSSJSplitPane splitPane_2 = new SSSJSplitPane();
		splitPane_2.setBackground(new Color(127, 125, 123));
		splitPane_2.setDividerLocation(150);
		splitPane_2.setOneTouchExpandable(false);
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_2);

		SSSJTabbedPane badJTabbedPane = new SSSJTabbedPane();
		splitPane_2.setLeftComponent(badJTabbedPane);

		JPanel AutoPanel = new JPanel();
		AutoPanel.setBackground(Color.WHITE);
		badJTabbedPane.addTab("自動同步", null, AutoPanel, null);
		AutoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		SSSJLabel label = new SSSJLabel("日期");
		AutoPanel.add(label, "2, 2, right, default");

		autoDate_jTxtF = new ObservingTextField();
		autoDate_jTxtF.setEditable(false);
		AutoPanel.add(autoDate_jTxtF, "4, 2, fill, default");
		autoDate_jTxtF.setColumns(10);

		SSSJLabel label_1 = new SSSJLabel("排程");
		AutoPanel.add(label_1, "2, 4, right, default");

		autoCron_jTxtF = new SSSJTextField();
		autoCron_jTxtF.setEditable(false);
		AutoPanel.add(autoCron_jTxtF, "4, 4, fill, default");
		autoCron_jTxtF.setColumns(10);
		
				autoExec_jBtn = new SSSJButton(SSSJButton.ITEM_LITE);
				autoExec_jBtn.setText("啟動");
				AutoPanel.add(autoExec_jBtn, "8, 4");

		JPanel ManualPanel = new JPanel();
		ManualPanel.setBackground(Color.WHITE);
		badJTabbedPane.addTab("手動同步", null, ManualPanel, null);
		ManualPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		SSSJLabel label_2 = new SSSJLabel("日期");
		ManualPanel.add(label_2, "2, 2, right, default");

		manualDate_jTxtF = new ObservingTextField();
		manualDate_jTxtF.setEditable(false);
		ManualPanel.add(manualDate_jTxtF, "4, 2, fill, default");
		manualDate_jTxtF.setColumns(10);
				
						JButton badJButton = new JButton();
						badJButton.setMargin(new Insets(0, 0, 0, 0));
						badJButton.setBorder(null);
						badJButton.setBackground(Color.WHITE);
						badJButton.setIcon(new ImageIcon(StartUI.class.getResource("/resource/search-calendar.png")));
						badJButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								SSSDatePicker dp = new SSSDatePicker(manualDate_jTxtF, Locale.TAIWAN);
								Date selectedDate = dp.parseDate(autoDate_jTxtF.getText());
								dp.setSelectedDate(selectedDate);
								dp.start(autoDate_jTxtF);
							}
						});
						ManualPanel.add(badJButton, "6, 2");
				
				SSSJLabel label_3 = new SSSJLabel();
				label_3.setText("範圍");
				ManualPanel.add(label_3, "2, 4, right, default");
		
				fullSync_jChkB = new JCheckBox("完全同步");
				ManualPanel.add(fullSync_jChkB, "4, 4");
		
				manualExec_jBtn = new SSSJButton(SSSJButton.ITEM_LITE);
				manualExec_jBtn.setText("執行");
				ManualPanel.add(manualExec_jBtn, "8, 4");

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_2.setRightComponent(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		Hashtable srvTitle1 = new Hashtable();
		srvTitle1.put("同步", "");

		Hashtable qryTitle1 = new Hashtable();
		qryTitle1.put("一般查詢", "");
		qryTitle1.put("典型查詢", new String[] { "查詢TAG未正確", "查詢TAG版號未在最新版" });
	}
	public SSSJButton getManualExec_jBtn() {
		return manualExec_jBtn;
	}
	public ObservingTextField getManualDate_jTxtF() {
		return manualDate_jTxtF;
	}
	public JCheckBox getFullSync_jChkB() {
		return fullSync_jChkB;
	}
}
