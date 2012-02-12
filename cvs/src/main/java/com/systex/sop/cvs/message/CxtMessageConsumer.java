package com.systex.sop.cvs.message;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.customize.comp.SSSJFrameBase;
import com.systex.sop.cvs.util.ThreadHelper;

public class CxtMessageConsumer implements Runnable {
	private SSSJFrameBase frame = null;
	
	public CxtMessageConsumer(SSSJFrameBase frame) {
		this.frame = frame;
	}

	@Override
	public void run() {
		String msg = null;
		try {
			while( (msg = CxtMessageQueue.getQueue().take()) != null) {
				frame.setCxtMessage(msg);
				ThreadHelper.sleep(1500);
				frame.setCxtMessage("");
			}
		} catch (InterruptedException e) {
			CVSLog.getLogger().error(this, e);
		}
	}

}
