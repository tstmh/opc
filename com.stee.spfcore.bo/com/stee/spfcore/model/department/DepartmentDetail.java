package com.stee.spfcore.model.department;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.stee.spfcore.model.Address;
import com.stee.spfcore.model.annotation.GeneratedId;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "DEPARTMENT_DETAILS", schema = "SPFCORE" )
@XStreamAlias( "DepartmentDetail" )
@Audited
public class DepartmentDetail {
	@Id
    @GenericGenerator( name = "DepartmentDetailGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator" )
    @GeneratedValue( generator = "DepartmentDetailGenerator" )
    @Column( name = "ID" )
    private String id;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "DEPARTMENT_DETAILS_DEPTS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "DEPARTMENT_DETAILS_ID" ) )
    @Column( name = "DEPT" )
    @Fetch( value = FetchMode.SUBSELECT )
    private List< String > departments;

    @Embedded
    private Address address;
    
    @Temporal(TemporalType.DATE)
	@Column(name = "SUBMISSION_DATE")
	private Date dateOfSubmission;

	@Column(name = "SUBMITTED_BY", length = 50)
	private String submittedBy;
	
	@Column(name = "DEPARTMENT_IN_CHARGE", length = 50)
	private String departmentInCharge;
	
	@Column(name = "IS_SESSION_DONE_IN_SETUP")
	private boolean isSessionDoneInSetup;

    public DepartmentDetail() {
		// DO NOTHING
    }

    public String getId() {
		return id;
	}

    public void setId(String id) {
		this.id = id;
	}
    
    public List< String > getDepartments() {
        return departments;

    }

    public void setDepartments( List< String > departments ) {
        if ( this.departments == null ) {
            this.departments = new ArrayList<>();
        }
        this.departments.clear();
        if ( departments != null ) {
            this.departments.addAll( departments );
        }
    }

	public Address getAddress() {
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
    }

	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public String getDepartmentInCharge() {
		return departmentInCharge;
	}

	public void setDepartmentInCharge(String departmentInCharge) {
		this.departmentInCharge = departmentInCharge;
	}
	
	public Boolean getIsSessionDoneInSetup() {
		return isSessionDoneInSetup;
	}
	
	public void setIsSessionDoneInSetup(boolean isSessionDoneInSetup) {
		this.isSessionDoneInSetup = isSessionDoneInSetup;
	}

	@Override
	public String toString() {
		return "DepartmentDetail [id=" + id + ", departments=" + departments
				+ ", address=" + address + ", dateOfSubmission="
				+ dateOfSubmission + ", submittedBy=" + submittedBy
				+ ", departmentInCharge=" + departmentInCharge 
				+ ", isSessionDoneInSetup=" + isSessionDoneInSetup + "]";
	}
}
