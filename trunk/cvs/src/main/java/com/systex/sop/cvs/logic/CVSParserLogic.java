package com.systex.sop.cvs.logic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.dao.CVSParserDAO;
import com.systex.sop.cvs.dao.CommonDAO;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.TbsoptcvstagId;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.dto.TbsoptcvsverId;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class CVSParserLogic {
	private CommonDAO commonDAO = new CommonDAO();
	private CVSParserDAO dao = new CVSParserDAO();
	
	/** the structure that collects tags **/
	class VERTAG {
		public String version;
		public String tagname;
	}
	
	/** the structure that collects version description **/
	public static class VERDESC {
		private final int ID = 1;
		private final int DESC = 2;
		private final int STEP = 3;
		public String revision;
		public Timestamp date;
		public String author;
		public String state;
		public String filename;
		public String desc_ID;
		public StringBuffer desc_DESC = null;
		public StringBuffer desc_STEP = null;
		public StringBuffer rawdesc = new StringBuffer();
		public void splitDesc() {
			int CURRENT = 0;
			String [] lineArray = rawdesc.toString().replaceAll(";", System.getProperty("line.separator")).split(System.getProperty("line.separator"));
			for (String line : lineArray) {
				if (StringUtil.isEmpty(line)) continue;
				
				/** DESC_ID **/
				if (line.startsWith("ID:")) {
					CURRENT = ID;
					if (line.indexOf(";") >= 0) {
						desc_ID = line.substring(3, line.indexOf(";")).trim();
					}else{
						desc_ID = line.substring(3).trim();
					}
				}
				
				/** DESC_DESC **/
				if (line.startsWith("DESC:") || CURRENT == DESC) {
					CURRENT = DESC;
					if (line.startsWith("DESC:")) {
						desc_DESC = new StringBuffer();
						desc_DESC.append(line.substring(5));
					}else{
						desc_DESC.append(line);
					}
				}
				
				/** DESC_STEP **/
				if (line.startsWith("STEP:") || CURRENT == STEP) {
					CURRENT = STEP;
					if (line.startsWith("STEP:")) {
						desc_STEP = new StringBuffer();
						desc_STEP.append(line.substring(5));
					}else{
						desc_STEP.append(line);
					}
				}
			} // for
		}
	}
	
	/** verify date **/
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
	
	private String fxProgramID(String rcsfile) {
		String programid = null;
		String [] pArray = rcsfile.split("/");
		for (int i=0; i<pArray.length; i++) {
			if ("view".equalsIgnoreCase(pArray[i]) || "model".equalsIgnoreCase(pArray[i])) {
				programid = pArray[i+1];
				if (!StringUtil.isEmpty(programid))  {
					if (!programid.toUpperCase().startsWith("SOP")) {
						programid = null;
					}
				}
			}
		}
		return (programid == null)? " ": programid;
	}
	
	private char fxClientServer(String module) {
		char csCode = CVSConst.CLIENTSERVER.CLIENT.getFlag();
		String cs = module.split("-")[2];
		if ("client".equalsIgnoreCase(cs)) {
			csCode = CVSConst.CLIENTSERVER.CLIENT.getFlag();
		}else
		if ("server".equalsIgnoreCase(cs)) {
			csCode = CVSConst.CLIENTSERVER.SERVER.getFlag();
		}else
		if ("schema".equalsIgnoreCase(cs)) {
			csCode = CVSConst.CLIENTSERVER.SCHEMA.getFlag();
		}
		
		return csCode;
	}
	
	private List<VERTAG> fxCollectTagList(List<String> lineList, int beginLine) {
		List<VERTAG> tagList = new ArrayList<VERTAG>();
		for (int i = beginLine; i < lineList.size(); i++) {
			String line = lineList.get(i);
			if (line.startsWith("keyword") || line.startsWith("total")) { break; }
			VERTAG tag = new VERTAG();
			tag.tagname = line.split(": ")[0].trim();
			tag.version = line.split(": ")[1];
			tagList.add(tag);
		}
		
		return tagList;
	}
	
	private List<VERDESC> fxCollectDescList(List<String> lineList, int beginLine) {
		List<VERDESC> verdescList = new ArrayList<VERDESC>();
		VERDESC verdesc = null;
		boolean isDescArea = false;
		for (int i = beginLine; i < lineList.size(); i++) {
			String line = lineList.get(i);
			
			if (isDescArea == true) {
				if (line.startsWith(CVSConst.SPLIT_DESC) ||
					line.startsWith(CVSConst.BLOCK_END)) {
					isDescArea = false;
				}else{
					verdesc.rawdesc.append(line).append(System.getProperty("line.separator"));
				}
				continue;
			}
			
			if (line.startsWith("revision")) {
				verdesc = new VERDESC();
				try {
					verdesc.revision = line.substring(9);
				}catch(NumberFormatException e){
					CVSLog.getLogger().error(this, e);
					throw e;
				}
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
			vd.splitDesc();
		}
		
		return verdescList;
	}
	
	public Tbsoptcvsmap parser(String hostname, String module, List<String> lineList, boolean isFullSync) {
		CVSLog.getLogger().info(StringUtil.concat("Parsing... [module]", module, ", [lineList]", lineList.size(), ", [isFullSync]", isFullSync));
		
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
		String head = lineList.get(2).substring(6);
		
		// collects tag data list
		List<VERTAG> vertagList = fxCollectTagList(lineList, 7);
		
		// collects desc data list
		List<VERDESC> verdescList = fxCollectDescList(lineList, 7 + vertagList.size());
		
		/** Convert to DTO and SAVE into DB **/
		Tbsoptcvsmap map = dao.queryMapByRcsfile(rcsfile);
		if (map == null) {
			map = new Tbsoptcvsmap();
			map.setRcsfile(rcsfile);
			map.setFilename(filename);
			map.setProgramid(programid);
			map.setModule(moduleName);
			map.setClientserver(csCode);
			map.setVersionhead(head);
			map.setCreator(hostname);
			map.setCreatetime(TimestampHelper.now());
			map.setModifier(map.getCreator());
			map.setLastupdate(map.getCreatetime());
			commonDAO.saveDTO(map);
		}else{
			map.setVersionhead(head);
			map.setModifier(hostname);
			map.setLastupdate(TimestampHelper.now());
			commonDAO.updateDTO(map);
		}
		
		if (!isFullSync) {
			synchronized (CVSParserLogic.class) {
				commonDAO.executeHQL(StringUtil.concat("delete from Tbsoptcvstag where m_sid = ", map.getMSid()));
			}
		}
		
		List<Tbsoptcvstag> tagList = new ArrayList<Tbsoptcvstag>();
		for (VERTAG vertag : vertagList) {
			Tbsoptcvstag tag = new Tbsoptcvstag();
			tag.setId(new TbsoptcvstagId(map.getMSid(), vertag.version, vertag.tagname));
			tag.setCreator(hostname);
			tag.setCreatetime(TimestampHelper.now());
			tag.setModifier(tag.getCreator());
			tag.setLastupdate(tag.getCreatetime());
			tagList.add(tag);
		}
		commonDAO.saveDTO(tagList, 500);
		
		List<Tbsoptcvsver> insertVerList = new ArrayList<Tbsoptcvsver>();
		List<Tbsoptcvsver> updateVerList = new ArrayList<Tbsoptcvsver>();
		for (VERDESC verdesc : verdescList) {
			TbsoptcvsverId verId = new TbsoptcvsverId(map.getMSid(), verdesc.revision);
			Tbsoptcvsver ver = (Tbsoptcvsver) commonDAO.getDTO(Tbsoptcvsver.class, verId);
			ver = (ver == null)? new Tbsoptcvsver(): ver;
			ver.setAuthor(verdesc.author);
			ver.setVerdate(verdesc.date);
			ver.setState("Exp".equalsIgnoreCase(verdesc.state)? '0': '1');
			ver.setDescId(verdesc.desc_ID);
			ver.setDescDesc( (verdesc.desc_DESC == null)? null: verdesc.desc_DESC.toString() );
			ver.setDescStep( (verdesc.desc_STEP == null)? null: verdesc.desc_STEP.toString() );
			ver.setFulldesc(verdesc.rawdesc.toString());
			ver.setModifier(hostname);
			ver.setLastupdate(TimestampHelper.now());
			if (ver.getId() == null) {
				ver.setId(verId);
				ver.setCreator(hostname);
				ver.setCreatetime(TimestampHelper.now());
				ver.setModifier(ver.getCreator());
				ver.setLastupdate(ver.getCreatetime());
				insertVerList.add(ver);
			}else{
				updateVerList.add(ver);
			}
		}
		commonDAO.saveDTO(insertVerList, 500);
		commonDAO.updateDTO(updateVerList, 500);
		
		return map;
	}
}
