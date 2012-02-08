package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JLabel;

import com.systex.sop.cvs.ui.customize.ui.SSSBasicLabelUI;

@SuppressWarnings("serial")
public class SSSJLabel extends JLabel {

	public SSSJLabel() {
		super();
		setUI(new SSSBasicLabelUI());
	}
	
	public SSSJLabel(String text) {
		super(text);
		setUI(new SSSBasicLabelUI());
	}
}
