package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

/**
 * 管理頁面之控制類別
 * <p>
 *
 */
public class Workspace {
	public enum PAGE { SYNC_CVS, RESET_LOGIN, QRY_CLASSIC, QRY_NORMAL, ENV_CHK, SYS_INFO };
	private static JPanel body;
	private static ConcurrentHashMap<PAGE, JPanel> pageMap = new ConcurrentHashMap<PAGE, JPanel>();
	private static PAGE currentPage = null;
	
	/** 切換頁面 **/
	public static void changePage(PAGE page) {
		body.removeAll();
		body.add(getPage(page), BorderLayout.CENTER);
		body.updateUI();
		currentPage = page;
	}
	
	/** 取得當前頁面定義 **/
	public static PAGE getCurrentPageEnum() {
		return currentPage;
	}
	
	/** 取得頁面 **/
	public static JPanel getPage(PAGE page) {
		if (!pageMap.containsKey(page)) {
			registerPage(page);
		}
		
		return pageMap.get(page);
	}
	
	/** 註記頁面的容器板 **/
	public static void registerBody(JPanel panel) {
		body = panel;
		body.setLayout(new BorderLayout(0, 0));
		body.setBorder(null);
	}
	
	private static void registerPage(PAGE page) {
		if (!pageMap.containsKey(page)) {
			JPanel comp = null;
			switch (page) {
			case SYNC_CVS:
				comp = new SyncPage();
				break;
			case RESET_LOGIN:
				comp = new ResetPage();
				break;
			case QRY_CLASSIC:
				comp = new QueryClassicPage();
				break;
			case QRY_NORMAL:
				comp = new QueryNormalPage();
				break;
			case ENV_CHK:
				break;
			case SYS_INFO:
				break;
			default:
				break;
			}
			pageMap.put(page, comp);
		}
	}

}
