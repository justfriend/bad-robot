package com.systex.sop.cvs.ui.customize.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.ibm.iwt.window.IWindowButton;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.other.PWindowDragger;

@SuppressWarnings("serial")
public class SSSJDialogBase extends JDialog {
	private JPanel mainPane;
	private SSSJLabel title_jL;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SSSJDialogBase dialog = new SSSJDialogBase();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public SSSJDialogBase() {
		setBounds(100, 100, 264, 382);
		setUndecorated(true);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(99, 87, 74)));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(207, 199, 188));
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("2dlu"),},
			new RowSpec[] {
				RowSpec.decode("1dlu:grow"),
				RowSpec.decode("23px"),}));
		
		// 視窗關閉功能
		SSSIWindowButton btnX = new SSSIWindowButton(IWindowButton.CLOSE);
		btnX.setBackground(new Color(207, 199, 188));
		btnX.setBorder(null);
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SSSJDialogBase.this.dispose();	// 關閉
			}
		});
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new MatteBorder(0, 0, 1, 0, new Color(230, 226, 219)));
		panel_5.setBackground(new Color(207, 199, 188));
		panel_1.add(panel_5, "1, 1, 7, 1, fill, fill");
		
		title_jL = new SSSJLabel();
		title_jL.setFont(new Font(SSSPalette.fontFamily, Font.BOLD, 14));
		title_jL.setText("TITLE");
		panel_1.add(title_jL, "2, 2, default, center");
		panel_1.add(btnX, "6, 2");
		
		// 允許視窗拖移
		new PWindowDragger(this, panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(207, 199, 188));
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("220px"),
				ColumnSpec.decode("7px"),},
			new RowSpec[] {
				RowSpec.decode("4px"),}));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(207, 199, 188));
		panel.add(panel_3, BorderLayout.WEST);
		panel_3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("4px"),},
			new RowSpec[] {
				RowSpec.decode("23px"),}));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(207, 199, 188));
		panel.add(panel_4, BorderLayout.EAST);
		panel_4.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("4px"),},
			new RowSpec[] {
				RowSpec.decode("23px"),}));
		
		mainPane = new JPanel();
		mainPane.setBorder(new LineBorder(new Color(131, 125, 113)));
		mainPane.setBackground(Color.WHITE);
		panel.add(mainPane, BorderLayout.CENTER);
	}

	public JPanel getMainPane() {
		return mainPane;
	}
	
	public SSSJLabel getTitle_jL() {
		return title_jL;
	}
	
	public void setTitle(String text) {
		getTitle_jL().setText(text);
	}
}
