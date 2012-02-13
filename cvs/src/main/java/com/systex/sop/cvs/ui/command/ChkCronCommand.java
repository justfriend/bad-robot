package com.systex.sop.cvs.ui.command;

import org.quartz.CronScheduleBuilder;

import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel;
import com.systex.sop.cvs.util.PropReader;

public class ChkCronCommand extends BaseCommand {

	protected ChkCronCommand(SSSTrafficLabel c) {
		super(c);
	}

	@Override
	public CMD_RESULT execute() {
		try {
			CronScheduleBuilder.cronSchedule(PropReader.getProperty("CVS.CRONTAB"));
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			return CMD_RESULT.FAILURE;
		}

		return CMD_RESULT.SUCCESS;
	}

}
