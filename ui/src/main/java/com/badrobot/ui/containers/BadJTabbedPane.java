package com.badrobot.ui.containers;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class BadJTabbedPane extends JTabbedPane {
	public BadJTabbedPane() {
		super();
		setUI(new BadJTabbedPaneUI(this));
		setFocusable(false);
	}
}
