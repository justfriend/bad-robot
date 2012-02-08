package com.badrobot.ui.containers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class BadJStatusPanel extends JPanel {
	private BufferedImage cache = null;
	private Color color1 = new Color(206, 230, 190, 255);
	private Color color2 = new Color(158, 204, 122, 255);
	private Color color3 = new Color(127, 190, 53, 255);
	private Color color4 = new Color(152, 206, 66, 255);
	private float positionColor1 = 0.0f;
	private float positionColor2 = 0.1f;
	private float positionColor3 = 0.4f;
	private float positionColor4 = 1.0f;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(-1, 26);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		// Paint fast background gradient
		if (cache == null || cache.getHeight() != getHeight()) {
			cache = createBackgroundImage();
		}

		g2.drawImage(cache, 0, 0, getWidth(), getHeight(), null);

		g2.setColor(new Color(78, 140, 31));
		g2.drawLine(0, 0, getWidth() - 1, 0);
		g2.setColor(new Color(206, 230, 190));
		g2.drawLine(0, 1, getWidth() - 1, 1);

		g2.dispose();
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
		cache = null;
		repaint();
	}

	public Color getColor1() {
		return this.color1;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
		cache = null;
		repaint();
	}

	public Color getColor2() {
		return this.color2;
	}

	public void setColor3(Color color3) {
		this.color3 = color3;
		cache = null;
		repaint();
	}

	public Color getColor3() {
		return this.color3;
	}

	public void setColor4(Color color4) {
		this.color4 = color4;
		cache = null;
		repaint();
	}

	public Color getColor4() {
		return this.color4;
	}

	public void setPositionColor1(float positionColor1) {
		if (positionColor1 < 0.0f || positionColor1 > 1.0f) {
			positionColor1 = 0.0f;
		}

		if (positionColor1 > this.positionColor2) {
			positionColor1 = this.positionColor2;
		}

		this.positionColor1 = positionColor1;
		cache = null;
		repaint();
	}

	public float getPositionColor1() {
		return this.positionColor1;
	}

	public void setPositionColor2(float positionColor2) {
		if (positionColor2 < 0.0f || positionColor2 > 1.0f) {
			positionColor2 = 0.1f;
		}

		if (positionColor2 < this.positionColor1) {
			positionColor2 = this.positionColor1;
		}

		if (positionColor2 > this.positionColor3) {
			positionColor2 = this.positionColor3;
		}

		this.positionColor2 = positionColor2;
		cache = null;
		repaint();
	}

	public float getPositionColor2() {
		return this.positionColor2;
	}

	public void setPositionColor3(float positionColor3) {
		if (positionColor3 < 0.0f || positionColor3 > 1.0f) {
			positionColor3 = 0.4f;
		}

		if (positionColor3 < this.positionColor2) {
			positionColor3 = this.positionColor2;
		}

		if (positionColor3 > this.positionColor4) {
			positionColor3 = this.positionColor4;
		}

		this.positionColor3 = positionColor3;
		cache = null;
		repaint();
	}

	public float getPositionColor3() {
		return this.positionColor3;
	}

	public void setPositionColor4(float positionColor4) {
		if (positionColor4 < 0.0f || positionColor4 > 1.0f) {
			positionColor4 = 1.0f;
		}

		if (positionColor4 < this.positionColor3) {
			positionColor4 = this.positionColor3;
		}

		this.positionColor4 = positionColor4;
		cache = null;
		repaint();
	}

	public float getPositionColor4() {
		return this.positionColor4;
	}

	private BufferedImage createBackgroundImage() {
		cache = new BufferedImage(2, getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = cache.createGraphics();

		Point2D start = new Point2D.Float(0, 0);
		Point2D stop = new Point2D.Float(0, getHeight());

		final float[] FRACTIONS = { positionColor1, positionColor2, positionColor3, positionColor4 };

		final Color[] COLORS = { color1, color2, color3, color4 };

		LinearGradientPaint bgndGradient = new LinearGradientPaint(start, stop, FRACTIONS, COLORS);

		g2d.setPaint(bgndGradient);
		g2d.fillRect(0, 0, 2, getHeight());

		g2d.dispose();

		return cache;
	}

	public BadJStatusPanel() {
		this.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.createGap(new ConstantSize(0, ConstantSize.PX)),
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
				RowSpec.createGap(new ConstantSize(0, ConstantSize.PX)), FormFactory.DEFAULT_ROWSPEC, }));
	}
}
