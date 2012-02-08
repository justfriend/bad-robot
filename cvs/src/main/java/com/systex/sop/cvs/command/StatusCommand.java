package com.systex.sop.cvs.command;

import com.badrobot.ui.containers.BadTrafficLabel;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;

public abstract class StatusCommand {
	protected BadTrafficLabel c;

	protected StatusCommand(BadTrafficLabel c) {
		this.c = c;
	}

	public abstract CMD_RESULT execute();

	public CMD_RESULT refresh() {
		CMD_RESULT result = execute();
		c.setLight(result.getLight());
		c.repaint();

		return result;
	}
}
