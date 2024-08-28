package com.stee.spfcore.vo.userRoleManagement;

public class BpmsUser {
    private String userId; //added to get the user ID from bpmdb
    private String userCode; // user id
    private String department; // department description
    private String userGroupName; // role
    private String userName;
    private String spfOfficer; // based on service type, red mine #1510
    private String employmentStatus;
    private String subunit; // subunit description

    private transient String departmentCode; // department code
    private transient String subunitCode; // subnit code

    public BpmsUser() {
        // This method is intentionally left empty
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode( String departmentCode ) {
        this.departmentCode = departmentCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode( String userCode ) {
        this.userCode = userCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName( String userGroupName ) {
        this.userGroupName = userGroupName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public String getSpfOfficer() {
        return spfOfficer;
    }

    public void setSpfOfficer( String spfOfficer ) {
        this.spfOfficer = spfOfficer;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus( String employmentStatus ) {
        this.employmentStatus = employmentStatus;
    }

    public String getSubUnit() {
        return subunit;
    }

    public void setSubUnit(String subunit) {
        this.subunit = subunit;
    }

    public String getSubUnitCode() {
        return subunitCode;
    }

    public void setSubUnitCode(String subunitCode) {
        this.subunitCode = subunitCode;
    }

    public String toString() {
        return String.format( "userId=%s, userCode=%s, userName=%s, userGroupName=%s, department=%s, subunit=%s, spfOfficer=%s, employmentStatus=%s", this.userId, this.userCode, this.userName, this.userGroupName, this.department, this.subunit, this.spfOfficer, this.employmentStatus );
    }
}
