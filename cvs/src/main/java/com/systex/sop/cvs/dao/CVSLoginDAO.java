package com.systex.sop.cvs.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.TimestampHelper;

public class CVSLoginDAO {
	
	/**
	 * 查詢登入資訊
	 * <p>
	 */
	public Tbsoptcvslogin retrieveLogin() {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.add(Restrictions.eq("flag", CVSConst.LOGIN_FLAG.SESSION.getText()));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
				return null;
			}else{
				return oldObj;
			}
		}catch(HibernateException e){
			CVSLog.getLogger().error(this, e);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
	}
	
	/**
	 * 重置登入
	 * <p>
	 * 登入卡死時可以進行重置
	 * @param hostname
	 * @return
	 */
	public Tbsoptcvslogin doResetLogin(String hostname) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.add(Restrictions.eq("flag", CVSConst.LOGIN_FLAG.SESSION.getText()));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
				return null;
			}else{
				if (CVSConst.LOGIN_STATUS.LOGIN.getFlag() == oldObj.getStatus()) {
					session.delete(oldObj);
				}else{
					return oldObj;	// 登入資訊存在但無須刪除
				}
			}
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
	
	/**
	 * 進行登入
	 * <p>
	 * 若已有其他使用者登入則回傳該使用者之登入資訊反之則回傳NULL
	 * 
	 * @param hostname 登入的主機名稱
	 * @return
	 * @throws Exception
	 */
	public Tbsoptcvslogin doLogin(String hostname) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.add(Restrictions.eq("flag", CVSConst.LOGIN_FLAG.SESSION.getText()));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
				Tbsoptcvslogin newObj = new Tbsoptcvslogin();
				newObj.setFlag(CVSConst.LOGIN_FLAG.SESSION.getText());
				newObj.setStatus(CVSConst.LOGIN_STATUS.LOGIN.getFlag());
				newObj.setDescription(" ");
				newObj.setCreator(hostname);
				newObj.setCreatetime(TimestampHelper.now());
				newObj.setModifier(hostname);
				newObj.setLastupdate(null);
				session.save(newObj);
			}else{
				if (CVSConst.LOGIN_STATUS.LOGIN.getFlag() == oldObj.getStatus()) {
					return oldObj;
				}else{
					oldObj.setStatus(CVSConst.LOGIN_STATUS.LOGIN.getFlag());
					oldObj.setDescription("Login");
					oldObj.setCreator(hostname);
					oldObj.setModifier(hostname);
					oldObj.setCreatetime(TimestampHelper.now());
					oldObj.setLastupdate(null);
					session.update(oldObj);
				}
			}
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
	
	/**
	 * 進行登出
	 * <p>
	 * 登出成功後回傳NULL，如果狀態非登入狀態則回傳當前的登入資訊
	 * 
	 * @param hostname 登出的主機名稱
	 * @return
	 * @throws Exception
	 */
	public Tbsoptcvslogin doLogout(String hostname) {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.add(Restrictions.eq("flag", CVSConst.LOGIN_FLAG.SESSION.getText()));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
				// 未登入則登出 (在此儘做警告)
				CVSLog.getLogger().warn("進行登出，但未發現登入紀錄...");
			}else{
				if (CVSConst.LOGIN_STATUS.LOGOUT.getFlag() == oldObj.getStatus()) {
					return oldObj;
				}else{
					oldObj.setStatus(CVSConst.LOGIN_STATUS.LOGOUT.getFlag());
					oldObj.setDescription("Logout");
					oldObj.setModifier(hostname);
					oldObj.setLastupdate(TimestampHelper.now());
					session.update(oldObj);
				}
			}
			SessionUtil.commit(txn);
		}catch(HibernateException e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
			throw e;
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
}
