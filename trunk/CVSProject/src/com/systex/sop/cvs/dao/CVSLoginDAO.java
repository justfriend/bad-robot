package com.systex.sop.cvs.dao;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.dto.Tbsoptcvslogin;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.SessionUtil;

public class CVSLoginDAO {
	
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
			cri.add(Restrictions.eq("flag", "S"));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
				Tbsoptcvslogin newObj = new Tbsoptcvslogin();
				newObj.setFlag(CVSConst.FLAG.SESSION.getText());
				newObj.setStatus(CVSConst.STATUS.LOGIN.getText());
				newObj.setDescription(" ");
				newObj.setCreator(hostname);
				newObj.setModifier(hostname);
				session.save(newObj);
			}else{
				if (CVSConst.STATUS.LOGIN.getText() == oldObj.getStatus()) {
					return oldObj;
				}else{
					oldObj.setStatus(CVSConst.STATUS.LOGIN.getText());
					oldObj.setDescription("Login");
					oldObj.setCreator(hostname);
					oldObj.setModifier(hostname);
					oldObj.setCreatetime(null); // reset
					session.update(oldObj);
				}
			}
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
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
	public Tbsoptcvslogin doLogout(String hostname) throws Exception {
		Session session = null;
		Transaction txn = null;
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			Criteria cri = session.createCriteria(Tbsoptcvslogin.class);
			cri.add(Restrictions.eq("flag", "S"));
			cri.setMaxResults(1);
			Tbsoptcvslogin oldObj = (Tbsoptcvslogin) cri.uniqueResult();
			
			if (oldObj == null) {
			}else{
				if (CVSConst.STATUS.LOGIN.getText() == oldObj.getStatus()) {
					return oldObj;
				}else{
					oldObj.setStatus(CVSConst.STATUS.LOGOUT.getText());
					oldObj.setDescription("Logout");
					oldObj.setModifier(hostname);
					oldObj.setLastupdate(new Timestamp(new Date().getTime()));
					session.update(oldObj);
				}
			}
			SessionUtil.commit(txn);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			SessionUtil.rollBack(txn);
		}finally{
			SessionUtil.closeSession(session);
		}
		
		return null;
	}
}
