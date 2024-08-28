package com.stee.spfcore.service.personnel;

import java.util.Date;
import java.util.List;

import com.stee.spfcore.model.personnel.ChangeRecord;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.HRChangeRecord;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.internal.HRProcessingInfo;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.vo.personnel.PersonalDepartmentWorkEmail;
import com.stee.spfcore.vo.personnel.PersonalOnContinuousLeaveStatus;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;
import com.stee.spfcore.vo.personnel.PersonnelCriteria;
import com.stee.spfcore.vo.personnel.SearchPersonnelResults;
import com.stee.spfcore.vo.personnel.PersonalNricName;

public interface IPersonnelService {

    /**
     * Get the personal detail by nric.
     * 
     * @param nric
     * @return PersonalDetail or null if detail not found.
     * @throws PersonnelServiceException
     *             Exception while retrieving personalDetail.
     */
    public PersonalDetail getPersonal( String nric ) throws PersonnelServiceException, AccessDeniedException;

    /**
     * Get the employment information of a personal.
     * 
     * @param nric
     * @return Employment or null if detail not found.
     * @throws PersonnelServiceException
     *             Exception while retrieving employment information.
     */
    public Employment getEmployment( String nric ) throws PersonnelServiceException, AccessDeniedException;

    /**
     * Add new personal.
     * 
     * @param personal
     *            the personal detail to be added.
     * @throws PersonnelServiceException
     *             Exception while adding the personal.
     */
    public void addPersonal( PersonalDetail personal, String requester ) throws PersonnelServiceException, AccessDeniedException;

    /**
     * Updating personal detail
     * 
     * @param personal
     *            the personal detail to be updated.
     * @throws PersonnelServiceException
     *             Exception while updating the personal detail.
     */
    public void updatePersonal( PersonalDetail personal, String requester ) throws PersonnelServiceException, AccessDeniedException;

    /**
     * Return personnel that match the specified arguments.
     * 
     * @param name
     * @param nric
     * @param orgOrDeptCode
     * @return
     * @throws PersonnelServiceException
     */
    public List< PersonalDetail > searchPersonnel( String name, String nric, String orgOrDeptCode ) throws PersonnelServiceException;

    public SearchPersonnelResults searchPersonnel( String name, String nric, String department, String uwoDepartment, String uwoSubunit, int maxResults ) throws PersonnelServiceException;

    /**
     * Save the HRChangeRecord to DB.
     * The record keep track of the personnel that is change by HR interface
     * 
     * @param record
     * @throws PersonnelServiceException
     */
    public void saveHRChangeRecord( HRChangeRecord record ) throws PersonnelServiceException;

    /**
     * Return a list of nric of personnel that has been updated by HR interface on the specified date.
     * 
     * @param date
     * @return
     * @throws PersonnelServiceException
     */
    public List< String > getHRUpdatedPersonnel( Date date ) throws PersonnelServiceException;

    /**
     * 
     * @Title: searchPersonalByCriteria
     * @Description: search Personal Leaves By Criteria
     * @param @param personnelCriteria
     * @param @return
     * @return List<Leave>
     * @throws
     */
    public List< Leave > searchPersonalLeavesByCriteria( PersonnelCriteria personnelCriteria ) throws PersonnelServiceException, AccessDeniedException;

    /**
     * Return the change record that indicate when user last change the personnel detail.
     * 
     * @param nric
     * @return
     * @throws PersonnelServiceException
     */
    public ChangeRecord getChangeRecord( String nric ) throws PersonnelServiceException;

    /**
     * This is used internally by HR interface to get HRProcessingInfo that keep track of
     * where the personnel data come from.
     * 
     * @param nric
     * @return
     * @throws PersonnelServiceException
     */
    public HRProcessingInfo getHRProcessingInfo( String nric ) throws PersonnelServiceException;

    /**
     * This is used internally by HR interface to save HRProcessingInfo to keep track of
     * where the personnel data come from.
     * 
     * @param info
     * @throws PersonnelServiceException
     */
    public void saveHRProcessingInfo( HRProcessingInfo info ) throws PersonnelServiceException;

    public List< Object[] > getActivePersonnelForHivScreeningDueOfficerList( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException;

    public List< Integer > getActivePersonnelCountsGroupByDepartment( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException;

    public List< PersonalPreferredContacts > getActivePcwfMemberPreferredContacts() throws PersonnelServiceException;

    public List< PersonalPreferredContacts > getActivePersonnelPreferredContacts( List< String > departments, List< String > serviceTypes ) throws PersonnelServiceException;

    public List< PersonalPreferredContacts > getPersonnelPreferredContacts( List< String > nrics ) throws PersonnelServiceException;

    public List< PersonalDepartmentWorkEmail > getPersonnelOfficeEmails( List< String > nrics ) throws PersonnelServiceException;

    public String getPersonalName( String nric ) throws PersonnelServiceException;
    
    public List< PersonalNricName > getPersonalName( List< String > nrics ) throws PersonnelServiceException;

    public List< PersonalOnContinuousLeaveStatus > getOnContinuousLeaveStatuses( List< String > nrics, Date startDate, int numberOfDays ) throws PersonnelServiceException;

    public Boolean isOnFullMonthNoPayLeave( String nric, Date date ) throws PersonnelServiceException;

    public void saveExtraEmploymentInfo( ExtraEmploymentInfo info ) throws PersonnelServiceException;

    public ExtraEmploymentInfo getExtraEmploymentInfo( String nric ) throws PersonnelServiceException, AccessDeniedException;
}
