package com.stee.spfcore.model;

import java.util.ArrayList;
import java.util.List;

import com.stee.spfcore.dao.AuditRevisionEntity;
import com.stee.spfcore.model.accounting.BankInformation;
import com.stee.spfcore.model.accounting.BatchFileConfig;
import com.stee.spfcore.model.announcement.Announcement;
import com.stee.spfcore.model.announcement.AnnouncementSender;
import com.stee.spfcore.model.benefits.ApprovalRecord;
import com.stee.spfcore.model.benefits.BenefitsProcess;
import com.stee.spfcore.model.benefits.BereavementGrant;
import com.stee.spfcore.model.benefits.GrantBudget;
import com.stee.spfcore.model.benefits.HealthCareProvider;
import com.stee.spfcore.model.benefits.NewBornGift;
import com.stee.spfcore.model.benefits.SupportingDocument;
import com.stee.spfcore.model.benefits.WeddingGift;
import com.stee.spfcore.model.benefits.WeightMgmtSubsidy;
import com.stee.spfcore.model.blacklist.BlacklistModule;
import com.stee.spfcore.model.blacklist.Blacklistee;
import com.stee.spfcore.model.calendar.MarketingEvent;
import com.stee.spfcore.model.calendar.PublicHoliday;
import com.stee.spfcore.model.category.Category;
import com.stee.spfcore.model.category.SubCategory;
import com.stee.spfcore.model.code.Code;
import com.stee.spfcore.model.code.CodeMapping;
import com.stee.spfcore.model.corporateCard.CardAutoBroadcast;
import com.stee.spfcore.model.corporateCard.CardBooking;
import com.stee.spfcore.model.corporateCard.CardCollectionDetail;
import com.stee.spfcore.model.corporateCard.CardDetail;
import com.stee.spfcore.model.corporateCard.CardTypeDetail;
import com.stee.spfcore.model.course.Course;
import com.stee.spfcore.model.course.CourseParticipant;
import com.stee.spfcore.model.course.RegistrationConfig;
import com.stee.spfcore.model.course.ReminderAttachment;
import com.stee.spfcore.model.course.Slot;
import com.stee.spfcore.model.course.Supervisor;
import com.stee.spfcore.model.course.Trainer;
import com.stee.spfcore.model.course.internal.ApplicantSelectionTaskStatus;
import com.stee.spfcore.model.course.internal.OutcomeBroadcastTaskStatus;
import com.stee.spfcore.model.course.internal.ReminderStatus;
import com.stee.spfcore.model.course.internal.WaitListTask;
import com.stee.spfcore.model.department.DepartmentDetail;
import com.stee.spfcore.model.genericEvent.GETicketingChoice;
import com.stee.spfcore.model.genericEvent.GETicketingOption;
import com.stee.spfcore.model.genericEvent.GETicketingSection;
import com.stee.spfcore.model.genericEvent.GenericEventApplication;
import com.stee.spfcore.model.genericEvent.GenericEventDepartment;
import com.stee.spfcore.model.genericEvent.GenericEventDetail;
import com.stee.spfcore.model.genericEvent.internal.GETargetedUser;
import com.stee.spfcore.model.genericEvent.internal.NotifyApplicationResultTask;
import com.stee.spfcore.model.globalParameters.GlobalParameters;
import com.stee.spfcore.model.hrps.HRPSConfig;
import com.stee.spfcore.model.hrps.HRPSDetails;
import com.stee.spfcore.model.internal.ProcessingInfo;
import com.stee.spfcore.model.marketing.BooleanRule;
import com.stee.spfcore.model.marketing.CodeRule;
import com.stee.spfcore.model.marketing.DateRule;
import com.stee.spfcore.model.marketing.DurationRule;
import com.stee.spfcore.model.marketing.ListRule;
import com.stee.spfcore.model.marketing.ListRuleValue;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.model.marketing.NumberRule;
import com.stee.spfcore.model.marketing.Rule;
import com.stee.spfcore.model.marketing.RuleSet;
import com.stee.spfcore.model.marketing.StringRule;
import com.stee.spfcore.model.marketingContent.Attachment;
import com.stee.spfcore.model.marketingContent.ContentTemplate;
import com.stee.spfcore.model.marketingContent.MarketingContent;
import com.stee.spfcore.model.marketingContent.MarketingContentSet;
import com.stee.spfcore.model.marketingContent.TemplateResource;
import com.stee.spfcore.model.marketingContent.internal.BinaryFile;
import com.stee.spfcore.model.marketingContent.internal.ContentIdBinaryFileJoint;
import com.stee.spfcore.model.marketingContent.internal.EmailLog;
import com.stee.spfcore.model.marketingContent.internal.HtmlFile;
import com.stee.spfcore.model.marketingContent.internal.PublishingContent;
import com.stee.spfcore.model.marketingContent.internal.PublishingContentSetTask;
import com.stee.spfcore.model.marketingContent.internal.SmsLog;
import com.stee.spfcore.model.marketingContent.internal.UserContentViewRecord;
import com.stee.spfcore.model.membership.DiscrepancyRecord;
import com.stee.spfcore.model.membership.Insurance;
import com.stee.spfcore.model.membership.Membership;
import com.stee.spfcore.model.membership.MembershipFees;
import com.stee.spfcore.model.membership.MembershipPaymentCheckRecord;
import com.stee.spfcore.model.membership.Nominee;
import com.stee.spfcore.model.membership.PaymentHistory;
import com.stee.spfcore.model.membership.PaymentRecord;
import com.stee.spfcore.model.membership.ReconciliationReport;
import com.stee.spfcore.model.membership.ReconciliationReportCpoRecord;
import com.stee.spfcore.model.membership.ReconciliationReportDiscrepancyRecord;
import com.stee.spfcore.model.membership.StatusChangeRecord;
import com.stee.spfcore.model.personnel.ChangeRecord;
import com.stee.spfcore.model.personnel.Child;
import com.stee.spfcore.model.personnel.Email;
import com.stee.spfcore.model.personnel.Employment;
import com.stee.spfcore.model.personnel.ExtraEmploymentInfo;
import com.stee.spfcore.model.personnel.HRChangeRecord;
import com.stee.spfcore.model.personnel.Leave;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.model.personnel.Phone;
import com.stee.spfcore.model.personnel.Spouse;
import com.stee.spfcore.model.personnel.internal.HRProcessingInfo;
import com.stee.spfcore.model.planner.PlannerConfig;
import com.stee.spfcore.model.report.SsrsReport;
import com.stee.spfcore.model.report.SsrsReportEnvironment;
import com.stee.spfcore.model.sag.SAGAcademicResults;
import com.stee.spfcore.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.model.sag.SAGAwardQuantum;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.model.sag.SAGCoCurricularActivity;
import com.stee.spfcore.model.sag.SAGConfigSetup;
import com.stee.spfcore.model.sag.SAGDonation;
import com.stee.spfcore.model.sag.SAGEventDetail;
import com.stee.spfcore.model.sag.SAGEventRsvp;
import com.stee.spfcore.model.sag.SAGFamilyBackground;
import com.stee.spfcore.model.sag.SAGPrivileges;
import com.stee.spfcore.model.sag.SAGTask;
import com.stee.spfcore.model.sag.inputConfig.SAGInputs;
import com.stee.spfcore.model.sag.inputConfig.SAGSubInputs;
import com.stee.spfcore.model.survey.Answer;
import com.stee.spfcore.model.survey.Choice;
import com.stee.spfcore.model.survey.Option;
import com.stee.spfcore.model.survey.Question;
import com.stee.spfcore.model.survey.Response;
import com.stee.spfcore.model.survey.Section;
import com.stee.spfcore.model.survey.Survey;
import com.stee.spfcore.model.survey.SurveyBroadcast;
import com.stee.spfcore.model.survey.SurveyTask;
import com.stee.spfcore.model.survey.internal.SurveyBroadcastLog;
import com.stee.spfcore.model.system.SystemStatus;
import com.stee.spfcore.model.template.Tag;
import com.stee.spfcore.model.template.Template;
import com.stee.spfcore.model.userAnnouncement.UserAnnouncement;
import com.stee.spfcore.model.userRoleManagement.UrmBpmGroup;
import com.stee.spfcore.model.userRoleManagement.UrmBrpGroupUser;
import com.stee.spfcore.model.vendor.CategoryInfo;
import com.stee.spfcore.model.vendor.Vendor;

