package com.stee.spfcore.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: AgeUtils 
 * @Description: tools for calculating age
 * @author yu.yunxia
 * @date Aug 8, 2016 4:07:07 PM 
 *
 */
public final class AgeUtils {

    private AgeUtils(){}
    
    /**
     * 
     * @Title: getAge 
     * @Description: Get age based on date of birth.
     * @param @param dateOfBirth
     * @param @return
     * @return Integer
     * @throws
     */
    public static final Integer getAge(Date dateOfBirth) {
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if ((today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) || (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob
                .get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
    
}
