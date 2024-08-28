package com.stee.spfcore.model.corporateCard;

public enum BroadcastType {
    AUTOMATIC( "Auto-Broadcast" ), MANUAL( "Manual-Broadcast" ), NONE( "None" );

    private String broadcastTypeText;

    private BroadcastType( String broadcastTypeText ) {
        this.broadcastTypeText = broadcastTypeText;
    }

    public static BroadcastType get( String broadcastTypeText ) {
        BroadcastType broadcastType = NONE;
        if ( broadcastTypeText != null ) {
            for ( BroadcastType tempBroadcastType : BroadcastType.values() ) {
                if ( broadcastTypeText.equals( tempBroadcastType.broadcastTypeText ) ) {
                    broadcastType = tempBroadcastType;
                    break;
                }
            }
        }
        return broadcastType;
    }

    @Override
    public String toString() {
        return this.broadcastTypeText;
    }
}
