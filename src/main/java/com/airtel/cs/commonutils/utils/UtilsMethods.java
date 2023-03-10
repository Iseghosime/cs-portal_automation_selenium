package com.airtel.cs.commonutils.utils;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.dataproviders.databeans.AssignmentQueueRuleDataBeans;
import com.airtel.cs.commonutils.dataproviders.databeans.NftrDataBeans;
import com.airtel.cs.commonutils.dataproviders.databeans.SLARuleFileDataBeans;
import com.airtel.cs.commonutils.exceptions.RuleNotFoundException;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.actionconfig.ActionConfigResult;
import com.airtel.cs.model.cs.response.agentlimit.AgentLimit;
import com.airtel.cs.model.cs.response.agentlimit.LimitConfig;
import com.airtel.cs.model.cs.response.agents.AgentAttributes;
import com.airtel.cs.model.cs.response.agents.AgentDetailAttribute;
import com.airtel.cs.model.cs.response.agents.Authorities;
import com.airtel.cs.model.cs.response.agents.RoleDetails;
import com.airtel.cs.model.cs.response.consolelog.ChromeNetworkLog;
import com.airtel.cs.model.cs.response.kycprofile.KYCProfile;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Log4j2
public class UtilsMethods extends Driver {

    private static final String YESTERDAY = "Yesterday";
    private static final String TODAY = "Today";
    private static RequestSource api = new RequestSource();

    /**
     * This method use to add headers
     *
     * @param key   The Key
     * @param value The Value
     */
    public static void addHeaders(String key, String value) {
        map.add(new Header(key, value));
    }

    /**
     * This method use to replace headers
     *
     * @param key   The Key
     * @param value The Value
     */
    public static void replaceHeader(String key, String value) {
        map.removeIf((Header header) -> key.equals(header.getName()));
        addHeaders("sr-client-id", value);
    }

