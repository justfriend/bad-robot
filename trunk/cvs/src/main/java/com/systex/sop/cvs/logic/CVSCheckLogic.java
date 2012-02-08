package com.systex.sop.cvs.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.quartz.CronScheduleBuilder;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.constant.CVSConst.CMDTYPE;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.helper.CVSModuleHelper;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StreamCloseHelper;
import com.systex.sop.cvs.util.StringUtil;

public class CVSCheckLogic {
	private Map<CVSConst.CMDTYPE, Boolean> resultMap = new HashMap<CVSConst.CMDTYPE, Boolean>();
	
	/** 檢查CVS.EXE是否可執行 **/
	public boolean chkCVS() {
		ProcessBuilder pb = new ProcessBuilder("cvs.exe");
		pb.redirectErrorStream(true);
		
		// process resource
		Process p = null;
		
		// input stream resource
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		// output stream resource
		StringBuffer out = new StringBuffer();
		
		String line = null;
		try {
			p = pb.start();
			is = p.getInputStream();
			isr = new InputStreamReader(is, CVSConst.ENCODING_IN);
			br = new BufferedReader(isr);
			
			while ((line = br.readLine()) != null) {
				if (StringUtil.isEmpty(line) || line.startsWith("?")) continue;
				out.append(line);
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			return false;
		}finally{
			StreamCloseHelper.closeReader(br, isr);
			StreamCloseHelper.closeInputStream(is);
			if (p != null) p.destroy();
		}
		
		if (out.indexOf("Usage:") >= 0) 
			return true;
		else
			return false;
	}
	
	/** 檢查各模組路徑是否皆控管於CVS下 **/
	public boolean chkModulePath() {
		CVSModuleHelper h = new CVSModuleHelper();
		Map<String, String> map = h.getMap();
		
		if (map == null || map.size() < 1) {
			CVSLog.getLogger().error("module path must be set in MODULE.properties");
			return false;
		}
		
		for (String module : map.keySet()) {
			File f = new File(map.get(module) + "\\CVS\\Root");
			if (!f.isFile()) {
				CVSLog.getLogger().error(f.getAbsolutePath() + " not exist");
				return false;
			}
		}
		
		return true;
	}
	
	/** 檢查取得的CVS LOG路徑是否存在(自動建立) **/
	public boolean chkLogPath() {
		String path = PropReader.getProperty("CVS.LOG_PATH");
		if (StringUtil.isEmpty(path)) {
			CVSLog.getLogger().error("CVS.LOG_PATH must be set");
			return false;
		}
		
		File f = new File(path);
		if (f.isDirectory()) {
			return true;
		}else{
			if (f.mkdirs())
				return true;
			else
				return false;
		}
	}
	
	/** 檢查DB連線 **/
	public boolean chkDBConn() {
		Session session = null;

		try {
			session = SessionUtil.openSession(); // 此階段若有失敗不會收到 EXCEPTION(未被扔出)，必須真的做查詢才會發現
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.list();
			return true;
		} catch (Exception e) {
			CVSLog.getLogger().error(this, e);
			return false;
		} finally {
			SessionUtil.closeSession(session);
		}
	}
	
	/** 檢查CRON設定 **/
	public boolean chkCron() {
		try {
			CronScheduleBuilder.cronSchedule(PropReader.getProperty("CVS.CRONTAB"));
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Entry point
	 * <p>
	 * 進行環境檢查
	 * @return
	 */
	public Map<CVSConst.CMDTYPE, Boolean> execute() {
		resultMap.put(CMDTYPE.CHK_CVS, chkCVS());
		resultMap.put(CMDTYPE.CHK_MODULE, chkModulePath());
		resultMap.put(CMDTYPE.CHK_LOGPATH, chkLogPath());
		resultMap.put(CMDTYPE.CHK_DBCONN, chkDBConn());
		resultMap.put(CMDTYPE.CHK_CRON, chkCron());
		
		return resultMap;
	}
	
	public static void main (String [] args) {
		CVSCheckLogic app = new CVSCheckLogic();
		System.err.println (app.execute());
	}
}
