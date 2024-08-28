package com.stee.spfcore.service.marketingContent.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.service.configuration.IMarketingContentConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.marketingContent.MarketingContentException;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import com.stee.spfcore.utils.Util;

public class ECMUtil {

    private static final Logger logger = Logger.getLogger( ECMUtil.class.getName() );

    private static final String USER_AGENT = "Mozilla/5.0";

    private String contentURL;
    private String authHeader;

    public ECMUtil() throws UnsupportedEncodingException {

        IMarketingContentConfig config = ServiceConfig.getInstance().getMarketingContentConfig();

        contentURL = config.ecmContentURL();
        contentURL = contentURL.replace( "ObjectStoreID", URLEncoder.encode( config.ecmObjectStoreID(), "UTF-8" ) );

        String password = config.ecmPassword();

        String encryptionKey = EnvironmentUtils.getEncryptionKey();

        if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
            try {
                Encipher encipher = new Encipher( encryptionKey );
                password = encipher.decrypt( password );
            }
            catch ( Exception e ) {
                logger.log( Level.SEVERE, "Error while decrypting the configured password.", e );
            }
        }

        String auth = config.ecmUsername() + ":" + password;
        String encodedAuth = Base64.encodeBase64String( auth.getBytes( Charset.forName( "ISO-8859-1" ) ) );
        authHeader = "Basic " + encodedAuth;
    }

    public BinaryFile download( String contentId ) throws MarketingContentException {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String path = null;

        try {
            path = Util.replaceNewLine( contentURL + URLEncoder.encode( contentId, "UTF-8" ) );

            logger.info( String.format( "downloading ECM file. contentId=%s, path=%s", contentId, path).replace('\n', '_').replace('\r','_') );

            httpClient = HttpClientBuilder.create().setUserAgent( USER_AGENT ).build();

            HttpGet httpGet = new HttpGet( path );
            httpGet.setHeader( HttpHeaders.AUTHORIZATION, authHeader );

            response = httpClient.execute( httpGet );

            int statusCode = response.getStatusLine().getStatusCode();
            if ( statusCode != 200 ) {
                logger.log( Level.WARNING, "Failed : HTTP error code : " + statusCode );
                throw new MarketingContentException( "Failed : HTTP error code : " + statusCode );
            }

            logger.info( String.format( "downloaded ECM file. statusCode=%s, path=%s", statusCode, path ).replace('\n','_').replace('\r','_') );

            BinaryFile binaryFile = new BinaryFile();
            binaryFile.setDocId( contentId );
            binaryFile.setName( getFileName( response ) );
            binaryFile.setContentType( getContentType( response ) );
            binaryFile.setContent( EntityUtils.toByteArray( response.getEntity() ) );

            return binaryFile;

        }
        catch ( MalformedURLException e ) {
            logger.log( Level.WARNING, "Malformed URL:" + path, e );
            throw new MarketingContentException( "Failed to invoke GET REST Service: " + Util.replaceNewLine( path ), e );
        }
        catch ( IOException e ) {
            logger.log( Level.WARNING, "Fail to get response from " + Util.replaceNewLine( path ), e );
            throw new MarketingContentException( "Failed to invoke GET REST Service: " + path, e );
        }
        finally {
            try {
                if ( response != null ) {
                    response.close();
                }
                if ( httpClient != null ) {
                    httpClient.close();
                }
            }
            catch ( IOException ex ) {
                if ( logger.isLoggable( Level.INFO ) ) {
                    logger.log( Level.INFO, "Fail to close HttpClient/HttpResponse", ex );
                }
            }
        }
    }

    private String getFileName( CloseableHttpResponse response ) throws UnsupportedEncodingException {

        Header header = response.getFirstHeader( "Content-Disposition" );
        String headerString = header.getValue();

        String[] params = headerString.split( ";" );
        for ( String param : params ) {
            param = param.trim();
            if ( param.startsWith( "filename" ) ) {
                int index = param.indexOf( "=" );
                param = param.substring( index + 1 );

                if ( param.startsWith( "UTF-8''" ) ) {
                    param = param.substring( 7 );

                    param = URLDecoder.decode( param, "UTF-8" );
                }

                return param;
            }
        }

        return "Unknown";
    }

    private String getContentType( CloseableHttpResponse response ) {

        Header header = response.getFirstHeader( "Content-Type" );

        String headerString = header.getValue();
        String[] param = headerString.split( ";" );

        return param[ 0 ];
    }

}
