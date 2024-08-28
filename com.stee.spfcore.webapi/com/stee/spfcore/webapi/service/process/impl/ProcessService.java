package com.stee.spfcore.webapi.service.process.impl;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codehaus.jettison.json.JSONObject;

import com.stee.spfcore.webapi.model.benefits.BereavementGrant;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;
import com.stee.spfcore.webapi.model.membership.Membership;
import com.stee.spfcore.webapi.model.sag.SAGApplication;
import com.stee.spfcore.webapi.service.config.IProcessConfig;
import com.stee.spfcore.webapi.service.config.ServiceConfig;
import com.stee.spfcore.webapi.service.process.IProcessService;
import com.stee.spfcore.webapi.service.process.ProcessServiceException;
import com.stee.spfcore.webapi.utils.Encipher;
import com.stee.spfcore.webapi.utils.EnvironmentUtils;

public class ProcessService implements IProcessService {

    private static final Logger logger = LoggerFactory.getLogger( ProcessService.class);

    public ProcessService() {
    }

    @Override
    public void submitInsuranceNomination( Membership membership ) throws ProcessServiceException {
        String nric = membership.getNric();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "submit.insurance.nomination" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info("Response:" + processResponse );
        
    }

    @Override
    public void withdrawMembershipRequest( Membership membership ) throws ProcessServiceException {
        String nric = membership.getNric();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "withdraw.membership.request" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info("Response:" + processResponse );
    }

    @Override
    public void applyBereavementGrant( BereavementGrant bereavementGrant ) throws ProcessServiceException {
        String referenceNumber = bereavementGrant.getReferenceNumber();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.bereavement.grant" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info("Response:" + processResponse );
    }

    @Override
    public void applyNewBornGift( NewBornGift newBornGift ) throws ProcessServiceException {
        String referenceNumber = newBornGift.getReferenceNumber();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.new.born.gift" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info("Response:" + processResponse );

    }

    @Override
    public void applyWeddingGift( WeddingGift weddingGift ) throws ProcessServiceException {
        String referenceNumber = weddingGift.getReferenceNumber();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.wedding.gift" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info("Response:" + processResponse );

    }

    private String startProcess( String bpdId, String branchId, String snapshotId, JSONObject json ) throws ProcessServiceException {

        try {
            StartProcessUrl url = new StartProcessUrl( bpdId, branchId, snapshotId, json );
            String contentType = BaseUrl.getContentType();

            
            logger.info( "Request url:" + url.getUrl() );
            

            IProcessConfig config = ServiceConfig.getInstance().getProcessConfig();

            String password = config.password();
            String encryptionKey = EnvironmentUtils.getEncryptionKey();

            if ( encryptionKey != null && !encryptionKey.isEmpty() ) {
                try {
                    Encipher encipher = new Encipher( encryptionKey );
                    password = encipher.decrypt( password );
                }
                catch ( Exception e ) {
                    logger.warn("Error while decrypting the configured password.", e );
                }
            }

            HttpChannel http = new HttpChannel( config.username(), password );
            String response = http.post( url.getUrl(), contentType, contentType );

            logger.info("Process created, response = " + response );
            

            return response;
        }
        catch ( Exception exception ) {
            logger.warn("Fail to create process", exception );
            throw new ProcessServiceException( "Fail to create process" );
        }
    }

    @Override
    public void processMembership( String nric ) throws ProcessServiceException {

        Map< String, String > params = new HashMap< String, String >();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "process.membership.request" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info( "Response:" + processResponse );
       

    }

    @Override
    public void applySAGApplication( SAGApplication sagApplication ) throws ProcessServiceException {
        String referenceNumber = sagApplication.getReferenceNumber();
        Map< String, String > params = new HashMap< String, String >();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.sag.application" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        logger.info( "Response:" + processResponse );
        

    }

    private boolean isEmpty( String text ) {
        return ( text == null ) || ( text.trim().length() == 0 );
    }
}