public class AnnotatedModelList {

    private AnnotatedModelList(){}
    public static final List< Class< ? >> MODEL_LIST;

    static {
        MODEL_LIST = new ArrayList<>();
        addAllModelClasses( MODEL_LIST );
    }

    public static void addAllModelClasses( List< Class< ? >> classList ) {
    	addAuditClasses( classList );
        addSystemClasses( classList );
        addCodeClasses( classList );
        addPersonnelClasses( classList );
        addMembershipClasses( classList );
        addBenefitsClasses( classList );
        addCategoryClasses( classList );
        addTemplateClasses( classList );
        addVendorClasses( classList );
        addMarketingClasses( classList );
        addSpfLeeClasses( classList );
        addDepartmentClasses( classList );
        addWelfareCourseClasses( classList );
        addFluVaccinationHivHealthScreeningClasses( classList );
        addBlacklistClasses( classList );
        addSurveyClasses( classList );
        addCorporateCardClasses( classList );
        addCalendarClasses( classList );
        addMarketingContentClasses( classList );
        addGlobalParametersClasses( classList );
        addAnnouncementClasses( classList );
        addUserAnnouncementClasses( classList );
        addGenericEventClasses( classList );
        addReportClasses( classList );
        addPlannerConfigClasses( classList );
        addRateThisWebsiteClasses( classList );
        addAccountingClasses( classList );
        addHRPSClasses(classList);
        addUserAccountClasses(classList);
        //addCoreInterfaceClasses( classList );

    }
    
