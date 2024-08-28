package com.stee.spfcore.webapi.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final int MILLISECS_PER_DAY = 1000 * 3600 * 24;

	/**
	 * <p>
	 * Checks if two date objects are on the same day ignoring time.
	 * </p>
	 *
	 * <p>
	 * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002
	 * 13:45 and 12 Mar 2002 13:45 would return false.
	 * </p>
	 * 
	 * @param date1
	 *            the first date, not altered, not null
	 * @param date2
	 *            the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameDay( final Date date1, final Date date2 ) {
		if ( date1 == null || date2 == null ) {
			throw new IllegalArgumentException( "The date must not be null" );
		}
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime( date1 );
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime( date2 );
		return isSameDay( cal1, cal2 );
	}

	/**
	 * <p>
	 * Checks if two calendar objects are on the same day ignoring time.
	 * </p>
	 *
	 * <p>
	 * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002
	 * 13:45 and 12 Mar 2002 13:45 would return false.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameDay( final Calendar cal1, final Calendar cal2 ) {
		if ( cal1 == null || cal2 == null ) {
			throw new IllegalArgumentException( "The date must not be null" );
		}
		return ( cal1.get( Calendar.ERA ) == cal2.get( Calendar.ERA )
				&& cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) && cal1
					.get( Calendar.DAY_OF_YEAR ) == cal2
				.get( Calendar.DAY_OF_YEAR ) );
	}

	/**
	 * Check if date1 is before date2, ignore time.
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeDay( final Date date1, final Date date2 ) {
		if ( date1 == null || date2 == null ) {
			throw new IllegalArgumentException( "The date must not be null" );
		}
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime( date1 );
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime( date2 );

		return isBeforeDay( cal1, cal2 );
	}

	/**
	 * Check if calendar object cal1 is before date2, ignore time.
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeDay( final Calendar cal1, final Calendar cal2 ) {
		if ( cal1.get( Calendar.YEAR ) < cal2.get( Calendar.YEAR ) ) {
			return true;
		} else if ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) {
			if ( cal1.get( Calendar.DAY_OF_YEAR ) < cal2
					.get( Calendar.DAY_OF_YEAR ) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if date1 is after date2, ignore time.
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isAfterDay( final Date date1, final Date date2 ) {
		if ( date1 == null || date2 == null ) {
			throw new IllegalArgumentException( "The date must not be null" );
		}
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime( date1 );
		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime( date2 );

		return isAfterDay( cal1, cal2 );
	}

	/**
	 * Check if calendar object cal1 is after date2, ignore time.
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isAfterDay( final Calendar cal1, final Calendar cal2 ) {
		if ( cal1.get( Calendar.YEAR ) > cal2.get( Calendar.YEAR ) ) {
			return true;
		} else if ( cal1.get( Calendar.YEAR ) == cal2.get( Calendar.YEAR ) ) {
			if ( cal1.get( Calendar.DAY_OF_YEAR ) > cal2
					.get( Calendar.DAY_OF_YEAR ) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the floor number of months difference from date1 to date2. If date2
	 * is before date1, return value will be negative. floorMonthsDifference(
	 * date1, date2 ) = -floorMonthsDifference( date2, date1 ) Example: x months
	 * and N days will return x months.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int floorMonthsDifference( Date date1, Date date2 ) {
		if ( date1 == null || date2 == null ) {
			throw new IllegalArgumentException( "The date must not be null" );
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime( date1 );

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime( date2 );

		int yearDiff = cal2.get( Calendar.YEAR ) - cal1.get( Calendar.YEAR );
		int monthDiff = yearDiff * 12 + cal2.get( Calendar.MONTH )
				- cal1.get( Calendar.MONTH );
		int dayDiff = cal2.get( Calendar.DAY_OF_MONTH )
				- cal1.get( Calendar.DAY_OF_MONTH );

		if ( monthDiff > 0 ) {
			if ( dayDiff < 0 ) {
				monthDiff--;
			}
		} else if ( monthDiff < 0 ) {
			if ( dayDiff > 0 ) {
				monthDiff++;
			}
		}

		return monthDiff;
	}

	/**
	 * 
	 * Gets the ceiling number of months difference from date1 to date2. If
	 * date2 is before date1, return value will be negative.
	 * ceilingMonthsDifference( date1, date2 ) = -ceilingMonthsDifference(
	 * date2, date1 ) Example: x months and N days will return ( x + 1 ) months.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int ceilingMonthsDifference( Date date1, Date date2 ) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime( date1 );

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime( date2 );

		int yearDiff = cal2.get( Calendar.YEAR ) - cal1.get( Calendar.YEAR );
		int monthDiff = yearDiff * 12 + cal2.get( Calendar.MONTH )
				- cal1.get( Calendar.MONTH );
		int dayDiff = cal2.get( Calendar.DAY_OF_MONTH )
				- cal1.get( Calendar.DAY_OF_MONTH );

		if ( monthDiff > 0 ) {
			if ( dayDiff >= 0 ) {
				monthDiff++;
			}
		} else if ( monthDiff < 0 ) {
			if ( dayDiff <= 0 ) {
				monthDiff--;
			}
		} else {
			if ( dayDiff > 0 ) {
				monthDiff++;
			} else if ( dayDiff < 0 ) {
				monthDiff--;
			}
		}

		return monthDiff;
	}

	public static int daysDifference( Date date1, Date date2 ) {
		long days1 = date1.getTime() / MILLISECS_PER_DAY;
		long days2 = date2.getTime() / MILLISECS_PER_DAY;
		return (int) ( days2 - days1 );
	}

}