    /**
     * This method use to print response detail
     *
     * @param response The response object
     */
    public static void printResponseDetail(Response response) {
        commonLib.info("Then Response : " + response.asString());
        commonLib.info("And Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        commonLib.info("And Status Code: " + response.getStatusCode());
    }

    /**
     * This method use to print Get method api request
     *
     * @param queryable The Request query
     */
    public static void printGetRequestDetail(QueryableRequestSpecification queryable) {
        commonLib.info("When Request URL: " + queryable.getURI());
        log.info("And Request Headers are  : " + queryable.getHeaders());
        if (!(queryable.getQueryParams().toString() == null || queryable.getQueryParams().toString().equals("{}"))) {
            commonLib.info("Query Parameter is  : " + queryable.getQueryParams().toString());
        }
    }

    /**
     * This method use to print post method api request
     *
     * @param queryable The Post Request query
     */
    public static void printPostRequestDetail(QueryableRequestSpecification queryable) {
        commonLib.info("When Request URL: " + queryable.getURI());
        log.info("And Request Headers are  : " + queryable.getHeaders());
        commonLib.info("And Body is  : " + queryable.getBody().toString());
    }

    /**
     * This method is used to get date from epoch in given pattern
     *
     * @param epoch   The Epoch
     * @param pattern The Pattern
     * @return String The date
     */
    public static String getDateFromEpoch(long epoch, String pattern) {
        if (epoch == 0) {
            return "-";
        } else {
            Date date = new Date(epoch);
            DateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        }
    }

    /**
     * This method is used to convert the date to required pattern
     *
     * @param date            The date
     * @param newPatten       The new pattern date format
     * @param existingPattern The existing pattern date format
     * @return String The new date
     */
    public static String getDateFromString(String date, String newPatten, String existingPattern) {
        try {
            Date newDate = new SimpleDateFormat(existingPattern).parse(date);
            DateFormat format = new SimpleDateFormat(newPatten);
            return format.format(newDate);
        } catch (ParseException e) {
            commonLib.fail("Not able to parse the date: " + date + " " + e.fillInStackTrace(), true);
        }
        return "Invalid Date String";
    }

    /**
     * This method is used to convert given date date into utc time zone
     *
     * @param date            The date
     * @param existingPattern The existing pattern
     * @param newPattern      The new pattern
     * @return String The String
     */
    public static String getDateFromStringInUTC(String date, String existingPattern, String newPattern) {
        try {
            Date newDate = new SimpleDateFormat(existingPattern).parse(date);
            DateFormat format = new SimpleDateFormat(newPattern);
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            return format.format(newDate);
        } catch (ParseException e) {
            commonLib.fail("Not able to parse the date: " + date + " " + e.fillInStackTrace(), true);
        }
        return "Invalid Date String";
    }

    /**
     * This method used to convert epoch time into UTC date in given pattern
     *
     * @param epoch   The Epoch
     * @param pattern The pattern
     * @return String The date in UTC
     */
    public static String getDateFromEpochInUTC(long epoch, String pattern) {
        Date date = new Date(epoch);
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        return format.format(date);
    }

    /**
     * This method used to get time from epoch
     *
     * @param epoch   The epoch
     * @param pattern The pattern
     * @return String The String
     */
    public static String getTimeFromEpoch(long epoch, String pattern) {
        Date date = new Date(epoch);
        Date nearestMinute = DateUtils.round(date, Calendar.MINUTE);
        DateFormat format1 = new SimpleDateFormat(pattern);
        return format1.format(nearestMinute);
    }

    /**
     * This method is use to replace day name with date based on
     *
     * @param historyDateTime The date & Time with day name
     * @param pattern         The new Pattern
     * @param amount          The Today - number of days
     * @param dayName         The day name
     * @return String The date
     */
    public static String getTimeFromStringBasedOnDay(String historyDateTime, String pattern, Integer amount, String dayName) {
        final Calendar cal = Calendar.getInstance();
        String pattern1 = pattern.split("hh")[0].trim();
        DateFormat format1 = new SimpleDateFormat(pattern1);
        cal.add(Calendar.DATE, amount);
        String replaceDate = format1.format(cal.getTime());
        historyDateTime = historyDateTime.replace(dayName, replaceDate);
        commonLib.info(historyDateTime + " :" + replaceDate);
        return historyDateTime;
    }

    /**
     * This method used to check is first date is less than second date
     *
     * @param historyDateTime  first date
     * @param historyDateTime1 second date
     * @param pattern          date format pattern
     * @return true/false
     */
    public static boolean isSortOrderDisplay(String historyDateTime, String historyDateTime1, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            if (historyDateTime.contains(YESTERDAY))
                historyDateTime = getTimeFromStringBasedOnDay(historyDateTime, pattern, -1, YESTERDAY);
            if (historyDateTime1.contains(YESTERDAY))
                historyDateTime1 = getTimeFromStringBasedOnDay(historyDateTime1, pattern, -1, YESTERDAY);
            if (historyDateTime.contains(TODAY))
                historyDateTime = getTimeFromStringBasedOnDay(historyDateTime, pattern, 0, TODAY);
            if (historyDateTime1.contains(TODAY))
                historyDateTime1 = getTimeFromStringBasedOnDay(historyDateTime1, pattern, 0, TODAY);

            Date date1 = format.parse(historyDateTime);
            Date date2 = format.parse(historyDateTime1);
            if (date2.compareTo(date1) <= 0) {
                log.info(date2 + " come before " + date1);
                return true;
            } else {
                log.info(date2 + " come after " + date1);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            commonLib.fail("Date Patter does not same as date string Please check date pattern.", false);
            return false;
        }
    }

    /**
     * This method use to convert milliseconds to hour
     *
     * @param committedSla time in milliseconds
     * @return conversion milliseconds to hour
     */
    public static String convertToHR(String committedSla) {
        long ms = Long.parseLong(committedSla);
        final String valueOf = String.valueOf(TimeUnit.MILLISECONDS.toHours(ms));
        log.info("Converting SLA: " + committedSla + " to " + valueOf);
        return valueOf;
    }

    /**
     * This method is used to round off up to 2 decimal point
     *
     * @param value value to round off
     * @return value round off up to 2 digit
     */

    public static String valueRoundOff(Double value) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(value);
    }

    /**
     * This method is used to check today date and month is same as given date month and date
     *
     * @param birthDay birth date
     * @param pattern  date format in which birth date display
     * @return true/false whether today date same as birth date
     */

