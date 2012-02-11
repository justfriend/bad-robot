package com.systex.sop.cvs.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.ui.tableClass.CommentMissDO;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StringUtil;
import com.systex.sop.cvs.util.TimestampHelper;

@SuppressWarnings("unchecked")
public class CVSQueryDAO {
	private CommonDAO dao = new CommonDAO();
	
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
	
	/** 查詢最新版本TAG未上 **/
	public List<CommentMissDO> queryCommentMiss(String author, boolean isIgnoreDel, Timestamp date) {
		String andAuthor = StringUtil.isEmpty(author)? "": StringUtil.concat(" AND UPPER(v.author) = '", author.toUpperCase(), "'");
		String andIgnore = (isIgnoreDel)? " AND v.state = '0'": "";
		String andVrdate = (date == null)? "": StringUtil.concat(" AND v.verdate >= TO_DATE('", TimestampHelper.convertToyyyyMMdd(date), "', 'yyyymmdd')");
		String sql = StringUtil.concat(
				"SELECT m.rcsfile, m.filename, m.programid, m.module, m.clientserver, m.versionhead,",
				"       v.m_sid, v.version, v.author, v.verdate, v.state, v.fulldesc, v.desc_id, v.desc_desc, v.desc_step, v.creator, v.createtime, v.modifier, v.lastupdate",
				"  FROM tbsoptcvsver v",
				" INNER JOIN tbsoptcvsmap m ON (v.m_sid = m.m_sid AND v.version = m.versionhead)",
				" WHERE (v.desc_id IS NULL OR v.desc_desc IS NULL)", andAuthor, andIgnore, andVrdate);
		
		List<CommentMissDO> objList = new ArrayList<CommentMissDO>();
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("v", Tbsoptcvsver.class);
			Iterator<Tbsoptcvsver> iterator = query.list().iterator();
			Tbsoptcvsver ver = null;
			Tbsoptcvsmap map = null;
			while(iterator.hasNext()) {
				ver = iterator.next();
				map = ver.getTbsoptcvsmap();
				CommentMissDO obj = new CommentMissDO();
				obj.setRcsfile(map.getRcsfile());
				obj.setFilename(map.getFilename());
				obj.setProgramid(map.getProgramid());
				obj.setModule(map.getModule());
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
				objList.add(obj);
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
	
	/** 查詢最新版本TAG未上 **/
	public List<NewVerNoTagDO> queryNewVerNoTag(String author, boolean isIgnoreDel) {
		String andAuthor = StringUtil.isEmpty(author)? "": StringUtil.concat(" AND UPPER(v.author) = '", author.toUpperCase(), "'");
		String andIgnore = isIgnoreDel? " AND v.state = '0'": "";
		String sql = StringUtil.concat(
				"SELECT m.rcsfile, m.filename, m.programid, m.module, m.clientserver, m.versionhead,",
				"       v.m_sid, v.version, v.author, v.verdate, v.state, v.fulldesc, v.desc_id, v.desc_desc, v.desc_step, v.creator, v.createtime, v.modifier, v.lastupdate",
				"  FROM tbsoptcvsver v",
				" INNER JOIN tbsoptcvsmap m ON (v.m_sid = m.m_sid AND v.version = m.versionhead)",
				"  LEFT JOIN tbsoptcvstag t ON (v.m_sid = t.m_sid AND v.version = t.version AND t.tagname = '", PropReader.getProperty("CVS.SOPATAG"), "')",
				" WHERE t.tagname IS NULL", andAuthor, andIgnore);
		
		List<NewVerNoTagDO> objList = new ArrayList<NewVerNoTagDO>();
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity("v", Tbsoptcvsver.class);
			Iterator<Tbsoptcvsver> iterator = query.list().iterator();
			Tbsoptcvsver ver = null;
			Tbsoptcvsmap map = null;
			while(iterator.hasNext()) {
				ver = iterator.next();
				map = ver.getTbsoptcvsmap();
				NewVerNoTagDO obj = new NewVerNoTagDO();
				obj.setRcsfile(map.getRcsfile());
				obj.setFilename(map.getFilename());
				obj.setProgramid(map.getProgramid());
				obj.setModule(map.getModule());
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
				objList.add(obj);
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
}
