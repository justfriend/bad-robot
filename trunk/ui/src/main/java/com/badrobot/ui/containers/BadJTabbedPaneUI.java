package com.badrobot.ui.containers;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

public class BadJTabbedPaneUI extends BasicTabbedPaneUI {
	
	private Color [] selected_TOP_Y_Color;
	private Color [] selected_TOP_N_Color;
	private Color [] selected_BOM_Y_Color;
	private Color [] selected_BOM_N_Color;
	private Color selectedColor;
	private Color borderTopEdgeColor;
	private Color borderLeftEdgeColor;
	private Color borderRightEdgeColor;
	private Color contentAreaColor;
	private Color border_Y_Color;
	private Color border_N_Color;
	protected Color lightHighlight = new Color(255, 0, 0);
	protected Color shadow = new Color(0, 255, 0);
	
	
	protected Color darkShadow = new Color(0, 0, 255);
	private boolean tabsOverlapBorder;
	
	private boolean contentOpaque = true;
	
	public BadJTabbedPaneUI() {
		selected_TOP_Y_Color = new Color [] {new Color(252, 252, 252), new Color(241, 242, 242)};
		selected_BOM_Y_Color = new Color [] {new Color(239, 240, 241), new Color(227, 229, 230)};
		selected_TOP_N_Color = new Color [] {new Color(198, 226, 165), new Color(150, 195,  98)};
		selected_BOM_N_Color = new Color [] {new Color(136, 186,  78), new Color(116, 168,  56)};
		selectedColor = UIManager.getColor("TabbedPane.selected");
		borderTopEdgeColor = new Color(167, 173, 175);
		borderLeftEdgeColor = new Color(134, 141, 143);
		borderRightEdgeColor = new Color(134, 141, 143);
		contentAreaColor = new Color(243, 243, 243);
//		contentAreaColor = new Color(255, 0, 0);
		border_Y_Color = new Color( 58, 126,  20);
		border_N_Color = new Color(166, 172, 174);
		
		tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
		contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
	}
	
