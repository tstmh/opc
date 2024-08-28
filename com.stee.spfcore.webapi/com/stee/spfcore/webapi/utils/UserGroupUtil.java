package com.stee.spfcore.webapi.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.stee.spfcore.webapi.service.config.IProcessConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;
import com.stee.spfcore.webapi.utils.rest.RESTInvoker;

public class UserGroupUtil {
    private static final String RESULT_TYPE = "application/xml";

    private static final Logger LOGGER = Logger.getLogger( UserGroupUtil.class.getName() );
    private static RESTInvoker restInvoker = null;

    public static List< String > getUsersInGroups( List< String > groups ) {
        List< String > users = new ArrayList< String >();
        Set< String > usersSet = new HashSet< String >();
        if ( groups != null ) {
            for ( String group : groups ) {
                if ( group != null ) {
                    getUsersInGroup( group, usersSet );
                }
            }
        }
        users.addAll( usersSet );
        return users;
    }

    public static List< String > getUsersInGroup( String group ) {
        List< String > users = new ArrayList< String >();
        Set< String > usersSet = new HashSet< String >();
        if ( group != null ) {
            getUsersInGroup( group, usersSet );
        }
        users.addAll( usersSet );
        return users;
    }

    private static void getUsersInGroup( String group, Set< String > users ) {
        RESTInvoker restInvoker = getRestInvokerInstance();
        try {
            group = group.replaceAll( " ", "%20" );
            String url = "?filter=" + group + "&includeDeleted=false&parts=all%7Cmembers";
            String xmlResult = restInvoker.get( url, RESULT_TYPE );
            extractUsers( xmlResult, users );
        }
        catch ( Exception e ) {
            LOGGER.log( Level.SEVERE, "getUsersInGroup()", e );
        }
    }

    private static RESTInvoker getRestInvokerInstance() {
        if ( restInvoker == null ) {
            restInvoker = createRestInvoker();
        }
        return restInvoker;
    }

    private static RESTInvoker createRestInvoker() {
        IProcessConfig processConfig = ServiceConfig.getInstance().getProcessConfig();

        String baseURL = "http://" + processConfig.hostname() + ":" + processConfig.port() + "/rest/bpm/wle/v1/groups";

        String restUsername = processConfig.username();
        String restPassword = processConfig.password();

        String encryptionKey = EnvironmentUtils.getEncryptionKey();

        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                restPassword = encipher.decrypt( restPassword );
            }
            catch ( Exception e ) {
                LOGGER.log( Level.SEVERE, "Error while decrypting the configured password.", e );
            }
        }

        return new RESTInvoker( baseURL, restUsername, restPassword );
    }

    private static void extractUsers( String xmlResult, Set< String > users ) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build( new ByteArrayInputStream( xmlResult.getBytes() ) );
        Element rootElement = document.getRootElement();
        List< Element > dataElements = rootElement.getChildren( "data" );
        for ( Element dataElement : dataElements ) {
            List< Element > groupsElements = dataElement.getChildren( "groups" );
            for ( Element groupsElement : groupsElements ) {
                List< Element > membersElements = groupsElement.getChildren( "members" );
                for ( Element membersElement : membersElements ) {
                    users.add( membersElement.getTextTrim() );
                }
            }
        }

    }
}
