package com.badrobot.ui.containers;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class BadJStatusText extends JCheckBox {
	public BadJStatusText() {
		super();
		this.setUI(new BadJStatusTextUI());
	}
}
