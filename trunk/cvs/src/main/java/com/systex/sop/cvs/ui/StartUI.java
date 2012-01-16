package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.badrobot.ui.containers.BadJButton;
import com.badrobot.ui.containers.BadJStatusPanel;
import com.badrobot.ui.containers.BadJStatusText;
import com.badrobot.ui.containers.BadTrafficLabel;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.badrobot.ui.containers.BadJTabbedPane;
import javax.swing.JTabbedPane;

public class StartUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartUI window = new StartUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public StartUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 520);
		frame.setBackground(new Color(92, 153, 45));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		BadJStatusPanel panel_1 = new BadJStatusPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		BadTrafficLabel bdjlblT = new BadTrafficLabel(BadTrafficLabel.LIGHT.YELLOW);
		bdjlblT.setText("T");
		panel_1.add(bdjlblT, "2, 2");
		
		BadJTabbedPane badJTabbedPane = new BadJTabbedPane();
		badJTabbedPane.setTabPlacement(JTabbedPane.LEFT);
		frame.getContentPane().add(badJTabbedPane, BorderLayout.CENTER);
		
		JPanel panel_execute = new JPanel();
		badJTabbedPane.addTab("Execute", null, panel_execute, null);
		
		JPanel panel_cvs = new JPanel();
		badJTabbedPane.addTab("CVS environment", null, panel_cvs, null);
		
		JPanel panel_path = new JPanel();
		badJTabbedPane.addTab("PATH environment", null, panel_path, null);
		
		JPanel panel_cron = new JPanel();
		badJTabbedPane.addTab("CRON settings", null, panel_cron, null);
	}

}
