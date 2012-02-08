package com.systex.sop.cvs.ui.customize.comp;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.systex.sop.cvs.ui.StartUI;

@SuppressWarnings("serial")
public class SSSImgJPanel extends JPanel {
	private Image img = null;

	public SSSImgJPanel() {
		super();
	}
	
	public SSSImgJPanel(Image img) {
		this.img = img;
	}

	public SSSImgJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public SSSImgJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public SSSImgJPanel(LayoutManager layout) {
		super(layout);
	}
	
	{
		if (img == null) {
			try {
				img = ImageIO.read(StartUI.class.getResource("/resource/grayGridBG.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (img != null) {
			int w = getWidth();
			int h = getHeight();
			int imgWidth = img.getWidth(null);
			int imgHeight = img.getHeight(null);
			for (int y = 0; y < h/imgHeight + 1; y++) {
				for (int x = 0; x < w/imgWidth + 1; x++) {
					g.drawImage(img, x * imgWidth, y * imgHeight, imgWidth, imgHeight, null);
				}
			}
		}
	}
}
