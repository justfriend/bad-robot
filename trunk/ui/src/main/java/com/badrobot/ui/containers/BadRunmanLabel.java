package com.badrobot.ui.containers;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class BadRunmanLabel extends JLabel {
	public BadRunmanLabel() {
		super();
		setUI(new BadRunmanLabelUI(this));
	}
}
