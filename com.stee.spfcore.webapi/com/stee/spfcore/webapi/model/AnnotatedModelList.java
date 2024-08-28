package com.stee.spfcore.webapi.model;

import java.util.ArrayList;
import java.util.List;

import com.stee.spfcore.webapi.dao.AuditRevisionEntity;
import com.stee.spfcore.webapi.model.personnel.Child;
import com.stee.spfcore.webapi.model.personnel.Email;
import com.stee.spfcore.webapi.model.personnel.Employment;
import com.stee.spfcore.webapi.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.webapi.model.personnel.Leave;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;
import com.stee.spfcore.webapi.model.personnel.Phone;
import com.stee.spfcore.webapi.model.personnel.Spouse;

public class AnnotatedModelList {

    public static final List< Class< ? >> MODEL_LIST;

    static {
        MODEL_LIST = new ArrayList< Class< ? >>();
        addAllModelClasses( MODEL_LIST );
    }

    public static void addAllModelClasses( List< Class< ? >> classList ) {
    	addAuditClasses( classList );
        addPersonnelClasses( classList );
    }
    
    public static void addAuditClasses( List< Class< ? >> classList ) {
        classList.add( AuditRevisionEntity.class );
    }

    public static void addPersonnelClasses( List< Class< ? >> classList ) {
        classList.add( Child.class );
        classList.add( Email.class );
        classList.add( Employment.class );
        classList.add( Leave.class );
        classList.add( PersonalDetail.class );
        classList.add( Phone.class );
        classList.add( Spouse.class );
        classList.add( ExtraEmploymentInfo.class );
    }

}
 
