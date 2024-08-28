package com.stee.spfcore.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TypeResolver;

import com.stee.spfcore.dao.dac.DataAccessCheck;
import com.stee.spfcore.model.ApplicationStatus;
import com.stee.spfcore.model.accounting.PaymentStatus;
import com.stee.spfcore.model.benefits.SupportingDocument;
import com.stee.spfcore.model.code.CodeType;
import com.stee.spfcore.model.internal.SAGAwardType;
import com.stee.spfcore.model.internal.SAGPaymentType;
import com.stee.spfcore.model.personnel.ContactLabel;
import com.stee.spfcore.model.sag.SAGAnnouncementConfig;
import com.stee.spfcore.model.sag.SAGApplication;
import com.stee.spfcore.model.sag.SAGAwardQuantum;
import com.stee.spfcore.model.sag.SAGBatchFileRecord;
import com.stee.spfcore.model.sag.SAGConfigSetup;
import com.stee.spfcore.model.sag.SAGDateConfigType;
import com.stee.spfcore.model.sag.SAGDonation;
import com.stee.spfcore.model.sag.SAGEventDetail;
import com.stee.spfcore.model.sag.SAGEventRsvp;
import com.stee.spfcore.model.sag.SAGFamilyBackground;
import com.stee.spfcore.model.sag.SAGPrivileges;
import com.stee.spfcore.model.sag.SAGTask;
import com.stee.spfcore.model.sag.inputConfig.SAGInputType;
import com.stee.spfcore.model.sag.inputConfig.SAGInputs;
import com.stee.spfcore.model.sag.inputConfig.SAGSubInputs;
import com.stee.spfcore.security.AccessDeniedException;
import com.stee.spfcore.security.SecurityInfo;
import com.stee.spfcore.utils.ConvertUtil;
import com.stee.spfcore.utils.Util;
import com.stee.spfcore.vo.sag.SAGApplicationApprovalsCriteria;
import com.stee.spfcore.vo.sag.SAGApplicationChildDetail;
import com.stee.spfcore.vo.sag.SAGApplicationCriteria;
import com.stee.spfcore.vo.sag.SAGApplicationDetailByApprovals;
import com.stee.spfcore.vo.sag.SAGApplicationFamilyBackground;
import com.stee.spfcore.vo.sag.SAGApplicationResult;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAward;
import com.stee.spfcore.vo.sag.SAGApplicationsApprovedForAwardWithPayment;
import com.stee.spfcore.vo.sag.SAGApplicationsForAudit;
import com.stee.spfcore.vo.sag.SAGAwardQuantumDescription;
import com.stee.spfcore.vo.sag.SAGPrivilegePersonalDetails;
import com.stee.spfcore.vo.sag.SAGPrivilegeUserDetail;

public class SAGApplicationDAO {

    private static final Logger logger = Logger.getLogger( SAGApplicationDAO.class.getName() );

