package com.systex.sop.cvs.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;

public class WriteCallable implements Callable<TaskResult> {
	private String module;
	private Timestamp edate;
	
	public WriteCallable(String module, Timestamp edate) {
		this.module = module;
		this.edate = edate;
	}
	
	@Override
	public TaskResult call() throws Exception {
		CVSParserLogic logic = new CVSParserLogic();
		TaskResult result = new TaskResult();
		result.setModule(this.module);
		
		File f = new File(CVSFunc.fxLogFilePath(module, edate));
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String line = null;
		try {
			result.setBeginTime(new Timestamp(System.currentTimeMillis()));	// [START]
			fis = new FileInputStream(f);
			isr = new InputStreamReader(fis, CVSConst.ENCODING_OUT);
			br = new BufferedReader(isr);
			List<String> tmpList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line)) continue;
				tmpList.add(line);
				if (line.startsWith("=============================================================================")) {
					logic.parser("1000073", module, tmpList);
					tmpList.clear();
				}
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			e.printStackTrace();
		}finally{
			result.setEndedTime(new Timestamp(System.currentTimeMillis()));	// [ENDED]
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(fis);
			f = null;
		}
		
		return result;
	}

}
