package tests;

import Utils.DataProviders.DataProviders;
import Utils.DataProviders.TestDatabean;
import Utils.ExtentReports.ExtentTestManager;
import Utils.PassUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.loginPagePOM;

import java.lang.reflect.Method;

public class SupervisorLoginTest extends BaseTest {

    @DataProviders.User(UserType = "ALL")
    @Test(priority = 1, description = "Logging IN", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void LoggingIN(Method method, TestDatabean Data) {
        ExtentTestManager.startTest("Logging Into Portal", "Logging Into Portal with AUUID :  " + Data.getLoginAUUID());
        SoftAssert softAssert = new SoftAssert();
        loginPagePOM loginPagePOM = new loginPagePOM(driver);
        loginPagePOM.openBaseURL(config.getProperty(tests.BaseTest.Env + "-baseurl"));
        softAssert.assertEquals(driver.getCurrentUrl(), config.getProperty(tests.BaseTest.Env + "-baseurl"), "URl isn't as expected");
        loginPagePOM.enterAUUID(Data.getLoginAUUID());//*[@id="mat-input-7"]
        loginPagePOM.clickOnSubmitBtn();
        loginPagePOM.enterPassword(PassUtils.decodePassword(Data.getPassword()));
        softAssert.assertTrue(loginPagePOM.checkLoginButton(), "Login Button is not enabled even after entering Passowrd");
        loginPagePOM.clickOnVisibleButton();
        loginPagePOM.clickOnVisibleButton();
        loginPagePOM.clickOnLogin();
        softAssert.assertAll();
    }

//    @Test(priority = 2, description = "Supervisor SKIP Login ")
//    public void agentSkipQueueLogin(Method method) {
//        ExtentTestManager.startTest("Supervisor SKIP Queue Login Test", "Supervisor SKIP Queue Login");
//        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening URL");
//        SideMenuPOM sideMenu = new SideMenuPOM(driver);
//        sideMenu.waitTillLoaderGetsRemoved();
//        sideMenu.clickOnSideMenu();
//        sideMenu.clickOnName();
//        agentLoginPagePOM AgentLoginPagePOM = sideMenu.openSupervisorDashboard();
//        SoftAssert softAssert = new SoftAssert();
//        AgentLoginPagePOM.waitTillLoaderGetsRemoved();
//        softAssert.assertTrue(AgentLoginPagePOM.isQueueLoginPage(),"Agent Does not redirect to Queue Login Page");
//        softAssert.assertTrue(AgentLoginPagePOM.checkSkipButton(),"Queue Login Page Does Not have SKIP button");
//        softAssert.assertTrue(AgentLoginPagePOM.checkSubmitButton(),"Queue Login Page have Does Not Submit button");
//        AgentLoginPagePOM.clickSkipBtn();
//        AgentLoginPagePOM.waitTillLoaderGetsRemoved();
//        Assert.assertEquals(driver.getTitle(), config.getProperty("supervisorTicketListPage"));
//        softAssert.assertAll();
//    }
//
//    @Test(priority = 3, description = "Supervisor Logging into Queue ")
//    public void agentQueueLogin(Method method) {
//        ExtentTestManager.startTest("Supervisor Logging into Queue", "Supervisor Logging into Queue");
//        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening URL");
//        SideMenuPOM sideMenu = new SideMenuPOM(driver);
//        sideMenu.waitTillLoaderGetsRemoved();
//        sideMenu.clickOnSideMenu();
//        sideMenu.clickOnName();
//        agentLoginPagePOM AgentLoginPagePOM = sideMenu.openSupervisorDashboard();
//        SoftAssert softAssert = new SoftAssert();
//        AgentLoginPagePOM.waitTillLoaderGetsRemoved();
//        softAssert.assertTrue(AgentLoginPagePOM.isQueueLoginPage(),"Agent Does not redirect to Queue Login Page");
//        softAssert.assertTrue(AgentLoginPagePOM.checkSkipButton(),"Queue Login Page Does Not have SKIP button");
//        softAssert.assertTrue(AgentLoginPagePOM.checkSubmitButton(),"Queue Login Page have Does Not Submit button");
//        AgentLoginPagePOM.clickSelectQueue();
//        AgentLoginPagePOM.selectAllQueue();
//        AgentLoginPagePOM.clickSubmitBtn();
//        AgentLoginPagePOM.waitTillLoaderGetsRemoved();
//        Assert.assertEquals(driver.getTitle(), config.getProperty("supervisorTicketListPage"),"User Does Not Redirect to Supervisor ticket List Page");
//        softAssert.assertAll();
//    }
//
//    @Test(priority = 4, description = "Verify there are 2 options displayed to select from in the Search Dropdown : 1) Ticket Id & 2) MSISDN", dataProviderClass = DataProviders.class)
//    public void validateTicketSearchOptions(Method method) {
//        ExtentTestManager.startTest("Validate Search Ticket Option", "Verify there are 2 options displayed to select from in the Search Dropdown : 1) Ticket Id & 2) MSISDN");
//        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening URL");
//        supervisorTicketListPagePOM ticketListPage = new supervisorTicketListPagePOM(driver);
//        SoftAssert softAssert = new SoftAssert();
//        ticketListPage.clickTicketOption();
//        List<String> list=ticketListPage.getListOfSearchOption();
//        softAssert.assertTrue(list.contains(config.getProperty("ticketOption")),config.getProperty("ticketOption")+" option does not found in list.");
//        softAssert.assertTrue(list.contains(config.getProperty("msisdnOption")),config.getProperty("msisdnOption")+" option does not found in list.");
//        softAssert.assertAll();
//    }

}
