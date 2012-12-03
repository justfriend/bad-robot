package com.systex.sop.cvs.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.systex.sop.cvs.constant.CVSConst;
import com.systex.sop.cvs.constant.CVSConst.PROG_TYPE;
import com.systex.sop.cvs.dto.Tbsoptcvsmap;
import com.systex.sop.cvs.dto.Tbsoptcvstag;
import com.systex.sop.cvs.dto.Tbsoptcvsver;
import com.systex.sop.cvs.dto.TbsoptcvsverId;
import com.systex.sop.cvs.helper.CVSFunc;
import com.systex.sop.cvs.logic.CVSParserLogic;
import com.systex.sop.cvs.ui.tableClass.TagDO;
import com.systex.sop.cvs.ui.tableClass.TagDiffDO;
import com.systex.sop.cvs.ui.tableClass.VerMapDO;
import com.systex.sop.cvs.ui.tableClass.VerTreeDO;
import com.systex.sop.cvs.util.CriteriaUtil;
import com.systex.sop.cvs.util.PropReader;
import com.systex.sop.cvs.util.SessionUtil;
import com.systex.sop.cvs.util.StringUtil;

@SuppressWarnings("unchecked")
public class CVSQueryDAO {
	private CommonDAO dao = new CommonDAO();

	private VerMapDO genVerMapDO(Tbsoptcvsmap map, Tbsoptcvsver ver) {
		VerMapDO obj = new VerMapDO();
		obj.setRcsfile(map.getRcsfile());
		obj.setFilename(map.getFilename());
		obj.setProgramid(map.getProgramid());
		obj.setModule(CVSFunc.fxModule(map.getModule(), map.getClientserver()));
		obj.setVersion(ver.getId().getVersion());
		obj.setVersionhead(map.getVersionhead());
		obj.setMSid(ver.getId().getMSid());
		obj.setAuthor(ver.getAuthor());
		obj.setVerdate(ver.getVerdate());
		obj.setState(('1' == ver.getState()) ? "刪除" : "正常");
		obj.setFulldesc(ver.getFulldesc());
		obj.setDescId(ver.getDescId());
		obj.setDescDesc(ver.getDescDesc());
		obj.setDescStep(ver.getDescStep());
		obj.setLastupdate(ver.getLastupdate());

		return obj;
	}

