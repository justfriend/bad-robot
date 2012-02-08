package com.systex.sop.cvs.ui.customize.other;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.ui.SSSSplitPaneUI;

@SuppressWarnings("serial")
public class SSSSplitPaneDivider extends BasicSplitPaneDivider {
	
	public SSSSplitPaneDivider(SSSSplitPaneUI ui) {
		super(ui);
	}
	
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		BasicSplitPaneUI ui = getBasicSplitPaneUI();
		Dimension size = ui.getSplitPane().getSize();
		
		// Expect size.width is 2 (UIManager.put("SplitPane.dividerSize", 2))
		g.setColor(SSSPalette.SPLIT_1P);
		g.drawLine(0, 0, 0, size.height);
		g.setColor(SSSPalette.SPLIT_2P);
		g.drawLine(1, 0, 1, size.height);
	}
}
