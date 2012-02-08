package com.systex.sop.cvs.command;

import java.util.HashMap;
import java.util.Map;

import com.systex.sop.cvs.constant.CVSConst.CMDTYPE;
import com.systex.sop.cvs.constant.CVSConst.CMD_RESULT;


public class StatusInvoker {
	private Map<CMDTYPE, StatusCommand> cmdMap = new HashMap<CMDTYPE, StatusCommand>();
	
	public void setCommand(CMDTYPE type, StatusCommand cmd) {
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