    public static void addAuditClasses( List< Class< ? >> classList ) {
        classList.add( AuditRevisionEntity.class );
    }

    public static void addReportClasses( List< Class< ? >> classList ) {
        classList.add( SsrsReportEnvironment.class );
        classList.add( SsrsReport.class );
    }

    public static void addSystemClasses( List< Class< ? >> classList ) {
        classList.add( SystemStatus.class );
    }

    public static void addCodeClasses( List< Class< ? >> classList ) {
        classList.add( Code.class );
        classList.add( CodeMapping.class );
    }

    public static void addPersonnelClasses( List< Class< ? >> classList ) {
        classList.add( ChangeRecord.class );
        classList.add( Child.class );
        classList.add( Email.class );
        classList.add( Employment.class );
        classList.add( Leave.class );
        classList.add( PersonalDetail.class );
        classList.add( Phone.class );
        classList.add( Spouse.class );
        classList.add( HRChangeRecord.class );
        classList.add( HRProcessingInfo.class );
        classList.add( ExtraEmploymentInfo.class );
        classList.add( UserProcessingDetails.class );
    }

    public static void addMembershipClasses( List< Class< ? >> classList ) {
        classList.add( Insurance.class );
        classList.add( Membership.class );
        classList.add( Nominee.class );
        classList.add( PaymentRecord.class );
        classList.add( MembershipFees.class );
        classList.add( StatusChangeRecord.class );
        classList.add( DiscrepancyRecord.class );
        classList.add( PaymentHistory.class );
        classList.add( ReconciliationReport.class );
        classList.add( ReconciliationReportCpoRecord.class );
        classList.add( ReconciliationReportDiscrepancyRecord.class );
        classList.add( MembershipPaymentCheckRecord.class );
    }

    public static void addBenefitsClasses( List< Class< ? >> classList ) {
        classList.add( ProcessingInfo.class );
        classList.add( ApprovalRecord.class );
        classList.add( BereavementGrant.class );
        classList.add( GrantBudget.class );
        classList.add( NewBornGift.class );
        classList.add( SupportingDocument.class );
        classList.add( WeddingGift.class );
        classList.add( ReferenceGenerator.class );
        classList.add( HealthCareProvider.class );
        classList.add( WeightMgmtSubsidy.class );
        classList.add( BenefitsProcess.class);
    }

    public static void addCategoryClasses( List< Class< ? >> classList ) {
        classList.add( Category.class );
        classList.add( SubCategory.class );
    }

    public static void addTemplateClasses( List< Class< ? >> classList ) {
        classList.add( Tag.class );
        classList.add( Template.class );
    }

    public static void addVendorClasses( List< Class< ? >> classList ) {
        classList.add( Vendor.class );
        classList.add( CategoryInfo.class );
    }

    public static void addMarketingClasses( List< Class< ? >> classList ) {
        classList.add( MemberGroup.class );
        classList.add( RuleSet.class );
        classList.add( Rule.class );
        classList.add( BooleanRule.class );
        classList.add( CodeRule.class );
        classList.add( DateRule.class );
        classList.add( DurationRule.class );
        classList.add( ListRule.class );
        classList.add( NumberRule.class );
        classList.add( StringRule.class );
        classList.add( ListRuleValue.class );
    }

    public static void addSpfLeeClasses( List< Class< ? >> classList ) {
        classList.add( SAGApplication.class );
        classList.add( SAGAcademicResults.class );
        classList.add( SAGCoCurricularActivity.class );
        classList.add( SAGFamilyBackground.class );
        classList.add( SAGInputs.class );
        classList.add( SAGSubInputs.class );
        classList.add( SAGConfigSetup.class );
        classList.add( SAGAwardQuantum.class );
        classList.add( SAGPrivileges.class );
        classList.add( SAGEventDetail.class );
        classList.add( SAGEventRsvp.class );
        classList.add( SAGDonation.class );
        classList.add( SAGAnnouncementConfig.class );
        classList.add( SAGTask.class );
        classList.add( SAGBatchFileRecord.class );
    }

    public static void addDepartmentClasses( List< Class< ? >> classList ) {
        classList.add( DepartmentDetail.class );
    }

