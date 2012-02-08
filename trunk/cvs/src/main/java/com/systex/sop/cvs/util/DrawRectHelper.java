package com.systex.sop.cvs.util;

import java.awt.Graphics;

public class DrawRectHelper {

	public static void drawRoundRect(Graphics g, int left, int right, int top, int bottom, int width, int height) {
		g.drawLine(2 + left, 0 + top, width - 3 - right, 0 + top); 									// 上面的線條
		g.drawLine(2 + left, height - 1 - bottom, width - 3 - right, height - 1 - bottom); 			// 下面的線條
		g.drawLine(0 + left, 2 + top, 0 + left, height - 3 - bottom); 								// 左邊的線條
		g.drawLine(width - 1 - right, 2 + top, width - 1 - right, height - 3 - bottom); 			// 右邊的線條
		g.drawLine(1 + left, 1 + top, 1 + left, 1 + top); 											// 左上角的點
		g.drawLine(1 + left, height - 2 - bottom, 1 + left, height - 2 - bottom); 					// 左下角的點
		g.drawLine(width - 2 - right, 1 + top, width - 2 - right, 1 + top); 						// 右上角的點
		g.drawLine(width - 2 - right, height - 2 - bottom, width - 2 - right, height - 2 - bottom); // 右下角的點
	}
	
}
