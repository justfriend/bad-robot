package com.systex.sop.cvs.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.systex.sop.cvs.helper.CVSLog;

/**
 * Session Utility (Hibernate)
 * <p>
 * <p>
 * Modify history : <br>
 * ================================ <br>
 * 2012/01/04	.[- _"].	release
 * <p>
 *
 */
public class SessionUtil {
	private static SessionFactory sessionFty = null;
	
	private static void buildSessionFactory() {
		sessionFty = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Close session
	 * <p>
	 * @param session
	 */
	public static void closeSession(Session session) {
		if (session != null) {
			try {
				session.flush(); /* AUTO FLUSH */
				session.clear();
			} catch (HibernateException e) {
				e.printStackTrace();
			} finally {
				if (session != null) {
					try {
						session.close();
					} catch (HibernateException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Close session factory
	 * <p>
	 * Call this before application shutdown
	 */
	public static void closeSessionFactory() {
		if (sessionFty == null) {
			try {
				sessionFty.close();
			} catch(HibernateException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get session
	 * <p>
	 * @return
	 */
	public synchronized static Session openSession() {
		if (sessionFty == null) {
			buildSessionFactory();
		}
		
		return sessionFty.openSession();
	}
	
	/**
	 * Commit transaction
	 * <p>
	 * @param txn
	 */
	public static void commit(Transaction txn) {
		if (txn == null) {
			throw new RuntimeException ("Transaction is null");
		} else {
			try {
				txn.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Rollback transaction
	 * <p>
	 * @param txn
	 */
	public static void rollBack(Transaction txn) {
		if (txn != null) {
			try {
				txn.rollback();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main (String [] args) throws Exception {
		SessionUtil.openSession().close();
	}

}
