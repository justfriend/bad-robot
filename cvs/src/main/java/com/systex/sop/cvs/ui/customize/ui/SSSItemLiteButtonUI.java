package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.systex.sop.cvs.ui.customize.SSSPalette;

public class SSSItemLiteButtonUI extends BasicButtonUI implements MouseListener {
	private static final Color COLOR_BORDER = new Color(203, 142, 70);			// 邊框顏色 (一般)
	private static final Color COLOR_BORDER_CLICK = new Color(125, 125, 125);	// 邊框顏色 (點下)
	private static final Color COLOR_TOP = new Color(250, 244, 226);			// 一段式漸層的上方色彩 (背景色)
	private static final Color COLOR_BOM = new Color(236, 222, 183);			// 一段式漸層的上方色彩 (背景色)
	private static final Color COLOR_OVER_TOP = Color.WHITE;					// 一段式漸層的上方色彩 (背景色)(滑鼠經過)
	private static final Color COLOR_OVER_BOM = Color.WHITE;					// 一段式漸層的上方色彩 (背景色)(滑鼠經過)
	private static final Color COLOR_CLICK_TOP = new Color(113, 113, 113);		// 一段式漸層的上方色彩 (背景色)(滑鼠點下)
	private static final Color COLOR_CLICK_BOM = new Color(171, 171, 171);		// 一段式漸層的上方色彩 (背景色)(滑鼠點下)
	
	private static final int arcWidth = 13;
	private static final int arcHeight = 13;
	
	private static Rectangle viewRect = new Rectangle();
	private static Rectangle textRect = new Rectangle();
	private static Rectangle iconRect = new Rectangle();
	private boolean isArmed = false;
	private boolean isPressed = false;
	
	public SSSItemLiteButtonUI(AbstractButton btn) {
		btn.addMouseListener(this);
		btn.setBorderPainted(false);
	}
	
	private String layout(AbstractButton b, FontMetrics fm, int width, int height) {
		Insets i = b.getInsets();
		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = width - (i.right + viewRect.x);
		viewRect.height = height - (i.bottom + viewRect.y);

		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

		// layout the text and icon
		return SwingUtilities.layoutCompoundLabel(b, fm, b.getText(), b.getIcon(), b.getVerticalAlignment(), b
				.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect,
				iconRect, textRect, b.getText() == null ? 0 : b.getIconTextGap());
	}
	
	@Override
	public void paint(Graphics graphics, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		
		/** .[- _"]. BEGIN **/
		Graphics2D g = (Graphics2D) graphics;
		
		Dimension size = b.getSize();
		LinearGradientPaint linearPaint = null;
		Color border = COLOR_BORDER;
		Color foreColor = Color.BLACK;
		if (isArmed || !b.isEnabled()) {
			if (isPressed || !b.isEnabled()) {
				linearPaint = new LinearGradientPaint(
						new Point(0, 0),
						new Point(0, size.height),
						new float[] { 0.0f, 1.0f },
						new Color[] { COLOR_CLICK_TOP, COLOR_CLICK_BOM });
				g.setPaint(linearPaint);
				g.fillRoundRect(0, 0, size.width - 1, size.height - 1, arcWidth, arcHeight);
				border = COLOR_BORDER_CLICK;
				foreColor = Color.WHITE;
			}else{
				linearPaint = new LinearGradientPaint(
						new Point(0, 0),
						new Point(0, size.height),
						new float[] { 0.0f, 1.0f },
						new Color[] { COLOR_OVER_TOP, COLOR_OVER_BOM });
				g.setPaint(linearPaint);
				g.fillRoundRect(0, 0, size.width - 1, size.height - 1, arcWidth, arcHeight);
			}
		}else{
			linearPaint = new LinearGradientPaint(
					new Point(0, 0),
					new Point(0, size.height),
					new float[] { 0.0f, 0.6f },
					new Color[] { COLOR_TOP, COLOR_BOM });
			g.setPaint(linearPaint);
			g.fillRoundRect(0, 0, size.width - 1, size.height - 1, arcWidth, arcHeight);
		}
		
		// [外框總成]
		g.setColor(border);
		g.drawRoundRect(0, 0, size.width - 1, size.height - 1, arcWidth, arcHeight);
		
		/** .[- _"]. ENDED **/

		String text = layout(b, SwingUtilities2.getFontMetrics(b, g), b.getWidth(), b.getHeight());

		clearTextShiftOffset();

		// perform UI specific press action, e.g. Windows L&F shifts text
		if (model.isArmed() && model.isPressed()) {
			paintButtonPressed(g, b);
		}

		// Paint the Icon
		if (b.getIcon() != null) {
			paintIcon(g, c, iconRect);
		}

		if (text != null && !text.equals("")) {
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
			/** .[- _"]. START **/
			Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
			g.setFont(font);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			b.setForeground(foreColor);
			/** .[- _"]. ENDED **/
			if (v != null) {
				v.paint(g, textRect);
			} else {
				paintText(g, b, textRect, text);
			}
		}

		if (b.isFocusPainted() && b.hasFocus()) {
			// paint UI specific focus
			paintFocus(g, b, viewRect, textRect, iconRect);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		isArmed = true;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		isArmed = false;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		isPressed = true;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		isPressed = false;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}
}
