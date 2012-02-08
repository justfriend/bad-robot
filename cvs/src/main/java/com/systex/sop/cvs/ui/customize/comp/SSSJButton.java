package com.systex.sop.cvs.ui.customize.comp;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.systex.sop.cvs.ui.customize.ui.SSSItemDarkButtonUI;
import com.systex.sop.cvs.ui.customize.ui.SSSItemLiteButtonUI;
import com.systex.sop.cvs.ui.customize.ui.SSSSubjectButtonUI;
import com.systex.sop.cvs.ui.customize.ui.SSSTitleButtonUI;

@SuppressWarnings("serial")
public class SSSJButton extends JButton {
	public static final int DEFAULT = 0;
	public static final int SUBJECT = 1;
	public static final int TITLE = 2;
	public static final int ITEM_DARK = 3;
	public static final int ITEM_LITE = 4;
	
	public SSSJButton(int style) {
		super();
		if (style == DEFAULT) {
			// pass
		}
		if (style == SUBJECT) {
			this.setUI(new SSSSubjectButtonUI(this));
		}else
		if (style == TITLE) {
			this.setUI(new SSSTitleButtonUI(this));
			this.setHorizontalAlignment(SwingConstants.LEFT);
		}else
		if (style == ITEM_DARK) {
			this.setUI(new SSSItemDarkButtonUI(this));
		}else
		if (style == ITEM_LITE) {
			this.setUI(new SSSItemLiteButtonUI(this));
		}
	}
	
	public SSSJButton() {
		this(ITEM_LITE);
	}
	
}
