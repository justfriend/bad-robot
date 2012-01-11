package com.systex.sop.cvs.helper;

import java.sql.Timestamp;

import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class CVSFunc {
	
	public static String [] fxCmdArray(Timestamp edate) {
		return new String[] { "cvs.exe", "-q", "log", "-S", "-d", "> " + TimestampHelper.convertToyyyyMMdd2(edate) };
	}
	
	public static String fxLogFilePath(String module, Timestamp edate) {
		// D:\\Project\\SOPA_CVS\\SOPA_01_03_Standard\\CVS_LOG\\20120101_sop-ml-server.log
		return StringUtil.concat(
				PropReader.getProperty("CVS.LOG_PATH"),
				TimestampHelper.convertToyyyyMMdd(edate), "_", module, ".log"
		);
	}

}
