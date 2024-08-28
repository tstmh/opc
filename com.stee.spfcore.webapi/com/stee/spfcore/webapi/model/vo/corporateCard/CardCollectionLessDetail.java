package com.stee.spfcore.webapi.model.vo.corporateCard;

public class CardCollectionLessDetail {
    private String serialNumber;
    private String cardStatus;
    private String collectTakeOverFrom;
    private String collectTakeOverOn;
    private String returnHandoverTo;
    private String returnHandoverOn;

    public CardCollectionLessDetail() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber( String serialNumber ) {
        this.serialNumber = serialNumber;
    }

    public String getCollectTakeOverFrom() {
        return collectTakeOverFrom;
    }

    public void setCollectTakeOverFrom( String collectTakeOverFrom ) {
        this.collectTakeOverFrom = collectTakeOverFrom;
    }

    public String getCollectTakeOverOn() {
        return collectTakeOverOn;
    }

    public void setCollectTakeOverOn( String collectTakeOverOn ) {
        this.collectTakeOverOn = collectTakeOverOn;
    }

    public String getReturnHandoverTo() {
        return returnHandoverTo;
    }

    public void setReturnHandoverTo( String returnHandoverTo ) {
        this.returnHandoverTo = returnHandoverTo;
    }

    public String getReturnHandoverOn() {
        return returnHandoverOn;
    }

    public void setReturnHandoverOn( String returnHandoverOn ) {
        this.returnHandoverOn = returnHandoverOn;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus( String cardStatus ) {
        this.cardStatus = cardStatus;
    }

}
