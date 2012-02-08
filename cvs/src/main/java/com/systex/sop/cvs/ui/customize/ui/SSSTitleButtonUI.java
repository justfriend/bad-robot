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
import com.systex.sop.cvs.util.DrawRectHelper;

public class SSSTitleButtonUI extends BasicButtonUI implements MouseListener {
	private static Rectangle viewRect = new Rectangle();
	private static Rectangle textRect = new Rectangle();
	private static Rectangle iconRect = new Rectangle();
	private boolean isArmed = false;
	private boolean isPressed = false;
	
	public SSSTitleButtonUI(AbstractButton btn) {
		btn.addMouseListener(this);
		btn.setBorderPainted(false);
		btn.setBackground(Color.WHITE);
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
		if (isArmed) {
			if (isPressed) {
				Dimension size = b.getSize();
				LinearGradientPaint linearPaint = new LinearGradientPaint(
						new Point(1, 1),
						new Point(size.width - 2, 1),
						new float[] { 0.0f, 0.6f },
						new Color[] {SSSPalette.TBTN_DARK_LEFT, SSSPalette.TBTN_DARK_RIGHT});
				g.setPaint(linearPaint);
				g.fillRect(3, 1, size.width - 5, size.height - 2);
				g.setColor(new Color(194, 139, 77));
				DrawRectHelper.drawRoundRect(g, 2, 1, 0, 0, size.width, size.height);
				linearPaint = new LinearGradientPaint(
						new Point(1, 1),
						new Point(size.width - 2, 1),
						new float[] { 0.4f, 0.9f },
						new Color[] {new Color(184, 111, 31), new Color(194, 139, 77)});
				g.setPaint(linearPaint);
				g.drawLine(4, 1, size.width - 4, 1);
			}else{
				Dimension size = b.getSize();
				Graphics2D g2 = (Graphics2D) g;
				LinearGradientPaint linearPaint = new LinearGradientPaint(
						new Point(0, size.height/2 -3),
						new Point(0, size.height/2 + 3),
						new float[] { 0.0f, 0.6f },
						new Color[] {SSSPalette.TBTN_LITE_TOP, SSSPalette.TBTN_LITE_BOM});
				g2.setPaint(linearPaint);
				g2.fillRect(3, 1, size.width - 5, size.height - 2);
				g.setColor(new Color(216, 173, 89));
				DrawRectHelper.drawRoundRect(g, 2, 1, 0, 0, size.width, size.height);
			}
		}
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
			/** .[- _"]. START **/
			Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
			g.setFont(font);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			/** .[- _"]. ENDED **/
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
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
