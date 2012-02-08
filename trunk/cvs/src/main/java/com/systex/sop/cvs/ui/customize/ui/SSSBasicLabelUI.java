package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import sun.swing.SwingUtilities2;

import com.systex.sop.cvs.ui.customize.SSSPalette;

public class SSSBasicLabelUI extends BasicLabelUI {

	protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		/** .[- _"]. START **/
		Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		/** .[- _"]. ENDED **/
		
		int mnemIndex = l.getDisplayedMnemonicIndex();
		g2.setColor(l.getForeground());
		SwingUtilities2.drawStringUnderlineCharAt(l, g2, s, mnemIndex, textX, textY);
	}

	protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		/** .[- _"]. START **/
		Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		/** .[- _"]. ENDED **/
		
		int accChar = l.getDisplayedMnemonicIndex();
		Color background = l.getBackground();
		g2.setColor(background.brighter());
		SwingUtilities2.drawStringUnderlineCharAt(l, g2, s, accChar, textX + 1, textY + 1);
		g2.setColor(background.darker());
		SwingUtilities2.drawStringUnderlineCharAt(l, g2, s, accChar, textX, textY);
	}
	
}
