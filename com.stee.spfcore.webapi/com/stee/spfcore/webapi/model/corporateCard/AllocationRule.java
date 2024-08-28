package com.stee.spfcore.webapi.model.corporateCard;

public enum AllocationRule {

	FIRST_COME_FIRST_SERVED( "First-Come-First-Served" ), 
    FIRST_COME_FIRST_SERVED_APPROVAL( "First-Come-First-Served with Approval" ), 
    LEAST_USED( "Least Used" ), 
    BALLOTING( "Balloting" ), 
    NONE( "None" );

    private String allocationRuleText;

    private AllocationRule( String allocationRuleText ) {
        this.allocationRuleText = allocationRuleText;
    }

    public static AllocationRule get( String allocationRuleText ) {
        AllocationRule allocationRule = NONE;
        if ( allocationRuleText != null ) {
            for ( AllocationRule tempAllocationRule : AllocationRule.values() ) {
                if ( allocationRuleText.equals( tempAllocationRule.allocationRuleText ) ) {
                    allocationRule = tempAllocationRule;
                    break;
                }
            }
        }
        return allocationRule;
    }

    public String toString() {
        return this.allocationRuleText;
    }
    
}
