package com.stee.spfcore.vo.membership;

public class DiscrepancyRecordCriteria {
    private String nric;
    private Integer month;
    private Integer year;

    public DiscrepancyRecordCriteria() {
    }

    public DiscrepancyRecordCriteria( String nric, Integer month, Integer year ) {
        this.nric = nric;
        this.month = month;
        this.year = year;
    }

    public String getNric() {
        return nric;
    }

    public void setNric( String nric ) {
        this.nric = nric;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth( Integer month ) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear( Integer year ) {
        this.year = year;
    }
}
