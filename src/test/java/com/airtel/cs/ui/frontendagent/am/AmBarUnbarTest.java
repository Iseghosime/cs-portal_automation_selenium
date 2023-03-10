package com.airtel.cs.ui.frontendagent.am;

import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.actiontrail.EventLogsResponse;
import com.airtel.cs.model.cs.response.actiontrail.EventResult;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AmBarUnbarTest extends Driver {
    boolean barIconVisible, unbarIconVisible;
    String customerNumber;

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }


    @Test(priority = 1, groups = {"SanityTest", "RegressionTest"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().clickOnUserName();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(customerNumber);
            pages.getMsisdnSearchPage().clickOnSearch();
            final boolean pageLoaded = pages.getCustomerProfilePage().isCustomerProfilePageLoaded();
            assertCheck.append(actions.assertEqualBoolean(pageLoaded, true, "Customer Profile Page Loaded Successfully", "Customer Profile Page NOT Loaded"));
            if (!pageLoaded) continueExecutionFA = false;
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openCustomerInteraction" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void barUnbarTest() {
        try {
            pages.getDemoGraphicPage().clickAirtelStatusToUnlock();
            assertCheck.append(actions.assertEqualBoolean(pages.getAuthTabPage().isAuthTabLoad(), true, "Authentication tab loaded correctly", "Authentication tab does not load correctly"));
            pages.getDemoGraphicPage().selectPolicyQuestion();
            assertCheck.append(actions.assertEqualBoolean(pages.getAuthTabPage().isAuthBtnEnable(), true, "Authenticate Button enabled after minimum number of question chosen", "Authenticate Button does not enable after choose minimum number of question"));
            pages.getAuthTabPage().clickAuthBtn();
            barIconVisible = pages.getBarUnbar().isBarIconVisible();
            unbarIconVisible = pages.getBarUnbar().isUnBarIconVisible();
            if (barIconVisible)
                assertCheck = pages.getAmBarUnbar().barTest();
            else if (unbarIconVisible)
                assertCheck = pages.getAmBarUnbar().unBarTest();
            pages.getBarUnbar().clickCrossIcon();
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - barUnbarTest" + e.fillInStackTrace(), true);
            pages.getPinReset().clickCloseBtn();
        }
    }

    @Test(priority = 3, groups = {"SanityTest", "RegressionTest"}, dependsOnMethods = {"barUnbarTest"})
    public void checkActionTrail() {
        try {
            selUtils.addTestcaseDescription("Validating entry should be captured in Action Trail after performing bar/unbar action", "description");
            pages.getBarUnbar().goToActionTrail();
            EventLogsResponse eventLogs = api.getEventHistory(customerNumber, "ACTION");
            int statusCode = eventLogs.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "Event Logs API success and status code is :" + statusCode, "Event Logs API got failed and status code is :" + statusCode, false, true));
            EventResult eventResult = eventLogs.getResult().get(0);
            if (statusCode == 200) {
                assertCheck.append(actions.assertEqualStringType(pages.getBarUnbar().getActionType().toLowerCase(), eventResult.getActionType().toLowerCase(), "Action type for UnBar is expected", "Action type for unbar is not as expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getBarUnbar().getReason().toLowerCase(), eventResult.getReason().toLowerCase(), "Reason for bar/unbar is expected", "Reason for bar/unbar is not as expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getBarUnbar().getComment().toLowerCase(), eventResult.getComments().toLowerCase(), "Comment for bar/unbar is expected", "Comment for bar/unbar is not as expected"));
            } else
                commonLib.fail("Not able to fetch action trail as event log API's status code is :", true);
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - checkActionTrail" + e.fillInStackTrace(), true);
        }
    }
}