    // private static final String HQL_SEARCH_SAG_BY_CRITERIA =
    // "SELECT DISTINCT sag.referenceNumber AS referenceNumber, sag.awardType AS awardType"
    // +
    // ", personal.name AS applicantName, personal.nric AS applicantId, sag.childName AS childName"
    // +
    // ", sag.applicationStatus AS applicationStatus, sag.submittedBy AS submittedBy "
    // + "FROM PersonalDetail AS personal, SAGApplication AS sag "
    // + "WHERE personal.nric = sag.memberNric";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA = "SELECT DISTINCT sag.REFERENCE_NUMBER AS referenceNumber, sag.AWARD_TYPE AS awardType"
            + ", personal.NAME AS applicantName, personal.NRIC AS applicantId, sag.CHILD_NAME AS childName" + ", sag.APPLICATION_STATUS AS applicationStatus, sag.SUBMITTED_BY AS submittedBy, submittedByPersonal.NAME AS submittedByName"
            + ", submittedByPersonal.PREFERRED_CONTACT_MODE AS submittedByPrefContactMode" + ", sag.SEQUENCE_NUMBER AS sequenceNumber" + ", sag.PAYMENT_ADVICE_EMAIL AS submittedByPaymentAdviceEmail"
            + ", (Select TOP (1) prefEmail.ADDRESS from SPFCORE.PERSONAL_EMAIL_DETAILS AS prefEmail where prefEmail.PERSONAL_NRIC = submittedByPersonal.NRIC and prefEmail.PREFER = 1) AS submittedByPrefEmail"
            + ", (Select TOP (1) workEmail.ADDRESS from SPFCORE.PERSONAL_EMAIL_DETAILS AS workEmail where workEmail.PERSONAL_NRIC = submittedByPersonal.NRIC and workEmail.LABEL = '"
            + ContactLabel.WORK.name()
            + "') AS submittedByWorkEmail"
            + ", (Select TOP (1) prefContact.PHONE_NUMBER from SPFCORE.PERSONAL_PHONE_DETAILS AS prefContact where prefContact.PERSONAL_NRIC = submittedByPersonal.NRIC and prefContact.PREFER = 1) AS submittedByPrefContactNumber"
            + ", most_recent_app_record.OFFICER_ACTION AS officerAction"
            + ", sag.CHILD_HIGHEST_EDU_LVL AS childHighestEduLevel, sag.CHILD_NEW_EDU_LVL AS childNewEduLevel, inputs.LIST_ORDER as listOrder "
            + ", (CASE WHEN sag.award_Type = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN 0 "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN 1 "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN 2 END) AS awardOrder "
            + ", sag.CHILD_PSLE_SCORE AS childPsleScore, sag.CHILD_CGPA AS childCgpa "
            + ", (CASE WHEN sag.award_Type = '"
            + SAGAwardType.STUDY_AWARD
            + "' OR sag.award_Type = '"
            + SAGAwardType.STUDY_GRANT
            + ""
            + "'THEN 0 "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' AND sag.CHILD_HIGHEST_EDU_LVL = 'PSLE' THEN (CASE WHEN sag.CHILD_PSLE_SCORE IS NULL OR sag.CHILD_PSLE_SCORE='' THEN 0 ELSE CAST(sag.CHILD_PSLE_SCORE AS DECIMAL(4,0)) END) "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD + "' AND sag.CHILD_HIGHEST_EDU_LVL IN ('NIT','HNIT','DIP','IB') THEN sag.CHILD_CGPA END) AS score "
            + ", taskId "
            + "FROM SPFCORE.SAG_APPLICATION AS sag ";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SAGINPUTS_SUFFIX = " LEFT JOIN SPFCORE.SAG_INPUTS as inputs " + "ON (inputs.AWARD_TYPE = sag.AWARD_TYPE and " + "inputs.INPUT_TYPE = ( Case " + "when sag.award_Type = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD + "' then '" + SAGInputType.HIGHEST_EDU_LEVEL + "'" + "when sag.award_Type = '" + SAGAwardType.STUDY_AWARD + "' then '" + SAGInputType.NEW_EDU_LEVEL + "'" + "END ) AND "
            + "inputs.INPUT_ID = ( Case " + "when sag.award_Type = '" + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD + "' then sag.child_highest_edu_lvl " + "when sag.award_Type = '" + SAGAwardType.STUDY_AWARD
            + "' then sag.child_new_edu_lvl " + "END ) )";

    private static final String SQL_SEARCH_SAG_TASK = " LEFT JOIN (" + "SELECT REFERENCE_NUMBER, Task_ID AS taskId FROM SPFCORE.SAG_Task ) AS sagTask ON sag.REFERENCE_NUMBER = sagTask.REFERENCE_NUMBER ";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SUFFIX = " LEFT JOIN (" + "SELECT * FROM (" + "	 SELECT app.*, row_number() OVER (" + "PARTITION BY app.SAG_REFERENCE_NUMBER " + "ORDER BY app.DATE_OF_COMPLETION DESC "
            + ") AS row_num " + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app" + ") AS ordered_app_records " + "WHERE ordered_app_records.row_num = 1" + ") AS most_recent_app_record "
            + "ON (sag.REFERENCE_NUMBER = most_recent_app_record.SAG_REFERENCE_NUMBER ";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SUBMMITED_BY_SUFFIX = " LEFT JOIN SPFCORE.PERSONAL_DETAILS as submittedByPersonal " + "ON (submittedByPersonal.NRIC = sag.SUBMITTED_BY )";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_SUFFIX = ", SPFCORE.PERSONAL_DETAILS AS personal " + "WHERE personal.NRIC = sag.MEMBER_NRIC";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_ORDER_BY_REFERENCE_NUMBER_SUFFIX = " ORDER BY sag.REFERENCE_NUMBER ASC";

    private static final String SQL_SEARCH_SAG_BY_CRITERIA_ORDER_BY_SUFFIX = " ORDER BY awardOrder, inputs.LIST_ORDER, score DESC, sag.REFERENCE_NUMBER";

    private static final String HQL_GET_SEARCH_PRIVILEGE_USER_DETAILS = "SELECT DISTINCT priv.id as privilegeId, priv.memberNric AS nric, priv.financialYear as financialYear"
            + ", priv.comments AS comments, personal.name AS name, personal.employment.organisationOrDepartment AS departmentId" + ", code.description AS departmentDescription, "
            + "( SELECT DISTINCT emailContacts.address AS address from personal.emailContacts AS emailContacts WHERE emailContacts.prefer = true ) AS preferredEmail,"
            + " ( SELECT DISTINCT phones.number AS contactNumber from personal.phoneContacts AS phones WHERE phones.prefer = true ) AS preferredContactNumber " + "FROM SAGPrivileges AS priv, PersonalDetail AS personal, Code AS code "
            + "WHERE priv.memberNric = personal.nric AND personal.employment.organisationOrDepartment = code.id AND code.type = :codeType";

    private static final String HQL_SEARCH_CHILD_DETAILS = "SELECT sag.memberNric AS memberNric, sag.financialYear AS financialYear, sag.referenceNumber AS referenceNumber"
            + ", sag.childName AS childName, sag.childNric AS childNric, sag.childEmail AS childEmail" + " FROM SAGApplication AS sag" + " WHERE sag.allowChildEmailForPRD = true" + " AND sag.financialYear = :financialYear"
            + " ORDER BY sag.referenceNumber";

    private static final String HQL_GET_PERSONAL_DETAILS_FOR_ADD_PRIVILEGE = "SELECT DISTINCT personal.name AS name, personal.nric AS nric,"
            + " personal.gender AS genderId, ( SELECT genderCode.description AS genderDesc from Code AS genderCode where genderCode.id = personal.gender AND genderCode.type = '"
            + CodeType.GENDERS
            + "' ) AS genderDescription,"
            + " personal.nationality as nationalityId, ( SELECT nationalityCode.description AS nationalityDesc from Code AS nationalityCode where nationalityCode.id = personal.nationality AND nationalityCode.type = '"
            + CodeType.NATIONALITY
            + "' ) AS nationalityDescription,"
            + " personal.employment.organisationOrDepartment AS departmentId, ( SELECT departmentCode.description AS departmentDesc from Code AS departmentCode where departmentCode.id = personal.employment.organisationOrDepartment AND departmentCode.type = '"
            + CodeType.UNIT_DEPARTMENT + "' ) AS departmentDescription,"
            + " personal.employment.rankOrGrade AS rankId, ( SELECT rankCode.description AS rankDesc from Code AS rankCode where rankCode.id = personal.employment.rankOrGrade AND rankCode.type = '" + CodeType.RANK
            + "' ) AS rankDescription," + " ( SELECT DISTINCT emailContacts.address AS address from personal.emailContacts AS emailContacts WHERE emailContacts.prefer = true ) AS preferredEmail,"
            + " ( SELECT DISTINCT phones.number AS contactNumber from personal.phoneContacts AS phones WHERE phones.prefer = true ) AS preferredContactNumber" + " FROM PersonalDetail AS personal";

    private static final String SQL_GET_SAG_APPLICATION_BY_APPROVALS = "SELECT DISTINCT sag.REFERENCE_NUMBER AS referenceNumber, " + "sag.APPLICATION_STATUS AS applicationStatus, sag.SUBMISSION_DATE AS dateOfSubmission, "
            + "sag.CHILD_NAME AS childName, sag.CHILD_NRIC AS childNric, " + "sag.SUBMITTED_BY AS submittedBy, "
            + "(CASE " + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN "
            + "sag.CHILD_HIGHEST_EDU_LVL "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN "
            + "sag.CHILD_NEW_EDU_LVL "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN "
            + "'-' "
            + "END) as educationLevel, "
            + "(CASE " + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN '"
            + ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.name() ).replaceAll( "_", " " )
            + "' "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN '"
            + ( SAGAwardType.STUDY_AWARD.name() ).replaceAll( "_", " " )
            + "' "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN '"
            + ( SAGAwardType.STUDY_GRANT.name() ).replaceAll( "_", " " )
            + "' "
            + "END) as awardTypeDescription, "
            + "most_recent_app_record.OFFICER_NAME as officerName, "
            + "personal.NRIC AS memberNric, personal.NAME AS memberName "
            + "FROM SPFCORE.SAG_APPLICATION AS sag JOIN ("
            + "SELECT * FROM ("
            + "	 SELECT app.*, row_number() OVER ("
            + "PARTITION BY app.SAG_REFERENCE_NUMBER "
            + "ORDER BY app.DATE_OF_COMPLETION DESC "
            + ") AS row_num "
            + "FROM SPFCORE.APPLICATION_APPROVAL_RECORD app"
            + ") AS ordered_app_records "
            + "WHERE ordered_app_records.row_num = 1"
            + ") AS most_recent_app_record " + "ON (sag.REFERENCE_NUMBER = most_recent_app_record.SAG_REFERENCE_NUMBER ";

    private static final String SQL_GET_SAG_APPLICATION_BY_APPROVALS_SUFFIX = ") , SPFCORE.PERSONAL_DETAILS AS personal" + " where sag.MEMBER_NRIC = personal.NRIC" + " ORDER BY sag.REFERENCE_NUMBER";

    private static final String SQL_GET_SAG_APPROVED_APPLICATIONS = "SELECT sag.CHILD_NAME AS childName, sag.CHILD_NRIC AS childNric, " + "sag.REFERENCE_NUMBER AS referenceNumber, sag.SEQUENCE_NUMBER AS sequenceNumber, "
            + "(CASE WHEN sag.award_Type = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN 0 "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN 1 "
            + "WHEN sag.award_Type = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN 2 END) AS awardOrder, "
            + "sag.CHILD_CURR_SCHOOL AS childCurrentSchoolId, "
            + "(CASE WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN (SELECT sagCurrSchoolInputs.INPUT_DESCRIPTION FROM SPFCORE.SAG_INPUTS sagCurrSchoolInputs WHERE sagCurrSchoolInputs.INPUT_ID = sag.CHILD_CURR_SCHOOL AND sagCurrSchoolInputs.AWARD_TYPE = sag.AWARD_TYPE "
            + "AND sagCurrSchoolInputs.INPUT_TYPE = '"
            + SAGInputType.CURRENT_SCHOOL_INST
            + "') WHEN sag.AWARD_TYPE <> '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN sag.CHILD_CURR_SCHOOL "
            + "END) AS childCurrentSchoolDesc, "
            + "sag.CHILD_HIGHEST_EDU_LVL AS childHighestEduLevelId, "
            + "(SELECT sagEduLvlInputs.INPUT_DESCRIPTION FROM SPFCORE.SAG_INPUTS sagEduLvlInputs WHERE sagEduLvlInputs.INPUT_ID = sag.CHILD_HIGHEST_EDU_LVL AND sagEduLvlInputs.AWARD_TYPE = sag.AWARD_TYPE "
            + "AND sagEduLvlInputs.INPUT_TYPE = '"
            + SAGInputType.HIGHEST_EDU_LEVEL
            + "') AS childHighestEduLevelDesc, "
            + "sag.CHILD_NEW_EDU_LVL AS childNewEduLevelId, "
            + "(SELECT sagNewEduLvlInputs.INPUT_DESCRIPTION FROM SPFCORE.SAG_INPUTS sagNewEduLvlInputs WHERE sagNewEduLvlInputs.INPUT_ID = sag.CHILD_NEW_EDU_LVL AND sagNewEduLvlInputs.AWARD_TYPE = sag.AWARD_TYPE "
            + "AND sagNewEduLvlInputs.INPUT_TYPE = '"
            + SAGInputType.NEW_EDU_LEVEL
            + "') AS childNewEduLevelDesc, "
            + "sag.AWARD_TYPE AS awardType, "
            + "(CASE "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN '"
            + ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.name() ).replaceAll( "_", " " )
            + "' "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN '"
            + ( SAGAwardType.STUDY_AWARD.name() ).replaceAll( "_", " " )
            + "' "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN '"
            + ( SAGAwardType.STUDY_GRANT.name() ).replaceAll( "_", " " )
            + "' "
            + "END) AS awardTypeDescription, sag.FINANCIAL_YEAR AS financialYear, "
            + "sag.CHEQUE_UPDATED_DATE AS chequeUpdatedDate, sag.CHEQUE_VALUE_DATE AS chequeValueDate, "
            + "awardQuantum.AMOUNT AS awardAmount, "
            + "personal.nric AS memberNric, personal.name AS memberName "
            + "FROM SPFCORE.SAG_APPLICATION AS sag LEFT JOIN SPFCORE.SAG_AWARD_QUANTUM awardQuantum "
            + "ON (sag.AWARD_TYPE = awardQuantum.AWARD_TYPE AND sag.FINANCIAL_YEAR = awardQuantum.FINANCIAL_YEAR AND "
            + "awardQuantum.SUB_TYPE = ( CASE "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN sag.CHILD_HIGHEST_EDU_LVL "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN sag.CHILD_NEW_EDU_LVL " + "WHEN sag.AWARD_TYPE = '" + SAGAwardType.STUDY_GRANT + "' THEN sag.AWARD_TYPE " + "END ) ), SPFCORE.PERSONAL_DETAILS AS personal" + " where sag.MEMBER_NRIC = personal.NRIC ";

    private static final String SQL_GET_SAG_APPROVED_APPLICATIONS_SUFFIX = " ORDER BY awardOrder, sag.SEQUENCE_NUMBER, sag.REFERENCE_NUMBER";

    private static final String HQL_IS_SAG_APPLICATION_EXISTS = "SELECT count(sag.referenceNumber) FROM SAGApplication AS sag " + " WHERE sag.referenceNumber = :referenceNumber";

    private static final String HQL_DELETE_DONATIONS_BY_ID = "DELETE FROM SAGDonation donation WHERE donation.id IN (:deleteList)";

    private static final String HQL_DELETE_PRIVILEGES_BY_ID = "DELETE FROM SAGPrivileges privilege WHERE privilege.id IN (:deleteList)";

    private static final String HQL_DELETE_EVENT_RSVP_BY_ID = "DELETE FROM SAGEventRsvp rsvp WHERE rsvp.id IN (:deleteList)";

    private static final String SQL_GET_SAG_APPLICATIONS_FOR_AUDIT = "Select TOP (:percentValue) PERCENT sag.Reference_Number as referenceNumber, sag.AWARD_TYPE as awardType, " + "(CASE " + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD + "' THEN '" + ( SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD.name() ).replaceAll( "_", " " ) + "' " + "WHEN sag.AWARD_TYPE = '" + SAGAwardType.STUDY_AWARD + "' THEN '"
            + ( SAGAwardType.STUDY_AWARD.name() ).replaceAll( "_", " " ) + "' " + "WHEN sag.AWARD_TYPE = '" + SAGAwardType.STUDY_GRANT + "' THEN '" + ( SAGAwardType.STUDY_GRANT.name() ).replaceAll( "_", " " ) + "' "
            + "END) AS awardTypeDescription, " + "personal.name as memberName, personal.nric as memberNric, " + "sag.child_name as childName, sag.CHILD_NRIC AS childNric, " + "employment.ORGANISATION_OR_DEPARTMENT AS departmentId, "
            + "(Select deptCode.description AS deptDesc from SPFCORE.CODES deptCode where deptCode.code_type = 'UNIT_DEPARTMENT' AND deptCode.id = employment.ORGANISATION_OR_DEPARTMENT) AS departmentDescription, "
            + "sag.SUBMISSION_DATE as dateOfSubmission, fam.incomeAvg AS perCapitaIncome, " + "(CASE WHEN fam.incomeAvg is null then 1 else 0 END) AS perCapitaIncomeIndex " + "from SPFCORE.SAG_APPLICATION sag " + "LEFT JOIN "
            + "( Select famBg.Reference_Number AS refNumber , " + "(SUM ( (CASE WHEN famBg.Gross_salary IS NULL THEN 0 WHEN famBg.Gross_salary IS NOT NULL THEN famBg.Gross_salary END ) "
            + "+ (CASE WHEN famBg.special_allowance IS NULL THEN 0 WHEN famBg.special_allowance IS NOT NULL THEN famBg.special_allowance END ) ) / (count(famBg.Id) +1) ) AS incomeAvg "
            + "from SPFCORE.SAG_FAMILY_BACKGROUND famBg, SPFCORE.SAG_APPLICATION sagApp " + "where famBg.REFERENCE_NUMBER = sagApp.REFERENCE_NUMBER " + "Group BY famBg.REFERENCE_NUMBER ) AS fam "
            + "ON (fam.refNumber = sag.Reference_Number) " + "LEFT JOIN SPFCORE.PERSONAL_DETAILS personal ON (sag.member_nric = personal.nric) " + "LEFT JOIN PERSONAL_EMPLOYMENT_DETAILS employment ON (personal.nric = employment.nric) "
            + "WHERE sag.AWARD_TYPE= '" + SAGAwardType.STUDY_AWARD + "' " + "AND fam.incomeAvg IS NOT NULL AND fam.incomeAvg > 0 " + "AND employment.SERVICE_TYPE != '412' ";

    private static final String SQL_GET_SAG_APPLICATIONS_FOR_AUDIT_SUFFIX = " ORDER BY perCapitaIncomeIndex, perCapitaIncome, referenceNumber ASC";

    private static final String SQL_GET_SAG_AWARD_PREVIOUS_BALANCE = "SELECT SUM( donation.AMOUNT ) - " + "( SELECT SUM( sag.AMOUNT_PAID ) " + "	FROM SPFCORE.SAG_APPLICATION AS sag "
            + "WHERE sag.FINANCIAL_YEAR = :sagFinancialYear ) AS balance " + "FROM SPFCORE.SAG_DONATION AS donation " + "WHERE donation.FINANCIAL_YEAR = :donationFinancialYear";

    private static final String SQL_GET_MAX_SAG_SEQUENCE_NUMBER = "Select max(sag.SEQUENCE_NUMBER) AS sequenceNumber from SPFCORE.SAG_APPLICATION sag where sag.FINANCIAL_YEAR = :financialYear";

    private static final String SQL_UPDATE_EXPIRED_SAG_STATUS = "UPDATE SAGApplication SET applicationStatus = :newStatus WHERE applicationStatus = :oldStatus AND (SELECT configuredDate FROM SAGConfigSetup WHERE configType = :configType AND financialYear = :financialYear) < :date";

    private static final String SQL_GET_SAG_APPROVED_APPLICATIONS_PAYMENT_INFORMATION = "SELECT sag.CHILD_NAME AS childName, sag.CHILD_NRIC AS childNric, " + "sag.REFERENCE_NUMBER AS referenceNumber, sag.SEQUENCE_NUMBER AS sequenceNumber, "
            + "(CASE WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN 0 "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN 1 "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_GRANT
            + "' THEN 2 END) AS awardOrder, "
            + "sag.AWARD_TYPE AS awardType, "
            + "sag.FINANCIAL_YEAR AS financialYear, "
            + "awardQuantum.AMOUNT AS awardAmount, "
            + "personal.nric AS memberNric, personal.name AS memberName, "
            + "sag.PAYMENT_ADVICE_EMAIL AS submittedByPaymentAdviceEmail"
            + ", (Select TOP (1) prefEmail.ADDRESS from SPFCORE.PERSONAL_EMAIL_DETAILS AS prefEmail where prefEmail.PERSONAL_NRIC = personal.NRIC and prefEmail.PREFER = 1) AS submittedByPrefEmail"
            + ", (Select TOP (1) workEmail.ADDRESS from SPFCORE.PERSONAL_EMAIL_DETAILS AS workEmail where workEmail.PERSONAL_NRIC = personal.NRIC and workEmail.LABEL = '"
            + ContactLabel.WORK.name()
            + "') AS submittedByWorkEmail, "
            + "sag.PREFERRED_PAYMENT_MODE AS preferredPaymentMode, "
            + "(CASE WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.GIRO
            + "' THEN (SELECT bankInformation.SWIFT_BIC_CODE FROM SPFCORE.BANK_INFORMATION bankInformation WHERE bankInformation.BANK_CODE = sag.BANK ) "
            + "WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.PAYNOW
            + "' THEN "
            + "sag.PAYNOW_PROXY_TYPE "
            + "END) as bicCodeProxyType, "
            + "(CASE WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.GIRO
            + "' THEN "
            + "sag.ACCOUNT_NO "
            + "WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.PAYNOW
            + "' THEN "
            + "sag.PAYNOW_PROXY_VALUE "
            + "END) as accNoProxyValue, "
            + "(CASE WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.GIRO
            + "' THEN "
            + "sag.BANK_RECIPIENT_NAME "
            + "WHEN sag.PREFERRED_PAYMENT_MODE = '"
            + SAGPaymentType.PAYNOW
            + "' THEN "
            + "sag.PAYNOW_RECIPIENT_NAME "
            + "END) as accName, "
            + "bfr.PAYMENT_STATUS AS paymentStatus "
            + "FROM SPFCORE.SAG_APPLICATION AS sag "
            + "LEFT JOIN SPFCORE.SAG_BATCH_FILE_RECORD bfr on sag.REFERENCE_NUMBER = bfr.REFERENCE_NUMBER "
            + "LEFT JOIN SPFCORE.SAG_AWARD_QUANTUM awardQuantum "
            + "ON (sag.AWARD_TYPE = awardQuantum.AWARD_TYPE AND sag.FINANCIAL_YEAR = awardQuantum.FINANCIAL_YEAR AND "
            + "awardQuantum.SUB_TYPE = ( CASE "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.SCHOLASTIC_ACHIEVEMENT_AWARD
            + "' THEN sag.CHILD_HIGHEST_EDU_LVL "
            + "WHEN sag.AWARD_TYPE = '"
            + SAGAwardType.STUDY_AWARD
            + "' THEN sag.CHILD_NEW_EDU_LVL " + "WHEN sag.AWARD_TYPE = '" + SAGAwardType.STUDY_GRANT + "' THEN sag.AWARD_TYPE " + "END ) ), SPFCORE.PERSONAL_DETAILS AS personal" + " where sag.MEMBER_NRIC = personal.NRIC ";


    public List< SAGApplication > searchSAGApplications( String nric ) throws AccessDeniedException {

        logger.log( Level.INFO, "Search SAG Applications for member: " + nric );
        return searchSAGApplications( nric, null, null, null, true );
    }

    @SuppressWarnings( "unchecked" )
    public SAGApplication searchSAGApplicationsRetrieveFamilyBackground( String nric, String childNric, String awardType, String financialYear, boolean isOrderAsc ) throws AccessDeniedException {

        logger.log( Level.INFO, "Search SAG Applications by awardType for member: " + nric + childNric );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( null != nric ) {
            if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
                throw new AccessDeniedException( "Caller does not have access to data belong to " + nric );
            }
        }
        SAGApplication sagApplication = new SAGApplication();

        SAGApplication sagApplicationTemp = new SAGApplication();
        /*select sag from SAGApplication sag where sag.memberNric = :nric and sag.childNric = :childNric
         * and sag.applicationStatus = :applicationStatus and sag.financialYear = :financialYear
         *
         * if sag == null
         * select sag from SAGApplication sag where sag.memberNric = :nric
         * and sag.financialYear = :financialYear
         */

        financialYear = ConvertUtil.convertToCurrentCalendarYearString();
        String lastFinancialYear = "";
        lastFinancialYear = String.valueOf(Integer.valueOf(financialYear)-1);
        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != nric && !nric.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE sag.memberNric = :nric" );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }

        if ( isWhereClauseAdded ) {
            queryStr.append( " AND" );
        }
        else {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE" );
        }
        queryStr.append( " sag.awardType != :awardType" );

        //queryStr.append( " ORDER BY sag.dateOfSubmission,sag.referenceNumber" );
        queryStr.append( " ORDER BY sag.dateOfSubmission " );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );
        queryStr.append( " ,sag.referenceNumber " );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );

        Query query = session.createQuery( queryStr.toString() );
        if ( null != nric && !nric.isEmpty() ) {
            query.setParameter( "nric", nric );
        }
        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }
        query.setParameter( "awardType", "SAA" );

        List< SAGApplication > sagApplicationList = ( List< SAGApplication > ) query.list();
        logger.info("number of sagApplicationList record: "+sagApplicationList.size());
        if(sagApplicationList.size()==0)
        {
            logger.info("no applications applied for the current year, retrieve last year record");
            logger.info("get last year record for the child if there is successful record");
            queryStr = new StringBuffer();
            queryStr.append( "SELECT sag FROM SAGApplication sag" );
            isWhereClauseAdded = false;
            if ( null != nric && !nric.isEmpty() ) {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE sag.memberNric = :nric" );
            }
            if ( null != lastFinancialYear && !lastFinancialYear.isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.financialYear = :financialYear" );
            }
            if ( null != childNric && !childNric.isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.childNric  = :childNric " );
            }

            queryStr.append( " AND sag.applicationStatus = '"+ ApplicationStatus.SUCCESSFUL.name() + "'");

            queryStr.append( " ORDER BY sag.dateOfSubmission, sag.referenceNumber " );
            queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );


            query = session.createQuery( queryStr.toString() );
            if ( null != nric && !nric.isEmpty() ) {
                query.setParameter( "nric", nric );
            }
            if ( null != lastFinancialYear && !lastFinancialYear.isEmpty() ) {
                query.setParameter( "financialYear", lastFinancialYear );
            }
            if ( null != childNric && !childNric.isEmpty() ) {
                query.setParameter( "childNric", childNric );
            }

            List< SAGApplication > sagApplicationListChildLastYear = ( List< SAGApplication > ) query.list();
            logger.info("query: "+queryStr.toString());
            if(sagApplicationListChildLastYear.size() > 0)
            {
                //if there are successful applications for the child in the previous financial year
                sagApplicationTemp = sagApplicationListChildLastYear.get(0);
                sagApplication.setChildNric(sagApplicationTemp.getChildNric());
                sagApplication.setChildName(sagApplicationTemp.getChildName());
                sagApplication.setChildDateOfBirth(sagApplicationTemp.getChildDateOfBirth());
                sagApplication.setChildEmail(sagApplicationTemp.getChildEmail());
                sagApplication.setChildCurrentSchool(sagApplicationTemp.getChildCurrentSchool());
                sagApplication.setChildMusicArtsSpecialDetails(sagApplicationTemp.getChildMusicArtsSpecialDetails());
                sagApplication.setChildCommendationDetails(sagApplicationTemp.getChildCommendationDetails());
                sagApplication.setChildOtherCurrentSchool(sagApplicationTemp.getChildOtherCurrentSchool());
                List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
                sagApplication.setSupportingDocuments(docList);
                //need to populate the family background info into the sagApplication
                List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
                for(int i=0; i<sagApplicationTemp.getChildFamBackgroundDetails().size(); i++)
                {

                    SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
                    SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getDateofBirth());
                    SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getGrossSalary());
                    SAGFamilyBackgroundTemp.setIdNo(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getIdNo());
                    SAGFamilyBackgroundTemp.setIdType(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getIdType());
                    SAGFamilyBackgroundTemp.setName(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getName());
                    SAGFamilyBackgroundTemp.setOccupation(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getOccupation());
                    SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getOtherRelationship());
                    SAGFamilyBackgroundTemp.setRelationship(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getRelationship());
                    SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationTemp.getChildFamBackgroundDetails().get(i).getSpecialAllowance());
                    childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
                }

                sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);

            }
            else
            {
                sagApplication = null;
            }


        }
        else
        {
            logger.info("existing applications for the current financial year. retrieve child info from previous year applications");
            logger.log( Level.INFO, "Search SAG Applications by awardType for member: " + nric + childNric );

            queryStr = new StringBuffer();
            queryStr.append( "SELECT sag FROM SAGApplication sag" );
            isWhereClauseAdded = false;

            if ( null != nric && !nric.isEmpty() ) {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE sag.memberNric = :nric" );
            }

            if ( null != childNric && !childNric.isEmpty() ) {
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.childNric = :childNric" );
            }

            queryStr.append( " AND sag.applicationStatus = '"+ ApplicationStatus.SUCCESSFUL.name() + "'");

            queryStr.append( " ORDER BY sag.referenceNumber" );
            queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );

            query = session.createQuery( queryStr.toString() );
            if ( null != nric && !nric.isEmpty() ) {
                query.setParameter( "nric", nric );
            }

            if ( null != childNric && !childNric.isEmpty() ) {
                query.setParameter( "childNric", childNric );
            }
            List< SAGApplication > sagApplicationListChild = ( List< SAGApplication > ) query.list();
            if(sagApplicationListChild.size()>0)
            {
                //sagApplication = sagApplicationListChild.get( 0 );
                sagApplication.setChildNric(sagApplicationListChild.get(0).getChildNric());
                sagApplication.setChildName(sagApplicationListChild.get(0).getChildName());
                sagApplication.setChildDateOfBirth(sagApplicationListChild.get(0).getChildDateOfBirth());
                sagApplication.setChildEmail(sagApplicationListChild.get(0).getChildEmail());
                sagApplication.setChildCurrentSchool(sagApplicationListChild.get(0).getChildCurrentSchool());
                sagApplication.setChildMusicArtsSpecialDetails(sagApplicationListChild.get(0).getChildMusicArtsSpecialDetails());
                sagApplication.setChildCommendationDetails(sagApplicationListChild.get(0).getChildCommendationDetails());
                sagApplication.setChildOtherCurrentSchool(sagApplicationListChild.get(0).getChildOtherCurrentSchool());
                List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
             /*for(int i=0; i<sagApplicationListChild.get(0).getSupportingDocuments().size(); i++)
             {
            	 SupportingDocument doc = sagApplicationListChild.get(0).getSupportingDocuments().get(i);
            	 doc.setFebId(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getFebId());
            	 doc.setBpmId(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getBpmId());
            	 doc.setDocumentName(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getDocumentName());
            	 doc.setDocumentType(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getDocumentType());
            	 doc.setOtherDocumentType(sagApplicationListChild.get(0).getSupportingDocuments().get(i).getOtherDocumentType());
            	 docList.add(doc);
             }*/
                sagApplication.setSupportingDocuments(docList);
                //need to populate the family background info into the sagApplication

                List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
                for(int i=0; i<sagApplicationList.get(0).getChildFamBackgroundDetails().size(); i++)
                {

                    SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
                    SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getDateofBirth());
                    SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getGrossSalary());
                    SAGFamilyBackgroundTemp.setIdNo(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdNo());
                    SAGFamilyBackgroundTemp.setIdType(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdType());
                    SAGFamilyBackgroundTemp.setName(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getName());
                    SAGFamilyBackgroundTemp.setOccupation(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOccupation());
                    SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOtherRelationship());
                    SAGFamilyBackgroundTemp.setRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getRelationship());
                    SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getSpecialAllowance());
                    childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
                }

                //sagApplication.setChildFamBackgroundDetails(null);
                sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);
                for(int j=0; j< sagApplication.getChildFamBackgroundDetails().size(); j++)
                {
                    logger.info("id no: "+sagApplication.getChildFamBackgroundDetails().get(j).getIdNo());
                    logger.info("name: "+sagApplication.getChildFamBackgroundDetails().get(j).getName());
                    logger.info("relationship: "+sagApplication.getChildFamBackgroundDetails().get(j).getRelationship());
                    logger.info("gross salary: "+sagApplication.getChildFamBackgroundDetails().get(j).getGrossSalary());
                }

            }
            else
            {
                //sagApplication.setChildNric("");
                //sagApplication.setChildName("");
                //sagApplication.setChildDateOfBirth(null);
                //sagApplication.setChildEmail("");
                //sagApplication.setChildCurrentSchool("");
                //sagApplication.setChildMusicArtsSpecialDetails(sagApplicationListChild.get(0).getChildMusicArtsSpecialDetails());
                // sagApplication.setChildCommendationDetails(sagApplicationListChild.get(0).getChildCommendationDetails());
                //sagApplication.setChildOtherCurrentSchool(sagApplicationListChild.get(0).getChildOtherCurrentSchool());
                //List<SupportingDocument> docList = new ArrayList<SupportingDocument>();
                //sagApplication.setSupportingDocuments(docList);
                //need to populate the family background info into the sagApplication
                List<SAGFamilyBackground> childFamBackgroundDetails = new ArrayList<SAGFamilyBackground>();
                for(int i=0; i<sagApplicationList.get(0).getChildFamBackgroundDetails().size(); i++)
                {

                    SAGFamilyBackground SAGFamilyBackgroundTemp = new SAGFamilyBackground();
                    SAGFamilyBackgroundTemp.setDateofBirth(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getDateofBirth());
                    SAGFamilyBackgroundTemp.setGrossSalary(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getGrossSalary());
                    SAGFamilyBackgroundTemp.setIdNo(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdNo());
                    SAGFamilyBackgroundTemp.setIdType(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getIdType());
                    SAGFamilyBackgroundTemp.setName(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getName());
                    SAGFamilyBackgroundTemp.setOccupation(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOccupation());
                    SAGFamilyBackgroundTemp.setOtherRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getOtherRelationship());
                    SAGFamilyBackgroundTemp.setRelationship(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getRelationship());
                    SAGFamilyBackgroundTemp.setSpecialAllowance(sagApplicationList.get(0).getChildFamBackgroundDetails().get(i).getSpecialAllowance());
                    childFamBackgroundDetails.add(SAGFamilyBackgroundTemp);
                }

                //sagApplication.setChildFamBackgroundDetails(null);
                sagApplication.setChildFamBackgroundDetails(childFamBackgroundDetails);
            }


        }
        return sagApplication;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplications( String nric, String childNric, String awardType, String financialYear, boolean isOrderAsc ) throws AccessDeniedException {

        logger.log( Level.INFO, "Search SAG Applications by awardType for member: " + nric + childNric );
        logger.log( Level.INFO, "Before searchSAGApplication");

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( null != nric ) {
            if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), nric ) ) {
                throw new AccessDeniedException( "Caller does not have access to data belong to " + nric );
            }
        }

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != nric && !nric.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE sag.memberNric = :nric" );
        }

        if ( null != awardType && !awardType.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.awardType = :awardType" );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
        }

        queryStr.append( " ORDER BY sag.referenceNumber" );
        queryStr.append( ( isOrderAsc ? " ASC" : " DESC" ) );

        Query query = session.createQuery( queryStr.toString() );
        if ( null != nric && !nric.isEmpty() ) {
            query.setParameter( "nric", nric );
        }
        if ( null != awardType && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }

        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }

        return ( List< SAGApplication > ) query.list();
    }


    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSimliarSAGApplication( String childNric, String financialYear, String referenceNumber ) throws AccessDeniedException {

        logger.log( Level.INFO, "Search Similiar SAG Applications by child NRIC: " + childNric );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        if ( null != financialYear && !financialYear.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.financialYear = :financialYear" );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
        }

        queryStr.append( " AND sag.referenceNumber != :referenceNumber ORDER BY sag.referenceNumber DESC" );

        Query query = session.createQuery( queryStr.toString() );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }

        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }

        if ( null != referenceNumber && !referenceNumber.isEmpty() ) {
            query.setParameter( "referenceNumber", referenceNumber );
        }

        return ( List< SAGApplication > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplicationsByChildNricAndEduLevel( String childNric, String awardType, String childNewEduLevel, String childHighestEduLevel, String financialYear) {

        logger.log( Level.INFO, "Search Similiar SAG Applications by child NRIC and Education Level: " + childNric );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        logger.log(Level.INFO, childNric + awardType + childNewEduLevel + childHighestEduLevel + financialYear);

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( "SELECT sag FROM SAGApplication sag" );
        boolean isWhereClauseAdded = false;

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        logger.log(Level.INFO, financialYear+c.get(Calendar.YEAR));

        if ( null != childNric && !childNric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND" );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE" );
            }
            queryStr.append( " sag.childNric = :childNric" );
            queryStr.append( " AND sag.awardType = :awardType" );


            if(awardType.equals("SA")){
                logger.log(Level.INFO, "SA");
                if( null != childNewEduLevel && !childNewEduLevel.isEmpty() ){
                    logger.log(Level.INFO, "SA child new edu lvl");
//	        		 if ( isWhereClauseAdded ) {
//	                     queryStr.append( " AND" );
//	                 }
//	                 else {
//	                     isWhereClauseAdded = true;
//	                     queryStr.append( " WHERE" );
//	                 }
                    queryStr.append( " AND sag.childNewEduLevel = :childNewEduLevel" );
                }

            }

            if (awardType.equals("SAA")){
                logger.log(Level.INFO, "SAA");
                if( null != childHighestEduLevel && !childHighestEduLevel.isEmpty() ){
                    logger.log(Level.INFO, "SAA child highest edu lvl");
//	       		 if ( isWhereClauseAdded ) {
//	                    queryStr.append( " AND" );
//	                }
//	                else {
//	                    isWhereClauseAdded = true;
//	                    queryStr.append( " WHERE" );
//	                }
                    queryStr.append( " AND sag.childHighestEduLevel = :childHighestEduLevel" );

                }
            }

            queryStr.append( " AND sag.applicationStatus = :applicationStatus" );



            if (null != financialYear && !financialYear.isEmpty()){

//	        	Date currentDate = new Date();
//	        	Calendar c = Calendar.getInstance();
//	        	c.setTime(currentDate);
//	        	logger.log(Level.INFO, financialYear+c.get(Calendar.YEAR));
//	        	if(Integer.parseInt(financialYear) != c.get(Calendar.YEAR)){
//	        		logger.log(Level.INFO, "not equals");
                if ( isWhereClauseAdded ) {
                    queryStr.append( " AND" );
                }
                else {
                    isWhereClauseAdded = true;
                    queryStr.append( " WHERE" );
                }
                queryStr.append( " sag.financialYear != :financialYear" );
//		        }
            }
        }

        logger.log(Level.INFO, queryStr.toString());

        Query query = session.createQuery( queryStr.toString() );

        if ( null != childNric && !childNric.isEmpty() ) {
            query.setParameter( "childNric", childNric );
        }

        if ( null != awardType && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }

        if(awardType.equals("SA")){
            if ( null != childNewEduLevel && !childNewEduLevel.isEmpty() ) {
                query.setParameter( "childNewEduLevel", childNewEduLevel );
            }
        }

        if (awardType.equals("SAA")){
            if ( null != childHighestEduLevel && !childHighestEduLevel.isEmpty() ) {
                query.setParameter( "childHighestEduLevel", childHighestEduLevel );
            }
        }

        query.setParameter( "applicationStatus", ApplicationStatus.SUCCESSFUL );

