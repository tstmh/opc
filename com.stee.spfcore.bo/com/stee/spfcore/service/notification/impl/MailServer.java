package com.stee.spfcore.service.notification.impl;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.stee.spfcore.notification.BatchElectronicMail;
import com.stee.spfcore.notification.ElectronicMail;
import com.stee.spfcore.service.notification.NotificationServiceException;
import com.stee.spfcore.utils.Util;

public class MailServer implements IMailServer {

    private static final Logger logger = Logger.getLogger( MailServer.class.getName() );

    private String hostname;
    private int port;
    private Properties properties;
    private int maxRecipientsPerMessage;

    public MailServer( String hostname, int port, String protocol, int maxRecipientsPerMessage ) {
        this.hostname = hostname;
        this.port = port;
        this.maxRecipientsPerMessage = maxRecipientsPerMessage;

        this.properties = new Properties();
        this.properties.put( "mail.smtp.host", this.hostname );
        this.properties.put( "mail.smtp.port", this.port );

        if ( "ssl".equals( protocol ) ) {
            this.properties.put( "mail.smtp.auth", true );
            this.properties.put( "mail.smtp.ssl.enable", true );
        }
        else if ( "tls".equals( protocol ) ) {
            this.properties.put( "mail.smtp.auth", true );
            this.properties.put( "mail.smtp.starttls.enable", true );
        }

        Security.setProperty( "ssl.SocketFactory.provider", "com.ibm.jsse2.SSLSocketFactoryImpl" );
        Security.setProperty( "ssl.ServerSocketFactory.provider", "com.ibm.jsse2.SSLServerSocketFactoryImpl" );
    }

    @Override
    public void send( ElectronicMail email ) throws NotificationServiceException {

        final String username = email.getUserAddress();
        final String password = email.getUserPassword();

        Session session = Session.getInstance( this.properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( username, password );
            }
        } );

        try {
            MimeMessage message = new MimeMessage( session );
            message.setFrom( new InternetAddress( username ) );
            message.setRecipients( Message.RecipientType.TO, InternetAddress.parse( email.getToAddress() ) );

            if ( email.getCcAddress() != null && !email.getCcAddress().isEmpty() ) {
                message.setRecipients( Message.RecipientType.CC, InternetAddress.parse( email.getCcAddress() ) );
            }

            message.setSubject( Util.replaceNewLine( email.getSubject() ) );

            // No attachment.
            if ( email.getAttachments().isEmpty() ) {
                setMailBody( message, email.isHtmlContent(), email.getText() );
            }
            else {
                // set body
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                setMailBody( messageBodyPart, email.isHtmlContent(), email.getText() );

                // creates multi-part
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart( messageBodyPart );

                // Attachments
                for ( String filePath : email.getAttachments() ) {
                    MimeBodyPart attachPart = new MimeBodyPart();

                    try {
                        attachPart.attachFile( filePath );
                    }
                    catch ( IOException ex ) {
                        logger.severe(String.format("Unable to attach the following file attachment to email: %s", filePath.replace('\n', '_').replace('\r', '_') ));
                        // Will allow the email to continue to send, even if file attachment error.
                        continue;
                    }

                    multipart.addBodyPart( attachPart );
                }

                message.setContent( multipart );
            }

            Transport.send( message );
        }
        catch ( MessagingException exception ) {
            logger.severe(String.format("Fail to send email via SMTP: %s %s", this.hostname, exception ));
        }
    }

    private void setMailBody( Part part, boolean isHtml, String text ) throws MessagingException {
        if ( isHtml ) {
            part.setContent( text, "text/html" );
        }
        else {
            part.setText( text );
        }
    }

    @Override
    public void send( BatchElectronicMail email ) throws NotificationServiceException {
        final String username = email.getUserAddress();
        final String password = email.getUserPassword();

        Session session = Session.getInstance( this.properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( username, password );
            }
        } );

        try {
            MimeMessage message = new MimeMessage( session );
            message.setFrom( new InternetAddress( username ) );

            message.setSubject( Util.replaceNewLine( email.getSubject() ) );

            // No attachment.
            if ( email.getAttachments().isEmpty() ) {
                setMailBody( message, email.isHtmlContent(), email.getText() );
            }
            else {
                // set body
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                setMailBody( messageBodyPart, email.isHtmlContent(), email.getText() );

                // creates multi-part
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart( messageBodyPart );

                // Attachments
                for ( String filePath : email.getAttachments() ) {
                    MimeBodyPart attachPart = new MimeBodyPart();

                    try {
                        attachPart.attachFile( filePath );
                    }
                    catch ( IOException ex ) {
                        logger.severe(String.format("Unable to attach the following file attachment to email: %s", filePath ));
                        // Will allow the email to continue to send, even if file attachment error.
                        continue;
                    }

                    multipart.addBodyPart( attachPart );
                }

                message.setContent( multipart );
            }

            // only first batch of cc addresses will be sent,
            // and it will only be sent together with the first batch of to addresses.
            List< List< InternetAddress >> ccAddresses = new ArrayList<>();
            List< InternetAddress > ccSubList = new ArrayList<>();
            if ( ( email.getCcRecipients() != null ) && ( !email.getCcRecipients().isEmpty() ) ) {
                ccAddresses = convert( email.getCcRecipients() );
            }
            if(ccAddresses != null && !ccAddresses.isEmpty())
            		ccSubList = ccAddresses.get( 0 );

            List< List< InternetAddress >> addresses = convert( email.getToRecipients() );
            for ( List< InternetAddress > subList : addresses ) {
                for ( InternetAddress address : subList ) {
                    message.addRecipient( Message.RecipientType.TO, address );
                }
                if ( ccSubList != null && !ccSubList.isEmpty()) {
                    for ( InternetAddress address : ccSubList ) {
                        message.addRecipient( Message.RecipientType.CC, address );
                    }
                }
                Transport.send( message );

                if ( logger.isLoggable( Level.FINEST ) ) {
                    StringBuilder builder = new StringBuilder( "Success send email to: " );
                    for ( InternetAddress address : subList ) {
                        builder.append( address.getAddress() ).append( "," );
                    }
                    logger.finest( builder.toString() );
                }

                // Clear the recipients so can reuse the message.
                message.setRecipients( Message.RecipientType.TO, ( Address[] ) null );
                message.setRecipients( Message.RecipientType.CC, ( Address[] ) null );
                ccSubList = null;
            }

        }
        catch ( MessagingException exception ) {
            logger.severe(String.format("Fail to send email via SMTP: %s %s", this.hostname, exception ));
        }

    }

    private List< List< InternetAddress >> convert( List< String > recipients ) throws AddressException {

        List< List< InternetAddress >> addresses = new ArrayList<>();
        List< InternetAddress > subList = new ArrayList<>();

        for ( String recipient : recipients ) {
        	// Will skip the email 
    			InternetAddress address;
    			try {
    				address = new InternetAddress(recipient);
    			}
    			catch (AddressException e) {
    				logger.log(Level.WARNING, "Error will parsing email address:" + Util.replaceNewLine( recipient ), e);
    				continue;
    			}
        	
          subList.add( address );
          if ( subList.size() >= maxRecipientsPerMessage ) {
          	addresses.add( subList );
            subList = new ArrayList<>();
          }
        }

        if ( !subList.isEmpty() ) {
            addresses.add( subList );
        }

        return addresses;
    }

}
