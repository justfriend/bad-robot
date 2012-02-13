package com.systex.sop.cvs.ui.command;

import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel;

public abstract class BaseCommand {
	protected SSSTrafficLabel c;

	protected BaseCommand(SSSTrafficLabel c) {
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
