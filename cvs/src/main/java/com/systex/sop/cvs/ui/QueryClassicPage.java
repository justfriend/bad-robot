package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
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
import com.systex.sop.cvs.constant.CVSConst.PROG_TYPE;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
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
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.TimestampHelper;

/**
 * 典型查詢頁面
 * <P>
 *
 */
@SuppressWarnings("serial")
public class QueryClassicPage extends JPanel {
	// 控制變數
	
	// 幫助項目
	private QueryPageLogic logic = new QueryPageLogic();
	
	// 元件項目
	private SSSJTable table;
	private SSSJTextField author_jTxtF;
	private JCheckBox ignoreDel_jChkB;
	private SSSJTable table_1;
	private SSSJTextField author_1_jTxtF;
	private ObservingTextField beginDate_1_jTxtF;
	private JCheckBox ignoreDel_1_jChkB;
	private SSSJComboBox type_jCmbB;
	private SSSJComboBox type_1_jCmbB;
	
	/** Constructor **/
	public QueryClassicPage() {
		initial();
		initUI();
	}
	
	public void initUI() {
		setBackground(new Color(127, 125, 123));
		logic.registerPopupMenu(getTable());
		logic.registerPopupMenu(getTable_1());
		getBeginDate_1_jTxtF().setText(PropReader.getProperty("CVS.COMMENT_ERR_BEGINDATE"));
		getAuthor_jTxtF().setText(PropReader.getProperty("CVS.DEFAULT_AUTHOR"));
		getAuthor_1_jTxtF().setText(PropReader.getProperty("CVS.DEFAULT_AUTHOR"));
	}
	
