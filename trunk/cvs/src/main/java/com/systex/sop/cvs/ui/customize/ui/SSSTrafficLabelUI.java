package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel.LIGHT;

public class SSSTrafficLabelUI extends BasicLabelUI {
	private JLabel c = null;
	private Color color_border_GREEN = new Color(59,148, 59);
	private Color color_border_YELLOW = new Color(165, 116, 35);
	private Color color_border_RED = new Color(165, 51, 35);
	private Color color_fill_WHITE = new Color(254, 250, 241);
	private Color color_fill_GREEN = new Color(90, 225, 91);
	private Color color_fill_YELLOW = new Color(254, 184, 62);
	private Color color_fill_RED = new Color(254, 88, 62);
	private Color fillColor = null;
	private Color borderColor = null;
	
	public SSSTrafficLabelUI(JLabel c, LIGHT light) {
		this.c = c;
		setLight(light);
	}
	
	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(12, 12);
	}
	
	private void paintBorder(Graphics2D g) {
		Dimension d = getPreferredSize(c);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(borderColor);
		g.drawRect(0, 0, (int) d.getWidth() - 1, (int) d.getHeight() - 1);
	}
	
	@Override
	protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(l.getForeground());
		paintBorder(g2d);
		paintFillRect(g2d);
	}
	
	private void paintFillRect(Graphics2D g) {
		Dimension d = getPreferredSize(c);
		g.setPaint(new GradientPaint(1, 1, color_fill_WHITE, 1, (int) d.getHeight() - 1, fillColor));
		Rectangle2D rec = new Rectangle2D.Float(1, 1, (int) d.getWidth() - 2, (int) d.getHeight() - 2);
		g.fill(new Area(rec));
	}
	
	public void setLight(LIGHT light) {
		light = (light == null)? LIGHT.RED: light;
		
		if (light == LIGHT.RED) {
			fillColor = color_fill_RED;
			borderColor = color_border_RED;
		}else
		if (light == LIGHT.GREEN) {
			fillColor = color_fill_GREEN;
			borderColor = color_border_GREEN;
		}else
		if (light == LIGHT.YELLOW) {
			fillColor = color_fill_YELLOW;
			borderColor = color_border_YELLOW;
		}
	}
}
