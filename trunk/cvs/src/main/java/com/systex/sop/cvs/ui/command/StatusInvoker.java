package com.systex.sop.cvs.ui.command;

import java.util.HashMap;
import java.util.Map;

import com.systex.sop.cvs.constant.CVSConst.CMDTYPE;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;


public class StatusInvoker {
	private static StatusInvoker instance;
	private Map<CMDTYPE, BaseCommand> cmdMap = new HashMap<CMDTYPE, BaseCommand>();
	
	private StatusInvoker() {};
	
	public static StatusInvoker getInstance() {
		if (instance == null) {
			instance = new StatusInvoker();
		}
		return instance;
	}
	
	
	public void setCommand(CMDTYPE type, BaseCommand cmd) {
		cmdMap.put(type, cmd);
	}
	
	public Map<CMDTYPE, CMD_RESULT> refresh(CMDTYPE ... types) {
		Map<CMDTYPE, CMD_RESULT> resultMap = new HashMap<CMDTYPE, CMD_RESULT>();
		
		for (CMDTYPE type : types) {
			if (!cmdMap.containsKey(type)) throw new RuntimeException(type + " (key) do not found");
		}
		
		for (CMDTYPE type : types) {
			resultMap.put(type, cmdMap.get(type).refresh());
		}
		
		return resultMap;
	}
}
