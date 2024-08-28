package com.stee.spfcore.service.corporateCard.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.SecureRandom;

import com.stee.spfcore.dao.BlacklistDAO;
import com.stee.spfcore.dao.CalendarDAO;
import com.stee.spfcore.dao.CodeDAO;
import com.stee.spfcore.dao.CorporateCardDAO;
import com.stee.spfcore.dao.GlobalParametersDAO;
import com.stee.spfcore.dao.PersonnelDAO;
import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.TemplateDAO;
import com.stee.spfcore.model.DateStartEnd;
import com.stee.spfcore.model.TimeStartEnd;
import com.stee.spfcore.model.blacklist.BlacklistModuleNameConstants;
import com.stee.spfcore.model.calendar.PublicHoliday;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.corporateCard.*;
import com.stee.spfcore.model.globalParameters.GlobalParameters;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.personnel.*;
import com.stee.spfcore.model.template.Template;
import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.service.calendar.CalendarServiceException;
import com.stee.spfcore.service.calendar.CalendarServiceFactory;
import com.stee.spfcore.service.calendar.ICalendarService;
import com.stee.spfcore.service.configuration.IMailSenderConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.corporateCard.CorporateCardException;
import com.stee.spfcore.service.corporateCard.ICorporateCardService;
import com.stee.spfcore.service.notification.INotificationService;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.service.notification.NotificationServiceFactory;
import com.stee.spfcore.utils.*;
import com.stee.spfcore.vo.corporateCard.*;
import com.stee.spfcore.vo.personnel.PersonalPreferredContacts;

public class CorporateCardService implements ICorporateCardService {
    private static final Logger LOGGER = Logger.getLogger( CorporateCardService.class.getName() );

    private static final String PCWF_CARD_MODULE_NAME = "CCBP";
    private static final String GROUP_CARD_MODULE_NAME = "CCBG";
    private static final String CANCEL_CARD_FOR_NEXT_HOLDER_TEMPLATE_NAME = "CCB-E010";
    private static final String CANCEL_CARD_FOR_PREV_HOLDER_TEMPLATE_NAME = "CCB-E011";
    private static final String SUCCESS_CARD_BOOKING_TEMPLATE_NAME = "CCB-E012";
    private static final String SUCCESS_PREV_HOLDER_TEMPLATE_NAME = "CCB-E013";
    private static final String SUCCESS_NEXT_HOLDER_TEMPLATE_NAME = "CCB-E014";
    private static final String CANCEL_CARD_FOR_NEXT_PREV_HOLDER_TEMPLATE_NAME = "CCB-E015";
    private static final String PCWF_CARD = "PCWF";
    private static final String POLICE_WELFARE_DIVISION = "Police Welfare Division";

    private GlobalParametersDAO globalParametersDao;
    private BlacklistDAO blacklistDao;
    private CodeDAO codeDao;
    private PersonnelDAO personnelDao;
    private TemplateDAO templateDao;
    private CorporateCardDAO dao;
    private CalendarDAO calendarDao;
    private EmailUtil emailUtil;
    private SmsUtil smsUtil;

    public CorporateCardService() {
        globalParametersDao = new GlobalParametersDAO();
        blacklistDao = new BlacklistDAO();
        codeDao = new CodeDAO();
        personnelDao = new PersonnelDAO();
        templateDao = new TemplateDAO();
        dao = new CorporateCardDAO();
        calendarDao = new CalendarDAO();
        emailUtil = new EmailUtil();
        smsUtil = new SmsUtil();
    }

