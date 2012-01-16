package com.badrobot.ui.containers;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class BadTrafficLabel extends JLabel {
	public enum LIGHT {RED, GREEN , YELLOW};

	public BadTrafficLabel(LIGHT light) {
		super();
		this.setUI(new BadTrafficLabelUI(this, light));
	}

}
