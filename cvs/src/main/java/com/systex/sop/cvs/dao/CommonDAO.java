package com.systex.sop.cvs.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.systex.sop.cvs.util.SessionUtil;

@SuppressWarnings("unchecked")
public class CommonDAO {
	
	public List querySQL(String sql) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			return query.list();
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public int executeSQL(String sql) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			SQLQuery query = session.createSQLQuery(sql);
			return query.executeUpdate();
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public int executeHQL(String hql) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Query query = session.createQuery(hql);
			return query.executeUpdate();
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public List queryDTO(Class<? extends Object> loadClass, String hql) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Query qry = session.createQuery(hql);
			return qry.list();
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public Object loadDTO(Class<? extends Object> loadClass, Serializable key) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Object obj = session.load(loadClass, key);
			if (obj != null) obj.toString();	// 印出來強迫加載
			return obj;
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}

	public Object getDTO(Class<? extends Object> loadClass, Serializable key) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Object obj = session.get(loadClass, key);
			if (obj != null) obj.toString();	// 印出來強迫加載
			return obj;
		}catch(HibernateException e){
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void saveDTO(Object dto) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			session.save(dto);
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			SessionUtil.rollBack(txn);
			throw e;
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
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void saveDTO(List<? extends Object> dtoList, final int batch) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			int cursor = 0;
			Iterator<? extends Object> iter = dtoList.iterator();
			while (iter.hasNext()) {
				session.save(iter.next());
				if (++cursor % batch == 0) {
					session.flush();
					session.clear();
				}
			}
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	public void updateDTO(List<? extends Object> dtoList, int batch) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			int cursor = 0;
			Iterator<? extends Object> iter = dtoList.iterator();
			while (iter.hasNext()) {
				session.update(iter.next());
				if (++cursor >= batch) {
					session.flush();
					cursor = 0;
				}
			}
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
}
