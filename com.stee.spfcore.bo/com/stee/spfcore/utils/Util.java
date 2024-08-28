package com.stee.spfcore.utils;

import java.util.List;

public class Util {
    private Util(){}
    public static final String NEW_LINE = System.getProperty( "line.separator" );
    public static final String INDENT = "  ";

    /**
     * Replace new line to '_'. For fixing log forging
     * 
     * @param text
     * @return
     */
    public static String replaceNewLine( String text ) {
        if ( text == null || text.isEmpty() ) {
            return text;
        }

        return text.replace( '\n', '_' );
    }

    public static String replaceNewLine( List< String > list ) {

        StringBuilder builder = new StringBuilder();

        if ( list != null && !list.isEmpty() ) {
            builder.append( "[" );
            for ( String value : list ) {
                builder.append( replaceNewLine( value ) ).append( "," );
            }
            builder.append( "]" );
        }

        return builder.toString();
    }

    public static String toExceptionString( Exception e ) {
        StringBuilder builder = new StringBuilder();
        builder.append( e.getClass().toString() );
        builder.append( " : " );
        builder.append( e.getMessage() );
        for ( StackTraceElement item : e.getStackTrace() ) {
            builder.append( NEW_LINE );
            builder.append( INDENT );
            builder.append( item.toString() );
        }
        return builder.toString();
    }

}
