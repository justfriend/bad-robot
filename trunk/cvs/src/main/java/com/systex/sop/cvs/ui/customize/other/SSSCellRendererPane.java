package com.systex.sop.cvs.ui.customize.other;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class SSSCellRendererPane extends CellRendererPane {

	public void paintComponent(Graphics g2, Component c, Container p, int x, int y, int w, int h, boolean shouldValidate) {
		if (c == null) {
			if (p != null) {
				Color oldColor = g2.getColor();
				g2.setColor(p.getBackground());
				g2.fillRect(x, y, w, h);
				g2.setColor(oldColor);
			}
			return;
		}

		if (c.getParent() != this) {
			this.add(c);
		}

		c.setBounds(x, y, w, h);

		if (shouldValidate) {
			c.validate();
		}

		boolean wasDoubleBuffered = false;
		if ((c instanceof JComponent) && ((JComponent) c).isDoubleBuffered()) {
			wasDoubleBuffered = true;
			((JComponent) c).setDoubleBuffered(false);
		}

		Graphics2D cg = (Graphics2D) g2.create(x, y, w, h);
		cg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		cg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		cg.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		cg.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		try {
			c.requestFocus();
			c.paint(cg);
		} finally {
			cg.dispose();
		}

		if (wasDoubleBuffered && (c instanceof JComponent)) {
			((JComponent) c).setDoubleBuffered(true);
		}

		c.setBounds(-w, -h, 0, 0);
	}

}
