package com.systex.sop.cvs.logic;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.systex.sop.cvs.dao.CVSParserDAO;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class CVSParserLogic {
//	private CommonDAO dao = new CommonDAO();
	private CVSParserDAO dao = new CVSParserDAO();
	
	static class VERTAG {
		public BigDecimal version;
		public String tagname;
	}
	
	static class VERDESC {
		public BigDecimal revision;
		public Timestamp date;
		public String author;
		public String state;
		public String filename;
		public String desc_ID;
		public String desc_DESC;
		public String desc_STEP;
		public StringBuffer rawdesc = new StringBuffer();
	}
	
	private void checkParserData(List<String> lineList) {
		if (lineList == null || lineList.size() < 1) {
			throw new RuntimeException("傳入的解析資料為空");
		}
		
		if (lineList.size() < 13) {
			for (String line : lineList) CVSLog.getLogger().info(line);
			throw new RuntimeException("解析資料的行數至少應有13行");
		}
		
		if (lineList.get(0).startsWith("RCS file") == false) {
			throw new RuntimeException("解析資料的第一行應為「RCS file」開頭");
		}
	}
	
	private static String fxProgramID(String rcsfile) {
		String programid = null;
		String [] pArray = rcsfile.split("/");
		for (int i=0; i<pArray.length; i++) {
			if ("view".equalsIgnoreCase(pArray[i]) || "module".equalsIgnoreCase(pArray[i])) {
				programid = pArray[i+1];
			}
		}
		return (programid == null)? " ": programid;
	}
	
	private static char fxClientServer(String module) {
		char csCode = ' ';
		String cs = module.split("-")[2];
		if ("client".equalsIgnoreCase(cs)) {
			csCode = '0';
		}else
		if ("server".equalsIgnoreCase(cs)) {
			csCode = '1';
		}
		
		return csCode;
	}
	
	private static List<VERTAG> fxCollectTagList(List<String> lineList, int beginLine) {
		List<VERTAG> tagList = new ArrayList<VERTAG>();
		for (int i = beginLine; i < lineList.size(); i++) {
			String line = lineList.get(i);
			if (line.startsWith("keyword")) { break; }
			VERTAG tag = new VERTAG();
			tag.tagname = line.split(": ")[0].trim();
			tag.version = new BigDecimal(line.split(": ")[1]);
			tagList.add(tag);
		}
		
		return tagList;
	}
	
	private static List<VERDESC> fxCollectDescList(List<String> lineList, int beginLine) {
		List<VERDESC> verdescList = new ArrayList<VERDESC>();
		VERDESC verdesc = null;
		boolean isDescArea = false;
		for (int i = beginLine; i < lineList.size(); i++) {
			String line = lineList.get(i);
			
			if (isDescArea == true) {
				if (line.startsWith("----------------------------") ||
					line.startsWith("=============================================================================")) {
					isDescArea = false;
				}else{
					verdesc.rawdesc.append(line).append(System.getProperty("line.separator"));
				}
				continue;
			}
			
			if (line.startsWith("revision")) {
				verdesc = new VERDESC();
				verdesc.revision = new BigDecimal(line.substring(9));
				verdescList.add(verdesc);
				continue;
			}
			
			if (line.startsWith("date:")) {
				Map<String, String> kvMap = new HashMap<String, String>();
				String [] kvArray = line.split(";");
				for (String kv : kvArray) {
					String [] keyVal = kv.split(": ", 2);
					kvMap.put(keyVal[0].trim(), keyVal[1].trim());
				}
				verdesc.date = TimestampHelper.cvsdate2Ts(kvMap.get("date"));
				verdesc.author = kvMap.get("author");
				verdesc.state = kvMap.get("state");
				verdesc.filename = kvMap.get("filename");
				isDescArea = true;
			}
		} // for
		
		// Extract ID, DESC and STEP
		for (VERDESC vd : verdescList) {
			Map<String, String> descMap = new HashMap<String, String>();
			String [] descArray = vd.rawdesc.toString().split(";");
			for (String desc : descArray) {
				String [] kvArray = desc.split(":");
				if (kvArray.length == 2) {
					descMap.put(kvArray[0].trim().toUpperCase(), kvArray[1].trim());
				}
			}
			vd.desc_ID = (descMap.containsKey("ID"))? descMap.get("ID"): "";
			vd.desc_DESC = (descMap.containsKey("DESC"))? descMap.get("DESC"): "";
			vd.desc_STEP = (descMap.containsKey("STEP"))? descMap.get("STEP"): "";
			vd.desc_ID = (vd.desc_ID == null)? " ": vd.desc_ID;
			vd.desc_DESC = (vd.desc_DESC == null)? " ": vd.desc_DESC;
			vd.desc_STEP = (vd.desc_STEP == null)? " ": vd.desc_STEP;
		}
		
		return verdescList;
	}
	
	public Tbsoptcvsmap parser(String hostname, String module, List<String> lineList) {
		
		/** Check DATA **/
		checkParserData(lineList);
		
		/** Parse DATA **/
		// RCS file
		String rcsfile = lineList.get(0).substring(10, lineList.get(0).indexOf(",v"));
		
		// file name
		String filename = rcsfile.split("/")[rcsfile.split("/").length - 1];
		
		// program id
		String programid = fxProgramID(rcsfile);
		
		// module
		String moduleName = module.split("-")[1];
		
		// client or server code
		char csCode = fxClientServer(module);
		
		// head version
		BigDecimal head = new BigDecimal(lineList.get(2).substring(6));
		
		// collects tag data list
		List<VERTAG> vertagList = fxCollectTagList(lineList, 7);
		
		// collects desc data list
		List<VERDESC> verdescList = fxCollectDescList(lineList, 7 + vertagList.size());
		
		/** Convert to DTO and SAVE into DB **/
		Tbsoptcvsmap map = null;
		List<Tbsoptcvsver> verList = new ArrayList<Tbsoptcvsver>();
		List<Tbsoptcvstag> tagList = new ArrayList<Tbsoptcvstag>();
		
		// MAP
		map = dao.queryMapByRcsfile(rcsfile);
		if (map == null) {
			map = new Tbsoptcvsmap();
			map.setRcsfile(rcsfile);
			map.setFilename(filename);
			map.setProgramid(programid);
			map.setModule(moduleName);
			map.setVersionhead(head);
			map.setClientserver(csCode);
			map.setCreator(hostname);
			map.setCreatetime(TimestampHelper.now());
			map.setModifier(map.getCreator());
			map.setLastupdate(map.getCreatetime());
		}else{
			map.setVersionhead(head);
			map.setModifier(hostname);
			map.setLastupdate(TimestampHelper.now());
		}
		
		// VER
//		Tbsoptcvsver ver = null;
//		for (VERDESC desc : verdescList) {
//			ver = dao.queryVerByVer(map.getMSid(), desc.revision);
//			if (ver == null) {
//				ver = new Tbsoptcvsver();
//				ver.setTbsoptcvsmap(map);
//				ver.setVersion(desc.revision);
//				ver.setAuthor(desc.author);
//				ver.setVerdate(desc.date);
//				ver.setState(("Exp".equalsIgnoreCase(desc.state)? '0': '1'));
//				ver.setFulldesc(desc.rawdesc.toString());
//				ver.setDescId(desc.desc_ID);
//				ver.setDescDesc(desc.desc_DESC);
//				ver.setDescStep(desc.desc_STEP);
//				ver.setCreator(hostname);
//				ver.setCreatetime(TimestampHelper.now());
//				ver.setModifier(ver.getCreator());
//				ver.setLastupdate(ver.getCreatetime());
//			}else{
//				ver.setModifier(hostname);
//				ver.setLastupdate(TimestampHelper.now());
//			}
//			verList.add(ver);
//		}
		
		// TAG
		
		/** TEST **/
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			session.save(map);
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			e.printStackTrace();
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
		/** TEST **/
		
		return null;
	}
}
