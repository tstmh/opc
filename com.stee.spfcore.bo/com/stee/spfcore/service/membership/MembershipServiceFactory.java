package com.stee.spfcore.service.membership;

import com.stee.spfcore.service.membership.impl.MembershipService;

public class MembershipServiceFactory {

	private MembershipServiceFactory(){}
	private static IMembershipService membershipService;
	
	public static synchronized IMembershipService getMembershipService () {
		if (membershipService == null) {
			membershipService = createMembershipService ();
		}
		return membershipService;
	}
	
	private static IMembershipService createMembershipService () {
		
		return new MembershipService();
		
	}
}
