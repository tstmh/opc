package com.stee.spfcore.webapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.webapi.dao.ReferenceGeneratorDAO;
import com.stee.spfcore.webapi.model.ReferenceGenerator;
import com.stee.spfcore.webapi.model.internal.ApplicationType;

public class ReferenceNumberGenerator {

    private static final Logger logger = Logger.getLogger( ReferenceNumberGenerator.class.getName() );
    private static ReferenceNumberGenerator instance = null;

    private ReferenceGeneratorDAO dao;

    public synchronized static ReferenceNumberGenerator getInstance() {
        if ( instance == null ) {
            instance = new ReferenceNumberGenerator();
        }

        return instance;
    }

	@SuppressWarnings( "deprecation" )
    public synchronized String getUniqueId( ApplicationType applicationType ) throws Exception {

        String prefix = null;
        
        /*ModuleDelimiter: 
         * 	a. default = "-".
         *  b. for SAA, SA and SG = "".
         * */ 
        String moduleDelimiter = "-";

        if ( applicationType == ApplicationType.BEREAVEMENT_GRANT ) {
            prefix = "BG";
        }
        else if ( ( applicationType == ApplicationType.NEWBORN_GIFT ) ) {

            if ( EnvironmentUtils.isInternet() ) {
                prefix = "NB"; // Portal
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "NB"; // BPM
            }
        }
        else if ( ( applicationType == ApplicationType.WEDDING_GIFT ) ) {

            if ( EnvironmentUtils.isInternet() ) {
                prefix = "WG";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "WG";
            }
        }
        else if ( applicationType == ApplicationType.WEIGHT_MGMT_SUBSIDY ) {

            if ( EnvironmentUtils.isInternet() ) {
                prefix = "WM";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "WM";
            }
        }
        else if ( applicationType == ApplicationType.COURSE_REGISTRATION ) {

            if ( EnvironmentUtils.isInternet() ) {
                prefix = "CR";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "CR";
            }
        }
        else if ( applicationType == ApplicationType.SCHOLASTIC_ACHIEVEMENT_AWARD ) {
            // SAG - SCHOLASTIC Achievement Award
        	moduleDelimiter = "";
            if ( EnvironmentUtils.isInternet() ) {
                prefix = "SAA";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "SAA";
            }
        }
        else if ( applicationType == ApplicationType.STUDY_AWARD ) {
            //SAG - STUDY AWARD
        	moduleDelimiter = "";
            if ( EnvironmentUtils.isInternet() ) {
                prefix = "SA";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "SA";
            }
        }
        else if ( applicationType == ApplicationType.STUDY_GRANT ) {
            // SAG - STUDY GRANT
        	moduleDelimiter = "";
            if ( EnvironmentUtils.isInternet() ) {
                prefix = "SG";
            }
            else if ( EnvironmentUtils.isIntranet() ) {
                prefix = "SG";
            }
        }
        else {
            prefix = "";
        }

        String uniqueId = null;

        try {
            ReferenceGenerator referenceGenerator = dao.getReferenceGenerator( applicationType );

            if ( referenceGenerator == null ) {
                referenceGenerator = new ReferenceGenerator( applicationType, 0, new Date() );
                dao.addReferenceGenerator( referenceGenerator );
            }

            int referenceNumber = referenceGenerator.getReferenceNumber();
            Date lastUpdate = referenceGenerator.getLastUpdate();
            Date now = new Date();

            if ( now.getYear() > lastUpdate.getYear() ) {
                referenceNumber = 1;
            }
            else {
                referenceNumber = referenceNumber + 1;
            }

            SimpleDateFormat date = new SimpleDateFormat( "yyyy" );

            String format = null;
            if ( EnvironmentUtils.isInternet() ) {
                format = "%s"+moduleDelimiter+"%sM%04d";
            }
            else {
                format = "%s"+moduleDelimiter+"%sA%04d";
            }

            uniqueId = String.format( format, prefix, date.format( now ), referenceNumber );

            referenceGenerator.setReferenceNumber( referenceNumber );
            referenceGenerator.setLastUpdate( now );

            dao.updateReferenceGenerator( referenceGenerator );
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to generate reference number:" + exception );
            throw new Exception( "Fail to generate reference number:" + exception );
        }

        return uniqueId;
    }
}