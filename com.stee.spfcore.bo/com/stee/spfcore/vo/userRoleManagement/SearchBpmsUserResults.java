package com.stee.spfcore.vo.userRoleManagement;

import java.util.ArrayList;
import java.util.List;

public class SearchBpmsUserResults {
    private List< String > responseMsgs = new ArrayList< String >();
    private List< BpmsUser > userResults = new ArrayList< BpmsUser >();

    public SearchBpmsUserResults() {
        // This method is intentionally left empty
    }

    public List< String > getResponseMsgs() {
        return responseMsgs;
    }

    public void setResponseMsgs( List< String > responseMsgs ) {
        this.responseMsgs = responseMsgs;
    }

    public List< BpmsUser > getUserResults() {
        return userResults;
    }

    public void setUserResults( List< BpmsUser > userResults ) {
        this.userResults = userResults;
    }
}
