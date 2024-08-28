package com.stee.spfcore.model.corporateCard;

public enum BroadcastFrequency {
    WEEKLY( "Weekly" ), FORTNIGHTLY( "Fortnightly" ), MONTHLY( "Monthly" ), QUARTERLY( "Quarterly" ), NONE( "None" );

    private String broadcastFrequencyText;

    private BroadcastFrequency( String broadcastFrequencyText ) {
        this.broadcastFrequencyText = broadcastFrequencyText;
    }

    public static BroadcastFrequency get( String broadcastFrequencyText ) {
        BroadcastFrequency broadcastFrequency = NONE;
        if ( broadcastFrequencyText != null ) {
            for ( BroadcastFrequency tempBroadcastFrequency : BroadcastFrequency.values() ) {
                if ( broadcastFrequencyText.equals( tempBroadcastFrequency.broadcastFrequencyText ) ) {
                    broadcastFrequency = tempBroadcastFrequency;
                    break;
                }
            }
        }
        return broadcastFrequency;
    }

    @Override
    public String toString() {
        return this.broadcastFrequencyText;
    }
}
