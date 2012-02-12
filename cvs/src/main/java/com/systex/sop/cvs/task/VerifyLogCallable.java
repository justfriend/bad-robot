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

public class VerifyLogCallable implements Callable<TaskResult> {
	private String workdir;		// 工作目錄
	private String filepath;	// 檔案路徑
	private String version;		// 版號
	private String filename;	// 檔案名稱
	
	public VerifyLogCallable(String workdir, String filepath, String version, String filename) {
		this.workdir = workdir;
		this.filepath = filepath;
		this.version = version;
		this.filename = filename;
	}
	
	@Override
	public TaskResult call() throws Exception {
		TaskResult result = new TaskResult();
		
		/**
		 *  進行提示註記更新
		 */
		ProcessBuilder pb = new ProcessBuilder(CVSFunc.fxCmdVerifyArray(version, filepath));
		pb.directory(new File(workdir));
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
		try {
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));	// [START]
			p = pb.start();
			is = p.getInputStream();
			isr = new InputStreamReader(is, CVSConst.ENCODING_IN);
			br = new BufferedReader(isr);
			
			os = new FileOutputStream(new File(CVSFunc.fxVerifyFilePath(filename, version)));
			osw = new OutputStreamWriter(os, CVSConst.ENCODING_OUT);
			wr = new BufferedWriter(osw);
			
			// 輸出 結果
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				wr.write(line);
				wr.newLine();
			}
			
			result.setIsDone(Boolean.TRUE);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			result.setIsDone(Boolean.FALSE);
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