//        Date currentDate = new Date();
//    	Calendar c = Calendar.getInstance();
//    	c.setTime(currentDate);
//    	logger.log(Level.INFO, financialYear+c.get(Calendar.YEAR));
//    	if(Integer.parseInt(financialYear) != c.get(Calendar.YEAR)){
        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }
//    	}
        return ( List< SAGApplication > ) query.list();

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplicationsByReferenceNumber( List< String > referenceNumberList ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( referenceNumberList != null && !referenceNumberList.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGApplication.class );

            criteria.add( Restrictions.in( "referenceNumber", referenceNumberList ) );

            return ( List< SAGApplication > ) criteria.list();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplicationsByFinancialYear( String financialYear ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( financialYear != null && !financialYear.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGApplication.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
            criteria.addOrder( Order.asc( "referenceNumber" ) );

            return ( List< SAGApplication > ) criteria.list();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSAGApplicationsBySubmission( String nric, String financialYear ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( nric != null && !nric.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGApplication.class );

            criteria.add( Restrictions.eq( "submittedBy", nric ) );

            if ( null != financialYear && !financialYear.isEmpty() ) {
                criteria.add( Restrictions.eq( "financialYear", financialYear ) );
            }

            criteria.addOrder( Order.asc( "referenceNumber" ) );

            return ( List< SAGApplication > ) criteria.list();
        }

        return null;
    }

    /**
     * Get SAG Application by Reference Number.
     *
     * @param referenceNumber
     * @return
     * @throws AccessDeniedException
     */
    public SAGApplication getSAGApplication( String referenceNumber ) throws AccessDeniedException {

        logger.log( Level.INFO, "Get SAG Application for referenceNumber: " + Util.replaceNewLine( referenceNumber ) );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGApplication sagApplication = ( SAGApplication ) session.get( SAGApplication.class, referenceNumber, new LockOptions( LockMode.PESSIMISTIC_READ ) );

        if ( sagApplication != null ) {
            logger.log( Level.INFO, "sagApplication -- memberNric = " + Util.replaceNewLine( sagApplication.getMemberNric() ) );

            if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), sagApplication.getMemberNric() ) ) {
                throw new AccessDeniedException( "Caller does not have access to data belong to " + sagApplication.getMemberNric() );
            }
        }

        return sagApplication;
    }

    public void saveSAGApplication( SAGApplication sagApplication, String requestor, boolean isNewSave ) throws AccessDeniedException {
        if ( isNewSave ) {
            addSAGApplication( sagApplication, requestor );
        }
        else {
            updateSAGApplication( sagApplication, requestor );
        }

    }

    public void addSAGApplication( SAGApplication sagApplication, String requestor ) throws AccessDeniedException {
        SessionFactoryUtil.setUser( requestor );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        logger.log( Level.INFO, "Add New SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
        sagApplication.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), sagApplication.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller does not have access to data belong to " + sagApplication.getMemberNric() );
        }

        session.save( sagApplication );

        session.flush();
    }

    public void updateSAGApplication( SAGApplication sagApplication, String requestor ) throws AccessDeniedException {
        SessionFactoryUtil.setUser( requestor );
        for(int j=0; j<sagApplication.getChildFamBackgroundDetails().size(); j++)
        {
            logger.info("Family Background name: "+sagApplication.getChildFamBackgroundDetails().get(j).getName());
            logger.info("Family Background salary: "+sagApplication.getChildFamBackgroundDetails().get(j).getGrossSalary());
            logger.info("Family Background id: "+sagApplication.getChildFamBackgroundDetails().get(j).getIdNo());
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        logger.log( Level.INFO, "Add New SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
        sagApplication.preSave();

        if ( !DataAccessCheck.canAccess( SecurityInfo.createInstance(), sagApplication.getMemberNric() ) ) {
            throw new AccessDeniedException( "Caller does not have access to data belong to " + sagApplication.getMemberNric() );
        }

        session.get( SAGApplication.class, sagApplication.getReferenceNumber(), new LockOptions( LockMode.PESSIMISTIC_WRITE ) );

        session.merge( sagApplication );

        session.flush();
    }

    public void batchUpdateSAGApplication( List< SAGApplication > sagApplicationList, String requestor, int batchSize ) throws AccessDeniedException {
        SessionFactoryUtil.setUser( requestor );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        List< String > canAccessList = new ArrayList< String >();

        int count = 0;
        for ( SAGApplication sagApplication : sagApplicationList ) {
            logger.log( Level.INFO, "Add New SAGApplication with ReferenceNumber = " + Util.replaceNewLine( sagApplication.getReferenceNumber() ) );
            sagApplication.preSave();

            String memberNric = sagApplication.getMemberNric();
            if ( ( null == canAccessList || canAccessList.isEmpty() || !canAccessList.contains( memberNric ) ) && !DataAccessCheck.canAccess( SecurityInfo.createInstance(), memberNric ) ) {
                throw new AccessDeniedException( "Caller does not have access to data belong to " + sagApplication.getMemberNric() );
            }
            else {
                canAccessList.add( sagApplication.getMemberNric() );
                session.merge( sagApplication );
                count++;
                if ( count % batchSize == 0 ) {
                    session.flush();
                    session.clear();
                }
            }
        }
        if ( count % batchSize > 0 ) {
            session.flush();
            session.clear();
        }
    }

    // public List<SAGApplicationResult> searchSAGApplicationByCriteria(
    // SAGApplicationCriteria sagApplicationCriteria ) {
    // StringBuffer queryStr = new StringBuffer();
    //
    // Session session = SessionFactoryUtil.getInstance().getCurrentSession();
    //
    // queryStr.append( HQL_SEARCH_SAG_BY_CRITERIA );
    // buildQueryStringByCriteria( queryStr, sagApplicationCriteria );
    //
    // Query query = session.createQuery( queryStr.toString() );
    // setCriteriaParameter( query, sagApplicationCriteria );
    // query.setResultTransformer( Transformers
    // .aliasToBean( SAGApplicationResult.class ) );
    //
    // @SuppressWarnings("unchecked")
    // List<SAGApplicationResult> results = (List<SAGApplicationResult>) query
    // .list();
    //
    // if ( null != results ) {
    // logger.log( Level.INFO, "search result Size -- " + results.size() );
    // }
    //
    // return results;
    // }

    public List< SAGApplicationResult > searchSAGApplicationByCriteria( SAGApplicationCriteria sagApplicationCriteria, boolean isOrderByReferenceNumber ) {
        StringBuffer queryStr = new StringBuffer();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA );
        queryStr.append( SQL_SEARCH_SAG_TASK );
        queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SAGINPUTS_SUFFIX );
        queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SUBMMITED_BY_SUFFIX );
        queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_JOIN_SUFFIX );

        buildQueryStringByCriteria( queryStr, sagApplicationCriteria, isOrderByReferenceNumber );

        logger.log( Level.INFO, "sqlQuery -- " + queryStr.toString() );
        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        setCriteriaParameter( sqlQuery, sagApplicationCriteria );
        addScalarsForSearchSAGByCriteria( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationResult.class ) );

        @SuppressWarnings( "unchecked" )
        List< SAGApplicationResult > results = ( List< SAGApplicationResult > ) sqlQuery.list();

        if ( null != results ) {
            logger.log( Level.INFO, "search result Size -- " + results.size() );
        }

        return results;
    }

    private SQLQuery addScalarsForSearchSAGByCriteria( SQLQuery sqlQuery ) {
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "awardType" );
        sqlQuery.addScalar( "applicantName" );
        sqlQuery.addScalar( "applicantId" );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "submittedByName" );
        sqlQuery.addScalar( "officerAction" );
        sqlQuery.addScalar( "childNewEduLevel" );
        sqlQuery.addScalar( "childHighestEduLevel" );
        sqlQuery.addScalar( "listOrder", StandardBasicTypes.INTEGER );

        sqlQuery.addScalar( "submittedByPaymentAdviceEmail" );
        sqlQuery.addScalar( "submittedByPrefEmail" );
        sqlQuery.addScalar( "submittedByWorkEmail" );
        sqlQuery.addScalar( "submittedByPrefContactNumber" );
        sqlQuery.addScalar( "sequenceNumber" );
        sqlQuery.addScalar( "childPsleScore" );
        sqlQuery.addScalar( "childCgpa", StandardBasicTypes.DOUBLE );

        Properties contactModeParams = new Properties();
        contactModeParams.put( "enumClass", "com.stee.spfcore.model.personnel.ContactMode" );
        contactModeParams.put( "type", "12" );

        sqlQuery.addScalar( "submittedByPrefContactMode", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, contactModeParams ) );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );

        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );
        sqlQuery.addScalar( "taskId" );
        return sqlQuery;
    }

    private StringBuffer buildQueryStringByCriteria( StringBuffer queryStr, SAGApplicationCriteria sagApplicationCriteria, boolean isOrderByReferenceNumber ) {

        boolean isJoinClosed = false;
        boolean isFinalSuffixAdded = false;

        if ( null != sagApplicationCriteria ) {

            if ( null != sagApplicationCriteria.getApprovalRecordStatus() && !sagApplicationCriteria.getApprovalRecordStatus().isEmpty() ) {
                queryStr.append( " AND most_recent_app_record.OFFICER_ACTION = :officerAction" );
            }

            queryStr.append( " )" );
            isJoinClosed = true;
            queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_SUFFIX );
            isFinalSuffixAdded = true;

            if ( null != sagApplicationCriteria.getReferenceNumber() && !sagApplicationCriteria.getReferenceNumber().isEmpty() ) {
                queryStr.append( " AND sag.reference_Number LIKE :referenceNumber" );
            }

            if ( null != sagApplicationCriteria.getApplicantId() && !sagApplicationCriteria.getApplicantId().isEmpty() ) {
                queryStr.append( " AND personal.nric LIKE :applicantId" );
            }

            if ( null != sagApplicationCriteria.getApplicantName() && !sagApplicationCriteria.getApplicantName().isEmpty() ) {
                queryStr.append( " AND personal.name LIKE :applicantName" );
            }

            if ( null != sagApplicationCriteria.getChildName() && !sagApplicationCriteria.getChildName().isEmpty() ) {
                queryStr.append( " AND sag.child_Name LIKE :childName" );
            }

            if ( null != sagApplicationCriteria.getAwardType() && !sagApplicationCriteria.getAwardType().isEmpty() ) {
                queryStr.append( " AND sag.award_Type IN (:awardTypeList)" );
            }

            if ( null != sagApplicationCriteria.getFinancialYear() && !sagApplicationCriteria.getFinancialYear().isEmpty() ) {
                queryStr.append( " AND sag.financial_Year = :financialYear" );
            }

            if ( ( null != sagApplicationCriteria.getRecommendOrSuccessfulApplications() && !sagApplicationCriteria.getRecommendOrSuccessfulApplications().isEmpty() )
                    || ( null != sagApplicationCriteria.getApplicationStatus() && !sagApplicationCriteria.getApplicationStatus().isEmpty() ) ) {
                // Modifying applicationStatus criteria clause to IN.
                queryStr.append( " AND sag.application_Status in (:applicationStatusList)" );
            }
        }

        // if sagApplicationCriteria is null, then terminate sql properly.
        if ( !isJoinClosed ) {
            queryStr.append( " )" );
        }

        if ( !isFinalSuffixAdded ) {
            queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_SUFFIX );
        }

        if ( isOrderByReferenceNumber ) {
            queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_ORDER_BY_REFERENCE_NUMBER_SUFFIX );
        }
        else {
            queryStr.append( SQL_SEARCH_SAG_BY_CRITERIA_ORDER_BY_SUFFIX );
        }
        return queryStr;
    }

    private Query setCriteriaParameter( Query query, SAGApplicationCriteria sagApplicationCriteria ) {

        if ( null != sagApplicationCriteria ) {

            if ( null != sagApplicationCriteria.getApprovalRecordStatus() && !sagApplicationCriteria.getApprovalRecordStatus().isEmpty() ) {
                query.setParameter( "officerAction", sagApplicationCriteria.getApprovalRecordStatus() );
            }
            if ( null != sagApplicationCriteria.getReferenceNumber() && !sagApplicationCriteria.getReferenceNumber().isEmpty() ) {
                query.setParameter( "referenceNumber", "%" + sagApplicationCriteria.getReferenceNumber() + "%" );
            }

            if ( null != sagApplicationCriteria.getApplicantId() && !sagApplicationCriteria.getApplicantId().isEmpty() ) {
                query.setParameter( "applicantId", "%" + sagApplicationCriteria.getApplicantId() + "%" );
            }

            if ( null != sagApplicationCriteria.getApplicantName() && !sagApplicationCriteria.getApplicantName().isEmpty() ) {
                query.setParameter( "applicantName", "%" + sagApplicationCriteria.getApplicantName() + "%" );
            }

            if ( null != sagApplicationCriteria.getChildName() && !sagApplicationCriteria.getChildName().isEmpty() ) {
                query.setParameter( "childName", "%" + sagApplicationCriteria.getChildName() + "%" );
            }

            if ( null != sagApplicationCriteria.getAwardType() && !sagApplicationCriteria.getAwardType().isEmpty() ) {
                List< String > awardTypeList = new ArrayList< String >();
                if ( sagApplicationCriteria.getAwardType().contains( "," ) ) {
                    awardTypeList = Arrays.asList( sagApplicationCriteria.getAwardType().split( "," ) );
                }
                else {
                    awardTypeList.add( sagApplicationCriteria.getAwardType() );
                }
                query.setParameterList( "awardTypeList", awardTypeList );
            }

            if ( null != sagApplicationCriteria.getFinancialYear() && !sagApplicationCriteria.getFinancialYear().isEmpty() ) {
                query.setParameter( "financialYear", sagApplicationCriteria.getFinancialYear() );
            }

            if ( ( null != sagApplicationCriteria.getRecommendOrSuccessfulApplications() && !sagApplicationCriteria.getRecommendOrSuccessfulApplications().isEmpty() )
                    || ( null != sagApplicationCriteria.getApplicationStatus() && !sagApplicationCriteria.getApplicationStatus().isEmpty() ) ) {
                List< String > applicationStatusNameList = new ArrayList< String >();

                if ( null != sagApplicationCriteria.getRecommendOrSuccessfulApplications() && !sagApplicationCriteria.getRecommendOrSuccessfulApplications().isEmpty() ) {
                    boolean reqdRecommended = sagApplicationCriteria.getRecommendOrSuccessfulApplications().contains( ApplicationStatus.RECOMMENDED.toString() );
                    boolean reqdSuccessful = sagApplicationCriteria.getRecommendOrSuccessfulApplications().contains( ApplicationStatus.SUCCESSFUL.toString() );
                    if ( reqdSuccessful ) {
                        applicationStatusNameList.add( ApplicationStatus.SUCCESSFUL.name() );
                        applicationStatusNameList.add( ApplicationStatus.UNSUCCESSFUL.name() );
                    }

                    if ( reqdRecommended ) {
                        applicationStatusNameList.add( ApplicationStatus.RECOMMENDED.name() );
                        applicationStatusNameList.add( ApplicationStatus.NOT_RECOMMENDED.name() );
                    }

                }
                else if ( null != sagApplicationCriteria.getApplicationStatus() && !sagApplicationCriteria.getApplicationStatus().isEmpty() ) {
                    applicationStatusNameList = getApplicationStatusNamesList( sagApplicationCriteria.getApplicationStatus() );
                }

                query.setParameterList( "applicationStatusList", applicationStatusNameList );
            }
        }
        return query;
    }

    private List< String > getApplicationStatusNamesList( String applicationStatus ) {
        if ( null != applicationStatus ) {
            List< String > applicationStatusNamesList = new ArrayList< String >();
            if ( applicationStatus.contains( "," ) ) {
                List< String > applicationStatusValueList = Arrays.asList( applicationStatus.split( "," ) );
                if ( null != applicationStatusValueList && !applicationStatusValueList.isEmpty() ) {
                    for ( String eachValue : applicationStatusValueList ) {
                        applicationStatusNamesList.add( ApplicationStatus.get( eachValue ).name() );
                    }
                }
            }
            else {
                applicationStatusNamesList.add( ApplicationStatus.get( applicationStatus ).name() );
            }
            logger.log( Level.INFO, applicationStatusNamesList.toString() );
            return applicationStatusNamesList;

        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGInputs > getListOfSAGInputs( String awardType ) {
        StringBuffer queryStr = new StringBuffer();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        queryStr.append( "Select si from SAGInputs si" );

        if ( awardType != null && !awardType.isEmpty() ) {
            queryStr.append( " where si.awardType = :awardType" );
        }

        queryStr.append( " order by si.sagInputType asc, si.order asc" );

        Query query = session.createQuery( queryStr.toString() );
        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }
        return ( List< SAGInputs > ) query.list();

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGInputs > getListOfSAGInputByType( String awardType, SAGInputType inputType ) {
        StringBuffer queryStr = new StringBuffer();

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        queryStr.append( "SELECT si FROM SAGInputs si" );

        boolean isWhereClauseAdded = false;
        if ( awardType != null && !awardType.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE si.awardType = :awardType" );
        }

        if ( inputType != null ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND " );
            }
            else {
                queryStr.append( " WHERE " );
            }
            queryStr.append( "si.sagInputType = :inputType" );
        }

        queryStr.append( " ORDER BY si.order ASC" );

        Query query = session.createQuery( queryStr.toString() );
        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "awardType", awardType );
        }

        if ( awardType != null && !awardType.isEmpty() ) {
            query.setParameter( "inputType", inputType );
        }
        return ( List< SAGInputs > ) query.list();

    }

    public SAGInputs getSAGInput( String awardType, SAGInputType inputType, String inputId ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( awardType != null && inputId != null && inputType != null ) {
            Criteria criteria = session.createCriteria( SAGInputs.class );

            criteria.add( Restrictions.eq( "awardType", awardType ) ).add( Restrictions.eq( "inputId", inputId ) ).add( Restrictions.eq( "sagInputType", inputType ) );

            return ( SAGInputs ) criteria.uniqueResult();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGSubInputs > getSubInputListByCriteria( String awardType, String parentId, SAGInputType parentType ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( awardType != null && parentId != null && parentType != null ) {
            Criteria criteria = session.createCriteria( SAGSubInputs.class );

            criteria.add( Restrictions.eq( "awardType", awardType ) ).add( Restrictions.eq( "parentInputId", parentId ) ).add( Restrictions.eq( "parentInputType", parentType ) ).addOrder( Order.asc( "order" ) );

            return ( List< SAGSubInputs > ) criteria.list();
        }

        return null;
    }

    public SAGConfigSetup getConfigSetup( String id ) {
        logger.log( Level.INFO, "Get SAG Config Setup by Id" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGConfigSetup sagConfigSetup = ( SAGConfigSetup ) session.get( SAGConfigSetup.class, id );

        if ( null != sagConfigSetup ) {
            logger.log( Level.INFO, "sagConfigSetup year = " + Util.replaceNewLine( sagConfigSetup.getFinancialYear() ) );
        }

        return sagConfigSetup;
    }

    public SAGConfigSetup getConfigSetup( String financialYear, SAGDateConfigType configType ) {
        logger.log( Level.INFO, "Get SAG Config Setup by financialYear and ConfigType" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( null != financialYear && null != configType ) {
            Criteria criteria = session.createCriteria( SAGConfigSetup.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );

            criteria.add( Restrictions.disjunction().add( Restrictions.eq( "configType", configType ) ) );

            return ( SAGConfigSetup ) criteria.uniqueResult();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGConfigSetup > searchConfigSetup( String financialYear ) {
        logger.log( Level.INFO, "Search SAG Config Setup" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( null != financialYear ) {
            Criteria criteria = session.createCriteria( SAGConfigSetup.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );

            return ( List< SAGConfigSetup > ) criteria.list();
        }

        return null;
    }

    public void saveSAGConfigSetup( SAGConfigSetup sagConfigSetup, String requestor ) {
        logger.log( Level.INFO, "save SAG Config Setup" );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagConfigSetup );
        session.flush();
    }

    public SAGPrivileges getSAGPrivilege( String id ) {
        logger.log( Level.INFO, "Get SAG Privilege by Id" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGPrivileges sagPrivilege = ( SAGPrivileges ) session.get( SAGPrivileges.class, id );

        if ( null != sagPrivilege ) {
            logger.log( Level.INFO, "SAGPrivileges year = " + Util.replaceNewLine( sagPrivilege.getFinancialYear() ) + " --- " + Util.replaceNewLine( sagPrivilege.getMemberNric() ) );
        }

        return sagPrivilege;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGPrivileges > searchSAGPrivileges( String financialYear, String memberNric ) {
        logger.log( Level.INFO, "Search SAG Privileges" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGPrivileges.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != memberNric && !memberNric.isEmpty() ) {
            criteria.add( Restrictions.eq( "memberNric", memberNric ) );
        }
        return ( List< SAGPrivileges > ) criteria.list();
    }

    public SAGPrivilegeUserDetail getSAGPrivilegeUserDetail( String id ) {
        logger.log( Level.INFO, "Get SAG Privilege by Id" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( HQL_GET_SEARCH_PRIVILEGE_USER_DETAILS );
        queryStr.append( " AND priv.id = :id" );

        Query query = session.createQuery( queryStr.toString() );
        query.setParameter( "codeType", CodeType.UNIT_DEPARTMENT );
        query.setParameter( "id", "id" );
        query.setResultTransformer( Transformers.aliasToBean( SAGPrivilegeUserDetail.class ) );

        return ( SAGPrivilegeUserDetail ) query.uniqueResult();

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGPrivilegeUserDetail > searchSAGPrivilegeUserDetail( String financialYear, String memberNric ) {
        logger.log( Level.INFO, "Search SAG Privileges" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( HQL_GET_SEARCH_PRIVILEGE_USER_DETAILS );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            queryStr.append( " AND priv.financialYear = :financialYear" );
        }

        if ( null != memberNric && !memberNric.isEmpty() ) {
            queryStr.append( " AND priv.memberNric = :memberNric" );
        }

        Query query = session.createQuery( queryStr.toString() );

        query.setParameter( "codeType", CodeType.UNIT_DEPARTMENT );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            query.setParameter( "financialYear", financialYear );
        }

        if ( null != memberNric && !memberNric.isEmpty() ) {
            query.setParameter( "memberNric", memberNric );
        }

        query.setResultTransformer( Transformers.aliasToBean( SAGPrivilegeUserDetail.class ) );

        return ( List< SAGPrivilegeUserDetail > ) query.list();
    }

    public void saveSAGPrivilege( SAGPrivileges sagPrivilege, String requestor ) {
        logger.log( Level.INFO, " save SAG Privilege " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagPrivilege );
        session.flush();
    }

    public void deletePrivileges( List< String > privilegesIdList, String requestor ) {
        logger.log( Level.INFO, " Delete SAG Donation records " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        String queryStr = HQL_DELETE_PRIVILEGES_BY_ID;
        Query query = session.createQuery( queryStr );

        query.setParameterList( "deleteList", privilegesIdList );
        query.executeUpdate();
    }

    public SAGAwardQuantum getAwardQuantum( String id ) {
        logger.log( Level.INFO, "Get SAG Award Quantum" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGAwardQuantum sagAwardQuantum = ( SAGAwardQuantum ) session.get( SAGAwardQuantum.class, id );

        if ( null != sagAwardQuantum ) {
            logger.log( Level.INFO,
                    "SAG AwardQuantum = " + Util.replaceNewLine( sagAwardQuantum.getFinancialYear() ) + " --- " + Util.replaceNewLine( sagAwardQuantum.getAwardType() ) + " ---- " + Util.replaceNewLine( sagAwardQuantum.getSubType() ) );
        }

        return sagAwardQuantum;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGAwardQuantum > searchAwardQuantum( String financialYear, String awardType, String subType ) {
        logger.log( Level.INFO, "Search SAG Award Quantum" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGAwardQuantum.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != awardType && !awardType.isEmpty() ) {
            criteria.add( Restrictions.eq( "awardType", awardType ) );
        }

        if ( null != subType && !subType.isEmpty() ) {
            criteria.add( Restrictions.eq( "subType", subType ) );
        }
        return ( List< SAGAwardQuantum > ) criteria.list();
    }

    public void saveAwardQuantum( SAGAwardQuantum sagAwardQuantum, String requestor ) {
        logger.log( Level.INFO, " save SAG Award Quantum " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagAwardQuantum );
        session.flush();
    }

    public SAGEventDetail getSAGEventDetail( String id ) {
        logger.log( Level.INFO, "Get SAG Event Details" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGEventDetail sagEventDetail = ( SAGEventDetail ) session.get( SAGEventDetail.class, id );

        if ( null != sagEventDetail ) {
            logger.log( Level.INFO, "SAG Event Detail = " + Util.replaceNewLine( sagEventDetail.getFinancialYear() ) );
        }

        return sagEventDetail;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGEventDetail > searchEventDetail( String eventId, String financialYear ) {
        logger.log( Level.INFO, "Search SAG Event Detail" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGEventDetail.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != eventId && !eventId.isEmpty() ) {
            criteria.add( Restrictions.eq( "id", eventId ) );
        }

        return ( List< SAGEventDetail > ) criteria.list();
    }

    public void saveSAGEventDetail( SAGEventDetail sagEventDetail, String requestor ) {
        logger.log( Level.INFO, " save SAG Event detail " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagEventDetail );
        session.flush();
    }

    public SAGEventRsvp getEventRsvp( String id ) {
        logger.log( Level.INFO, "Get SAG Event Rsvp" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGEventRsvp sagEventRsvp = ( SAGEventRsvp ) session.get( SAGEventRsvp.class, id );

        if ( null != sagEventRsvp ) {
            logger.log( Level.INFO, "SAG Event Rsvp = " + Util.replaceNewLine( sagEventRsvp.getFinancialYear() ) );
        }

        return sagEventRsvp;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGEventRsvp > searchEventRsvp( String financialYear, String eventId, String refSeqNumber, String attendeeName, String attendeeId ) {
        logger.log( Level.INFO, "Search SAG Event Rsvp" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGEventRsvp.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != eventId && !eventId.isEmpty() ) {
            criteria.add( Restrictions.eq( "eventId", eventId ) );
        }

        if ( null != refSeqNumber && !refSeqNumber.isEmpty() ) {
            criteria.add( Restrictions.eq( "sequenceNumberReference", refSeqNumber ) );
        }

        if ( null != attendeeName && !attendeeName.isEmpty() ) {
            criteria.add( Restrictions.ilike( "attendeeName", attendeeName, MatchMode.ANYWHERE ) );
        }

        if ( null != attendeeId && !attendeeId.isEmpty() ) {
            criteria.add( Restrictions.eq( "attendeeId", attendeeId ) );
        }

        criteria.addOrder( Order.asc( "order" ) );

        return ( List< SAGEventRsvp > ) criteria.list();
    }

    public void saveEventRsvp( SAGEventRsvp sagEventRsvp, String requestor ) {
        logger.log( Level.INFO, " save SAG Event Rsvp " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagEventRsvp );
        session.flush();
    }

    public void deleteEventRsvp( List< String > rsvpIdList, String requestor ) {
        logger.log( Level.INFO, " Delete SAG Event Rsvp " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        String queryStr = HQL_DELETE_EVENT_RSVP_BY_ID;
        Query query = session.createQuery( queryStr );

        query.setParameterList( "deleteList", rsvpIdList );
        query.executeUpdate();
    }

    public SAGDonation getDonation( String id ) {
        logger.log( Level.INFO, "Get SAG Event Rsvp" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGDonation sagDonation = ( SAGDonation ) session.get( SAGDonation.class, id );

        if ( null != sagDonation ) {
            logger.log( Level.INFO, "SAG Donation = " + Util.replaceNewLine( sagDonation.getFinancialYear() ) );
        }

        return sagDonation;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGDonation > searchDonationList( String financialYear, String organization ) {
        logger.log( Level.INFO, "Search SAG Donations" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGDonation.class );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            criteria.add( Restrictions.eq( "financialYear", financialYear ) );
        }

        if ( null != organization && !organization.isEmpty() ) {
            criteria.add( Restrictions.ilike( "organization", organization, MatchMode.ANYWHERE ) );
        }

        return ( List< SAGDonation > ) criteria.list();
    }

    public void saveDonation( SAGDonation sagDonation, String requestor ) {
        logger.log( Level.INFO, " save SAG Donation " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagDonation );
        session.flush();
    }

    public void deleteDonations( List< String > donationsIdList, String requestor ) {
        logger.log( Level.INFO, " Delete SAG Donation records " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        String queryStr = HQL_DELETE_DONATIONS_BY_ID;
        Query query = session.createQuery( queryStr );

        query.setParameterList( "deleteList", donationsIdList );
        query.executeUpdate();
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplicationChildDetail > searchChildDetails( String financialYear ) {
        logger.log( Level.INFO, "get Child Details  by FinancialYear from SAGApplication having allowChildEmailForPRD set to true" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        String queryStr = HQL_SEARCH_CHILD_DETAILS;
        Query query = session.createQuery( queryStr );

        query.setParameter( "financialYear", financialYear );
        query.setResultTransformer( Transformers.aliasToBean( SAGApplicationChildDetail.class ) );

        return ( List< SAGApplicationChildDetail > ) query.list();
    }

    public boolean isApplicationExists( String referenceNumber ) {
        logger.log( Level.INFO, "Check if SAGApplication Exists already by ReferenceNumber" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        String queryStr = HQL_IS_SAG_APPLICATION_EXISTS;
        Query query = session.createQuery( queryStr );

        query.setParameter( "referenceNumber", referenceNumber );

        int result = ( ( Number ) query.uniqueResult() ).intValue();

        if ( result > 0 ) {
            logger.log( Level.INFO, "application Exists --- " + result );
            return true;
        }

        return false;

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGPrivilegePersonalDetails > searchPersonalForManagePrivileges( String name, String nric, String departmentId ) {
        logger.log( Level.INFO, "Get SAGPrivilegePersonalDetails" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( HQL_GET_PERSONAL_DETAILS_FOR_ADD_PRIVILEGE );
        boolean isWhereClauseAdded = false;
        if ( null != name && !name.isEmpty() ) {
            isWhereClauseAdded = true;
            queryStr.append( " WHERE personal.name LIKE :name" );
        }

        if ( null != nric && !nric.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND " );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE " );
            }
            queryStr.append( "personal.nric LIKE :nric" );
        }

        if ( null != departmentId && !departmentId.isEmpty() ) {
            if ( isWhereClauseAdded ) {
                queryStr.append( " AND " );
            }
            else {
                isWhereClauseAdded = true;
                queryStr.append( " WHERE " );
            }
            queryStr.append( "personal.employment.organisationOrDepartment = :departmentId" );
        }

        Query query = session.createQuery( queryStr.toString() );

        if ( null != name && !name.isEmpty() ) {
            query.setParameter( "name", "%" + name + "%" );
        }

        if ( null != nric && !nric.isEmpty() ) {
            query.setParameter( "nric", "%" + nric + "%" );
        }

        if ( null != departmentId && !departmentId.isEmpty() ) {
            query.setParameter( "departmentId", departmentId );
        }
        query.setResultTransformer( Transformers.aliasToBean( SAGPrivilegePersonalDetails.class ) );

        return ( List< SAGPrivilegePersonalDetails > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplicationDetailByApprovals > searchSAGApplicationsByApprovalCriteria( SAGApplicationApprovalsCriteria criteria ) {
        logger.log( Level.INFO, "Get SAGPrivilegePersonalDetails" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( SQL_GET_SAG_APPLICATION_BY_APPROVALS );
        buildQueryStringByCriteria( queryStr, criteria );
        queryStr.append( SQL_GET_SAG_APPLICATION_BY_APPROVALS_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        logger.info(queryStr.toString());
        setCriteriaParameter( sqlQuery, criteria );
        addScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationDetailByApprovals.class ) );

        return ( List< SAGApplicationDetailByApprovals > ) sqlQuery.list();
    }

    private StringBuffer buildQueryStringByCriteria( StringBuffer queryStr, SAGApplicationApprovalsCriteria criteria ) {

        if ( null != criteria ) {
            // Append Search by Officer Action Clause.
            if ( criteria.getOfficerAction() != null && !criteria.getOfficerAction().isEmpty() ) {
                queryStr.append( " AND most_recent_app_record.OFFICER_ACTION IN (:officerActionParamList)" );
            }

            // Append Search by Officer Level clause.
            if ( criteria.getOfficerLevel() != null && !criteria.getOfficerLevel().isEmpty() ) {
                queryStr.append( " AND most_recent_app_record.OFFICER_LEVEL IN (:officerLevelParamList)" );
            }

            // Append Search by Completion Date range clause.
            if ( criteria.getSearchStartDate() != null && criteria.getSearchEndDate() != null ) {
                queryStr.append( " AND most_recent_app_record.DATE_OF_COMPLETION BETWEEN " + ":searchCompletionStartDate AND :searchCompletionEndDate" );
            }
        }
        return queryStr;
    }

    private Query setCriteriaParameter( Query query, SAGApplicationApprovalsCriteria criteria ) {
        if ( null != criteria ) {
            // Set Officer Action Search Param List
            if ( criteria.getOfficerAction() != null && !criteria.getOfficerAction().isEmpty() ) {
                List< String > officerActionParamList = new ArrayList< String >();
                if ( criteria.getOfficerAction().contains( "," ) ) {
                    officerActionParamList = Arrays.asList( criteria.getOfficerAction().split( "," ) );
                }
                else {
                    officerActionParamList.add( criteria.getOfficerAction() );
                }
                query.setParameterList( "officerActionParamList", officerActionParamList );
            }

            // Set Officer Level Search Param List.
            if ( criteria.getOfficerLevel() != null && !criteria.getOfficerLevel().isEmpty() ) {
                List< String > officerLevelParamList = new ArrayList< String >();
                if ( criteria.getOfficerLevel().contains( "," ) ) {
                    officerLevelParamList = Arrays.asList( criteria.getOfficerLevel().split( "," ) );
                }
                else {
                    officerLevelParamList.add( criteria.getOfficerLevel() );
                }
                query.setParameterList( "officerLevelParamList", officerLevelParamList );
            }

            // Set Completion Date Search Params
            if ( criteria.getSearchStartDate() != null && criteria.getSearchEndDate() != null ) {
                query.setParameter( "searchCompletionStartDate", criteria.getSearchStartDate() );
                query.setParameter( "searchCompletionEndDate", criteria.getSearchEndDate() );
            }
        }

        return query;
    }

    private Query addScalars( SQLQuery sqlQuery ) {

        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "awardTypeDescription" );
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "submittedBy" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );
        sqlQuery.addScalar( "childNric" );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "educationLevel" );
        sqlQuery.addScalar( "officerName" );

        Properties params = new Properties();
        params.put( "enumClass", "com.stee.spfcore.model.ApplicationStatus" );
        params.put( "type", "12" );

        sqlQuery.addScalar( "applicationStatus", new TypeLocatorImpl( new TypeResolver() ).custom( EnumType.class, params ) );
        return sqlQuery;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplicationsApprovedForAward > searchSAGApplicationsApprovedForAward( SAGApplicationCriteria criteria ) {
        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( SQL_GET_SAG_APPROVED_APPLICATIONS );
        buildQueryStringByApplicationsForAwardCriteria( queryStr, criteria );
        queryStr.append( SQL_GET_SAG_APPROVED_APPLICATIONS_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        setApplicationsForAwardCriteriaParameter( sqlQuery, criteria );
        addApplicationsForAwardScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationsApprovedForAward.class ) );

        return ( List< SAGApplicationsApprovedForAward > ) sqlQuery.list();
    }

    private StringBuffer buildQueryStringByApplicationsForAwardCriteria( StringBuffer queryStr, SAGApplicationCriteria criteria ) {
        if ( null != criteria ) {
            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                queryStr.append( " AND sag.AWARD_TYPE IN (:awardTypeList)" );
            }

            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                queryStr.append( " AND sag.FINANCIAL_YEAR = :financialYear" );
            }

            if ( null != criteria.getApplicationStatus() ) {
                queryStr.append( " AND sag.APPLICATION_STATUS IN (:applicationStatus)" );
            }

            if ( null != criteria.getChequeUpdatedDate() ) {
                queryStr.append( " AND sag.CHEQUE_UPDATED_DATE = :chequeUpdatedDate" );
            }
        }

        return queryStr;
    }

    private Query setApplicationsForAwardCriteriaParameter( Query query, SAGApplicationCriteria criteria ) {
        if ( null != criteria ) {

            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                List< String > awardTypeList = new ArrayList< String >();
                if ( criteria.getAwardType().contains( "," ) ) {
                    awardTypeList = Arrays.asList( criteria.getAwardType().split( "," ) );
                }
                else {
                    awardTypeList.add( criteria.getAwardType() );
                }
                logger.log( Level.INFO, "awardTypeList" + awardTypeList.toString() );
                query.setParameterList( "awardTypeList", awardTypeList );
            }

            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                logger.log( Level.INFO, "financialYear " + criteria.getFinancialYear() );
                query.setParameter( "financialYear", criteria.getFinancialYear() );
            }

            if ( null != criteria.getApplicationStatus() ) {
                logger.log( Level.INFO, "applicationStatus " + criteria.getApplicationStatus().toString() );
                query.setParameterList( "applicationStatus", getApplicationStatusNamesList( criteria.getApplicationStatus() ) );
            }

            if ( null != criteria.getChequeUpdatedDate() ) {
                query.setParameter( "chequeUpdatedDate", criteria.getChequeUpdatedDate() );

            }
        }
        return query;
    }

    private SQLQuery addApplicationsForAwardScalars( SQLQuery sqlQuery ) {
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "financialYear" );
        sqlQuery.addScalar( "awardType" );
        sqlQuery.addScalar( "awardTypeDescription" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );
        sqlQuery.addScalar( "childNric" );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "childCurrentSchoolId" );
        sqlQuery.addScalar( "childCurrentSchoolDesc" );
        sqlQuery.addScalar( "childHighestEduLevelId" );
        sqlQuery.addScalar( "childHighestEduLevelDesc" );
        sqlQuery.addScalar( "childNewEduLevelId" );
        sqlQuery.addScalar( "childNewEduLevelDesc" );
        sqlQuery.addScalar( "awardAmount", StandardBasicTypes.DOUBLE );
        sqlQuery.addScalar( "sequenceNumber" );

        sqlQuery.addScalar( "chequeUpdatedDate", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "chequeValueDate", StandardBasicTypes.DATE );

        return sqlQuery;
    }

    /**
     * Search SAG Applications for Audit. Return top 10 Percent of Records or
     * Max Top 100 records. ONLY for INTRANET side. (The query will work only
     * for MSSQL)
     *
     * @param financialYear
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List< SAGApplicationsForAudit > searchSAGApplicationsForAudit( String financialYear, String applicationStatus ) {
        logger.log( Level.INFO, "Get List of SAGApplications For Audit" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( SQL_GET_SAG_APPLICATIONS_FOR_AUDIT );
        if ( null != financialYear && !financialYear.isEmpty() ) {
            queryStr.append( " AND sag.FINANCIAL_YEAR = :financialYear" );
        }

        List< String > appStatusList = new ArrayList< String >();
        if ( null != applicationStatus && !applicationStatus.isEmpty() ) {
            logger.log( Level.INFO, "applicationStatus Strings for audit search: " + applicationStatus );
            List< String > applicationStatusStrings = new ArrayList< String >();
            if ( applicationStatus.contains( "," ) ) {
                applicationStatusStrings = Arrays.asList( applicationStatus.split( "," ) );
            }
            else {
                applicationStatusStrings.add( applicationStatus );
            }

            for ( String each : applicationStatusStrings ) {
                ApplicationStatus status = ApplicationStatus.get( each );
                appStatusList.add( status.name() );
            }

            if ( null != appStatusList && !appStatusList.isEmpty() ) {
                queryStr.append( " AND sag.APPLICATION_STATUS in (:applicationStatusList)" );
            }
        }
        queryStr.append( SQL_GET_SAG_APPLICATIONS_FOR_AUDIT_SUFFIX );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

        sqlQuery.setParameter( "percentValue", 10 );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            sqlQuery.setParameter( "financialYear", financialYear );
        }

        if ( null != appStatusList && !appStatusList.isEmpty() ) {
            sqlQuery.setParameterList( "applicationStatusList", appStatusList );
        }

        addApplicationsForAuditScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationsForAudit.class ) );

        List< SAGApplicationsForAudit > results = ( List< SAGApplicationsForAudit > ) sqlQuery.list();

        if ( results != null && results.size() > 100 ) {
            return results.subList( 0, 100 );
        }

        return results;
    }

    private SQLQuery addApplicationsForAuditScalars( SQLQuery sqlQuery ) {

        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "awardType" );
        sqlQuery.addScalar( "awardTypeDescription" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );
        sqlQuery.addScalar( "childNric" );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "departmentId" );
        sqlQuery.addScalar( "departmentDescription" );
        sqlQuery.addScalar( "dateOfSubmission", StandardBasicTypes.DATE );
        sqlQuery.addScalar( "perCapitaIncome", StandardBasicTypes.DOUBLE );
        return sqlQuery;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGAnnouncementConfig > searchSAGAnnouncementConfigByYear( String financialYear ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( financialYear != null && !financialYear.isEmpty() ) {
            Criteria criteria = session.createCriteria( SAGAnnouncementConfig.class );

            criteria.add( Restrictions.eq( "financialYear", financialYear ) );

            return ( List< SAGAnnouncementConfig > ) criteria.list();
        }

        return null;
    }

    public SAGAnnouncementConfig getSAGAnnouncementConfig( String id ) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        SAGAnnouncementConfig sagAnnouncementConfig = ( SAGAnnouncementConfig ) session.get( SAGAnnouncementConfig.class, id );

        return sagAnnouncementConfig;
    }

    public void saveSAGAnnouncementConfig( SAGAnnouncementConfig sagAnnouncementConfig, String requestor ) {
        logger.log( Level.INFO, "save SAG Announcement Config" );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagAnnouncementConfig );
        session.flush();
    }

    public Double getSAGAwardBalanceByYear( String financialYear ) {
        logger.log( Level.INFO, "Get SAG Award Balance by Year" );

        if ( null != financialYear && !financialYear.isEmpty() ) {
            Session session = SessionFactoryUtil.getInstance().getCurrentSession();

            StringBuffer queryStr = new StringBuffer();
            queryStr.append( SQL_GET_SAG_AWARD_PREVIOUS_BALANCE );

            SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

            sqlQuery.setParameter( "sagFinancialYear", financialYear );

            sqlQuery.setParameter( "donationFinancialYear", financialYear );

            Object result = sqlQuery.uniqueResult();

            if ( null != result && Number.class.isAssignableFrom( result.getClass() ) ) {
                return ( ( Number ) result ).doubleValue();
            }

        }

        return 0.00d;
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getSAGAwardYears() {
        logger.log( Level.INFO, "Get SAG Award Years" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        Criteria criteria = session.createCriteria( SAGApplication.class );

        criteria.setProjection( Projections.distinct( Projections.property( "financialYear" ) ) );
        criteria.addOrder( Order.desc( "financialYear" ) );

        return ( List< String > ) criteria.list();

    }

    public String getMaxSAGSequenceNumber( String financialYear ) {
        logger.log( Level.INFO, "Get Max Sequence Number " );
        if ( null != financialYear ) {
            Session session = SessionFactoryUtil.getInstance().getCurrentSession();

            StringBuffer queryStr = new StringBuffer();
            queryStr.append( SQL_GET_MAX_SAG_SEQUENCE_NUMBER );

            SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );

            sqlQuery.setParameter( "financialYear", financialYear );

            return ( String ) sqlQuery.uniqueResult();
        }

        return null;
    }

    public void saveSAGTask(SAGTask sagTask, String requestor){
        logger.log( Level.INFO, "Save SAG Task " );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        logger.log( Level.INFO, "Add New SAG Task with ReferenceNumber = " + Util.replaceNewLine( sagTask.getReferenceNumber() ) );
        session.saveOrUpdate( sagTask );
        session.flush();
    }

    @SuppressWarnings("unchecked")
    public SAGTask getSAGTask(String referenceNumber) {
        logger.log( Level.INFO, "Get SAG Task for referenceNumber: " + Util.replaceNewLine( referenceNumber ) );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        if ( referenceNumber != null  ) {
            Criteria criteria = session.createCriteria( SAGTask.class );
            criteria.add( Restrictions.eq( "referenceNumber", referenceNumber ) );
            criteria.setMaxResults(1);

            List<SAGTask> list = (List<SAGTask>) criteria.list();
            if (list.isEmpty()) {
                return null;
            }
            else {
                return list.get(0);
            }
        }

        return null;
    }

    public void deleteSAGTask(SAGTask sagTask, String currentUser) {
        logger.log( Level.INFO, "Delete SAG Task " );
        logger.log( Level.INFO, "sagTask " + sagTask.toString());
        SessionFactoryUtil.setUser(currentUser);
        Session session = SessionFactoryUtil.getCurrentSession();
        session.delete(sagTask);

    }

    @SuppressWarnings( "unchecked" )
    public void searchDraftSAGApplication( String financialYear) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        ApplicationStatus status = ApplicationStatus.DRAFT;
        if ( (financialYear != null && !financialYear.equals(""))) {
            List< SAGApplication > SAGApplicationList =session.createCriteria( SAGApplication.class )
                    .add(Restrictions.eq("financialYear", financialYear ))
                    .add( Restrictions.eq( "applicationStatus", status )).list();
            logger.log( Level.INFO, "SAG Application size" +SAGApplicationList.size());
            for(int i=0; i< SAGApplicationList.size(); i++)
            {
                logger.log( Level.INFO, "SAG Application" +SAGApplicationList.get(i).getReferenceNumber());
                SAGApplicationList.get(i).setApplicationStatus(ApplicationStatus.REJECTED);
                try {
                    updateSAGApplication(SAGApplicationList.get(i),"System");
                } catch (AccessDeniedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void processExpiredApplication() {

        logger.log( Level.INFO, "Process expired SAG Application... " );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        queryStr.append( SQL_UPDATE_EXPIRED_SAG_STATUS );

        Query query = session.createQuery( queryStr.toString() );
        query.setParameter( "newStatus", ApplicationStatus.EXPIRED );
        query.setParameter( "oldStatus", ApplicationStatus.DRAFT );
        query.setParameter( "configType", SAGDateConfigType.APP_PROCESSING_CUT_OFF );
        query.setParameter( "financialYear", ConvertUtil.convertToFinancialYearString() );
        query.setParameter( "date", new Date() );

        query.executeUpdate();
        logger.log( Level.INFO, "Complete processing expired SAG Application... " );
    }

    @SuppressWarnings("unchecked")
    public List <SAGApplication> searchSAGApplicationBySN (String financialYear, List <String> seqNo) {

        logger.log(Level.INFO, "Get SAG Application by Financial Year and Sequence No. list");

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryStr = new StringBuilder();

        queryStr.append("select sag from SAGApplication sag where 1=1 ");
        if (financialYear != null) {
            queryStr.append("and sag.financialYear = :financialYear ");
        }

        if (seqNo != null) {
            if (seqNo.size() > 0) {
                queryStr.append("and sag.sequenceNumber in :seqNo ");
            }
        }

        Query query = session.createQuery(queryStr.toString());

        if (financialYear != null) {
            query.setParameter("financialYear", financialYear);
        }

        if (seqNo != null) {
            if (seqNo.size() > 0) {
                query.setParameterList("seqNo", seqNo);
            }
        }

        return (List <SAGApplication>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List <String> searchSAGReferenceNoBySN (String financialYear, List <String> seqNo) {

        logger.log(Level.INFO, "Get Reference No by Financial Year and Sequence No. list");

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryStr = new StringBuilder();

        queryStr.append("select sag.referenceNumber from SAGApplication sag where 1=1 ");
        if (financialYear != null) {
            queryStr.append("and sag.financialYear = :financialYear ");
        }

        if (seqNo != null) {
            if (seqNo.size() > 0) {
                queryStr.append("and sag.sequenceNumber in :seqNo ");
            }
        }

        Query query = session.createQuery(queryStr.toString());

        if (financialYear != null) {
            query.setParameter("financialYear", financialYear);
        }

        if (seqNo != null) {
            if (seqNo.size() > 0) {
                query.setParameterList("seqNo", seqNo);
            }
        }

        return (List<String>)query.list() ;
    }

    @SuppressWarnings("unchecked")
    public List <String> searchSAGSNByReferenceNo (String financialYear, List <String> referenceNoList) {
        logger.log(Level.INFO, "Get Sequence No by Financial Year and Reference No. list");

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryStr = new StringBuilder();

        queryStr.append("select sag.sequenceNumber from SAGApplication sag where 1=1 ");
        if (financialYear != null) {
            queryStr.append("and sag.financialYear = :financialYear ");
        }

        if (referenceNoList != null) {
            if (referenceNoList.size() > 0) {
                queryStr.append("and sag.referenceNumber in :referenceNoList ");
            }
        }

        queryStr.append("order by sag.sequenceNumber");

        Query query = session.createQuery(queryStr.toString());

        if (financialYear != null) {
            query.setParameter("financialYear", financialYear);
        }

        if (referenceNoList != null) {
            if (referenceNoList.size() > 0) {
                query.setParameterList("referenceNoList", referenceNoList);
            }
        }

        return (List<String>)query.list() ;
    }

    @SuppressWarnings("unchecked")
    public List<SAGEventRsvp> getSAGEventRsvpListByIdList (List <String> febIds) {

        logger.log(Level.INFO, "Get SAG Event RSVP List by FEB ID List");
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select rsvp from SAGEventRsvp rsvp where 1=1 ");
        if (febIds != null) {
            if (febIds.size() > 0) {
                queryStr.append("and rsvp.id in :febIds");
            }
        }

        Query query = session.createQuery(queryStr.toString());
        if (febIds != null) {
            if (febIds.size() > 0) {
                query.setParameterList("febIds", febIds);
            }
        }
        return (List<SAGEventRsvp>) query.list();
    }

    @SuppressWarnings("unchecked")
    public List <SAGAwardQuantumDescription> searchAwardQuantumByFY (String financialYear) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();

        queryStr.append("SELECT {awardQuantum.*}, ip.INPUT_DESCRIPTION as description FROM SPFCORE.SAG_AWARD_QUANTUM awardQuantum LEFT JOIN ");
        queryStr.append("SPFCORE.SAG_INPUTS ip ON awardQuantum.SUB_TYPE = ip.INPUT_ID and awardQuantum.AWARD_TYPE = ip.AWARD_TYPE ");
        queryStr.append("WHERE awardQuantum.AWARD_TYPE = 'SA' and awardQuantum.FINANCIAL_YEAR = :financialYear and ip.INPUT_TYPE = 'NEW_EDU_LEVEL' ");
        queryStr.append("or (awardQuantum.award_type = 'SAA' and awardQuantum.FINANCIAL_YEAR = :financialYear and ip.INPUT_TYPE = 'HIGHEST_EDU_LEVEL') ");
        queryStr.append("or (awardQuantum.award_type = 'SG' and awardQuantum.FINANCIAL_YEAR = :financialYear) ");
        queryStr.append("order by ip.AWARD_TYPE, ip.LIST_ORDER asc");

        SQLQuery sqlQuery = session.createSQLQuery(queryStr.toString());

        if (financialYear != null) {
            sqlQuery.setParameter("financialYear", financialYear);
        }

        sqlQuery.addEntity("awardQuantum", SAGAwardQuantum.class);
        sqlQuery.addScalar("description", StandardBasicTypes.STRING);

        sqlQuery.setResultTransformer(Transformers.aliasToBean(SAGAwardQuantumDescription.class));

        return (List<SAGAwardQuantumDescription>)sqlQuery.list();
    }

    @SuppressWarnings("unchecked")
    public List<SAGApplication> getApplicationWithSameChildNric (String financialYear, String childNric, List<String> statuses, String referenceNumber) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        List<ApplicationStatus> applicationStatuses = new ArrayList<ApplicationStatus>();

        queryStr.append("select sag from SAGApplication sag where 1=1 ");
        if (financialYear != null){
            queryStr.append("and sag.financialYear = :financialYear ");
        }

        if (childNric != null) {
            queryStr.append("and sag.childNric = :childNric ");
        }

        if (statuses != null) {
            if (statuses.size() > 0) {
                queryStr.append("and sag.applicationStatus in :applicationStatus ");
            }
        }

        if (referenceNumber != null) {
            queryStr.append("and sag.referenceNumber != :referenceNumber");
        }

        Query query = session.createQuery(queryStr.toString());

        if (financialYear != null){
            query.setParameter("financialYear", financialYear);
        }

        if (childNric != null) {
            query.setParameter("childNric", childNric);
        }

        if (statuses != null) {
            if (statuses.size() > 0) {
                for (String status: statuses) {
                    applicationStatuses.add(ApplicationStatus.get(status));
                }

                query.setParameterList("applicationStatus", applicationStatuses);
            }
        }

        if (referenceNumber!= null) {
            query.setParameter("referenceNumber", referenceNumber);
        }

        return (List<SAGApplication>)query.list();
    }

    @SuppressWarnings("unchecked")
    public List<SAGApplication> getSuccessfulApplicationByChildNric (List<String> financialYear, String childNric) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuilder queryStr = new StringBuilder();
        ApplicationStatus status = ApplicationStatus.SUCCESSFUL;

        queryStr.append("select sag from SAGApplication sag where 1=1 ");
        if (financialYear != null) {
            if (financialYear.size() > 0) {
                queryStr.append("and sag.financialYear in :financialYear ");
            }
        }

        if (childNric != null) {
            queryStr.append("and sag.childNric = :childNric ");
        }

        queryStr.append("and sag.applicationStatus = :status order by sag.financialYear desc");

        Query query = session.createQuery(queryStr.toString());

        if (financialYear != null) {
            if (financialYear.size() > 0) {
                query.setParameterList("financialYear", financialYear);
            }
        }

        if (childNric != null) {
            query.setParameter("childNric", childNric);
        }

        query.setParameter("status", status);

        return (List<SAGApplication>)query.list();
    }

    @SuppressWarnings("unchecked")
    public List<SAGApplicationFamilyBackground> getDuplicatedFamilyBackground (String financialYear) {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT  app.member_nric as memberNric, sag.REFERENCE_NUMBER as referenceNumber,");
        //queryStr.append(" SUM(sag.GROSS_SALARY+sag.SPECIAL_ALLOWANCE) as grossSalary");
        queryStr.append(" ( SUM(sag.GROSS_SALARY+sag.SPECIAL_ALLOWANCE) / (COUNT(sag.REFERENCE_NUMBER)+1) ) as Pci");
        queryStr.append(" FROM SPFCORE.SAG_FAMILY_BACKGROUND sag, SPFCORE.SAG_APPLICATION app");
        queryStr.append(" WHERE sag.reference_number = app.reference_number and app.FINANCIAL_YEAR = :financialYear and "
                + "app.member_nric IN (SELECT app2.MEMBER_NRIC from spfcore.sag_application app2 where app2.financial_year "
                + "= :financialYear and (app2.AWARD_TYPE = :awardType or app2.AWARD_TYPE = :awardTypeSG) group by app2.MEMBER_NRIC having "
                + "count(app2.MEMBER_NRIC)>1)");
        queryStr.append(" GROUP BY app.member_nric,sag.REFERENCE_NUMBER");
        queryStr.append(" ORDER BY app.member_nric");

        SQLQuery sqlQuery = session.createSQLQuery(queryStr.toString());
        sqlQuery.addScalar("memberNric", StandardBasicTypes.STRING);
        sqlQuery.addScalar("referenceNumber", StandardBasicTypes.STRING);
        //sqlQuery.addScalar("grossSalary", StandardBasicTypes.FLOAT);
        sqlQuery.addScalar("Pci", StandardBasicTypes.DOUBLE);

        sqlQuery.setParameter("financialYear", financialYear);
        sqlQuery.setParameter("awardType", "SA");
        sqlQuery.setParameter("awardTypeSG", "SG");
        sqlQuery.setResultTransformer(Transformers.aliasToBean(SAGApplicationFamilyBackground.class));
        List<SAGApplicationFamilyBackground> SAGApplicationFamilyBackground = (List<SAGApplicationFamilyBackground>)sqlQuery.list();
        logger.info("finish retrieving");
        return SAGApplicationFamilyBackground;

    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplication > searchSuccessfulSAGApplicationsByPaymentMode( String paymentMode, String financialYear ) {
        logger.log( Level.INFO, "Get List of SAGApplications By Payment Mode" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();
        ApplicationStatus status = ApplicationStatus.SUCCESSFUL;

        queryStr.append("SELECT sag FROM SAGApplication sag where 1=1 ");
        if (paymentMode != null){
            queryStr.append("and sag.preferredPaymentMode = :paymentMode ");
        }
        if (financialYear != null) {
            queryStr.append("and sag.financialYear = :financialYear ");
        }
        queryStr.append("and sag.applicationStatus = :applicationStatus order by sag.referenceNumber asc");

        Query query = session.createQuery( queryStr.toString() );
        if (paymentMode != null){
            query.setParameter("paymentMode", paymentMode);
        }
        if (financialYear != null) {
            query.setParameter("financialYear", financialYear);
        }
        query.setParameter("applicationStatus", status);

        return ( List< SAGApplication > ) query.list();
    }

    private Query setApplicationsForAwardPaymentCriteriaParameter( Query query, SAGApplicationCriteria criteria,
                                                                   String paymentMode, List<String> referenceNumberList, List<String> paymentStatusList ) {
        if ( null != criteria ) {

            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                List< String > awardTypeList = new ArrayList< String >();
                if ( criteria.getAwardType().contains( "," ) ) {
                    awardTypeList = Arrays.asList( criteria.getAwardType().split( "," ) );
                }
                else {
                    awardTypeList.add( criteria.getAwardType() );
                }
                logger.log( Level.INFO, "awardTypeList" + awardTypeList.toString() );
                query.setParameterList( "awardTypeList", awardTypeList );
            }

            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                logger.log( Level.INFO, "financialYear " + criteria.getFinancialYear() );
                query.setParameter( "financialYear", criteria.getFinancialYear() );
            }

            if ( null != criteria.getApplicationStatus() ) {
                logger.log( Level.INFO, "applicationStatus " + criteria.getApplicationStatus().toString() );
                query.setParameterList( "applicationStatus", getApplicationStatusNamesList( criteria.getApplicationStatus() ) );
            }

            if ( null != criteria.getChequeUpdatedDate() ) {
                logger.log( Level.INFO, "chequeUpdatedDate " + criteria.getChequeUpdatedDate().toString() );
                query.setParameter( "chequeUpdatedDate", formatDate(criteria.getChequeUpdatedDate()) );
            }
        }

        if ( null != paymentMode ) {
            query.setParameter( "paymentMode", paymentMode );
        }

        if (null != referenceNumberList && !referenceNumberList.isEmpty()){
            query.setParameterList("referenceNumberList", referenceNumberList);
        }

        List<String> paymentStatuses = new ArrayList<String>();
        if (null != paymentStatusList && !paymentStatusList.isEmpty()){
            for (String status: paymentStatusList) {
                logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter status:"+status);
                PaymentStatus paymentStatus = PaymentStatus.get(status);
                if (paymentStatus != null){
                    paymentStatuses.add(paymentStatus.name());
                    logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter paymentStatuses:" + paymentStatus.name());
                }
            }
            query.setParameterList("paymentStatusList", paymentStatuses);
            logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter paymentStatusesList:" + paymentStatuses.size());
        }

        return query;
    }

    private Query setApplicationsForAwardPaymentCriteriaParameter( Query query, Date startDate, Date endDate, SAGApplicationCriteria criteria,
                                                                   String paymentMode, List<String> referenceNumberList, List<String> paymentStatusList ) {
        if ( null != criteria ) {

            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                List< String > awardTypeList = new ArrayList< String >();
                if ( criteria.getAwardType().contains( "," ) ) {
                    awardTypeList = Arrays.asList( criteria.getAwardType().split( "," ) );
                }
                else {
                    awardTypeList.add( criteria.getAwardType() );
                }
                logger.log( Level.INFO, "awardTypeList" + awardTypeList.toString() );
                query.setParameterList( "awardTypeList", awardTypeList );
            }

            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                logger.log( Level.INFO, "financialYear " + criteria.getFinancialYear() );
                query.setParameter( "financialYear", criteria.getFinancialYear() );
            }

            if ( null != criteria.getApplicationStatus() ) {
                logger.log( Level.INFO, "applicationStatus " + criteria.getApplicationStatus().toString() );
                query.setParameterList( "applicationStatus", getApplicationStatusNamesList( criteria.getApplicationStatus() ) );
            }

            if ( null != criteria.getChequeUpdatedDate() ) {
                logger.log( Level.INFO, "chequeUpdatedDate " + criteria.getChequeUpdatedDate().toString() );
                query.setParameter( "chequeUpdatedDate", formatDate(criteria.getChequeUpdatedDate()) );
            }

            if ( null != startDate ) {
                logger.log( Level.INFO, "startDate " + startDate.toString() );
                query.setParameter( "startDate", formatDate(startDate) );
            }

            if ( null != endDate ) {
                logger.log( Level.INFO, "endDate " + endDate.toString() );
                query.setParameter( "endDate", formatDate(endDate) );
            }
        }

        if ( null != paymentMode ) {
            query.setParameter( "paymentMode", paymentMode );
        }

        if (null != referenceNumberList && !referenceNumberList.isEmpty()){
            query.setParameterList("referenceNumberList", referenceNumberList);
        }

        List<String> paymentStatuses = new ArrayList<String>();
        if (null != paymentStatusList && !paymentStatusList.isEmpty()){
            for (String status: paymentStatusList) {
                logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter status:"+status);
                PaymentStatus paymentStatus = PaymentStatus.get(status);
                if (paymentStatus != null){
                    paymentStatuses.add(paymentStatus.name());
                    logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter paymentStatuses:" + paymentStatus.name());
                }
            }
            query.setParameterList("paymentStatusList", paymentStatuses);
            logger.log( Level.INFO, "setApplicationsForAwardPaymentCriteriaParameter paymentStatusesList:" + paymentStatuses.size());
        }

        return query;
    }

    private String formatDate(Date updatedDate){
        if (updatedDate != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(updatedDate);
        }
        return null;
    }

    private StringBuffer buildQueryStringByApplicationsForAwardPaymentCriteria( StringBuffer queryStr,
                                                                                SAGApplicationCriteria criteria, String paymentMode, List<String> referenceNumberList,
                                                                                List<String> paymentStatusList) {
        if ( null != criteria ) {
            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                queryStr.append( " AND sag.AWARD_TYPE IN (:awardTypeList)" );
            }
            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                queryStr.append( " AND sag.FINANCIAL_YEAR = :financialYear" );
            }
            if ( null != criteria.getApplicationStatus() ) {
                queryStr.append( " AND sag.APPLICATION_STATUS IN (:applicationStatus)" );
            }
            if ( null != criteria.getChequeUpdatedDate() ) {
                queryStr.append( " AND sag.CHEQUE_UPDATED_DATE = :chequeUpdatedDate" );
            }
        }

        if ( null != paymentMode ) {
            queryStr.append( " AND sag.PREFERRED_PAYMENT_MODE = :paymentMode" );
        }

        if (null != referenceNumberList && !referenceNumberList.isEmpty()){
            queryStr.append (" AND sag.REFERENCE_NUMBER in (:referenceNumberList)");
        }

        queryStr.append( " AND (bfr.PAYMENT_STATUS IS NULL " );
        if (null != paymentStatusList && !paymentStatusList.isEmpty()){
            queryStr.append( " OR bfr.PAYMENT_STATUS IN (:paymentStatusList)" );
        }
        queryStr.append( " )" );

        return queryStr;
    }

    private StringBuffer buildQueryStringByApplicationsForAwardPaymentByDateCriteria( StringBuffer queryStr,
                                                                                      Date startDate, Date endDate, SAGApplicationCriteria criteria, String paymentMode, List<String> referenceNumberList,
                                                                                      List<String> paymentStatusList) {
        if ( null != criteria ) {
            if ( null != criteria.getAwardType() && !criteria.getAwardType().isEmpty() ) {
                queryStr.append( " AND sag.AWARD_TYPE IN (:awardTypeList)" );
            }
            if ( null != criteria.getFinancialYear() && !criteria.getFinancialYear().isEmpty() ) {
                queryStr.append( " AND sag.FINANCIAL_YEAR = :financialYear" );
            }
            if ( null != criteria.getApplicationStatus() ) {
                queryStr.append( " AND sag.APPLICATION_STATUS IN (:applicationStatus)" );
            }
            if ( null != criteria.getChequeUpdatedDate() ) {
                queryStr.append( " AND sag.CHEQUE_UPDATED_DATE = :chequeUpdatedDate" );
            }

            if ( null != startDate && null != endDate ) {
                queryStr.append( " AND sag.CHEQUE_UPDATED_DATE BETWEEN :startDate and :endDate" );
            }
        }

        if ( null != paymentMode ) {
            queryStr.append( " AND sag.PREFERRED_PAYMENT_MODE = :paymentMode" );
        }

        if (null != referenceNumberList && !referenceNumberList.isEmpty()){
            queryStr.append (" AND sag.REFERENCE_NUMBER in (:referenceNumberList)");
        }

        queryStr.append( " AND (bfr.PAYMENT_STATUS IS NULL " );
        if (null != paymentStatusList && !paymentStatusList.isEmpty()){
            queryStr.append( " OR bfr.PAYMENT_STATUS IN (:paymentStatusList)" );
        }
        queryStr.append( " )" );

        return queryStr;
    }

    private SQLQuery addApplicationsForAwardPaymentScalars( SQLQuery sqlQuery ) {
        sqlQuery.addScalar( "referenceNumber" );
        sqlQuery.addScalar( "financialYear" );
        sqlQuery.addScalar( "awardType" );
        sqlQuery.addScalar( "memberNric" );
        sqlQuery.addScalar( "memberName" );
        sqlQuery.addScalar( "childNric" );
        sqlQuery.addScalar( "childName" );
        sqlQuery.addScalar( "awardAmount", StandardBasicTypes.DOUBLE );
        sqlQuery.addScalar( "sequenceNumber" );
        sqlQuery.addScalar( "submittedByPaymentAdviceEmail" );
        sqlQuery.addScalar( "submittedByWorkEmail" );
        sqlQuery.addScalar( "submittedByPrefEmail" );
        sqlQuery.addScalar( "preferredPaymentMode" );
        sqlQuery.addScalar( "bicCodeProxyType" );
        sqlQuery.addScalar( "accNoProxyValue" );
        sqlQuery.addScalar( "accName" );

        return sqlQuery;
    }

    @SuppressWarnings( "unchecked" )
    public List< SAGApplicationChildDetail > searchPrdChildDetails( List<String> financialYear, List<String> educationLevel ) {
        logger.log( Level.INFO, "get Child Details  by FinancialYear EducationLevel from SAGApplication" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append( "SELECT sag.memberNric AS memberNric, sag.financialYear AS financialYear, sag.referenceNumber AS referenceNumber"
                + ", sag.childName AS childName, sag.childNric AS childNric, sag.childEmail AS childEmail"
                + " FROM SAGApplication AS sag, PersonalDetail AS personal"
                + " WHERE sag.memberNric = personal.nric"
                + " and sag.allowChildEmailForPRD = true" );
        queryStr.append( " and sag.awardType != :awardType" );
        queryStr.append( " and personal.employment.serviceType != :serviceType" );

        if (financialYear != null && !financialYear.isEmpty()) {
            queryStr.append(" and sag.financialYear in (:financialYear)");
        }
        if (educationLevel != null && !educationLevel.isEmpty()) {
            queryStr.append(" and sag.childNewEduLevel in (:educationLevel)");
        }

        queryStr.append( " ORDER BY sag.referenceNumber, sag.childNric, sag.childNewEduLevel, sag.memberNric" );

        Query query = session.createQuery( queryStr.toString() );

        query.setParameter("awardType", SAGAwardType.STUDY_GRANT.name());
        query.setParameter("serviceType", "412");
        if (financialYear != null && !financialYear.isEmpty()){
            query.setParameterList("financialYear", financialYear);
        }
        if (educationLevel != null && !educationLevel.isEmpty()) {
            query.setParameterList("educationLevel", educationLevel);
        }

        query.setResultTransformer( Transformers.aliasToBean( SAGApplicationChildDetail.class ) );

        return ( List< SAGApplicationChildDetail > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List<SAGApplicationsApprovedForAwardWithPayment> searchSAGApplicationsApprovedForAwardWithPayment
            ( SAGApplicationCriteria criteria, String paymentMode, List<String> paymentStatusList ) {
        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award with Payment" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append(SQL_GET_SAG_APPROVED_APPLICATIONS_PAYMENT_INFORMATION);
        buildQueryStringByApplicationsForAwardPaymentCriteria( queryStr, criteria, paymentMode, null, paymentStatusList );
        queryStr.append( SQL_GET_SAG_APPROVED_APPLICATIONS_SUFFIX );

        logger.log( Level.INFO, "sqlQuery -- " + queryStr.toString() );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        setApplicationsForAwardPaymentCriteriaParameter( sqlQuery, criteria, paymentMode, null, paymentStatusList);
        addApplicationsForAwardPaymentScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationsApprovedForAwardWithPayment.class ) );

        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award with Payment Result Size >> " + sqlQuery.list().size() );

        return ( List< SAGApplicationsApprovedForAwardWithPayment > ) sqlQuery.list();
    }

    @SuppressWarnings( "unchecked" )
    public List<SAGApplicationsApprovedForAwardWithPayment> searchSAGApplicationsApprovedForAwardWithPaymentByDate
            ( Date startDate, Date endDate, SAGApplicationCriteria criteria, String paymentMode, List<String> paymentStatusList ) {
        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award with Payment by date" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append(SQL_GET_SAG_APPROVED_APPLICATIONS_PAYMENT_INFORMATION);
        buildQueryStringByApplicationsForAwardPaymentByDateCriteria( queryStr, startDate, endDate, criteria, paymentMode, null, paymentStatusList );
        queryStr.append( SQL_GET_SAG_APPROVED_APPLICATIONS_SUFFIX );

        logger.log( Level.INFO, "sqlQuery -- " + queryStr.toString() );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        setApplicationsForAwardPaymentCriteriaParameter( sqlQuery, startDate, endDate, criteria, paymentMode, null, paymentStatusList);
        addApplicationsForAwardPaymentScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationsApprovedForAwardWithPayment.class ) );

        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award with Payment By Date Result Size >> " + sqlQuery.list().size() );

        return ( List< SAGApplicationsApprovedForAwardWithPayment > ) sqlQuery.list();
    }

    @SuppressWarnings( "unchecked" )
    public List<SAGApplicationsApprovedForAwardWithPayment> searchSAGApplicationsApprovedForAwardWithPaymentByRefNum (
            SAGApplicationCriteria criteria, String paymentMode, List<String> referenceNumberList, List<String> paymentStatusList) {
        logger.log( Level.INFO, "Get List of SAGApplications Approved For Award with Payment By Reference Number" );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append(SQL_GET_SAG_APPROVED_APPLICATIONS_PAYMENT_INFORMATION);
        buildQueryStringByApplicationsForAwardPaymentCriteria( queryStr, criteria, paymentMode, referenceNumberList, paymentStatusList );
        queryStr.append( SQL_GET_SAG_APPROVED_APPLICATIONS_SUFFIX );

        logger.log( Level.INFO, "sqlQuery -- " + queryStr.toString() );

        SQLQuery sqlQuery = session.createSQLQuery( queryStr.toString() );
        setApplicationsForAwardPaymentCriteriaParameter( sqlQuery, criteria, paymentMode, referenceNumberList, paymentStatusList );
        addApplicationsForAwardPaymentScalars( sqlQuery );
        sqlQuery.setResultTransformer( Transformers.aliasToBean( SAGApplicationsApprovedForAwardWithPayment.class ) );

        return ( List< SAGApplicationsApprovedForAwardWithPayment > ) sqlQuery.list();
    }

    public void saveSAGBatchFileRecord( SAGBatchFileRecord sagBatchFileRecord, String requestor ) {
        logger.log( Level.INFO, "save SAG Batch File Record" );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        session.saveOrUpdate( sagBatchFileRecord );
        session.flush();
    }

    public void saveSAGBatchFileRecordList( List<SAGBatchFileRecord> sagBatchFileRecordList, String requestor ) {
        logger.log( Level.INFO, "save SAG Batch File Record List" );
        SessionFactoryUtil.setUser( requestor );
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        for (int i = 0; i < sagBatchFileRecordList.size(); i ++) {
            SAGBatchFileRecord record = sagBatchFileRecordList.get(i);
            session.saveOrUpdate(record);

            if ( i % 20 == 0) { //20, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
            }
        }
        session.flush();
    }

    @SuppressWarnings("unchecked")
    public List <SAGBatchFileRecord> searchSAGBatchFileRecordByFY (String financialYear, List<String> paymentStatusList) {
        logger.log( Level.INFO, "Get List of SAG Batch File Records By Financial Year" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append("SELECT batch FROM SAGBatchFileRecord batch where 1=1 ");
        if (financialYear != null) {
            queryStr.append("and batch.financialYear = :financialYear ");
        }
        if (paymentStatusList != null && !paymentStatusList.isEmpty()) {
            queryStr.append("and batch.paymentStatus in (:paymentStatusList) ");
        }
        queryStr.append("order by batch.referenceNumber asc");

        Query query = session.createQuery( queryStr.toString() );
        if (financialYear != null) {
            query.setParameter("financialYear", financialYear);
        }
        if (paymentStatusList != null && !paymentStatusList.isEmpty()){
            List<PaymentStatus> paymentStatuses = new ArrayList<PaymentStatus>();
            if (null != paymentStatusList && !paymentStatusList.isEmpty()){
                for (String status: paymentStatusList) {
                    paymentStatuses.add(PaymentStatus.get(status));
                    logger.log( Level.INFO, "searchSAGBatchFileRecordByFY status:" + PaymentStatus.get(status));
                }
                query.setParameterList("paymentStatusList", paymentStatuses);
            }
        }

        return ( List< SAGBatchFileRecord > ) query.list();
    }

    @SuppressWarnings("unchecked")
    public List <SAGBatchFileRecord> searchSAGBatchFileRecordByReferenceNumber (List<String> referenceNumberList) {
        logger.log( Level.INFO, "Get List of SAG Batch File Records By Reference Number" );

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        StringBuffer queryStr = new StringBuffer();

        queryStr.append("SELECT batch FROM SAGBatchFileRecord batch where 1=1 ");

        if (referenceNumberList != null) {
            queryStr.append("and batch.referenceNumber in (:referenceNumberList) ");
        }

        Query query = session.createQuery( queryStr.toString() );
        if (referenceNumberList != null && !referenceNumberList.isEmpty()){
            query.setParameterList("referenceNumberList", referenceNumberList);
        }

        return ( List< SAGBatchFileRecord > ) query.list();
    }

}
