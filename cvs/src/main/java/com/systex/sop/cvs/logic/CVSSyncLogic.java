package com.systex.sop.cvs.logic;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.schedular.CVSSchedularManager;

public class CVSSyncLogic {
	private CVSCheckLogic chkLogic = new CVSCheckLogic();
	
	public boolean schedularTurnON() throws Exception {
		if (!chkLogic.chkCron()) throw new Exception("check CRON failure");
		
		try {
			CVSSchedularManager.getInstance().start();
			return true;
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			return false;
		}
	}
	
	public boolean schedularTurnOFF() throws Exception {
		try {
			CVSSchedularManager.getInstance().suspend();
			return true;
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			return false;
		}
	}

}
