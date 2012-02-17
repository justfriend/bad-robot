package com.systex.sop.cvs.util;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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
		File f = new File(PropReader.getPropertyHome(), "hibernate.cfg.xml");
		sessionFty = new Configuration().configure(f).buildSessionFactory();
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
		if (sessionFty != null) {
			try {
				sessionFty.close();
				sessionFty = null;
			} catch(HibernateException e){
				throw e;
			}
		}
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
			txn.commit();
		}
	}
	
	public static SessionFactory getSessionFty() {
		return sessionFty;
	}
	
	public static void main (String [] args) throws Exception {
		SessionUtil.openSession().close();
	}
	
	/**
	 * Get session
	 * <p>
	 * @return
	 */
	public static Session openSession() {
		if (sessionFty == null) buildSessionFactory();
		return sessionFty.openSession();
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
				e.printStackTrace(); // ignore
			}
		}
	}

}
