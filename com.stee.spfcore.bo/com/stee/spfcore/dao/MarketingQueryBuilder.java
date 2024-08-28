package com.stee.spfcore.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stee.spfcore.model.ActivationStatus;
import com.stee.spfcore.model.marketing.BooleanRule;
import com.stee.spfcore.model.marketing.CodeRule;
import com.stee.spfcore.model.marketing.DateRule;
import com.stee.spfcore.model.marketing.DurationRule;
import com.stee.spfcore.model.marketing.DurationUnit;
import com.stee.spfcore.model.marketing.Field;
import com.stee.spfcore.model.marketing.ListRule;
import com.stee.spfcore.model.marketing.ListRuleValue;
import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.model.marketing.Category;
import com.stee.spfcore.model.marketing.NumberRule;
import com.stee.spfcore.model.marketing.Operator;
import com.stee.spfcore.model.marketing.Rule;
import com.stee.spfcore.model.marketing.RuleSet;
import com.stee.spfcore.model.marketing.RuleType;
import com.stee.spfcore.model.marketing.SortOrder;
import com.stee.spfcore.model.marketing.StringRule;
import com.stee.spfcore.model.membership.MembershipType;

public class MarketingQueryBuilder {

	public static void build(MemberGroup group, StringBuilder query, Map<String, Object> properties) {

		Counter counter = new Counter();

		query.append("FROM PersonalDetail p ");
		List<RuleSet> ruleSets = group.getRuleSets();

		if (!ruleSets.isEmpty()) {
			query.append("where ");
		}

		boolean firstRule = true;
		// Between rule set is or condition
		for (RuleSet ruleSet : ruleSets) {
			if (!firstRule) {
				query.append(" or ");
			}

			processRuleSet(ruleSet, query, properties, counter);

			firstRule = false;
		}
	}

	public static String processOrderByField (MemberGroup group, StringBuilder query, boolean appendOrderField) {

		Category category = group.getOrderByCategory();
		Field field = group.getOrderByField();
		SortOrder sortOrder = group.getOrder();
		String orderByField = null;

		if (category != null && field != null && sortOrder != null) {

			// Only Personnel & Employment category can be sorted.
			if (category == Category.PERSONNEL) {

				if (field == Field.DATE_OF_BIRTH) {
					orderByField = "p.dateOfBirth";
				}
				else if (field == Field.NRIC) {
					orderByField = "p.nric";
				}
				else if (field == Field.NAME) {
					orderByField = "p.name";
				}
			}
			else if (category == Category.EMPLOYMENT) {

				if (field == Field.APPOINTMENT_DATE) {
					orderByField = "p.employment.dateOfAppointment";
				}
				else if (field == Field.RETIREMENT_DATE) {
					orderByField = "p.employment.dateOfRetirement";
				}
				else if (field == Field.LEAVING_SERVICE_DATE) {
					orderByField = "p.employment.leavingServiceDate";
				}
			}

			if (orderByField != null && appendOrderField) {
				query.append(", ").append(orderByField).append(" ");
			}
		}

		return orderByField;
	}


	public static void appendOrderByField (MemberGroup group, String orderByField, StringBuilder query) {

		if (orderByField != null) {
			SortOrder sortOrder = group.getOrder();

			query.append(" order by ").append(orderByField).append(" ");

			if (sortOrder == SortOrder.ASCENDING) {
				query.append("asc");
			}
			else {
				query.append("desc");
			}
		}
	}

