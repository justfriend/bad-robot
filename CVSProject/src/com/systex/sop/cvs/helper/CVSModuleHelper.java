package com.systex.sop.cvs.helper;

import java.util.HashMap;
import java.util.Map;

import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.StringUtil;

public class CVSModuleHelper {
	
	/** key is module name, value is module path **/
	private Map<String, String> map = new HashMap<String, String>();
	
	public CVSModuleHelper() {
		load();
	}
	
	public void load() {
		for (int i=0; i<100; i++) {
			String key = String.format("%02d", i);
			String value = PropReader.getProperty("MODULE." + key);
			if (StringUtil.isEmpty(value)) continue;
			String name = value.split(";", 2)[0].trim();
			String path = value.split(";", 2)[1].trim();
			map.put(name, path);
		}
		
		CVSLog.getLogger().info("[MODULE LOAD]" + map);
	}
	
	public Map<String, String> getMap() {
		return map;
	}
	
	public static void main(String [] args) throws Exception {
		CVSModuleHelper app = new CVSModuleHelper();
		app.load();
	}
	
}
