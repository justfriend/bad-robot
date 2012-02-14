package com.systex.sop.cvs.helper;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.systex.sop.cvs.util.PropReader;

public class CVSLog {
	private static Logger defaultLogger = null;
	
	static {
		File f = new File(PropReader.getPropertyHome(), "log4j.properties");
		PropertyConfigurator.configure(f.getAbsolutePath());
		defaultLogger =  Logger.getLogger(CVSLog.class);
	}
	
	public static Logger getLogger() {
		return defaultLogger;
	}
	
}
