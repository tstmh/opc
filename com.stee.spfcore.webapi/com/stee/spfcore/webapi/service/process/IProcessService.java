package com.stee.spfcore.webapi.service.process;

import com.stee.spfcore.webapi.model.benefits.BereavementGrant;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.sag.SAGApplication;

public interface IProcessService {

    /**
     * Start the process of insurance nomination submission
     * 
     * @param .
     * @throws ProcessServiceException.
     */
    public void submitInsuranceNomination( Membership membership ) throws ProcessServiceException;

    /**
     * Start the process of membership withdraw request
     * 
     * @param .
     * @throws ProcessServiceException.
     */
    public void withdrawMembershipRequest( Membership membership ) throws ProcessServiceException;

    /**
     * Start the process of bereavement grant application
     * 
     * @param .
     * @throws ProcessServiceException.
     */
    public void applyBereavementGrant( BereavementGrant bereavementGrant ) throws ProcessServiceException;

    /**
     * Start the process of new born gift application
     * 
     * @param .
     * @throws ProcessServiceException.
     */
    public void applyNewBornGift( NewBornGift newBornGift ) throws ProcessServiceException;

    /**
     * Start the process of wedding gift application
     * 
     * @param .
     * @throws ProcessServiceException.
     */
    public void applyWeddingGift( WeddingGift weddingGift ) throws ProcessServiceException;

    /**
     * Start the process to process membership based on personnel detail
     * This is used by HR Interface.
     * 
     * @param nric
     * @throws ProcessServiceException
     */
    public void processMembership( String nric ) throws ProcessServiceException;

    /**
     * Start the process for SAG Application.
     * 
     * @param sagApplication
     * @throws ProcessServiceException
     */
    public void applySAGApplication( SAGApplication sagApplication ) throws ProcessServiceException;

}
