package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.systex.sop.cvs.ui.customize.comp.SSSJDialogBase;
import com.systex.sop.cvs.ui.customize.comp.SSSJTable;
import com.systex.sop.cvs.ui.customize.other.PWindowDragger;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TableUtil;

@SuppressWarnings("serial")
public class TAGDialog extends SSSJDialogBase {
	private SSSJTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TAGDialog dialog = new TAGDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public TAGDialog() {
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
		
		// Override MouseListener
		new PWindowDragger(this, getTitle_jL()) {
			@Override
			protected MouseListener createMouseListener() {
				return new MouseAdapter() {
					/** 滑鼠放開時記憶座標 **/
					public void mouseReleased(MouseEvent e) {
						PropReader.setProperty("CVS.TAG_X", Integer.toString(TAGDialog.this.getX()));	// 記憶座標X
						PropReader.setProperty("CVS.TAG_Y", Integer.toString(TAGDialog.this.getY()));	// 記憶座標Y
					}

					/** 照抄 **/
					@Override
					public void mousePressed(MouseEvent e) {
						Point clickPoint = new Point(e.getPoint());
						SwingUtilities.convertPointToScreen(clickPoint, fComponent);

						dX = clickPoint.x - fWindow.getX();
						dY = clickPoint.y - fWindow.getY();
					}
				};
			}
		};
	}
	
	public void setTitle(Long id, String version, String filename) {
		filename = (filename.length() > 22)? filename.substring(0, 20) + "..": filename;
		setTitle(StringUtil.concat("[", version, "] ", filename));
	}
	public SSSJTable getTable() {
		return table;
	}
}
