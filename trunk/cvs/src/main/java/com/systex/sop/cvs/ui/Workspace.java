package com.systex.sop.cvs.ui;

import java.awt.BorderLayout;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

public class Workspace {
	public enum PAGE { SYNC_CVS, RESET_LOGIN, QRY_CLASSIC, QRY_NORMAL, ENV_CHK, SYS_INFO };
	private static JPanel body;
	private static ConcurrentHashMap<PAGE, JPanel> pageMap = new ConcurrentHashMap<PAGE, JPanel>();
	private static PAGE currentPage = null;
	
	public static void changePage(PAGE page) {
		body.removeAll();
		body.add(getPage(page), BorderLayout.CENTER);
		body.updateUI();
		currentPage = page;
	}

	public static PAGE getCurrentPage() {
		return currentPage;
	}
	
	public static JPanel getPage(PAGE page) {
		if (!pageMap.containsKey(page)) {
			registerPage(page);
		}
		
		return pageMap.get(page);
	}
	
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
