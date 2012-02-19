package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJCheckBox;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.customize.other.ObservingTextField;
import com.systex.sop.cvs.ui.customize.other.QueryActionListener;
import com.systex.sop.cvs.ui.customize.other.SSSDatePicker;
import com.systex.sop.cvs.ui.logic.QueryPageLogic;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;
import com.systex.sop.cvs.ui.customize.comp.SSSJComboBox;
import javax.swing.DefaultComboBoxModel;
import com.systex.sop.cvs.constant.CVSConst.PROG_TYPE;

/**
 * 一般查詢畫面
 * <p>
 *
 */
@SuppressWarnings("serial")
public class QueryNormalPage extends JPanel {
	// 控制變數
	private Boolean isBeginDatePicker = null;
	
	// 幫助項目
	private QueryPageLogic logic = new QueryPageLogic();
	
	// 元件項目
	private SSSJTextField filename_jTxtF;
	private SSSJTextField author_jTxtF;
	private JCheckBox ignoreDel_jChkB;
	private ObservingTextField beginVerTime_jTxtF;
	private SSSJTextField desc_jTxtF;
	private SSSJTextField id_jTxtF;
	private SSSJTextField program_jTxtF;
	private ObservingTextField endedVerTime_jTxtF;
	private SSSJTable table;
	private SSSJTextField tag_jTxtF;
	private SSSJCheckBox tag_jChkB;
	private SSSJComboBox type_jCmbB;
	private SSSJTextField module_jTxtF;
	
	/** Constructor **/
	public QueryNormalPage() {
		initial();
		initUI();
	}
	
	private void initUI() {
		setBackground(new Color(127, 125, 123));
		tag_jTxtF.setText(PropReader.getProperty("CVS.SOPATAG"));
		logic.registerPopupMenu(getTable());
		getAuthor_jTxtF().setText(PropReader.getProperty("CVS.DEFAULT_AUTHOR"));
	}
	
