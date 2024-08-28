package com.stee.spfcore.vo.personnel;

public class SearchPersonnelResult {
    private String nric;
    private String name;
    private String organisationOrDepartment;
    private String subunit;

    public SearchPersonnelResult( String nric, String name, String organisationOrDepartment, String subunit ) {
        this.nric = nric;
        this.name = name;
        this.organisationOrDepartment = organisationOrDepartment;
        this.subunit = subunit;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getSubunit() {
        return subunit;
    }

    public void setSubunit( String subunit ) {
        this.subunit = subunit;
    }

    public String getOrganisationOrDepartment() {
        return organisationOrDepartment;
    }

    public void setOrganisationOrDepartment( String organisationOrDepartment ) {
        this.organisationOrDepartment = organisationOrDepartment;
    }
}
