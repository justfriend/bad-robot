package com.systex.sop.cvs.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.util.HostnameUtil;
import com.systex.sop.cvs.util.StreamCloseHelper;

public class WriteCallable implements Callable<TaskSyncResult> {
	private String module;
	private Timestamp edate;
	private boolean isFullSync;
	
	public WriteCallable(String module, Timestamp edate, boolean isFullSync) {
		this.module = module;
		this.edate = edate;
		this.isFullSync = isFullSync;
	}
	
	@Override
	public TaskSyncResult call() throws Exception {
		CVSLog.getLogger().fatal("[BEGIN]" + module);
		TaskSyncResult result = null;
		
		synchronized(WriteCallable.class) {
			result = TaskSyncResult.getTaskResult(module);
			if (result == null) {
				result = new TaskSyncResult();
				result.setModule(module);
				TaskSyncResult.putTaskResult(module, result);					// keep ref. by itself
			}
		}
		
		CVSParserLogic logic = new CVSParserLogic();
		File f = new File(CVSFunc.fxLogFilePath(module, edate));
		try {
			result.setBeginTime2(new Timestamp(System.currentTimeMillis()));			// [START]
			
			/** 取得總行數 **/
			FileInputStream fis = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			FileReader fr = null;
			LineNumberReader lnr = null;
			try {
				fr = new FileReader(f);
				lnr = new LineNumberReader(fr);
				lnr.skip(f.length());
				result.setTotalLines2(lnr.getLineNumber());
			}catch(Exception e){
				CVSLog.getLogger().error("無法取得檔案總行數, FILE is " + ((f == null)? "null": f.getAbsolutePath()) );
				CVSLog.getLogger().error(this, e);
				result.setTotalLines2(-1);
				result.setCurrentLine2(-1);
				throw e;
			}finally{
				StreamCloseHelper.closeReader(br, isr);
				StreamCloseHelper.closeInputStream(fis);
				StreamCloseHelper.closeReader(lnr, fr);
			}
			
			/** 進行寫入 **/
			logic.parserPerFile(result, HostnameUtil.getHostname(), module, f, isFullSync);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			result.setEndedTime2(new Timestamp(System.currentTimeMillis()));			// [ENDED]
			f = null;
			CVSLog.getLogger().fatal("[ENDED]" + module);
		}
		
		return result;
	}

}
