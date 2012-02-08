package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.systex.sop.cvs.ui.customize.SSSPalette;
import com.systex.sop.cvs.ui.customize.comp.SSSJTabbedPane;

public class SSSTabbedPaneUI extends BasicTabbedPaneUI {

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
	private Color border_D_Color;
	private Color border_N_Color;
	private Color border_Y_Color;
	private Color borderLeftEdgeColor;
	private Color borderRightEdgeColor;
	private Color borderTopEdgeColor;
	private SSSJTabbedPane c;
	private Color contentAreaColor;
	private boolean contentOpaque = true;
	protected Color darkShadow = new Color(0, 0, 255);
	private Color [] enable_BOM_Color;
	private Color [] enable_TOP_Color;
	protected Color lightHighlight = new Color(255, 0, 0);
	private Color [] selected_BOM_N_Color;
	private Color [] selected_BOM_Y_Color;
	private Color [] selected_TOP_N_Color;
	
	
	private Color [] selected_TOP_Y_Color;
	private Color selectedColor;
	
	protected Color shadow = new Color(0, 255, 0);
	
	private boolean tabsOverlapBorder;
	
	public SSSTabbedPaneUI(SSSJTabbedPane c) {
		super();
		this.c = c;
		enable_TOP_Color = new Color [] {new Color(255, 223, 83), new Color(255, 223, 83)};
		enable_BOM_Color = new Color [] {new Color(255, 201, 0), new Color(255, 201, 0)};
		selected_TOP_Y_Color = new Color [] {new Color(244, 174, 117), new Color(230, 125, 37)};
//		selected_TOP_Y_Color = new Color [] {Color.RED, Color.BLUE};
		selected_BOM_Y_Color = new Color [] {new Color(227, 115, 23), new Color(210, 95, 1)};
//		selected_BOM_Y_Color = new Color [] {Color.MAGENTA, Color.CYAN};
		selected_TOP_N_Color = new Color [] {new Color(240, 239, 239), new Color(219, 217, 216)};
		selected_BOM_N_Color = new Color [] {new Color(219, 217, 216), new Color(196, 194, 193)};
		selectedColor = UIManager.getColor("TabbedPane.selected");
		borderTopEdgeColor = new Color(210, 95, 1);
		borderLeftEdgeColor = new Color(134, 141, 143);
		borderRightEdgeColor = new Color(134, 141, 143);
		contentAreaColor = new Color(243, 243, 243);
		border_Y_Color = new Color(91, 88, 86);
		border_N_Color = new Color(98, 69, 47);
		border_D_Color = new Color(255, 140, 0);
		
		tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");
		contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
		
//		Insets insets = c.getInsets();
//		insets.left = insets.left + 100;
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		if (super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) < 30)
			return 28;
		else
			return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
	}

	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
        int width = tabInsets.left + tabInsets.right + 3;
        Component tabComponent = tabPane.getTabComponentAt(tabIndex);
        if (tabComponent != null) {
            width += tabComponent.getPreferredSize().width;
        } else {
            Icon icon = getIconForTab(tabIndex);
            if (icon != null) {
                width += icon.getIconWidth() + textIconGap;
            }
            View v = getTextViewForTab(tabIndex);
            if (v != null) {
                // html
                width += (int) v.getPreferredSpan(View.X_AXIS);
            } else {
                // plain text
                String title = tabPane.getTitleAt(tabIndex);
                width += SwingUtilities2.stringWidth(tabPane, metrics, title);
            }
        }
        
        width = 195;
        return width;
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
//		paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
//		paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
//		paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);

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
				g.drawLine(x, selRect.y + selRect.height - 1, x, y + h - 2);
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
			g.drawLine(x, y, x + w, y);
			g.drawLine(x, y + 1, x + w, y + 1);
		}
	}
	
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
			boolean isSelected) {
//		System.err.println ("x is " + x + " y is " + y + " w is " + w + " h is " + h);
		Graphics2D g2d = (Graphics2D) g;
		
		/** 上半部底色 **/
		if (isSelected) {
			g2d.setPaint(new GradientPaint(x, 0, Color.WHITE, x, h/2, selected_TOP_Y_Color[1]));
		}else{
			g2d.setPaint(new GradientPaint(x, 0, selected_TOP_N_Color[0], x, h/2, selected_TOP_N_Color[1]));
		}
		if (!c.isEnabledAt(tabIndex)) {
			g2d.setPaint(new GradientPaint(x, 0, enable_TOP_Color[0], x, h/2, enable_TOP_Color[1]));
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
		
		/** 下半部底色 **/
		if (isSelected) {
			g2d.setPaint(new GradientPaint(x, h/2, selected_BOM_Y_Color[0], x, h, selected_BOM_Y_Color[1]));
		}else{
			g2d.setPaint(new GradientPaint(x, h/2, selected_BOM_N_Color[0], x, h, selected_BOM_N_Color[1]));
		}
		if (!c.isEnabledAt(tabIndex)) {
			g2d.setPaint(new GradientPaint(x, 0, enable_BOM_Color[0], x, h/2, enable_BOM_Color[1]));
		}
		
		switch (tabPlacement) {
		case LEFT:
			g2d.fill(fxShape(x + 1, y + h/2, w - 1, h/2 - 1 -1));
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
		if (!c.isEnabledAt(tabIndex)) {
			g.setColor(border_D_Color);
		}else{
			if (!isSelected) {
				g.setColor(border_Y_Color);
			}else{
				g.setColor(border_N_Color);
			}
		}

		switch (tabPlacement) {
		case LEFT:
			g.drawLine(x + 2, y, x + w, y);								// 銝��
			g.drawLine(x + 1, y + 1, x + 1, y + 1);						// 撌虫�閫�暺�
			g.drawLine(x + 1, y + h - 2 -1, x + 1, y + h - 2-1);		// 撌虫�閫�暺�-1
			g.drawLine(x, y + 2, x, y + h - 3-1);						// 撌阡���
			g.drawLine(x + 2, y + h - 1-1, x + w, y + h - 1-1);			// 銝��
			break;
		case RIGHT:
			g.drawLine(x, y, x + w - 3, y);								// 銝��
			g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);				// �喃�閫�暺�
			g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);		// �喃�閫�暺�
			g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 3);			// �喲���
			g.drawLine(x, y + h - 1, x + w - 3, y + h - 1);				// 銝��
			break;
		case BOTTOM:
			g.drawLine(x + 3, y + h, x + w - 4, y + h);					// 銝��
			g.drawLine(x + 2, y + h - 1, x + 2, y + h - 1);				// 銝�撅斤�暺�撌�
			g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);				// 銝�撅斤�暺�撌�
			g.drawLine(x + w - 3, y + h - 1, x + w - 3, y + h - 1); 	// 銝�撅斤�暺���
			g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2); 	// 銝�撅斤�暺���
			g.drawLine(x, y, x, y + h - 2);								// 撌阡���
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 2);				// �喲���
			break;
		case TOP:
		default:
			g.drawLine(x, y, x + w - 1, y);								// 上方邊線
			g.drawLine(x, y + 1, x, y + h - 1);							// 左方邊線
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);				// 右方邊線
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
				Color fg = (!isSelected)? Color.BLACK: Color.WHITE;
				if (isSelected && (fg instanceof UIResource)) {
					Color selectedFG = UIManager.getColor("TabbedPane.selectedForeground");
					if (selectedFG != null) {
						fg = selectedFG;
					}
				}
				g.setColor(fg);
				/** .[- _"]. START **/
				Graphics2D g2 = (Graphics2D) g;
				font = new Font(SSSPalette.fontFamily, Font.BOLD, 12);
				g2.setFont(font);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				/** .[- _"]. ENDED **/
				
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g2, title, mnemIndex, textRect.x, textRect.y
						+ metrics.getAscent());

			} else { // tab disabled
				g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y
						+ metrics.getAscent());
				g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y
						+ metrics.getAscent());
			}
		}
	}
}
