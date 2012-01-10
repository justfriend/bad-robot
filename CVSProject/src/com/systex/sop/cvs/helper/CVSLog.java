package com.systex.sop.cvs.helper;

import org.apache.log4j.Logger;

public class CVSLog {
	private static Logger defaultLogger = Logger.getLogger(CVSLog.class);
	
	public static Logger getLogger() {
		return defaultLogger;
	}
	
}
