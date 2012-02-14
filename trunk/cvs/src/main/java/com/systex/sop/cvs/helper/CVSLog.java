package com.systex.sop.cvs.helper;

import org.apache.log4j.Logger;

public class CVSLog {
	private static Logger defaultLogger = null;
	
	static {
//		File f = new File(PropReader.getPropertyHome(), "log4j.properties");
//		PropertyConfigurator.configure(f.getAbsolutePath());
		defaultLogger =  Logger.getLogger(CVSLog.class);
	}
	
	public static Logger getLogger() {
		return defaultLogger;
	}
	
}
