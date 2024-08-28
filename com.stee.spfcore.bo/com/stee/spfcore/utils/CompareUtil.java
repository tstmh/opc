package com.stee.spfcore.utils;

public class CompareUtil {

	private CompareUtil(){}
	
	/**
	 * Compare two id String.
	 * The id 
	 * @param id1
	 * @param id2
	 * @return
	 */
	public static int compareIds( String id1, String id2 ) {
		long longId1 = 0L;
		long longId2 = 0L;
		if( null != id1 && null != id2) {
			if(id1.contains( "-" )) {
				longId1 = ConvertUtil.convertLongStringToLong( id1
						.substring( id1.lastIndexOf( "-" ) + 1,
								id1.length() ) );
			}
			
			if ( id2.contains( "-" ) ) {
				longId2 = ConvertUtil.convertLongStringToLong( id2
						.substring( id2.lastIndexOf( "-" ) + 1,
								id2.length() ) );
			}
		}
		
		return Long.compare( longId1, longId2 );
	}
}
