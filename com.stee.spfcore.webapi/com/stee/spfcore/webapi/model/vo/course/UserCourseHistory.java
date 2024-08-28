package com.stee.spfcore.webapi.model.vo.course;

import java.util.Date;

import com.stee.spfcore.webapi.model.course.ParticipantStatus;

public class UserCourseHistory {

    private String title;

    private Date startDate;

    private Date endDate;

    private ParticipantStatus status;

    private boolean attended;

    private String withdrawalReason;

    public UserCourseHistory() {
        super();
    }

    public UserCourseHistory( String title, Date startDate, Date endDate, ParticipantStatus status, boolean attended, String withdrawalReason ) {
        super();
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.attended = attended;
        this.withdrawalReason = withdrawalReason;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason( String withdrawalReason ) {
        this.withdrawalReason = withdrawalReason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus( ParticipantStatus status ) {
        this.status = status;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended( boolean attended ) {
        this.attended = attended;
    }

}
