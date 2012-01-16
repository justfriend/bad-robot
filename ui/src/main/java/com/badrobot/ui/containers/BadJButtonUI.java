package com.badrobot.ui.containers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import com.badrobot.ui.constant.RadiusType;

public class BadJButtonUI extends BasicButtonUI implements MouseListener {
	private boolean armed;
	private boolean enabled;
	private RadiusType radiusType;
	private Font font;
	private Thread fader;
	private volatile Color fadingColor;

	public BadJButtonUI(AbstractButton button) {
		this.enabled = true;
		this.radiusType = RadiusType.ROUNDED;
		this.fadingColor = new Color(255, 255, 255, 0);
		this.font = new java.awt.Font("Dialog.bold", 1, 12);
		setRadiusType(RadiusType.ROUNDED);
		button.setOpaque(false);
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addMouseListener(this);
	}

	private BufferedImage createButtonImage(AbstractButton button, String text) {
		System.err.println ("createButtonImage");
		BufferedImage buttonImage = new BufferedImage(button.getWidth(), button.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buttonImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Point2D start = new Point2D.Float(0, 0);
		Point2D stop = new Point2D.Float(0, button.getHeight());

		final float[] FRACTIONS_BACKGROUND = { 0.0f, 0.33f };

		final float[] FRACTIONS_FOREGROUND = { 0.0f, 1.0f };

		final float[] FRACTIONS_HIGHLIGHT = { 0.0f, 1.0f };

		final Color[] COLORS_BACKGROUND;
		final Color[] COLORS_FOREGROUND;
		final Color[] COLORS_HIGHLIGHT = new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f),
				new Color(1.0f, 1.0f, 1.0f, 0.2f) };
		final Color[] COLORS_DISABLED;

		if (armed) {
			COLORS_BACKGROUND = new Color[] { new Color(100, 0, 0), new Color(200, 0, 0) };
			COLORS_FOREGROUND = new Color[] { new Color(100, 0, 0), new Color(200, 0, 0) };
			COLORS_DISABLED = new Color[] { new Color(100, 0, 0), new Color(200, 0, 0) };
		} else {
			if (enabled) {
				COLORS_BACKGROUND = new Color[] { new Color(58, 126, 20), new Color(58, 126, 20) };		// 外層邊框
//				COLORS_FOREGROUND = new Color[] { new Color(136, 186, 78), new Color(116, 168, 56) };	// 內層底色
				COLORS_FOREGROUND = new Color[] { new Color(100, 0, 0), new Color(200, 0, 0) };
				COLORS_DISABLED = new Color[] { new Color(198, 226, 165), new Color(136, 186, 78) };	// 禁用顏色
			} else {
				COLORS_BACKGROUND = new Color[] { new Color(0, 0, 100), new Color(0, 0, 200) };
				COLORS_FOREGROUND = new Color[] { new Color(0, 0, 100), new Color(0, 0, 200) };
				COLORS_DISABLED = new Color[] { new Color(0, 0, 100), new Color(0, 0, 200) };
			}
		}

		// Draw background rectangle
		LinearGradientPaint paintBackground = new LinearGradientPaint(start, stop, FRACTIONS_BACKGROUND,
				COLORS_BACKGROUND);
		g2.setPaint(paintBackground);
		g2.fill(getBackgroundShape(button));

		// Draw foreground rectangle
		LinearGradientPaint paintForeground = new LinearGradientPaint(start, stop, FRACTIONS_FOREGROUND,
				COLORS_FOREGROUND);
		g2.setPaint(paintForeground);
		g2.fill(getForegroundShape(button));

