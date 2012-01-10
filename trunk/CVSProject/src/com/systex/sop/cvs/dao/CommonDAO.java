package com.systex.sop.cvs.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.SessionUtil;

@SuppressWarnings("unchecked")
public class CommonDAO {
	
	public Iterator queryDTO(Class<? extends Object> loadClass, String hql) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Query qry = session.createQuery(hql);
			return qry.list().iterator();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
	
	public Object loadDTO(Class<? extends Object> loadClass, Serializable key) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			return session.load(loadClass, key);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}

	public Object getDTO(Class<? extends Object> loadClass, Serializable key) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			return session.get(loadClass, key);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
	
	public void saveDTO(Object dto) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			session.save(dto);
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void updateDTO(Object dto) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			session.update(dto);
			txn.commit();
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void saveDTO(List<Object> dtoList, int batch) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			int cursor = 0;
			Iterator<Object> iter = dtoList.iterator();
			while (iter.hasNext()) {
				session.save(iter.next());
				if (++cursor >= batch) {
					session.flush();
					cursor = 0;
				}
			}
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void updateDTO(List<Object> dtoList, int batch) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			int cursor = 0;
			Iterator<Object> iter = dtoList.iterator();
			while (iter.hasNext()) {
				session.update(iter.next());
				if (++cursor >= batch) {
					session.flush();
					cursor = 0;
				}
			}
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
	}
}
