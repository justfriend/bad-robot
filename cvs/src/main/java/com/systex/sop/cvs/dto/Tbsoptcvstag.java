package com.systex.sop.cvs.dto;

// Generated Jan 11, 2012 3:55:55 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Tbsoptcvstag generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TBSOPTCVSTAG")
public class Tbsoptcvstag implements java.io.Serializable {

	private TbsoptcvstagId id;
	private Tbsoptcvsver tbsoptcvsver;

	public Tbsoptcvstag() {
	}

	public Tbsoptcvstag(TbsoptcvstagId id, Tbsoptcvsver tbsoptcvsver) {
		this.id = id;
		this.tbsoptcvsver = tbsoptcvsver;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "MSid", column = @Column(name = "M_SID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "version", column = @Column(name = "VERSION", nullable = false, length = 20)),
			@AttributeOverride(name = "tagname", column = @Column(name = "TAGNAME", nullable = false, length = 100)) })
	public TbsoptcvstagId getId() {
		return this.id;
	}

	public void setId(TbsoptcvstagId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "M_SID", referencedColumnName = "M_SID", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "VERSION", referencedColumnName = "VERSION", nullable = false, insertable = false, updatable = false) })
	public Tbsoptcvsver getTbsoptcvsver() {
		return this.tbsoptcvsver;
	}

	public void setTbsoptcvsver(Tbsoptcvsver tbsoptcvsver) {
		this.tbsoptcvsver = tbsoptcvsver;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