    public PersonalDetail getPersonal( String nric ) throws CorporateCardException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMCorporateCardService.getPersonal(), nric=%s", nric));
        }
        PersonalDetail result = null;
        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getPersonal( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String msg = String.format( "failed BPMCorporateCardService.getPersonal(), nric=%s", nric );
            LOGGER.warning( msg );
            LOGGER.warning( Util.toExceptionString( e ) );
            SessionFactoryUtil.rollbackTransaction();
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMCorporateCardService.getPersonal(), nric=%s", nric));
        }
        return result;
    }

    public Membership getMembership( String nric ) throws CorporateCardException {
        if (nric !=null && LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("start BPMCorporateCardService.getMembership(), nric=%s", nric));
        }
        Membership result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getMembership( nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            String msg = String.format( "failed BPMCorporateCardService.getMembership(), nric=%s", nric );
            LOGGER.warning( msg );
            LOGGER.warning( Util.toExceptionString( e ) );
            SessionFactoryUtil.rollbackTransaction();
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("end BPMCorporateCardService.getMembership(), nric=%s", nric));
        }
        return result;
    }

    @Override
    public CardTypeDetail getCardTypeDetail( String cardTypeId ) throws CorporateCardException {
        CardTypeDetail result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardTypeDetail( cardTypeId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardTypeDetail() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    @Override
    public CardTypeDetail getCardTypeDetail( CardType cardType, String department, String cardTypeName ) throws CorporateCardException {
        CardTypeDetail result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardTypeDetail( cardType, department, cardTypeName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardTypeDetail() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< CardTypeDetail > getAllCardTypeDetails() throws CorporateCardException {
        List< CardTypeDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            results = dao.getAllPcwfGroupCardTypeDetails( true, null, null );
            if ( results != null ) {
                numCardTypes = results.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeDetails(). Number of card types: %s", numCardTypes));
            }
            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public List< CardTypeDetail > getAllCardTypeDetailsForWpo() throws CorporateCardException {
        List< CardTypeDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            // only pcwf cards
            results = dao.getAllPcwfCardTypeDetails();
            if ( results != null ) {
                numCardTypes = results.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeDetailsForWpo(). Number of card types: %s", numCardTypes));
            }
            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeDetailsForWpo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public List< CardTypeDetail > getAllCardTypeDetailsForUwo( String department ) throws CorporateCardException {
        List< CardTypeDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            // only group cards that belong to dept and co-share with dept
            results = dao.getAllPcwfGroupCardTypeDetails( false, department, department );
            if ( results != null ) {
                numCardTypes = results.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeDetailsForUwo(). Number of card types: %s", numCardTypes));
            }
            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeDetailsForUwo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public List< CardTypeLessDetail > getAllCardTypeLessDetails( boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfGroupCardTypeDetails( true, null, null );
            if ( cardTypeDetails != null ) {
                numCardTypes = cardTypeDetails.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeLessDetails(). Number of card types: %s", numCardTypes));
            }
            if ( cardTypeDetails != null ) {
                int availablePcwfCards = 0;
                Map< String, Integer > availableDeptCardMap = new HashMap<>();
                cardTypeLessDetails = new ArrayList<>();
                for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                    String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                    CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                    cardTypeLessDetails.add( cardTypeLessDetail );
                    if ( includeAvailabilityToday ) {
                        this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                        CardType cardType = cardTypeDetail.getType();
                        if ( cardType == CardType.PCWF ) {
                            availablePcwfCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                        }
                        else if ( cardType == CardType.GROUP ) {
                            String dept = cardTypeDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            availableDeptCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                            availableDeptCardMap.put( dept, availableDeptCards );
                        }
                    }
                }
                if ( includeAvailabilityToday ) {
                    for ( CardTypeLessDetail cardTypeLessDetail : cardTypeLessDetails ) {
                        CardType cardType = cardTypeLessDetail.getType();
                        if ( cardType == CardType.PCWF ) {
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availablePcwfCards );
                        }
                        else if ( cardType == CardType.GROUP ) {
                            String dept = cardTypeLessDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availableDeptCards );
                        }
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeLessDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }
    
    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetailsForWpo( boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfCardTypeDetails();
            if ( cardTypeDetails != null ) {
                numCardTypes = cardTypeDetails.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCreatedCardTypeLessDetailsForWpo(). Number of card types: %s", numCardTypes));
            }
            if ( cardTypeDetails != null ) {
                int availablePcwfCards = 0;
                cardTypeLessDetails = new ArrayList<>();
                for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                	
                	if (cardTypeDetail.getStatus() != CardTypeDetailStatus.DRAFT){
                		String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                            availablePcwfCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                        }
                	}
                }
                if ( includeAvailabilityToday ) {
                    for ( CardTypeLessDetail cardTypeLessDetail : cardTypeLessDetails ) {
                        cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availablePcwfCards );
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCreatedCardTypeLessDetailsForWpo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }

    public List< CardTypeLessDetail > getAllCardTypeLessDetailsForWpo( boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfCardTypeDetails();
            if ( cardTypeDetails != null ) {
                numCardTypes = cardTypeDetails.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeLessDetailsForWpo(). Number of card types: %s", numCardTypes));
            }
            if ( cardTypeDetails != null ) {
                int availablePcwfCards = 0;
                cardTypeLessDetails = new ArrayList<>();
                for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                    String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                    CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                    cardTypeLessDetails.add( cardTypeLessDetail );
                    if ( includeAvailabilityToday ) {
                        this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                        availablePcwfCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                    }
                }
                if ( includeAvailabilityToday ) {
                    for ( CardTypeLessDetail cardTypeLessDetail : cardTypeLessDetails ) {
                        cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availablePcwfCards );
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeLessDetailsForWpo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }
    
    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetailsForUwo( String department, boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfGroupCardTypeDetails( false, department, department );
            if ( cardTypeDetails != null ) {
                numCardTypes = cardTypeDetails.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCreatedCardTypeLessDetailsForUwo(). Number of card types: %s", numCardTypes));
            }
            if ( cardTypeDetails != null ) {
                cardTypeLessDetails = new ArrayList<>();
                for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                	
                	if(cardTypeDetail.getStatus() != CardTypeDetailStatus.DRAFT){
                		String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                        }
                	}
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCreatedCardTypeLessDetailsForUwo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }

    public List< CardTypeLessDetail > getAllCardTypeLessDetailsForUwo( String department, boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            int numCardTypes = 0;
            List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfGroupCardTypeDetails( false, department, department );
            if ( cardTypeDetails != null ) {
                numCardTypes = cardTypeDetails.size();
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("getAllCardTypeLessDetailsForUwo(). Number of card types: %s", numCardTypes));
            }
            if ( cardTypeDetails != null ) {
                cardTypeLessDetails = new ArrayList<>();
                for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                    String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                    CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                    cardTypeLessDetails.add( cardTypeLessDetail );
                    if ( includeAvailabilityToday ) {
                        this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeLessDetailsForUwo() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }

    public List< CardTypeLessDetail > getAllCardTypeLessDetails( BroadcastFrequency frequency, boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            boolean includePcwf = false;
            List< String > groupCardDepartments = new ArrayList<>();

            List< CardAutoBroadcast > cardAutoBroadcasts = dao.getCardAutoBroadcasts( frequency );
            if ( cardAutoBroadcasts != null ) {
                for ( CardAutoBroadcast cardAutoBroadcast : cardAutoBroadcasts ) {
                    CardType type = cardAutoBroadcast.getType();
                    if ( type == CardType.PCWF ) {
                        includePcwf = true;
                        break;
                    }
                    else if ( type == CardType.GROUP ) {
                        String department = cardAutoBroadcast.getDepartment();
                        if ( department != null ) {
                            groupCardDepartments.add( department );
                        }
                    }
                }
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("includePcwf=%s, groupCardDepartments count=%s", includePcwf, groupCardDepartments.size()));
            }
            cardTypeLessDetails = new ArrayList<>();
            if ( includePcwf ) {
                int availablePcwfCards = 0;
                List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfCardTypeDetails();
                if ( cardTypeDetails != null ) {
                    List< CardTypeLessDetail > tmpList = new ArrayList<>();
                    for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                        String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        tmpList.add( cardTypeLessDetail );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                            availablePcwfCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                        }
                    }
                    if ( includeAvailabilityToday ) {
                        for ( CardTypeLessDetail cardTypeLessDetail : tmpList ) {
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availablePcwfCards );
                        }
                    }
                }
            }
            if ( !groupCardDepartments.isEmpty() ) {
                Map< String, Integer > availableDeptCardMap = new HashMap<>();
                List< CardTypeDetail > cardTypeDetails = dao.getAllGroupCardTypeDetails( groupCardDepartments );
                if ( cardTypeDetails != null ) {
                    List< CardTypeLessDetail > tmpList = new ArrayList<>();
                    for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                        String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        tmpList.add( cardTypeLessDetail );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                            String dept = cardTypeDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            availableDeptCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                            availableDeptCardMap.put( dept, availableDeptCards );
                        }
                    }
                    if ( includeAvailabilityToday ) {
                        for ( CardTypeLessDetail cardTypeLessDetail : tmpList ) {
                            String dept = cardTypeLessDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availableDeptCards );
                        }
                    }
                }
            }

            LOGGER.info( String.format( "cardTypeLessDetails count=%s", cardTypeLessDetails.size() ) );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeLessDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }
    
    public List< CardTypeLessDetail > getAllCreatedCardTypeLessDetails( BroadcastFrequency frequency, boolean includeAvailabilityToday ) throws CorporateCardException {
        List< CardTypeLessDetail > cardTypeLessDetails = null;

        try {
            SessionFactoryUtil.beginTransaction();

            boolean includePcwf = false;
            List< String > groupCardDepartments = new ArrayList<>();

            List< CardAutoBroadcast > cardAutoBroadcasts = dao.getCardAutoBroadcasts( frequency );
            if ( cardAutoBroadcasts != null ) {
                for ( CardAutoBroadcast cardAutoBroadcast : cardAutoBroadcasts ) {
                    CardType type = cardAutoBroadcast.getType();
                    if ( type == CardType.PCWF ) {
                        includePcwf = true;
                        break;
                    }
                    else if ( type == CardType.GROUP ) {
                        String department = cardAutoBroadcast.getDepartment();
                        if ( department != null ) {
                            groupCardDepartments.add( department );
                        }
                    }
                }
            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("includePcwf=%s, groupCardDepartments count=%s", includePcwf, groupCardDepartments.size()));
            }
            cardTypeLessDetails = new ArrayList<>();
            if ( includePcwf ) {
                int availablePcwfCards = 0;
                List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfCreatedCardTypeDetails();
                if ( cardTypeDetails != null ) {
                    List< CardTypeLessDetail > tmpList = new ArrayList<>();
                    for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                        String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        tmpList.add( cardTypeLessDetail );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                            availablePcwfCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                        }
                    }
                    if ( includeAvailabilityToday ) {
                        for ( CardTypeLessDetail cardTypeLessDetail : tmpList ) {
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availablePcwfCards );
                        }
                    }
                }
            }
            if ( !groupCardDepartments.isEmpty() ) {
                Map< String, Integer > availableDeptCardMap = new HashMap<>();
                List< CardTypeDetail > cardTypeDetails = dao.getAllGroupCreatedCardTypeDetails( groupCardDepartments );
                if ( cardTypeDetails != null ) {
                    List< CardTypeLessDetail > tmpList = new ArrayList<>();
                    for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                        String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
                        CardTypeLessDetail cardTypeLessDetail = new CardTypeLessDetail( cardTypeDetail, departmentDescription );
                        tmpList.add( cardTypeLessDetail );
                        cardTypeLessDetails.add( cardTypeLessDetail );
                        if ( includeAvailabilityToday ) {
                            this.populateAvailabilityToday( cardTypeDetail, cardTypeLessDetail );
                            String dept = cardTypeDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            availableDeptCards += cardTypeLessDetail.getAvailableNumberOfCardsToday();
                            availableDeptCardMap.put( dept, availableDeptCards );
                        }
                    }
                    if ( includeAvailabilityToday ) {
                        for ( CardTypeLessDetail cardTypeLessDetail : tmpList ) {
                            String dept = cardTypeLessDetail.getDepartment();
                            Integer availableDeptCards = availableDeptCardMap.get( dept );
                            if ( availableDeptCards == null ) {
                                availableDeptCards = 0;
                            }
                            cardTypeLessDetail.setAvailableNumberOfCardsInDepartmentToday( availableDeptCards );
                        }
                    }
                }
            }

            LOGGER.info( String.format( "cardTypeLessDetails count=%s", cardTypeLessDetails.size() ) );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCardTypeLessDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardTypeLessDetails;
    }

    public List< CardTypeDetail > getAllEligibleCardTypeDetails( boolean isPcwfMember, String department ) throws CorporateCardException {
        List< CardTypeDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getAllPcwfGroupCardTypeDetails( isPcwfMember, department, department );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllEligibleCardTypeDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    @Override
    public String addCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) throws CorporateCardException {
        String errMsg = null;
        try {
            SessionFactoryUtil.beginTransaction();

            cardTypeDetail.setId( null );
            CardType cardType = cardTypeDetail.getType();
            String department = cardTypeDetail.getDepartment();
            String cardTypeName = cardTypeDetail.getName();

            CardTypeDetail existingCardTypeDetail = dao.getCardTypeDetail( cardType, department, cardTypeName );
            if ( existingCardTypeDetail != null ) {
                errMsg = "Card of same type, department and name already exist.";
                return errMsg;
            }

            dao.addCardTypeDetail( cardTypeDetail, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "addCardTypeDetail() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return errMsg;
    }

    @Override
    public String updateCardTypeDetail( CardTypeDetail cardTypeDetail, String requester ) throws CorporateCardException {
        String errMsg = null;
        try {
            SessionFactoryUtil.beginTransaction();

            CardType cardType = cardTypeDetail.getType();
            String department = cardTypeDetail.getDepartment();
            String cardTypeName = cardTypeDetail.getName();

            CardTypeDetail existingCardTypeDetail = dao.getCardTypeDetail( cardType, department, cardTypeName );
            if (( existingCardTypeDetail != null ) && ( !Objects.equals( existingCardTypeDetail.getId(), cardTypeDetail.getId() ) )) {
                errMsg = "Card of same type, department and name already exist.";
                return errMsg;
            }

            dao.updateCardTypeDetail( cardTypeDetail, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "updateCardTypeDetail() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
        return errMsg;
    }

    public CardBooking getCardBooking( String cardBookingId ) throws CorporateCardException {
        CardBooking results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getCardBooking( cardBookingId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardBooking() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public List< CardBooking > getCardBookings( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) throws CorporateCardException {
        List< CardBooking > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getCardBookings( startDate, endDate, cardTypeIds, cardBookingStatuses, nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardBookings() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public int getCardBookingsCount( Date startDate, Date endDate, List< String > cardTypeIds, List< CardBookingStatus > cardBookingStatuses, String nric ) throws CorporateCardException {
        int result = 0;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardBookingsCount( startDate, endDate, cardTypeIds, cardBookingStatuses, nric );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardBookingsCount() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< CardBooking > searchCardBookings( CardBookingSearchCriteria searchCriteria ) throws CorporateCardException {
        List< CardBooking > results = new ArrayList<>();

        try {
            SessionFactoryUtil.beginTransaction();

            List< CardTypeDetail > cardTypeDetailList = dao.getCardTypeDetails( null );
            Map< String, CardTypeDetail > cardTypeDetailMap = new HashMap<>();
            for ( CardTypeDetail cardTypeDetail : cardTypeDetailList ) {
                cardTypeDetailMap.put( cardTypeDetail.getId(), cardTypeDetail );
            }

            Date bookingStartDateStart = searchCriteria.getBookingStartDateStart();
            Date bookingStartDateEnd = searchCriteria.getBookingStartDateEnd();
            String applicantName = searchCriteria.getApplicantName();
            String applicantIdNo = searchCriteria.getApplicantIdNo();
            List< String > cardTypeIds = searchCriteria.getCardTypeIds();
            List< CardBookingStatus > approvedStatus = new ArrayList<>();
            approvedStatus.add( CardBookingStatus.APPROVED );
            int maxResults = searchCriteria.getMaxResults();
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("bookingStartDateStart=%s, bookingStartDateEnd=%s, applicantName=%s, applicantIdNo=%s, maxResults=%s", bookingStartDateStart, bookingStartDateEnd, applicantName, applicantIdNo, maxResults));
            }
            List< CardBooking > cardBookings = dao.getCardBookings( bookingStartDateStart, bookingStartDateEnd, applicantIdNo, cardTypeIds, approvedStatus, maxResults );
            for ( CardBooking cardBooking : cardBookings ) {
                String cardTypeId = cardBooking.getCardTypeId();
                CardTypeDetail cardTypeDetail = ( cardTypeDetailMap == null ) ? null : cardTypeDetailMap.get( cardTypeId );
                this.populateCardTypeName( cardBooking, cardTypeDetail );
                this.populatePreferredContacts( cardBooking, true, false, false );
                if ( ( applicantName != null ) && ( applicantName.length() > 0 ) ) {
                    // filter
                    PersonalPreferredContacts personalPreferredContacts = cardBooking.getPersonalPreferredContacts();
                    if ( personalPreferredContacts != null ) {
                        String name = personalPreferredContacts.getName();
                        if (( name != null ) && ( name.toUpperCase().indexOf( applicantName.toUpperCase() ) >= 0 )){
                            results.add( cardBooking );
                        }
                    }
                }
                else {
                    results.add( cardBooking );
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "searchCardBookings() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
    }

    public CardBooking getCardBookingMoreDetails( String cardBookingId ) throws CorporateCardException {
        CardBooking cardBooking = null;
        try {
            SessionFactoryUtil.beginTransaction();

            cardBooking = dao.getCardBooking( cardBookingId );
            if ( cardBooking != null ) {
                String cardTypeId = cardBooking.getCardTypeId();
                CardTypeDetail cardTypeDetail = ( cardTypeId == null ) ? null : dao.getCardTypeDetail( cardTypeId );

                cardBooking.setCardTypeDetail( cardTypeDetail );
                this.populateCardTypeName( cardBooking, cardTypeDetail );
                this.populatePreferredContacts( cardBooking, true, false, false );
                if ( cardTypeDetail != null ) {
                    this.populateOnBehalfUposPreferredContacts( cardBooking );
                    this.populateCollectionDetails( cardBooking );
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardBookingMoreDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return cardBooking;

    }

    public void addCardBooking( CardBooking cardBooking, String requester ) throws CorporateCardException {
        try {
            SessionFactoryUtil.beginTransaction();

            cardBooking.setId( null );
            dao.addCardBooking( cardBooking, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "addCardBooking() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    public void updateCardBooking( CardBooking cardBooking, String requester ) throws CorporateCardException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.updateCardBooking( cardBooking, requester );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "updateCardBooking() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    public void updateCardBookingRemarks( String cardBookingId, String remarks, String requester ) throws CorporateCardException {
        try {
            SessionFactoryUtil.beginTransaction();

            CardBooking cardBooking = dao.getCardBooking( cardBookingId );
            if ( cardBooking != null ) {
                cardBooking.setRemarks( remarks );
                dao.updateCardBooking( cardBooking, requester );

            }
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.log( Level.WARNING, String.format( "card booking not found. id=%s", cardBookingId ) );
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "updateCardBookingRemarks() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    public CardBooking getApprovedCardBooking( Date startDate, Date endDate, String allocatedSerialNumber ) throws CorporateCardException {
        CardBooking result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getApprovedCardBooking( startDate, endDate, allocatedSerialNumber );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getApprovedCardBooking() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public CardAutoBroadcast getCardAutoBroadcast( String cardAutoBroadcastId ) throws CorporateCardException {
        CardAutoBroadcast result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardAutoBroadcast( cardAutoBroadcastId );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardAutoBroadcast() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public CardAutoBroadcast getCardAutoBroadcast( CardType cardType, String department ) throws CorporateCardException {
        CardAutoBroadcast result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardAutoBroadcast( cardType, department );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardAutoBroadcast() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public List< CardAutoBroadcast > getCardAutoBroadcasts( BroadcastFrequency frequency ) throws CorporateCardException {
        List< CardAutoBroadcast > result = null;

        try {
            SessionFactoryUtil.beginTransaction();

            result = dao.getCardAutoBroadcasts( frequency );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getCardAutoBroadcasts() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }

    public void addOrUpdateCardAutoBroadcast( CardAutoBroadcast cardAutoBroadcast ) throws CorporateCardException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.addOrUpdateCardAutoBroadcast( cardAutoBroadcast );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "addOrUpdateCardAutoBroadcast() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }
    }

    @Override
    public List< CardTypeBookingsResult > processCardBookings( Date processingDate ) throws CorporateCardException {
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("processCardBookings() started. processingDate=%s", processingDate));
        }
        List< CardTypeBookingsResult > cardTypeBookingsResults = new ArrayList<>();

        // process card type with allocation rule fcfs with approval last.
        // restrictions such as same bookings per day across all cards will be approved first for non fcfs with approval cards,
        // then filter out for fcfs with approval cards
        AllocationRule[] allocationRuleProcessingOrder = new AllocationRule[] { AllocationRule.FIRST_COME_FIRST_SERVED, AllocationRule.LEAST_USED, AllocationRule.BALLOTING, AllocationRule.FIRST_COME_FIRST_SERVED_APPROVAL };

        try {
            SessionFactoryUtil.beginTransaction();
            List<GlobalParameters> globalParameters = globalParametersDao.getGlobalParametersList();
            LOGGER.info("Global Parameter list: "+ globalParameters);
            //get the list of globalParameters
            Map< String, CardTypeDetail > allCardTypeDetails = this.fetchAllCardTypeDetails( globalParameters, processingDate );
            Set< Entry< String, CardTypeDetail >> entrySet = allCardTypeDetails.entrySet();
            for ( AllocationRule curAllocationRule : allocationRuleProcessingOrder ) {
                for ( Entry< String, CardTypeDetail > entry : entrySet ) {
                    CardTypeDetail cardTypeDetail = entry.getValue();
                    AllocationRule cardAllocationRule = cardTypeDetail.getAllocationRule();
                    if ( cardAllocationRule == curAllocationRule ) {
                        cardTypeBookingsResults.add( this.processCardBookings( cardTypeDetail ) );
                    }
                }
            }

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "processCardBookings() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        LOGGER.info( String.format( "processCardBookings() ended. number of card types: %s", cardTypeBookingsResults.size() ) );
        return cardTypeBookingsResults;
    }

    public UpdateCardBookingStatusesResult updateCardBookingStatuses( List< CardBookingStatusUpdate > cardbookingsStatusUpdateList ) throws CorporateCardException {
        LOGGER.info( String.format( "updateCardBookingStatusUpdates() started. count=%s", cardbookingsStatusUpdateList.size() ) );
        UpdateCardBookingStatusesResult result = new UpdateCardBookingStatusesResult();

        List< String > errMsgs = new ArrayList<>();
        Map< String, CardBooking > cardBookingsMap = new HashMap<>();
        Map< String, CardTypeDetail > cardTypeDetailsMap = new HashMap<>();
        List< CardBookingStatusUpdate > tempUpdateList;
        List< CardBooking > approvedCardBookings = new ArrayList<>();
        List< CardBooking > rejectedCardBookings = new ArrayList<>();

        try {
            SessionFactoryUtil.beginTransaction();

            tempUpdateList = new ArrayList<>();
            this.fetchCardBookingsCardTypeDetails( cardbookingsStatusUpdateList, cardBookingsMap, cardTypeDetailsMap, tempUpdateList, errMsgs );
            cardbookingsStatusUpdateList = tempUpdateList;
            LOGGER.info( String.format( "fetchCardBookingsCardTypeDetails() ended. count=%s", cardbookingsStatusUpdateList.size() ) );

            // verify and remove for approval of same officer on the same day
            // to prevent approval of first booking first, then reject second booking 
            tempUpdateList = new ArrayList<>();
            this.verifyCardBookingsStatusUpdateList( cardbookingsStatusUpdateList, cardBookingsMap, tempUpdateList, errMsgs );
            cardbookingsStatusUpdateList = tempUpdateList;
            LOGGER.info( String.format( "verifyCardBookingsStatusUpdateList() ended. count=%s", cardbookingsStatusUpdateList.size() ) );

            this.updateCardBookingStatus( cardbookingsStatusUpdateList, cardBookingsMap, cardTypeDetailsMap, approvedCardBookings, rejectedCardBookings, errMsgs );

            if ( errMsgs.isEmpty() ) {
                // populate personal and collection details
                for ( CardBooking cardBooking : approvedCardBookings ) {
                    this.populatePreferredContacts( cardBooking, true, true, true );
                    this.populateOnBehalfUposPreferredContacts( cardBooking );
                }
                for ( CardBooking cardBooking : rejectedCardBookings ) {
                    this.populatePreferredContacts( cardBooking, true, false, false );
                    this.populateOnBehalfUposPreferredContacts( cardBooking );
                }

                SessionFactoryUtil.commitTransaction();
            }
            else {
                SessionFactoryUtil.rollbackTransaction();
                approvedCardBookings = null;
                rejectedCardBookings = null;
            }
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "updateCardBookingStatusUpdates() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        result.setErrMsgs( errMsgs );
        result.setApprovedCardBookings( approvedCardBookings );
        result.setRejectedCardBookings( rejectedCardBookings );
        LOGGER.info( String.format( "updateCardBookingStatusUpdates() ended. errMsgs=%s", Util.replaceNewLine( errMsgs ) ) );
        return result;
    }

    public void sendCancelCardBookingNotifications( CardBooking cardBooking ) {
        if ( cardBooking == null ) {
            return;
        }

        String cardBookingId = ( cardBooking != null ) ? cardBooking.getId() : null;
        String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
        String nric = ( cardBooking != null ) ? cardBooking.getNric() : null;

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications(). cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
        Template cancelCardForNextHolderTemplate = templateDao.getTemplateByName( CANCEL_CARD_FOR_NEXT_HOLDER_TEMPLATE_NAME );
        Template cancelCardForPrevHolderTemplate = templateDao.getTemplateByName( CANCEL_CARD_FOR_PREV_HOLDER_TEMPLATE_NAME );

        try {
            this.populatePreferredContacts( cardBooking, true, true, true );
            this.populateOnBehalfUposPreferredContacts( cardBooking );

            CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? dao.getCardTypeDetail( cardTypeId ) : null;
            CardType cardType = null;
            if ( cardTypeDetail != null) {
            	cardType = cardTypeDetail.getType();
            }
            PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
            List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : null;
            List< CardNextHolderPreferredContacts > nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : null;

            if ( previousHolderPreferredContacts != null ) {
                for ( CardPreviousHolderPreferredContacts previousHolderPreferredContact : previousHolderPreferredContacts ) {
                    String handOverTakeOverPersonnelName = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getName() : null;
                    String cancelledPersonnelName = null;
                    if (personalPreferredContact != null) {
                    	cancelledPersonnelName = personalPreferredContact.getName();
                    }
                    List< CardLessDetail > cardLessDetails = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getCardLessDetails() : null;
                    List< String > cardSerialNumbers = null;
                    if ( cardLessDetails != null ) {
                        cardSerialNumbers = new ArrayList<>();
                        for ( CardLessDetail cardLessDetail : cardLessDetails ) {
                            String cardSerialNumber = cardLessDetail.getSerialNumber();
                            if ( cardSerialNumber != null ) {
                                cardSerialNumbers.add( cardSerialNumber );
                            }
                        }
                    }

                    String emailSubject = ( cancelCardForPrevHolderTemplate != null ) ? cancelCardForPrevHolderTemplate.getEmailSubject() : null;
                    String emailBody = ( cancelCardForPrevHolderTemplate != null ) ? cancelCardForPrevHolderTemplate.getEmailBody() : null;
                    String smsText = ( cancelCardForPrevHolderTemplate != null ) ? cancelCardForPrevHolderTemplate.getSmsText() : null;

                    emailSubject = this.getCancelCardBookingTemplateResultString( emailSubject, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );
                    emailBody = this.getCancelCardBookingTemplateResultString( emailBody, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );
                    smsText = this.getCancelCardBookingTemplateResultString( smsText, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );

                    // send to hand over personnel
                    this.sendCancelCardBookingNotification( previousHolderPreferredContact, cardType, emailSubject, emailBody, smsText );

                    // send to applicant
                    this.sendCancelCardBookingNotification( personalPreferredContact, cardType, emailSubject, emailBody, smsText );
                }
            }

            if ( nextHolderPreferredContacts != null ) {
                for ( CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts ) {
                    String cancelledPersonnelName = null;
                    if (personalPreferredContact != null) {
                    	cancelledPersonnelName = personalPreferredContact.getName();
                    }
                    String handOverTakeOverPersonnelName = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getName() : null;
                    List< CardLessDetail > cardLessDetails = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getCardLessDetails() : null;
                    List< String > cardSerialNumbers = null;
                    if ( cardLessDetails != null ) {
                        cardSerialNumbers = new ArrayList<>();
                        for ( CardLessDetail cardLessDetail : cardLessDetails ) {
                            String cardSerialNumber = cardLessDetail.getSerialNumber();
                            if ( cardSerialNumber != null ) {
                                cardSerialNumbers.add( cardSerialNumber );
                            }
                        }
                    }

                    String emailSubject = ( cancelCardForNextHolderTemplate != null ) ? cancelCardForNextHolderTemplate.getEmailSubject() : null;
                    String emailBody = ( cancelCardForNextHolderTemplate != null ) ? cancelCardForNextHolderTemplate.getEmailBody() : null;
                    String smsText = ( cancelCardForNextHolderTemplate != null ) ? cancelCardForNextHolderTemplate.getSmsText() : null;

                    emailSubject = this.getCancelCardBookingTemplateResultString( emailSubject, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );
                    emailBody = this.getCancelCardBookingTemplateResultString( emailBody, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );
                    smsText = this.getCancelCardBookingTemplateResultString( smsText, cardTypeDetail, cardBooking, cancelledPersonnelName, handOverTakeOverPersonnelName, cardSerialNumbers );

                    // send to applicant
                    this.sendCancelCardBookingNotification( personalPreferredContact, cardType, emailSubject, emailBody, smsText );

                    // send to take over personnel
                    this.sendCancelCardBookingNotification( nextHolderPreferredContact, cardType, emailSubject, emailBody, smsText );
                }
            }
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "sendCancelCardBookingNotifications() failed.", e );
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications() ended. cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
    }

    private void fetchCardBookingsCardTypeDetails( List< CardBookingStatusUpdate > cardbookingsStatusUpdateList, Map< String, CardBooking > cardBookingsMap, Map< String, CardTypeDetail > cardTypeDetailsMap,
            List< CardBookingStatusUpdate > newCardbookingsStatusUpdateList, List< String > errMsgs ) {
        for ( CardBookingStatusUpdate cardbookingsStatusUpdate : cardbookingsStatusUpdateList ) {
            String cardBookingId = cardbookingsStatusUpdate.getCardBookingId();
            if ( cardBookingId == null ) {
                String errMsg = "Card Booking ID is undefined.";
                errMsgs.add( errMsg );
                continue;
            }

            CardBooking cardBooking = dao.getCardBooking( cardBookingId );
            if ( cardBooking == null ) {
                String errMsg = String.format( "Card booking details cannot be found. cardBookingId=%s", cardBookingId );
                errMsgs.add( errMsg );
                continue;
            }

            String cardTypeId = cardBooking.getCardTypeId();
            if ( cardTypeId == null ) {
                String errMsg = String.format( "Card Type ID is undefined. cardBookingId=%s", cardBookingId );
                errMsgs.add( errMsg );
                continue;
            }

            CardTypeDetail cardTypeDetail = dao.getCardTypeDetail( cardTypeId );
            if ( cardTypeDetail == null ) {
                String errMsg = String.format( "Card type details not found. cardBookingId=%s", cardBookingId );
                errMsgs.add( errMsg );
                continue;
            }

            CardBookingStatus newBookingStatus = cardbookingsStatusUpdate.getNewStatus();
            if ( newBookingStatus == null ) {
                String errMsg = String.format( "Booking status is undefined. cardBookingId=%s", cardBookingId );
                errMsgs.add( errMsg );
                continue;
            }

            cardBookingsMap.put( cardBookingId, cardBooking );
            cardTypeDetailsMap.put( cardTypeId, cardTypeDetail );
            newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate );
        }
    }

    private void verifyCardBookingsStatusUpdateList( List< CardBookingStatusUpdate > cardbookingsStatusUpdateList, Map< String, CardBooking > cardBookingsMap, List< CardBookingStatusUpdate > newCardbookingsStatusUpdateList,
            List< String > errMsgs ) {
        for ( CardBookingStatusUpdate cardbookingsStatusUpdate1 : cardbookingsStatusUpdateList ) {
            String cardBookingId1 = cardbookingsStatusUpdate1.getCardBookingId();
            CardBooking cardBooking1 = cardBookingsMap.get( cardBookingId1 );
            String nric1 = cardBooking1.getNric();
            if ( nric1 == null ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }
            DateStartEnd dateStartEnd1 = cardBooking1.getDateStartEnd();
            if ( dateStartEnd1 == null ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }
            Date bookingStart1 = DateTimeUtil.getDateDaysAfter( dateStartEnd1.getStart(), 0 );
            Date bookingEnd1 = DateTimeUtil.getDateDaysAfter( dateStartEnd1.getEnd(), 0 );
            if ( bookingStart1 == null ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }
            if ( bookingEnd1 == null ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }
            CardBookingStatus newStatus1 = cardbookingsStatusUpdate1.getNewStatus();
            if ( newStatus1 != CardBookingStatus.APPROVED ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }

            boolean sameNricOverlapDays = false;
            for ( CardBookingStatusUpdate cardbookingsStatusUpdate2 : cardbookingsStatusUpdateList ) {
                String cardBookingId2 = cardbookingsStatusUpdate2.getCardBookingId();
                if (Objects.equals(cardBookingId1, cardBookingId2)) {
                    continue;
                }

                CardBooking cardBooking2 = cardBookingsMap.get( cardBookingId2 );
                String nric2 = cardBooking2.getNric();
                if ( nric2 == null ) {
                    continue;
                }

                DateStartEnd dateStartEnd2 = cardBooking2.getDateStartEnd();
                if ( dateStartEnd2 == null ) {
                    continue;
                }

                Date bookingStart2 = DateTimeUtil.getDateDaysAfter( dateStartEnd2.getStart(), 0 );
                Date bookingEnd2 = DateTimeUtil.getDateDaysAfter( dateStartEnd2.getEnd(), 0 );
                if ( bookingStart2 == null ) {
                    continue;
                }
                if ( bookingEnd2 == null ) {
                    continue;
                }

                CardBookingStatus newStatus2 = cardbookingsStatusUpdate2.getNewStatus();
                if ( newStatus2 != CardBookingStatus.APPROVED ) {
                    continue;
                }

                if ( nric1.equals( nric2 ) ) {
                    long bookingStartTimeMs1 = bookingStart1.getTime();
                    long bookingEndTimeMs1 = bookingEnd1.getTime();
                    long bookingStartTimeMs2 = bookingStart2.getTime();
                    long bookingEndTimeMs2 = bookingEnd2.getTime();

                    if (( bookingStartTimeMs1 <= bookingEndTimeMs2 ) && ( bookingStartTimeMs2 <= bookingEndTimeMs1 )) {
                        sameNricOverlapDays = true;
                        break;
                    }
                }
            }

            if ( !sameNricOverlapDays ) {
                newCardbookingsStatusUpdateList.add( cardbookingsStatusUpdate1 );
                continue;
            }

            String errMsg = String.format( "Select and approve only 1 booking for Officer with ID No. %s.", nric1 );
            if ( !errMsgs.contains( errMsg ) ) {
                errMsgs.add( errMsg );
            }
        }
    }

    private void updateCardBookingStatus( List< CardBookingStatusUpdate > cardbookingsStatusUpdateList, Map< String, CardBooking > cardBookingsMap, Map< String, CardTypeDetail > cardTypeDetailsMap, List< CardBooking > approvedCardBookings,
            List< CardBooking > rejectedCardBookings, List< String > errMsgs ) {
        for ( CardBookingStatusUpdate cardbookingsStatusUpdate : cardbookingsStatusUpdateList ) {
            String cardBookingId = cardbookingsStatusUpdate.getCardBookingId();
            CardBooking cardBooking = cardBookingsMap.get( cardBookingId );
            String cardTypeId = cardBooking.getCardTypeId();
            CardTypeDetail cardTypeDetail = cardTypeDetailsMap.get( cardTypeId );
            CardBookingStatus newBookingStatus = cardbookingsStatusUpdate.getNewStatus();

            if ( newBookingStatus == CardBookingStatus.APPROVED ) {
                IsCardBookingApprovalPossibleResult result = this.isCardBookingApprovalPossible( cardTypeDetail, cardBooking );
                if ( result.isApprovalPossible() ) {
                    // approve card booking
                    cardBooking.setAllocatedCardSerialNumbers( result.getCardSerialNumbers() );
                    cardBooking.setStatus( CardBookingStatus.APPROVED );
                    this.logCardBooking( cardBooking, null );

                    approvedCardBookings.add( cardBooking );
                    cardBooking.setCardTypeDetail( cardTypeDetail );
                }
                else {
                    String errMsg = result.getNotPossibleReason();
                    if ( errMsg != null ) {
                        errMsgs.add( errMsg );
                    }
                }
            }
            else if ( newBookingStatus == CardBookingStatus.REJECTED ) {
                CardBookingStatus currentBookingStatus = cardBooking.getStatus();
                if ( currentBookingStatus == CardBookingStatus.PENDING ) {
                    // reject card booking
                    cardBooking.setAllocatedCardSerialNumbers( null );
                    cardBooking.setStatus( CardBookingStatus.REJECTED );
                    this.logCardBooking( cardBooking, "Rejected by processing officer." );

                    rejectedCardBookings.add( cardBooking );
                    cardBooking.setCardTypeDetail( cardTypeDetail );
                }
            }
            else {
                String errMsg = String.format( "Booking status specified must be APPROVED or REJECTED. cardBookingId=%s", cardBookingId );
                errMsgs.add( errMsg );
            }
        }
    }

    private CardTypeBookingsResult processCardBookings( CardTypeDetail cardTypeDetail ) {
        String departmentDescription = this.deriveDepartmentDescription( cardTypeDetail );
        CardTypeBookingsResult cardTypeBookingsResult = new CardTypeBookingsResult( cardTypeDetail, departmentDescription );
        AllocationRule allocationRule = cardTypeDetail.getAllocationRule();
        List< CardBooking > pendingCardBookings = new ArrayList<>();
        List< CardBooking > approvedCardBookings = new ArrayList<>();
        List< CardBooking > rejectedCardBookings = new ArrayList<>();

        // reject all bookings that processing date has passed
        List< CardBooking > cardBookings = this.fetchAllObsoletePendingCardBookings( cardTypeDetail );
        for ( CardBooking cardBooking : cardBookings ) {
            // reject card booking
            cardBooking.setAllocatedCardSerialNumbers( null );
            cardBooking.setStatus( CardBookingStatus.REJECTED );
            this.logCardBooking( cardBooking, "process date passed." );

            rejectedCardBookings.add( cardBooking );
            cardBooking.setCardTypeDetail( cardTypeDetail );
        }

        cardBookings = this.fetchAllPendingCardBookingsInPriorityOrder( cardTypeDetail );
        for ( CardBooking cardBooking : cardBookings ) {
            IsCardBookingApprovalPossibleResult result = this.isCardBookingApprovalPossible( cardTypeDetail, cardBooking );
            if ( result.isApprovalPossible() ) {
                if ( allocationRule == AllocationRule.FIRST_COME_FIRST_SERVED_APPROVAL ) {
                    pendingCardBookings.add( cardBooking );
                }
                else {
                    // approve card booking
                    cardBooking.setAllocatedCardSerialNumbers( result.getCardSerialNumbers() );
                    cardBooking.setStatus( CardBookingStatus.APPROVED );
                    this.logCardBooking( cardBooking, null );

                    approvedCardBookings.add( cardBooking );
                    cardBooking.setCardTypeDetail( cardTypeDetail );
                }
            }
            else {
                // reject card booking
                cardBooking.setAllocatedCardSerialNumbers( null );
                cardBooking.setStatus( CardBookingStatus.REJECTED );
                this.logCardBooking( cardBooking, result.getNotPossibleReason() );

                rejectedCardBookings.add( cardBooking );
                cardBooking.setCardTypeDetail( cardTypeDetail );
            }
        }

        // populate personal and collection details
        for ( CardBooking cardBooking : approvedCardBookings ) {
            this.populatePreferredContacts( cardBooking, true, true, true );
            this.populateOnBehalfUposPreferredContacts( cardBooking );
        }
        for ( CardBooking cardBooking : rejectedCardBookings ) {
            this.populatePreferredContacts( cardBooking, true, false, false );
            this.populateOnBehalfUposPreferredContacts( cardBooking );
        }
        for ( CardBooking cardBooking : pendingCardBookings ) {
            this.populatePreferredContacts( cardBooking, true, false, false );
        }

        cardTypeBookingsResult.setPendingCardBookings( pendingCardBookings );
        cardTypeBookingsResult.setApprovedCardBookings( approvedCardBookings );
        cardTypeBookingsResult.setRejectedCardBookings( rejectedCardBookings );

        return cardTypeBookingsResult;
    }

    private void logCardBooking( CardBooking cardBooking, String rejectedReason ) {
        String id = cardBooking.getId();
        String nric = cardBooking.getNric();
        String visitStart = null;
        String visitEnd = null;
        DateStartEnd visitStartEnd = cardBooking.getDateStartEnd();
        if ( visitStartEnd != null ) {
            visitStart = ConvertUtil.convertDateToDateString( visitStartEnd.getStart() );
            visitEnd = ConvertUtil.convertDateToDateString( visitStartEnd.getEnd() );
        }
        Integer numOfCards = cardBooking.getNumberOfCards();
        CardBookingStatus bookingStatus = cardBooking.getStatus();
        LOGGER.info( String.format( "cardBookingId=%s, nric=%s, visitStart=%s, visitEnd=%s, numOfCards=%s, status=%s, rejectedReason=%s", Util.replaceNewLine( id ), Util.replaceNewLine( nric ), visitStart, visitEnd, numOfCards,
                bookingStatus, Util.replaceNewLine( rejectedReason ) ) );
    }

    private IsCardBookingApprovalPossibleResult isCardBookingApprovalPossible( CardTypeDetail cardTypeDetail, CardBooking cardBooking ) {
        IsCardBookingApprovalPossibleResult result = new IsCardBookingApprovalPossibleResult();

        String nric = cardBooking.getNric();

        boolean possible = true;
        String notPossibleReason = "";

        Date visitStartDate = null;
        Date visitEndDate = null;
        String visitStart = null;
        String visitEnd = null;
        DateStartEnd visitStartEnd = cardBooking.getDateStartEnd();
        if ( visitStartEnd != null ) {
            visitStartDate = visitStartEnd.getStart();
            visitEndDate = visitStartEnd.getEnd();
            visitStart = ConvertUtil.convertDateToDateString( visitStartDate );
            visitEnd = ConvertUtil.convertDateToDateString( visitEndDate );
        }
        Integer maxBookingsPerMonth = cardTypeDetail.getMaxBookingsPerMonth();
        if ( maxBookingsPerMonth == null ) {
            maxBookingsPerMonth = 0;
        }

        CardBookingStatus currentBookingStatus = cardBooking.getStatus();
        if ( currentBookingStatus == CardBookingStatus.CANCELLED ) {
            possible = false;
            notPossibleReason = String.format( "Officer with ID No. %s has cancelled the card booking from %s to %s.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }
        if ( currentBookingStatus != CardBookingStatus.PENDING ) {
            possible = false;
            notPossibleReason = String.format( "Status of card booking of officer with ID No. %s from %s to %s is not PENDING.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }

        if ( this.isBlacklisted( nric, visitStartDate, visitEndDate ) ) {
            possible = false;
            notPossibleReason = String.format( "Officer with ID No. %s is blacklisted, thus card booking cannot be approved from %s to %s.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }

        String cardTypeId = cardBooking.getCardTypeId();
        if ( cardTypeId == null ) {
            possible = false;
            notPossibleReason = String.format( "Card Type ID not defined for card booking of officer with ID No. %s from %s to %s.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }

        int numOfBookingsCurrentMonth = this.getNumberOfApprovedBookingsForMonth( cardTypeId, nric, visitStartDate );
        if ( numOfBookingsCurrentMonth >= maxBookingsPerMonth ) {
            possible = false;
            notPossibleReason = String.format( "Officer with ID No. %s has reached the maximum no. of bookings / month, thus card booking cannot be approved from %s to %s.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }

        int numOfBookingsIntersectingVisitPeriod = this.getNumberOfApprovedBookingsIntersectsVisitPeriod( nric, visitStartDate, visitEndDate );
        if ( numOfBookingsIntersectingVisitPeriod >= 1 ) {
            possible = false;
            notPossibleReason = String.format( "Officer with ID No. %s has 1 approved card booking, thus card booking cannot be approved from %s to %s.", nric, visitStart, visitEnd );
            result.setApprovalPossible( possible );
            result.setNotPossibleReason( notPossibleReason );
            return result;
        }

        possible = false;
        notPossibleReason = String.format( "Card(s) is/are insufficient for Officer with ID No. %s, thus card booking cannot be approved from %s to %s.", nric, visitStart, visitEnd );

        Integer numOfCards = cardBooking.getNumberOfCards();
        List< CardDetail > activeCards = this.getActiveValidCardDetails( cardTypeDetail, visitStartDate, visitEndDate );
        List< CardBooking > allApprovedCardBookings = this.fetchAllApprovedCardBookings( cardTypeDetail, visitStartDate, visitEndDate );
        List< String > allocatedCardSerialNumbers = this.getCardSerialNumbers( allApprovedCardBookings );
        List< CardDetail > availableCards = this.removeCardDetails( activeCards, allocatedCardSerialNumbers );
        //there are available cards that can be booked
        if ( availableCards.size() > 0 && numOfCards != null && numOfCards > 0 && availableCards.size() >= numOfCards) {
            possible = true;
            notPossibleReason = "";

            ArrayList< String > cardSerialNumbers = new ArrayList<>();
            result.setCardSerialNumbers( cardSerialNumbers );
            for ( int i = 0; i < numOfCards; i++ ) {
                cardSerialNumbers.add( availableCards.get( i ).getSerialNumber() );
            }
        }

        result.setApprovalPossible( possible );
        result.setNotPossibleReason( notPossibleReason );
        return result;
    }

    private boolean isBlacklisted( String nric, Date startDate, Date endDate ) {
        boolean isBlacklisted = true;
        try {
            isBlacklisted = this.blacklistDao.isBlacklisted( nric, BlacklistModuleNameConstants.CORPORATE_CARD, startDate, endDate );
        }
        catch ( Exception e ) {
            LOGGER.info( String.format( "error when checking blacklist, assume blacklisted. nric=%s, exception=%s", nric, e.getMessage() ) );
        }
        return isBlacklisted;
    }

    private int getNumberOfApprovedBookingsForMonth( String cardTypeId, String nric, Date visitStart ) {
        Date firstDayOfMonth = DateTimeUtil.getFirstDateOfMonth( visitStart );
        Date lastDayOfMonth = DateTimeUtil.getLastDateOfMonth( visitStart );

        List< CardBookingStatus > cardBookingApprovedStatuses = new ArrayList<>();
        cardBookingApprovedStatuses.add( CardBookingStatus.APPROVED );

        List< String > cardTypeIds = new ArrayList<>();
        cardTypeIds.add( cardTypeId );

        int numOfApprovedBookings = 0;
        List< CardBooking > cardBookings = dao.getCardBookings( firstDayOfMonth, lastDayOfMonth, cardTypeIds, cardBookingApprovedStatuses, nric );
        if ( cardBookings != null ) {
            numOfApprovedBookings = cardBookings.size();
        }
        return numOfApprovedBookings;
    }

    private int getNumberOfApprovedBookingsIntersectsVisitPeriod( String nric, Date visitStart, Date visitEnd ) {
        visitStart = DateTimeUtil.getDateDaysAfter( visitStart, 0 );
        visitEnd = DateTimeUtil.getDateDaysAfter( visitEnd, 0 );

        List< CardBookingStatus > cardBookingApprovedStatuses = new ArrayList<>();
        cardBookingApprovedStatuses.add( CardBookingStatus.APPROVED );

        int numOfApprovedBookings = 0;
        List< CardBooking > cardBookings = dao.getCardBookings( visitStart, visitEnd, null, cardBookingApprovedStatuses, nric );
        if ( cardBookings != null ) {
            numOfApprovedBookings = cardBookings.size();
        }
        return numOfApprovedBookings;
    }

    private List< CardDetail > getActiveValidCardDetails( CardTypeDetail cardTypeDetail, Date visitStart, Date visitEnd ) {
        List< CardDetail > availableCardDetails = new ArrayList<>();
        List< CardDetail > cardDetails = cardTypeDetail.getCardDetails();
        if ( cardDetails != null ) {
            for ( CardDetail cardDetail : cardDetails ) {
                CardStatus cardStatus = cardDetail.getCardStatus();
                if ( cardStatus == CardStatus.AUTO_POPULATE ) {
                    DateStartEnd validityPeriod = cardDetail.getValidityPeriod();
                    Date validStart = DateTimeUtil.getDateDaysAfter( ( validityPeriod != null ) ? validityPeriod.getStart() : null, 0 );
                    Date validEnd = DateTimeUtil.getDateDaysAfter( ( validityPeriod != null ) ? validityPeriod.getEnd() : null, 1 );
                    if( ( validStart != null && validEnd != null) &&
                    (( visitStart != null ) && (( visitEnd != null ) && (( validStart.getTime() <= visitStart.getTime() ) &&
                            (( visitStart.getTime() < validEnd.getTime() ) && (( validStart.getTime() <= visitEnd.getTime() )
                                    && ( visitEnd.getTime() < validEnd.getTime() )))))) ){
                        availableCardDetails.add( cardDetail );
                    }
                }
            }
        }
        return availableCardDetails;
    }

    private List< CardBooking > fetchAllApprovedCardBookings( CardTypeDetail cardTypeDetail, Date visitStart, Date visitEnd ) {
        LOGGER.info( String.format( "fetchAllApprovedCardBookings() started. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );

        List< String > cardTypeIds = new ArrayList<>();
        cardTypeIds.add( cardTypeDetail.getId() );

        List< CardBookingStatus > cardBookingApprovedStatuses = new ArrayList<>();
        cardBookingApprovedStatuses.add( CardBookingStatus.APPROVED );

        List< CardBooking > cardBookings = dao.getCardBookings( visitStart, visitEnd, cardTypeIds, cardBookingApprovedStatuses, null );
        if ( cardBookings == null ) {
            cardBookings = new ArrayList<>();
        }
        LOGGER.info( String.format( "fetchAllApprovedCardBookings() ended. cardTypeId=%s, Number of approved card bookings: %s", Util.replaceNewLine( cardTypeDetail.getId() ), cardBookings.size() ) );
        return cardBookings;
    }

    private List< String > getCardSerialNumbers( List< CardBooking > cardBookings ) {
        List< String > cardSerialNumbers = new ArrayList<>();
        for ( CardBooking cardBooking : cardBookings ) {
            List< String > serialNumbers = cardBooking.getAllocatedCardSerialNumbers();
            if ( serialNumbers != null ) {
                cardSerialNumbers.addAll( serialNumbers );
            }
        }
        return cardSerialNumbers;
    }

    private List< CardDetail > removeCardDetails( List< CardDetail > cardDetails, List< String > serialNumbers ) {
        List< CardDetail > newCardDetails = new ArrayList<>();
        if ( cardDetails != null ) {
            for ( CardDetail cardDetail : cardDetails ) {
                boolean contain = false;
                if ( serialNumbers != null ) {
                    contain = serialNumbers.contains( cardDetail.getSerialNumber() );
                }
                if ( !contain ) {
                    newCardDetails.add( cardDetail );
                }
            }
        }
        return newCardDetails;
    }

    private List< CardBooking > fetchAllObsoletePendingCardBookings( CardTypeDetail cardTypeDetail ) {
        List< CardBookingStatus > cardBookingPendingStatuses = new ArrayList<>();
        cardBookingPendingStatuses.add( CardBookingStatus.PENDING );

        // exclude visit start date, so minimum 1 day before
        int days = Math.max( 1, cardTypeDetail.getProcessingDays() );
        Date endDate = DateTimeUtil.getDateDaysAfter( cardTypeDetail.getVisitDateStart(), -days );
        List< String > cardTypeIds = new ArrayList<>();
        cardTypeIds.add( cardTypeDetail.getId() );
        LOGGER.info( String.format( "fetchAllObsoletePendingCardBookings(). cardTypeId=%s, endDate=%s", cardTypeDetail.getId(), endDate ) );

        return dao.getCardBookings( null, endDate, cardTypeIds, cardBookingPendingStatuses, null );
    }

    private List< CardBooking > fetchAllPendingCardBookingsInPriorityOrder( CardTypeDetail cardTypeDetail ) {
        ArrayList< CardBooking > sortedCardBookings = new ArrayList<>();

        List< CardBookingStatus > cardBookingPendingStatuses = new ArrayList<>();
        cardBookingPendingStatuses.add( CardBookingStatus.PENDING );
        List< CardBookingStatus > cardBookingApprovedStatuses = new ArrayList<>();
        cardBookingApprovedStatuses.add( CardBookingStatus.APPROVED );

        Date startDate = cardTypeDetail.getVisitDateStart();
        Date endDate = cardTypeDetail.getVisitDateEnd();
        List< String > cardTypeIds = new ArrayList<>();
        cardTypeIds.add( cardTypeDetail.getId() );

        LOGGER.info( String.format( "fetchAllPendingCardBookingsInPriorityOrder(). cardTypeId=%s, visitStart=%s, visitEnd=%s", cardTypeDetail.getId(), startDate, endDate ) );

        AllocationRule allocationRule = cardTypeDetail.getAllocationRule();
        if ( allocationRule != null ) {
            List< CardBooking > cardBookings = dao.getCardBookings( startDate, endDate, cardTypeIds, cardBookingPendingStatuses, null );
            CardBooking[] tmpCardBookings = null;
            if ( cardBookings != null ) {
                for ( CardBooking cardBooking : cardBookings ) {
                    if ( allocationRule == AllocationRule.LEAST_USED ) {
                        this.populatePreviousApprovedCount( cardBooking, cardBookingApprovedStatuses );
                    }
                }
                tmpCardBookings = new CardBooking[ cardBookings.size() ];
                cardBookings.toArray( tmpCardBookings );

                switch ( allocationRule ) {
                    case FIRST_COME_FIRST_SERVED:
                    case FIRST_COME_FIRST_SERVED_APPROVAL: {
                        Arrays.sort( tmpCardBookings, new CardBookingFcfsComparator() );
                    }
                        break;
                    case LEAST_USED: {
                        Arrays.sort( tmpCardBookings, new CardBookingLeastUsedComparator() );
                    }
                        break;
                    case BALLOTING:
                    default: {
                    	
                    	try {
	                    	//Recommended by OWASP for random number generation
	                    	// Create a secure random number generator using the SHA1PRNG algorithm
	                    	SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
	                    	
	                    	// Get 128 random bytes
	                    	byte[] randomBytes = new byte[128];
	                    	secureRandomGenerator.nextBytes(randomBytes);
	                    	
	                    	// Create two secure number generators with the same seed
	                    	int seedByteCount = 5;
	                    	byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
	                    	
	                    	SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
	                    	rand.setSeed(seed);
	                    	
	                        for ( int i = 0; i < tmpCardBookings.length * 2; i++ ) {
	                            int i1 = rand.nextInt( tmpCardBookings.length );
	                            int i2 = rand.nextInt( tmpCardBookings.length );
	                            CardBooking tmp = tmpCardBookings[ i1 ];
	                            tmpCardBookings[ i1 ] = tmpCardBookings[ i2 ];
	                            tmpCardBookings[ i2 ] = tmp;
	                        }
                    	}
                    	catch(Exception e)
                    	{
                    		//do nothing
                    	}
                    }
                        break;
                }

                sortedCardBookings.addAll(Arrays.asList(tmpCardBookings));
            }
        }

        LOGGER.info( String.format( "fetchAllPendingCardBookingsInPriorityOrder() ended. cardTypeId=%s, Number of pending card bookings: %s", cardTypeDetail.getId(), sortedCardBookings.size() ) );
        return sortedCardBookings;
    }

    private void populateCardTypeName( CardBooking cardBooking, CardTypeDetail cardTypeDetail ) {
        String cardTypeName = ( cardTypeDetail == null ) ? null : cardTypeDetail.getName();
        cardBooking.setCardTypeName( cardTypeName );
    }

    private void populatePreferredContacts( CardBooking cardBooking, boolean populatePersonal, boolean populatePreviousHolders, boolean populateNextHolders ) {
        LOGGER.info("populatePreferredContacts service");
        
    	if ( populatePersonal ) {
            String nric = cardBooking.getNric();
            if (nric != null){
            	PersonalDetail personalDetail = dao.getPersonal( nric );      	
            	if (personalDetail != null){
	            	PersonalPreferredContacts personalPreferredContacts = new PersonalPreferredContacts( personalDetail );
	                String department = this.deriveDepartmentDescription( personalDetail );
	
	                cardBooking.setDepartment( department );
	                cardBooking.setPersonalPreferredContacts( personalPreferredContacts );
	
	                // following are deprecated, should remove in future
	                // use personalPreferredContacts instead     
	                if (personalPreferredContacts != null){
	                	String name = personalPreferredContacts.getName();
		                ContactMode preferredContactMode = personalPreferredContacts.getPreferredContactMode();
		                String preferredMobile = personalPreferredContacts.getPreferredMobile();
		                String preferredEmail = personalPreferredContacts.getPreferredEmail();
		                
		                cardBooking.setName( name );
		                cardBooking.setPreferredContactMode( preferredContactMode );
		                cardBooking.setPreferredMobile( preferredMobile );
		                cardBooking.setPreferredEmail( preferredEmail );
		
		                LOGGER.info( "== holder == " );
		                LOGGER.info( String.format( "%s", personalPreferredContacts ) );	
	                }
            	}
            }
        }

        Map< String, CardPreviousHolderPreferredContacts > previousHoldersMap = new HashMap<>();
        Map< String, CardNextHolderPreferredContacts > nextHoldersMap = new HashMap<>();

        List< String > cardSerialNumbersToTakeoverFromCollectionCenter = new ArrayList<>();
        List< String > cardSerialNumbersToHandoverToCollectionCenter = new ArrayList<>();
        List< CardPreviousHolderPreferredContacts > previousHolders = new ArrayList<>();
        List< CardNextHolderPreferredContacts > nextHolders = new ArrayList<>();
        boolean isPublicHoliday = false;
        
        DateStartEnd visitStartEnd = cardBooking.getDateStartEnd();
        if ( visitStartEnd != null ) {
            Date visitStart = visitStartEnd.getStart();
            Date visitEnd = visitStartEnd.getEnd();
            if ( visitStart != null && visitEnd != null ) {
                List< String > serialNumbers = cardBooking.getAllocatedCardSerialNumbers();
                if ( serialNumbers != null ) {
                    for ( String serialNumber : serialNumbers ) {
                        if ( populatePreviousHolders ) {
                            Calendar currentDate = Calendar.getInstance();
                            currentDate.setTime(visitEnd);
                            currentDate.get(Calendar.DAY_OF_WEEK);

                            ICalendarService previousCalendarService = CalendarServiceFactory.getInstance();
                            isPublicHoliday = true;
                            Date dayBeforeVisitStart;
                            dayBeforeVisitStart = visitStart;
                            if ( LOGGER.isLoggable( Level.INFO ) ) {
                                LOGGER.info(String.format("Visit Start Date: %s", visitStart));
                            }
                            String previousNric = null;
                            CardBooking previousApprovedCardBooking = null;
                            while(isPublicHoliday == true && previousNric == null)
                            {
                                dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                                if ( LOGGER.isLoggable( Level.INFO ) ) {
                                    LOGGER.info(String.format("1 day before Visit Start Date is: %s", dayBeforeVisitStart));
                                }
                                isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
                                if(isPublicHoliday)
                                {
                                    LOGGER.info("1 day before visit state date is a public holiday, to check if handover/takeover is possible for the day before");

                                    previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    if(previousNric == null)
                                    {
                                        LOGGER.info("Previous owner is not found");
                                    }
                                    else
                                    {
                                        LOGGER.info("Previous owner is found");
                                    }

                                }
                                else
                                {
                                    LOGGER.info("1 day before visit start date is not a public holiday");
                                    previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    if(previousNric == null)
                                    {
                                        LOGGER.info("Previous owner is not found");
                                    }
                                    else
                                    {
                                        LOGGER.info("Previous owner is found");
                                    }
                                }
                            }
                            Calendar dayBeforeVisitDate = Calendar.getInstance();
                            dayBeforeVisitDate.setTime(dayBeforeVisitStart);
                            int dayBeforeVisitDay = dayBeforeVisitDate.get(Calendar.DAY_OF_WEEK);
                            if(previousNric == null && dayBeforeVisitDay == Calendar.SUNDAY)
                            {
                                LOGGER.info("1 day before visit start date is a Sunday");
                                dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                                previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                if(previousNric == null)
                                {
                                    dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                                    previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                }
                            }
                            if(previousNric == null && dayBeforeVisitDay == Calendar.SATURDAY)
                            {
                                LOGGER.info("1 day before visit start date is a Saturday");
                                dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
                                previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();

                            }
                            isPublicHoliday = true;
                            dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,0);
                            if ( LOGGER.isLoggable( Level.INFO ) ) {
                                LOGGER.info(String.format("current date to check: %s", dayBeforeVisitStart));
                            }
                            while(isPublicHoliday == true && previousNric == null)
                            {

                                isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
                                if(isPublicHoliday)
                                {
                                    LOGGER.info("previous date is a public holiday, cannot directly add to collection point, to check handover/takeover");

                                    previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                    if(previousNric == null)
                                    {
                                    dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,-1);
                                    }

                                }
                                else
                                {
                                    LOGGER.info("current date is not a public holiday, can add to collection point");
                                    previousApprovedCardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
                                    previousNric = ( previousApprovedCardBooking == null ) ? null : previousApprovedCardBooking.getNric();
                                }

                            }
                            CardPreviousHolderPreferredContacts previousHolder = ( previousNric == null ) ? null : previousHoldersMap.get( previousNric );
                            if ( previousNric != null && previousHolder == null) {
                                // not in previousHoldersMap
                                // fetch from database
                                LOGGER.info("before get personal");
                                PersonalDetail previousPersonalDetail = dao.getPersonal( previousNric );
                                if ( previousPersonalDetail != null ) {
                                    previousHolder = new CardPreviousHolderPreferredContacts( previousPersonalDetail );
                                    previousHoldersMap.put( previousNric, previousHolder );
                                    previousHolders.add( previousHolder );
                                }
                            }
                            LOGGER.info("before previous holder 1913");
                            if ( previousHolder != null ) {
                                LOGGER.info("inside previous holder 1915, there is a previous owner");
                                CardLessDetail cardLessDetail = ( previousApprovedCardBooking == null ) ? null : new CardLessDetail( serialNumber, previousApprovedCardBooking );
                                if ( cardLessDetail != null ) {
                                    previousHolder.getCardLessDetails().add( cardLessDetail );
                                    LOGGER.info("Previous holder: "+previousHolder);
                                }
                            }
                            else {
                                LOGGER.info("add to collection point");

                                    cardSerialNumbersToTakeoverFromCollectionCenter.add( serialNumber );

                            }

                        }
                        if ( populateNextHolders ) {
                            LOGGER.info("populate next holders");
                            Calendar todayDate = Calendar.getInstance();
                            todayDate.setTime(visitEnd);
                            CardBooking nextApprovedCardBooking;
                            LOGGER.info("determine next holder");
                            nextApprovedCardBooking = determineNextHolder (visitEnd, serialNumber);
                            String nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
                            if ( LOGGER.isLoggable( Level.INFO ) ) {
                                LOGGER.info(String.format("next nric is: %s", nextNric));
                            }
                            CardNextHolderPreferredContacts nextHolder = ( nextNric == null ) ? null : nextHoldersMap.get( nextNric );
                            if ( nextNric != null && nextHolder == null) {
                                // not in nextHoldersMap
                                // fetch from database
                                LOGGER.info("next holder is not empty");
                                PersonalDetail nextPersonalDetail = dao.getPersonal( nextNric );
                                if ( nextPersonalDetail != null ) {
                                    nextHolder = new CardNextHolderPreferredContacts( nextPersonalDetail );
                                    nextHoldersMap.put( nextNric, nextHolder );
                                    nextHolders.add( nextHolder );
                                }
                            }
                            if ( nextHolder != null ) {
                                CardLessDetail cardLessDetail = ( nextApprovedCardBooking == null ) ? null : new CardLessDetail( serialNumber, nextApprovedCardBooking );
                                if ( cardLessDetail != null ) {
                                    LOGGER.info("add in cardlessdetails");
                                    nextHolder.getCardLessDetails().add( cardLessDetail );
                                    LOGGER.info("Next holder: "+nextHolder);
                                }
                            }
                            else {

                                LOGGER.info("add to collection point");
                                cardSerialNumbersToHandoverToCollectionCenter.add( serialNumber );

                            }
                        }
                    }
                }
            }
        }

        if ( populatePreviousHolders ) {
            LOGGER.info( String.format( "== previous holders, count=%s == ", previousHolders.size() ) );
            for ( CardPreviousHolderPreferredContacts previousHolder : previousHolders ) {
                LOGGER.info( previousHolder.toString() );
            }

            cardBooking.setPreviousHolderPreferredContacts( previousHolders );
            cardBooking.setCardSerialNumbersToTakeoverFromCollectionCenter( cardSerialNumbersToTakeoverFromCollectionCenter );
        }
        if ( populateNextHolders ) {
            LOGGER.info( String.format( "== next holders, count=%s == ", nextHolders.size() ) );
            for ( CardNextHolderPreferredContacts nextHolder : nextHolders ) {
                LOGGER.info( nextHolder.toString() );
            }

            cardBooking.setNextHolderPreferredContacts( nextHolders );
            cardBooking.setCardSerialNumbersToHandoverToCollectionCenter( cardSerialNumbersToHandoverToCollectionCenter );
        }
    }

    private void populateOnBehalfUposPreferredContacts( CardBooking cardBooking ) {
        List< PersonalPreferredContacts > uposPreferredContacts = null;
        String nric = cardBooking.getNric();
        String submitter = cardBooking.getSubmitter();
        if ( !Objects.equals( nric, submitter ) ) {
            // booking is applied on behalf

            // notification is sent to the user who apply on behalf
            List< String > upoUsers = new ArrayList<>();
            if ( submitter != null ) {
                upoUsers.add( submitter );
            }

            if (!upoUsers.isEmpty()) {
                uposPreferredContacts = personnelDao.getActivePersonnelPreferredContacts(null, null, upoUsers);
            }
        }
        if ( uposPreferredContacts == null ) {
            uposPreferredContacts = new ArrayList<>();
        }
        cardBooking.setOnBehalfUposPreferredContacts( uposPreferredContacts );
    }

    private void populatePreviousApprovedCount( CardBooking cardBooking, List< CardBookingStatus > cardBookingApprovedStatuses ) {

        int previousApprovedCardBookingsCount = 0;
        String nric = cardBooking.getNric();
        if ( nric != null ) {
            previousApprovedCardBookingsCount = dao.getCardBookingsCount( null, null, null, cardBookingApprovedStatuses, nric );
        }
        cardBooking.setNumberOfPreviousApprovedBookings( previousApprovedCardBookingsCount );
        LOGGER.info( String.format( "populatePreviousApprovedCount(), nric=%s, previous approved bookings count=%s", Util.replaceNewLine( nric ), previousApprovedCardBookingsCount ) );
    }

    private void populateCollectionDetails( CardBooking cardBooking ) {
        if ( cardBooking != null ) {
            CardTypeDetail cardTypeDetail = cardBooking.getCardTypeDetail();
            CardBookingStatus bookingStatus = cardBooking.getStatus();
            if ( bookingStatus == CardBookingStatus.APPROVED ) {
                DateStartEnd visitStartEnd = cardBooking.getDateStartEnd();
                if ( visitStartEnd != null ) {
                    Date visitStart = visitStartEnd.getStart();
                    Date visitEnd = visitStartEnd.getEnd();
                    if ( visitStart != null && visitEnd != null) {
                        List< String > serialNumbers = cardBooking.getAllocatedCardSerialNumbers();
                        if ( serialNumbers != null ) {
                            List< CardCollectionLessDetail > collectionDetailList = new ArrayList< CardCollectionLessDetail >();
                            cardBooking.setCardCollectionDetails( collectionDetailList );
                            for ( String serialNumber : serialNumbers ) {
                                CardCollectionLessDetail collectionDetail = new CardCollectionLessDetail();
                                String[] takeOverStrings = this.getTakeOverFromString( cardTypeDetail, serialNumber, visitStart );
                                String[] handOverToStrings = this.getHandOverToString( cardTypeDetail, serialNumber, visitEnd );
                                CardDetail cardDetail = this.findCardDetail( cardTypeDetail, serialNumber );
                                String cardDisplayStatus = ( cardDetail == null ) ? null : cardDetail.getCardDisplayStatus();
                                collectionDetail.setSerialNumber( serialNumber );
                                collectionDetail.setCardStatus( cardDisplayStatus );
                                collectionDetail.setCollectTakeOverFrom( takeOverStrings[ 0 ] );
                                collectionDetail.setCollectTakeOverOn( takeOverStrings[ 1 ] );
                                collectionDetail.setReturnHandoverTo( handOverToStrings[ 0 ] );
                                collectionDetail.setReturnHandoverOn( handOverToStrings[ 1 ] );
                                collectionDetailList.add( collectionDetail );
                            }
                        }
                    }
                }
            }
        }

    }

    private CardDetail findCardDetail( CardTypeDetail cardTypeDetail, String serialNumber ) {
        CardDetail result = null;
        if ( cardTypeDetail != null && serialNumber != null) {
            List< CardDetail > cardDetails = cardTypeDetail.getCardDetails();
            if ( cardDetails != null ) {
                for ( CardDetail cardDetail : cardDetails ) {
                    String cardSerialNumber = cardDetail.getSerialNumber();
                    if ( serialNumber.equalsIgnoreCase( cardSerialNumber ) ) {
                        result = cardDetail;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private String deriveDepartmentDescription( CardTypeDetail detail ) {
        String departmentDescription = null;
        if ( detail != null ) {
            String departmentId = detail.getDepartment();
            if ( departmentId != null ) {
                Code departmentCode = codeDao.getCode( CodeType.UNIT_DEPARTMENT, departmentId );
                if ( departmentCode != null ) {
                    departmentDescription = departmentCode.getDescription();
                }
            }
        }
        return departmentDescription;
    }

    private String deriveDepartmentDescription( PersonalDetail detail ) {
        String departmentDescription = null;
        if ( detail != null ) {
            Employment employment = detail.getEmployment();
            if ( employment != null ) {
                String departmentId = employment.getOrganisationOrDepartment();
                if ( departmentId != null ) {
                    Code departmentCode = codeDao.getCode( CodeType.UNIT_DEPARTMENT, departmentId );
                    if ( departmentCode != null ) {
                        departmentDescription = departmentCode.getDescription();
                    }
                }
            }
        }
        return departmentDescription;
    }

    private Map< String, CardTypeDetail > fetchAllCardTypeDetails( List<GlobalParameters> globalParameters, Date processingDate ) {
        LOGGER.info( "fetchAllCardTypeDetails() started." );
        Map< String, CardTypeDetail > cardTypeDetailsMap = new HashMap<>();
        List< CardTypeDetail > cardTypeDetails = dao.getAllPcwfGroupCardTypeDetails( true, null, null );
        if ( cardTypeDetails != null ) {
            for ( CardTypeDetail cardTypeDetail : cardTypeDetails ) {
                this.populateCardTypeDetails( cardTypeDetail, globalParameters, processingDate );
                cardTypeDetailsMap.put( cardTypeDetail.getId(), cardTypeDetail );
            }
        }
        LOGGER.info( String.format( "fetchAllCardTypeDetails() ended. Number of card types: %s", cardTypeDetailsMap.size() ) );
        return cardTypeDetailsMap;
    }

    private void populateCardTypeDetails( CardTypeDetail cardTypeDetail, List<GlobalParameters> globalParameters, Date processingDate ) {
        AllocationRule allocationRule = cardTypeDetail.getAllocationRule();
        GlobalParameters globalParameter = new GlobalParameters();
        for(int i = 0; i<globalParameters.size(); i++)
        {
        	LOGGER.info(globalParameters.get(i).getCorporateCardUnitOrPCWF());
        	if (Objects.equals(cardTypeDetail.getDepartment(), globalParameters.get(i).getCorporateCardUnitOrPCWF()))
        	{
        		globalParameter = globalParameters.get(i);
        	}
        }
        int processingDays = this.computeProcessingDays( globalParameter, allocationRule );
        int advCutOffDays = this.computeAdvProcessingCutOffDays( globalParameter, allocationRule );
        Date visitDate = DateTimeUtil.getDateDaysAfter( processingDate, advCutOffDays );
        cardTypeDetail.setProcessingDays( processingDays );
        cardTypeDetail.setVisitDateStart( visitDate );
        cardTypeDetail.setVisitDateEnd( visitDate );
        LOGGER.info( String.format( "cardTypeId:%s, processingDays=%s, visitDate=%s", Util.replaceNewLine( cardTypeDetail.getId() ), processingDays, visitDate ) );
    }

    private int computeProcessingDays( GlobalParameters globalParameters, AllocationRule rule ) {
        int processingDays = 0;
        if ( globalParameters != null ) {
            Integer fcfsApprovalProcessingDays = globalParameters.getCorporateCardFcfsApprovalProcessingPeriodDays();
            if ( rule == AllocationRule.FIRST_COME_FIRST_SERVED_APPROVAL && fcfsApprovalProcessingDays != null ) {
                processingDays += fcfsApprovalProcessingDays;
            }
        }
        return processingDays;
    }

    private int computeAdvProcessingCutOffDays( GlobalParameters globalParameters, AllocationRule rule ) {
        int advProcessingDays = 0;
        if ( globalParameters != null ) {
            Integer bookingResultsNotificationDays = globalParameters.getCorporateCardBookingResultsNotificationPeriodDays();
            Integer collectionDays = globalParameters.getCorporateCardCollectionPeriodDays();
            Integer fcfsApprovalProcessingDays = globalParameters.getCorporateCardFcfsApprovalProcessingPeriodDays();
            if ( bookingResultsNotificationDays != null ) {
                advProcessingDays += bookingResultsNotificationDays;
            }
            if ( collectionDays != null ) {
                advProcessingDays += collectionDays;
            }
            if ( rule == AllocationRule.FIRST_COME_FIRST_SERVED_APPROVAL && fcfsApprovalProcessingDays != null ) {
                advProcessingDays += fcfsApprovalProcessingDays;
            }
        }
        return advProcessingDays;
    }

    private void populateAvailabilityToday( CardTypeDetail cardTypeDetail, CardTypeLessDetail cardTypeLessDetail ) {
        if ( cardTypeDetail == null ) {
            return;
        }
        if ( cardTypeLessDetail == null ) {
            return;
        }

        Date today = DateTimeUtil.getDateDaysAfter( new Date(), 0 );
        List< CardDetail > activeCards = this.getActiveValidCardDetails( cardTypeDetail, today, today );
        List< CardBooking > allApprovedCardBookings = this.fetchAllApprovedCardBookings( cardTypeDetail, today, today );
        int activeNumberOfCardsToday = activeCards.size();
        int availableNumberOfCardsToday = activeNumberOfCardsToday - allApprovedCardBookings.size();
        availableNumberOfCardsToday = Math.max( 0, availableNumberOfCardsToday );
        cardTypeLessDetail.setActiveNumberOfCardsToday( activeNumberOfCardsToday );
        cardTypeLessDetail.setAvailableNumberOfCardsToday( availableNumberOfCardsToday );
        LOGGER.info( String.format( "cardTypeId=%s, activeNumberOfCardsToday=%s, availableNumberOfCardsToday=%s", Util.replaceNewLine( cardTypeDetail.getId() ), activeNumberOfCardsToday, availableNumberOfCardsToday ) );
    }

    private String getCancelCardBookingTemplateResultString(String cancelTemplatedString, CardTypeDetail cardTypeDetail, CardBooking cardBooking, String cancelledPersonnelName,
                                                            String handOverTakeOverPersonnelName, List< String > cardSerialNumberList ) {
        if ( cancelTemplatedString == null ) {
            return "";
        }

        CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;

        String resultString = cancelTemplatedString;
        if ( cardType == CardType.PCWF ) {
            resultString = resultString.replace( "\\$officer\\$", "PCWF Member" );
            resultString = resultString.replace( "\\$BestRegards\\$", "Best regards," );
            resultString = resultString.replace( "\\$signOff\\$", "Police Welfare Division." );
        }
        else if ( cardType == CardType.GROUP ) {
            resultString = resultString.replace( "\\$officer\\$", "Officer" );
            resultString = resultString.replace( "\\$BestRegards\\$", "Best regards." );
            resultString = resultString.replace( "\\$signOff\\$", "" );
        }

        String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "";
        StringBuilder cardSerialNumbers = new StringBuilder();
        if ( cardSerialNumberList != null ) {
            for ( String cardSerialNumber : cardSerialNumberList ) {
                if ( cardSerialNumbers.length() > 0 ) {
                    cardSerialNumbers.append( ", " );
                }
                cardSerialNumbers.append( cardSerialNumber );
            }
        }
        cancelledPersonnelName = ( cancelledPersonnelName != null ) ? cancelledPersonnelName : "";
        handOverTakeOverPersonnelName = ( handOverTakeOverPersonnelName != null ) ? handOverTakeOverPersonnelName : "";
        resultString = resultString.replace( "\\$cardName\\$", cardName );
        resultString = resultString.replace( "\\$officerA\\$", cancelledPersonnelName );
        resultString = resultString.replace( "\\$officerB\\$", handOverTakeOverPersonnelName );
        resultString = resultString.replace( "\\$cardSerialNo\\$", cardSerialNumbers.toString() );

        List< CardCollectionDetail > collectionDetails = ( cardTypeDetail != null ) ? cardTypeDetail.getCollectionDetails() : null;
        String weekDaysLocation = "";
        String weekDaysTime = "";
        String weekEndsLocation = "";
        String weekEndsTime = "";
        String publicHolidaysLocation = "";
        String publicHolidaysTime = "";
        if ( collectionDetails != null ) {
            for ( CardCollectionDetail collectionDetail : collectionDetails ) {
                String daysDescription = collectionDetail.getDaysDescription();
                if ( CardCollectionDaysDescriptionConstants.WEEK_DAYS.equals( daysDescription ) ) {
                    weekDaysLocation = this.getString( collectionDetail.getLocation(), "" );
                    TimeStartEnd timeStartEnd = collectionDetail.getTimeStartEnd();
                    Date timeStart = ( timeStartEnd != null ) ? timeStartEnd.getStart() : null;
                    Date timeEnd = ( timeStartEnd != null ) ? timeStartEnd.getEnd() : null;
                    weekDaysTime = this.getString( ConvertUtil.convertTimeToTimeString( timeStart ), "" ) + " to " + this.getString( ConvertUtil.convertTimeToTimeString( timeEnd ), "" );
                }
                else if ( CardCollectionDaysDescriptionConstants.WEEK_ENDS.equals( daysDescription ) ) {
                    weekEndsLocation = this.getString( collectionDetail.getLocation(), "" );
                    TimeStartEnd timeStartEnd = collectionDetail.getTimeStartEnd();
                    Date timeStart = ( timeStartEnd != null ) ? timeStartEnd.getStart() : null;
                    Date timeEnd = ( timeStartEnd != null ) ? timeStartEnd.getEnd() : null;
                    weekEndsTime = this.getString( ConvertUtil.convertTimeToTimeString( timeStart ), "" ) + " to " + this.getString( ConvertUtil.convertTimeToTimeString( timeEnd ), "" );
                }
                else if ( CardCollectionDaysDescriptionConstants.PUBLIC_HOLIDAYS.equals( daysDescription ) ) {
                    publicHolidaysLocation = this.getString( collectionDetail.getLocation(), "" );
                    TimeStartEnd timeStartEnd = collectionDetail.getTimeStartEnd();
                    Date timeStart = ( timeStartEnd != null ) ? timeStartEnd.getStart() : null;
                    Date timeEnd = ( timeStartEnd != null ) ? timeStartEnd.getEnd() : null;
                    publicHolidaysTime = this.getString( ConvertUtil.convertTimeToTimeString( timeStart ), "" ) + " to " + this.getString( ConvertUtil.convertTimeToTimeString( timeEnd ), "" );
                }
            }
            resultString = resultString.replace( "\\$weekDaysLocation\\$", weekDaysLocation );
            resultString = resultString.replace( "\\$weekDaysTime\\$", weekDaysTime );
            resultString = resultString.replace( "\\$weekedLocation\\$", weekEndsLocation );
            resultString = resultString.replace( "\\$weekendTime\\$", weekEndsTime );
            resultString = resultString.replace( "\\$publicHolidayLocation\\$", publicHolidaysLocation );
            resultString = resultString.replace( "\\$publicHolidayTime\\$", publicHolidaysTime );
        }

        DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
        Date visitDateStart = ( dateStartEnd != null ) ? dateStartEnd.getStart() : null;
        Date visitDateEnd = ( dateStartEnd != null ) ? dateStartEnd.getEnd() : null;
        Date collectionDate = DateTimeUtil.getDateDaysAfter( visitDateEnd, 0 );
        Date returnDate = DateTimeUtil.getDateDaysAfter( visitDateStart, 0 );
        String collectionDateString = ConvertUtil.convertDateToDateString( collectionDate );
        String returnDateString = ConvertUtil.convertDateToDateString( returnDate );
        resultString = resultString.replace( "\\$CardCollectionDate\\$", collectionDateString );
        resultString = resultString.replace( "\\$CardReturnDate\\$", returnDateString );

        return resultString;
    }

    private boolean sendCancelCardBookingNotification( PersonalPreferredContacts contact, CardType cardType, String emailSubject, String emailBody, String smsText ) {
        boolean success = false;
        String response = null;

        if ( contact == null ) {
            return false;
        }

        String nric = contact.getNric();
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotification() started. nric=%s", nric));
        }
        if ( nric != null ) {
            ContactMode preferredContactMode = contact.getPreferredContactMode();
            if ( preferredContactMode == ContactMode.SMS ) {
                if ( smsText != null ) {
                    String preferredMobile = contact.getPreferredMobile();
                    if ( preferredMobile != null ) {
                        ArrayList< String > recipients = new ArrayList<>();
                        recipients.add( preferredMobile );
                        success = this.sendSMS( recipients, smsText );
                        response = "sms.";
                    }
                    else {
                        response = "preferredMobile is null.";
                    }
                }
                else {
                    response = "smsText is null.";
                }
            }
            else if ( preferredContactMode == ContactMode.EMAIL ) {
                if ( emailSubject != null ) {
                    if ( emailBody != null ) {
                        String preferredEmail = contact.getPreferredEmail();
                        if ( preferredEmail != null ) {
                            ArrayList< String > recipients = new ArrayList<>();
                            recipients.add( preferredEmail );
                            if ( cardType == CardType.PCWF ) {
                                success = this.sendEmail( PCWF_CARD_MODULE_NAME, recipients, emailSubject, emailBody );
                                response = "email.";
                            }
                            else if ( cardType == CardType.GROUP ) {
                                success = this.sendEmail( GROUP_CARD_MODULE_NAME, recipients, emailSubject, emailBody );
                                response = "email.";
                            }
                            else {
                                response = String.format( "unknown cardType %s.", cardType );
                            }
                        }
                    }
                    else {
                        response = "emailBody is null.";
                    }
                }
                else {
                    response = "emailSubject is null.";
                }
            }
            else {
                response = String.format( "unknown preferredContactMode %s.", preferredContactMode );
            }
        }
        else {
            response = "nric is null.";
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotification() ended. nric=%s, success=%s, response=%s", nric, success, response));
        }
        return success;
    }

    private boolean sendEmail( String module, List< String > recipients, String subject, String body ) {
        IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( module );

        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( subject );
        mail.setUserAddress( config.senderAddress() );
        mail.setToRecipients( recipients );
        mail.setText( body );
        mail.setUserAddress( config.senderAddress() );

        String systemPassword = config.senderPassword();

        String encryptionKey = EnvironmentUtils.getEncryptionKey();
        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                systemPassword = encipher.decrypt( systemPassword );
            }
            catch ( Exception e ) {
                LOGGER.log( Level.SEVERE, "Error while decrypting the configured system email password.", e );
            }
        }
   
        mail.setUserPassword( systemPassword );

        INotificationService notificationService = NotificationServiceFactory.getInstance();
        try {
            notificationService.send( mail );
            return true;
        }
        catch ( NotificationServiceException e ) {
            LOGGER.log( Level.SEVERE, "Fail to send email to " + recipients, e );
            return false;
        }
    }

    private boolean sendSMS( List< String > recipients, String text ) {
        INotificationService notificationService = NotificationServiceFactory.getInstance();

        try {
            notificationService.sendExtSMS(recipients, text);
            return true;
        }
        catch ( NotificationServiceException e ) {
            LOGGER.log( Level.SEVERE, "Fail to send SMS to " + recipients, e );
            return false;
        }
    }

    private String getString( String text, String defaultString ) {
        return ( text != null ) ? text : defaultString;
    }

    private String[] getTakeOverFromString( CardTypeDetail cardTypeDetail, String serialNumber, Date visitStart ) {
        String takeOverFrom = "-";
        String takeOverFromDates = "-";
        
        Calendar visitStartDate = Calendar.getInstance();
        visitStartDate.setTime(visitStart);
        visitStartDate.get(Calendar.DAY_OF_WEEK);
        boolean isPublicHoliday = true;
        ICalendarService previousCalendarService = CalendarServiceFactory.getInstance();
        CardBooking cardBooking = null;
        Date dayBeforeVisitStart = visitStart;
        while(isPublicHoliday && cardBooking == null)
    	{
        	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		
        	}
    		else
    		{
    			LOGGER.info("previous date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    	}
        Calendar dayBeforeVisitDate = Calendar.getInstance();
    	dayBeforeVisitDate.setTime(dayBeforeVisitStart);
    	int dayBeforeVisitDay = dayBeforeVisitDate.get(Calendar.DAY_OF_WEEK);
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SUNDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	if(cardBooking == null)
        	{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SATURDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    	}
    	isPublicHoliday = true;
    	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,0);
        while(isPublicHoliday && cardBooking == null)
    	{
    		
    		isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		if(cardBooking == null)
        		{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,-1);
        		}
        	}
    		else
    		{
    			LOGGER.info("previous date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    		
    	}

        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            if ( nric != null ) {
            	PersonalDetail personalDetail = dao.getPersonal( nric );
            	if ( personalDetail != null ) {
                    String name = personalDetail.getName();
                    String preferredHandphone = this.getPreferredHandphone( personalDetail );
                    if ( name != null ) {
                        if ( preferredHandphone != null ) {
                            takeOverFrom = name + " (" + preferredHandphone + ")";
                        }
                        else {
                            takeOverFrom = name;
                        }
                        takeOverFromDates = ConvertUtil.convertDateToDateString( dayBeforeVisitStart ) + " - " + ConvertUtil.convertDateToDateString( visitStart );
                    }
                } else {
                    LOGGER.info( String.format( "takeOverFrom, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
                }
            }
        }
        else if ( cardTypeDetail != null && dayBeforeVisitStart != null) {
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("to add to collection detail, dayBeforeVisitStart is: %s", dayBeforeVisitStart));
            }
            CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayBeforeVisitStart );
            if ( cardCollectionDetail != null ) {
                takeOverFrom = cardCollectionDetail.getLocation();
                takeOverFromDates = ConvertUtil.convertDateToDateString( dayBeforeVisitStart );
            }
            else {
                LOGGER.info( String.format( "takeOverFrom, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
            }
        }

        return new String[] { takeOverFrom, takeOverFromDates };
    }

    private String[] getHandOverToString( CardTypeDetail cardTypeDetail, String serialNumber, Date visitEnd ) {
        String handOverTo = "-";
        String handOverToDates = "-";
        
        Calendar visitEndDate = Calendar.getInstance();
        visitEndDate.setTime(visitEnd);
        visitEndDate.get(Calendar.DAY_OF_WEEK);
        Date dayAfterVisitEnd = visitEnd;
        CardBooking cardBooking = null;
        boolean isPublicHoliday = true;
        ICalendarService nextCalendarService = CalendarServiceFactory.getInstance();
     
    	while(isPublicHoliday && cardBooking == null)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        		
        	}
    		else
    		{
    			LOGGER.info("next date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    	}
    	Calendar dayAfterVisitDate = Calendar.getInstance();
    	dayAfterVisitDate.setTime(dayAfterVisitEnd);
    	int dayAfterVisitDay = dayAfterVisitDate.get(Calendar.DAY_OF_WEEK);
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SATURDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	if(cardBooking == null)
        	{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
        		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SUNDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );

    	}
    	isPublicHoliday = true;
    	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,0);
        while(isPublicHoliday && cardBooking == null)
    	{
    		
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber ); 
        		if(cardBooking == null)
        		{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,1);
        		}
        	}
    		else
    		{
    			LOGGER.info("after date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    		
    	}
        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            if ( nric != null ) {
            	PersonalDetail personalDetail = dao.getPersonal( nric );
            	if ( personalDetail != null ) {
                    String name = personalDetail.getName();
                    String preferredHandphone = this.getPreferredHandphone( personalDetail );
                    if ( name != null ) {
                        if ( preferredHandphone != null ) {
                            handOverTo = name + " (" + preferredHandphone + ")";
                        }
                        else {
                            handOverTo = name;
                        }
                        handOverToDates = ConvertUtil.convertDateToDateString( visitEnd ) + " - " + ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
                    }
                } else {
                    LOGGER.info( String.format( "handOverTo, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
                }
            }
        }
        else if ( cardTypeDetail != null && dayAfterVisitEnd != null ) {
            CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayAfterVisitEnd );
            if ( cardCollectionDetail != null ) {
                handOverTo = cardCollectionDetail.getLocation();
                handOverToDates = ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
            }
            else {
                LOGGER.info( String.format( "handOverTo, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
            }
        }

        return new String[] { handOverTo, handOverToDates };
    }

    private CardCollectionDetail getCardCollectionDetail( CardTypeDetail cardTypeDetail, Date date ) {
        CardCollectionDetail cardCollectionDetail = null;
        if ( cardTypeDetail != null && date != null) {
            String daysDescription = CardCollectionDaysDescriptionConstants.WEEK_DAYS;
            if ( this.isPublicHoliday( date ) ) {
                daysDescription = CardCollectionDaysDescriptionConstants.PUBLIC_HOLIDAYS;
            }
            else if ( DateTimeUtil.isWeekEnd( date ) ) {
                daysDescription = CardCollectionDaysDescriptionConstants.WEEK_ENDS;
            }

            List< CardCollectionDetail > cardCollectionDetails = cardTypeDetail.getCollectionDetails();
            if ( cardCollectionDetails != null ) {
                for ( CardCollectionDetail tmpCardCollectionDetail : cardCollectionDetails ) {
                    String tmpDaysDescription = tmpCardCollectionDetail.getDaysDescription();
                    if ( tmpDaysDescription != null && tmpDaysDescription.equals( daysDescription)) {
                        cardCollectionDetail = tmpCardCollectionDetail;
                        break;
                    }
                }
            }
        }
        return cardCollectionDetail;
    }

    private String getPreferredHandphone( PersonalDetail detail ) {
        String preferredHandphone = "";

        final int MAX_MOBILE_COUNT = 4;

        if ( detail != null ) {
            int mobileCount = 0;
            List< Phone > phoneList = detail.getPhoneContacts();
            if ( phoneList != null ) {
                for ( Phone phone : phoneList ) {
                    if ( phone.getLabel() == ContactLabel.MOBILE &&mobileCount < MAX_MOBILE_COUNT ) {
                        String tempMobileContact = phone.getNumber();
                        if ( tempMobileContact != null && tempMobileContact.length() > 0 && phone.isPrefer()) {
                            preferredHandphone = tempMobileContact;
                        }
                        mobileCount++;
                    }
                    else if ( phone.getLabel() == ContactLabel.OTHERS ) {
                        String tempOtherHandphoneContact = phone.getNumber();
                        if ( tempOtherHandphoneContact != null && tempOtherHandphoneContact.length() > 0 && phone.isPrefer()) {
                            preferredHandphone = tempOtherHandphoneContact;
                        }
                    }
                }
            }
        }
        return preferredHandphone;
    }

    public boolean isPublicHoliday( Date date ) {
        boolean isPublicHoliday = false;
        List< PublicHoliday > publicHolidays = null;
        if ( date != null ) {
            publicHolidays = calendarDao.getPublicHolidays( date );
        }
        if ( publicHolidays != null && publicHolidays.size() > 0 ) {
            isPublicHoliday = true;
        }
        return isPublicHoliday;
    }
    
    private CardBooking determineNextHolder(Date visitEnd, String serialNumber)
    {
    	Calendar todayDate = Calendar.getInstance();
    	todayDate.setTime(visitEnd);
    	int day = todayDate.get(Calendar.DAY_OF_WEEK);
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("today date is: %s", day));
        }
    	CardBooking nextApprovedCardBooking = new CardBooking();
    	ICalendarService nextCalendarService = CalendarServiceFactory.getInstance();
    	Date dayAfterVisitEnd = null;
    	dayAfterVisitEnd = visitEnd;
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("dayAfterVisitEnd before getting into loop: %s", dayAfterVisitEnd));
        }
    	String nextNric = null;
    	boolean isPublicHoliday = true;
    	while(isPublicHoliday && nextNric == null)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("dayAfterVisitEnd inside loop: %s", dayAfterVisitEnd));
            }
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null :nextApprovedCardBooking.getNric();
        		
        	}
    		else
    		{
    			LOGGER.info("next date is not a public holiday");
    			nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null :nextApprovedCardBooking.getNric();
    		}
    	}
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("dayAfterVisitEnd %s", dayAfterVisitEnd));
        }
    	Calendar dayAfterVisitDate = Calendar.getInstance();
    	dayAfterVisitDate.setTime(dayAfterVisitEnd);
    	int dayAfterVisitDay = dayAfterVisitDate.get(Calendar.DAY_OF_WEEK);
    	if(nextNric == null && dayAfterVisitDay == Calendar.SATURDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
        	if(nextNric == null)
        	{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
            	nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();	
        	}	
    	}
    	if(nextNric == null && dayAfterVisitDay == Calendar.SUNDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();	

    	}
    	isPublicHoliday = true;
    	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,0);
        while(isPublicHoliday && nextNric == null)
    	{
    		
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
            	nextNric = ( nextApprovedCardBooking == null ) ? null : nextApprovedCardBooking.getNric();
            	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,1);
        		
        	}
    		else
    		{
    			LOGGER.info("previous date is not a public holiday");
    			nextApprovedCardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    		
    	}
        return nextApprovedCardBooking;
    }
    public int getNumberOfCardBookingCancellations ( String nric) throws CorporateCardException
    {
    	int result = 0;
    	try {
            SessionFactoryUtil.beginTransaction();
            Calendar startDateCalendar = Calendar.getInstance();
            Calendar todayDateCalendar = Calendar.getInstance();
            startDateCalendar.set(Calendar.MONTH, (startDateCalendar.get(Calendar.MONTH)-1));
            result = dao.getNumberOfCardBookingCancellations(nric, startDateCalendar, todayDateCalendar);

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getNumberOfCardBookingCancellations() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return result;
    }
    
    public void sendEmailForApprovedBookings(CardBooking cardBooking) throws CorporateCardException
    {
    		 try {
    			 SessionFactoryUtil.beginTransaction();
                 
                 String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
                 
                 CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? dao.getCardTypeDetail( cardTypeId ) : null;
                 CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
                 
                 Template successTemplate = templateDao.getTemplateByName(SUCCESS_CARD_BOOKING_TEMPLATE_NAME);
                 Template prevHolderTemplate = templateDao.getTemplateByName(SUCCESS_PREV_HOLDER_TEMPLATE_NAME);
                 Template nextHolderTemplate = templateDao.getTemplateByName(SUCCESS_NEXT_HOLDER_TEMPLATE_NAME);
                 
    			 LOGGER.info("Start populatePreferredContacts");
                 this.populatePreferredContacts( cardBooking, true, true, true );
                 LOGGER.info("Completed populatePreferredContacts");
                 
                 PersonalPreferredContacts personalPreferredContact;
                 if (cardBooking != null) personalPreferredContact = cardBooking.getPersonalPreferredContacts();
                 else personalPreferredContact = null;
                 List <CardPreviousHolderPreferredContacts> previousHolderPreferredContacts;
                 if (cardBooking != null)
                     previousHolderPreferredContacts = cardBooking.getPreviousHolderPreferredContacts();
                 else previousHolderPreferredContacts = new ArrayList<>();
                 List <CardNextHolderPreferredContacts> nextHolderPreferredContacts;
                 if (cardBooking != null) nextHolderPreferredContacts = cardBooking.getNextHolderPreferredContacts();
                 else nextHolderPreferredContacts = new ArrayList<>();
                 List <String> cardSerialNumbersToHandoverToCollectionPoint;
                 if (cardBooking != null)
                     cardSerialNumbersToHandoverToCollectionPoint = cardBooking.getCardSerialNumbersToHandoverToCollectionCenter();
                 else cardSerialNumbersToHandoverToCollectionPoint = new ArrayList<>();
                 List <String> cardSerialNumbersToTakeoverFromCollectionPoint;
                 if (cardBooking != null)
                     cardSerialNumbersToTakeoverFromCollectionPoint = cardBooking.getCardSerialNumbersToTakeoverFromCollectionCenter();
                 else cardSerialNumbersToTakeoverFromCollectionPoint = new ArrayList<>();

                 String successEmailBody = ( successTemplate != null) ? successTemplate.getEmailBody() : null;
                 String successEmailSubject = ( successTemplate != null) ? successTemplate.getEmailSubject() : null;
                 String successSmsText = ( successTemplate != null ) ? successTemplate.getSmsText() : null;
                 
                 DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
             	 String visitStartDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getStart()): "-";
             	 String visitEndDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getEnd()) : "-";
                 
             	 String handTakeOverString = "";
             	 //Removed for SAST Fixes 2021
             	 if (!cardSerialNumbersToTakeoverFromCollectionPoint.isEmpty()) {
             		 LOGGER.info("TAKE OVER FROM COLLECTION POINT");
                	 //Serial Numbers to take over from collection point
             		 //Inform Applicant to collect from collection point string
                	 handTakeOverString = "Please collect the card(s) from the Collection Point.";
                	 
                	 //Initialize Body, Subject, SMS
                	 String informApplicantToTakeOverCPEmailBody = successEmailBody;
                	 String informApplicantToTakeOVerCPEmailSubject = successEmailSubject;
                	 String informApplicantToTakeOverCPSmsText = successSmsText;
                	 
                	 //Populate Body, Subject, SMS
                	 informApplicantToTakeOverCPEmailBody = this.getPrevAndNextTemplateString(informApplicantToTakeOverCPEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToTakeoverFromCollectionPoint, handTakeOverString,true);
                	 informApplicantToTakeOVerCPEmailSubject = this.getPrevAndNextTemplateString(informApplicantToTakeOVerCPEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToTakeoverFromCollectionPoint, handTakeOverString, true);
                	 informApplicantToTakeOverCPSmsText = this.getPrevAndNextTemplateString(informApplicantToTakeOverCPSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToTakeoverFromCollectionPoint, handTakeOverString, true);
                	 
                	 //Send notification to inform applicant to collect from collection point
                	 this.sendCancelCardBookingNotification(personalPreferredContact, cardType, informApplicantToTakeOVerCPEmailSubject, informApplicantToTakeOverCPEmailBody, informApplicantToTakeOverCPSmsText);
                 } 
                 //Serial Numbers to take over from previous holder
                 LOGGER.info("Previous Holder List: " + previousHolderPreferredContacts);
                 if (previousHolderPreferredContacts.size() > 0) {
                	 LOGGER.info("INSIDE PREVIOUS HOLDER");
                	 String prevHolderEmailBody = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailBody() : null;
                     String prevHolderEmailSubject = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailSubject() : null;
                     String prevHolderSmsText = ( prevHolderTemplate != null ) ? prevHolderTemplate.getSmsText() : null;
                     
                     for (CardPreviousHolderPreferredContacts previousHolderPreferredContact : previousHolderPreferredContacts) {
                    	 List<CardLessDetail> cardLessDetails = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getCardLessDetails(): new ArrayList<>();
                    	 List <String> cardSerialNumbers = new ArrayList<>();
                    	 for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		 String serialNumber = cardLessDetail.getSerialNumber();
                    		 if (serialNumber != null) {
                    			 cardSerialNumbers.add(serialNumber);
                    		 }
                    	 }
                    	 //Initialize Body, Subject, SMS
                    	 String informPrevToHandOverEmailBody = prevHolderEmailBody;
                    	 String informPrevToHandOverEmailSubject = prevHolderEmailSubject;
                    	 String informPrevToHandOverSmsText = prevHolderSmsText;
                    	 
                    	 //Popuplate Body, Subject, SMS
                    	 informPrevToHandOverEmailBody = this.getPrevAndNextTemplateString(informPrevToHandOverEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers,"",false);
                     	 informPrevToHandOverEmailSubject = this.getPrevAndNextTemplateString(informPrevToHandOverEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, "", false);
                    	 informPrevToHandOverSmsText = this.getPrevAndNextTemplateString(informPrevToHandOverSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers,"",false);
                    	 
                     	 //Inform Applicant to take over card from previous holder string
                     	 handTakeOverString = "Please take over the card(s) to " + 
                     	 previousHolderPreferredContact.getName() + " by " + visitStartDate;
                    	 
                     	 //Initialize Body, Subject, SMS String
                    	 String infoApplicantToTakeOverEmailBody = successEmailBody;
                    	 String infoApplicantToTakeOverEmailSubject = successEmailSubject;
                    	 String infoApplicantToTakeOverSmsText = successSmsText;
                    	 
                    	 //Populate Body, Subject, SMS String
                    	 infoApplicantToTakeOverEmailBody = this.getPrevAndNextTemplateString(infoApplicantToTakeOverEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                    	 infoApplicantToTakeOverEmailSubject = this.getPrevAndNextTemplateString(infoApplicantToTakeOverEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, "", false);
                    	 infoApplicantToTakeOverSmsText = this.getPrevAndNextTemplateString(infoApplicantToTakeOverSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, "", false);
                    	 
                    	 //Send notification to applicant on who to take over from
                     	 this.sendCancelCardBookingNotification(personalPreferredContact, cardType, infoApplicantToTakeOverEmailSubject, infoApplicantToTakeOverEmailBody, infoApplicantToTakeOverSmsText);
                     	 //Send notification to previous holder on who to hand over to
                     	 this.sendCancelCardBookingNotification(previousHolderPreferredContact, cardType, informPrevToHandOverEmailSubject, informPrevToHandOverEmailBody, informPrevToHandOverSmsText);
                     }
                }
                // Removed for SAST Fixes 2021

                //Serial numbers to hand over to collection point
                if ( !cardSerialNumbersToHandoverToCollectionPoint.isEmpty() ) {
                	LOGGER.info("HAND OVER TO COLLECTION POINT");
                	
                	//Inform Applicant to return to card to collection point string
                	handTakeOverString = "Please return the card(s) to the Collection Point.";
                	
                	//Initialize Body, Subject, SMS
                	String informApplicantToHandOverCPEmailBody = successEmailBody;
                	String informApplicantToHandOverCPEmailSubject = successEmailSubject;
                	String informApplicantToHandOverCPSmsText = successSmsText;
                	
                	//Populate Body, Subject, SMS
                	informApplicantToHandOverCPEmailBody = this.getPrevAndNextTemplateString(informApplicantToHandOverCPEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToHandoverToCollectionPoint, handTakeOverString,true);
                	informApplicantToHandOverCPEmailSubject = this.getPrevAndNextTemplateString(informApplicantToHandOverCPEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToHandoverToCollectionPoint, handTakeOverString, true);
                	informApplicantToHandOverCPSmsText = this.getPrevAndNextTemplateString(informApplicantToHandOverCPSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbersToHandoverToCollectionPoint, handTakeOverString, true);
               	 	
                	//Send Email to inform applicant to return to collection point
               	    this.sendCancelCardBookingNotification(personalPreferredContact, cardType, informApplicantToHandOverCPEmailSubject, informApplicantToHandOverCPEmailBody, informApplicantToHandOverCPSmsText);
                }
    
                //Serial numbers to hand over to next holder
                LOGGER.info("Next Holder List: " + nextHolderPreferredContacts);
                if (nextHolderPreferredContacts.size() > 0) {
                	LOGGER.info("INSIDE NEXT HOLDER");
                	
                	String nextHolderEmailBody = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailBody() : null;
                    String nextHolderEmailSubject = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailSubject() : null;
                    String nextHolderSmsText = ( nextHolderTemplate != null ) ? nextHolderTemplate.getSmsText() : null;
                         
                    for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
                    	List<CardLessDetail> cardLessDetails = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getCardLessDetails() : new ArrayList<>();
                    	List <String> cardSerialNumbers = new ArrayList<>();
                    	for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		String serialNumber = cardLessDetail.getSerialNumber();
                    			if (serialNumber != null) {
                    				cardSerialNumbers.add(serialNumber);
                    			}
                    	}
                    	
                    	//Inform applicant to hand over to next holder
                        if (nextHolderPreferredContact!=null) {
                            handTakeOverString = "Please hand over the card(s) to " +
                                    nextHolderPreferredContact.getName() + " by " + visitEndDate;
                        }
                    	
                    	//Initialize Body, Subject, SMS
                    	String informNextToTakeOverEmailBody = nextHolderEmailBody;
                    	String informNextToTakeOverEmailSubject = nextHolderEmailSubject;
                    	String informNextToTakeOverSmsText = nextHolderSmsText;
                    	
                    	//Populate Body, Subject, SMS
                    	informNextToTakeOverEmailBody = this.getPrevAndNextTemplateString(informNextToTakeOverEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                    	informNextToTakeOverEmailSubject = this.getPrevAndNextTemplateString(informNextToTakeOverEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                     	informNextToTakeOverSmsText = this.getPrevAndNextTemplateString(informNextToTakeOverSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                     	
                     	//Initialize Body, Subject, SMS
                     	String informApplicantToHandOverEmailBody = successEmailBody;
                     	String informApplicantToHandOverEmailSubject = successEmailSubject;
                     	String informApplicantToHandOverSmsText = successSmsText;
                     	
                     	//Populate Body, Subject, SMS
                     	informApplicantToHandOverEmailBody = this.getPrevAndNextTemplateString(informApplicantToHandOverEmailBody, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                     	informApplicantToHandOverEmailSubject = this.getPrevAndNextTemplateString(informApplicantToHandOverEmailSubject, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                     	informApplicantToHandOverSmsText = this.getPrevAndNextTemplateString(informApplicantToHandOverSmsText, cardTypeDetail, cardBooking, personalPreferredContact, cardSerialNumbers, handTakeOverString, false);
                     	
                     	//Send email to inform applicant to hand over to next
                     	this.sendCancelCardBookingNotification(personalPreferredContact, cardType, informApplicantToHandOverEmailSubject, informApplicantToHandOverEmailBody, informApplicantToHandOverSmsText);
                     	
                     	//Send email to inform next to take over from applicant
                     	this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, informNextToTakeOverEmailSubject, informNextToTakeOverEmailBody, informNextToTakeOverSmsText);
                    }
                 }
                 SessionFactoryUtil.commitTransaction();
    		 }	 
             catch ( Exception e ) {
                 LOGGER.log( Level.WARNING, "sendEmailForApprovedBookings failed.", e );
             }
    	}
    
    public String getPrevAndNextTemplateString(String templateString, CardTypeDetail cardTypeDetail, CardBooking cardBooking,
    		PersonalPreferredContacts personalPreferredContact, 
    		List <String> cardSerialNumberList,
    		String handTakeOverString,
    		boolean isCollectionPoint) {
    	
    	String resultString = templateString;
    	
    	//Table String for collection point information
    	String contactDetailString = "<br><br><u>Contact Information</u>" + 
    	"<br><table style=\"width:50%\"><tr><th align=\"left\">Name</th><th align=\"left\">Email</th><th align=\"left\">Phone</th></tr>" +
        "<tr><td>$name$</td><td>$preferredEmail$</td><td>$preferredPhone$</td></tr>" + 
        "<style>table, th, td { border: 1px solid black;}</style>";
    	
    	//Table String for collection point information
    	String collectionPointString = "<br><br><u>Card Collection Information</u>" +
    	"<br><table style=\"width:50%\"><tr><th align=\"left\">Card Collection Point</th><th align=\"left\">Day</th><th align=\"left\">Time</th></tr>" +
    	"<tr><td>$weekDaysLocation$</td><td>Monday to Friday</td><td>$weekDaysTime$</td></tr>" +
    	"</tr></table><style>table, th, td { border: 1px solid black;}</style>"; 
    	
    	CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
    	String visitStartDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getStart()): "-";
    	String visitEndDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getEnd()) : "-";
    	String handTakeOverName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : null;
    	
    	if ( cardType == CardType.PCWF ) {
    		resultString = resultString.replace("\\$Officer\\$", "PCWF Member");
    		resultString = resultString.replace("\\BestRegards\\$", "Best Regards,");
    		resultString = resultString.replace("\\$signOff\\$", "Police Welfare Division");
    	} else if ( cardType == CardType.GROUP ) {
    		resultString = resultString.replace("\\$Officer\\$", "Officer");
    		resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
    		resultString = resultString.replace("\\$signOff\\$", "");
    	}
    	
    	resultString = resultString.replace("\\$cardName\\$", cardName);
    	resultString = resultString.replace("\\$name\\$", handTakeOverName);
    	
    	StringBuilder cardSerialNumbers = new StringBuilder();
    	if (cardSerialNumberList != null) {
    		for (String cardSerialNumber: cardSerialNumberList) {
    			if (cardSerialNumbers.length() > 0) {
    				cardSerialNumbers.append(", ");
    			}
    			cardSerialNumbers.append(cardSerialNumber);
    		}
    	}
    	
    	resultString = resultString.replace("\\$cardSerialNo\\$", cardSerialNumbers.toString());
    	resultString = resultString.replace("\\$visitStartDate\\$", visitStartDate);
    	resultString = resultString.replace("\\$visitEndDate\\$", visitEndDate);
    	resultString = resultString.replace("\\$handTakeOver\\$", handTakeOverString);
    	
    	if (collectionPointString != null && isCollectionPoint) {
    		LOGGER.info("COLLECTION DETAILS");
    		
    		List< CardCollectionDetail > collectionDetails = ( cardTypeDetail != null ) ? cardTypeDetail.getCollectionDetails() : null;
            String weekDaysLocation = "";
            String weekDaysTime = "";
    
            if ( collectionDetails != null ) {
                for ( CardCollectionDetail collectionDetail : collectionDetails ) {
                    String daysDescription = collectionDetail.getDaysDescription();
                    if ( CardCollectionDaysDescriptionConstants.WEEK_DAYS.equals( daysDescription ) ) {
                        weekDaysLocation = this.getString( collectionDetail.getLocation(), "" );
                        TimeStartEnd timeStartEnd = collectionDetail.getTimeStartEnd();
                        Date timeStart = ( timeStartEnd != null ) ? timeStartEnd.getStart() : null;
                        Date timeEnd = ( timeStartEnd != null ) ? timeStartEnd.getEnd() : null;
                        weekDaysTime = this.getString( ConvertUtil.convertTimeToTimeString( timeStart ), "" ) + " to " + this.getString( ConvertUtil.convertTimeToTimeString( timeEnd ), "" );
                    }
                    
                }
                collectionPointString = collectionPointString.replace( "\\$weekDaysLocation\\$", weekDaysLocation );
                collectionPointString = collectionPointString.replace( "\\$weekDaysTime\\$", weekDaysTime );
                
    		resultString = resultString.replace("\\$handTakeOverDetail\\$", collectionPointString);
            }
    	}
        
    	if (contactDetailString != null && !isCollectionPoint) {
    		LOGGER.info("CONTACT DETAILS");
    		if (personalPreferredContact != null) {
            	
            	String preferredEmail = ( personalPreferredContact != null ) ? personalPreferredContact.getPreferredEmail() : "-";
            	String preferredMobile = ( personalPreferredContact != null ) ? personalPreferredContact.getPreferredMobile() : "-";
          
            	if (contactDetailString != null) {
            		contactDetailString = contactDetailString.replace("\\$name\\$", handTakeOverName);
        			contactDetailString = contactDetailString.replace("\\$preferredEmail\\$", preferredEmail);
        			contactDetailString = contactDetailString.replace("\\$preferredPhone\\$", preferredMobile);
            	}
            	
            	resultString = resultString.replace("\\$handTakeOverDetail\\$", contactDetailString);
            	
            }
    	}
    	return resultString;
    }
    
    public boolean isPublicHoliday( ICalendarService calendarService, Date date ) {
        boolean isPublicHoliday = false;
        List< PublicHoliday > publicHolidays = null;
        if ( date != null ) {
            try {
                publicHolidays = calendarService.getPublicHolidaysForCorporateCard( date );
            }
            catch ( CalendarServiceException e ) {
                LOGGER.info( "Fail to get public holiday" );
            }
        }
        if ( publicHolidays != null && publicHolidays.size() > 0 ) {
            isPublicHoliday = true;
        }
        return isPublicHoliday;
    }
    
    public void sendBroadcast (List <String> emailList, List <String> smsList,  CardTypeDetail cardTypeDetail, String subject, String body, String smsText) {
    	
    	GlobalParameters globalParameters = null;
        String senderEmail;
        String senderEmailPassword;
        String emailSignOff;
        
        CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
        
        SessionFactoryUtil.beginTransaction();
        //Get global parameters
        if (cardType == CardType.PCWF) {
       	 globalParameters = globalParametersDao.getGlobalParametersByUnit(PCWF_CARD, null);
        } else {
            if (cardTypeDetail!=null) {
                globalParameters = globalParametersDao.getGlobalParametersByUnit(cardTypeDetail.getDepartment(), null);
            }
        }
        
        //Determine senderEmail, senderEmailPassword, emailSignOff
        if (globalParameters != null) {
        	senderEmail = globalParameters.getSenderEmail();
            senderEmailPassword = globalParameters.getSenderEmailPassword();
            emailSignOff = globalParameters.getEmailSignOff();
        } else {
        	LOGGER.info("globalParameter is null, using default setting from IMailSenderConfig ..");
        	//To cater for unit that did not set globalParameters, likely wont happen
        	//Use defualt setting from IMailSenderConfig instead
        	//CCBG: Group Card, CCBP: PCWF Card
       	 	if (cardType == CardType.PCWF) {
       	 		IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( PCWF_CARD_MODULE_NAME );
       	 		senderEmail = config.senderAddress();
       	 		senderEmailPassword = config.senderPassword();
       	 		emailSignOff = POLICE_WELFARE_DIVISION;
       	 	} else {
       	 		IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( GROUP_CARD_MODULE_NAME );
       	 		senderEmail = config.senderAddress();
       	 		senderEmailPassword = config.senderPassword();
       	 		emailSignOff = null;
       	 	}
        }
        
        SessionFactoryUtil.commitTransaction();
        
        //Replace Email / SMS
        subject = this.getTemplateForBroadcast(subject, emailSignOff, cardTypeDetail);
        body = this.getTemplateForBroadcast(body, emailSignOff, cardTypeDetail);
        smsText = this.getTemplateForBroadcast(smsText, emailSignOff, cardTypeDetail);
    	
    	try {
	    	if (!emailList.isEmpty()) {
	    		emailUtil.sendBroadcastEmail(subject, body, senderEmail, senderEmailPassword, emailList);
	    	}
	    	
	    	if (!smsList.isEmpty()) {
	    		smsUtil.sendExtSMS(smsText, smsList);
	    	}
	    	
    	} catch (Exception e) {
    		LOGGER.log( Level.WARNING, "sendBroadcast Failed..", e );
    	}
    }
    
    public String getTemplateForHandTakeOver (String templateString, CardTypeDetail cardTypeDetail, CardBooking cardBooking, 
    		List <String> cardSerialNumberList, String recipientName, String handTakeOverName, String signOff) {
    	
    	String resultString = templateString;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
    	String visitStartDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getStart()): "-";
    	String visitEndDate = ( dateStartEnd != null ) ? ConvertUtil.convertDateToDateString(dateStartEnd.getEnd()) : "-";
    	
    	StringBuilder cardSerialNumbers = new StringBuilder();
    	if (cardSerialNumberList != null) {
    		for (String cardSerialNumber: cardSerialNumberList) {
    			if (cardSerialNumbers.length() > 0) {
    				cardSerialNumbers.append(", ");
    			}
    			cardSerialNumbers.append(cardSerialNumber);
    		}
    	}
    	resultString = resultString.replace("\\$Officer\\$", recipientName);
    	resultString = resultString.replace("\\$passName\\$", cardName);
    	resultString = resultString.replace("\\$passSerialNo\\$", cardSerialNumbers.toString());
    	resultString = resultString.replace("\\$visitStartDate\\$", visitStartDate);
    	resultString = resultString.replace("\\$visitEndDate\\$", visitEndDate);
      	if (handTakeOverName != null) {
      		resultString = resultString.replace("\\$name\\$", handTakeOverName);
      	}
      	if (signOff != null) {
      		if (signOff.length() > 0) {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards,");
          		resultString = resultString.replace("\\$signOff\\$", this.formatSignOff(signOff));
      		} else {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      			resultString = resultString.replace("\\$signOff\\$", "");
      		}
      	} else {
      		resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      		resultString = resultString.replace("\\$signOff\\$", "");
      	}
    	return resultString;
    }
    
    public void sendEmailForSuccessfulBookings(CardBooking cardBooking) throws CorporateCardException
    {
    		 try {
    			 SessionFactoryUtil.beginTransaction();
                 
                 String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
                 
                 CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? dao.getCardTypeDetail( cardTypeId ) : null;
                 CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
                 
                 Template successTemplate = templateDao.getTemplateByName(SUCCESS_CARD_BOOKING_TEMPLATE_NAME);
                 Template prevHolderTemplate = templateDao.getTemplateByName(SUCCESS_PREV_HOLDER_TEMPLATE_NAME);
                 Template nextHolderTemplate = templateDao.getTemplateByName(SUCCESS_NEXT_HOLDER_TEMPLATE_NAME);
                 
                 GlobalParameters globalParameters = null;
                 String senderEmail;
                 String senderEmailPassword;
                 String emailSignOff;
                 
                 if (cardType == CardType.PCWF) {
                	 globalParameters = globalParametersDao.getGlobalParametersByUnit(PCWF_CARD, null);
                 } else {
                	 globalParameters = globalParametersDao.getGlobalParametersByUnit(cardTypeDetail.getDepartment(), null);
                 }
                 
                 if (globalParameters != null) {
                	   senderEmail = globalParameters.getSenderEmail();
                       senderEmailPassword = globalParameters.getSenderEmailPassword();
                       emailSignOff = globalParameters.getEmailSignOff();
                 } else {
                	 LOGGER.info("globalParameter is null, using default setting from IMailSenderConfig ..");
                	 if (cardType == CardType.PCWF) {
                		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( PCWF_CARD_MODULE_NAME );
                    	 senderEmail = config.senderAddress();
                    	 senderEmailPassword = config.senderPassword();
                    	 emailSignOff = POLICE_WELFARE_DIVISION;
                	 } else {
                		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( GROUP_CARD_MODULE_NAME );
                    	 senderEmail = config.senderAddress();
                    	 senderEmailPassword = config.senderPassword();
                    	 emailSignOff = null;
                	 }
                 }
                 
    			 LOGGER.info("=== START populatePreferredContacts ===");
                 this.populatePreferredContacts( cardBooking, true, true, true );
                 LOGGER.info("=== END populatePreferredContacts ===");
                 
                 PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
                 String recipientName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : "Officer";
                 List <CardPreviousHolderPreferredContacts> previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : new ArrayList<>();
                 List <CardNextHolderPreferredContacts> nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : new ArrayList<>();
                 
                 List <String> allocatedSerialNumbers = ( cardBooking != null) ? cardBooking.getAllocatedCardSerialNumbers() : new ArrayList<>();
                         
                 String successEmailBody = ( successTemplate != null) ? successTemplate.getEmailBody() : null;
                 String successEmailSubject = ( successTemplate != null) ? successTemplate.getEmailSubject() : null;
                 String successSmsText = ( successTemplate != null ) ? successTemplate.getSmsText() : null;
                 
                 DateStartEnd dateStartEnd = cardBooking.getDateStartEnd();
                 if ((dateStartEnd != null)) {
                     ConvertUtil.convertDateToDateString(dateStartEnd.getStart());
                 }
                 if ((dateStartEnd != null)) {
                     ConvertUtil.convertDateToDateString(dateStartEnd.getEnd());
                 }

                 //Email to Officer
             	 successEmailBody = this.getTemplateForHandTakeOver(successEmailBody, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);
             	 successEmailSubject = this.getTemplateForHandTakeOver(successEmailSubject, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);
             	 successSmsText = this.getTemplateForHandTakeOver(successSmsText, cardTypeDetail, cardBooking, allocatedSerialNumbers, recipientName, null, emailSignOff);

                 if ( LOGGER.isLoggable( Level.INFO ) ) {
                     LOGGER.info(String.format("Officer Name: %s ,Serial Numbers:  %s", recipientName, allocatedSerialNumbers));
                 }
             	 this.sendCardBookingNotification(personalPreferredContact, senderEmail, senderEmailPassword, successEmailSubject, successEmailBody, successSmsText);
             	 
                 //Serial Numbers to take over from previous holder
                 if (previousHolderPreferredContacts.size() > 0) {
                	 LOGGER.info("Need to inform previous holder");
                     for (CardPreviousHolderPreferredContacts previousHolderPreferredContact : previousHolderPreferredContacts) {
                    	 
                    	 String prevHolderEmailBody = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailBody() : null;
                         String prevHolderEmailSubject = ( prevHolderTemplate != null) ? prevHolderTemplate.getEmailSubject() : null;
                         String prevHolderSmsText = ( prevHolderTemplate != null ) ? prevHolderTemplate.getSmsText() : null;
                    	 
                    	 List<CardLessDetail> cardLessDetails = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getCardLessDetails(): new ArrayList<>();
                    	 List <String> cardSerialNumbers = new ArrayList<>();
                    	 for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		 String serialNumber = cardLessDetail.getSerialNumber();
                    		 if (serialNumber != null) {
                    			 cardSerialNumbers.add(serialNumber);
                    		 }
                    	 }
                    	 
                    	 String handTakeOverName = (previousHolderPreferredContact != null) ? previousHolderPreferredContact.getName() : "an officer";
                         if ( LOGGER.isLoggable( Level.INFO ) ) {
                             LOGGER.info(String.format("Previous Holder Name: %s , Serial Numbers: %s", handTakeOverName, cardSerialNumbers));
                         }
                    	 prevHolderEmailBody = this.getTemplateForHandTakeOver(prevHolderEmailBody, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 prevHolderEmailSubject = this.getTemplateForHandTakeOver(prevHolderEmailSubject, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 prevHolderSmsText = this.getTemplateForHandTakeOver(prevHolderSmsText, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 
                    	 //Inform previous holder who to hand over to
                    	 this.sendCardBookingNotification(previousHolderPreferredContact, senderEmail, senderEmailPassword, prevHolderEmailSubject, prevHolderEmailBody, prevHolderSmsText);
                     }	  	
                }
                //Serial numbers to hand over to next holder
                if (nextHolderPreferredContacts.size() > 0) {
                	LOGGER.info("Need to inform next holder");
                    for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
                    	
                    	String nextHolderEmailBody = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailBody() : null;
                        String nextHolderEmailSubject = ( nextHolderTemplate != null) ? nextHolderTemplate.getEmailSubject() : null;
                        String nextHolderSmsText = ( nextHolderTemplate != null ) ? nextHolderTemplate.getSmsText() : null;
                    	
                    	List<CardLessDetail> cardLessDetails = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getCardLessDetails() : new ArrayList<>();
                    	List <String> cardSerialNumbers = new ArrayList<>();
                    	for (CardLessDetail cardLessDetail : cardLessDetails) {
                    		String serialNumber = cardLessDetail.getSerialNumber();
                    			if (serialNumber != null) {
                    				cardSerialNumbers.add(serialNumber);
                    			}
                    	}
                    	
                    	 String handTakeOverName = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getName() : "an officer";
                        if ( LOGGER.isLoggable( Level.INFO ) ) {
                            LOGGER.info(String.format("Next Holder Name: %s , Serial Numbers: %s", handTakeOverName, cardSerialNumbers));
                        }
                    	 nextHolderEmailSubject = this.getTemplateForHandTakeOver(nextHolderEmailSubject, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 nextHolderEmailBody = this.getTemplateForHandTakeOver(nextHolderEmailBody, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 nextHolderSmsText  = this.getTemplateForHandTakeOver(nextHolderSmsText, cardTypeDetail, cardBooking, cardSerialNumbers, handTakeOverName, recipientName, emailSignOff);
                    	 
                    	 this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, nextHolderEmailSubject, nextHolderEmailBody, nextHolderSmsText);
                    	 this.sendCardBookingNotification(nextHolderPreferredContact, senderEmail, senderEmailPassword, nextHolderEmailSubject, nextHolderEmailBody, nextHolderSmsText);
                    }
                 }
                 SessionFactoryUtil.commitTransaction();
    		 }	 
             catch ( Exception e ) {
                 LOGGER.log( Level.WARNING, "sendEmailForApprovedBookings failed.", e );
             }
    	}
    
    public void sendEmailForCancelledBookings (CardBooking cardBooking) {
    	if ( cardBooking == null ) {
            return;
        }

        String cardBookingId = ( cardBooking != null ) ? cardBooking.getId() : null;
        String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
        String nric = ( cardBooking != null ) ? cardBooking.getNric() : null;

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications(). cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
        Template cancelCardTemplate = templateDao.getTemplateByName( CANCEL_CARD_FOR_NEXT_PREV_HOLDER_TEMPLATE_NAME );
        
        try {
            this.populatePreferredContacts( cardBooking, true, true, true );
            this.populateOnBehalfUposPreferredContacts( cardBooking );

            CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? dao.getCardTypeDetail( cardTypeId ) : null;
            CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;

            PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
            String cancelledPersonnelName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : null;
            List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : null;
            List< CardNextHolderPreferredContacts > nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : null;
            
            String emailSubject = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailSubject(): null;
            String emailBody = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailBody() : null;
            String smsText = (cancelCardTemplate != null) ? cancelCardTemplate.getSmsText() : null;
            
            ICalendarService calendarService = CalendarServiceFactory.getInstance();
            DateStartEnd bookingStartEnd = (cardBooking != null) ? cardBooking.getDateStartEnd() : null;
            //Check booking period of cancelled personnel
            //If is not PH and weekend, return or collect from collection point
            boolean isPHOrWeekend = this.checkIfBookingPeriodIsPHOrWeekend(bookingStartEnd, calendarService);
            if (!isPHOrWeekend) {
            	LOGGER.info("Booking period is WEEKDAY. Collection Point is OPEN!!!!");
            	//Return to collection point as collection point is open on weekdays
            	if (previousHolderPreferredContacts != null) {
            		for (CardPreviousHolderPreferredContacts prevHolderPreferredContact : previousHolderPreferredContacts) {
            			String prevHolderPersonnelName = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getName() : null;
            			List<String> serialNumbersToReturnToCollectionPoint = new ArrayList<>();
            			
            			List<CardLessDetail> cardLessDetails = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getCardLessDetails() : null;
            			if (cardLessDetails != null) {
            				for (CardLessDetail cardLessDetail : cardLessDetails) {
            					String cardSerialNumber = cardLessDetail.getSerialNumber();
            					serialNumbersToReturnToCollectionPoint.add(cardSerialNumber);
            				}
            			}
            			//Notify previous holder to return to collection point
                        if (!serialNumbersToReturnToCollectionPoint.isEmpty()) {
                            String returnCollectionPointEmailSubject = emailSubject;
                            String returnCollectionPointEmailBody = emailBody;
                            String returnCollectionPointSmsText = smsText;

                            if (bookingStartEnd != null){
                                returnCollectionPointEmailSubject = this.getTemplateForCancel(emailSubject, cardTypeDetail, bookingStartEnd.getStart(), serialNumbersToReturnToCollectionPoint, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");
                                returnCollectionPointEmailBody = this.getTemplateForCancel(emailBody, cardTypeDetail, bookingStartEnd.getStart(), serialNumbersToReturnToCollectionPoint, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");
                                returnCollectionPointSmsText = this.getTemplateForCancel(smsText, cardTypeDetail, bookingStartEnd.getStart(), serialNumbersToReturnToCollectionPoint, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");
                            }

                            this.sendCancelCardBookingNotification(prevHolderPreferredContact, cardType, returnCollectionPointEmailSubject, returnCollectionPointEmailBody, returnCollectionPointSmsText);
                        }
            		}
            	}
            	
            	if (nextHolderPreferredContacts != null) {
            		for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
            			String nextHolderPersonnelName = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getName() : null;
            			List <String> serialNumbersToCollectFromCollectionPoint = new ArrayList<>();
            			
            			List<CardLessDetail> cardLessDetails = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : null;
            			if (cardLessDetails != null) {
            				for (CardLessDetail cardLessDetail : cardLessDetails) {
            					String cardSerialNumber = cardLessDetail.getSerialNumber();
            					serialNumbersToCollectFromCollectionPoint.add(cardSerialNumber);
            					
            				}
            			}
            			//Notify next holder to collect from collection point
                		if (!serialNumbersToCollectFromCollectionPoint.isEmpty()) {
                            String collectCollectionPointEmailSubject = emailSubject;
                            String collectCollectionPointEmailBody = emailBody;
                            String collectCollectionPointSmsText = smsText;

                            if (bookingStartEnd != null){
                                collectCollectionPointEmailSubject = this.getTemplateForCancel(collectCollectionPointEmailSubject, cardTypeDetail, bookingStartEnd.getEnd(), serialNumbersToCollectFromCollectionPoint, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");
                                collectCollectionPointEmailBody = this.getTemplateForCancel(collectCollectionPointEmailBody, cardTypeDetail, bookingStartEnd.getEnd(), serialNumbersToCollectFromCollectionPoint, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");
                                collectCollectionPointSmsText = this.getTemplateForCancel(collectCollectionPointSmsText, cardTypeDetail, bookingStartEnd.getEnd(), serialNumbersToCollectFromCollectionPoint, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");
                            }

                            this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, collectCollectionPointEmailSubject, collectCollectionPointEmailBody, collectCollectionPointSmsText);
                		}
            		}
            	}
            } else { //weekend or weekday is PH
                if ( previousHolderPreferredContacts != null ) {
                	//For every previous holder, determine who to hand over to
                    for ( CardPreviousHolderPreferredContacts previousHolderPreferredContact : previousHolderPreferredContacts ) {
                        String prevHolderPersonnelName = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getName() : null;
                        Map<String,CardNextHolderPreferredContacts> serialNoNextMap = new HashMap<>();
                        Map<String,Date> serialNumberReturnDate = new HashMap<>();
                        
                        List< CardLessDetail > cardLessDetails = ( previousHolderPreferredContact != null ) ? previousHolderPreferredContact.getCardLessDetails() : null;
                        if ( cardLessDetails != null ) {
                            for ( CardLessDetail cardLessDetail : cardLessDetails ) {
                                String cardSerialNumber = cardLessDetail.getSerialNumber();
                                if (nextHolderPreferredContacts != null) {
                                	for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
                                		List<CardLessDetail> nextCardLessDetails = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : new ArrayList<>();
                                		for (CardLessDetail nextCardLessDetail : nextCardLessDetails) {
                                			String nextCardSerialNumber = nextCardLessDetail.getSerialNumber();
                                			if (cardSerialNumber.equals(nextCardSerialNumber)) {
                                				LOGGER.info(String.format("Serial No.: %s is with next holder %s",cardSerialNumber,nextHolderPreferredContact.getName()));
                                				serialNoNextMap.put(cardSerialNumber, nextHolderPreferredContact);
                                				//Dont have to check anymore since already found serial number
                                				break;
                                			}
                                		}
                                	}
                                }
                                //Every serial number not in hashmap means return to collection point
                                if (!serialNoNextMap.keySet().contains(cardSerialNumber)) {
                                	serialNumberReturnDate.put(cardSerialNumber, cardLessDetail.getBookingEndDate());
                                }
                                if ( LOGGER.isLoggable( Level.INFO ) ) {
                                    LOGGER.info(String.format("Serial Numbers to Collection Point for %s : %s", prevHolderPersonnelName, serialNumberReturnDate.toString()));
                                }
                            }
                        }
          
                        LOGGER.info("Serial No. Next Holder Map: " + serialNoNextMap.toString());
                        //Notify previous holder to return to collection point
                        if ((serialNumberReturnDate != null) && (!serialNumberReturnDate.isEmpty())) {
                            for (Entry<String, Date> entry : serialNumberReturnDate.entrySet()) {
                                String returnCollectionPointEmailSubject;
                                String returnCollectionPointEmailBody;
                                String returnCollectionPointSmsText;
                                List<String> serialNumberList = Arrays.asList(entry.getKey());

                                returnCollectionPointEmailSubject = this.getTemplateForCancel(emailSubject, cardTypeDetail, entry.getValue(), serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");
                                returnCollectionPointEmailBody = this.getTemplateForCancel(emailBody, cardTypeDetail, entry.getValue(), serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");
                                returnCollectionPointSmsText = this.getTemplateForCancel(smsText, cardTypeDetail, entry.getValue(), serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, "Collection Point", "return", "to");

                                this.sendCancelCardBookingNotification(previousHolderPreferredContact, cardType, returnCollectionPointEmailSubject, returnCollectionPointEmailBody, returnCollectionPointSmsText);
                            }
                        }
                        
                        for (Entry<String, CardNextHolderPreferredContacts> entry: serialNoNextMap.entrySet()) {
                        	String handOverEmailSubject = emailSubject;
                        	String handOverEmailBody = emailBody;
                        	String handOverSmsText = smsText;
                        	
                        	List<String> serialNumberList = Arrays.asList(entry.getKey());
                        	
                        	Date bookingStartDate = null;
                        	
                        	List<CardLessDetail> nextCardLessDetails = (entry.getValue() != null) ? entry.getValue().getCardLessDetails() : null;
                        	if (nextCardLessDetails != null) {
                        		for (CardLessDetail cardLessDetail : nextCardLessDetails) {
                        			if (entry.getKey().equals(cardLessDetail.getSerialNumber())) {
                        				bookingStartDate = cardLessDetail.getBookingStartDate();
                        				break;
                        			}
                        		}
                        	}
                        	
                        	handOverEmailSubject = this.getTemplateForCancel(handOverEmailSubject, cardTypeDetail, bookingStartDate, serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "hand over", "to");
                        	handOverEmailBody = this.getTemplateForCancel(handOverEmailBody, cardTypeDetail, bookingStartDate, serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "hand over", "to");
                        	handOverSmsText = this.getTemplateForCancel(handOverSmsText, cardTypeDetail, bookingStartDate, serialNumberList, prevHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "hand over", "to");
                        	
                        	this.sendCancelCardBookingNotification(previousHolderPreferredContact, cardType, handOverEmailSubject, handOverEmailBody, handOverSmsText);
                        }
                    }
                }
                
                if (nextHolderPreferredContacts != null) {
                	//For every next holder determine who to take over from
                	for (CardNextHolderPreferredContacts nextHolderPreferredContact: nextHolderPreferredContacts) {
                		String nextHolderPersonnelName = ( nextHolderPreferredContact != null ) ? nextHolderPreferredContact.getName() : null;
                		Map<String, CardPreviousHolderPreferredContacts> serialNoPrevMap = new HashMap<>();
                		List<String> serialNumbersToCollectFromCollectionPoint = new ArrayList<>();
                		Map<String, Date> serialNumberCollectDate = new HashMap<>();
                		
                		List<CardLessDetail> cardLessDetails = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : null;
                		if (cardLessDetails != null) {
                			for (CardLessDetail cardLessDetail : cardLessDetails) {
                				String cardSerialNumber = cardLessDetail.getSerialNumber();
                				if (previousHolderPreferredContacts != null) {
                					for (CardPreviousHolderPreferredContacts prevHolderPreferredContact : previousHolderPreferredContacts) {
                						List<CardLessDetail> prevCardLessDetails = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getCardLessDetails() : new ArrayList<>();
                						for (CardLessDetail prevCardLessDetail : prevCardLessDetails) {
                							String prevSerialNumber = prevCardLessDetail.getSerialNumber();
                							if (cardSerialNumber.equals(prevSerialNumber)) {
                								serialNoPrevMap.put(cardSerialNumber, prevHolderPreferredContact);
                								LOGGER.info(String.format("Serial No.: %s is with prev holder %s",cardSerialNumber,prevHolderPreferredContact.getName()));
                								//Dont have to check anymore since already found serial number
                								break;
                							}
                						}
                					}
                				}
                				//Every serial number not in hashmap means collect from collection point
                				if (!serialNoPrevMap.keySet().contains(cardSerialNumber)) {
                					//Collection Date is booking start date of next holder booking
                					serialNumberCollectDate.put(cardSerialNumber, cardLessDetail.getBookingStartDate());
                				}
                                if ( LOGGER.isLoggable( Level.INFO ) ) {
                                    LOGGER.info(String.format("Serial Numbers to Collection Point: %s", serialNumbersToCollectFromCollectionPoint.toString()));
                                    LOGGER.info(String.format("Serial Numbers to Collection Point for %s : %s", nextHolderPersonnelName, serialNumberCollectDate.toString()));
                                }
                			}
                		}
                		LOGGER.info("Serial No. Next Holder Map: " + serialNoPrevMap);
                		//Notify next holder to collect from collection point
                		
                		if (serialNumberCollectDate != null && !serialNumberCollectDate.isEmpty()) {
                            for (Entry<String, Date> entry : serialNumberCollectDate.entrySet()) {
                                String collectCollectionPointEmailSubject = emailSubject;
                                String collectCollectionPointEmailBody = emailBody;
                                String collectCollectionPointSmsText = smsText;

                                List<String> serialNumberList = Arrays.asList(entry.getKey());

                                collectCollectionPointEmailSubject = this.getTemplateForCancel(collectCollectionPointEmailSubject, cardTypeDetail, entry.getValue(), serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");
                                collectCollectionPointEmailBody = this.getTemplateForCancel(collectCollectionPointEmailBody, cardTypeDetail, entry.getValue(), serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");
                                collectCollectionPointSmsText = this.getTemplateForCancel(collectCollectionPointSmsText, cardTypeDetail, entry.getValue(), serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, "Collection Point", "collect", "from");

                                this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, collectCollectionPointEmailSubject, collectCollectionPointEmailBody, collectCollectionPointSmsText);
                            }
                		}
                		
                		for (Entry<String, CardPreviousHolderPreferredContacts> entry : serialNoPrevMap.entrySet()) {
                			String takeOverEmailSubject = emailSubject;
                			String takeOverEmailBody = emailBody;
                			String takeOverSmsText = smsText;
                			
                			List<String> serialNumberList = Arrays.asList(entry.getKey());
                		
                			Date bookingStartDate = null;
                			
                			List<CardLessDetail> cardLessDetailList = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : null;
                			if (cardLessDetailList != null) {
                				for (CardLessDetail cardLessDetail : cardLessDetailList) {
                					if (entry.getKey().equals(cardLessDetail.getSerialNumber())) {
                						//Take over date is booking start date of next holder booking
                						bookingStartDate = cardLessDetail.getBookingStartDate();
                						break;
                					}
                				}
                			}
                				
                			takeOverEmailSubject = this.getTemplateForCancel(takeOverEmailSubject, cardTypeDetail, bookingStartDate, serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "take over", "from");
                			takeOverEmailBody = this.getTemplateForCancel(takeOverEmailBody, cardTypeDetail, bookingStartDate, serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "take over", "from");
                			takeOverSmsText = this.getTemplateForCancel(takeOverSmsText, cardTypeDetail, bookingStartDate, serialNumberList, nextHolderPersonnelName, cancelledPersonnelName, entry.getValue().getName(), "take over", "from");
                			
                			this.sendCancelCardBookingNotification(nextHolderPreferredContact, cardType, takeOverEmailSubject, takeOverEmailBody, takeOverSmsText);
                		}
                	}
                }
            }
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "sendCancelCardBookingNotifications() failed.", e );
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications() ended. cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
    }
    
    public String getTemplateForCancel (String templateString, CardTypeDetail cardTypeDetail, Date bookingEndDate, 
    		List<String> cardSerialNumberList, String recipientName, String cancelledName, String handTakeOverName
    		,String collectReturnTakeOver, String fromTo) {
    	
    	String resultString = templateString;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	
    	String visitEndDate = ConvertUtil.convertDateToDateString(bookingEndDate);
    	
    	StringBuilder cardSerialNumbers = new StringBuilder();
    	if (cardSerialNumberList != null) {
    		for (String cardSerialNumber: cardSerialNumberList) {
    			if (cardSerialNumbers.length() > 0) {
    				cardSerialNumbers.append(", ");
    			}
    			cardSerialNumbers.append(cardSerialNumber);
    		}
    	}
    	
    	resultString = resultString.replace("\\$Officer\\$", recipientName);
    	resultString = resultString.replace("\\$passName\\$", cardName);
    	resultString = resultString.replace("\\$passSerialNo\\$", cardSerialNumbers.toString());
    	resultString = resultString.replace("\\$visitEndDate\\$", visitEndDate);
    	resultString = resultString.replace("\\$collectReturnHandTakeOver\\$", collectReturnTakeOver);
    	resultString = resultString.replace("\\$fromTo\\$", fromTo);
    	resultString = resultString.replace("\\$OfficerA\\$", cancelledName);
      	if (handTakeOverName != null) {
      		resultString = resultString.replace("\\$previousNext\\$", handTakeOverName);
      	}
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("Result String: %s", resultString));
        }
    	return resultString;
    }
    
    public boolean checkIfBookingPeriodIsPHOrWeekend(DateStartEnd bookingPeriod, ICalendarService calendarService) {
    	
    	Date bookingStart = (bookingPeriod != null) ? bookingPeriod.getStart() : null;
    	Date bookingEnd = (bookingPeriod != null) ? bookingPeriod.getEnd() : null;
    	
    	Calendar bookingStartCalendar = Calendar.getInstance();
    	bookingStartCalendar.setTime(bookingStart);
    	
    	Calendar bookingEndCalendar = Calendar.getInstance();
    	bookingEndCalendar.setTime(bookingEnd);
    	bookingEndCalendar.add(Calendar.DATE, 1);
    	
    	boolean isPHOrWeekend = false;
    	
    	while (bookingStartCalendar.before(bookingEndCalendar)) {
    		Date bookingStartDate = bookingStartCalendar.getTime();
    		//To include end date itself
    		bookingStartCalendar.add(Calendar.DATE, 1);
    		boolean isWeekend = false;
    		
    		int dayOfWeek = bookingStartCalendar.get(Calendar.DAY_OF_WEEK);
    		
    		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
    			isWeekend = true;
    		}
    		
    		boolean isPublicHoliday = isPublicHoliday(calendarService, bookingStartDate);
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("Is Weekend: %s, Is Public Holiday: %s", isWeekend, isPublicHoliday));
            }
    		if(isPublicHoliday || isWeekend) {
    			isPHOrWeekend = true;
    			return isPHOrWeekend;
    		}
    	}
    	
    	return isPHOrWeekend;
    }
    public void sendEmailForCancelledBooking (CardBooking cardBooking) {
    	if ( cardBooking == null ) {
            return;
        }

        String cardBookingId = ( cardBooking != null ) ? cardBooking.getId() : null;
        String cardTypeId = ( cardBooking != null ) ? cardBooking.getCardTypeId() : null;
        String nric = ( cardBooking != null ) ? cardBooking.getNric() : null;

        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications(). cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
        Template cancelCardTemplate = templateDao.getTemplateByName( CANCEL_CARD_FOR_NEXT_PREV_HOLDER_TEMPLATE_NAME );
        
        try {
            this.populatePreferredContacts( cardBooking, true, true, true );
            this.populateOnBehalfUposPreferredContacts( cardBooking );

            CardTypeDetail cardTypeDetail = ( cardTypeId != null ) ? dao.getCardTypeDetail( cardTypeId ) : null;
            CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
            
        	GlobalParameters globalParameters = null;
            String senderEmail;
            String senderEmailPassword;
            String emailSignOff;
            
            if (cardType == CardType.PCWF) {
           	 globalParameters = globalParametersDao.getGlobalParametersByUnit(PCWF_CARD, null);
            } else {
           	 globalParameters = globalParametersDao.getGlobalParametersByUnit(cardTypeDetail.getDepartment(), null);
            }
            
            if (globalParameters != null) {
         	   senderEmail = globalParameters.getSenderEmail();
                senderEmailPassword = globalParameters.getSenderEmailPassword();
                emailSignOff = globalParameters.getEmailSignOff();
            } else {
         	 LOGGER.info("globalParameter is null, using default setting from IMailSenderConfig ..");
         	 if (cardType == CardType.PCWF) {
         		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( PCWF_CARD_MODULE_NAME );
             	 senderEmail = config.senderAddress();
             	 senderEmailPassword = config.senderPassword();
             	 emailSignOff = POLICE_WELFARE_DIVISION;
         	 } else {
         		 IMailSenderConfig config = ServiceConfig.getInstance().getMailSenderConfig( GROUP_CARD_MODULE_NAME );
             	 senderEmail = config.senderAddress();
             	 senderEmailPassword = config.senderPassword();
             	 emailSignOff = null;
         	 }
          }

            PersonalPreferredContacts personalPreferredContact = ( cardBooking != null ) ? cardBooking.getPersonalPreferredContacts() : null;
            String cancelledPersonnelName = ( personalPreferredContact != null ) ? personalPreferredContact.getName() : null;
            List< CardPreviousHolderPreferredContacts > previousHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getPreviousHolderPreferredContacts() : null;
            List< CardNextHolderPreferredContacts > nextHolderPreferredContacts = ( cardBooking != null ) ? cardBooking.getNextHolderPreferredContacts() : null;
            
            String emailSubject = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailSubject(): null;
            String emailBody = (cancelCardTemplate != null) ? cancelCardTemplate.getEmailBody() : null;
            String smsText = (cancelCardTemplate != null) ? cancelCardTemplate.getSmsText() : null;
            
            if (previousHolderPreferredContacts != null) {
            	for (CardPreviousHolderPreferredContacts prevHolderPreferredContact : previousHolderPreferredContacts) {
            		String prevHolderPersonnelName = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getName() : "-";
            		List<CardLessDetail> cardLessDetails = (prevHolderPreferredContact != null) ? prevHolderPreferredContact.getCardLessDetails() : null;
            		if (cardLessDetails != null) {
            			for (CardLessDetail cardLessDetail : cardLessDetails) {
            				//For every serial number for previous holder determine who to hand over to
            				String cardSerialNumber = cardLessDetail.getSerialNumber();
            				Date bookingEndDate = cardLessDetail.getBookingEndDate();
            				String handOverEmailSubject = emailSubject;
                        	String handOverEmailBody = emailBody;
                        	String handOverSmsText = smsText;
            				String[] handOverDetails = this.getHandOverToStringForCancel(cardTypeDetail, cardSerialNumber, bookingEndDate);
                            if ( LOGGER.isLoggable( Level.INFO ) ) {
                                LOGGER.info(String.format("Serial No: %s, Hand Over Details: %s", cardSerialNumber, Arrays.toString(handOverDetails)));
                            }
            				handOverEmailSubject = this.getTemplateForCancel(handOverEmailSubject, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);
                        	handOverEmailBody = this.getTemplateForCancel(handOverEmailBody, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);
                        	handOverSmsText = this.getTemplateForCancel(handOverSmsText, cardTypeDetail, handOverDetails[1], cardSerialNumber, prevHolderPersonnelName, cancelledPersonnelName, handOverDetails[0], emailSignOff);

                        	this.sendCardBookingNotification(prevHolderPreferredContact, senderEmail, senderEmailPassword, handOverEmailSubject, handOverEmailBody, handOverSmsText);
            			}
            		}
            	}
            }
            
            if (nextHolderPreferredContacts != null) {
            	for (CardNextHolderPreferredContacts nextHolderPreferredContact : nextHolderPreferredContacts) {
            		String nextHolderPersonnelName = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getName() : "-";
            		List<CardLessDetail> cardLessDetails = (nextHolderPreferredContact != null) ? nextHolderPreferredContact.getCardLessDetails() : null;
            		if (cardLessDetails != null) {
            			for (CardLessDetail cardLessDetail : cardLessDetails) {
            				//For every serial number for next holder determine who to take over from
            				String cardSerialNumber = cardLessDetail.getSerialNumber();
            				Date bookingStartDate = cardLessDetail.getBookingStartDate();
            				String takeOverEmailSubject = emailSubject;
            				String takeOverEmailBody = emailBody;
            				String takeOverSmsText = smsText;
            				
            				String[] takeOverDetails = this.getTakeOverFromStringForCancel(cardTypeDetail, cardSerialNumber, bookingStartDate);
                            if ( LOGGER.isLoggable( Level.INFO ) ) {
                                LOGGER.info(String.format("Serial No: %s, Take Over Details: %s", cardSerialNumber, Arrays.toString(takeOverDetails)));
                            }
            				takeOverEmailSubject = this.getTemplateForCancel(takeOverEmailSubject, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);
                        	takeOverEmailBody = this.getTemplateForCancel(takeOverEmailBody, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);
                        	takeOverSmsText = this.getTemplateForCancel(takeOverSmsText, cardTypeDetail, takeOverDetails[1], cardSerialNumber, nextHolderPersonnelName, cancelledPersonnelName, takeOverDetails[0], emailSignOff);

                        	this.sendCardBookingNotification(nextHolderPreferredContact, senderEmail, senderEmailPassword, takeOverEmailSubject, takeOverEmailBody, takeOverSmsText);
            			}
            		}
            	}
            }
        }
        catch ( Exception e ) {
            LOGGER.log( Level.WARNING, "sendCancelCardBookingNotifications() failed.", e );
        }
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCancelCardBookingNotifications() ended. cardBookingId=%s, cardTypeId=%s, nric=%s", cardBookingId, cardTypeId, nric));
        }
    }
    
    private String[] getHandOverToStringForCancel( CardTypeDetail cardTypeDetail, String serialNumber, Date visitEnd ) {
        String handOverTo = "-";
        String handOverToDates = "-";
        
        Calendar visitEndDate = Calendar.getInstance();
        visitEndDate.setTime(visitEnd);
        visitEndDate.get(Calendar.DAY_OF_WEEK);

        Date dayAfterVisitEnd = visitEnd;
        CardBooking cardBooking = null;
        
        boolean isPublicHoliday = true;
        ICalendarService nextCalendarService = CalendarServiceFactory.getInstance();
     
    	while(isPublicHoliday && cardBooking == null)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
            	cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        		
        	}
    		else
    		{
    			LOGGER.info("next date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    	}
    	Calendar dayAfterVisitDate = Calendar.getInstance();
    	dayAfterVisitDate.setTime(dayAfterVisitEnd);
    	int dayAfterVisitDay = dayAfterVisitDate.get(Calendar.DAY_OF_WEEK);
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SATURDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	if(cardBooking == null)
        	{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
        		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayAfterVisitDay == Calendar.SUNDAY)
    	{
    		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd, 1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );

    	}
    	isPublicHoliday = true;
    	dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,0);
        while(isPublicHoliday && cardBooking == null)
    	{
    		
    		isPublicHoliday = isPublicHoliday(nextCalendarService, dayAfterVisitEnd);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("next date is a public holiday, to check if handover/takeover is possible for the day after");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber ); 
        		if(cardBooking == null)
        		{
        		dayAfterVisitEnd = DateTimeUtil.getDateDaysAfter( dayAfterVisitEnd,1);
        		}
        	}
    		else
    		{
    			LOGGER.info("after date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayAfterVisitEnd, serialNumber );
    		}
    		
    	}
        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            PersonalDetail personalDetail = null;
            if ( nric != null ) {
                personalDetail = dao.getPersonal( nric );
            }
            if ( personalDetail != null ) {
                String name = personalDetail.getName();
                handOverTo = "hand over the pass(es) to " + name;
                handOverToDates = ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
            }
            else {
                LOGGER.info( String.format( "handOverTo, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
            }
        }
        else if ( cardTypeDetail != null && dayAfterVisitEnd != null ) {
            CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayAfterVisitEnd );
            if ( cardCollectionDetail != null ) {
                handOverTo = "return the pass(es) to " + cardCollectionDetail.getLocation();
                handOverToDates = ConvertUtil.convertDateToDateString( dayAfterVisitEnd );
            }
            else {
                LOGGER.info( String.format( "handOverTo, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
            }
        }

        return new String[] { handOverTo, handOverToDates };
    }
    
    private String[] getTakeOverFromStringForCancel( CardTypeDetail cardTypeDetail, String serialNumber, Date visitStart ) {
        String takeOverFrom = "-";
        String takeOverFromDates = "-";
        
        Calendar visitStartDate = Calendar.getInstance();
        visitStartDate.setTime(visitStart);
        visitStartDate.get(Calendar.DAY_OF_WEEK);
        boolean isPublicHoliday = true;
        ICalendarService previousCalendarService = CalendarServiceFactory.getInstance();
        CardBooking cardBooking = null;
        Date dayBeforeVisitStart = visitStart;
        while(isPublicHoliday && cardBooking == null)
    	{
        	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		
        	}
    		else
    		{
    			LOGGER.info("previous date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    	}
        Calendar dayBeforeVisitDate = Calendar.getInstance();
    	dayBeforeVisitDate.setTime(dayBeforeVisitStart);
    	int dayBeforeVisitDay = dayBeforeVisitDate.get(Calendar.DAY_OF_WEEK);
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SUNDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	if(cardBooking == null)
        	{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        	}	
    	}
    	if(cardBooking == null && dayBeforeVisitDay == Calendar.SATURDAY)
    	{
    		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart, -1 );
    		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    	}
    	isPublicHoliday = true;
    	dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,0);
        while(isPublicHoliday && cardBooking == null)
    	{
    		
    		isPublicHoliday = isPublicHoliday(previousCalendarService, dayBeforeVisitStart);
    		if(isPublicHoliday)
        	{
        		LOGGER.info("previous date is a public holiday, to check if handover/takeover is possible for the day before");
        		
        		cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
        		if(cardBooking == null)
        		{
        		dayBeforeVisitStart = DateTimeUtil.getDateDaysAfter( dayBeforeVisitStart,-1);
        		}
        	}
    		else
    		{
    			LOGGER.info("previous date is not a public holiday");
    			cardBooking = dao.getApprovedCardBooking( null, dayBeforeVisitStart, serialNumber );
    		}
    	}

        if ( cardBooking != null ) {
            String nric = cardBooking.getNric();
            PersonalDetail personalDetail = null;
            if ( nric != null ) {
                personalDetail = dao.getPersonal( nric );
            }
            if ( personalDetail != null ) {
                String name = personalDetail.getName();
                takeOverFrom = "take over the pass(es) from " + name;
                takeOverFromDates = ConvertUtil.convertDateToDateString( visitStart );
                
            }
            else {
                LOGGER.info( String.format( "takeOverFrom, nric=%s personal details not found.", Util.replaceNewLine( nric ) ) );
            }
        }
        else if ( cardTypeDetail != null && dayBeforeVisitStart != null ) {
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("to add to collection detail, dayBeforeVisitStart is: %s", dayBeforeVisitStart));
            }
            CardCollectionDetail cardCollectionDetail = this.getCardCollectionDetail( cardTypeDetail, dayBeforeVisitStart );
            if ( cardCollectionDetail != null ) {
                takeOverFrom = "collect the pass(es) from " + cardCollectionDetail.getLocation();
                takeOverFromDates = ConvertUtil.convertDateToDateString( visitStart );
            }
            else {
                LOGGER.info( String.format( "takeOverFrom, card collection details not found. cardTypeId=%s", Util.replaceNewLine( cardTypeDetail.getId() ) ) );
            }
        }

        return new String[] { takeOverFrom, takeOverFromDates };
    }
    
    public String getTemplateForCancel (String templateString, CardTypeDetail cardTypeDetail, String bookingEndDate, 
    		String cardSerialNumber, String recipientName, String cancelledName, String handTakeOverString, String signOff) {
    	
    	String resultString = templateString;
    	String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
    	
    	resultString = resultString.replace("\\$Officer\\$", recipientName);
    	resultString = resultString.replace("\\$passName\\$", cardName);
    	resultString = resultString.replace("\\$passSerialNo\\$", cardSerialNumber);
    	resultString = resultString.replace("\\$visitEndDate\\$", bookingEndDate);
    	resultString = resultString.replace("\\$collectReturnHandTakeOver\\$", handTakeOverString);
    	resultString = resultString.replace("\\$OfficerA\\$", cancelledName);
    	
      	if (signOff != null) {
      		if (signOff.length() > 0) {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards,");
          		resultString = resultString.replace("\\$signOff\\$", this.formatSignOff(signOff));
      		} else {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      			resultString = resultString.replace("\\$signOff\\$", "");
      		}
      	} else {
      		resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      		resultString = resultString.replace("\\$signOff\\$", "");
      	}
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("Result String: %s", resultString));
        }
    	return resultString;
    }

	@Override
	public List<CardTypeDetail> getAllCreatedEligibleCardTypeDetails (boolean isPcwfMember, String department)throws CorporateCardException {
		List< CardTypeDetail > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getAllCreatedPcwfGroupCardTypeDetails(isPcwfMember, department, department);

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            LOGGER.log( Level.WARNING, "getAllCreatedEligibleCardTypeDetails() failed.", e );
            SessionFactoryUtil.rollbackTransaction();
        }

        return results;
	}
	
	private void sendCardBookingNotification( PersonalPreferredContacts contact, String senderAddress, String senderPassword, String emailSubject, String emailBody, String smsText ) {
        boolean success = false;
        String response = null;

        if ( contact == null ) {
            return;
        }
        
        if ( senderAddress == null ) {
        	return;
        }

        String nric = contact.getNric();
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("sendCardBookingNotification() started. nric=%s", nric));
        }
        if (nric != null) {
        	if (smsText != null) {
        		String preferredMobile = contact.getPreferredMobile();
        		if (preferredMobile != null) {
        			ArrayList<String> smsRecipients = new ArrayList<>();
        			smsRecipients.add(preferredMobile);
        			success = this.sendSMS(smsRecipients, smsText);
        			response = "sms.";
        		} else {
        			response = "preferredMobile is null.";
        		}
        	} else {
        		response = "smsText is null.";
        	}
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("Send SMS ended. nric=%s, success=%s, response=%s", nric, success, response));
            }
        	if (emailSubject != null) {
        		if (emailBody != null) {
        			String preferredEmail = contact.getPreferredEmail();
        			if (preferredEmail != null) {
        				ArrayList<String> emailRecipients = new ArrayList<>();
        				emailRecipients.add(preferredEmail);
        				success = this.sendEmail(senderAddress, senderPassword, emailRecipients, emailSubject, emailBody);
        			} else {
        				response = "preferredEmail is null.";
        			}
        		} else {
        			response = "emailBody is null.";
        		}
        	} else {
        		response = "emailSubject is null.";
        	}
            if ( LOGGER.isLoggable( Level.INFO ) ) {
                LOGGER.info(String.format("Send Email ended. nric=%s, success=%s, response=%s", nric, success, response));
            }
        } 
        LOGGER.info( "sendCardBookingNotification() ended." );
    }
	
	private boolean sendEmail( String senderAddress, String senderPassword, List< String > recipients, String subject, String body ) {
		
        BatchElectronicMail mail = new BatchElectronicMail();
        mail.setHtmlContent( true );
        mail.setSubject( subject );
        mail.setUserAddress( senderAddress );
        mail.setToRecipients( recipients );
        mail.setText( body );
        mail.setUserPassword( senderPassword );

        INotificationService notificationService = NotificationServiceFactory.getInstance();
        try {
            notificationService.send( mail );
            return true;
        }
        catch ( NotificationServiceException e ) {
            LOGGER.log( Level.SEVERE, "Fail to send email to " + recipients, e );
            return false;
        }
    }
	
	private String formatSignOff (String signOff) {
		String[] lines = signOff.split("\\r?\\n");
		
		StringBuilder builder = new StringBuilder (lines[0]);
		for (int i = 1; i < lines.length; i++) {
			builder.append("<BR>");
			builder.append(lines[i]);
		}
		
		return builder.toString();
	}
	
	private String getTemplateForBroadcast (String templateString, String signOff, CardTypeDetail cardTypeDetail) {
		String resultString = templateString;
		
		String cardName = ( cardTypeDetail != null ) ? cardTypeDetail.getName() : "-";
		CardType cardType = ( cardTypeDetail != null ) ? cardTypeDetail.getType() : null;
		
		resultString = resultString.replace("\\$cardName\\$", cardName);
		
		if (cardType == CardType.PCWF) {
			resultString = resultString.replace("\\$Officer\\$", "PCWF members");
			
		} else {
			resultString = resultString.replace("\\$Officer\\$", "Officer");
		}
		if (signOff != null) {
      		if (signOff.length() > 0) {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards,");
          		resultString = resultString.replace("\\$signOff\\$", this.formatSignOff(signOff));
      		} else {
      			resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      			resultString = resultString.replace("\\$signOff\\$", "");
      		}
      	} else {
      		resultString = resultString.replace("\\$BestRegards\\$", "Best Regards.");
      		resultString = resultString.replace("\\$signOff\\$", "");
      	}
        if ( LOGGER.isLoggable( Level.INFO ) ) {
            LOGGER.info(String.format("Result String: %s", resultString));
        }
		return resultString;
	}
	
}
