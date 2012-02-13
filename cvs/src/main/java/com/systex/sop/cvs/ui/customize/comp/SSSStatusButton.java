package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.systex.sop.cvs.ui.customize.ui.SSSStatusButtonUI;

@SuppressWarnings("serial")
public class SSSStatusButton extends JButton {
	private STATUS status = STATUS.NONE;
	
	public enum STATUS {NONE, ACTIVE, DONE, ERROR};
	
	{
		setUI(new SSSStatusButtonUI(this));
		setEnabled(false);
	}

	public SSSStatusButton() {
		super();
	}

	public SSSStatusButton(Action a) {
		super(a);
	}

	public SSSStatusButton(Icon icon) {
		super(icon);
	}

	public SSSStatusButton(String text, Icon icon) {
		super(text, icon);
	}

	public SSSStatusButton(String text) {
		super(text);
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

}
