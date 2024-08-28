package com.stee.spfcore.vo.userRoleManagement;

import java.util.ArrayList;
import java.util.List;

public class SearchBpmsGroupResults {
    private List< String > responseMsgs = new ArrayList< String >();
    private List< BpmsGroup > groupResults = new ArrayList< BpmsGroup >();

    public SearchBpmsGroupResults() {
        // This method is intentionally left empty
    }

    public List< String > getResponseMsgs() {
        return responseMsgs;
    }

    public void setResponseMsgs( List< String > responseMsgs ) {
        this.responseMsgs = responseMsgs;
    }

    public List< BpmsGroup > getGroupResults() {
        return groupResults;
    }

    public void setGroupResults( List< BpmsGroup > groupResults ) {
        this.groupResults = groupResults;
    }
}
