package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JLabel;

import com.systex.sop.cvs.ui.customize.ui.SSSTrafficLabelUI;

@SuppressWarnings("serial")
public class SSSTrafficLabel extends JLabel {
	public enum LIGHT {
		RED, GREEN, YELLOW
	};

	private SSSTrafficLabelUI ui = null;

	public SSSTrafficLabel(LIGHT light) {
		super();
		ui = new SSSTrafficLabelUI(this, light);
		this.setUI(ui);
	}

	public void setLight(LIGHT light) {
		ui.setLight(light);
	}

}
