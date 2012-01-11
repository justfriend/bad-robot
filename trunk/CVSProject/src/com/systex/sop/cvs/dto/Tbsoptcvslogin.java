package com.systex.sop.cvs.dto;

// Generated Jan 11, 2012 3:55:55 PM by Hibernate Tools 3.3.0.GA

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tbsoptcvslogin generated by hbm2java
 */
@Entity
@Table(name = "TBSOPTCVSLOGIN")
public class Tbsoptcvslogin implements java.io.Serializable {

	private Long sid;
	private char flag;
	private char status;
	private String description;
	private String creator;
	private Timestamp createtime;
	private String modifier;
	private Timestamp lastupdate;

	public Tbsoptcvslogin() {
	}

	public Tbsoptcvslogin(Long sid, char flag, char status, String description) {
		this.sid = sid;
		this.flag = flag;
		this.status = status;
		this.description = description;
	}

	public Tbsoptcvslogin(Long sid, char flag, char status, String description,
			String creator, Timestamp createtime, String modifier,
			Timestamp lastupdate) {
		this.sid = sid;
		this.flag = flag;
		this.status = status;
		this.description = description;
		this.creator = creator;
		this.createtime = createtime;
		this.modifier = modifier;
		this.lastupdate = lastupdate;
	}

	@Id
	@Column(name = "SID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSid() {
		return this.sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	@Column(name = "FLAG", nullable = false, length = 1)
	public char getFlag() {
		return this.flag;
	}

	public void setFlag(char flag) {
		this.flag = flag;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public char getStatus() {
		return this.status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 3000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CREATOR", length = 50)
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME")
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	@Column(name = "MODIFIER", length = 50)
	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTUPDATE")
	public Timestamp getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

}
