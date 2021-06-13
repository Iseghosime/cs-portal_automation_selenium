package com.airtel.cs.ui.frontendagent.actiondropdown;

import com.airtel.cs.common.actions.BaseActions;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.driver.Driver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ResetME2UPasswordTest extends Driver {

    private final BaseActions actions = new BaseActions();

    @BeforeMethod
    public void isResetMe2uFeatureEnabled() {
        if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.RESET_ME2U_PASSWORD), "false")) {
            commonLib.skip("Reset Me2u Feature is NOT Enabled for this Opco=" + OPCO);
            throw new SkipException("Reset Me2u Feature is NOT Enabled for this Opco=" + OPCO);
        }
    }

    @BeforeMethod
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, description = "Validate Customer Profile Page")
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            final String customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
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

    @Test(priority = 2, description = "Verify the Reset ME2U Password tab", dependsOnMethods = "openCustomerInteraction")
    public void validateResetME2UPassword() {
        try {
            selUtils.addTestcaseDescription("Open action drop down and click on Reset ME2U Password option,Validate title visible over modal,Close modal by clicking over cancel button", "description");
            pages.getCustomerProfilePage().clickOnAction();
            pages.getCustomerProfilePage().clickResetME2U();
            assertCheck.append(actions.assertEqual_boolean(pages.getCustomerProfilePage().isResetME2UPasswordTitle(), true, "Reset ME2U Password Tab Opened", "Reset ME2U Password Tab Does not open."));
            pages.getCustomerProfilePage().clickCancelBtn();
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException e) {
            commonLib.fail("Exception in Method :- validateResetME2UPassword" + e.fillInStackTrace(), true);
        }
    }
}