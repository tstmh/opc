package com.stee.spfcore.vo.corporateCard;

import java.util.List;

import com.stee.spfcore.model.corporateCard.CardDetail;
import com.stee.spfcore.model.corporateCard.CardType;
import com.stee.spfcore.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.model.corporateCard.CardTypeDetailStatus;

public class CardTypeLessDetail {
    private String cardTypeId;
    private CardType type;
    private String department;
    private String departmentDescription;
    private String name;
    private int availableNumberOfCardsToday;
    private int activeNumberOfCardsToday;

    private int totalNumberOfCards;
    private String displayName;
    private int availableNumberOfCardsInDepartmentToday;
    
    private CardTypeDetailStatus status;

    public CardTypeLessDetail() {
    }

    public CardTypeLessDetail( CardTypeDetail cardTypeDetail, String departmentDescription ) {
        this( cardTypeDetail, departmentDescription, 0 );
    }

    public CardTypeLessDetail( CardTypeDetail cardTypeDetail, String departmentDescription, int availableNumberOfCardsToday ) {
        this.cardTypeId = ( cardTypeDetail != null ) ? cardTypeDetail.getId() : null;
        this.type = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
        this.department = ( cardTypeDetail != null ) ? cardTypeDetail.getDepartment() : null;
        this.departmentDescription = departmentDescription;
        this.name = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : null;
        this.availableNumberOfCardsToday = availableNumberOfCardsToday;
        this.deriveTotalNumberOfCards( cardTypeDetail );
        this.deriveDisplayName();
        this.status = ( cardTypeDetail != null ) ? cardTypeDetail.getStatus() : null;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId( String cardTypeId ) {
        this.cardTypeId = cardTypeId;
    }

    public CardType getType() {
        return type;
    }

    public void setType( CardType type ) {
        this.type = type;
        this.deriveDisplayName();
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName( String displayName ) {
        this.displayName = displayName;
        this.deriveDisplayName();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment( String department ) {
        this.department = department;
        this.deriveDisplayName();
    }

    public int getAvailableNumberOfCardsToday() {
        return availableNumberOfCardsToday;
    }

    public void setAvailableNumberOfCardsToday( int availableNumberOfCardsToday ) {
        this.availableNumberOfCardsToday = availableNumberOfCardsToday;
    }

    public String getDepartmentDescription() {
        return departmentDescription;
    }

    public void setDepartmentDescription( String departmentDescription ) {
        this.departmentDescription = departmentDescription;
        this.deriveDisplayName();
    }

    public int getTotalNumberOfCards() {
        return totalNumberOfCards;
    }

    public void setTotalNumberOfCards( int totalNumberOfCardsToday ) {
        this.totalNumberOfCards = totalNumberOfCardsToday;
    }

    public int getActiveNumberOfCardsToday() {
        return activeNumberOfCardsToday;
    }

    public void setActiveNumberOfCardsToday( int activeNumberOfCardsToday ) {
        this.activeNumberOfCardsToday = activeNumberOfCardsToday;
    }

    public int getAvailableNumberOfCardsInDepartmentToday() {
        return availableNumberOfCardsInDepartmentToday;
    }

    public void setAvailableNumberOfCardsInDepartmentToday( int availableNumberOfCardsInDepartmentToday ) {
        this.availableNumberOfCardsInDepartmentToday = availableNumberOfCardsInDepartmentToday;
    }

    public final void deriveDisplayName() {
        this.displayName = this.name;
        if ( this.type == CardType.GROUP && this.displayName != null  && !this.displayName.isEmpty() ) {
            if ( this.departmentDescription != null && !this.departmentDescription.isEmpty() ) {
                this.displayName = this.displayName + " (" + this.departmentDescription + ")";
            }
            else if ( this.department != null && !this.department.isEmpty() ) {
                this.displayName = this.displayName + " (" + this.department + ")";
            }
            else if ( this.department != null && this.department.length() > 0) {
                this.displayName = this.displayName + " (" + this.department + ")";
            }
        }
    }

    public final void deriveTotalNumberOfCards( CardTypeDetail cardTypeDetail ) {
        int NumberOfCards = 0;
        if ( cardTypeDetail != null ) {
            List< CardDetail > cardDetails = cardTypeDetail.getCardDetails();
            if ( cardDetails != null ) {
                NumberOfCards = cardDetails.size();
            }
        }

        this.totalNumberOfCards = NumberOfCards;
    }

	public CardTypeDetailStatus getStatus() {
		return status;
	}

	public void setStatus(CardTypeDetailStatus status) {
		this.status = status;
	}
}
