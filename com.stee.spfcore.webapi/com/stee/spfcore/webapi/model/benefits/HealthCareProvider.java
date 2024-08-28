package com.stee.spfcore.webapi.model.benefits;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"HEALTHCARE_PROVIDER\"", schema = "\"SPFCORE\"")
@XStreamAlias("HealthCareProvider")
@Audited
public class HealthCareProvider {
    
    @Id
    @Column(name="\"SERVICE_PROVIDER\"")
    @ColumnTransformer(write = "UPPER(?)")
    private String serviceProvider;
    
    @Column(name="\"CONTACT_PERSON\"")
    @ColumnTransformer(write = "UPPER(?)")
    private String contactPerson;

    @Column(name="\"EMAIL\"", length=256)
    @ColumnTransformer(write = "UPPER(?)")
    private String email;
    
    @OneToMany(mappedBy="healthCareProvider", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WeightMgmtSubsidy> weightMgmtSubsidys;
   
    public HealthCareProvider() {
        super();
    }

    public HealthCareProvider(String serviceProvider, String contactPerson,
            String email) {
        super();
        this.serviceProvider = serviceProvider;
        this.contactPerson = contactPerson;
        this.email = email;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<WeightMgmtSubsidy> getWeightMgmtSubsidys() {
		return weightMgmtSubsidys;
	}

	public void setWeightMgmtSubsidys(List<WeightMgmtSubsidy> weightMgmtSubsidys) {
		this.weightMgmtSubsidys = weightMgmtSubsidys;
	}

	@Override
	public String toString() {
		return "HealthCareProvider [serviceProvider=" + serviceProvider
				+ ", contactPerson=" + contactPerson + ", email=" + email + "]";
	}

}
