package com.systex.sop.cvs.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;

public class LogCallable implements Callable<TaskSyncResult> {
	private String module;
	private String path;
	private Timestamp edate;
	
	public LogCallable(String module, String path, Timestamp edate) {
		this.module = module;
		this.path = path;
		this.edate = edate;
	}
	
	@Override
	public TaskSyncResult call() throws Exception {
		TaskSyncResult result = null;
		synchronized(LogCallable.class) {
			result = TaskSyncResult.getTaskResult(module);
			if (result == null) {
				result = new TaskSyncResult();
				result.setModule(module);
				TaskSyncResult.putTaskResult(module, result);							// keep ref. by itself
			}
		}
		
		/**
		 *  收集更新資訊 (CVS LOG) 並輸至指定路徑 (LOG PATH)
		 */
		ProcessBuilder pb = new ProcessBuilder(CVSFunc.fxCmdArray(edate));
		pb.directory(new File(path));
		pb.redirectErrorStream(true);
		
		// process resource
		Process p = null;
		
		// input stream resource
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		// output stream resource
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter wr = null;
		
		String line = null;
		int currentLine = 0;
		try {
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));				// [START]
			p = pb.start();
			is = p.getInputStream();
			isr = new InputStreamReader(is, CVSConst.ENCODING_IN);
			br = new BufferedReader(isr);
			result.setTotalLines(-1);													// 陸陸續讀入故無法取得總行數
			
			os = new FileOutputStream(new File(CVSFunc.fxLogFilePath(module, edate)));
			osw = new OutputStreamWriter(os, CVSConst.ENCODING_OUT);
			wr = new BufferedWriter(osw);
			
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				result.setCurrentLine(++currentLine);
				wr.write(line);
				wr.newLine();
				CVSLog.getLogger().debug(line);
				if (line.startsWith(CVSConst.BLOCK_END)) {								// 每次遇換筆行時檢查執行服務是否已要求中斷
					if (LogFutureTask.getInstance().getService().isShutdown() ||
						LogFutureTask.getInstance().getService().isTerminated()) {
						CVSLog.getLogger().info( module + " 已中斷");
						return result;
					}
				}
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			result.setEndedTime(new Timestamp(System.currentTimeMillis()));				// [ENDED]
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(is);
			StreamCloseHelper.closeWriter(wr, osw);
			StreamCloseHelper.closeOutputStream(os);
			if (p != null) p.destroy();
		}

		return result;
	}
	
}
