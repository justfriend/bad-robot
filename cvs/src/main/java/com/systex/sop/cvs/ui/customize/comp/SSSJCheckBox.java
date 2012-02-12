package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class SSSJCheckBox extends JCheckBox {
	
	{
		setIcon(new ImageIcon(SSSJCheckBox.class.getResource("/resource/uncheck.png")));
		setSelectedIcon(new ImageIcon(SSSJCheckBox.class.getResource("/resource/checked.png")));
	}
	
	public SSSJCheckBox() {
		super();
	}

	public SSSJCheckBox(Action a) {
		super(a);
	}

	public SSSJCheckBox(Icon icon, boolean selected) {
		super(icon, selected);
	}

	public SSSJCheckBox(Icon icon) {
		super(icon);
	}

	public SSSJCheckBox(String text, boolean selected) {
		super(text, selected);
	}

	public SSSJCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
	}

	public SSSJCheckBox(String text, Icon icon) {
		super(text, icon);
	}

	public SSSJCheckBox(String text) {
		super(text);
	}

}
