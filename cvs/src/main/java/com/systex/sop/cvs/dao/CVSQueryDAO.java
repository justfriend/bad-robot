package com.systex.sop.cvs.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.dto.TbsoptcvsverId;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StringUtil;

@SuppressWarnings("unchecked")
public class CVSQueryDAO {
	private CommonDAO dao = new CommonDAO();
	
	private VerMapDO genVerMapDO(Tbsoptcvsmap map, Tbsoptcvsver ver) {
		VerMapDO obj = new VerMapDO();
		obj.setRcsfile(map.getRcsfile());
		obj.setFilename(map.getFilename());
		obj.setProgramid(map.getProgramid());
		obj.setModule(CVSFunc.fxModule(map.getModule(), map.getClientserver()));
		obj.setVersion(ver.getId().getVersion());
		obj.setVersionhead(map.getVersionhead());
		obj.setMSid(ver.getId().getMSid());
		obj.setAuthor(ver.getAuthor());
		obj.setVerdate(ver.getVerdate());
		obj.setState( ('1' == ver.getState())? "刪除": "正常" );
		obj.setFulldesc(ver.getFulldesc());
		obj.setDescId(ver.getDescId());
		obj.setDescDesc(ver.getDescDesc());
		obj.setDescStep(ver.getDescStep());
		obj.setLastupdate(ver.getLastupdate());
		
		return obj;
	}
	
