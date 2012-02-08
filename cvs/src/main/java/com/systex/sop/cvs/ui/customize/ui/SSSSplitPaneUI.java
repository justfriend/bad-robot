package com.systex.sop.cvs.ui.customize.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.systex.sop.cvs.ui.customize.other.SSSSplitPaneDivider;

public class SSSSplitPaneUI extends BasicSplitPaneUI {
	public SSSSplitPaneUI() {
		super();
	}

	/**
	 * Creates a new SSSSplitPaneUI instance
	 */
	public static ComponentUI createUI(JComponent x) {
		SSSSplitPaneUI ui = new SSSSplitPaneUI();
		return ui;
	}

	/**
	 * Creates the default divider.
	 */
	public BasicSplitPaneDivider createDefaultDivider() {
		return new SSSSplitPaneDivider(this);
	}
}
