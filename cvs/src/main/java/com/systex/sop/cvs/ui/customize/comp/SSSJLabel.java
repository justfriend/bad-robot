package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Font;

import javax.swing.JLabel;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.ui.SSSBasicLabelUI;

@SuppressWarnings("serial")
public class SSSJLabel extends JLabel {

	public SSSJLabel() {
		super();
	}
	
	public SSSJLabel(String text) {
		super(text);
	}
	
	{
		setUI(new SSSBasicLabelUI());
		setFont(new Font(SSSPalette.fontFamily, 0, 12));
	}
}