    public static Boolean isCustomerBirthday(String birthDay, String pattern) {
        String d = "dd-MM";
        DateFormat format1 = new SimpleDateFormat(d);
        String today = format1.format(Calendar.getInstance().getTime());
        Date customerBirthDate;
        try {
            customerBirthDate = new SimpleDateFormat(pattern).parse(birthDay);
            String customerBday = format1.format(customerBirthDate);
            return today.equalsIgnoreCase(customerBday);
        } catch (ParseException e) {
            commonLib.fail("Not able to parse the customer birth date :" + birthDay, false);
        }
        return false;
    }

    /**
     * This method use to check whether user has permission assign or not
     *
     * @param permissionName permission name to check
     * @return true/false based on user have permission or not
     */
    public static Boolean isUserHasPermission(String permissionName) {
        AgentDetailAttribute agentDetailAPI = api.getAgentDetail();
        if (agentDetailAPI.getStatusCode() != 200) {
            commonLib.fail("Not able to get Agent detail using agent api", false);
            return false;
        } else {
            List<Authorities> allPermissions = agentDetailAPI.getResult().getUserDetails().getUserDetails().getAuthorities();
            return allPermissions.stream().anyMatch(authorities -> authorities.getAuthority().equalsIgnoreCase(permissionName));
        }
    }

    /**
     * This method is use to check given string first char is Negative sign(-) or not
     *
     * @param value The value
     * @return true/false
     */
    public static Boolean isValueNegative(String value) {
        return !isNull(value) && value.trim().charAt(0) == '-';
    }

