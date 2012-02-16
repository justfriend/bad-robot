package com.systex.sop.cvs.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.JDBCResCloseHelper;
import com.systex.sop.cvs.util.SessionUtil;

public class CVSParserDAO {
	
	public Tbsoptcvsmap queryMapByRcsfile(String rcsfile) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Criteria cri = session.createCriteria(Tbsoptcvsmap.class);
			cri.add(Restrictions.eq("rcsfile", rcsfile));
			return (Tbsoptcvsmap) cri.uniqueResult();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			throw new RuntimeException(e);
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void saveTAG(List<Tbsoptcvstag> dtoList) {
		if (dtoList == null || dtoList.size() < 1) return;
		
		Session session = null;
		Transaction txn = null;
		PreparedStatement stmt = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			stmt = session.connection().prepareStatement("INSERT INTO tbsoptcvstag(m_sid, version, tagname) VALUES(?, ?, ?)");
			for (Tbsoptcvstag tag : dtoList) {
				stmt.setLong(1, tag.getId().getMSid());
				stmt.setString(2, tag.getId().getVersion());
				stmt.setString(3, tag.getId().getTagname());
				stmt.addBatch();
			}
			stmt.executeBatch();
			SessionUtil.commit(txn);
		}catch(Exception e){
			SessionUtil.rollBack(txn);
			throw new RuntimeException(e);
		}finally{
			JDBCResCloseHelper.closeStatement(stmt);
			SessionUtil.closeSession(session);
		}
	}
}
