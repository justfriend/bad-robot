package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.logic.QueryClassicPageLogic;
import com.systex.sop.cvs.ui.tableClass.CVSColumnModel;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;

@SuppressWarnings("serial")
public class QueryClassicPage extends JPanel {
	private QueryClassicPageLogic logic = new QueryClassicPageLogic(this);
	private SSSJTable table;
	private SSSJTextField author_jTxtF;
	private JCheckBox ignoreDel_jChkB;
	
	private void initial() {
		setBackground(new Color(127, 125, 123));
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
		splitPane.setDividerLocation(95);
		panel.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_4);
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("20dlu"),}));
		
		SSSJLabel label = new SSSJLabel();
		label.setText("作者");
		panel_4.add(label, "2, 2, right, default");
		
		author_jTxtF = new SSSJTextField();
		panel_4.add(author_jTxtF, "4, 2, fill, default");
		
		SSSJLabel label_1 = new SSSJLabel();
		label_1.setText("忽略");
		panel_4.add(label_1, "2, 4, right, default");
		
		ignoreDel_jChkB = new JCheckBox("忽略已刪除");
		ignoreDel_jChkB.setSelected(true);
		ignoreDel_jChkB.setBackground(Color.PINK);
		panel_4.add(ignoreDel_jChkB, "4, 4");
		
		SSSJButton qry_jBtn = new SSSJButton();
		qry_jBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 查詢最新版本未上TAG
				logic.doQueryNewVerNoTag(getAuthor_jTxtF().getText(), getIgnoreDel_jChkB().isSelected());
			}
		});
		qry_jBtn.setText("查詢");
		panel_4.add(qry_jBtn, "8, 4");
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		table = new SSSJTable();
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("提交註記錯誤或遺漏", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		SSSJSplitPane splitPane_1 = new SSSJSplitPane();
		splitPane_1.setBorder(null);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setDividerLocation(130);
		panel_1.add(splitPane_1, BorderLayout.CENTER);
		
		JPanel panel_5 = new JPanel();
		splitPane_1.setLeftComponent(panel_5);
		panel_5.setLayout(new FormLayout(new ColumnSpec[] {},
			new RowSpec[] {}));
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("概要統計", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		SSSJSplitPane splitPane_3 = new SSSJSplitPane();
		splitPane_3.setBorder(null);
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_3.setDividerLocation(130);
		panel_3.add(splitPane_3, BorderLayout.CENTER);
		
		JPanel panel_7 = new JPanel();
		splitPane_3.setLeftComponent(panel_7);
		panel_7.setLayout(new FormLayout(new ColumnSpec[] {},
			new RowSpec[] {}));
	}
	
	public void initUI() {
		getTable().setColumnModel(new CVSColumnModel(new NewVerNoTagDO()));
	}

	public JTable getTable() {
		return table;
	}

	/**
	 * Create the panel.
	 */
	public QueryClassicPage() {
		initial();
		initUI();
	}

	public SSSJTextField getAuthor_jTxtF() {
		return author_jTxtF;
	}
	public JCheckBox getIgnoreDel_jChkB() {
		return ignoreDel_jChkB;
	}
}
