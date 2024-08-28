package com.stee.spfcore.utils;
/**
 * 
 * @ClassName: CleanPath 
 * @Description: tools for cleaning dynamic file path
 * @date 7th Jan 2019
 *
 */
public final class CleanPath {

    private CleanPath(){}
	public static String cleanString(String aString) {
		if (aString == null) return null;
	    
		StringBuilder cleanString = new StringBuilder();
	    
	    for (int i = 0; i < aString.length(); ++i) 
	    {
	    	cleanString.append(cleanChar(aString.charAt(i)));
	    }
	    return cleanString.toString();
	}


    private static char cleanChar(char aChar) {
       
       // 0 - 9
       for (int i = 48; i < 58; ++i) {
              if (aChar == i) return (char) i;
       }
       
       // 'A' - 'Z'
       for (int i = 65; i < 91; ++i) {
              if (aChar == i) return (char) i;
       }
       
       // 'a' - 'z'
       for (int i = 97; i < 123; ++i) {
              if (aChar == i) return (char) i;
       }
       
       // other valid characters
        switch (aChar) {
            case '/':
                return '/';
            case '\\':
                return '\\';
            case '.':
                return '.';
            case ':':
                return ':';
            case '-':
                return '-';
            case '_':
                return '_';
            case ' ':
                return ' ';
            default:
                return '%';
        }
    }
    
}