	private void initial() {
		setLayout(new BorderLayout(0, 0));
		
		SSSJTabbedPane tabbedPane = new SSSJTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("一般查詢", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		SSSJSplitPane splitPane = new SSSJSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBorder(null);
		splitPane.setBackground(new Color(127, 125, 123));
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(100);
		panel.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_2);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("18dlu"),
				RowSpec.decode("18dlu"),
				RowSpec.decode("18dlu"),}));
		
		SSSJLabel label = new SSSJLabel();
		label.setText("作者");
		panel_2.add(label, "2, 2, right, default");
		
		author_jTxtF = new SSSJTextField();
		panel_2.add(author_jTxtF, "4, 2, fill, default");
		
		SSSJLabel lblPccid = new SSSJLabel();
		lblPccid.setText("ID");
		panel_2.add(lblPccid, "6, 2, right, default");
		
		id_jTxtF = new SSSJTextField();
		panel_2.add(id_jTxtF, "8, 2, fill, default");
		
		SSSJLabel lblDesc = new SSSJLabel();
		lblDesc.setText("DESC");
		panel_2.add(lblDesc, "10, 2, right, default");
		
		desc_jTxtF = new SSSJTextField();
		panel_2.add(desc_jTxtF, "12, 2, fill, default");
		
		SSSJLabel label_4 = new SSSJLabel();
		label_4.setText("提交時間");
		panel_2.add(label_4, "14, 2, right, default");
		
		beginVerTime_jTxtF = new ObservingTextField();
		
		// XXX 起始日期取得焦點
		beginVerTime_jTxtF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				isBeginDatePicker = true;
				beginVerTime_jTxtF.setBackground(new Color(184, 207, 229));
				endedVerTime_jTxtF.setBackground(Color.WHITE);
			}
		});
		panel_2.add(beginVerTime_jTxtF, "16, 2, fill, default");
		
		endedVerTime_jTxtF = new ObservingTextField();
		
		// XXX 起始日期取得焦點
		endedVerTime_jTxtF.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				isBeginDatePicker = false;
				endedVerTime_jTxtF.setBackground(new Color(184, 207, 229));
				beginVerTime_jTxtF.setBackground(Color.WHITE);
			}
		});
		
		SSSJLabel label_6 = new SSSJLabel();
		label_6.setText("-");
		panel_2.add(label_6, "17, 2, center, default");
		panel_2.add(endedVerTime_jTxtF, "18, 2, fill, default");
		
		JButton datePicker_jBtn = new JButton("");
		
		// XXX 日期挑選器 (依上次焦點是停在起始或結束日期來決定對象)
		datePicker_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isBeginDatePicker == null) {
					StartUI.getInstance().getFrame().showMessageBox("請點選提交時間之起或迄元件");
				}else 
				if (isBeginDatePicker) {
					SSSDatePicker dp = new SSSDatePicker(beginVerTime_jTxtF, Locale.TAIWAN);
					Date selectedDate = dp.parseDate(beginVerTime_jTxtF.getText());
					dp.setSelectedDate(selectedDate);
					dp.start(beginVerTime_jTxtF);
					isBeginDatePicker = null;
					beginVerTime_jTxtF.setBackground(Color.WHITE);
				}else{
					SSSDatePicker dp = new SSSDatePicker(endedVerTime_jTxtF, Locale.TAIWAN);
					Date selectedDate = dp.parseDate(endedVerTime_jTxtF.getText());
					dp.setSelectedDate(selectedDate);
					dp.start(endedVerTime_jTxtF);
					isBeginDatePicker = null;
					endedVerTime_jTxtF.setBackground(Color.WHITE);
				}
			}
		});
		datePicker_jBtn.setBackground(Color.WHITE);
		datePicker_jBtn.setBorder(null);
		datePicker_jBtn.setIcon(new ImageIcon(QueryNormalPage.class.getResource("/resource/search-calendar.png")));
		panel_2.add(datePicker_jBtn, "20, 2");
		
		SSSJLabel label_1 = new SSSJLabel();
		label_1.setText("忽略");
		panel_2.add(label_1, "2, 3, right, default");
		
		ignoreDel_jChkB = new JCheckBox("忽略已刪除");
		ignoreDel_jChkB.setSelected(true);
		ignoreDel_jChkB.setBackground(Color.PINK);
		panel_2.add(ignoreDel_jChkB, "4, 3");
		
		SSSJLabel label_7 = new SSSJLabel();
		label_7.setText("分類");
		panel_2.add(label_7, "6, 3, right, default");
		
		type_jCmbB = new SSSJComboBox();
		type_jCmbB.setModel(new DefaultComboBoxModel(PROG_TYPE.values()));
		panel_2.add(type_jCmbB, "8, 3, fill, default");
		
		tag_jChkB = new SSSJCheckBox("");
		tag_jChkB.setSelected(true);
		
		// XXX 勾選TAG鎖定
		tag_jChkB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tag_jChkB.isSelected()) {
					tag_jTxtF.setEditable(true);
					tag_jTxtF.setForeground(Color.BLACK);
					tag_jTxtF.setBackground(Color.WHITE);
				}else{
					tag_jTxtF.setEditable(false);
					tag_jTxtF.setForeground(Color.GRAY);
					tag_jTxtF.setBackground(new Color(240, 240, 240));
				}
			}
		});
		
		SSSJLabel label_3 = new SSSJLabel();
		label_3.setText("程式名稱");
		panel_2.add(label_3, "10, 3, right, default");
		
		program_jTxtF = new SSSJTextField();
		panel_2.add(program_jTxtF, "12, 3, fill, default");
		
		SSSJLabel label_5 = new SSSJLabel();
		label_5.setText("鎖定版本");
		panel_2.add(label_5, "14, 3, right, default");
		
		tag_jTxtF = new SSSJTextField();
		tag_jTxtF.setBackground(Color.WHITE);
		panel_2.add(tag_jTxtF, "16, 3, 3, 1, fill, default");
		tag_jChkB.setBorder(null);
		tag_jChkB.setBackground(Color.WHITE);
		panel_2.add(tag_jChkB, "20, 3, left, default");
		
		SSSJButton query_jBtn = new SSSJButton();
		
		// XXX 進行一般查詢
		query_jBtn.addActionListener(new QueryActionListener(query_jBtn) {
			public void actPerformed(ActionEvent e) {
				PROG_TYPE type = (PROG_TYPE) getType_jCmbB().getSelectedItem();
				logic.doQueryNormal(getTable(),
						getAuthor_jTxtF().getText(),
						getIgnoreDel_jChkB().isSelected(),
						TimestampHelper.convertToTimestamp2(getBeginVerTime_jTxtF().getText()),
						TimestampHelper.convertToTimestamp2(getEndedVerTime_jTxtF().getText()),
						getFilename_jTxtF().getText(),
						getProgram_jTxtF().getText(),
						getId_jTxtF().getText(),
						getDesc_jTxtF().getText(),
						getModule_jTxtF().getText(),
						(getTag_jChkB().isSelected())? getTag_jTxtF().getText(): null,
						type);
			}
		});
		
		SSSJLabel label_8 = new SSSJLabel();
		label_8.setText("模組");
		panel_2.add(label_8, "6, 4, right, default");
		
		module_jTxtF = new SSSJTextField();
		panel_2.add(module_jTxtF, "8, 4, fill, default");
		
		SSSJLabel label_2 = new SSSJLabel();
		label_2.setText("檔案名稱");
		panel_2.add(label_2, "10, 4, right, default");
		
		filename_jTxtF = new SSSJTextField();
		panel_2.add(filename_jTxtF, "12, 4, fill, default");
		query_jBtn.setBackground(Color.WHITE);
		query_jBtn.setText("查詢");
		panel_2.add(query_jBtn, "16, 4, 3, 1");
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		table = new SSSJTable(new VerMapDO());
		
		// XXX 一般查詢 POPUP CLIECK
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					Long rcsid = (Long) getTable().getSelectValueAt("RCSID");
					String ver = (String) getTable().getSelectValueAt("版號");
					String filename = (String) getTable().getSelectValueAt("檔案名稱");
					logic.doRetrieveTagInfo(rcsid, ver, filename);
				}
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("關聯查詢", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

	}

	public SSSJTextField getFilename_jTxtF() {
		return filename_jTxtF;
	}

	public SSSJTextField getAuthor_jTxtF() {
		return author_jTxtF;
	}

	public JCheckBox getIgnoreDel_jChkB() {
		return ignoreDel_jChkB;
	}

	public ObservingTextField getBeginVerTime_jTxtF() {
		return beginVerTime_jTxtF;
	}

	public SSSJTextField getDesc_jTxtF() {
		return desc_jTxtF;
	}

	public SSSJTextField getId_jTxtF() {
		return id_jTxtF;
	}

	public SSSJTextField getProgram_jTxtF() {
		return program_jTxtF;
	}

	public ObservingTextField getEndedVerTime_jTxtF() {
		return endedVerTime_jTxtF;
	}

	public SSSJTable getTable() {
		return table;
	}

	public SSSJTextField getTag_jTxtF() {
		return tag_jTxtF;
	}

	public JCheckBox getTag_jChkB() {
		return tag_jChkB;
	}
	public SSSJComboBox getType_jCmbB() {
		return type_jCmbB;
	}
	public SSSJTextField getModule_jTxtF() {
		return module_jTxtF;
	}
}
