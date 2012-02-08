package com.systex.sop.cvs.command;

import org.quartz.SchedulerException;

import com.badrobot.ui.containers.BadTrafficLabel;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.schedular.CVSSchedularManager;

public class ChkSchedularCommand extends StatusCommand {

	protected ChkSchedularCommand(BadTrafficLabel c) {
		super(c);
	}

	@Override
	public CMD_RESULT execute() {
		
		try {
			if (CVSSchedularManager.getInstance().isStarted()) {
				return CMD_RESULT.SUCCESS;
			}else{
				return CMD_RESULT.WARNING;
			}
		} catch (SchedulerException e) {
			CVSLog.getLogger().error(this, e);
			return CMD_RESULT.FAILURE;
		}
	}

}
