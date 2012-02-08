package com.systex.sop.cvs.command;

import java.io.File;
import java.util.Map;

import com.badrobot.ui.containers.BadTrafficLabel;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;

public class ChkModuleCommand extends StatusCommand {

	protected ChkModuleCommand(BadTrafficLabel c) {
		super(c);
	}

	@Override
	public CMD_RESULT execute() {
		CVSModuleHelper h = new CVSModuleHelper();
		Map<String, String> map = h.getMap();

		if (map == null || map.size() < 1) {
			CVSLog.getLogger().error("module path must be set in MODULE.properties");
			return CMD_RESULT.FAILURE;
		}

		for (String module : map.keySet()) {
			File f = new File(map.get(module) + "\\CVS\\Root");
			if (!f.isFile()) {
				CVSLog.getLogger().error(f.getAbsolutePath() + " not exist");
				return CMD_RESULT.FAILURE;
			}
		}

		return CMD_RESULT.SUCCESS;
	}

}
