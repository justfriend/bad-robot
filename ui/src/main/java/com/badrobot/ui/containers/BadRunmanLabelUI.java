package com.badrobot.ui.containers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

public class BadRunmanLabelUI extends BasicLabelUI {
	private JLabel c = null;
	
	public BadRunmanLabelUI(JLabel c) {
		this.c = c;
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g;
		Dimension d = getPreferredSize(c);
		try {
			BufferedImage bufImg = ImageIO.read(new File("f://ani12.gif"));
			g2d.drawImage(bufImg, null, 10, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
