package com.stee.spfcore.service.process.impl;

import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.service.configuration.IProcessConfig;
import com.stee.spfcore.service.configuration.ServiceConfig;
import com.stee.spfcore.service.process.IProcessService;
import com.stee.spfcore.service.process.ProcessServiceException;
import com.stee.spfcore.utils.Encipher;
import com.stee.spfcore.utils.EnvironmentUtils;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessService implements IProcessService {

    private static final Logger logger = Logger.getLogger( ProcessService.class.getName() );

    public ProcessService() {
        // This method is intentionally left empty
    }

    @Override
    public void submitInsuranceNomination( Membership membership ) throws ProcessServiceException {
        String nric = membership.getNric();
        Map< String, String > params = new HashMap<>();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "submit.insurance.nomination" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }
    }

    @Override
    public void withdrawMembershipRequest( Membership membership ) throws ProcessServiceException {
        String nric = membership.getNric();
        Map< String, String > params = new HashMap<>();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "withdraw.membership.request" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }
    }

    @Override
    public void applyBereavementGrant( BereavementGrant bereavementGrant ) throws ProcessServiceException {
        String referenceNumber = bereavementGrant.getReferenceNumber();
        Map< String, String > params = new HashMap<>();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.bereavement.grant" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }
    }

    @Override
    public void applyNewBornGift( NewBornGift newBornGift ) throws ProcessServiceException {
        String referenceNumber = newBornGift.getReferenceNumber();
        Map< String, String > params = new HashMap<>();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.new.born.gift" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }

    }

    @Override
    public void applyWeddingGift( WeddingGift weddingGift ) throws ProcessServiceException {
        String referenceNumber = weddingGift.getReferenceNumber();
        Map< String, String > params = new HashMap<>();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.wedding.gift" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }

    }

    private String startProcess( String bpdId, String branchId, String snapshotId, JSONObject json ) throws ProcessServiceException {

        try {
            StartProcessUrl url = new StartProcessUrl( bpdId, branchId, snapshotId, json );
            String contentType = BaseUrl.getContentType();

            if ( logger.isLoggable( Level.FINEST ) ) {
                logger.log( Level.FINE, "Request url:" + url.getUrl() );
            }

            IProcessConfig config = ServiceConfig.getInstance().getProcessConfig();

            String password = config.password();
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

            HttpChannel http = new HttpChannel( config.username(), password );
            String response = http.post( url.getUrl(), contentType);

            if ( logger.isLoggable( Level.FINEST ) ) {
                logger.fine(String.format( "Process created, response = %s", response ));
            }


            return response;
        }
        catch ( Exception exception ) {
            logger.log( Level.SEVERE, "Fail to create process", exception );
            throw new ProcessServiceException( "Fail to create process" );
        }
    }

    @Override
    public void processMembership( String nric ) throws ProcessServiceException {

        Map< String, String > params = new HashMap<>();
        params.put( "NRIC", nric );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "process.membership.request" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }

    }

    @Override
    public void applySAGApplication( SAGApplication sagApplication ) throws ProcessServiceException {
        String referenceNumber = sagApplication.getReferenceNumber();
        Map< String, String > params = new HashMap<>();
        params.put( "ReferenceNumber", referenceNumber );
        JSONObject json = new JSONObject( params );

        IProcessConfig config = ServiceConfig.getInstance().getProcessConfig( "apply.sag.application" );
        String response = startProcess( config.bpdId(), config.branchId(), config.snapshotId(), json );
        ProcessResponse processResponse = new ProcessResponse( response );

        if ( logger.isLoggable( Level.FINER ) ) {
            logger.log( Level.FINER, "Response:" + processResponse );
        }

    }

    private boolean isEmpty( String text ) {
        return ( text == null ) || (text.trim().isEmpty());
    }
}
