package com.systex.sop.cvs.task;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.util.HostnameUtil;

public class VerifyWriteCallable implements Callable<TaskResult> {
	private String version;		// 版號
	private String filename;	// 檔案名稱
	private String module;		// 模組名稱
	
	public VerifyWriteCallable(String version, String filename, String module) {
		this.version = version;
		this.filename = filename;
		this.module = module;
	}
	
	@Override
	public TaskResult call() throws Exception {
		TaskResult result = new TaskResult();
		
		CVSParserLogic logic = new CVSParserLogic();
		File f = new File(CVSFunc.fxVerifyFilePath(filename, version));
		try {
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));				// [START]
			
			/** 進行寫入 **/
			logic.parserPerFile(new TaskSyncResult(), HostnameUtil.getHostname(), module, f, false);
			
			result.setIsDone(Boolean.TRUE);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			result.setIsDone(Boolean.FALSE);
			throw e;
		}finally{
			result.setEndedTime(new Timestamp(System.currentTimeMillis()));				// [ENDED]
			f = null;
		}
		
		return result;
	}

}
