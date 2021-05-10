package com.airtel.cs.ui.frontendagent;

import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.TestDatabean;
import com.airtel.cs.commonutils.extentreports.ExtentTestManager;
import com.airtel.cs.driver.Driver;
import com.aventstack.extentreports.Status;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class WidgetTaggedIssueTest extends Driver {

    @BeforeMethod
    public void checkExecution() {
        SoftAssert softAssert = new SoftAssert();
        if (!continueExecutionFA) {
            softAssert.fail("Terminate Execution as user not able to login into portal or Role does not assign to user. Please do needful.");
        }
        softAssert.assertAll();
    }

    @DataProviders.User(userType = "NFTR")
    @Test(priority = 1, description = "Validate Customer Interaction Page", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void openCustomerInteraction(TestDatabean data) {
        final String customerNumber = data.getCustomerNumber();
        selUtils.addTestcaseDescription("Validating the Search forCustomer Interactions :" + customerNumber, "description");
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenuPage().clickOnSideMenu();
        pages.getSideMenuPage().clickOnUserName();
        pages.getSideMenuPage().openCustomerInteractionPage();
        pages.getMsisdnSearchPage().enterNumber(customerNumber);
        pages.getMsisdnSearchPage().clickOnSearch();
        softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Validating Current Plan widget tagged issue")
    public void currentPlanWidgetTagIssue() {
        selUtils.addTestcaseDescription("Validating Current Plan widget tagged issue", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("currentPlan"));
        softAssert.assertTrue(pages.getCurrentBalanceWidgetPage().isCurrentBalanceWidgetVisible(), "Current Balance Widget is not visible ");
        String widgetName = pages.getCurrentBalanceWidgetPage().getWidgetTitle();
        pages.getCurrentBalanceWidgetPage().clickTicketIcon();
        softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
        try {
            if (pages.getWidgetInteraction().checkNoInteractionTag()) {
                ExtentTestManager.getTest().log(Status.INFO, "No Interaction tagged with widget");
            } else {
                List<String> visibleIssueList = pages.getWidgetInteraction().getListOfIssue();
                try {
                    for (String s : visibleIssueList) {
                        if (tagIssue.containsKey(s)) {
                            ExtentTestManager.getTest().log(Status.PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                            tagIssue.remove(s);
                            pages.getWidgetInteraction().clickIssueLabel(s);
                            pages.getWidgetInteraction().writeComment("Comment added using test automation");
                            pages.getWidgetInteraction().clickSubmitBtn();
                            pages.getWidgetInteraction().interactionTabClosed();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getCustomerProfilePage().goToViewHistory();
                            pages.getViewHistory().clickOnInteractionsTab();
                            String issueCode = pages.getViewHistory().getLastCreatedIssueCode();
                            softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                            pages.getViewHistory().openingCustomerInteractionDashboard();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getCurrentBalanceWidgetPage().clickTicketIcon();
                            widgetName = pages.getCurrentBalanceWidgetPage().getWidgetTitle();
                            softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                        } else {
                            ExtentTestManager.getTest().log(Status.FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                        }
                    }
                } catch (NoSuchElementException e) {
                    ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                    e.printStackTrace();
                    softAssert.fail("Issue Creation Failed");
                }
            }
            if (tagIssue.isEmpty()) {
                ExtentTestManager.getTest().log(Status.PASS, "All issue tagged to widget correctly configured and display on UI.");
            } else {
                for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                    ExtentTestManager.getTest().log(Status.FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                    softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
            softAssert.fail("Interaction tab does not open correctly");
        }
        pages.getWidgetInteraction().closeInteractionTab();
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Validating Recharge History widget tagged issue")
    public void rechargeHistoryWidgetTagIssue() {
        selUtils.addTestcaseDescription("Validating Recharge History widget tagged issue", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("rechargeHistory"));
        softAssert.assertTrue(pages.getRechargeHistoryWidget().isRechargeHistoryWidgetIsVisible(), "Recharge History Widget is not visible ");
        String widgetName = pages.getRechargeHistoryWidget().getWidgetTitle();
        pages.getRechargeHistoryWidget().clickTicketIcon();
        softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
        try {
            if (pages.getWidgetInteraction().checkNoInteractionTag()) {
                ExtentTestManager.getTest().log(Status.INFO, "No Interaction tagged with widget");
            } else {
                List<String> visibleIssueList = pages.getWidgetInteraction().getListOfIssue();
                try {
                    for (String s : visibleIssueList) {
                        if (tagIssue.containsKey(s)) {
                            ExtentTestManager.getTest().log(Status.PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                            tagIssue.remove(s);
                            pages.getWidgetInteraction().clickIssueLabel(s);
                            pages.getWidgetInteraction().writeComment("Comment added using test automation");
                            pages.getWidgetInteraction().clickSubmitBtn();
                            pages.getWidgetInteraction().interactionTabClosed();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded(), "Customer interaction page does not loaded correctly");
                            pages.getCustomerProfilePage().goToViewHistory();
                            pages.getViewHistory().clickOnInteractionsTab();
                            String issueCode = pages.getViewHistory().getLastCreatedIssueCode();
                            softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase(), "Issue code for category label " + s + " does not configured correctly.");
                            pages.getViewHistory().openingCustomerInteractionDashboard();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getRechargeHistoryWidget().clickTicketIcon();
                            widgetName = pages.getRechargeHistoryWidget().getWidgetTitle();
                            softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                        } else {
                            ExtentTestManager.getTest().log(Status.FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                        }
                    }
                } catch (NoSuchElementException e) {
                    ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                    e.printStackTrace();
                    softAssert.fail("Issue Creation Failed");
                }
            }
            if (tagIssue.isEmpty()) {
                ExtentTestManager.getTest().log(Status.PASS, "All issue tagged to widget correctly configured and display on UI.");
            } else {
                for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                    ExtentTestManager.getTest().log(Status.FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                    softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
            softAssert.fail("Interaction tab does not open correctly");
        }
        pages.getWidgetInteraction().closeInteractionTab();
        softAssert.assertAll();
    }

    @Test(priority = 4, description = "Validating Usage History widget tagged issue", dataProviderClass = DataProviders.class)
    public void usageWidgetTagIssue() {
        selUtils.addTestcaseDescription("Validating Usage History widget tagged issue", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("usageHistory"));
        softAssert.assertTrue(pages.getUsageHistoryWidget().isUsageHistoryWidgetIsVisible(), "Usage History Widget is not visible ");
        String widgetName = pages.getUsageHistoryWidget().getWidgetTitle();
        pages.getUsageHistoryWidget().clickTicketIcon();
        softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
        try {
            if (pages.getWidgetInteraction().checkNoInteractionTag()) {
                ExtentTestManager.getTest().log(Status.INFO, "No Interaction tagged with widget");
            } else {
                List<String> visibleIssueList = pages.getWidgetInteraction().getListOfIssue();
                try {
                    for (String s : visibleIssueList) {
                        if (tagIssue.containsKey(s)) {
                            ExtentTestManager.getTest().log(Status.PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                            tagIssue.remove(s);
                            pages.getWidgetInteraction().clickIssueLabel(s);
                            pages.getWidgetInteraction().writeComment("Comment added using test automation");
                            pages.getWidgetInteraction().clickSubmitBtn();
                            pages.getWidgetInteraction().interactionTabClosed();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded(), "Customer Interaction Page does not load properly");
                            pages.getCustomerProfilePage().goToViewHistory();
                            pages.getViewHistory().clickOnInteractionsTab();
                            String issueCode = pages.getViewHistory().getLastCreatedIssueCode();
                            softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase(), "Issue code not as expected");
                            pages.getViewHistory().openingCustomerInteractionDashboard();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getUsageHistoryWidget().clickTicketIcon();
                            widgetName = pages.getUsageHistoryWidget().getWidgetTitle();
                            softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                        } else {
                            ExtentTestManager.getTest().log(Status.FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                        }
                    }
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    softAssert.fail("Issue Creation Failed");
                    ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                }
            }
            if (tagIssue.isEmpty()) {
                ExtentTestManager.getTest().log(Status.PASS, "All issue tagged to widget correctly configured and display on UI.");
            } else {
                for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                    ExtentTestManager.getTest().log(Status.FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                    softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
            softAssert.fail("Interaction tab does not open correctly");
        }
        pages.getWidgetInteraction().closeInteractionTab();
        softAssert.assertAll();
    }

    @Test(priority = 5, description = "Validating Airtel Money widget tagged issue")
    public void airtelMoneyWidgetTagIssue() {
        selUtils.addTestcaseDescription("Validating Airtel Money widget tagged issue", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("airtelMoney"));
        softAssert.assertTrue(pages.getAmTxnWidgetPage().isAirtelMoneyTransactionWidgetIsVisible(), "Airtel Money Widget is not visible ");
        String widgetName = pages.getAmTxnWidgetPage().getWidgetTitle();
        pages.getAmTxnWidgetPage().clickTicketIcon();
        softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
        try {
            if (pages.getWidgetInteraction().checkNoInteractionTag()) {
                ExtentTestManager.getTest().log(Status.INFO, "No Interaction tagged with widget");
            } else {
                List<String> visibleIssueList = pages.getWidgetInteraction().getListOfIssue();
                try {
                    for (String s : visibleIssueList) {
                        if (tagIssue.containsKey(s)) {
                            ExtentTestManager.getTest().log(Status.PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                            tagIssue.remove(s);
                            pages.getWidgetInteraction().clickIssueLabel(s);
                            pages.getWidgetInteraction().writeComment("Comment added using test automation");
                            pages.getWidgetInteraction().clickSubmitBtn();
                            pages.getWidgetInteraction().interactionTabClosed();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getCustomerProfilePage().goToViewHistory();
                            pages.getViewHistory().clickOnInteractionsTab();
                            String issueCode = pages.getViewHistory().getLastCreatedIssueCode();
                            softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                            pages.getViewHistory().openingCustomerInteractionDashboard();
                            softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                            pages.getAmTxnWidgetPage().clickTicketIcon();
                            widgetName = pages.getAmTxnWidgetPage().getWidgetTitle();
                            softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                        } else {
                            ExtentTestManager.getTest().log(Status.FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                        }
                    }
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    softAssert.fail("Issue Creation Failed");
                    ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                }
            }
            if (tagIssue.isEmpty()) {
                ExtentTestManager.getTest().log(Status.PASS, "All issue tagged to widget correctly configured and display on UI.");
            } else {
                for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                    ExtentTestManager.getTest().log(Status.FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                    softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
            softAssert.fail("Interaction tab does not open correctly");
        }
        pages.getWidgetInteraction().closeInteractionTab();
        softAssert.assertAll();
    }

    @Test(priority = 6, description = "Validating DA Detail widget tagged issue")
    public void daDetailsTest() {
        selUtils.addTestcaseDescription("Validating DA Detail widget tagged issue", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("daDetails"));
        try {
            softAssert.assertTrue(pages.getCurrentBalanceWidgetPage().isCurrentBalanceWidgetMenuVisible(), "Current Balance Widget MENU is not visible ");
            pages.getCurrentBalanceWidgetPage().openingDADetails();
            softAssert.assertTrue(pages.getDaDetailsPage().isDAWidgetIsVisible());
            String widgetName = pages.getDaDetailsPage().getWidgetTitle();
            pages.getDaDetailsPage().clickTicketIcon();
//            softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
            try {
                if (pages.getWidgetInteraction().checkNoInteractionTag()) {
                    ExtentTestManager.getTest().log(Status.INFO, "No Interaction tagged with widget");
                } else {
                    List<String> visibleIssueList = pages.getWidgetInteraction().getListOfIssue();
                    try {
                        for (String s : visibleIssueList) {
                            if (tagIssue.containsKey(s)) {
                                ExtentTestManager.getTest().log(Status.PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                                tagIssue.remove(s);
                                pages.getWidgetInteraction().clickIssueLabel(s);
                                pages.getWidgetInteraction().writeComment("Comment added using test automation");
                                pages.getWidgetInteraction().clickSubmitBtn();
                                pages.getWidgetInteraction().interactionTabClosed();
                                softAssert.assertTrue(pages.getCustomerProfilePage().isCustomerProfilePageLoaded());
                                pages.getCustomerProfilePage().goToViewHistory();
                                pages.getViewHistory().clickOnInteractionsTab();
                                String issueCode = pages.getViewHistory().getLastCreatedIssueCode();
                                softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                                pages.getCustomerProfilePage().clickOnDADetailsTab();
                                softAssert.assertTrue(pages.getDaDetailsPage().isDAWidgetIsVisible());
                                pages.getDaDetailsPage().clickTicketIcon();
                                widgetName = pages.getDaDetailsPage().getWidgetTitle();
                                softAssert.assertTrue(pages.getWidgetInteraction().getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                            } else {
                                ExtentTestManager.getTest().log(Status.FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                                softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        softAssert.fail("Issue Creation Failed");
                        ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                    }
                }
                if (tagIssue.isEmpty()) {
                    ExtentTestManager.getTest().log(Status.PASS, "All issue tagged to widget correctly configured and display on UI.");
                } else {
                    for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                        ExtentTestManager.getTest().log(Status.FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                        softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ExtentTestManager.getTest().log(Status.INFO, e.fillInStackTrace());
                softAssert.fail("Interaction tab does not open correctly");
            } finally {
                pages.getWidgetInteraction().closeInteractionTab();
            }
            pages.getDaDetailsPage().openingCustomerInteractionDashboard();

        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status.FAIL, e.fillInStackTrace());
            softAssert.fail("DA details does not display correctly");
        }
        softAssert.assertAll();
    }

    /*@Test(priority = 6, description = "Validating SMS History widget tagged issue")
    public void smsHistoryWidgetTaggedIssueTest() {
        selUtils.addTestcaseDescription("Validating SMS History widget tagged issue", "description");
        UsageHistoryWidgetPOM usageHistory = new UsageHistoryWidgetPOM(driver);
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("smsHistory"));
        try {
            softAssert.assertTrue(usageHistory.isUsageHistoryWidgetMenuVisible(), "Usage History's MENU is not visible ");
            MoreUsageHistoryPOM moreUsageHistory = usageHistory.openingMoreDetails();
            String widgetName = moreUsageHistory.getSMSWidgetTitle();
            WidgetInteractionPOM tagIssueTab = moreUsageHistory.clickSMSTicketIcon();
            softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
            try {
                if (tagIssueTab.checkNoInteractionTag()) {
                    ExtentTestManager.getTest().log(Status
                   .WARNING, "No Interaction tagged with widget");
                } else {
                    List<String> visibleIssueList = tagIssueTab.getListOfIssue();
                    try {
                        for (String s : visibleIssueList) {
                            if (tagIssue.containsKey(s)) {
                                ExtentTestManager.getTest().log(Status
                               .PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                                tagIssue.remove(s);
                                tagIssueTab.clickIssueLabel(s);
                                tagIssueTab.writeComment("Comment added using test automation");
                                customerInteractionPagePOM customerInteractionPage = tagIssueTab.clickSubmitBtn();
                                tagIssueTab.interactionTabClosed();
                                softAssert.assertTrue(customerInteractionPage.isPageLoaded());
                                viewHistoryPOM viewHistory = customerInteractionPage.clickOnViewHistory();
                                viewHistory.clickOnInteractionsTab();
                                String issueCode = viewHistory.getLastCreatedIssueCode();
                                softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                                moreUsageHistory = customerInteractionPage.clickOnUsageHistoryTab();
                                softAssert.assertTrue(moreUsageHistory.isSMSDatePickerVisible());
                                moreUsageHistory.clickSMSTicketIcon();
                                widgetName = moreUsageHistory.getSMSWidgetTitle();
                                softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                            } else {
                                ExtentTestManager.getTest().log(Status
                               .FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                                softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        softAssert.fail("Issue Creation Failed");
                        ExtentTestManager.getTest().log(Status
                       .INFO, e.fillInStackTrace());
                    }
                }
                if (tagIssue.isEmpty()) {
                    ExtentTestManager.getTest().log(Status
                   .PASS, "All issue tagged to widget correctly configured and display on UI.");
                } else {
                    for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                        ExtentTestManager.getTest().log(Status
                       .FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                        softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ExtentTestManager.getTest().log(Status
               .INFO, e.fillInStackTrace());
                softAssert.fail("Interaction tab does not open correctly");
            } finally {
                tagIssueTab.closeInteractionTab();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status
           .FAIL, e.fillInStackTrace());
            softAssert.fail("SMS Widget does not display correctly");
        }
        softAssert.assertAll();
    }

    @Test(priority = 7, description = "Validating Data History widget tagged issue")
    public void dataHistoryWidgetTaggedIssueTest() {
        selUtils.addTestcaseDescription("Validating Data History widget tagged issue", "description");
        UsageHistoryWidgetPOM usageHistory = new UsageHistoryWidgetPOM(driver);
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("dataHistory"));
        try {
            MoreUsageHistoryPOM moreUsageHistory = new MoreUsageHistoryPOM(driver);
            String widgetName = moreUsageHistory.getDataWidgetTitle();
            WidgetInteractionPOM tagIssueTab = moreUsageHistory.clickDataTicketIcon();
            softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
            try {
                if (tagIssueTab.checkNoInteractionTag()) {
                    ExtentTestManager.getTest().log(Status
                   .WARNING, "No Interaction tagged with widget");
                } else {
                    List<String> visibleIssueList = tagIssueTab.getListOfIssue();
                    try {
                        for (String s : visibleIssueList) {
                            if (tagIssue.containsKey(s)) {
                                ExtentTestManager.getTest().log(Status
                               .PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                                tagIssue.remove(s);
                                tagIssueTab.clickIssueLabel(s);
                                tagIssueTab.writeComment("Comment added using test automation");
                                customerInteractionPagePOM customerInteractionPage = tagIssueTab.clickSubmitBtn();
                                tagIssueTab.interactionTabClosed();
                                softAssert.assertTrue(customerInteractionPage.isPageLoaded());
                                viewHistoryPOM viewHistory = customerInteractionPage.clickOnViewHistory();
                                viewHistory.clickOnInteractionsTab();
                                String issueCode = viewHistory.getLastCreatedIssueCode();
                                softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                                moreUsageHistory = customerInteractionPage.clickOnUsageHistoryTab();
                                softAssert.assertTrue(moreUsageHistory.isDataDatePickerVisible());
                                moreUsageHistory.clickDataTicketIcon();
                                widgetName = moreUsageHistory.getDataWidgetTitle();
                                softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                            } else {
                                ExtentTestManager.getTest().log(Status
                               .FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                                softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            }
                        }
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        softAssert.fail("Issue Creation Failed");
                        ExtentTestManager.getTest().log(Status
                       .INFO, e.fillInStackTrace());
                    }
                }
                if (tagIssue.isEmpty()) {
                    ExtentTestManager.getTest().log(Status
                   .PASS, "All issue tagged to widget correctly configured and display on UI.");
                } else {
                    for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                        ExtentTestManager.getTest().log(Status
                       .FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                        softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ExtentTestManager.getTest().log(Status
               .INFO, e.fillInStackTrace());
                softAssert.fail("Interaction tab does not open correctly");
            } finally {
                tagIssueTab.closeInteractionTab();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status
           .FAIL, e.fillInStackTrace());
            softAssert.fail("Data Widget does not display correctly");
        }
        softAssert.assertAll();
    }

    @Test(priority = 8, description = "Validating Call History widget tagged issue")
    public void callHistoryWidgetTaggedIssueTest() {
        selUtils.addTestcaseDescription("Validating Call History widget tagged issue", "description");
        UsageHistoryWidgetPOM usageHistory = new UsageHistoryWidgetPOM(driver);
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        Map<String, String> tagIssue = data.getListOfIssue(config.getProperty("callHistory"));
        try {
            MoreUsageHistoryPOM moreUsageHistory = new MoreUsageHistoryPOM(driver);
            String widgetName = moreUsageHistory.getCallWidgetTitle();
            WidgetInteractionPOM tagIssueTab = moreUsageHistory.clickCallTicketIcon();
            softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
            try {
                if (tagIssueTab.checkNoInteractionTag()) {
                    ExtentTestManager.getTest().log(Status
                   .WARNING, "No Interaction tagged with widget");
                } else {
                    List<String> visibleIssueList = tagIssueTab.getListOfIssue();
                    try {
                        for (String s : visibleIssueList) {
                            if (tagIssue.containsKey(s)) {
                                ExtentTestManager.getTest().log(Status
                               .PASS, "Validate " + s + " :Issue tag to widget is display correctly");
                                tagIssue.remove(s);
                                tagIssueTab.clickIssueLabel(s);
                                tagIssueTab.writeComment("Comment added using test automation");
                                customerInteractionPagePOM customerInteractionPage = tagIssueTab.clickSubmitBtn();
                                tagIssueTab.interactionTabClosed();
                                softAssert.assertTrue(customerInteractionPage.isPageLoaded());
                                viewHistoryPOM viewHistory = customerInteractionPage.clickOnViewHistory();
                                viewHistory.clickOnInteractionsTab();
                                String issueCode = viewHistory.getLastCreatedIssueCode();
                                softAssert.assertEquals(issueCode.toLowerCase().trim(), data.getCode(s).trim().toLowerCase());
                                moreUsageHistory = customerInteractionPage.clickOnUsageHistoryTab();
                                softAssert.assertTrue(moreUsageHistory.isCallDatePickerVisible());
                                moreUsageHistory.clickCallTicketIcon();
                                widgetName = moreUsageHistory.getCallWidgetTitle();
                                softAssert.assertTrue(tagIssueTab.getTabTitle().toLowerCase().contains(widgetName), "Interaction tab does not have title displayed");
                            } else {
                                ExtentTestManager.getTest().log(Status
                               .FAIL, s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                                softAssert.fail(s + " :Issue must not tag to widget and display on UI as this is not mention in config sheet.");
                            }
                        }
                    } catch (NoSuchElementException e) {
                        e.printStackTrace();
                        softAssert.fail("Issue Creation Failed");
                        ExtentTestManager.getTest().log(Status
                       .INFO, e.fillInStackTrace());
                    }
                }
                if (tagIssue.isEmpty()) {
                    ExtentTestManager.getTest().log(Status
                   .PASS, "All issue tagged to widget correctly configured and display on UI.");
                } else {
                    for (Map.Entry<String, String> mapElement : tagIssue.entrySet()) {
                        ExtentTestManager.getTest().log(Status
                       .FAIL, mapElement.getKey() + " issue does not display on UI but present in config sheet.");
                        softAssert.fail(mapElement.getKey() + " :Issue does not display on UI but present in config sheet.");
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ExtentTestManager.getTest().log(Status
               .INFO, e.fillInStackTrace());
                softAssert.fail("Interaction tab does not open correctly");
            } finally {
                tagIssueTab.closeInteractionTab();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExtentTestManager.getTest().log(Status
           .FAIL, e.fillInStackTrace());
            softAssert.fail("Call Widget does not display correctly");
        }
        softAssert.assertAll();
    }*/
}
