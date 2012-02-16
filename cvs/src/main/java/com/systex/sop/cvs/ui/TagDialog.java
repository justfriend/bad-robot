package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.systex.sop.cvs.ui.customize.comp.SSSJDialogBase;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TableUtil;

/**
 * 版本標籤對話框
 * <p>
 *
 */
@SuppressWarnings("serial")
public class TagDialog extends SSSJDialogBase {
	private SSSJTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TagDialog dialog = new TagDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public TagDialog() {
		super();
		
		initial();
		initUI();
	}
	
	private void initial() {
		getMainPane().setBorder(null);
		getMainPane().setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		getMainPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(1, 2, 1, 2));
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		table = new SSSJTable(new TagDO());
		table.setBorder(new LineBorder(Color.WHITE));
		table.setRowHeight(24);
		panel.add(table, BorderLayout.CENTER);
	}

	private void initUI() {
		TableUtil.addRows(table, new ArrayList<TagDO>());	// default empty
		
		// 增加座標記憶功能
		getTitleBar().addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent e) {
						PropReader.setProperty("CVS.TAG_X", Integer.toString(TagDialog.this.getX()));	// 記憶座標X
						PropReader.setProperty("CVS.TAG_Y", Integer.toString(TagDialog.this.getY()));	// 記憶座標Y
					}
		});
	}
	
	public void setTitle(Long id, String version, String filename, int maxLengths) {
		filename = (filename.length() > maxLengths)? filename.substring(0, maxLengths - 2) + "..": filename;
		setTitle(StringUtil.concat("[", version, "] ", filename));
	}
	
	public SSSJTable getTable() {
		return table;
	}
}
