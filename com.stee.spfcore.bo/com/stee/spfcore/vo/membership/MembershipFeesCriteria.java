package com.stee.spfcore.vo.membership;

import java.util.Date;

import com.stee.spfcore.model.ActivationStatus;

/**
 * 
 * @ClassName: MembershipFeesCriteria
 * @Description: Membership Fees Criteria
 * @author yu.yunxia
 * @date Aug 12, 2016 11:18:26 AM
 *
 */
public class MembershipFeesCriteria {

    private Date currentDate;
    
    private String leaveType;
    
    private ActivationStatus membershipStatus;

    
    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public ActivationStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(ActivationStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    @Override
    public String toString() {
        return "MembershipFeesCriteria [currentDate=" + currentDate
                + ", leaveType=" + leaveType + ", membershipStatus="
                + membershipStatus + "]";
    }

}
