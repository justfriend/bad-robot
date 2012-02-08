package com.systex.sop.cvs.util;

import java.net.InetAddress;

import com.systex.sop.cvs.helper.CVSLog;

public class HostnameUtil {
	
	public static String getHostname() {
		String hostname = "unknow";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		}catch(Exception e){
			CVSLog.getLogger().error(HostnameUtil.class, e);
		}
		
		return hostname;
	}
	
}
