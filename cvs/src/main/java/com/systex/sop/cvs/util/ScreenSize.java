package com.systex.sop.cvs.util;

import java.awt.Toolkit;

/**
 * Screen resolution helper
 * <p>
 * 取得客戶端螢幕解析度並提供讓視窗置中對齊。
 * <p>
 * Modify history : <br>
 * ================================ <br>
 * 2012/01/16	.[- _"].	release
 * <p>
 */
public class ScreenSize {
	
	public static int getScreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
	public static int getScreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	public static int getDynamicScreenWidth(double widthRatio) {
		return (int) ((double) getScreenWidth() * widthRatio);
	}
	
	public static int getDynamicScreenHeight(double heightRatio) {
		return (int) ((double) getScreenHeight() * heightRatio);
	}
	
	public static int getAdjustScreenWidth(double windowWidth) {
		try {
			return (int) ((getScreenWidth() / 2) - (windowWidth / 2));
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getAdjustScreenHeight(double windowHeight) {
		try {
			return (int) ((getScreenHeight() / 2) - (windowHeight / 2));
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

}
