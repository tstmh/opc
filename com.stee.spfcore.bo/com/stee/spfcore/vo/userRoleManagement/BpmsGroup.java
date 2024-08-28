package com.stee.spfcore.vo.userRoleManagement;

public class BpmsGroup {
    private String groupName;
    private String groupDescription;

    public BpmsGroup() {
        // DO NOTHING
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName( String groupName ) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription( String groupDescription ) {
        this.groupDescription = groupDescription;
    }

    public String toString() {
        return String.format( "groupName=%s, groupDescription=%s", this.groupName, this.groupDescription );
    }

}