	/** 查詢提交註記錯誤或遺漏 **/
	public List<VerMapDO> queryCommentMiss(String author, boolean isIgnoreDel, Timestamp date) {
		String andAuthor = StringUtil.genStringIgnoreCaseEqSQL("v.author", author);
		String andIgnore = (isIgnoreDel)? " AND v.state = '0'": "";
		String andVrdate = StringUtil.genDateBetweenSQL("v.verdate", date, null);
		
		String sql = StringUtil.concat(
				"SELECT m.rcsfile, m.filename, m.programid, m.module, m.clientserver, m.versionhead,",
				"       v.m_sid, v.version, v.author, v.verdate, v.state, v.fulldesc, v.desc_id, v.desc_desc, v.desc_step, v.creator, v.createtime, v.modifier, v.lastupdate",
				"  FROM tbsoptcvsver v",
				" INNER JOIN tbsoptcvsmap m ON (v.m_sid = m.m_sid)",
				" WHERE (v.desc_id IS NULL OR v.desc_desc IS NULL)", andAuthor, andIgnore, andVrdate);
		
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			query.addEntity("v", Tbsoptcvsver.class);
			Iterator<Tbsoptcvsver> iterator = query.list().iterator();
			Tbsoptcvsver ver = null;
			Tbsoptcvsmap map = null;
			while(iterator.hasNext()) {
				ver = iterator.next();
				map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
	
	/** 查詢最新版本TAG未上 **/
	public List<VerMapDO> queryNewVerNoTag(String author, boolean isIgnoreDel) {
		String andAuthor = StringUtil.genStringIgnoreCaseEqSQL("v.author", author);
		String andIgnore = isIgnoreDel? " AND v.state = '0'": "";
		
		String sql = StringUtil.concat(
				"SELECT m.rcsfile, m.filename, m.programid, m.module, m.clientserver, m.versionhead,",
				"       v.m_sid, v.version, v.author, v.verdate, v.state, v.fulldesc, v.desc_id, v.desc_desc, v.desc_step, v.creator, v.createtime, v.modifier, v.lastupdate",
				"  FROM tbsoptcvsver v",
				" INNER JOIN tbsoptcvsmap m ON (v.m_sid = m.m_sid AND v.version = m.versionhead)",
				"  LEFT JOIN tbsoptcvstag t ON (v.m_sid = t.m_sid AND v.version = t.version AND t.tagname = '", PropReader.getProperty("CVS.SOPATAG"), "')",
				" WHERE t.tagname IS NULL", andAuthor, andIgnore);
		
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			query.addEntity("v", Tbsoptcvsver.class);
			Iterator<Tbsoptcvsver> iterator = query.list().iterator();
			Tbsoptcvsver ver = null;
			Tbsoptcvsmap map = null;
			while(iterator.hasNext()) {
				ver = iterator.next();
				map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
	
	/** 一般查詢 **/
	public List<VerMapDO> queryNormal(String author, boolean isIgnoreDel, Timestamp beginDate, Timestamp endedDate,
			String filename, String program, String id, String desc, String tagName) 
	{	
		String andAuthor = StringUtil.genStringIgnoreCaseEqSQL("v.author", author);
		String andIgnore = (isIgnoreDel)? " AND v.state = '0'": "";
		String andVrdate = StringUtil.genDateBetweenSQL("v.verdate", beginDate, endedDate);
		String andFilename = StringUtil.genStringIgnoreCaseLikeSQL("m.filename", filename);
		String andProgram = StringUtil.genStringIgnoreCaseLikeSQL("m.programid", program);
		String andId = StringUtil.genStringEqSQL("v.desc_id", id);
		String andDesc = StringUtil.genStringIgnoreCaseLikeSQL("v.desc_desc", desc);
		String innerTAG = "";
		if (StringUtil.isNotEmpty(tagName)) {
			innerTAG = StringUtil.concat(
				" INNER JOIN tbsoptcvstag t ON (v.m_sid = t.m_sid AND v.version = t.version AND t.tagname = '", tagName.toUpperCase(), "')");
		}
			
		String sql = StringUtil.concat(
				"SELECT m.rcsfile, m.filename, m.programid, m.module, m.clientserver, m.versionhead,",
				"       v.m_sid, v.version, v.author, v.verdate, v.state, v.fulldesc, v.desc_id, v.desc_desc, v.desc_step, v.creator, v.createtime, v.modifier, v.lastupdate",
				"  FROM tbsoptcvsver v",
				" INNER JOIN tbsoptcvsmap m ON (v.m_sid = m.m_sid)",
				innerTAG,
				" WHERE 1 = 1 ", andAuthor, andIgnore, andVrdate, andFilename, andProgram, andId, andDesc);
		CVSLog.getLogger().info(sql);
		
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			query.addEntity("v", Tbsoptcvsver.class);
			Iterator<Tbsoptcvsver> iterator = query.list().iterator();
			Tbsoptcvsver ver = null;
			Tbsoptcvsmap map = null;
			while(iterator.hasNext()) {
				ver = iterator.next();
				map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
	
	/** 查詢版號歷程 **/
	public List<VerTreeDO> queryVerTree(Long rcsid) {
		List<VerTreeDO> objList = new ArrayList<VerTreeDO>();
		
		Tbsoptcvsmap map = (Tbsoptcvsmap) dao.getDTO(Tbsoptcvsmap.class, rcsid);
		
		for (Tbsoptcvsver ver : map.getTbsoptcvsvers()) {
			if (ver.getTbsoptcvstags() == null || ver.getTbsoptcvstags().size() < 1) {
				VerTreeDO obj = new VerTreeDO();
				obj.setAuthor(ver.getAuthor());
				obj.setTagname(null);
				obj.setVerdate(ver.getVerdate());
				obj.setVersion(ver.getId().getVersion());
				objList.add(obj);
			}else{
				for (Tbsoptcvstag tag : ver.getTbsoptcvstags()) {
					VerTreeDO obj = new VerTreeDO();
					obj.setAuthor(ver.getAuthor());
					obj.setTagname(tag.getId().getTagname());
					obj.setVerdate(ver.getVerdate());
					obj.setVersion(ver.getId().getVersion());
					objList.add(obj);
				}
			}
		}
		Collections.sort(objList);
		
		return objList;
	}
	
	/** 取得TAG清單 **/
	public List<TagDO> retrieveTAG(Long rcsid, String version) {
		List<Tbsoptcvstag> objList = (List<Tbsoptcvstag>) dao.queryDTO(
				Tbsoptcvstag.class,
				StringUtil.concat("FROM Tbsoptcvstag WHERE id.MSid = ", rcsid, " AND id.version = '", version, "'")
		);
		
		List<TagDO> tList = new ArrayList<TagDO>();
		for (Tbsoptcvstag tag : objList) {
			TagDO t = new TagDO();
			t.setTagname(tag.getId().getTagname());
			tList.add(t);
		}
		
		return tList;
	}
	
	/** 取得特定版號 **/
	public Tbsoptcvsver retrieveVER(Long rcsid, String version) {
		return (Tbsoptcvsver) dao.loadDTO(Tbsoptcvsver.class, new TbsoptcvsverId(rcsid, version));
	}
	
	/** 更新提交註記 **/
	public void updateVER(Long rcsid, String version, String comment) {
		Tbsoptcvsver ver = (Tbsoptcvsver) dao.loadDTO(Tbsoptcvsver.class, new TbsoptcvsverId(rcsid, version));
		CVSParserLogic.VERDESC vd = new CVSParserLogic.VERDESC();
		vd.rawdesc = new StringBuffer( (comment == null)? "": comment);
		vd.splitDesc();
		ver.setDescId(vd.desc_ID);
		ver.setDescDesc( (vd.desc_DESC == null)? null: vd.desc_DESC.toString() );
		ver.setDescStep( (vd.desc_STEP == null)? null: vd.desc_STEP.toString() );
		ver.setFulldesc(vd.rawdesc.toString());
		dao.updateDTO(ver);
	}
}
