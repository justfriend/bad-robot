package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

import com.ibm.iwt.window.IWindowButton;
import com.systex.sop.cvs.ui.customize.SSSPalette;

@SuppressWarnings("serial")
public class SSSIWindowButton extends IWindowButton implements MouseListener {
	private enum MOUSE_EVENT {NORMAL, ENTERED, EXITED, PRESSED, RELEASED};
	private MOUSE_EVENT currentEVENT = MOUSE_EVENT.NORMAL;
	
	public SSSIWindowButton(int btnType) {
		super(btnType);
		this.setOpaque(false);
		this.setBorderPainted(false);
		this.setFocusable(false);
		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(20, 18));
	}
	
	private void paintButton(Graphics2D g, int buttonType) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		Color foreColor = null, backColor = null, borderColor = null;
		if (MOUSE_EVENT.ENTERED == currentEVENT) {
			backColor = SSSPalette.OVER_BG;
			foreColor = SSSPalette.OVER_FG;
			borderColor = SSSPalette.OVER_BD;
		}else
		if (MOUSE_EVENT.PRESSED == currentEVENT) {
			backColor = SSSPalette.PRESS_BG;
			foreColor = SSSPalette.PRESS_FG;
			borderColor = SSSPalette.PRESS_BD;
		}else{
			backColor = SSSPalette.FRAME_BG;
			foreColor = SSSPalette.FRAME_FG_DARK;
			borderColor = SSSPalette.FRAME_BG;
		}
		
		int w = getWidth();
		int h = getHeight();
		g.setColor(backColor);
		g.fillRect(0, 0, w - 1, h - 1);
		g.setColor(borderColor);
		g.drawRect(0, 0, w - 1, h - 1);
		g.setColor(foreColor);
		
		if (IWindowButton.MINIMIZE == buttonType) {
			g.drawLine(4, 13, 15, 13);
		}else if (IWindowButton.RESTORE_MIN == buttonType) {
			g.drawLine(8, 5, 15, 5);
			g.drawRect(8, 4, 7, 6);
			g.setColor(backColor);
			g.fillRect(5, 8, 7, 5);
			g.setColor(foreColor);
			g.drawLine(5, 9, 12, 9);
			g.drawRect(5, 8, 7, 5);
		}else if (IWindowButton.RESTORE_MAX == buttonType) {
			g.drawLine(5, 4, 15, 4);
			g.drawRect(5, 5, 10, 8);
		}else if (IWindowButton.CLOSE == buttonType) {
			g.drawLine(5, 4, 14, 13);
			g.drawLine(14, 4, 5, 13);
		}
	}

	@Override
	protected void paintCloseButton(Graphics g) {
		paintButton((Graphics2D) g, IWindowButton.CLOSE);
	}
	
	@Override
	protected void paintMinButton(Graphics g) {
		paintButton((Graphics2D) g, IWindowButton.MINIMIZE);
	}

	@Override
	protected void paintRestoreMaxButton(Graphics g) {
		paintButton((Graphics2D) g, IWindowButton.RESTORE_MAX);
	}

	@Override
	protected void paintRestoreMinButton(Graphics g) {
		paintButton((Graphics2D) g, IWindowButton.RESTORE_MIN);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentEVENT = MOUSE_EVENT.ENTERED;
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentEVENT = MOUSE_EVENT.EXITED;
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentEVENT = MOUSE_EVENT.PRESSED;
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentEVENT = MOUSE_EVENT.RELEASED;
				((AbstractButton) e.getSource()).repaint();
			}
		});
	}
}
