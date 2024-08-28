package com.stee.spfcore.model.hrps.internal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "MEMBERSHIP_DEDUCTION_DETAILS", schema = "SPFCORE")
@XStreamAlias("MembershipDeductionDetails")
@Audited
@SequenceDef (name="MembershipDeductionDetails_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")

public class MembershipDeductionDetail {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"NRIC\"")
	private String nric;
	
	@Column(name = "\"NAME\"")
	private String name;
	
	@Column(name = "\"UNIT\"")
	private String unit;
	
	@Column(name = "\"RANK\"")
	private String rank;
	
	@Column(name = "\"TYPE\"")
	private DeductionType type;
	
	@Column(name = "\"MONTH\"")
	private int month;
	
	@Column(name = "\"YEAR\"")
	private int year;
	
	@Column(name = "\"AMOUNT\"")
	private double amount;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"REFERENCE_DATE\"")
	private Date referenceDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	@Column(name = "\"UPDATED_BY\"")
	private String updatedBy;
	
	public MembershipDeductionDetail () {
		super();
	}

	public MembershipDeductionDetail(String id, String nric, String name,
			String unit, String rank, DeductionType type, int month, 
			int year, double amount, Date referenceDate, Date updatedDate, String updatedBy) {
		super();
		this.id = id;
		this.nric = nric;
		this.name = name;
		this.unit = unit;
		this.rank = rank;
		this.type = type;
		this.month = month;
		this.year = year;
		this.amount = amount;
		this.referenceDate = referenceDate;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public DeductionType getType() {
		return type;
	}

	public void setType(DeductionType type) {
		this.type = type;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
