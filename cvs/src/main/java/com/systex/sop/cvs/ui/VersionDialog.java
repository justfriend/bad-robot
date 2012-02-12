package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.comp.SSSImgJPanel;
import com.systex.sop.cvs.ui.customize.comp.SSSJDialogBase;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.customize.comp.SSSJTextField;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.PropReader;

@SuppressWarnings("serial")
public class VersionDialog extends SSSJDialogBase {
	private SSSJTable table;
	private SSSJTextField filename_jTxtF;
	
	/** Constructor **/
	public VersionDialog() {
		super();
		initial();
		initUI();
	}
	
	private void initUI() {
		setBounds(100, 100, 600, 600);
		setTitle("CVS Version Tree");
		// 增加座標記憶功能
		getTitleBar().addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						PropReader.setProperty("CVS.VERTREE_X", Integer.toString(VersionDialog.this.getX()));	// 記憶座標X
						PropReader.setProperty("CVS.VERTREE_Y", Integer.toString(VersionDialog.this.getY()));	// 記憶座標Y
					}
		});
	}
	
	private void initial() {
		getMainPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		getMainPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("30dlu"),
				RowSpec.decode("default:grow"),}));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, "1, 1, fill, fill");
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("200dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("40dlu"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		SSSJLabel label = new SSSJLabel();
		label.setText("檔案名稱");
		panel_1.add(label, "2, 1, right, default");
		
		filename_jTxtF = new SSSJTextField();
		filename_jTxtF.setEditable(false);
		filename_jTxtF.setForeground(Color.DARK_GRAY);
		filename_jTxtF.setBackground(new Color(207, 199, 188));
		filename_jTxtF.setBorder(new LineBorder(SystemColor.activeCaptionBorder));
		panel_1.add(filename_jTxtF, "4, 1, fill, default");
		
		SSSImgJPanel imgJPanel = new SSSImgJPanel();
		imgJPanel.setBorder(new EmptyBorder(4, 24, 0, 0));
		panel.add(imgJPanel, "1, 2, fill, fill");
		imgJPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		imgJPanel.add(scrollPane_1, BorderLayout.CENTER);
		
		table = new SSSJTable(new VerTreeDO());
		table.setBorder(null);
		scrollPane_1.setViewportView(table);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			VersionDialog dialog = new VersionDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SSSJTable getTable() {
		return table;
	}
	public SSSJTextField getFilename_jTxtF() {
		return filename_jTxtF;
	}
}
