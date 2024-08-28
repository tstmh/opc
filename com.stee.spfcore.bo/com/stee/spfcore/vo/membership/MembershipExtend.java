package com.stee.spfcore.vo.membership;

import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.personnel.PersonalDetail;

public class MembershipExtend {
    
    private Membership membership;
    
    private PersonalDetail personalDetail;

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public PersonalDetail getPersonalDetail() {
        return personalDetail;
    }

    public void setPersonalDetail(PersonalDetail personalDetail) {
        this.personalDetail = personalDetail;
    }

    @Override
    public String toString() {
        return "MembershipExtend [membership=" + membership.toString()
                + ", personalDetail=" + personalDetail.toString() + "]";
    }
    
    

}