    /**
     * This method use to read authorization token from netwpork console log
     *
     * @return String The Auth Token
     * @throws IOException throw in-case of not able to read input stream properly
     */
    public static String getAuthTokenFromConsole() throws IOException {
        LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry entry : logEntries) {
            String consoleLog = entry.getMessage();
            if (consoleLog.contains(constants.getValue(CommonConstants.CONSOLE_NETWORK_LOG_EXTRA_INFO_TYPE)) && consoleLog.contains(constants.getValue(CommonConstants.API_AUTHORIZATION_KEY))) {
                ChromeNetworkLog obj = objectMapper.readValue(consoleLog, ChromeNetworkLog.class);
                if (!obj.getMessage().getParams().getHeaders().getAuthorization().isEmpty()) {
                    authToken = obj.getMessage().getParams().getHeaders().getAuthorization();
                    commonLib.pass("Token: " + authToken);
                    break;
                }
            }
        }
        return authToken;
    }

    /**
     * This method use to override existing headers value with new value which fetch from console log
     *
     * @throws IOException throw in-case of not able to read input stream properly
     */
    public static void getNewAddHeader() throws IOException {
        getAuthTokenFromConsole();
        map.clear();
        pages.getLoginPage().setCsAndDownstreamApiHeader();
        addHeaders(constants.getValue(CommonConstants.API_AUTHORIZATION_KEY), authToken);
        if (authToken == null || authToken.isEmpty()) {
            commonLib.fail("Not able to add new token into header as auth token empty", false);
        }
    }

    /**
     * This method use to get Assignment rule matching ticket meta Info attribute names
     *
     * @param rules          The Assignment rules based on category code
     * @param ticketMetaInfo The customer attribute names along with ticket meta info
     * @return AssignmentQueueRuleDataBeans The rule
     * @throws RuleNotFoundException In-case of no rules found in excel sheet based on category code
     */
    public static AssignmentQueueRuleDataBeans getAssignmentRule(PriorityQueue<AssignmentQueueRuleDataBeans> rules, NftrDataBeans ticketMetaInfo) throws RuleNotFoundException {
        if (CollectionUtils.isNotEmpty(rules)) {
            Map<String, String> ticketMetaInfoMap = objectMapper.convertValue(ticketMetaInfo, Map.class);
            for (AssignmentQueueRuleDataBeans metadataConfigs : rules) {
                if (Integer.parseInt(metadataConfigs.getRulePriority()) == -1) {
                    return metadataConfigs;
                }
                if (ticketMetaInfoMap.containsKey(metadataConfigs.getAttributeName()) && metadataConfigs.getAttributeValue().contains(ticketMetaInfoMap.get(metadataConfigs.getAttributeName()))) {
                    return metadataConfigs;
                }
            }
        } else {
            throw new RuleNotFoundException("No Matching rule found in excel sheet with given category code" + ticketMetaInfo.getIssueCode());
        }
        return null;
    }

    /**
     * This method use to get SLA Calculation rule matching ticket meta Info attribute names
     *
     * @param rules          The Assignment rules based on category code
     * @param ticketMetaInfo The customer attribute names along with ticket meta info
     * @return SLARuleFileDataBeans The rule
     * @throws RuleNotFoundException In-case of no rules found in excel sheet based on category code
     */
    public static SLARuleFileDataBeans getSLACalculationRule(List<SLARuleFileDataBeans> rules, NftrDataBeans ticketMetaInfo) throws RuleNotFoundException {
        SLARuleFileDataBeans defaultRule = null;
        if (CollectionUtils.isNotEmpty(rules)) {
            Map<String, String> ticketMetaInfoMap = objectMapper.convertValue(ticketMetaInfo, Map.class);
            for (SLARuleFileDataBeans metadataConfigs : rules) {
                Map<String, String> ticketSLARule = objectMapper.convertValue(metadataConfigs, Map.class);
                if (isSLARuleMatch(getSLAOverrideAttrNames(), ticketSLARule, ticketMetaInfoMap)) {
                    return metadataConfigs;
                }
                if (Boolean.parseBoolean(metadataConfigs.getDefaultRule())) {
                    defaultRule = metadataConfigs;
                }
            }
        } else {
            throw new RuleNotFoundException("No Matching rule found in excel sheet with given category code: " + ticketMetaInfo.getIssueCode());
        }
        return defaultRule;
    }

    /**
     * This method is use to get empty string in case of the given text is null
     *
     * @param text The original text
     * @return String The Value
     */
    public static String stringNotNull(String text) {
        return text == null ? "" : text;
    }

    /**
     * This method use to get SLA Override attributes name
     *
     * @return List The list of attribute name
     */
    public static List<String> getSLAOverrideAttrNames() {
        return Arrays.asList(constants.getValue(CommonConstants.CS_SLA_OVERRIDE_ATTRIBUTE_NAMES).split(","));
    }

    /**
     * This method use to get SLA Override attributes name & attribute value
     *
     * @return List The list of attributes
     */
    public static Map<String, String> getSLAOverrideAttrValues() {
        List<String> valueList = Arrays.asList(constants.getValue(CommonConstants.CS_SLA_OVERRIDE_DEFAULT_ATTRIBUTE_VALUES).split(","));
        List<String> nameList = getSLAOverrideAttrNames();
        Map<String, String> valuePair = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            valuePair.put(nameList.get(i), valueList.get(i));
        }
        return valuePair;
    }

    /**
     * This method is use to check all attribute value present in both ticket SLA rule and ticket meta info
     *
     * @param attributeNames    The attribute names
     * @param ticketSLARule     The ticket SLA Rule
     * @param ticketMetaInfoMap The ticket meta info
     * @return true/false
     */
    public static boolean isSLARuleMatch(List<String> attributeNames, Map<String, String> ticketSLARule, Map<String, String> ticketMetaInfoMap) {
        for (String attrName : attributeNames) {
            if (!stringNotNull(ticketSLARule.get(attrName)).equalsIgnoreCase(ticketMetaInfoMap.get(attrName)) && !stringNotNull(ticketSLARule.get(attrName)).equalsIgnoreCase(constants.getValue(CommonConstants.CS_SLA_ANY_ATTRIBUTE_NAME))) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is use to get ticket meta-info based on customer msisdn from KYC Profile API for SLA Calculation
     * Customer info is not able to fetch from KYC profile api than set default value for required parameter
     *
     * @param msisdn       The msisdn
     * @param ticketNumber The ticket number
     * @return NftrDataBeans object
     */
    public static NftrDataBeans setAllCustomerAttribute(String msisdn, String ticketNumber) {
        KYCProfile kycProfile = api.kycProfileAPITest(msisdn);
        NftrDataBeans nftrDataBeans = new NftrDataBeans();
        final Integer statusCode = kycProfile.getStatusCode();
        nftrDataBeans.setTicketNumber(ticketNumber);
        NftrDataBeans s = objectMapper.convertValue(getSLAOverrideAttrValues(), NftrDataBeans.class);
        String interactionChannel = getAgentDetail().getAdditionalDetails().getInteractionChannel().get(0).getName();
        interactionChannel = interactionChannel == null ? s.getInteractionChannel() : interactionChannel;
        if (statusCode == 200) {
            nftrDataBeans.setCustomerSegment(getCustomerAttribute(kycProfile.getResult().getSegment()));
            nftrDataBeans.setCustomerSubSegment(getCustomerAttribute(kycProfile.getResult().getSubSegment()));
            nftrDataBeans.setCustomerType(getCustomerAttribute(kycProfile.getResult().getCustomerType()));
            nftrDataBeans.setCustomerVip(String.valueOf(kycProfile.getResult().getVip()).toUpperCase());
            nftrDataBeans.setLineType(getCustomerAttribute(kycProfile.getResult().getLineType()));
            nftrDataBeans.setServiceCategory(getCustomerAttribute(kycProfile.getResult().getServiceCategory()));
        } else {
            nftrDataBeans.setCustomerSegment(s.getCustomerSegment());
            nftrDataBeans.setCustomerSubSegment(s.getCustomerSubSegment());
            nftrDataBeans.setCustomerType(s.getCustomerType());
            nftrDataBeans.setCustomerVip(s.getCustomerVip().toUpperCase());
            nftrDataBeans.setLineType(s.getLineType());
            nftrDataBeans.setServiceCategory(s.getServiceCategory());
        }
        nftrDataBeans.setInteractionChannel(interactionChannel);
        return nftrDataBeans;
    }

    /**
     * This method is used to get null string if given string is null or '-'
     *
     * @param attrValue The value
     * @return String The value
     */
    public static String getCustomerAttribute(String attrValue) {
        String value = stringNotNull(attrValue);
        return value.equals("-") || attrValue == null ? null : attrValue;
    }

    /**
     * This method is use to get SLA Workgroup name and time from rule
     *
     * @param slaRule The SLA Rule
     * @return Map<Workgroup, SLA>
     */
    public static Map<String, String> getWorkGroups(SLARuleFileDataBeans slaRule) {
        Map<String, String> workGroups = new HashMap<>();
        if (slaRule.getWorkgroup1() != null)
            workGroups.put(slaRule.getWorkgroup1(), slaRule.getSla1());
        if (slaRule.getWorkgroup2() != null)
            workGroups.put(slaRule.getWorkgroup2(), slaRule.getSla2());
        if (slaRule.getWorkgroup3() != null)
            workGroups.put(slaRule.getWorkgroup3(), slaRule.getSla3());
        if (slaRule.getWorkgroup4() != null)
            workGroups.put(slaRule.getWorkgroup4(), slaRule.getSla4());
        return workGroups;
    }

    /**
     * This method used to validate that text is not empty and not null
     *
     * @param text The text
     * @return true/false
     */
    public static boolean isNull(String text) {
        return text != null && !text.isEmpty();
    }

    /**
     * This method use to get Agent Details
     *
     * @return AgentAttributes
     */
    public static AgentAttributes getAgentDetail() {
        AgentDetailAttribute agentDetail = api.getAgentDetail();
        if (agentDetail.getStatusCode() != 200) {
            commonLib.fail(constants.getValue("cs.agent.detail.failure"), false);
        }
        return agentDetail.getResult();
    }

    /**
     * This method use to check whether user has role assign or not
     *
     * @param role    permission name to check
     * @return true/false based on user have roles or not
     */
    public static Boolean isUserHasRole(List<String> role) {
        AgentDetailAttribute agentDetailAPI = api.getAgentDetail();
        if (agentDetailAPI.getStatusCode() != 200) {
            commonLib.fail("Not able to get Agent detail using agent api", false);
            return false;
        } else {
            List<RoleDetails> allRoles = agentDetailAPI.getResult().getUserDetails().getUserDetails().getRole();
            return allRoles.stream().anyMatch(role::contains);
        }
    }


    /**
     * This method use to get Action config based on action key
     *
     * @param actionKey The action key
     * @return Object ActionConfigResult
     * @throws NullPointerException In-case of no Config found based on given key
     */
    public static ActionConfigResult getActionConfigBasedOnKey(String actionKey) {
        ActionConfigResult actionConfigResponse = api.getActionConfig(actionKey);
        if (actionConfigResponse != null) {
            return actionConfigResponse;
        } else {
            commonLib.fail(constants.getValue(CommonConstants.SEND_INTERNET_SETTING_ACTION_KEY) + " action key does not present in config API", false);
        }
        throw new NullPointerException("Action key does not found in config API");
    }

    /**
     * This method returns endDate in UTC timezone
     *
     * @param endDate
     * @return
     */
    public static Long getUTCEndDate(Long endDate) {
        LocalDate endDt = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime ldt = endDt.atTime(LocalTime.MAX).withNano(0);
        ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC);
        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date sd = null;
        try {
            sd = m_ISO8601Local.parse(zdt.toString());
        } catch (ParseException e) {
            commonLib.fail("error in parsing enddate" + e.getMessage(), false);
        }
        Long endDateEpoch = sd.getTime();
        return endDateEpoch;
    }

    /**
     * This method returns startDate in UTC timezone
     *
     * @param startDate
     * @return
     */
    public static Long getUTCStartDate(Long startDate) {
        LocalDate startDt = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime ldt = startDt.atStartOfDay();
        ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC);
        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date sd = new Date();
        try {
            sd = m_ISO8601Local.parse(zdt.toString());
        } catch (ParseException e) {
            commonLib.fail("error in parsing startdate" + e.getMessage(), false);
        }
        Long startDateEpoch = sd.getTime();
        return startDateEpoch;
    }

    /**
     * This method use to get Agent limit config based on action key and role Id
     *
     * @param actionKey The action key
     * @param roleId    The role id
     * @return Object LimitConfig
     */
    public static LimitConfig getAgentLimitConfigBasedOnKey(String actionKey, String roleId) {
        int statusCode = 0;
        AgentLimit agentLimitAPI = api.getAgentLimitConfig(roleId);
        if (ObjectUtils.isNotEmpty(agentLimitAPI)) {
            statusCode = agentLimitAPI.getStatusCode();
        }
        List<LimitConfig> limitConfigsList = agentLimitAPI.getResult();
        Optional<LimitConfig> limitConfigResultOP = limitConfigsList.stream()
                .filter(result -> actionKey.equals(result.getFeatureKey())).findFirst();
        assertCheck.append(actions.assertEqualIntType(statusCode, 200, "Agent Limit " + config.getProperty("cs.portal.api.success"), "Agent Limit " + config.getProperty("cs.portal.api.fail") + statusCode));
        if (limitConfigResultOP.isPresent()) {
            return limitConfigResultOP.get();
        } else {
            commonLib.info(actionKey + config.getProperty("cs.agent.limit.key.not.found"), false);
        }
        return null;
    }

    /**
     * This method use to get category hierarchy for auto assignment/un-assignment
     *
     * @return List The list of String
     */
    public static List<String> getCategoryHierarchy() {
        return Arrays.asList(constants.getValue(CommonConstants.AUTO_ASSIGNMENT_CATEGORY_HIERARCHY).split(","));
    }

    /**
     * This method use to write ticket id in properties file
     *
     * @param ticketId The ticket id
     */
    public static void setAutoAssignmentTicketId(String ticketId) {
        constants.setValue(CommonConstants.AUTO_ASSIGNMENT_TICKET_ID, ticketId);
    }

    /**
     * This method returns Date in UTC timezone in "ddMMyyyy" format
     *
     * @param date
     * @return
     */
    public static Long getDateInUtc(Long date, String dateType) {
        LocalDate dt = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime ldt = null;
        if (dateType.equalsIgnoreCase("startDate"))
            ldt = dt.atTime(LocalTime.MAX).withNano(0);
        else if (dateType.equalsIgnoreCase("endDate"))
            ldt = dt.atStartOfDay();
        else {
            commonLib.info("Date Type is not as expected");
        }
        ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC);
        DateFormat m_ISO8601Local = new SimpleDateFormat("ddMMyyyy");
        m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date sd = new Date();
        try {
            sd = m_ISO8601Local.parse(zdt.toString());
        } catch (ParseException e) {
            commonLib.fail("error in parsing date" + e.getMessage(), false);
        }
        Long startDateEpoch = sd.getTime();
        return startDateEpoch;
    }

    /**
     * This method is used to set ticket number
     *
     * @param ticketNumber The ticket number
     * @return NftrDataBeans object
     */
    public static NftrDataBeans setTicketId( String ticketNumber) {
        NftrDataBeans nftrDataBeans = new NftrDataBeans();
        nftrDataBeans.setTicketNumber(ticketNumber);
        return nftrDataBeans;
    }
}
