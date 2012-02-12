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
import com.systex.sop.cvs.dao.CVSQueryDAO;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.HostnameUtil;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class ModifyCallable implements Callable<TaskResult> {
	private CVSQueryDAO dao = new CVSQueryDAO();
	private Long rcsid;
	private String workdir;		// 工作目錄
	private String filepath;	// 檔案路徑
	private String version;		// 版號
	private String comment;		// 註記 (新)
	
	public ModifyCallable(Long rcsid, String workdir, String filepath, String version, String comment) {
		this.rcsid = rcsid;
		this.workdir = workdir;
		this.filepath = filepath;
		this.version = version;
		this.comment = comment;
	}
	
	@Override
	public TaskResult call() throws Exception {
		TaskResult result = new TaskResult();
		result.setIsDone(Boolean.FALSE);
		
		/**
		 *  進行提示註記更新
		 */
		ProcessBuilder pb = new ProcessBuilder(CVSFunc.fxCmdModifyArray(version, filepath, comment));
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
			
			os = new FileOutputStream(new File(CVSFunc.fxModifyFilePath()), true);	// APPEND
			osw = new OutputStreamWriter(os, CVSConst.ENCODING_OUT);
			wr = new BufferedWriter(osw);
			
			// 輸出 HEADER
			wr.write(CVSConst.BLOCK_END);
			wr.newLine();
			wr.write(StringUtil.concat("[NOWTIME] ", TimestampHelper.now()));
			wr.newLine();
			wr.write(StringUtil.concat("[WORKDIR] ", workdir));
			wr.newLine();
			wr.write(StringUtil.concat("[FL.PATH] ", filepath));
			wr.newLine();
			wr.write(StringUtil.concat("[VERSION] ", version));
			wr.newLine();
			wr.write(StringUtil.concat("[HOSTNME] ", HostnameUtil.getHostname()));
			wr.newLine();
			wr.write(StringUtil.concat("[COMMENT] ", comment));
			wr.newLine();
			wr.write(CVSConst.SPLIT_DESC);
			wr.newLine();
			
			// 輸出 結果
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				wr.write(line);
				wr.newLine(); // same as wr.write(System.getProperty("line.separator"));
				if (line.startsWith("done")) result.setIsDone(Boolean.TRUE);
			}
			
			// 更新資料庫
			result.setIsDone(null);						// NULL 代表更新CVS成功但更新資料庫失敗
			dao.updateVER(rcsid, version, comment);
			result.setIsDone(Boolean.TRUE);				// 標回成功
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
