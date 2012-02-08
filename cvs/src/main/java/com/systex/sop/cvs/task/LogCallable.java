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

public class LogCallable implements Callable<TaskResult> {
	private String module;
	private String path;
	private Timestamp edate;
	
	public LogCallable(String module, String path, Timestamp edate) {
		this.module = module;
		this.path = path;
		this.edate = edate;
	}
	
	@Override
	public TaskResult call() throws Exception {
		TaskResult result = TaskResult.getResultMap().get(module);
		result = (result == null)? new TaskResult(): result;
		result.setModule(module);
		TaskResult.getResultMap().put(module, result);	// keep ref. by itself
		
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
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));	// [START]
			p = pb.start();
			is = p.getInputStream();
			isr = new InputStreamReader(is, CVSConst.ENCODING_IN);
			br = new BufferedReader(isr);
			result.setTotalLines(-1);
			
			os = new FileOutputStream(new File(CVSFunc.fxLogFilePath(module, edate)));
			osw = new OutputStreamWriter(os, CVSConst.ENCODING_OUT);
			wr = new BufferedWriter(osw);
			
			while ((line = br.readLine()) != null) {
				result.setCurrentLine(++currentLine);
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				wr.write(line);
				wr.newLine(); // same as wr.write(System.getProperty("line.separator"));
				
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			result.setEndedTime(new Timestamp(System.currentTimeMillis()));	// [ENDED]
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(is);
			StreamCloseHelper.closeWriter(wr, osw);
			StreamCloseHelper.closeOutputStream(os);
			if (p != null) p.destroy();
		}

		return result;
	}
	
}
