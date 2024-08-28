package com.stee.spfcore.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Table( name = "USER_ACCOUNT_DETAILS", schema = "SPFCORE" )
@Audited
public class UserAccountDetail {
	
	@Id
    @Column( name = "\"NRIC\"", length = 10 )
    private String nric;
	
	@Column (name = "\"STATUS\"", length = 1)
	private String status;
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column (name = "\"LOGIN_DATE_TIME\"")
	private Date loginDateTime;
	
	@Temporal ( TemporalType.TIMESTAMP )
	@Column (name = "\"UPDATED_DATE\"")
	private Date updatedDate;
	
	@Column (name = "\"UPDATED_BY\"" )
	private String updatedBy;

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
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
