package com.systex.sop.cvs.util;

import java.io.File;

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
	private static int retry = 0;
	
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
		int retry = 0;
		Session session = null;
		
		while (retry < 3) {
			try {
				if (sessionFty == null) buildSessionFactory();
				session = sessionFty.openSession();
//				session.createSQLQuery("SELECT 1 FROM dual").list();
				return session;
			}catch(HibernateException e){
				CVSLog.getLogger().warn(e);
				retry++;
				if (retry == 2) {
					throw e;
				}else{
					closeSessionFactory();
					ThreadHelper.sleep(3000);
					CVSLog.getLogger().warn("connection retrying...");
				}
			}
		}
		
		return null;
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
