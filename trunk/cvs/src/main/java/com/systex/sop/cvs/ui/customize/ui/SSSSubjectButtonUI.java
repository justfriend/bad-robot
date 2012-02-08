package com.systex.sop.cvs.ui.customize.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import com.systex.sop.cvs.ui.customize.SSSPalette;

public class SSSSubjectButtonUI extends BasicButtonUI implements MouseListener {
	private boolean isArmed = false;
	
	public SSSSubjectButtonUI(AbstractButton button) {
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics graphics, JComponent comp) {
		Graphics2D g2 = (Graphics2D) graphics;
		
		AbstractButton btn = (AbstractButton) comp;
		Dimension size = btn.getSize();
		
		// fill rect
		g2.setColor(new Color(244, 244, 244));
		g2.fillRect(0, 0, size.width, size.height);
		
		// draw border
		g2.setColor(new Color(223, 223, 223));
		g2.drawLine(0, 0, size.width, 0);
		g2.setColor(new Color(240, 240, 240));
		g2.drawLine(0, size.height - 2, size.width, size.height - 2);
		g2.setColor(new Color(231, 231, 231));
		g2.drawLine(0, size.height - 1, size.width, size.height - 1);
		
		// Draw text
		FontMetrics metrics = g2.getFontMetrics();
		Font font = new Font(SSSPalette.fontFamily, Font.BOLD, 13);
		g2.setFont(font);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		if (isArmed) {
			g2.setColor(Color.BLACK);
		}else{
			g2.setColor(Color.GRAY);
		}
//		int x = (int) ((btn.getWidth() - textBoundary.getWidth()) / 2 - 1);	// center 
		int x = 10;
		int y = btn.getHeight() - metrics.getHeight() - ((btn.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() - 1;
		g2.drawString(btn.getText(), x, y);
		g2.dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		isArmed = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isArmed = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
