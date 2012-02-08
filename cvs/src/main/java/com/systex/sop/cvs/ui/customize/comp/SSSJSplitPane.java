package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JSplitPane;
import javax.swing.border.MatteBorder;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.ui.SSSSplitPaneUI;

@SuppressWarnings("serial")
public class SSSJSplitPane extends JSplitPane {

	public SSSJSplitPane() {
		super();
		setBorder(new MatteBorder(1, 1, 1, 1, SSSPalette.FRAME_BD));
		setUI(new SSSSplitPaneUI());
	}

}
