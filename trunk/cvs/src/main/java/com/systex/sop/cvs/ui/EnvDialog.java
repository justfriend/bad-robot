package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.systex.sop.cvs.ui.customize.comp.SSSImgJPanel;
import com.systex.sop.cvs.ui.customize.comp.SSSJDialogBase;
import com.systex.sop.cvs.ui.customize.comp.SSSJSplitPane;
import com.systex.sop.cvs.ui.customize.ui.SSSSplitPaneUI;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JButton;
import com.systex.sop.cvs.ui.customize.comp.SSSJLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSJButton;
import javax.swing.border.MatteBorder;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel.LIGHT;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;
import com.systex.sop.cvs.ui.customize.comp.SSSStatusButton;

public class EnvDialog extends SSSJDialogBase {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EnvDialog dialog = new EnvDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EnvDialog() {
		super();
		setBounds(0, 0, 600, 400);
		getMainPane().setLayout(new BorderLayout(0, 0));
		
		SSSImgJPanel imgJPanel = new SSSImgJPanel();
		getMainPane().add(imgJPanel, BorderLayout.CENTER);
		imgJPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(126dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(51dlu;default)"),}));
		
		SSSStatusButton statusButton = new SSSStatusButton();
		statusButton.setHorizontalTextPosition(AbstractButton.CENTER);
		statusButton.setText("名偵探柯南");
		imgJPanel.add(statusButton, "4, 4, fill, default");
	}

}