	private void initial() {
		setLayout(new BorderLayout(0, 0));
		
		SSSJTabbedPane tabbedPane = new SSSJTabbedPane();
		tabbedPane.setBorder(null);
		add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("最新版本未下TAG", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		SSSJSplitPane splitPane = new SSSJSplitPane();
		splitPane.setBorder(null);
		splitPane.setBackground(new Color(127, 125, 123));
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(75);
		panel.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_4);
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("18dlu"),
				RowSpec.decode("18dlu"),}));
		
		SSSJLabel label = new SSSJLabel();
		label.setText("作者");
		panel_4.add(label, "2, 2, right, default");
		
		author_jTxtF = new SSSJTextField();
		panel_4.add(author_jTxtF, "4, 2, fill, default");
		
		SSSJLabel label_1 = new SSSJLabel();
		label_1.setText("忽略");
		panel_4.add(label_1, "2, 3, right, default");
		
		ignoreDel_jChkB = new JCheckBox("忽略已刪除");
		ignoreDel_jChkB.setSelected(true);
		ignoreDel_jChkB.setBackground(Color.PINK);
		panel_4.add(ignoreDel_jChkB, "4, 3");
		
		SSSJButton qry_jBtn = new SSSJButton();
		qry_jBtn.setBackground(Color.WHITE);
		
		// XXX 查詢「最新版本未下TAG」
		qry_jBtn.addActionListener(new QueryActionListener(qry_jBtn) {
			@Override
			public void actPerformed(ActionEvent event) {
				PROG_TYPE type = (PROG_TYPE) getType_jCmbB().getSelectedItem();
				logic.doQueryNewVerNoTag(getAuthor_jTxtF().getText(), getIgnoreDel_jChkB().isSelected(), type, getTable());
			}
		});
		
		SSSJLabel label_5 = new SSSJLabel();
		label_5.setText("分類");
		panel_4.add(label_5, "6, 3, right, default");
		
		type_jCmbB = new SSSJComboBox();
		type_jCmbB.setModel(new DefaultComboBoxModel(PROG_TYPE.values()));
		panel_4.add(type_jCmbB, "8, 3, fill, default");
		qry_jBtn.setText("查詢");
		panel_4.add(qry_jBtn, "10, 3, default, center");
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		table = new SSSJTable(new VerMapDO());
		
		// XXX 最新版本未下TAG POPUP CLIECK
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
		tabbedPane.addTab("提交註記錯誤或遺漏", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		SSSJSplitPane splitPane_1 = new SSSJSplitPane();
		splitPane_1.setBorder(null);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setDividerLocation(75);
		splitPane_1.setBackground(new Color(127, 125, 123));
		panel_1.add(splitPane_1, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		splitPane_1.setLeftComponent(panel_5);
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("25dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("50dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("18dlu"),
				RowSpec.decode("18dlu"),}));
		
		SSSJLabel label_3 = new SSSJLabel();
		label_3.setText("作者");
		panel_5.add(label_3, "2, 2, right, default");
		
		author_1_jTxtF = new SSSJTextField();
		panel_5.add(author_1_jTxtF, "4, 2, fill, default");
		
		SSSJLabel label_4 = new SSSJLabel();
		label_4.setText("起算日");
		panel_5.add(label_4, "6, 2, right, default");
		
		beginDate_1_jTxtF = new ObservingTextField();
		panel_5.add(beginDate_1_jTxtF, "8, 2, fill, default");
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SSSDatePicker dp = new SSSDatePicker(beginDate_1_jTxtF, Locale.TAIWAN);
				Date selectedDate = dp.parseDate(beginDate_1_jTxtF.getText());
				dp.setSelectedDate(selectedDate);
				dp.start(beginDate_1_jTxtF);
			}
		});
		btnNewButton.setIcon(new ImageIcon(QueryClassicPage.class.getResource("/resource/search-calendar.png")));
		btnNewButton.setBorder(null);
		btnNewButton.setBackground(Color.WHITE);
		panel_5.add(btnNewButton, "10, 2, left, default");
		
		SSSJLabel label_2 = new SSSJLabel();
		label_2.setText("忽略");
		panel_5.add(label_2, "2, 3, right, default");
		
		ignoreDel_1_jChkB = new JCheckBox("忽略已刪除");
		ignoreDel_1_jChkB.setBackground(Color.PINK);
		ignoreDel_1_jChkB.setSelected(true);
		panel_5.add(ignoreDel_1_jChkB, "4, 3");
		
		SSSJButton qry_1_jBtn = new SSSJButton();
		qry_1_jBtn.setBackground(Color.WHITE);
		
		// XXX 查詢「提交註記錯誤或遺漏」
		qry_1_jBtn.addActionListener(new QueryActionListener(qry_1_jBtn) {
			@Override
			public void actPerformed(ActionEvent event) {
				PROG_TYPE type = (PROG_TYPE) getType_1_jCmbB().getSelectedItem();
				logic.doQueryCommentMiss(
						getAuthor_1_jTxtF().getText(),
						getIgnoreDel_1_jChkB().isSelected(),
						TimestampHelper.convertToTimestamp2(getBeginDate_1_jTxtF().getText()),
						getTable_1(), type );
			}
		});
		
		SSSJLabel label_6 = new SSSJLabel();
		label_6.setText("分類");
		panel_5.add(label_6, "6, 3, right, default");
		
		type_1_jCmbB = new SSSJComboBox();
		type_1_jCmbB.setModel(new DefaultComboBoxModel(PROG_TYPE.values()));
		panel_5.add(type_1_jCmbB, "8, 3, fill, default");
		qry_1_jBtn.setText("查詢");
		panel_5.add(qry_1_jBtn, "10, 3, default, center");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_1);
		
		table_1 = new SSSJTable(new VerMapDO());
		// XXX 提交註記錯誤或遺漏 POPUP CLIECK
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					Long rcsid = (Long) getTable_1().getSelectValueAt("RCSID");
					String ver = (String) getTable_1().getSelectValueAt("版號");
					String filename = (String) getTable_1().getSelectValueAt("檔案名稱");
					logic.doRetrieveTagInfo(rcsid, ver, filename);
				}
			}
		});
		scrollPane_1.setViewportView(table_1);
	}

	public SSSJTable getTable() {
		return table;
	}

	public SSSJTextField getAuthor_jTxtF() {
		return author_jTxtF;
	}

	public JCheckBox getIgnoreDel_jChkB() {
		return ignoreDel_jChkB;
	}

	public SSSJTable getTable_1() {
		return table_1;
	}

	public SSSJTextField getAuthor_1_jTxtF() {
		return author_1_jTxtF;
	}

	public SSSJTextField getBeginDate_1_jTxtF() {
		return beginDate_1_jTxtF;
	}

	public JCheckBox getIgnoreDel_1_jChkB() {
		return ignoreDel_1_jChkB;
	}
	public SSSJComboBox getType_jCmbB() {
		return type_jCmbB;
	}
	public SSSJComboBox getType_1_jCmbB() {
		return type_1_jCmbB;
	}
}
