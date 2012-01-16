package com.badrobot.ui.containers;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class BadJButton extends JButton {

	public BadJButton() {
		super();
		this.setUI(new BadJButtonUI(this));
	}

}
