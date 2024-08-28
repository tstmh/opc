package com.stee.spfcore.model.course;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Embeddable
public class RegistrationConfig {

	@Temporal(TemporalType.DATE)
	@Column(name = "\"OPEN_DATE_TIME\"")
	private Date openDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "\"CLOSE_DATE_TIME\"")
	private Date closeDate;
	
	@Column(name = "\"REQUIRE_VEHICLE_NUMBER\"")
	private boolean requireVehicleNumber;
	
	@Column(name = "\"REQUIRE_DIETARY_PREFERENCE\"")
	private boolean requireDietaryPreference;
	
	@ElementCollection( fetch = FetchType.EAGER )
  @CollectionTable( name = "COURSE_DIETARY_OPTIONS", schema = "SPFCORE", joinColumns = @JoinColumn( name = "COURSE_ID" ) )
  @Column( name = "DIETARY_OPTION" )
  @Fetch( value = FetchMode.SELECT )
	private List<String> dietaryOptions;

	@Column(name = "\"AUTO_OUTCOME_BROADCAST\"")
	private boolean autoOutcomeBroadcast; 
	
	@Column(name = "\"OUTCOME_BROADCASTED\"")
	private boolean outcomeBroadcasted;
	
	public RegistrationConfig() {
		super();
	}

	public RegistrationConfig(Date openDate, Date closeDate, boolean requireVehicleNumber,
			boolean requireDietaryPreference, List<String> dietaryOptions, boolean autoOutcomeBroadcast, boolean outcomeBroadcasted) {
		super();
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.requireVehicleNumber = requireVehicleNumber;
		this.requireDietaryPreference = requireDietaryPreference;
		this.dietaryOptions = dietaryOptions;
		this.autoOutcomeBroadcast = autoOutcomeBroadcast;
		this.outcomeBroadcasted = outcomeBroadcasted;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public boolean isRequireVehicleNumber() {
		return requireVehicleNumber;
	}

	public void setRequireVehicleNumber(boolean requireVehicleNumber) {
		this.requireVehicleNumber = requireVehicleNumber;
	}

	public boolean isRequireDietaryPreference() {
		return requireDietaryPreference;
	}

	public void setRequireDietaryPreference(boolean requireDietaryPreference) {
		this.requireDietaryPreference = requireDietaryPreference;
	}

	public List<String> getDietaryOptions() {
		return dietaryOptions;
	}

	public void setDietaryOptions(List<String> dietaryOptions) {
		this.dietaryOptions = dietaryOptions;
	}

	public boolean isAutoOutcomeBroadcast() {
		return autoOutcomeBroadcast;
	}

	public void setAutoOutcomeBroadcast(boolean autoOutcomeBroadcast) {
		this.autoOutcomeBroadcast = autoOutcomeBroadcast;
	}

	public boolean isOutcomeBroadcasted() {
		return outcomeBroadcasted;
	}

	public void setOutcomeBroadcasted(boolean outcomeBroadcasted) {
		this.outcomeBroadcasted = outcomeBroadcasted;
	}

	
}
