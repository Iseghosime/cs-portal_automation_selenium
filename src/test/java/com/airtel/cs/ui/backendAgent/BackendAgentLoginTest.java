package com.airtel.cs.ui.backendAgent;

import com.airtel.cs.common.actions.BaseActions;
import com.airtel.cs.commonutils.PassUtils;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.TestDatabean;
import com.airtel.cs.driver.Driver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class BackendAgentLoginTest extends Driver {

    private final BaseActions actions = new BaseActions();

    @BeforeMethod
    public void checkExecution() {
        if (continueExecutionFA) {
            assertCheck.append(actions.assertEqual_boolean(continueExecutionFA, true, "Proceeding for test case as user able to login over portal", "Skipping tests because user not able to login into portal or Role does not assign to user"));
        } else {
            commonLib.skip("Skipping tests because user not able to login into portal or Role does not assign to user");
            assertCheck.append(actions.assertEqual_boolean(continueExecutionFA, false, "Skipping tests because user not able to login into portal or Role does not assign to user"));
            throw new SkipException("Skipping tests because user not able to login into portal or Role does not assign to user");
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }

    @DataProviders.User(userType = "BA")
    @Test(priority = 1, description = "Logging IN ", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void loggingIN(TestDatabean data) throws InterruptedException {
        selUtils.addTestcaseDescription("Logging Into Portal with AUUID :  " + data.getLoginAUUID(), "description");
        SoftAssert softAssert = new SoftAssert();
        final String value = constants.getValue(ApplicationConstants.DOMAIN_URL);
        pages.getLoginPage().openBaseURL(value);
        softAssert.assertEquals(driver.getCurrentUrl(), config.getProperty(evnName + "-baseurl"), "URl isn't as expected");
        pages.getLoginPage().waitTillLoaderGetsRemoved();
        pages.getLoginPage().enterAUUID(data.getLoginAUUID());//*[@id="mat-input-7"]
        pages.getLoginPage().clickOnSubmitBtn();
        pages.getLoginPage().enterPassword(PassUtils.decodePassword(data.getPassword()));
        softAssert.assertTrue(pages.getLoginPage().checkLoginButton(), "Login Button is not enabled even after entering Passowrd");
        pages.getLoginPage().clickOnVisibleButton();
        pages.getLoginPage().clickOnVisibleButton();
        pages.getLoginPage().clickOnLogin();
        Thread.sleep(20000); // wait for 20 Seconds for Dashboard page In case of slow Network slow
        if (pages.getSideMenuPage().isSideMenuVisible()) {
            pages.getSideMenuPage().clickOnSideMenu();
            if (!pages.getSideMenuPage().isAgentDashboard()) {
                continueExecutionBA = false;
                softAssert.fail("Agent Dashboard does not Assign to User Visible.Please Assign Role to user.");
            } else {
                continueExecutionBA = true;
            }
            pages.getSideMenuPage().clickOnSideMenu();
        } else {
            continueExecutionBA = false;
            softAssert.fail("Agent Dashboard does Open with user.Check for the ScreenShot.");
        }
        pages.getSideMenuPage().waitTillLoaderGetsRemoved();
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Backend Agent Login into Queue", dependsOnMethods = "loggingIN")
    public void agentQueueLogin() {
        selUtils.addTestcaseDescription("Backend Agent Login into Queue", "description");
        commonLib.info("Opening URL");
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenuPage().waitTillLoaderGetsRemoved();
        softAssert.assertTrue(pages.getSideMenuPage().isSideMenuVisible());
        pages.getSideMenuPage().clickOnSideMenu();
        pages.getSideMenuPage().clickOnUserName();
        pages.getSideMenuPage().openBackendAgentDashboard();
        pages.getAgentLoginPage().waitTillLoaderGetsRemoved();
        softAssert.assertTrue(pages.getAgentLoginPage().isQueueLoginPage());
        softAssert.assertTrue(pages.getAgentLoginPage().checkSubmitButton());
        pages.getAgentLoginPage().clickSelectQueue();
        pages.getAgentLoginPage().selectAllQueue();
        pages.getAgentLoginPage().clickSubmitBtn();
        pages.getAgentLoginPage().waitTillLoaderGetsRemoved();
        Assert.assertEquals(driver.getTitle(), config.getProperty("backendAgentTicketListPage"), "Backend Agent Does not Redirect to Ticket List Page");
        softAssert.assertAll();
    }

    @Test(priority = 3, dependsOnMethods = "agentQueueLogin", description = "Ticket Search ")
    public void validateTicket() {
        selUtils.addTestcaseDescription("Backend Agent Validate Ticket List Page", "description");
        SoftAssert softAssert = new SoftAssert();
        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
        softAssert.assertTrue(pages.getSupervisorTicketList().isTicketIdLabel(), "Ticket Meta Data Does Not Have Ticket Id");
        softAssert.assertTrue(pages.getSupervisorTicketList().isWorkGroupName(), "Ticket Meta Data Does Not  Have Workgroup");
        softAssert.assertTrue(pages.getSupervisorTicketList().isPrioritylabel(), "Ticket Meta Data  Does Not  Have Priority");
        softAssert.assertTrue(pages.getSupervisorTicketList().isStateLabel(), "Ticket Meta Data Does Not  Have State");
        softAssert.assertTrue(pages.getSupervisorTicketList().isCreationDateLabel(), "Ticket Meta Data Does Not Have Creation Date");
        softAssert.assertTrue(pages.getSupervisorTicketList().isCreatedByLabel(), "Ticket Meta Data Does Not Have Created By");
        softAssert.assertTrue(pages.getSupervisorTicketList().isQueueLabel(), "Ticket Meta Data Have Does Not Queue");
        softAssert.assertTrue(pages.getSupervisorTicketList().isIssueLabel(), "Ticket Meta Data Have Does Not Issue");
        softAssert.assertTrue(pages.getSupervisorTicketList().isIssueTypeLabel(), "Ticket Meta Data Does Not Have Issue Type");
        softAssert.assertTrue(pages.getSupervisorTicketList().isSubTypeLabel(), "Ticket Meta Data Does Not Have Issue Sub Type");
        softAssert.assertTrue(pages.getSupervisorTicketList().isSubSubTypeLabel(), "Ticket Meta Data Does Not Have Issue Sub Sub Type");
        softAssert.assertTrue(pages.getSupervisorTicketList().isCodeLabel(), "Ticket Meta Data Does Not Have Code");
        softAssert.assertFalse(pages.getSupervisorTicketList().getMSISDN().isEmpty(), "MSISDN Can not be empty");
        softAssert.assertAll();
    }
}
