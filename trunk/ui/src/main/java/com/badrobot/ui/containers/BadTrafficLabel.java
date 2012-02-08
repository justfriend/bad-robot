package com.badrobot.ui.containers;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class BadTrafficLabel extends JLabel {
	public enum LIGHT {
		RED, GREEN, YELLOW
	};

	private BadTrafficLabelUI ui = null;

	public BadTrafficLabel(LIGHT light) {
		super();
		ui = new BadTrafficLabelUI(this, light);
		this.setUI(ui);
	}

	public void setLight(LIGHT light) {
		ui.setLight(light);
	}

}
