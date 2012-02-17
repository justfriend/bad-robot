package com.systex.sop.cvs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.dto.TbsoptcvsverId;
import com.systex.sop.cvs.helper.CVSLog;
import com.systex.sop.cvs.util.JDBCResCloseHelper;
import com.systex.sop.cvs.util.SessionUtil;

/**
 * 同步寫入之資料存取 (TXN)
 * <P>
 *
 */
public class CVSParserDAOTxn {
	private static final String sequenceMapStmtStr = "Select CVSMAP_MSID_SEQ.NEXTVAL from DUAL";
	private static final String selectMapStmtStr = "Select M_SID,RCSFILE,FILENAME,PROGRAMID,MODULE,CLIENTSERVER,VERSIONHEAD from TBSOPTCVSMAP where RCSFILE = ?";
	private static final String selectVerStmtStr = "Select M_SID,VERSION,AUTHOR,VERDATE,STATE,FULLDESC,DESC_ID,DESC_DESC,DESC_STEP,CREATOR,CREATETIME,MODIFIER,LASTUPDATE from TBSOPTCVSVER where M_SID = ? and VERSION = ?";
	private static final String updateMapStmtStr = "Update TBSOPTCVSMAP set VERSIONHEAD = ? where RCSFILE = ?";
	private static final String updateVerStmtStr = "Update TBSOPTCVSVER set AUTHOR = ?, VERDATE = ?, STATE = ?, FULLDESC = ?, DESC_ID = ?, DESC_DESC = ?, DESC_STEP = ?, CREATOR = ?, CREATETIME = ?, MODIFIER = ?, LASTUPDATE = ? where M_SID = ? and VERSION = ?";
	private static final String insertMapStmtStr = "Insert into TBSOPTCVSMAP (M_SID,RCSFILE,FILENAME,PROGRAMID,MODULE,CLIENTSERVER,VERSIONHEAD) values (?,?,?,?,?,?,?)";
	private static final String insertVerStmtStr = "Insert into TBSOPTCVSVER (M_SID,VERSION,AUTHOR,VERDATE,STATE,FULLDESC,DESC_ID,DESC_DESC,DESC_STEP,CREATOR,CREATETIME,MODIFIER,LASTUPDATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String insertTagStmtStr = "Insert into TBSOPTCVSTAG (M_SID,VERSION,TAGNAME) values (?,?,?)";
	private static final String deleteTagStmtStr = "Delete from TBSOPTCVSTAG where M_SID = ?";
	private PreparedStatement sequenceMapStmt = null;
	private PreparedStatement selectMapStmt = null;
	private PreparedStatement selectVerStmt = null;
	private PreparedStatement updateMapStmt = null;
	private PreparedStatement updateVerStmt = null;
	private PreparedStatement insertMapStmt = null;
	private PreparedStatement insertVerStmt = null;
	private PreparedStatement insertTagStmt = null;
	private PreparedStatement deleteTagStmt = null;
	private Transaction txn = null;
	private Session session = null;
	
	private int batchSize = 50;
	
	private int insertMapCount = 0;
	private int insertVerCount = 0;
	private int insertTagCount = 0;
	private int updateMapCount = 0;
	private int updateVerCount = 0;
	
	/** 批次提交檢查 **/
	public void addBatchCheck() {
		addBatchCheck(batchSize);
	}
	
