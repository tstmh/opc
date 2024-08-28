package com.stee.spfcore.vo.personnel;

public class PersonalNricName {
    private String nric;
    private String name;

    public PersonalNricName() {
    }

    public PersonalNricName( String nric, String name ) {
        this.nric = nric;
        this.name = name;
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
}
