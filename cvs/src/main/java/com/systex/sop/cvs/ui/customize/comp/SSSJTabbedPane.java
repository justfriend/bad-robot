package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JTabbedPane;

import com.systex.sop.cvs.ui.customize.ui.SSSTabbedPaneUI;

@SuppressWarnings("serial")
public class SSSJTabbedPane extends JTabbedPane {
	
	public SSSJTabbedPane() {
		super();
		setUI(new SSSTabbedPaneUI(this));
		setFocusable(false);
	}
	
}
