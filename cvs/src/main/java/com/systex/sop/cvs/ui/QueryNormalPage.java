package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.constant.CVSConst.PROG_TYPE;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJCheckBox;
import com.systex.sop.cvs.ui.customize.comp.SSSJComboBox;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.customize.other.ObservingTextField;
import com.systex.sop.cvs.ui.customize.other.QueryActionListener;
import com.systex.sop.cvs.ui.customize.other.SSSDatePicker;
import com.systex.sop.cvs.ui.logic.QueryPageLogic;
import com.systex.sop.cvs.ui.tableClass.TagDiffDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

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
	private SSSJTable table2;
	private SSSJTextField startTag_jTxtF;
	private SSSJTextField endTag_jTxtF;
	private JList module_jL;

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
		panel_2.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
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
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("18dlu"), RowSpec.decode("18dlu"),
						RowSpec.decode("18dlu"), }));

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
					StartUI.getInstance().getFrame()
							.showMessageBox("請點選提交時間之起或迄元件");
				} else if (isBeginDatePicker) {
					SSSDatePicker dp = new SSSDatePicker(beginVerTime_jTxtF,
							Locale.TAIWAN);
					Date selectedDate = dp.parseDate(beginVerTime_jTxtF
							.getText());
					dp.setSelectedDate(selectedDate);
					dp.start(beginVerTime_jTxtF);
					isBeginDatePicker = null;
					beginVerTime_jTxtF.setBackground(Color.WHITE);
				} else {
					SSSDatePicker dp = new SSSDatePicker(endedVerTime_jTxtF,
							Locale.TAIWAN);
					Date selectedDate = dp.parseDate(endedVerTime_jTxtF
							.getText());
					dp.setSelectedDate(selectedDate);
					dp.start(endedVerTime_jTxtF);
					isBeginDatePicker = null;
					endedVerTime_jTxtF.setBackground(Color.WHITE);
				}
			}
		});
		datePicker_jBtn.setBackground(Color.WHITE);
		datePicker_jBtn.setBorder(null);
		datePicker_jBtn.setIcon(new ImageIcon(QueryNormalPage.class
				.getResource("/resource/search-calendar.png")));
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
				} else {
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
				logic.doQueryNormal(getTable(), getAuthor_jTxtF().getText(),
						getIgnoreDel_jChkB().isSelected(), TimestampHelper
								.convertToTimestamp2(getBeginVerTime_jTxtF()
										.getText()), TimestampHelper
								.convertToTimestamp2(getEndedVerTime_jTxtF()
										.getText()), getFilename_jTxtF()
								.getText(), getProgram_jTxtF().getText(),
						getId_jTxtF().getText(), getDesc_jTxtF().getText(),
						getModule_jTxtF().getText(), (getTag_jChkB()
								.isSelected()) ? getTag_jTxtF().getText()
								: null, type);
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
					String filename = (String) getTable().getSelectValueAt(
							"檔案名稱");
					logic.doRetrieveTagInfo(rcsid, ver, filename);
				}
			}
		});
		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("關聯查詢", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_1.add(splitPane_1, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		splitPane_1.setLeftComponent(panel_3);
		panel_3.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(80dlu;default):grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(30dlu;default):grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(46dlu;default):grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
						RowSpec.decode("fill:default"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		SSSJLabel lbltag = new SSSJLabel();
		lbltag.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		lbltag.setText("START TAG");
		panel_3.add(lbltag, "2, 3");

		Calendar c = Calendar.getInstance();
				
		startTag_jTxtF = new SSSJTextField();
		this.startTag_jTxtF.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		startTag_jTxtF.setText("SOPA_01_03_"+c.get(Calendar.YEAR));
		panel_3.add(startTag_jTxtF, "4, 3, fill, default");
		
		SSSJLabel lbltag_1 = new SSSJLabel();
		lbltag_1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		lbltag_1.setText("END TAG");
		panel_3.add(lbltag_1, "2, 5");

		endTag_jTxtF = new SSSJTextField();
		this.endTag_jTxtF.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		endTag_jTxtF.setText("SOPA_01_03_"+c.get(Calendar.YEAR));
		panel_3.add(endTag_jTxtF, "4, 5, fill, default");

		SSSJLabel label_9 = new SSSJLabel();
		label_9.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		label_9.setText("排除的模組(可多選)");
		panel_3.add(label_9, "2, 7, right, default");

		module_jL = new JList();
		this.module_jL.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		module_jL.setVisibleRowCount(2);
		module_jL.setFont(new Font("微軟正黑體", Font.BOLD, 14));
		module_jL.setModel(new AbstractListModel() {
			String[] values = new String[] { "dbxml", "util", "qfi", "sbl",
					"bus", "buk", "bas", "cus", "emm", "cmo", "smt", "stk",
					"tmap", "sg", "mstl", "ml", "bnp", "cgmi" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panel_3.add(module_jL, "4, 7, fill, fill");

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_1);

		table2 = new SSSJTable(new TagDiffDO());
		this.table2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));

		scrollPane_1.setViewportView(table2);

		SSSJButton query2_jBtn = new SSSJButton();
		query2_jBtn.setText("查詢");
		panel_3.add(query2_jBtn, "8, 7");

		query2_jBtn.addActionListener(new QueryActionListener(query2_jBtn) {
			public void actPerformed(ActionEvent e) {

				List<String> module = new ArrayList<String>();
				for (Object o : module_jL.getSelectedValues()) {
					module.add(o.toString());
				}

				logic.doQueryTagDiff(table2, startTag_jTxtF.getText(),
						endTag_jTxtF.getText(), module);
			}
		});

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