	private static void processRuleSet(RuleSet ruleSet, StringBuilder query, Map<String, Object> properties, Counter counter) {

		Map<Category, List<Rule>> groupedRules = groupRuleSet(ruleSet);

		query.append(" ( ");

		boolean firstCategory = true;
		for (Category category : groupedRules.keySet()) {
			List<Rule> ruleList = groupedRules.get(category);

			if (!firstCategory) {
				query.append(" and ");
			}

			if (Category.PERSONNEL == category) {
				processPersonnelRules(ruleList, query, properties, counter);
			}
			else if (Category.EMPLOYMENT == category) {
				processEmploymentRules(ruleList, query, properties, counter);
			}
			else if (Category.CHILD == category) {
				processChildRules(ruleList, query, properties, counter);
			}
			else if (Category.SPOUSE == category) {
				processSpouseRules(ruleList, query, properties, counter);
			}
			else if (Category.MEMBERSHIP == category) {
				processMembershipRules(ruleList, query, properties, counter);
			}
			else if (Category.COURSE == category) {
				processCourseRules (ruleList, query, properties, counter);
			}
			else if (Category.BLACKLIST == category) {
				processBlacklistRules (ruleList, query, properties, counter);
			}

			firstCategory = false;
		}

		query.append(" ) ");
	}

	private static Map<Category, List<Rule>> groupRuleSet(RuleSet ruleSet) {

		List<Rule> rules = ruleSet.getRules();
		Map<Category, List<Rule>> groupedRules = new HashMap<Category, List<Rule>>();

		for (Rule rule : rules) {
			List<Rule> ruleList = groupedRules.get(rule.getCategory());
			if (ruleList == null) {
				ruleList = new ArrayList<Rule>();
				groupedRules.put(rule.getCategory(), ruleList);
			}

			ruleList.add(rule);
		}

		return groupedRules;
	}

	private static void processPersonnelRules(List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		query.append(" ( ");

		boolean firstRule = true;
		for (Rule rule : ruleList) {

			if (!firstRule) {
				query.append(" and ");
			}

			int id = counter.getAndIncrement();

			if (Field.NAME == rule.getField()) {
				String paramId = "personalName" + id;
				String columnName = "p.name";
				processStringFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.DATE_OF_BIRTH == rule.getField()) {
				String paramId = "personalDOB" + id;
				String columnName = "p.dateOfBirth";
				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.NRIC == rule.getField()) {
				String paramId = "personalNric" + id;
				String columnName = "p.nric";
				processStringFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.CITIZENSHIP == rule.getField()) {
				String paramId = "personalCitizenship" + id;
				String columnName = "p.citizenship";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.EDUATION_LEVEL == rule.getField()) {
				String paramId = "personalEduation" + id;
				String columnName = "p.eduationLevel";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.GENDER == rule.getField()) {
				String paramId = "personalGender" + id;
				String columnName = "p.gender";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.MARITAL_STATUS == rule.getField()) {
				String paramId = "personalMaritalStatus" + id;
				String columnName = "p.maritalStatus";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.NATIONALITY == rule.getField()) {
				String paramId = "personalNationality" + id;
				String columnName = "p.nationality";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.RACE == rule.getField()) {
				String paramId = "race" + id;
				String columnName = "p.race";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.INTEREST == rule.getField()) {
				processInterestFields (String.valueOf(id), rule, query, properties);
			}

			firstRule = false;
		}

		query.append(" ) ");
	}


	private static void processEmploymentRules(List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		query.append(" ( ");

		boolean firstRule = true;
		for (Rule rule : ruleList) {

			if (!firstRule) {
				query.append(" and ");
			}

			int id = counter.getAndIncrement();

			if (Field.APPOINTMENT_DATE == rule.getField()) {
				String paramId = "appointmentDate" + id;
				String columnName = "p.employment.dateOfAppointment";
				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.RETIREMENT_DATE == rule.getField()) {
				String paramId = "retirementDate" + id;
				String columnName = "p.employment.dateOfRetirement";
				processDateFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.LEAVING_SERVICE_DATE == rule.getField()) {
				String paramId = "leavingServiceDate" + id;
				String columnName = "p.employment.leavingServiceDate";
				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.RANK == rule.getField()) {
				String paramId = "employmentRank" + id;
				String columnName = "p.employment.rankOrGrade";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.DEPARTMENT == rule.getField()) {
				String paramId = "department" + id;
				String columnName = "p.employment.organisationOrDepartment";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.SUBUNIT == rule.getField()) {
				String paramId = "subunit" + id;
				String columnName = "p.employment.subunit";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.SERVICE_TYPE == rule.getField()) {
				String paramId = "serviceType" + id;
				String columnName = "p.employment.serviceType";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.EMPLOYMENT_STATUS == rule.getField()) {
				String paramId = "employmentStatus" + id;
				String columnName = "p.employment.employmentStatus";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.DIVISION_STATUS == rule.getField()) {
				String paramId = "divisionStatus" + id;
				String columnName = "p.employment.divisionStatus";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.SCHEME_OF_SERVICE == rule.getField()) {
				String paramId = "schemeOfService" + id;
				String columnName = "p.employment.schemeOfService";
				processCodeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.DESIGNATION == rule.getField()) {
				String paramId = "designation" + id;
				String columnName = "p.employment.designation";
				processCodeFields(paramId, columnName, rule, query, properties);
			}

			firstRule = false;
		}

		query.append(" ) ");
	}


