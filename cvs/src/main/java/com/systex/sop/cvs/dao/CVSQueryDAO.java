package com.systex.sop.cvs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.ui.tableClass.NewVerNoTagDO;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StringUtil;

public class CVSQueryDAO {
	
	@SuppressWarnings("unchecked")
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
			while(iterator.hasNext()) {
				Tbsoptcvsver user = iterator.next();
				NewVerNoTagDO obj = new NewVerNoTagDO();
				try {
					PropertyUtils.copyProperties(obj, user.getTbsoptcvsmap());
					PropertyUtils.copyProperties(obj, user);
					objList.add(obj);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return objList;
	}
}
