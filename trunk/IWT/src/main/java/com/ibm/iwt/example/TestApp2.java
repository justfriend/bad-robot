package com.ibm.iwt.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ibm.iwt.IFrame;
import com.ibm.iwt.layout.GroupFlowLayoutConstraints;
import com.ibm.iwt.window.IWindowButton;
import com.ibm.iwt.window.IWindowTitleBar;

/**
 * Tests changing the colors and sizes on the title bar.
 * @author MAbernethy
 */
public class TestApp2 extends IFrame
{
	private static Color lite = new Color(207, 199, 188);
	private static Color dark = new Color(115, 105, 92);
	
	public static void main(String[] args)
	{
		TestApp2 t = new TestApp2();
		t.setVisible(true);
	}
	
	public TestApp2()
	{
		setSize(640, 480);
//		IWTUtilities.setApplicationBorderSize(this, new Insets(3,3,3,3));
//		setIContentPaneBorder(new LineBorder(new Color(207, 199, 188), 10));
		setTitleBar(new TitleBar());
		setTitleBarBorder(new MatteBorder(1, 1, 0, 1, dark));
		setTitleBarBackground(lite);
		setIContentPaneBorder(new MatteBorder(0, 1, 1, 1, dark));
		
//		setTitleBarHeight(35);
		setTitleBarBackground(new Color(207, 199, 188));		
//		setTitleBarButtonColors(Color.red, Color.white);
//		setTitleBarButtonSize(new Dimension(26, 26));
//		setTitle("Window");
	}
	
	private class TitleBar extends IWindowTitleBar implements ChangeListener
	{
		private Color c = new Color(0,0,0);
		private JMenuBar jmbar = new JMenuBar();
		private JMenu jmenu = new JMenu("ÀÉ®×");
		private JMenuItem jmitem = new JMenuItem("Â÷¶}");
		private JButton jbtn = new JButton();
//		private ImageIcon icon = new ImageIcon(TestApp2.class.getResource("/com/ibm/iwt/ki.png"));
//		private JSlider slider;
		
		public TitleBar()
		{
			setPreferredSize(new Dimension(0, 32));
			removeWindowDecorations();
//			addWindowButton(IWindowButton.CLOSE, SwingConstants.LEFT);
			setWindowButtonColors(lite, dark);
			addWindowButton(IWindowButton.CLOSE, dark, lite);
//			addWindowButton(IWindowButton.CLOSE, IWindowButton.CLOSE);
//			slider = new JSlider();
//			add(slider, new GroupFlowLayoutConstraints(SwingConstants.RIGHT, new Insets(3,3,3,3)));
			jmbar.add(jmenu);
			jmenu.add(jmitem);
//			jbtn.setIcon(icon);
			add(jbtn, new GroupFlowLayoutConstraints(SwingConstants.LEFT, new Insets(3,3,3,3)));
			add(jmbar, new GroupFlowLayoutConstraints(SwingConstants.LEFT, new Insets(3,3,3,3)));
//			slider.addChangeListener(this);
//			slider.setMaximum(255);
//			slider.setMinimum(0);
//			slider.setOpaque(false);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
		}

		public void stateChanged(ChangeEvent e)
		{
//			c = new Color(slider.getValue(), 0, 0);
			repaint();
		}
	}
}