		// Draw highlight shape
		LinearGradientPaint paintHighlight = new LinearGradientPaint(start, stop, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
		g2.setPaint(paintHighlight);
		g2.fill(getForegroundShape(button));

		// Draw disabled rectangle
		LinearGradientPaint paintDisabled = new LinearGradientPaint(start, stop, FRACTIONS_BACKGROUND, COLORS_DISABLED);
		g2.setPaint(paintDisabled);
		g2.fill(getForegroundShape(button));

		// DrawMouseOverHighlight
		if (enabled) {
			Point2D center = new Point2D.Float((button.getWidth() / 2), (button.getHeight() / 2));
			float[] dist = { 0.0f, 1.0f };
			Color[] colors = { fadingColor, new Color(1.0f, 1.0f, 1.0f, 0.0f) };
			int radius = button.getWidth() / 3;
			RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
			g2.setPaint(gradient);
			g2.fill(getBackgroundShape(button));
		}

		// Draw text
		g2.setFont(font);
		FontMetrics metrics = g2.getFontMetrics();
		Rectangle2D textBoundary = metrics.getStringBounds(text, g2);

		int armedOffset;
		if (armed) {
//			armedOffset = 1;
			armedOffset = 0;
		} else {
			armedOffset = 0;
		}

		// Draw text shadow
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(text, (int) ((button.getWidth() - textBoundary.getWidth()) / 2) + armedOffset, button.getHeight()
				- metrics.getHeight() - ((button.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent()
				+ armedOffset);

		// Draw text
		if (enabled) {
			g2.setColor(Color.WHITE);
		} else {
			g2.setColor(Color.LIGHT_GRAY);
		}
		g2.drawString(text, (int) ((button.getWidth() - textBoundary.getWidth()) / 2 + armedOffset - 1), button
				.getHeight()
				- metrics.getHeight()
				- ((button.getHeight() - metrics.getHeight()) / 2)
				+ metrics.getAscent()
				+ armedOffset - 1);

		g2.dispose();

		return buttonImage;
	}

	private float getBackgroundRadius(AbstractButton button) {
		switch (this.radiusType) {
		case SHARP:
			return (0.0f);
		case ROUNDED:
			return (10.0f);
		case PILL:
			return (button.getHeight());
		default:
			return (10.0f);
		}
	}

	private Shape getBackgroundShape(AbstractButton button) {
		return new RoundRectangle2D.Float(0, 0, button.getWidth(), button.getHeight(), getBackgroundRadius(button),
				getBackgroundRadius(button));
	}

	private Shape getForegroundShape(AbstractButton button) {
		float foregroundRadius = getBackgroundRadius(button) - 1.0f;
		return new RoundRectangle2D.Float(1, 1, button.getWidth() - 2, button.getHeight() - 2, foregroundRadius,
				foregroundRadius);
	}

	public RadiusType getRadiusType() {
		return radiusType;
	}

	@Override
	public void paint(Graphics g, JComponent comp) {
		System.err.println ("paint");
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		RenderingHints rh = g2.getRenderingHints();
		rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHints(rh);

		BufferedImage buttonImage = createButtonImage((AbstractButton) comp, ((AbstractButton) comp).getText());

		if (!enabled) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}
		g2.drawImage(buttonImage, 0, 0, null);

		g2.dispose();
	}

	public void setRadiusType(RadiusType radiusType) {
		this.radiusType = radiusType;
	}
	
	private Thread createFadingAnimation(final javax.swing.AbstractButton button, final boolean fadeIn) {
		if (fader != null && fader.isAlive()) {
			fader.interrupt();
		}

		fader = new Thread(new java.lang.Runnable() {
			@Override
			public void run() {
				try {
					int fadingAlpha = fadingColor.getAlpha();

					// Fade to transparent
					if (!fadeIn) {
						while (fadingAlpha > 0) {
							Thread.sleep(50);
							fadingColor = new java.awt.Color(255, 199, 0, fadingAlpha);
							fadingAlpha -= 15;
							button.repaint();
						}
						fadingColor = new java.awt.Color(255, 199, 0, 0);
						button.repaint();
					}

					// Fade to light
					if (fadeIn) {
						while (fadingAlpha < 100) {
							Thread.sleep(50);
							fadingColor = new java.awt.Color(255, 199, 0, fadingAlpha);
							fadingAlpha += 15;
							button.repaint();
						}
						fadingColor = new java.awt.Color(255, 199, 0, 100);
						button.repaint();
					}
				} catch (java.lang.NullPointerException exception) {
				} catch (InterruptedException exception) {
				}
			}
		});

		return fader;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// Start HighlightAnimation
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createFadingAnimation((javax.swing.AbstractButton) e.getSource(), true).start();
			}
		});
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// Stop HighlightAnimation
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createFadingAnimation((javax.swing.AbstractButton) e.getSource(), false).start();
			}
		});
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		System.err.println ("mousePressed");
		if (enabled) {
			armed = true;
		}
		javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
			@Override
			public void run() {
				// Handler logic
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		System.err.println ("mouseReleased");
		armed = false;
		javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable() {
			@Override
			public void run() {
				// Handler logic
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}
}
