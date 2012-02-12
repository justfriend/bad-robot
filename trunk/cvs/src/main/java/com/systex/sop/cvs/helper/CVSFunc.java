package com.systex.sop.cvs.helper;

import java.sql.Timestamp;
import java.util.Calendar;

import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class CVSFunc {
	
	public static String [] fxCmdArray(Timestamp edate) {
		String [] cmdArray = new String[] { "cvs.exe", "-q", "log", "-S", "-d", "> " + TimestampHelper.convertToyyyyMMdd2(edate) };
		return cmdArray;
	}
	
	public static String [] fxCmdModifyArray(String version, String filepath, String comment) {
		String [] cmdArray = new String[] { "cvs.exe", "admin", "-m", StringUtil.concat(version, ":", comment, ""), filepath };
		return cmdArray;
	}
	
	public static String [] fxCmdVerifyArray(String version, String filepath) {
		String [] cmdArray = new String[] { "cvs.exe", "-q", "log", StringUtil.concat("-r", version), "-N", filepath };
		return cmdArray;
	}
	
	public static String fxLogFilePath(String module, Timestamp edate) {
		return StringUtil.concat(
				PropReader.getProperty("CVS.LOG_PATH"),
				TimestampHelper.convertToyyyyMMdd(edate), "_", module, ".log"
		);
	}
	
	public static String fxModifyFilePath() {
		return StringUtil.concat(
				PropReader.getProperty("CVS.LOG_PATH"),
				"Modify.log"
		);
	}
	
	public static String fxVerifyFilePath(String filename, String version) {
		return StringUtil.concat(
				PropReader.getProperty("CVS.LOG_PATH"),
				filename, "_", version, ".log"
		);
	}
	
	public static String fxModule(String modulePart, char clientServer) {
		return StringUtil.concat("sop-", modulePart, "-", (clientServer == '0')? "client": "server");
	}
	
	public static String fxToModule(String rcsfile) {
		return rcsfile.split("/")[2];
	}
	
	public static String fxExtraSource(String rcsfile) {
		if (rcsfile.indexOf("-server/") > 0) {
			return rcsfile.substring(rcsfile.indexOf("-server/") + 8);
		}else
		if (rcsfile.indexOf("-client/") > 0) {
			return rcsfile.substring(rcsfile.indexOf("-client/") + 8);
		}
		
		return null;
	}
	
	public static String fxElapseTime(long spendTime) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(spendTime);
		return StringUtil.concat(
				String.format("%02d", c.get(Calendar.MINUTE)), ":", 
				String.format("%02d", c.get(Calendar.SECOND)), ".",
				String.format("%03d", c.get(Calendar.MILLISECOND)) );
	}
}