	public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
		protected void calculateTabRects(int tabPlacement, int tabCount) {
			super.calculateTabRects(tabPlacement, tabCount);
			int shift = -1;
			if (LEFT == tabPlacement || RIGHT == tabPlacement) shift = 0;
			for (int i = 0; i < rects.length; i++) {
				rects[i].x = rects[i].x + (shift * i);
			}
		}
		protected void padSelectedTab(int tabPlacement, int selectedIndex) {}
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		if (super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) < 30)
			return 28;
		else
			return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
	}

	@Override
	protected LayoutManager createLayoutManager() {
		if (tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
			return super.createLayoutManager();
		} else { /* WRAP_TAB_LAYOUT */
			return new TabbedPaneLayout();
		}
	}
	
	private Shape fxShape(int x, int y, int w, int h) {
		Rectangle2D rec = new Rectangle2D.Float(x, y, w, h + 1);
		Area a = new Area(rec);
		return a;
	}
	
	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
		int width = tabPane.getWidth();
		int height = tabPane.getHeight();
		Insets insets = tabPane.getInsets();
		Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

		int x = insets.left;
		int y = insets.top;
		int w = width - insets.right - insets.left;
		int h = height - insets.top - insets.bottom;

		switch (tabPlacement) {
		case LEFT:
			x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				x -= tabAreaInsets.right;
			}
			w -= (x - insets.left);
			break;
		case RIGHT:
			w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				w += tabAreaInsets.left;
			}
			break;
		case BOTTOM:
			h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				h += tabAreaInsets.top;
			}
			break;
		case TOP:
		default:
			y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				y -= tabAreaInsets.bottom;
			}
			h -= (y - insets.top);
		}

		if (tabPane.getTabCount() > 0 && (contentOpaque || tabPane.isOpaque())) {
			// Fill region behind content area
//			Color color = UIManager.getColor("TabbedPane.contentAreaColor");
			Color color = contentAreaColor;
			if (color != null) {
				g.setColor(color);
			} else if (selectedColor == null || selectedIndex == -1) {
				g.setColor(tabPane.getBackground());
			} else {
				g.setColor(selectedColor);
			}
			g.fillRect(x, y, w, h);
		}

		paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
		paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);

	}

	@Override
	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h) {
		Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

//		g.setColor(lightHighlight);
		g.setColor(borderTopEdgeColor);

		// Draw unbroken line if tabs are not on TOP, OR
		// selected tab is not in run adjacent to content, OR
		// selected tab is not visible (SCROLL_TAB_LAYOUT)
		//
		if (tabPlacement != TOP || selectedIndex < 0 || (selRect.y + selRect.height + 1 < y)
				|| (selRect.x < x || selRect.x > x + w)) {
			g.drawLine(x, y, x + w - 2, y);
		} else {
			// Break line to show visual connection to selected tab
			g.drawLine(x, y, selRect.x - 1, y);
			if (selRect.x + selRect.width < x + w - 2) {
				g.drawLine(selRect.x + selRect.width, y, x + w - 2, y);
			} else {
				g.setColor(shadow);
				g.drawLine(x + w - 2, y, x + w - 2, y);
			}
		}
	}
	
	@Override
	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
			int h) {
		Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

		g.setColor(borderLeftEdgeColor);

		// Draw unbroken line if tabs are not on LEFT, OR
		// selected tab is not in run adjacent to content, OR
		// selected tab is not visible (SCROLL_TAB_LAYOUT)
		if (tabPlacement != LEFT || selectedIndex < 0 || (selRect.x + selRect.width + 1 < x)
				|| (selRect.y < y || selRect.y > y + h)) {
			g.drawLine(x, y, x, y + h - 2);
		} else {
			// Break line to show visual connection to selected tab
			g.drawLine(x, y, x, selRect.y - 1);
			if (selRect.y + selRect.height < y + h - 2) {
				g.drawLine(x, selRect.y + selRect.height, x, y + h - 2);
			}
		}
	}
	
	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
			int h) {
		Rectangle selRect = selectedIndex < 0 ? null : getTabBounds(selectedIndex, calcRect);

		g.setColor(borderRightEdgeColor);

		// Draw unbroken line if tabs are not on RIGHT, OR
		// selected tab is not in run adjacent to content, OR
		// selected tab is not visible (SCROLL_TAB_LAYOUT)
		if (tabPlacement != RIGHT || selectedIndex < 0 || (selRect.x - 1 > w) || (selRect.y < y || selRect.y > y + h)) {
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		} else {
			// Break line to show visual connection to selected tab
			g.drawLine(x + w - 1, y, x + w - 1, selRect.y - 1);

			if (selRect.y + selRect.height < y + h - 2) {
				g.drawLine(x + w - 1, selRect.y + selRect.height, x + w - 1, y + h - 2);
			}
		}
	}
	
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
			boolean isSelected) {
//		System.err.println ("x is " + x + " y is " + y + " w is " + w + " h is " + h);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/** 上半部-顏色漸變 **/
		if (isSelected) {
			g2d.setPaint(new GradientPaint(x, 0, selected_TOP_Y_Color[0], x, h/2, selected_TOP_Y_Color[1]));
		}else{
			g2d.setPaint(new GradientPaint(x, 0, selected_TOP_N_Color[0], x, h/2, selected_TOP_N_Color[1]));
		}
		switch (tabPlacement) {
		case LEFT:
			g2d.fill(fxShape(x + 1, y, w - 1, h/2 - 1));
			break;
		case RIGHT:
			g2d.fill(fxShape(x, y + 1, w - 1, h/2 - 1));
			break;
		case BOTTOM:
			g2d.fill(fxShape(x + 1, y, w - 2, h/2 - 1));
			break;
		case TOP:
		default:
			g2d.fill(fxShape(x + 1, y + 1, w - 2, h/2 - 1));
		}
		
		/** 下半部-顏色漸變 **/
		if (isSelected) {
			g2d.setPaint(new GradientPaint(x, h/2, selected_BOM_Y_Color[0], x, h, selected_BOM_Y_Color[1]));
		}else{
			g2d.setPaint(new GradientPaint(x, h/2, selected_BOM_N_Color[0], x, h, selected_BOM_N_Color[1]));
		}
		switch (tabPlacement) {
		case LEFT:
			g2d.fill(fxShape(x + 1, y + h/2, w - 1, h/2 - 1));
			break;
		case RIGHT:
			g2d.fill(fxShape(x, y + h/2, w - 1, h/2 - 1));
			break;
		case BOTTOM:
			g2d.fill(fxShape(x + 1, y + h/2, w - 2, h/2 - 1));
			break;
		case TOP:
		default:
			g2d.fill(fxShape(x + 1, y + h/2 + 1, w - 2, h/2 - 1));
		}
	}
	
	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
			boolean isSelected) {
		if (!isSelected) {
			g.setColor(border_Y_Color);
		}else{
			g.setColor(border_N_Color);
		}

		switch (tabPlacement) {
		case LEFT:
			g.drawLine(x + 2, y, x + w - 1, y);							// 上面的線
			g.drawLine(x + 1, y + 1, x + 1, y + 1);						// 左上角的點
			g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);				// 左下角的點
			g.drawLine(x, y + 2, x, y + h - 3);							// 左邊的線
			g.drawLine(x + 2, y + h - 1, x + w - 1, y + h - 1);			// 下面的線
			break;
		case RIGHT:
			g.drawLine(x, y, x + w - 3, y);								// 上面的線
			g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);				// 右上角的點
			g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);		// 右下角的點
			g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 3);			// 右邊的線
			g.drawLine(x, y + h - 1, x + w - 3, y + h - 1);				// 下面的線
			break;
		case BOTTOM:
			g.drawLine(x + 3, y + h, x + w - 4, y + h);					// 下面的線
			g.drawLine(x + 2, y + h - 1, x + 2, y + h - 1);				// 下一層的點(左)
			g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);				// 下二層的點(左)
			g.drawLine(x + w - 3, y + h - 1, x + w - 3, y + h - 1); 	// 下一層的點(右)
			g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2); 	// 下二層的點(右)
			g.drawLine(x, y, x, y + h - 2);								// 左邊的線
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 2);				// 右邊的線
			break;
		case TOP:
		default:
			g.drawLine(x + 3, y, x + w - 4, y);							// 上面的線
			g.drawLine(x + 2, y + 1, x + 2, y + 1);						// 下一層的點(左)
			g.drawLine(x + 1, y + 2, x + 1, y + 2);						// 下二層的點(左)
			g.drawLine(x + w - 3, y + 1, x + w - 3, y + 1); 			// 下一層的點(右)
			g.drawLine(x + w - 2, y + 2, x + w - 2, y + 2); 			// 下二層的點(右)
			g.drawLine(x, y + 3, x, y + h - 1);							// 左邊的線
			g.drawLine(x + w - 1, y + 3, x + w - 1, y + h - 1);			// 右邊的線
		}
	}
	
	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title,
			Rectangle textRect, boolean isSelected) {

		g.setFont(font);

		View v = getTextViewForTab(tabIndex);
		if (v != null) {
			// html
			v.paint(g, textRect);
		} else {
			// plain text
			int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

			if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
				Color fg = (!isSelected)? new Color(255, 255, 227): new Color(120, 120, 125);
				if (isSelected && (fg instanceof UIResource)) {
					Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
					if (selectedFG != null) {
						fg = selectedFG;
					}
				}
				g.setColor(fg);
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y
						+ metrics.getAscent());

			} else { // tab disabled
				g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y
						+ metrics.getAscent());
				g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x - 1, textRect.y
						+ metrics.getAscent() - 1);

			}
		}
	}
}
