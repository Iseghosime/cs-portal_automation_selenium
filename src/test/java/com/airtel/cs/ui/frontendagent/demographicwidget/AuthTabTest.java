package com.airtel.cs.ui.frontendagent.demographicwidget;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.common.actions.BaseActions;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.dataproviders.ActionTagDataBeans;
import com.airtel.cs.commonutils.dataproviders.AuthTabDataBeans;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.QuestionAnswerKeyDataBeans;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.response.configuration.ConfigurationPOJO;
import com.airtel.cs.pojo.response.configuration.LockedSection;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class AuthTabTest extends Driver {

    RequestSource api = new RequestSource();
    Map<String, String> authTabConfig;
    public BaseActions actions = new BaseActions();

    @BeforeMethod
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @DataProviders.User(userType = "NFTR")
    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            final String customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().clickOnUserName();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(customerNumber);
            pages.getMsisdnSearchPage().clickOnSearch();
            final boolean pageLoaded = pages.getCustomerProfilePage().isCustomerProfilePageLoaded();
            assertCheck.append(actions.assertEqual_boolean(pageLoaded, true, "Customer Profile Page Loaded Successfully", "Customer Profile Page NOT Loaded"));
            if (!pageLoaded) continueExecutionFA = false;
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openCustomerInteraction" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void validateAnswerQuestionConfig() {
        try {
            selUtils.addTestcaseDescription("Jira id - CSP-63443,Verify that the answers of the questions in pop up should either show data from configuration or show inline spinner", "description");
            ConfigurationPOJO config = api.getConfiguration("authorization_data");
            authTabConfig = config.getResult().getAuthDataConfig();
            final String statusCode = config.getStatusCode();
            assertCheck.append(actions.assertEqual_stringType(statusCode, "200", "Config API Status Code is as Expected and is :" + statusCode, "Config API Status Code is NOT as Expected and is :" + statusCode));
            for (Map.Entry mapElement : authTabConfig.entrySet()) {
                String key = (String) mapElement.getKey();
                String value = mapElement.getValue().toString();
                commonLib.info(key + " :" + value);
                assertCheck.append(actions.assertEqual_stringNotNull(value, "Question Answer values are present For Question Key :" + key + "and value is :" + value, "For Question Key '" + key + "' value is missing. Please configure the same"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method :- validateAnswerQuestionConfig" + e.fillInStackTrace(), false);
        }
    }

    @Test(priority = 3, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void validateAnswerKey() {
        try {
            selUtils.addTestcaseDescription("Verify the question Answer as Per Config", "description");
            DataProviders dataProviders = new DataProviders();
            List<QuestionAnswerKeyDataBeans> config = dataProviders.getQuestionAnswerKey();
            for (QuestionAnswerKeyDataBeans questionAnswer : config) {
                final String questionKey = questionAnswer.getQuestionKey();
                commonLib.info("Question Key: '" + questionKey + "' ; Answer Found in API: '" + authTabConfig.get(questionKey));
                final String answerKey = questionAnswer.getAnswerKey();
                if (authTabConfig.get(questionKey) != null) {
                    assertCheck.append(actions.assertEqual_stringType(authTabConfig.get(questionKey), questionAnswer.getAnswerKey(), "Answer Key Validated and is :" + questionKey, "Answer key is not expected for Question: " + questionKey));
                } else {
                    commonLib.fail("Question Key does not found in Database but present in config sheet.", true);
                }
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method :- validateAnswerKey" + e.fillInStackTrace(), false);
        }
    }

    @Test(priority = 4, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void validateLockedSectionStatus() {
        try {
            selUtils.addTestcaseDescription("Jira id - CSP-63442,Verify that there is a authorization pop for the actions like SIM Bar Unbar, PIN reset", "description");
            DataProviders dataProviders = new DataProviders();
            ConfigurationPOJO config = api.getConfiguration("locked_sections_keys");
            List<LockedSection> lockedSection = config.getResult().getLockedSectionsKeysConfig();
            final String statusCode = config.getStatusCode();
            assertCheck.append(actions.assertEqual_stringType(statusCode, "200", "Config API Status Code is as Expected and is :" + statusCode, "Config API Status Code is NOT as Expected and is :" + statusCode));
            List<ActionTagDataBeans> actionTags = dataProviders.getActionTag();
            for (LockedSection ls : lockedSection) {
                final String key = ls.getKey();
                final Boolean isAuthenticated = ls.getIsAuthenticated();
                commonLib.info(key + " : " + isAuthenticated);
                for (ActionTagDataBeans at : actionTags) {
                    if (isAuthenticated != Boolean.parseBoolean(at.getIsAuth())) {
                        commonLib.fail("Action does not locked but as per config Action must be locked.", true);
                        break;
                    } else if (ls.getIsAuthenticated() == Boolean.parseBoolean(at.getIsAuth())) {
                        commonLib.pass("Action Verified " + at.getActionTagName());
                    }
                }
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method :- validateLockedSectionStatus" + e.fillInStackTrace(), false);
        }
    }

    @Test(priority = 5, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = "openCustomerInteraction")
    public void validateAuthTab() throws InterruptedException {
        try {
            selUtils.addTestcaseDescription("Verify the Authentication tab", "description");
            if (pages.getDemoGraphicPage().isPUKInfoLocked()) {
                pages.getDemoGraphicPage().clickPUKToUnlock();
            }
            DataProviders data = new DataProviders();
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isAuthTabLoad(), true, "Authentication tab loaded correctly", "Authentication tab does not load correctly"));
            Map<String, String> questionList = pages.getAuthTabPage().getQuestionAnswer();
            List<AuthTabDataBeans> list = data.getPolicy();
            List<String> questions = data.getPolicyQuestion();
            assertCheck.append(actions.assertEqual_stringType(pages.getAuthTabPage().getAuthInstruction().toLowerCase().trim(), list.get(0).getPolicyMessage().toLowerCase().trim(), "Policy Message same as configured", "Policy Message not same as configured"));
            for (String s : questions) {
                String trim = s.replaceAll("[^a-zA-Z]+", "").toLowerCase().trim();
                if (!questionList.containsKey(trim)) {
                    commonLib.fail(s + " :Question must configured on UI as present in config sheet", true);
                }
                questionList.remove(trim);
            }
            if (questionList.isEmpty()) {
                commonLib.pass("All Questions correctly configured and display on UI.");
            } else {
                for (Map.Entry<String, String> mapElement : questionList.entrySet()) {
                    commonLib.fail(mapElement.getKey() + " Question Display on UI but does not present in config sheet.", true);
                    commonLib.fail(mapElement.getKey() + " :Question Display on UI but does not present in config sheet.", true);
                }
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | AssertionError e) {
            commonLib.fail("Exception in Method :- validateAuthTab" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 6, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void validateAuthTabMinQuestion() throws InterruptedException {
        try {
            selUtils.addTestcaseDescription("Verify the Authentication tab Minimum question Configured correctly", "description");
            DataProviders data = new DataProviders();
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isAuthTabLoad(), true, "Authentication tab loaded correctly", "Authentication tab does not load correctly"));
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isAuthBtnEnable(), false, "Authenticate button in NOT enabled without choosing minimum number of question", "Authenticate button is enable without choosing minimum number of question."));
            pages.getDemoGraphicPage().selectPolicyQuestion();
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isAuthBtnEnable(), true, "Authenticate button is enabled", "Authenticate Button does not enable after choose minimum number of question"));
            pages.getAuthTabPage().clickCloseBtn();
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method :- validateAuthTabMinQuestion" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 7, groups = {"SanityTest", "RegressionTest"}, dependsOnMethods = "validateAuthTabMinQuestion")
    public void authCustomer() {
        try {
            selUtils.addTestcaseDescription("Authenticate User", "description");
            DataProviders data = new DataProviders();
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isSIMBarPopup(), true, "SIM Bar/Unbar pop up opened", "SIM Bar/Unbar popup does not open"));
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isIssueDetailTitleVisible(), true, "Issue details configured correctly", "Issue Detail does not configured"));
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isSubmitBtnEnable(), false, "Submit button Not enabled without comment", "Submit button enable without adding comment"));
            pages.getAuthTabPage().clickSelectReasonDropDown();
            List<String> reason = pages.getAuthTabPage().getReasonConfig();
            List<String> configReason = data.issueDetailReason("SIM Bar Unbar");
            for (String s : reason) {
                if (!configReason.contains(s)) {
                    commonLib.fail(s + ": Must not configured on UI as not mentioned in config.", true);
                }
                configReason.remove(s);
            }

            for (String s : configReason) {
                commonLib.fail(s + ": Must configured on UI as mentioned in config.", true);
            }
            pages.getAuthTabPage().chooseReason();
            pages.getAuthTabPage().enterComment("Adding comment using Automation");
            assertCheck.append(actions.assertEqual_boolean(pages.getAuthTabPage().isSubmitBtnEnable(), true, "Submit button does enabled after adding comment", "Submit button does NOT enabled after adding comment"));
            pages.getAuthTabPage().closeSIMBarPopup();
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException e) {
            commonLib.fail("Exception in Method :- authCustomer" + e.fillInStackTrace(), true);
            pages.getAuthTabPage().closeSIMBarPopup();
        }
    }
}