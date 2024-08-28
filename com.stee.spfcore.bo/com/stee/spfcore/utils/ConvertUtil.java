package com.stee.spfcore.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConvertUtil {

    private ConvertUtil(){}
    private static final Logger logger = Logger.getLogger( ConvertUtil.class.getName() );

    public static synchronized Date convertFebDateControlStringToDate( String dateString ) {
        SimpleDateFormat FEB_DATE_CONTROL_INPUT_DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
        Date date = null;
        try {
            if ( dateString != null ) {
                date = FEB_DATE_CONTROL_INPUT_DATE_FORMAT.parse( dateString );
            }
        }
        catch ( ParseException e ) {
            logger.log( Level.SEVERE, "Fail to parse dateString:" + Util.replaceNewLine( dateString ), e );
        }
        return date;
    }

    public static synchronized String convertFebDateControlDateToString( Date date ) {
        SimpleDateFormat FEB_DATE_CONTROL_OUTPUT_DATE_FORMAT = new SimpleDateFormat( "M/d/yyyy" );
        String dateString = null;
        if ( date != null ) {
            dateString = FEB_DATE_CONTROL_OUTPUT_DATE_FORMAT.format( date );
        }
        return dateString;
    }

    public static synchronized Date convertDateStringToDate( String dateString ) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "dd/MM/yyyy" );
        Date date = null;
        try {
            if ( dateString != null ) {
                date = DATE_FORMAT.parse( dateString );
            }
        }
        catch ( ParseException e ) {
            logger.log( Level.SEVERE, "Fail to parse dateString:" + Util.replaceNewLine( dateString ), e );
        }
        return date;
    }

    public static synchronized String convertDateToDateString( Date date ) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "dd/MM/yyyy" );
        String dateString = "";
        if ( date != null ) {
            dateString = DATE_FORMAT.format( date );
        }

        return dateString;
    }

    public static synchronized String convertDateToDateStringWithDay( Date date ) {
        SimpleDateFormat DATE_FORMAT_W_DAY = new SimpleDateFormat( "EEEE, dd/MM/yyyy" );
        String dateString = "";
        if ( date != null ) {
            dateString = DATE_FORMAT_W_DAY.format( date );
        }

        return dateString;
    }

    public static synchronized Date convertTimeStringToTime( String timeString ) {
        SimpleDateFormat TIME_HR_MIN_FORMAT = new SimpleDateFormat( "hh:mm a" );
        Date time = null;
        try {
            if ( timeString != null ) {
                time = TIME_HR_MIN_FORMAT.parse( timeString );
            }
        }
        catch ( ParseException e ) {
            logger.log( Level.SEVERE, "Fail to parse timeString:" + Util.replaceNewLine( timeString ), e );
        }
        return time;
    }

    public static synchronized String convertTimeToTimeString( Date time ) {
        SimpleDateFormat TIME_HR_MIN_FORMAT = new SimpleDateFormat( "hh:mm a" );
        String timeString = null;
        if ( time != null ) {
            timeString = TIME_HR_MIN_FORMAT.format( time );
        }

        return timeString;
    }

    public static synchronized String convertTimeToTimeString24Hrs( Date time ) {
        SimpleDateFormat TIME_24HR_MIN_FORMAT = new SimpleDateFormat( "HH:mm" );
        String timeString = null;
        if ( time != null ) {
            timeString = TIME_24HR_MIN_FORMAT.format( time );
        }

        return timeString;
    }

    public static int convertIntStringToInt( String integerString ) {
        int integer = 0;
        try {
            if ( integerString != null ) {
                integer = Integer.parseInt( integerString );
            }
        }
        catch ( NumberFormatException e ) {
            logger.log( Level.SEVERE, "Fail to parse integerString:" + Util.replaceNewLine( integerString ), e );
        }
        return integer;
    }

    public static String convertIntToIntString( int integer ) {
        return Integer.toString( integer );
    }

    public static long convertLongStringToLong( String longString ) {
        long longInteger = 0L;
        try {
            if ( longString != null ) {
                longInteger = Integer.parseInt( longString );
            }
        }
        catch ( NumberFormatException e ) {
            logger.log( Level.SEVERE, "Fail to parse longString:" + Util.replaceNewLine( longString ), e );
        }
        return longInteger;
    }

    public static String convertLongToLongString( int longInteger ) {
        return Long.toString( longInteger );
    }

    public static String convertToFiscalYearString( Calendar cal ) {
        String fiscalYear = "FY";
        String firstYear = "";
        String secondYear = "";

        if ( null == cal ) {
            cal = Calendar.getInstance();
        }

        if ( cal.get( Calendar.MONTH ) < 3 ) {
            firstYear = convertIntToIntString( cal.get( Calendar.YEAR ) - 1 );
            secondYear = convertIntToIntString( cal.get( Calendar.YEAR ) );
        }
        else {
            firstYear = convertIntToIntString( cal.get( Calendar.YEAR ) );
            secondYear = convertIntToIntString( cal.get( Calendar.YEAR ) + 1 );
        }
        fiscalYear = fiscalYear + firstYear.substring( 2, firstYear.length() ) + "-" + secondYear.substring( 2, secondYear.length() );
        return fiscalYear;
    }

    public static String convertToFiscalYearString() {
        return convertToFiscalYearString( null );
    }

    public static String convertToFinancialYearString() {
        Calendar cal = Calendar.getInstance();

        return convertToFinancialYearString( cal );
    }

    public static String convertToFinancialYearString( Calendar cal ) {
        String financialYear = "";
        if ( null != cal ) {
            if ( cal.get( Calendar.MONTH ) < 3 ) {
                financialYear = convertIntToIntString( cal.get( Calendar.YEAR ) - 1 );
            }
            else {
                financialYear = convertIntToIntString( cal.get( Calendar.YEAR ) );
            }
        }

        return financialYear;

    }

    public static String convertToCurrentCalendarYearString() {
        Calendar cal = Calendar.getInstance();
        return convertIntToIntString( cal.get( Calendar.YEAR ) );
    }

    public static int getYear( Date date ) {
        Calendar c = Calendar.getInstance();
        c.setTime( date );
        return c.get( Calendar.YEAR );
    }
}