    public static void addWelfareCourseClasses( List< Class< ? >> classList ) {
        classList.add( Course.class );
        classList.add( CourseParticipant.class );
        classList.add( RegistrationConfig.class );
        classList.add( Slot.class );
        classList.add( Supervisor.class );
        classList.add( Trainer.class );
        classList.add( ReminderStatus.class );
        classList.add( ApplicantSelectionTaskStatus.class );
        classList.add( OutcomeBroadcastTaskStatus.class );
        classList.add( WaitListTask.class );
        classList.add( ReminderAttachment.class );
    }

    public static void addFluVaccinationHivHealthScreeningClasses( List< Class< ? >> classList ) {
        classList.add( DepartmentNumbers.class );
        classList.add( DepartmentSessions.class );
        classList.add( DraftDepartmentsSessions.class );
        classList.add( LocationDepartments.class );
        classList.add( RecipientCcRoles.class );
        classList.add( Reminder.class );
        classList.add( SessionDetail.class );
        classList.add( TimeSlotDetail.class );
        classList.add( SupportingDocument.class );
        classList.add( EventLocationSessionConfig.class );
        classList.add( EventLocationDepartmentSessionConfig.class );
    }

    public static void addBlacklistClasses( List< Class< ? >> classList ) {
        classList.add( Blacklistee.class );
        classList.add( BlacklistModule.class );
    }

    public static void addSurveyClasses( List< Class< ? >> classList ) {
        classList.add( Survey.class );
        classList.add( Section.class );
        classList.add( Question.class );
        classList.add( Option.class );
        classList.add( Response.class );
        classList.add( Answer.class );
        classList.add( Choice.class );
        classList.add( SurveyTask.class );
        classList.add( SurveyBroadcast.class );
        classList.add( SurveyBroadcastLog.class );
    }

    public static void addCorporateCardClasses( List< Class< ? >> classList ) {
        classList.add( CardTypeDetail.class );
        classList.add( CardDetail.class );
        classList.add( CardCollectionDetail.class );
        classList.add( CardBooking.class );
        classList.add( CardAutoBroadcast.class );
    }

    public static void addCalendarClasses( List< Class< ? >> classList ) {
        classList.add( PublicHoliday.class );
        classList.add( MarketingEvent.class );
    }

    public static void addMarketingContentClasses( List< Class< ? >> classList ) {
        classList.add( MarketingContentSet.class );
        classList.add( MarketingContent.class );
        classList.add( Attachment.class );

        classList.add( BinaryFile.class );
        classList.add( HtmlFile.class );
        classList.add( PublishingContent.class );
        classList.add( PublishingContentSetTask.class );
        classList.add( ContentIdBinaryFileJoint.class );

        classList.add( UserContentViewRecord.class );
        classList.add( EmailLog.class );
        classList.add( SmsLog.class );

        classList.add( ContentTemplate.class );
        classList.add( TemplateResource.class );
    }

    public static void addGlobalParametersClasses( List< Class< ? >> classList ) {
        classList.add( GlobalParameters.class );
    }

    public static void addAnnouncementClasses( List< Class< ? >> classList ) {
        classList.add( Announcement.class );
        classList.add( AnnouncementSender.class );
    }

    public static void addUserAnnouncementClasses( List< Class< ? >> classList ) {
        classList.add( UserAnnouncement.class );
    }

    public static void addGenericEventClasses( List< Class< ? >> classList ) {
        classList.add( GenericEventDetail.class );
        classList.add( GETicketingOption.class );
        classList.add( GenericEventApplication.class );
        classList.add( GETicketingChoice.class );
        classList.add( NotifyApplicationResultTask.class );
        classList.add( GETargetedUser.class );
        classList.add( GETicketingSection.class );
        classList.add( GenericEventDepartment.class );
    }

    public static void addPlannerConfigClasses( List< Class< ? >> classList ) {
        classList.add( PlannerConfig.class );
    }

    public static void addRateThisWebsiteClasses( List< Class< ? >> classList ) {
    	classList.add( UrmBpmGroup.class );
    	classList.add( UrmBrpGroupUser.class );
    }

    public static void addAccountingClasses( List< Class< ? >> classList ) {
        classList.add( BankInformation.class );
        classList.add( BatchFileConfig.class );
    }
    
    public static void addHRPSClasses( List< Class< ?>> classList ) {
    	classList.add( HRPSConfig.class );
    	classList.add( HRPSDetails.class );
        classList.add(HRPSConfig.class);
    }
    
    public static void addUserAccountClasses ( List< Class< ?>> classList ) {
    	classList.add( UserAccountDetail.class );
    }
    
    /*public static void addCoreInterfaceClasses( List< Class< ? >> classList )
    {
    	classList.add( ExtInterfacerCommon.class );
    }*/

}
 