	/** 查詢提交註記錯誤或遺漏 **/
	public List<VerMapDO> queryCommentMiss(String author, boolean isIgnoreDel,
			Timestamp date, PROG_TYPE type) {
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");

			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			CriteriaUtil.between(cri, "verdate", date, null);
			cri.add(Restrictions.or(Restrictions.isNull("descId"),
					Restrictions.isNull("descDesc")));
			if (isIgnoreDel)
				cri.add(Restrictions.ne("state", '1'));
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			} else if (PROG_TYPE.SCHEMA == type) {
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}

			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}

		return objList;
	}

	/** 查詢最新版本TAG未上 **/
	public List<VerMapDO> queryNewVerNoTag(String author, boolean isIgnoreDel,
			PROG_TYPE type) {
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");
			cri.createCriteria(
					"tbsoptcvstags",
					"t",
					Criteria.LEFT_JOIN,
					Restrictions.eq("t.id.tagname",
							PropReader.getProperty("CVS.SOPATAG")));

			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			cri.add(Restrictions.eqProperty("m.versionhead", "v.id.version"));
			cri.add(Restrictions.isNull("t.id.tagname"));
			if (isIgnoreDel)
				cri.add(Restrictions.ne("state", '1'));
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			} else if (PROG_TYPE.SCHEMA == type) {
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}

			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}

		return objList;
	}

	/** 一般查詢 **/
	public List<VerMapDO> queryNormal(String author, boolean isIgnoreDel,
			Timestamp beginDate, Timestamp endedDate, String filename,
			String program, String id, String desc, String module,
			String tagName, PROG_TYPE type) {
		List<VerMapDO> objList = new ArrayList<VerMapDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsver.class, "v");
			cri.createCriteria("tbsoptcvsmap", "m");

			CriteriaUtil.likeIgnoreCase(cri, "author", author);
			CriteriaUtil.between(cri, "verdate", beginDate, endedDate);
			CriteriaUtil.likeIgnoreCase(cri, "m.filename", filename);
			CriteriaUtil.likeIgnoreCase(cri, "m.programid", program);
			CriteriaUtil.likeIgnoreCase(cri, "m.module", module);
			CriteriaUtil.likeIgnoreCase(cri, "descId", id);
			CriteriaUtil.likeIgnoreCase(cri, "descDesc", desc);
			if (isIgnoreDel)
				cri.add(Restrictions.ne("state", '1'));
			if (StringUtil.isNotEmpty(tagName)) {
				cri.createCriteria("tbsoptcvstags", "t", Criteria.INNER_JOIN,
						Restrictions.eq("t.id.tagname", tagName));
			}
			if (PROG_TYPE.PROGRAM == type) {
				cri.add(Restrictions.ne("m.clientserver", '2'));
			} else if (PROG_TYPE.SCHEMA == type) {
				cri.add(Restrictions.eq("m.clientserver", '2'));
			}

			cri.setMaxResults(PropReader.getPropertyInt("CVS.MAX_RESULT_COUNT"));
			Iterator<Tbsoptcvsver> iter = cri.list().iterator();
			while (iter.hasNext()) {
				Tbsoptcvsver ver = iter.next();
				Tbsoptcvsmap map = ver.getTbsoptcvsmap();
				objList.add(genVerMapDO(map, ver));
			}
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}

		return objList;
	}

	/** 查詢版號歷程 (版本樹) **/
	public List<VerTreeDO> queryVerTree(Long rcsid) {
		List<VerTreeDO> objList = new ArrayList<VerTreeDO>();
		Session session = null;
		Criteria cri = null;
		try {
			session = SessionUtil.openSession();
			cri = session.createCriteria(Tbsoptcvsmap.class, "m");
			cri.createCriteria("m.tbsoptcvsvers", "v");
			cri.createCriteria("v.tbsoptcvstags", "t", Criteria.LEFT_JOIN);

			cri.add(Restrictions.eq("m.MSid", rcsid));

			Tbsoptcvsmap map = (Tbsoptcvsmap) cri.uniqueResult();
			for (Tbsoptcvsver ver : map.getTbsoptcvsvers()) {
				if (ver.getTbsoptcvstags() == null
						|| ver.getTbsoptcvstags().size() < 1) {
					VerTreeDO obj = new VerTreeDO();
					obj.setAuthor(ver.getAuthor());
					obj.setTagname(null);
					obj.setVerdate(ver.getVerdate());
					obj.setVersion(ver.getId().getVersion());
					objList.add(obj);
				} else {
					for (Tbsoptcvstag tag : ver.getTbsoptcvstags()) {
						VerTreeDO obj = new VerTreeDO();
						obj.setAuthor(ver.getAuthor());
						obj.setTagname(tag.getId().getTagname());
						obj.setVerdate(ver.getVerdate());
						obj.setVersion(ver.getId().getVersion());
						objList.add(obj);
					}
				}
			}
			Collections.sort(objList);
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
			cri = null;
		}

		return objList;
	}

	/** 取得TAG清單 **/
	public List<TagDO> retrieveTAG(Long rcsid, String version) {
		List<Tbsoptcvstag> objList = (List<Tbsoptcvstag>) dao.queryDTO(
				Tbsoptcvstag.class, StringUtil.concat(
						"FROM Tbsoptcvstag WHERE id.MSid = ", rcsid,
						" AND id.version = '", version, "'"));

		List<TagDO> tList = new ArrayList<TagDO>();
		for (Tbsoptcvstag tag : objList) {
			TagDO t = new TagDO();
			t.setTagname(tag.getId().getTagname());
			tList.add(t);
		}

		return tList;
	}

	/** 取得特定版號 **/
	public Tbsoptcvsver retrieveVER(Long rcsid, String version) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Tbsoptcvsver obj = (Tbsoptcvsver) session.load(Tbsoptcvsver.class,
					new TbsoptcvsverId(rcsid, version));
			Hibernate.initialize(obj.getTbsoptcvsmap()); // 載入MAP
			return obj;
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
		}
	}

	/** 更新提交註記 **/
	public void updateVER(Long rcsid, String version, String comment) {
		Session session = null;
		try {
			session = SessionUtil.openSession();
			Tbsoptcvsver ver = (Tbsoptcvsver) session.load(Tbsoptcvsver.class,
					new TbsoptcvsverId(rcsid, version));

			// 更新 DESC_ID, DESC_DESC, FULLDESC
			CVSParserLogic.VERDESC vd = new CVSParserLogic.VERDESC();
			vd.rawdesc = new StringBuffer((comment == null) ? "" : comment);
			vd.splitDesc();
			ver.setDescId(vd.desc_ID);
			ver.setDescDesc((vd.desc_DESC == null) ? null : vd.desc_DESC
					.toString());
			ver.setDescStep((vd.desc_STEP == null) ? null : vd.desc_STEP
					.toString());
			ver.setFulldesc(vd.rawdesc.toString());
			session.update(ver);
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
		}
	}

	/**
	 * @throws SQLException
	 *
	 */
	public List<TagDiffDO> queryTagDiff(String startTag, String endTag,
			List<String> module) {
		List<TagDiffDO> objList = null;
		Session session = null;
		Date date = new Date();
		String tableName = "A" + String.valueOf(date.getTime());
		StringBuffer createSql = StringUtil
				.concatBuf(
						"CREATE TABLE " + tableName
								+ " AS "
								+
								// --起始TAG沒有的檔案(新增)
								"SELECT DESC_ID, FULLDESC ,AUTHOR ,VERDATE, FILENAME ,PROGRAMID ,MODULE, VERSION ,VERSIONHEAD ",
						"FROM (( SELECT H.DESC_ID ,H.FULLDESC,H.AUTHOR,H.VERDATE,X.ENDTAG_FILENAME AS FILENAME,X.ENDTAG_PROGRAMID AS PROGRAMID,X.ENDTAG_MODULE AS MODULE,H.VERSION,X.ENDTAG_VERSIONHEAD AS VERSIONHEAD ",
						"        FROM TBSOPTCVSVER H ",
						"            INNER JOIN (SELECT ENDTAG.* ",
						"                        FROM (  SELECT VER.M_SID AS STARTTAG_M_SID,VER.VERSION AS STARTTAG_VERSION,VER.AUTHOR AS STARTTAG_AUTHOR,VER.VERDATE AS STARTTAG_VERDATE,VER.FULLDESC AS STARTTAG_FULLDESC,VER.DESC_ID AS STARTTAG_DESC_ID,VER.DESC_DESC AS STARTTAG_DESC_DESC,VER.DESC_STEP STARTTAG_DESC_STEP,TAG.TAGNAME AS STARTTAG_TAGNAME,MAP.RCSFILE AS STARTTAG_RCSFILE,MAP.FILENAME AS STARTTAG_FILENAME, MAP.PROGRAMID AS STARTTAG_PROGRAMID, MAP.MODULE AS STARTTAG_MODULE, MAP.CLIENTSERVER AS STARTTAG_CLIENTSERVER, MAP.VERSIONHEAD AS STARTTAG_VERSIONHEAD ",
						"                                FROM TBSOPTCVSVER VER ",
						"                                    INNER JOIN TBSOPTCVSTAG TAG ",
						"                                    ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                    INNER JOIN TBSOPTCVSMAP MAP ",
						"                                    ON VER.M_SID = MAP.M_SID ",
						"                                WHERE TAG.TAGNAME = '"
								+ startTag + "' ",
						"                            )",
						"                            STARTTAG ",
						"                            FULL JOIN ( SELECT VER.M_SID AS ENDTAG_M_SID,VER.VERSION AS ENDTAG_VERSION,VER.AUTHOR AS ENDTAG_AUTHOR,VER.VERDATE AS ENDTAG_VERDATE,VER.FULLDESC AS ENDTAG_FULLDESC,VER.DESC_ID AS ENDTAG_DESC_ID,VER.DESC_DESC AS ENDTAG_DESC_DESC,VER.DESC_STEP ENDTAG_DESC_STEP,TAG.TAGNAME AS ENDTAG_TAGNAME,MAP.RCSFILE AS ENDTAG_RCSFILE,MAP.FILENAME AS ENDTAG_FILENAME, MAP.PROGRAMID AS ENDTAG_PROGRAMID, MAP.MODULE AS ENDTAG_MODULE, MAP.CLIENTSERVER AS ENDTAG_CLIENTSERVER, MAP.VERSIONHEAD AS ENDTAG_VERSIONHEAD ",
						"                                        FROM TBSOPTCVSVER VER ",
						"                                            INNER JOIN TBSOPTCVSTAG TAG ",
						"                                            ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                            INNER JOIN TBSOPTCVSMAP MAP ",
						"                                            ON VER.M_SID = MAP.M_SID ",
						"                                        WHERE TAG.TAGNAME = '"
								+ endTag + "' ",
						"                            )",
						"                            ENDTAG ",
						"                            ON STARTTAG.STARTTAG_M_SID = ENDTAG.ENDTAG_M_SID ",
						"                        WHERE STARTTAG.STARTTAG_VERSION IS NULL ",
						"            )",
						"            X ",
						"            ON H.M_SID = X.ENDTAG_M_SID ",
						"        WHERE INSTR(H.VERSION,'.',1,2) = 0 AND TO_NUMBER(SUBSTR(H.VERSION,INSTR(H.VERSION,'.',1,1)+1,3)) <= TO_NUMBER(SUBSTR(X.ENDTAG_VERSION,INSTR(X.ENDTAG_VERSION,'.',1,1)+1,3)) ",
						"    )",
						"    UNION ",
						// --ENDTAG沒有的檔案(被刪除)
						"        (   SELECT H.DESC_ID ,H.FULLDESC,H.AUTHOR,H.VERDATE,X.STARTTAG_FILENAME AS FILENAME,X.STARTTAG_PROGRAMID AS PROGRAMID,X.STARTTAG_MODULE AS MODULE,H.VERSION,X.STARTTAG_VERSIONHEAD AS VERSIONHEAD ",
						"            FROM TBSOPTCVSVER H ",
						"                INNER JOIN (SELECT STARTTAG.* ",
						"                            FROM (  SELECT VER.M_SID AS STARTTAG_M_SID,VER.VERSION AS STARTTAG_VERSION,VER.AUTHOR AS STARTTAG_AUTHOR,VER.VERDATE AS STARTTAG_VERDATE,VER.FULLDESC AS STARTTAG_FULLDESC,VER.DESC_ID AS STARTTAG_DESC_ID,VER.DESC_DESC AS STARTTAG_DESC_DESC,VER.DESC_STEP STARTTAG_DESC_STEP,TAG.TAGNAME AS STARTTAG_TAGNAME,MAP.RCSFILE AS STARTTAG_RCSFILE,MAP.FILENAME AS STARTTAG_FILENAME, MAP.PROGRAMID AS STARTTAG_PROGRAMID, MAP.MODULE AS STARTTAG_MODULE, MAP.CLIENTSERVER AS STARTTAG_CLIENTSERVER, MAP.VERSIONHEAD AS STARTTAG_VERSIONHEAD ",
						"                                    FROM TBSOPTCVSVER VER ",
						"                                        INNER JOIN TBSOPTCVSTAG TAG ",
						"                                        ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                        INNER JOIN TBSOPTCVSMAP MAP ",
						"                                        ON VER.M_SID = MAP.M_SID ",
						"                                    WHERE TAG.TAGNAME = '"
								+ startTag + "' ",
						"                                )",
						"                                STARTTAG ",
						"                                FULL JOIN ( SELECT VER.M_SID AS ENDTAG_M_SID,VER.VERSION AS ENDTAG_VERSION,VER.AUTHOR AS ENDTAG_AUTHOR,VER.VERDATE AS ENDTAG_VERDATE,VER.FULLDESC AS ENDTAG_FULLDESC,VER.DESC_ID AS ENDTAG_DESC_ID,VER.DESC_DESC AS ENDTAG_DESC_DESC,VER.DESC_STEP ENDTAG_DESC_STEP,TAG.TAGNAME AS ENDTAG_TAGNAME,MAP.RCSFILE AS ENDTAG_RCSFILE,MAP.FILENAME AS ENDTAG_FILENAME, MAP.PROGRAMID AS ENDTAG_PROGRAMID, MAP.MODULE AS ENDTAG_MODULE, MAP.CLIENTSERVER AS ENDTAG_CLIENTSERVER, MAP.VERSIONHEAD AS ENDTAG_VERSIONHEAD ",
						"                                            FROM TBSOPTCVSVER VER ",
						"                                                INNER JOIN TBSOPTCVSTAG TAG ",
						"                                                ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                                INNER JOIN TBSOPTCVSMAP MAP ",
						"                                                ON VER.M_SID = MAP.M_SID ",
						"                                            WHERE TAG.TAGNAME = '"
								+ endTag + "' ",
						"                                )",
						"                                ENDTAG ",
						"                                ON STARTTAG.STARTTAG_M_SID = ENDTAG.ENDTAG_M_SID ",
						"                            WHERE ENDTAG.ENDTAG_VERSION IS NULL ",
						"                )",
						"                X ",
						"                ON H.M_SID = X.STARTTAG_M_SID ",
						"            WHERE INSTR(H.VERSION,'.',1,2) = 0 AND TO_NUMBER(SUBSTR(H.VERSION,INSTR(H.VERSION,'.',1,1)+1,3)) > TO_NUMBER(SUBSTR(X.STARTTAG_VERSION,INSTR(X.STARTTAG_VERSION,'.',1,1)+1,3)) ",
						"        )",
						"    UNION ",
						// --兩TAG間有異動過的
						"        (   SELECT H.DESC_ID ,H.FULLDESC,H.AUTHOR,H.VERDATE,X.ENDTAG_FILENAME AS FILENAME,X.ENDTAG_PROGRAMID AS PROGRAMID,X.ENDTAG_MODULE AS MODULE,H.VERSION,X.ENDTAG_VERSIONHEAD AS VERSIONHEAD ",
						"            FROM TBSOPTCVSVER H ",
						"                INNER JOIN (SELECT STARTTAG.*,ENDTAG.* ",
						"                            FROM (  SELECT VER.M_SID AS STARTTAG_M_SID,VER.VERSION AS STARTTAG_VERSION,VER.AUTHOR AS STARTTAG_AUTHOR,VER.VERDATE AS STARTTAG_VERDATE,VER.FULLDESC AS STARTTAG_FULLDESC,VER.DESC_ID AS STARTTAG_DESC_ID,VER.DESC_DESC AS STARTTAG_DESC_DESC,VER.DESC_STEP STARTTAG_DESC_STEP,TAG.TAGNAME AS STARTTAG_TAGNAME,MAP.RCSFILE AS STARTTAG_RCSFILE,MAP.FILENAME AS STARTTAG_FILENAME, MAP.PROGRAMID AS STARTTAG_PROGRAMID, MAP.MODULE AS STARTTAG_MODULE, MAP.CLIENTSERVER AS STARTTAG_CLIENTSERVER, MAP.VERSIONHEAD AS STARTTAG_VERSIONHEAD ",
						"                                    FROM TBSOPTCVSVER VER ",
						"                                        INNER JOIN TBSOPTCVSTAG TAG ",
						"                                        ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                        INNER JOIN TBSOPTCVSMAP MAP ",
						"                                        ON VER.M_SID = MAP.M_SID ",
						"                                    WHERE TAG.TAGNAME = '"
								+ startTag + "') STARTTAG ",
						"                                        FULL JOIN ( SELECT VER.M_SID AS ENDTAG_M_SID,VER.VERSION AS ENDTAG_VERSION,VER.AUTHOR AS ENDTAG_AUTHOR,VER.VERDATE AS ENDTAG_VERDATE,VER.FULLDESC AS ENDTAG_FULLDESC,VER.DESC_ID AS ENDTAG_DESC_ID,VER.DESC_DESC AS ENDTAG_DESC_DESC,VER.DESC_STEP ENDTAG_DESC_STEP,TAG.TAGNAME AS ENDTAG_TAGNAME,MAP.RCSFILE AS ENDTAG_RCSFILE,MAP.FILENAME AS ENDTAG_FILENAME, MAP.PROGRAMID AS ENDTAG_PROGRAMID, MAP.MODULE AS ENDTAG_MODULE, MAP.CLIENTSERVER AS ENDTAG_CLIENTSERVER, MAP.VERSIONHEAD AS ENDTAG_VERSIONHEAD ",
						"                                                    FROM TBSOPTCVSVER VER ",
						"                                                        INNER JOIN TBSOPTCVSTAG TAG ",
						"                                                        ON VER.M_SID = TAG.M_SID AND VER.VERSION = TAG.VERSION ",
						"                                                        INNER JOIN TBSOPTCVSMAP MAP ",
						"                                                        ON VER.M_SID = MAP.M_SID ",
						"                                                    WHERE TAG.TAGNAME = '"
								+ endTag + "' ",
						"                                        )",
						"                                        ENDTAG ",
						"                                        ON STARTTAG.STARTTAG_M_SID = ENDTAG.ENDTAG_M_SID ",
						"                                    WHERE STARTTAG.STARTTAG_M_SID = ENDTAG.ENDTAG_M_SID AND STARTTAG.STARTTAG_VERSION != ENDTAG.ENDTAG_VERSION ",
						"                                )",
						"                                X ON H.M_SID = X.STARTTAG_M_SID ",
						"                            WHERE INSTR(H.VERSION,'.',1,2) = 0 AND TO_NUMBER(SUBSTR(H.VERSION,INSTR(H.VERSION,'.',1,1)+1,3)) > TO_NUMBER(SUBSTR(X.STARTTAG_VERSION,INSTR(X.STARTTAG_VERSION,'.',1,1)+1,3)) AND TO_NUMBER(SUBSTR(H.VERSION,INSTR(H.VERSION,'.',1,1)+1,3)) <= TO_NUMBER(SUBSTR(X.ENDTAG_VERSION,INSTR(X.ENDTAG_VERSION,'.',1,1)+1,3)) ",
						"                )", "        ) ");

		StringBuffer querySql = StringUtil.concatBuf("select * from "
				+ tableName + " where 1=1 ");

		if (module.size() > 0) {
			for (String m : module) {
				querySql.append("AND MODULE != '" + m + "' ");
				CVSConst.CUSTOMIZED_MODULE customized_module = CVSConst.CUSTOMIZED_MODULE
						.findByModule(m);
				if (customized_module != null) {
					querySql.append("AND INSTR(FILENAME ,'"
							+ customized_module.getBrk() + "',1,1) = 0 ");
				}
			}

		}

		try {
			session = SessionUtil.openSession();

			dao.executeSQL(createSql.toString());

			SQLQuery query = session.createSQLQuery(querySql.toString());
			query.setResultTransformer(Transformers
					.aliasToBean(TagDiffDO.class));
			objList = query.list();
			dao.executeSQL("drop table " + tableName);
		} catch (HibernateException e) {
			throw e;
		} finally {
			SessionUtil.closeSession(session);
			session = null;
		}

		return objList;
	}
}
