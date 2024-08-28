package com.stee.spfcore.vo.membership;

import com.stee.spfcore.model.membership.PaymentDataSource;

public class PaymentHistoryCriteria {
    private Integer month;
    private Integer year;
    private PaymentDataSource source;

    public PaymentHistoryCriteria() {
    }

    public PaymentHistoryCriteria( Integer month, Integer year, PaymentDataSource source ) {
        this.month = month;
        this.year = year;
        this.source = source;
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

    public PaymentDataSource getSource() {
        return source;
    }

    public void setSource( PaymentDataSource source ) {
        this.source = source;
    }
}
