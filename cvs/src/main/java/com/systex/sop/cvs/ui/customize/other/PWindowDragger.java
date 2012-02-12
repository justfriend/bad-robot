package com.systex.sop.cvs.ui.customize.other;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

/**
 * 可以让指定的Component通过鼠标拖动来移动Window的便利类
 * 
 * @author pengranxiang
 */
public class PWindowDragger {

	protected Window fWindow;
	protected Component fComponent;
	protected int dX;
	protected int dY;
	protected MouseListener mouseListener;
	protected MouseMotionListener mouseMotionListener;

	/**
	 * 让指定的Component通过鼠标拖动来移动Window
	 * 
	 * @param window
	 * @param component
	 */
	public PWindowDragger(Window window, Component component) {

		fWindow = window;
		fComponent = component;
		
		mouseListener = createMouseListener();
		mouseMotionListener = createMouseMotionListener();
		fComponent.addMouseListener(mouseListener);
		fComponent.addMouseMotionListener(mouseMotionListener);
	}

	protected MouseListener createMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point clickPoint = new Point(e.getPoint());
				SwingUtilities.convertPointToScreen(clickPoint, fComponent);

				dX = clickPoint.x - fWindow.getX();
				dY = clickPoint.y - fWindow.getY();
			}
		};
	}

	protected MouseMotionAdapter createMouseMotionListener() {
		return new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point dragPoint = new Point(e.getPoint());
				SwingUtilities.convertPointToScreen(dragPoint, fComponent);

				fWindow.setLocation(dragPoint.x - dX, dragPoint.y - dY);
			}
		};
	}

	public MouseListener getMouseListener() {
		return mouseListener;
	}

	public MouseMotionListener getMouseMotionListener() {
		return mouseMotionListener;
	}

}
