package com.systex.sop.cvs.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.systex.sop.cvs.constant.CVSConst.PROG_TYPE;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.dto.TbsoptcvsverId;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.CriteriaUtil;
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
	public List<VerMapDO> queryCommentMiss(String author, boolean isIgnoreDel, Timestamp date, PROG_TYPE type) {
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");
			
			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			CriteriaUtil.between(cri, "verdate", date, null);
			cri.add(Restrictions.or(
					Restrictions.isNull("descId"),
					Restrictions.isNull("descDesc") ));
			if (isIgnoreDel) cri.add(Restrictions.ne("state", '1'));
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			}else
			if (PROG_TYPE.SCHEMA == type){
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}
			
			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}
		
		return objList;
	}
	
	/** 查詢最新版本TAG未上 **/
	public List<VerMapDO> queryNewVerNoTag(String author, boolean isIgnoreDel, PROG_TYPE type) {
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");
			cri.createCriteria("tbsoptcvstags", "t", Criteria.LEFT_JOIN, Restrictions.eq("t.id.tagname", PropReader.getProperty("CVS.SOPATAG")));

			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			cri.add(Restrictions.eqProperty("m.versionhead", "v.id.version"));
			cri.add(Restrictions.isNull("t.id.tagname"));
			if (isIgnoreDel) cri.add(Restrictions.ne("state", '1'));
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			}else
			if (PROG_TYPE.SCHEMA == type){
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}

			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}

		return objList;
	}
	
	/** 一般查詢 **/
	public List<VerMapDO> queryNormal(String author, boolean isIgnoreDel, Timestamp beginDate, Timestamp endedDate,
			String filename, String program, String id, String desc, String tagName, PROG_TYPE type) 
	{
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");
			
			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			CriteriaUtil.between(cri, "verdate", beginDate, endedDate);
			CriteriaUtil.likeIgnoreCase(cri, "m.filename", filename);
			CriteriaUtil.likeIgnoreCase(cri, "m.programid", program);
			CriteriaUtil.likeIgnoreCase(cri, "descId", id);
			CriteriaUtil.likeIgnoreCase(cri, "descDesc", desc);
			if (isIgnoreDel) cri.add(Restrictions.ne("state", '1'));
			if (StringUtil.isNotEmpty(tagName)) {
				cri.createCriteria("tbsoptcvstags", "t", Criteria.INNER_JOIN, Restrictions.eq("t.id.tagname", tagName));
			}
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			}else
			if (PROG_TYPE.SCHEMA == type){
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}
			
			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}
		
		return objList;
	}
	
	/** 查詢版號歷程 (版本樹) **/
	public List<VerTreeDO> queryVerTree(Long rcsid) {
		List<VerTreeDO> objList = new ArrayList<VerTreeDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsmap.class, "m");
			cri.createCriteria("m.tbsoptcvsvers", "v");
			cri.createCriteria("v.tbsoptcvstags", "t", Criteria.LEFT_JOIN);

			cri.add(Restrictions.eq("m.MSid", rcsid));
			
			Tbsoptcvsmap map = (Tbsoptcvsmap) cri.uniqueResult();
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
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}
		
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
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Tbsoptcvsver obj = (Tbsoptcvsver) session.load(Tbsoptcvsver.class, new TbsoptcvsverId(rcsid, version));
			Hibernate.initialize(obj.getTbsoptcvsmap());	// 載入MAP
			return obj;
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	/** 更新提交註記 **/
	public void updateVER(Long rcsid, String version, String comment) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Tbsoptcvsver ver = (Tbsoptcvsver) session.load(Tbsoptcvsver.class, new TbsoptcvsverId(rcsid, version));
			
			// 更新 DESC_ID, DESC_DESC, FULLDESC
			CVSParserLogic.VERDESC vd = new CVSParserLogic.VERDESC();
			vd.rawdesc = new StringBuffer( (comment == null)? "": comment);
			vd.splitDesc();
			ver.setDescId(vd.desc_ID);
			ver.setDescDesc( (vd.desc_DESC == null)? null: vd.desc_DESC.toString() );
			ver.setDescStep( (vd.desc_STEP == null)? null: vd.desc_STEP.toString() );
			ver.setFulldesc(vd.rawdesc.toString());
			session.update(ver);
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
}
