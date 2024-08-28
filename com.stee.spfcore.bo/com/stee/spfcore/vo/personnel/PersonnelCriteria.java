package com.stee.spfcore.vo.personnel;

import java.util.Date;

/**
 * 
 * @ClassName: PersonnelCriteria 
 * @Description: Personnel Criteria
 * @author yu.yunxia
 * @date Sep 13, 2016 6:34:10 PM 
 *
 */
public class PersonnelCriteria {
    
    private String queryNRIC;
    
    private Date queryStartDate;
    
    private Date queryEndDate;

    public String getQueryNRIC() {
        return queryNRIC;
    }

    public void setQueryNRIC(String queryNRIC) {
        this.queryNRIC = queryNRIC;
    }

    public Date getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(Date queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public Date getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(Date queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    @Override
    public String toString() {
        return "PersonnelCriteria [queryNRIC=" + queryNRIC
                + ", queryStartDate=" + queryStartDate + ", queryEndDate="
                + queryEndDate + "]";
    }
}
