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
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = null;
		try {
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));	// [START]
			
			/** 取得總行數 **/
			FileReader fr = null;
			LineNumberReader lnr = null;
			try {
				fr = new FileReader(f);
				lnr = new LineNumberReader(fr);
				lnr.skip(f.length());
			}catch(Exception e){
				result.setErrMsg(e.getMessage());
				throw e;
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
				tmpList.add(line);
				CVSLog.getLogger().debug(line);
				if (line.startsWith(CVSConst.BLOCK_END)) {
					logic.parser(HostnameUtil.getHostname(), module, tmpList, false);
					tmpList.clear();
				}
			}
			
			result.setIsDone(Boolean.TRUE);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			result.setIsDone(Boolean.FALSE);
			throw e;
		}finally{
			result.setEndedTime(new Timestamp(System.currentTimeMillis()));	// [ENDED]
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(fis);
			f = null;
		}
		
		return result;
	}

}
