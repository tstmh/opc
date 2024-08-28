package com.stee.spfcore.vo.personnel;

import java.util.List;

public class SearchPersonnelResults {
    private String message;
    private List< SearchPersonnelResult > resultList;

    public SearchPersonnelResults() {
        // DO NOTHING
    }

    public List< SearchPersonnelResult > getResultList() {
        return resultList;
    }

    public void setResultList( List< SearchPersonnelResult > resultList ) {
        this.resultList = resultList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }
}
