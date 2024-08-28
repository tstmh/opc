package com.stee.spfcore.model.personnel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table( name = "\"PERSONAL_EMPLOYMENT_DETAILS\"", schema = "\"SPFCORE\"" )
@XStreamAlias("Employment")
@Audited
public class Employment {
    @Id
    @Column( name = "\"NRIC\"", length = 10 )
    private String nric;

    @Column( name = "\"ORGANISATION_OR_DEPARTMENT\"", length = 50 )
    private String organisationOrDepartment;

    @Column( name = "\"SUBUNIT\"", length = 50 )
    private String subunit;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_ENLISTMENT\"" )
    private Date dateOfEnlistment;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_APPOINTMENT\"" )
    private Date dateOfAppointment;

    @Temporal( TemporalType.DATE )
    @Column( name = "\"DATE_OF_RETIREMENT\"" )
    private Date dateOfRetirement;

    @Column( name = "\"DESIGNATION\"", length = 50 )
    private String designation;

    @Column( name = "\"RANK_OR_GRADE\"", length = 50 )
    private String rankOrGrade;

    @Column( name = "\"SERVICE_TYPE\"", length = 50 )
    private String serviceType;

    @Column( name = "\"TENURE_OF_SERVICE\"", length = 50 )
    private String tenureOfService;

    @Column( name = "\"MEDICAL_SCHEME\"", length = 50 )
    private String medicalScheme;

    @Column( name = "\"DIVISION_STATUS\"", length = 50 )
    private String divisionStatus;

    @Column( name = "\"SCHEME_OF_SERVICE\"", length = 50 )
    private String schemeOfService;

    @Column( name = "\"EMPLOYMENT_STATUS\"", length = 50 )
    private String employmentStatus;
    
    @Temporal( TemporalType.DATE )
    @Column( name = "\"LEAVING_SERVICE_DATE\"" )
    private Date leavingServiceDate;

    @Column( name = "\"GROSS_MONTHLY_SALARY\"" )
    private Double salary;
    
    public Employment() {
        super();
    }

    public Employment( String nric ) {
        super();
        this.nric = nric;
    }

    public String getOrganisationOrDepartment() {
        return organisationOrDepartment;
    }

    public void setOrganisationOrDepartment( String organisationOrDepartment ) {
        this.organisationOrDepartment = organisationOrDepartment;
    }

    public String getSubunit() {
        return subunit;
    }

    public void setSubunit( String subunit ) {
        this.subunit = subunit;
    }

    public Date getDateOfEnlistment() {
        return dateOfEnlistment;
    }

    public void setDateOfEnlistment( Date dateOfEnlistment ) {
        this.dateOfEnlistment = dateOfEnlistment;
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment( Date dateOfAppointment ) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Date getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement( Date dateOfRetirement ) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation( String designation ) {
        this.designation = designation;
    }

    public String getRankOrGrade() {
        return rankOrGrade;
    }

    public void setRankOrGrade( String rankOrGrade ) {
        this.rankOrGrade = rankOrGrade;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType( String serviceType ) {
        this.serviceType = serviceType;
    }

    public String getTenureOfService() {
        return tenureOfService;
    }

    public void setTenureOfService( String tenureOfService ) {
        this.tenureOfService = tenureOfService;
    }

    public String getMedicalScheme() {
        return medicalScheme;
    }

    public void setMedicalScheme( String medicalScheme ) {
        this.medicalScheme = medicalScheme;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public String getDivisionStatus() {
        return divisionStatus;
    }

    public void setDivisionStatus( String divisionStatus ) {
        this.divisionStatus = divisionStatus;
    }

    public String getSchemeOfService() {
        return schemeOfService;
    }

    public void setSchemeOfService( String schemeOfService ) {
        this.schemeOfService = schemeOfService;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus( String employmentStatus ) {
        this.employmentStatus = employmentStatus;
    }
    
    public Double getSalary() {
    	return salary;
    }

    public void setSalary(Double salary) {
    	this.salary = salary;
    }

		public Date getLeavingServiceDate() {
			return leavingServiceDate;
		}

		public void setLeavingServiceDate(Date leavingServiceDate) {
			this.leavingServiceDate = leavingServiceDate;
		}

		
		public void preSave () {
			if (nric != null) {
				nric = nric.toUpperCase();
			}
		}
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((nric == null) ? 0 : nric.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Employment other = (Employment) obj;
			if (nric == null) {
				if (other.nric != null)
					return false;
			} else if (!nric.equals(other.nric))
				return false;
			return true;
		}
    
    
    
}
