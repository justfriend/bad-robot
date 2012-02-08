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

public class SSSItemDarkButtonUI extends BasicButtonUI implements MouseListener {
	public static final Color TBTN_ITEMDARK_BORDER = new Color(33, 32, 30);			// 邊框顏色
	public static final Color TBTN_ITEMDARK_BORDER_1 = new Color(71, 68, 64);		// 邊框顏色邊緣色彩修飾 (邊緣)
	public static final Color TBTN_ITEMDARK_BORDER_2 = new Color(43, 42, 39);		// 邊框顏色邊緣色彩修飾 (邊框連接點)
	public static final Color TBTN_ITEMDARK_BORDER_3 = new Color(77, 75, 71);		// 邊框顏色邊緣色彩修飾 (內)
	public static final Color TBTN_ITEMDARK_TOP_TOP = new Color(104, 101, 95);		// 二段式漸層上段的上方色彩
	public static final Color TBTN_ITEMDARK_TOP_BOM = new Color(61, 58, 53);		// 二段式漸層上段的下方色彩
	public static final Color TBTN_ITEMDARK_BOM_TOP = new Color(38, 36, 32);		// 二段式漸層下段的上方色彩
	public static final Color TBTN_ITEMDARK_BOM_BOM = new Color(20, 19, 17);		// 二段式漸層下段的下方色彩
	public static final Color TBTN_ITEMDARK_OVER_TOP = new Color(76, 73, 65);		// 一段式漸層的上方色彩 (滑鼠經過)
	public static final Color TBTN_ITEMDARK_OVER_BOM = new Color(20, 19, 17);		// 一段式漸層的上方色彩 (滑鼠經過)
	public static final Color TBTN_ITEMDARK_CLICK_TOP = TBTN_ITEMDARK_OVER_BOM;		// 一段式漸層的上方色彩 (滑鼠點下)
	public static final Color TBTN_ITEMDARK_CLICK_BOM = TBTN_ITEMDARK_OVER_TOP;		// 一段式漸層的上方色彩 (滑鼠點下)
	
	private static Rectangle viewRect = new Rectangle();
	private static Rectangle textRect = new Rectangle();
	private static Rectangle iconRect = new Rectangle();
	private boolean isArmed = false;
	private boolean isPressed = false;
	
	public SSSItemDarkButtonUI(AbstractButton btn) {
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
		
		Dimension size = b.getSize();
		LinearGradientPaint linearPaint = null;
		if (isArmed) {
			if (isPressed) {
				// 上半部漸層
				linearPaint = new LinearGradientPaint(
						new Point(0, 0),
						new Point(0, size.height/2),
						new float[] { 0.0f, 1.0f },
						new Color[] { TBTN_ITEMDARK_BOM_BOM, TBTN_ITEMDARK_BOM_TOP });
				g.setPaint(linearPaint);
				g.fillRect(1, 1, size.width - 2, size.height / 2);
				
				// 下半部漸層
				linearPaint = new LinearGradientPaint(
						new Point(0, size.height/2),
						new Point(0, size.height),
						new float[] { 0.0f, 1.0f },
						new Color[] { TBTN_ITEMDARK_TOP_BOM, TBTN_ITEMDARK_TOP_TOP });
				g.setPaint(linearPaint);
				g.fillRect(1, size.height / 2, size.width - 2, size.height / 2 -1);
			}else{
				linearPaint = new LinearGradientPaint(
						new Point(0, 0),
						new Point(0, size.height),
						new float[] { 0.0f, 1.0f },
						new Color[] { TBTN_ITEMDARK_OVER_TOP, TBTN_ITEMDARK_OVER_BOM });
				g.setPaint(linearPaint);
				g.fillRect(1, 1, size.width - 2, size.height - 2);
			}
		}else{
			// 上半部漸層
			linearPaint = new LinearGradientPaint(
					new Point(0, 0),
					new Point(0, size.height/2),
					new float[] { 0.0f, 1.0f },
					new Color[] { TBTN_ITEMDARK_TOP_TOP, TBTN_ITEMDARK_TOP_BOM });
			g.setPaint(linearPaint);
			g.fillRect(1, 1, size.width - 2, size.height / 2);
			
			// 下半部漸層
			linearPaint = new LinearGradientPaint(
					new Point(0, size.height/2),
					new Point(0, size.height),
					new float[] { 0.0f, 1.0f },
					new Color[] { TBTN_ITEMDARK_BOM_TOP, TBTN_ITEMDARK_BOM_BOM });
			g.setPaint(linearPaint);
			g.fillRect(1, size.height / 2, size.width - 2, size.height / 2 -1);
		}
		
		// [外框總成]
		g.setColor(TBTN_ITEMDARK_BORDER);
		DrawRectHelper.drawRoundRect(g, 0, 0, 0, 0, size.width, size.height);
		//  外框邊緣 (四個角落共八點)
		g.setColor(TBTN_ITEMDARK_BORDER_1);
		g.fillRect(0, 1, 1, 1);
		g.fillRect(1, 0, 1, 1);
		g.fillRect(size.width - 2, 0, 1, 1);
		g.fillRect(size.width - 1, 1, 1, 1);
		g.fillRect(1, size.height - 1, 1, 1);
		g.fillRect(0, size.height - 2, 1, 1);
		g.fillRect(size.width - 2, size.height - 1, 1, 1);
		g.fillRect(size.width - 1, size.height - 2, 1, 1);
		//  外框邊緣內 (四個角落共四點)
		g.setColor(TBTN_ITEMDARK_BORDER_3);
		g.fillRect(1, 1, 1, 1);
		g.fillRect(size.width - 2, 1, 1, 1);
		g.fillRect(1, size.height - 2, 1, 1);
		g.fillRect(size.width - 2, size.height - 2, 1, 1);
		//  外框連接點 (四個角落共八點)
		g.setColor(TBTN_ITEMDARK_BORDER_2);
		g.fillRect(2, 0, 1, 1);
		g.fillRect(0, 2, 1, 1);
		g.fillRect(size.width - 3, 0, 1, 1);
		g.fillRect(size.width - 1, 2, 1, 1);
		g.fillRect(2, size.height - 1, 1, 1);
		g.fillRect(0, size.height - 3, 1, 1);
		g.fillRect(size.width - 3, size.height - 1, 1, 1);
		g.fillRect(size.width - 1, size.height - 3, 1, 1);
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
			b.setForeground(Color.WHITE);
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
