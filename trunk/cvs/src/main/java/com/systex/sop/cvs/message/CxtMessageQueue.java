package com.systex.sop.cvs.message;

import java.util.concurrent.LinkedBlockingQueue;

import com.systex.sop.cvs.helper.CVSLog;

/**
 * 框架訊息佇列
 * <p>
 *
 */
public class CxtMessageQueue {
	private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	public static LinkedBlockingQueue<String> getQueue() {
		return queue;
	}
	
	public static void addCxtMessage(String msg) {
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			CVSLog.getLogger().error(CxtMessageQueue.class, e);
		}
	}
}
