package com.stee.spfcore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = 8660553952811468302L;

	@Column(name = "\"BLOCK_NUMBER\"", length = 10)
	private String blockNumber;

	@Column(name = "\"STREET_NAME\"", length = 32)
	private String streetName;

	@Column(name = "\"FLOOR_NUMBER\"", length = 3)
	private String floorNumber;

	@Column(name = "\"UNIT_NUMBER\"", length = 5)
	private String unitNumber;

	@Column(name = "\"BUILDING_NAME\"", length = 32)
	private String buildingName;

	@Column(name = "\"POSTAL_CODE\"", length = 6)
	private String postalCode;

	@Column(name = "\"RESIDENTIAL_ADDRESS\"", length = 500)
	private String residentialAddress;

	
	public Address() {
		super();
	}

	public Address(String blockNumber, String streetName, String floorNumber, String unitNumber, String buildingName,
			String postalCode) {
		super();
		this.blockNumber = blockNumber;
		this.streetName = streetName;
		this.floorNumber = floorNumber;
		this.unitNumber = unitNumber;
		this.buildingName = buildingName;
		this.postalCode = postalCode;
	}

	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
	
	public void buildResidentialAddress() {

		StringBuilder builder = new StringBuilder();

		if ((blockNumber != null) && (blockNumber.length() > 0)) {
			builder.append(blockNumber);
		}

		if ((streetName != null) && (streetName.length() > 0)) {
			builder.append(builder.length() > 0 ? " " : "");
			builder.append(streetName);
		}

		if ((floorNumber != null) && (floorNumber.length() > 0)) {
			builder.append(builder.length() > 0 ? " #" : "#");
			builder.append(floorNumber);
		}

		if ((unitNumber != null) && (unitNumber.length() > 0)) {
			builder.append(((floorNumber != null) && (floorNumber.length() > 0)) ? "-" : "");
			builder.append(unitNumber);
		}

		if ((buildingName != null) && (buildingName.length() > 0)) {
			builder.append(builder.length() > 0 ? " " : "");
			builder.append(buildingName);
		}

		if ((postalCode != null) && (postalCode.length() > 0)) {
			builder.append(builder.length() > 0 ? " " : "");
			builder.append("S(").append(postalCode).append(")");
		}
		
		if (builder.length() == 0) {
			residentialAddress = "-";
		} else {
			residentialAddress = builder.toString();
		}
	}
}
