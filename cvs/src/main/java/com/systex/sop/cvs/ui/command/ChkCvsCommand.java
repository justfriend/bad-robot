package com.systex.sop.cvs.ui.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.ui.customize.comp.SSSTrafficLabel;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;

public class ChkCvsCommand extends BaseCommand {

	protected ChkCvsCommand(SSSTrafficLabel c) {
		super(c);
	}

	@Override
	public CMD_RESULT execute() {
		ProcessBuilder pb = new ProcessBuilder("cvs.exe");
		pb.redirectErrorStream(true);

		// process resource
		Process p = null;

		// input stream resource
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		// output stream resource
		StringBuffer out = new StringBuffer();

		String line = null;
		try {
			p = pb.start();
			is = p.getInputStream();
			isr = new InputStreamReader(is, CVSConst.ENCODING_IN);
			br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				out.append(line);
			}
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			return CMD_RESULT.FAILURE;
		} finally {
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(is);
			if (p != null)
				p.destroy();
		}

		if (out.indexOf("Usage:") >= 0)
			return CMD_RESULT.SUCCESS;
		else
			return CMD_RESULT.FAILURE;
	}

}
