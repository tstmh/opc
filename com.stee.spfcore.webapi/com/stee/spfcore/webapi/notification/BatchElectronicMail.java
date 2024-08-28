package com.stee.spfcore.webapi.notification;

import java.util.ArrayList;
import java.util.List;

public class BatchElectronicMail {

    private String userAddress;
    private String userPassword;
    private List< String > toRecipients;
    private List< String > ccRecipients;
    private String subject;
    private String text;
    private boolean isHtmlContent = false;
    private List< String > attachments = new ArrayList< String >();

    public BatchElectronicMail() {
        super();
    }

    public BatchElectronicMail( String userAddress, String userPassword, List< String > toRecipients, String subject, String text ) {
        super();
        this.userAddress = userAddress;
        this.userPassword = userPassword;
        this.toRecipients = toRecipients;
        this.ccRecipients = null;
        this.subject = subject;
        this.text = text;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress( String userAddress ) {
        this.userAddress = userAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword( String userPassword ) {
        this.userPassword = userPassword;
    }

    public List< String > getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients( List< String > toRecipients ) {
        this.toRecipients = toRecipients;
    }

    public List< String > getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients( List< String > ccRecipients ) {
        this.ccRecipients = ccRecipients;
    }

    public void setAttachments( List< String > attachments ) {
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject( String subject ) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    public boolean isHtmlContent() {
        return isHtmlContent;
    }

    public void setHtmlContent( boolean isHtmlContent ) {
        this.isHtmlContent = isHtmlContent;
    }

    public void addAttachment( String file ) {
        this.attachments.add( file );
    }

    public List< String > getAttachments() {
        return this.attachments;
    }
}
