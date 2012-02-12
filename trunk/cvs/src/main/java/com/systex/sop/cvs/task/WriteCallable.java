package com.systex.sop.cvs.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.util.HostnameUtil;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;

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
		TaskSyncResult result = null;
		synchronized(WriteCallable.class) {
			result = TaskSyncResult.getTaskResult(module);
			if (result == null) {
				result = new TaskSyncResult();
				result.setModule(module);
				TaskSyncResult.putTaskResult(module, result);	// keep ref. by itself
			}
		}
		
		CVSParserLogic logic = new CVSParserLogic();
		File f = new File(CVSFunc.fxLogFilePath(module, edate));
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = null;
		int currentLine = 0;
		try {
			result.setBeginTime2(new Timestamp(System.currentTimeMillis()));	// [START]
			
			/** 取得總行數 **/
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
			}finally{
				StreamCloseHelper.closeReader(lnr, fr);
			}
			
			/** 進行寫入 **/
			fis = new FileInputStream(f);
			isr = new InputStreamReader(fis, CVSConst.ENCODING_OUT);
			br = new BufferedReader(isr);
			List<String> tmpList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line)) continue;
				result.setCurrentLine2(++currentLine);
				tmpList.add(line);
				CVSLog.getLogger().debug(line);
				if (line.startsWith(CVSConst.BLOCK_END)) {
					logic.parser(HostnameUtil.getHostname(), module, tmpList, isFullSync);
					tmpList.clear();
				}
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			result.setEndedTime2(new Timestamp(System.currentTimeMillis()));	// [ENDED]
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(fis);
			f = null;
		}
		
		return result;
	}

}