	private static void processChildRules(List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		query.append(" ( ");

		boolean firstRule = true;
		for (Rule rule : ruleList) {

			if (!firstRule) {
				query.append(" and ");
			}

			if (Field.CHILD_COUNT == rule.getField()) {
				processChildCountField (rule, query, properties, counter);
			}
			else if (Field.DATE_OF_BIRTH == rule.getField()) {
				processChildDateOfBirthField (rule, query, properties, counter);
			}

			firstRule = false;
		}

		query.append(" ) ");
	}

	private static void processSpouseRules(List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {


		int id = counter.getAndIncrement();

		query.append(" ( ");

		String spouseTableName = "spouse" + id;

		query.append(" p.nric in (select distinct ").append ("innerP").append(id);
		query.append(".nric from PersonalDetail innerP").append(id).append(" join innerP");
		query.append(id).append(".spouses ").append(spouseTableName).append(" where ");

		boolean firstRule = true;
		for (Rule rule : ruleList) {

			int subId = counter.getAndIncrement();

			if (!firstRule) {
				query.append(" and ");
			}

			if (Field.DATE_OF_MARRIAGE == rule.getField()) {

				String paramId = "dateOfMarriage" + subId;
				String columnName = spouseTableName + ".dateOfMarriage";

				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.DATE_OF_BIRTH == rule.getField()) {

				String paramId = "dateOfBirth" + subId;
				String columnName = spouseTableName + ".dateOfBirth";

				processDateFields (paramId, columnName, rule, query, properties);
			}

			firstRule = false;
		}

		query.append(" ) ) ");
	}


	private static void processMembershipRules(List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		int id = counter.getAndIncrement();

		String membershipTableName = "innerM" + id;
		query.append(" ( ");

		query.append(" p.nric in (select distinct ").append (membershipTableName);
		query.append(".nric from Membership ").append(membershipTableName).append(" where ");

		boolean firstRule = true;
		for (Rule rule : ruleList) {

			int subId = counter.getAndIncrement();

			if (!firstRule) {
				query.append(" and ");
			}

			if (Field.CESSATION_DATE == rule.getField()) {
				String paramId = "dateOfCessation" + subId;
				String columnName = membershipTableName + ".dateOfCessation";

				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.EFFECTIVE_DATE == rule.getField()) {

				String paramId = "effectiveDate" + subId;
				String columnName = membershipTableName + ".effectiveDate";

				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.EXPIRY_DATE == rule.getField()) {

				String paramId = "expiryDate" + subId;
				String columnName = membershipTableName + ".expiryDate";

				processDateFields (paramId, columnName, rule, query, properties);
			}
			else if (Field.MEMBERSHIP_STATUS == rule.getField()) {

				String paramId = "membershipStatus" + id;
				String columnName = membershipTableName + ".membershipStatus";

				processMembershipStatusFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.MEMBERSHIP_TYPE == rule.getField()) {

				String paramId = "membershipType" + id;
				String columnName = membershipTableName + ".membershipType";

				processMembershipTypeFields(paramId, columnName, rule, query, properties);
			}
			else if (Field.HAS_INSURANCE_COVERAGE == rule.getField()) {

				String paramId = "hasInsuranceCoverage" + id;
				String columnName = membershipTableName + ".hasInsuranceCoverage";

				processBooleanFields(paramId, columnName, rule, query, properties);
			}

			firstRule = false;
		}

		query.append(" ) ) ");
	}

	private static void processCourseRules (List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		query.append(" ( ");

		boolean firstRule = true;

		for (Rule rule : ruleList) {

			if (Field.ATTENDED_COURSE_TITLE == rule.getField()) {
				ListRule listRule = (ListRule) rule;

				if (!firstRule) {
					query.append(" and ");
				}

				int id = counter.getAndIncrement();
				String courseParticipantTable = "cp" + id;
				String paramId = "titleIds" + id;
				String courseTableName = "course" + id;

				query.append(" p.nric ");

				query.append(listRule.getOperator().toExpression());

				query.append(" (select distinct ").append(courseParticipantTable).append(".nric ");
				query.append("from CourseParticipant as ").append(courseParticipantTable);
				query.append(" where ").append(courseParticipantTable).append(".attended = true ");
				query.append("and ").append(courseParticipantTable).append(".courseId in ");
				query.append("(select ").append(courseTableName).append(".id from Course ").append(courseTableName);
				query.append(" where ").append(courseTableName).append(".titleId in (:").append(paramId);
				query.append("))");

				List<String> stringList = convertToStringList(listRule.getValues());
				properties.put(paramId, stringList);

				query.append(")");

				firstRule = false;
			}
		}

		query.append(")");
	}


	private static void processBlacklistRules (List<Rule> ruleList, StringBuilder query, Map<String, Object> properties, Counter counter) {

		query.append(" ( ");

		boolean firstRule = true;

		for (Rule rule : ruleList) {

			if (Field.BLACKLISTED_MODULE == rule.getField()) {

				if (!firstRule) {
					query.append(" and ");
				}

				ListRule listRule = (ListRule) rule;
				int id = counter.getAndIncrement();
				String blacklistTableName = "innerB" + id;
				String paramId = "modules" + id;

				query.append(" p.nric ");

				query.append(listRule.getOperator().toExpression());

				query.append(" (select distinct ").append (blacklistTableName);
				query.append(".nric from Blacklistee ").append(blacklistTableName).append(" where ");
				query.append("(").append(blacklistTableName).append(".effectiveDate is not null and ").append(blacklistTableName);
				query.append(".effectiveDate <= current_date ()) and (").append(blacklistTableName);
				query.append(".obsoleteDate is null or ").append(blacklistTableName).append(".obsoleteDate > current_date())");

				query.append(" and ").append(blacklistTableName).append(".module in ");
				query.append (" (:").append(paramId).append (")");

				List<String> stringList = convertToStringList(listRule.getValues());
				properties.put(paramId, stringList);

				query.append(" ) ");

				firstRule = false;
			}
		}

		query.append (" ) ");
	}


	private static String convertOperator(Operator operator) {

		if (Operator.EQUAL == operator) {
			return " = ";
		}
		else if (Operator.NOT_EQUAL == operator) {
			return " != ";
		}
		else if (Operator.GREATER_THAN == operator) {
			return " > ";
		}
		else if (Operator.GREATER_THAN_OR_EQUAL_TO == operator) {
			return " >= ";
		}
		else if (Operator.LESS_THAN == operator) {
			return " < ";
		}
		else if (Operator.LESS_THAN_OR_EQUAL_TO == operator) {
			return " <= ";
		}
		else {
			return " ";
		}

	}

	private static void processStringFields(String paramId, String columnName, Rule rule, StringBuilder query,
											Map<String, Object> properties) {

		if (RuleType.STRING == rule.getType()) {
			StringRule stringRule = (StringRule) rule;
			query.append(" ").append(columnName).append(" like :").append(paramId).append(" ");
			String value = stringRule.getValue();
			if (Operator.CONTAINS == stringRule.getOperator()) {
				value = "%" + value + "%";
			}
			properties.put(paramId, value);
		}
		else if (RuleType.LIST == rule.getType()) {

			ListRule listRule = (ListRule) rule;
			query.append(" ").append(columnName).append(" ");

			query.append(listRule.getOperator().toExpression());

			String listValueName = paramId + "_values";
			String listRuleName = paramId + "_lr";

			query.append(" (select ").append(listValueName).append (".value from ListRule as ");
			query.append(listRuleName).append (" inner join ").append (listRuleName).append(".values as ");
			query.append(listValueName).append(" where ").append(listRuleName).append (".id = :").append (paramId).append (" )");

			properties.put(paramId, listRule.getId());
		}
	}

	private static List<String> convertToStringList(List<ListRuleValue> values) {
		List<String> list = new ArrayList<String>();

		for (ListRuleValue value : values) {
			list.add(value.getValue());
		}

		return list;
	}

	private static void processCodeFields(String paramId, String columnName, Rule rule, StringBuilder query,
										  Map<String, Object> properties) {

		if (RuleType.CODE == rule.getType()) {
			CodeRule codeRule = (CodeRule) rule;

			query.append(" ").append(columnName);

			query.append(" ").append(codeRule.getOperator().toExpression()).append (" ");

			query.append(":").append(paramId).append(" ");
			String value = codeRule.getValue();
			properties.put(paramId, value);
		}
		else if (RuleType.LIST == rule.getType()) {

			ListRule listRule = (ListRule) rule;
			query.append(" ").append(columnName).append(" ");

			query.append(listRule.getOperator().toExpression());

			query.append(" (:").append(paramId).append(") ");

			List<String> stringList = convertToStringList(listRule.getValues());
			properties.put(paramId, stringList);
		}
	}

	private static void processDateFields (String paramId, String columnName, Rule rule, StringBuilder query,
										   Map<String, Object> properties) {

		if (RuleType.DATE == rule.getType()) {
			DateRule dateRule = (DateRule) rule;

			query.append(" ").append(columnName).append(" ").append(convertOperator(rule.getOperator()));
			query.append(" :").append(paramId).append(" ");

			properties.put(paramId, dateRule.getValue());
		}
		else if (RuleType.DURATION == rule.getType()) {
			DurationRule durationRule = (DurationRule) rule;

			Calendar cal = Calendar.getInstance();
			if (durationRule.getReference() != null) {
				cal.setTime(durationRule.getReference());
			}

			if (DurationUnit.DAYS == durationRule.getUnit()) {
				cal.add(Calendar.DATE, -(durationRule.getValue()));
			}
			else if (DurationUnit.MONTHS == durationRule.getUnit()) {
				cal.add(Calendar.MONTH, -(durationRule.getValue()));
			}
			else if (DurationUnit.YEARS == durationRule.getUnit()) {
				cal.add(Calendar.YEAR, -(durationRule.getValue()));
			}

			query.append(" :").append(paramId).append(" ").append(convertOperator(rule.getOperator()));
			query.append(" ").append(columnName).append(" ");

			properties.put(paramId, cal.getTime());
		}

	}


	private static void processChildCountField (Rule rule, StringBuilder query, Map<String, Object> properties, Counter counter) {

		if (RuleType.NUMBER == rule.getType()) {
			NumberRule numberRule = (NumberRule) rule;
			String paramId = "childCount" + counter.getAndIncrement();

			query.append(" p.children.size ").append(convertOperator(rule.getOperator()));
			query.append(" :").append(paramId).append(" ");

			properties.put(paramId, numberRule.getValue().intValue());
		}
	}

	private static void processChildDateOfBirthField (Rule rule, StringBuilder query, Map<String, Object> properties, Counter counter) {

		int id = counter.getAndIncrement();

		String childTableName = "child" + id;

		query.append(" p.nric in (select distinct ").append ("innerP").append(id);
		query.append(".nric from PersonalDetail innerP").append(id).append(" join innerP");
		query.append(id).append(".children ").append(childTableName).append(" where ");

		String paramId = "childDOB" + id;
		String columnName = childTableName + ".dateOfBirth";

		processDateFields (paramId, columnName, rule, query, properties);

		query.append(" ) ");
	}


	private static void processMembershipStatusFields(String paramId, String columnName, Rule rule, StringBuilder query,
													  Map<String, Object> properties) {

		if (RuleType.STRING == rule.getType()) {
			StringRule stringRule = (StringRule) rule;
			query.append(" ").append(columnName).append(" = :").append(paramId).append(" ");
			ActivationStatus status = ActivationStatus.get(stringRule.getValue());
			properties.put(paramId, status);
		}
		else if (RuleType.LIST == rule.getType()) {

			ListRule listRule = (ListRule) rule;
			query.append(" ").append(columnName).append(" ");

			query.append(listRule.getOperator().toExpression());

			query.append(" (:").append(paramId).append(") ");

			List<ActivationStatus> statusList = convertToActivationStatusList(listRule.getValues());
			properties.put(paramId, statusList);
		}
	}

	private static List<ActivationStatus> convertToActivationStatusList (List<ListRuleValue> values) {
		List<ActivationStatus> list = new ArrayList<ActivationStatus>();

		for (ListRuleValue value : values) {
			ActivationStatus status = ActivationStatus.get(value.getValue());
			list.add(status);
		}

		return list;
	}

	private static void processMembershipTypeFields(String paramId, String columnName, Rule rule, StringBuilder query,
													Map<String, Object> properties) {

		if (RuleType.STRING == rule.getType()) {
			StringRule stringRule = (StringRule) rule;
			query.append(" ").append(columnName).append(" = :").append(paramId).append(" ");
			MembershipType type = MembershipType.get(stringRule.getValue());
			properties.put(paramId, type);
		}
		else if (RuleType.LIST == rule.getType()) {

			ListRule listRule = (ListRule) rule;
			query.append(" ").append(columnName).append(" ");

			query.append(listRule.getOperator().toExpression());

			query.append(" (:").append(paramId).append(") ");

			List<MembershipType> statusList = convertToMembershipTypeList(listRule.getValues());
			properties.put(paramId, statusList);
		}
	}

	private static List<MembershipType> convertToMembershipTypeList (List<ListRuleValue> values) {
		List<MembershipType> list = new ArrayList<MembershipType>();

		for (ListRuleValue value : values) {
			MembershipType status = MembershipType.get(value.getValue());
			list.add(status);
		}

		return list;
	}

	private static void processBooleanFields(String paramId, String columnName, Rule rule, StringBuilder query,
											 Map<String, Object> properties) {

		if (RuleType.BOOLEAN == rule.getType()) {
			BooleanRule booleanRule = (BooleanRule) rule;

			query.append(" ").append(columnName).append(" = :").append(paramId).append(" ");
			properties.put(paramId, booleanRule.getValue());
		}
	}

	private static void processInterestFields (String id, Rule rule, StringBuilder query, Map<String, Object> properties) {

		if (RuleType.LIST == rule.getType()) {
			ListRule listRule = (ListRule) rule;

			String paramId = "interestList" + id;
			String innerPersonal = "innerP" + id;
			String interestAs = "interest" + id;

			query.append(" p.nric ");

			query.append(listRule.getOperator().toExpression());

			query.append(" (select distinct ").append (innerPersonal);
			query.append(".nric from PersonalDetail ").append(innerPersonal).append (" inner join ");
			query.append(innerPersonal).append(".subCategoryIds as ").append(interestAs);
			query.append(" where ").append (interestAs).append(" in (:").append(paramId).append("))");

			List<String> interestList = convertToStringList(listRule.getValues());
			properties.put(paramId, interestList);
		}
	}


	/**
	 * Inner class to hold a counter that value will be
	 * used as ID in the parameter name and table name
	 *
	 */
	private static class Counter {

		private int counter = 0;

		public int getAndIncrement () {
			return counter++;
		}
	}

}
