package com.stee.spfcore.webapi.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class DateTimeUtil {
    public final static int DAYS_PER_WEEK = 7;
    public final static long MILLISECONDS_PER_DAY = 24 * 3600 * 1000;
    private final static SimpleDateFormat MONTH_DATE_FORMAT = new SimpleDateFormat( "MMMM" );

    public static Date getDateMonthsAfterToday( int months ) {
        return getDateMonthsAfter( new Date(), months );
    }

    public static Date getDateWeeksAfterToday( int weeks ) {
        return getDateWeeksAfter( new Date(), weeks );
    }

    public static Date getDateDaysAfterToday( int days ) {
        return getDateDaysAfter( new Date(), days );
    }

    public static Date getDateMonthsAfter( Date date, int months ) {
        if ( date == null ) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime( date );
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        int day = c.get( Calendar.DAY_OF_MONTH );
        int month = c.get( Calendar.MONTH ) + months;
        int year = c.get( Calendar.YEAR );
        while ( month > 11 ) {
            month -= 12;
            year += 1;
        }
        while ( month < 0 ) {
            month += 12;
            year -= 1;
        }
        c.set( Calendar.MONTH, month );
        c.set( Calendar.YEAR, year );
        int maxDay = c.getActualMaximum( Calendar.DAY_OF_MONTH );
        if ( day > maxDay ) {
            day = maxDay;
        }
        c.set( Calendar.DAY_OF_MONTH, day );
        return c.getTime();
    }

    public static Date getDateWeeksAfter( Date date, int weeks ) {
        return getDateDaysAfter( date, DAYS_PER_WEEK * weeks );
    }

    public static Date getDateDaysAfter( Date date, int days ) {
        if ( date == null ) {
            return null;
        }
        long localTimeZoneOffset = TimeZone.getDefault().getRawOffset();
        long daysUntilToday = ( date.getTime() + localTimeZoneOffset ) / MILLISECONDS_PER_DAY;
        daysUntilToday += days;
        return new Date( daysUntilToday * MILLISECONDS_PER_DAY - localTimeZoneOffset );
    }

    public static Date getFirstDateOfMonth( Date date ) {
        if ( date == null ) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime( date );
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        c.set( Calendar.DAY_OF_MONTH, c.getActualMinimum( Calendar.DAY_OF_MONTH ) );
        return c.getTime();
    }

    public static Date getLastDateOfMonth( Date date ) {
        if ( date == null ) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime( date );
        c.set( Calendar.HOUR_OF_DAY, 0 );
        c.set( Calendar.MINUTE, 0 );
        c.set( Calendar.SECOND, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        c.set( Calendar.DAY_OF_MONTH, c.getActualMaximum( Calendar.DAY_OF_MONTH ) );
        return c.getTime();
    }

    public static String getMonthName( Date date ) {
        if ( date == null ) {
            return null;
        }

        return MONTH_DATE_FORMAT.format( date );
    }

    public static Integer getMonth( Date date ) {
        if ( date == null ) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime( date );
        return c.get( Calendar.MONTH ) + 1;
    }

    public static Integer getYear( Date date ) {
        if ( date == null ) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime( date );
        return c.get( Calendar.YEAR );
    }

    public static Integer getDayOfWeek( Date date ) {
        if ( date == null ) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime( date );
        return c.get( Calendar.DAY_OF_WEEK );
    }

    public static Integer getDay( Date date ) {
        if ( date == null ) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime( date );
        return c.get( Calendar.DAY_OF_MONTH );
    }

    public static Boolean isWeekDay( Date date ) {
        if ( date == null ) {
            return null;
        }
        int dayOfWeek = getDayOfWeek( date );
        switch ( dayOfWeek ) {
            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
            case Calendar.THURSDAY:
            case Calendar.FRIDAY:
                return true;
            default:
                return false;
        }
    }

    public static Boolean isWeekEnd( Date date ) {
        if ( date == null ) {
            return null;
        }
        int dayOfWeek = getDayOfWeek( date );
        switch ( dayOfWeek ) {
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                return true;
            default:
                return false;
        }
    }

    public static Date getEarlierDate( Date date1, Date date2 ) {
        if ( date1 != null ) {
            if ( date2 != null ) {
                if ( date1.getTime() < date2.getTime() ) {
                    return date1;
                }
                else {
                    return date2;
                }
            }
            else {
                return date1;
            }
        }
        else if ( date2 != null ) {
            return date2;
        }
        return null;
    }

    public static Date getLaterDate( Date date1, Date date2 ) {
        if ( date1 != null ) {
            if ( date2 != null ) {
                if ( date1.getTime() > date2.getTime() ) {
                    return date1;
                }
                else {
                    return date2;
                }
            }
            else {
                return date1;
            }
        }
        else if ( date2 != null ) {
            return date2;
        }
        return null;
    }

    public static Date getDate( int year, int month, int day ) {
        Calendar c = Calendar.getInstance();
        c.set( year, month - 1, day, 0, 0, 0 );
        c.set( Calendar.MILLISECOND, 0 );
        return c.getTime();
    }

    public static Date getTime( int hour, int minute, int second ) {
        Calendar c = Calendar.getInstance();
        c.set( 0, 0, 0, hour, minute, second );
        c.set( Calendar.MILLISECOND, 0 );
        return c.getTime();
    }

    public static int computeAge( Date dateOfBirth, Date asOfDate ) {
        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.setTime( dateOfBirth );
        Calendar asOfCalendar = Calendar.getInstance();
        asOfCalendar.setTime( asOfDate );
        int age = asOfCalendar.get( Calendar.YEAR ) - dobCalendar.get( Calendar.YEAR );
        if ( asOfCalendar.get( Calendar.MONTH ) < dobCalendar.get( Calendar.MONTH ) ) {
            age--;
        }
        else if ( asOfCalendar.get( Calendar.MONTH ) == dobCalendar.get( Calendar.MONTH ) && asOfCalendar.get( Calendar.DAY_OF_MONTH ) < dobCalendar.get( Calendar.DAY_OF_MONTH ) ) {
            age--;
        }
        return age;
    }

    public static Integer getFinancialYear( Date date ) {
        if ( date == null ) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH );
        if ( month < Calendar.APRIL ) {
            year -= 1;
        }
        return year;
    }

    public static Date getFinancialYearStart( Integer financialYear ) {
        if ( financialYear == null ) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set( financialYear, Calendar.APRIL, 1, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        return calendar.getTime();
    }

    public static Date getFinancialYearEnd( Integer financialYear ) {
        if ( financialYear == null ) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set( financialYear + 1, Calendar.MARCH, 31, 23, 59, 59 );
        calendar.set( Calendar.MILLISECOND, 999 );
        return calendar.getTime();
    }

    public static List< Date > getFullMonthDates( Date currentDate ) {
        List< Date > fullMonthDates = null;
        if ( currentDate != null ) {
            fullMonthDates = new LinkedList< Date >();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( currentDate );
            calendar.set( Calendar.HOUR_OF_DAY, 0 );
            calendar.set( Calendar.MINUTE, 0 );
            calendar.set( Calendar.SECOND, 0 );
            calendar.set( Calendar.MILLISECOND, 0 );
            int maxDayOfMonth = calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
            int minDayOfMonth = calendar.getActualMinimum( Calendar.DAY_OF_MONTH );
            for ( int i = minDayOfMonth; i <= maxDayOfMonth; i++ ) {
                calendar.set( Calendar.DAY_OF_MONTH, i );
                fullMonthDates.add( calendar.getTime() );
            }
        }
        return fullMonthDates;
    }
}
