package com.airtel.cs.ui.usermanagement;

import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.TestDatabean;
import com.airtel.cs.commonutils.extentreports.ExtentTestManager;
import com.airtel.cs.driver.Driver;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class UserManagementTest extends Driver {

    int currentBucketSize;

    @BeforeMethod
    public void checkExecution() {
        SoftAssert softAssert = new SoftAssert();
        if (!continueExecutionFA) {
            softAssert.fail("Terminate Execution as user not able to login into portal or Role does not assign to user. Please do needful.");
        }
        softAssert.assertAll();
    }

    @Test(priority = 1)
    public void openUserManagementPage() {
        ExtentTestManager.startTest("Validating User Management", "Validating User Management");
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenu().clickOnSideMenu();
        pages.getSideMenu().clickOnName();
        pages.getSideMenu().openUserManagementPage();
        pages.getUserManagementPage().waitTillUMPageLoaded();
        softAssert.assertTrue(pages.getUserManagementPage().isSearchVisible());
        softAssert.assertAll();
    }

    @Test(priority = 2,dependsOnMethods = "openUserManagementPage")
    public void validateAddToUser() throws InterruptedException {
        ExtentTestManager.startTest("Validating Add to User Management page", "Validating Add to User Management page");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().clickAddUserBtn();
        Thread.sleep(60000);
        pages.getUserManagementPage().switchFrameToAddUser();
        softAssert.assertTrue(pages.getUserManagementPage().checkingAddUser(),"Add to user page does not open");
        softAssert.assertAll();
    }

    @DataProviders.User()
    @Test(priority = 3, dependsOnMethods = "openUserManagementPage", description = "Validating User Management Edit Page", dataProviderClass = DataProviders.class, dataProvider = "loginData")
    public void openEditUserPage(TestDatabean data) {
        ExtentTestManager.startTest("Validating User Management Edit Page", "Validating User Management Edit Page and Search Auuid Functionality  ");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().waitTillLoaderGetsRemoved();
        pages.getUserManagementPage().searchAuuid(data.getLoginAUUID());
        pages.getUserManagementPage().clickSearchButton();
        pages.getUserManagementPage().waitUntilResultPageIsVisible();
        softAssert.assertEquals(pages.getUserManagementPage().resultIsVisible(data.getLoginAUUID()), data.getLoginAUUID());
        currentBucketSize = Integer.parseInt(pages.getUserManagementPage().getCurrentTicketBucketSize());
        pages.getUserManagementPage().clickViewEditButton();
        pages.getUserManagementPage().waitUntilEditPageIsOpen();
        softAssert.assertAll();

    }


    @Test(priority = 4, dependsOnMethods = "openEditUserPage", description = "Validating User Management Edit User : Interaction Channel")
    public void getInteractionChannel() throws InterruptedException {
        ExtentTestManager.startTest("Validating User Management Edit User : Interaction Channel", "Validating User Management Edit User : Interaction Channel");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().openListInteractionChannels();
        ArrayList<String> strings = pages.getUserManagementPage().getInteractionChannels();
        try {
            pages.getUserManagementPage().pressESC();
        } catch (NoSuchElementException | TimeoutException e) {
            pages.getUserManagementPage().clickOutside();
        }
        DataProviders data = new DataProviders();
        List<String> interactionChannel = data.getInteractionChannelData();
        for (String s : strings) {
            if (interactionChannel.contains(s)) {
                ExtentTestManager.getTest().log(LogStatus.INFO, "Validate " + s + " interaction channel is display correctly");
                interactionChannel.remove(s);
            } else {
                ExtentTestManager.getTest().log(LogStatus.FAIL, s + " interaction channel must not display on frontend as tag not mention in config sheet.");
                softAssert.fail(s + " interaction channel should not display on UI as interaction channel not mention in config sheet.");
            }
        }
        if (interactionChannel.isEmpty()) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "All interaction channel correctly configured and display on UI.");
        } else {
            for (String element : interactionChannel) {
                ExtentTestManager.getTest().log(LogStatus.FAIL, element + " interaction channel does not display on UI but present in config sheet.");
                softAssert.fail(element + " interaction channel does not display on UI but present in config sheet.");
            }
        }
        softAssert.assertAll();

    }


    @Test(priority = 5, dependsOnMethods = "openEditUserPage", description = "Validating User Management Edit User : Work Flows")
    public void getWorkflows() throws InterruptedException {
        ExtentTestManager.startTest("Validating User Management Edit User : Work Flows", "Validating User Management Edit User : Work Flows");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().openWorkgroupList();
        ArrayList<String> strings = pages.getUserManagementPage().getWorkflows();
        try {
            pages.getUserManagementPage().pressESC();
        } catch (NoSuchElementException | TimeoutException e) {
            pages.getUserManagementPage().clickOutside();
        }
        DataProviders data = new DataProviders();
        List<String> workFlow = data.getWorkFlowData();
        for (String s : strings) {
            if (workFlow.contains(s)) {
                ExtentTestManager.getTest().log(LogStatus.INFO, "Validate " + s + " workgroup is display correctly");
                workFlow.remove(s);
            } else {
                ExtentTestManager.getTest().log(LogStatus.FAIL, s + " workgroup must not display on frontend as tag not mention in config sheet.");
                softAssert.fail(s + " workgroup should not display on UI as interaction channel not mention in config sheet.");
            }
        }
        if (workFlow.isEmpty()) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "All workgroup correctly configured and display on UI.");
        } else {
            for (String element : workFlow) {
                ExtentTestManager.getTest().log(LogStatus.FAIL, element + " workgroup does not display on UI but present in config sheet.");
                softAssert.fail(element + " workgroup does not display on UI but present in config sheet.");
            }
        }
        softAssert.assertAll();

    }

    @Test(priority = 6, dependsOnMethods = "openEditUserPage", description = "Validating User Management Edit User : Login Queue")
    public void getLoginQueue() throws InterruptedException {
        ExtentTestManager.startTest("Validating User Management Edit User : Login Queue", "Validating User Management Edit User :  Login Queue");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().openLoginQueueList();
        ArrayList<String> strings = pages.getUserManagementPage().getLoginQueues();
        try {
            pages.getUserManagementPage().pressESC();
        } catch (NoSuchElementException | TimeoutException e) {
            pages.getUserManagementPage().clickOutside();
        }
        DataProviders data = new DataProviders();
        List<String> loginQueue = data.getLoginQueueData();
        for (String s : strings) {
            if (loginQueue.contains(s)) {
                ExtentTestManager.getTest().log(LogStatus.INFO, "Validate " + s + " ticketPool is display correctly");
                loginQueue.remove(s);
            } else {
                ExtentTestManager.getTest().log(LogStatus.FAIL, s + " ticketPool must not display on frontend as tag not mention in config sheet.");
                softAssert.fail(s + " ticketPool should not display on UI as interaction channel not mention in config sheet.");
            }
        }
        if (loginQueue.isEmpty()) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "All ticketPool correctly configured and display on UI.");
        } else {
            for (String element : loginQueue) {
                ExtentTestManager.getTest().log(LogStatus.FAIL, element + " ticketPool does not display on UI but present in config sheet.");
                softAssert.fail(element + " ticketPool does not display on UI but present in config sheet.");
            }
        }
        softAssert.assertAll();

    }

    @DataProviders.User()
    @Test(priority = 7, dependsOnMethods = "openEditUserPage", description = "Validating Bucket Size", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void changeBucketSize(TestDatabean data) {
        ExtentTestManager.startTest("Validating Bucket Size", "Validating Bucket Size");
        SoftAssert softAssert = new SoftAssert();
        pages.getUserManagementPage().setTicketBucketSize(currentBucketSize + 1);
        pages.getUserManagementPage().clickUpdateButton();
        pages.getUserManagementPage().waitTillLoaderGetsRemoved();
        pages.getUserManagementPage().searchAuuid(data.getLoginAUUID());
        pages.getUserManagementPage().clickSearchButton();
        pages.getUserManagementPage().waitUntilResultPageIsVisible();
        softAssert.assertEquals(pages.getUserManagementPage().resultIsVisible(data.getLoginAUUID()), data.getLoginAUUID());
        softAssert.assertEquals(Integer.parseInt(pages.getUserManagementPage().getCurrentTicketBucketSize()), currentBucketSize + 1, "Updated Bucket Size is not as Expected");
        softAssert.assertAll();

    }

}