	/** 批次提交檢查 **/
	public void addBatchCheck(int size) {
		try {
			if (insertMapCount >= size) {
				insertMapStmt.executeBatch();
				insertMapCount = 0;
			}
			if (insertVerCount >= size) {
				insertVerStmt.executeBatch();
				insertVerCount = 0;
			}
			if (insertTagCount >= size) {
				insertTagStmt.executeBatch();
				insertTagCount = 0;
			}
			if (updateMapCount >= size) {
				updateMapStmt.executeBatch();
				updateMapCount = 0;
			}
			if (updateVerCount >= size) {
				updateVerStmt.executeBatch();
				updateVerCount = 0;
			}
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** 開始交易 **/
	@SuppressWarnings("deprecation")
	public void beginTxn() {
		try {
			session = SessionUtil.openSession();
			txn = session.beginTransaction();
			Connection conn = session.connection();
			sequenceMapStmt = conn.prepareStatement(sequenceMapStmtStr);
			selectMapStmt = conn.prepareStatement(selectMapStmtStr);
			selectVerStmt = conn.prepareStatement(selectVerStmtStr);
			updateMapStmt = conn.prepareStatement(updateMapStmtStr);
			updateVerStmt = conn.prepareStatement(updateVerStmtStr);
			insertMapStmt = conn.prepareStatement(insertMapStmtStr);
			insertVerStmt = conn.prepareStatement(insertVerStmtStr);
			insertTagStmt = conn.prepareStatement(insertTagStmtStr);
			deleteTagStmt = conn.prepareStatement(deleteTagStmtStr);
		}catch(Exception e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** 關閉資源 **/
	public void close() {
		JDBCResCloseHelper.closeStatement(
				sequenceMapStmt,
				selectMapStmt,
				selectVerStmt,
				updateMapStmt,
				updateVerStmt,
				insertMapStmt,
				insertVerStmt,
				insertTagStmt,
				deleteTagStmt);
		SessionUtil.closeSession(session);
	}
	
	/** 交易提交 **/
	public void commit() {
		SessionUtil.commit(txn);
	}
	
	/** 回滾 **/
	public void rollback() {
		SessionUtil.rollBack(txn);
	}
	
	/** DELETE TAG **/
	public int deleteTag(Long msid) {
		try {
			deleteTagStmt.setLong(1, msid);
			return deleteTagStmt.executeUpdate();
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** INSERT MAP (add batch) **/
	public void insertAddBatch(Tbsoptcvsmap map) {
		try {
			insertMapStmt.setLong(1, map.getMSid());
			insertMapStmt.setString(2, map.getRcsfile());
			insertMapStmt.setString(3, map.getFilename());
			insertMapStmt.setString(4, map.getProgramid());
			insertMapStmt.setString(5, map.getModule());
			insertMapStmt.setString(6, map.getClientserver().toString());
			insertMapStmt.setString(7, map.getVersionhead());
			
			insertMapStmt.addBatch();
			insertMapCount++;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** INSERT TAG (add batch) **/
	public void insertAddBatch(Tbsoptcvstag tag) {
		try {
			insertTagStmt.setLong(1, tag.getId().getMSid());
			insertTagStmt.setString(2, tag.getId().getVersion());
			insertTagStmt.setString(3, tag.getId().getTagname());
			
			insertTagStmt.addBatch();
			insertTagCount++;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** INSERT VER (add batch) **/
	public void insertAddBatch(Tbsoptcvsver ver) {
		try {
			insertVerStmt.setLong(1, ver.getId().getMSid());
			insertVerStmt.setString(2, ver.getId().getVersion());
			insertVerStmt.setString(3, ver.getAuthor());
			insertVerStmt.setTimestamp(4, ver.getVerdate());
			insertVerStmt.setString(5, "" + ver.getState());
			insertVerStmt.setString(6, ver.getFulldesc());
			insertVerStmt.setString(7, ver.getDescId());
			insertVerStmt.setString(8, ver.getDescDesc());
			insertVerStmt.setString(9, ver.getDescStep());
			insertVerStmt.setString(10, ver.getCreator());
			insertVerStmt.setTimestamp(11, ver.getCreatetime());
			insertVerStmt.setString(12, ver.getModifier());
			insertVerStmt.setTimestamp(13, ver.getLastupdate());
			
			insertVerStmt.addBatch();
			insertVerCount++;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** INSERT TAG (add batch) **/
	public void inserTagtAddBatch(List<Tbsoptcvstag> list) {
		for (Tbsoptcvstag tag : list) {
			insertAddBatch(tag);
		}
	}
	
	/** INSERT VER (add batch) **/
	public void insertVerAddBatch(List<Tbsoptcvsver> list) {
		for (Tbsoptcvsver ver : list) {
			insertAddBatch(ver);
		}
	}
	
	/** MAP SEQUENCE NEXTVAL **/
	public long nextvalMap() {
		ResultSet rs = null;
		try {
			rs = sequenceMapStmt.executeQuery();
			rs.next();
			return rs.getLong(1);
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}finally{
			JDBCResCloseHelper.closeResultSet(rs);
		}
	}
	
	/** QUERY MAP **/
	public Tbsoptcvsmap selectMap(String rcsfile) {
		ResultSet rs = null;
		try {
			selectMapStmt.setString(1, rcsfile);
			rs = selectMapStmt.executeQuery();
			Tbsoptcvsmap map = null;
			if (rs != null && rs.next()) {
				map = new Tbsoptcvsmap();
				map.setMSid(rs.getLong("m_sid"));
				map.setRcsfile(rs.getString("rcsfile"));
				map.setFilename(rs.getString("filename"));
				map.setProgramid(rs.getString("programid"));
				map.setModule(rs.getString("module"));
				map.setClientserver(rs.getString("clientserver").charAt(0));
				map.setVersionhead(rs.getString("versionhead"));
			}
			return map;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}finally{
			JDBCResCloseHelper.closeResultSet(rs);
		}
	}
	
	/** QUERY VER **/
	public Tbsoptcvsver selectVer(TbsoptcvsverId id) {
		ResultSet rs = null;
		try {
			selectVerStmt.setLong(1, id.getMSid());
			selectVerStmt.setString(2, id.getVersion());
			rs = selectVerStmt.executeQuery();
			Tbsoptcvsver ver = null;
			if (rs != null && rs.next()) {
				ver = new Tbsoptcvsver();
				ver.setId(id);
				ver.setAuthor(rs.getString("author"));
				ver.setVerdate(rs.getTimestamp("verdate"));
				ver.setState(rs.getString("state").charAt(0));
				ver.setFulldesc(rs.getString("fulldesc"));
				ver.setDescId(rs.getString("desc_id"));
				ver.setDescDesc(rs.getString("desc_desc"));
				ver.setDescStep(rs.getString("desc_step"));
				ver.setCreator(rs.getString("creator"));
				ver.setCreatetime(rs.getTimestamp("createtime"));
				ver.setModifier(rs.getString("modifier"));
				ver.setLastupdate(rs.getTimestamp("lastupdate"));
			}
			return ver;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}finally{
			JDBCResCloseHelper.closeResultSet(rs);
		}
	}
	
	/** UPDATE MAP (add batch) **/
	public void updateMap(String rcsfile, String versionhead) {
		try {
			// UPDATE
			updateMapStmt.setString(1, versionhead);
			// WHERE
			updateMapStmt.setString(2, rcsfile);
			
			updateMapStmt.addBatch();
			updateMapCount++;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
	
	/** UPDATE VER (add batch) **/
	public void updateVer(List<Tbsoptcvsver> verList) {
		for (Tbsoptcvsver ver : verList) {
			updateVer(ver);
		}
	}
	
	/** UPDATE VER (add batch) **/
	public void updateVer(Tbsoptcvsver ver) {
		try {
			// UPDATE
			updateVerStmt.setString(1, ver.getAuthor());
			updateVerStmt.setTimestamp(2, ver.getVerdate());
			updateVerStmt.setString(3, "" + ver.getState());
			updateVerStmt.setString(4, ver.getFulldesc());
			updateVerStmt.setString(5, ver.getDescId());
			updateVerStmt.setString(6, ver.getDescDesc());
			updateVerStmt.setString(7, ver.getDescStep());
			updateVerStmt.setString(8, ver.getCreator());
			updateVerStmt.setTimestamp(9, ver.getCreatetime());
			updateVerStmt.setString(10, ver.getModifier());
			updateVerStmt.setTimestamp(11, ver.getLastupdate());
			// WHERE
			updateVerStmt.setLong(12, ver.getId().getMSid());
			updateVerStmt.setString(13, ver.getId().getVersion());
			
			updateVerStmt.addBatch();
			updateVerCount++;
		}catch(SQLException e){
			CVSLog.getLogger().error(this, e);
			throw new HibernateException(e);
		}
	}
}
