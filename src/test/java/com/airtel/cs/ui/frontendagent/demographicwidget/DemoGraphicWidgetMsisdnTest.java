package com.airtel.cs.ui.frontendagent.demographicwidget;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.commonutils.actions.BaseActions;
import com.airtel.cs.commonutils.UtilsMethods;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.response.AMProfilePOJO;
import com.airtel.cs.pojo.response.GsmKycPOJO;
import com.airtel.cs.pojo.response.PlansPOJO;
import com.airtel.cs.pojo.response.ProfilePOJO;
import com.airtel.cs.pojo.response.kycprofile.KYCProfile;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Objects;

@Log4j2
public class DemoGraphicWidgetMsisdnTest extends Driver {

    RequestSource api = new RequestSource();
    private final BaseActions actions = new BaseActions();
    private static String customerNumber = null;

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not",
                    "description");
            customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(customerNumber);
            pages.getMsisdnSearchPage().clickOnSearch();
            final boolean pageLoaded = pages.getCustomerProfilePage().isCustomerProfilePageLoaded();
            assertCheck.append(
                    actions.assertEqualBoolean(pageLoaded, true, "Customer Profile Page Loaded Successfully", "Customer Profile Page NOT Loaded"));
            if (!pageLoaded)
                continueExecutionFA = false;
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openCustomerInteraction" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testPukDetails() {
        try {
            selUtils.addTestcaseDescription(
                    "Verify Auuid shown in middle and at the footer of the demographic widget, Verify PUK is locked or unlocked, If Locked then verify data, else unlock PUK details, Validate PUK1 and PUK2", "description");
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getMiddleAuuidDGW(), loginAUUID, "Auuid is visible at the middle of the Demo Graphic Widget and is correct", "Auuid is NOT visible at the middle of the Demo Graphic Widget"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getFooterAuuidDGW(), loginAUUID, "Auuid is visible at the footer of the Demo Graphic Widget and is correct", "Auuid is NOT visible at the footer of the Demo Graphic Widget"));
            KYCProfile kycProfile = api.kycProfileAPITest(customerNumber);
            final Integer statusCode = kycProfile.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "KYC Profile API Status Code Matched and is :" + statusCode, "KYC Profile API Status Code NOT Matched and is :" + statusCode, false));
            if (pages.getDemoGraphicPage().isPUKInfoLocked()) {
                try {
                    pages.getDemoGraphicPage().clickPUKToUnlock();
                    assertCheck.append(actions
                            .assertEqualBoolean(pages.getAuthTabPage().isAuthTabLoad(), true, "Authentication tab loaded correctly",
                                    "Authentication tab does not load correctly"));
                    pages.getDemoGraphicPage().selectPolicyQuestion();
                    assertCheck.append(actions.assertEqualBoolean(pages.getAuthTabPage().isAuthBtnEnable(), true,
                            "Authenticate Button enabled after minimum number of question chosen",
                            "Authenticate Button does not enable after choose minimum number of question"));
                    pages.getAuthTabPage().clickAuthBtn();
                    assertCheck.append(actions.assertEqualStringType(pages.getAuthTabPage().getWidgetUnlockMessage(), "Unlocking the widget", "Unlock Widget, Successfully", "Unlock Widget, Un-Successful"));
                    assertCheck.append(actions.assertEqualStringType(pages.getAuthTabPage().getToastMessage(), "Customer response saved successfully", "Toast Message Matched Successfully", "Toast Message NOT Matched"));
                } catch (Exception e) {
                    pages.getAuthTabPage().clickCloseBtn();
                }
            }
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getPUK1().trim(), kycProfile.getResult().getPuk().get(0).getValue(),
                    "Customer's PUK1 Number is as Expected", "Customer's PUK1 Number is not as Expected"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getPUK2().trim(), kycProfile.getResult().getPuk().get(1).getValue(),
                    "Customer's PUK2 Number is as Expected", "Customer's PUK2 Number is not as Expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testPukDetails " + e, true);
        }
    }

    @Test(priority = 3, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testAirtelMoneyProfile() {
        try {
            if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.AIRTEL_MONEY_PROFILE), "true")) {
                selUtils.addTestcaseDescription(
                        "Verify Airtel Money Profile is locked or unlocked, if locked then verify data, else unlock Airtel Money Profile", "description");
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getMiddleAuuidAMP(), loginAUUID, "Auuid is visible at the middle of the Airtel Money Profile widget and is correct", "Auuid is NOT visible at the middle of the Airtel Money Profile widget"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getFooterAuuidAMP(), loginAUUID, "Auuid is visible at the footer of the Airtel Money Profile widget and is correct", "Auuid is NOT visible at the footer of the Airtel Money Profile widget"));
                ProfilePOJO profileAPI = api.profileAPITest(customerNumber);
                final int statusCode = profileAPI.getStatusCode();
                assertCheck.append(actions.assertEqualIntType(statusCode, 200, "Profile API Status Code Matched and is :" + statusCode, "Profile API Status Code NOT Matched and is :" + statusCode, false));
                AMProfilePOJO amProfileAPI = api.amServiceProfileAPITest(customerNumber);
                final int amProfileAPIStatusCode = amProfileAPI.getStatusCode();
                assertCheck.append(actions.assertEqualIntType(amProfileAPIStatusCode, 200, "AM Profile API Status Code Matched and is :" + amProfileAPIStatusCode, "AM Profile API Status Code NOT Matched and is :" + amProfileAPIStatusCode, false));
                if (pages.getDemoGraphicPage().isAirtelMoneyProfileLocked()) {
                    try {
                        pages.getDemoGraphicPage().clickAirtelStatusToUnlock();
                        assertCheck.append(actions
                                .assertEqualBoolean(pages.getAuthTabPage().isAuthTabLoad(), true, "Authentication tab loaded correctly",
                                        "Authentication tab does not load correctly"));
                        pages.getDemoGraphicPage().selectPolicyQuestion();
                        assertCheck.append(actions.assertEqualBoolean(pages.getAuthTabPage().isAuthBtnEnable(), true,
                                "Authenticate Button enabled after minimum number of question chosen",
                                "Authenticate Button does not enable after choose minimum number of question"));
                        pages.getAuthTabPage().clickAuthBtn();
                        assertCheck.append(actions.assertEqualStringType(pages.getAuthTabPage().getWidgetUnlockMessage(), "Unlocking the widget", "Unlock Widget, Successfully", "Unlock Widget, Un-Successful"));
                        assertCheck.append(actions.assertEqualStringType(pages.getAuthTabPage().getToastMessage(), "Customer response saved successfully", "Toast Message Shown Successfully", "Toast Message NOT Successful"));
                    } catch (Exception e) {
                        pages.getAuthTabPage().clickCloseBtn();
                        commonLib.fail("Not able to unlock Airtel Money Profile", true);
                    }
                }
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getAccountStatus().toLowerCase().trim(),
                        pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getAirtelMoneyStatus()), "Customer's Airtel Money Status is as Expected",
                        "Customer's Airtel Money Status is not as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getServiceStatus().toLowerCase().trim(),
                        pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getServiceStatus()), "Customer's Airtel Money Service Status is as Expected",
                        "Customer's Airtel Money Service Status is not as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getWalletBalance().toUpperCase().trim(),
                        amProfileAPI.getResult().getWallet().get(0).getCurrency().toUpperCase() + " " + amProfileAPI.getResult().getWallet().get(0)
                                .getBalance(), "Customer's Airtel Wallet Balance & Currency code as Expected",
                        "Customer's Airtel Wallet Balance & Currency code not same not as Expected"));
                if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.MULTI_WALLET_BALANCE), "true")) {
                    assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getWalletBalance2().toUpperCase().trim(),
                            pages.getDemoGraphicPage().getKeyValueAPI(amProfileAPI.getResult().getWallet().get(1).getCurrency().toUpperCase()) + " " + pages.getDemoGraphicPage().getKeyValueAPI(amProfileAPI.getResult().getWallet()
                                    .get(1).getBalance()), "Customer's Airtel Wallet Balance & Currency code as Expected",
                            "Customer's Airtel Wallet Balance & Currency code not same not as Expected"));
                }
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getRegistrationStatus().toLowerCase().trim(),
                        pages.getDemoGraphicPage().getKeyValueAPI(amProfileAPI.getResult().getRegStatus()), "Customer's Airtel Money Registration Status as Expected",
                        "Customer's Airtel Money Registration Status not same not as Expected"));
                actions.assertAllFoundFailedAssert(assertCheck);
            } else {
                commonLib.skip("Airtel Money Profile is Not configured for Opco=" + OPCO);
            }
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testAirtelMoneyProfile " + e, true);
        }

    }

    @Test(priority = 4, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testCustomerInfoAndGSMKYCStatus() {
        try {
            selUtils.addTestcaseDescription(
                    "Validate Customer Name,Validate Customer DOB,Validate if Customer has Birthday or Anniversary with Airtel, Validate GSM KYC Status", "description");
            GsmKycPOJO gsmKycAPI = api.gsmKYCAPITest(customerNumber);
            final int statusCode = gsmKycAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "GSM KYC API Status Code Matched and is :" + statusCode, "GSM KYC API Status Code NOT Matched and is :" + statusCode, false));
            final String customerName = pages.getDemoGraphicPage().getCustomerName();
            assertCheck.append(actions.matchUiAndAPIResponse(customerName, gsmKycAPI.getResult().getName(),
                    "Customer Name is as Expected", "Customer Name is not as Expected"));
            if (!customerName.contains("Unable to fetch data")) {
                pages.getDemoGraphicPage().hoverOnCustomerInfoIcon();
                final String customerDOB = pages.getDemoGraphicPage().getCustomerDOB();
                assertCheck.append(actions
                        .assertEqualStringType(customerDOB.trim(), pages.getDemoGraphicPage().getKeyValueAPI(UtilsMethods.getDateFromEpoch(gsmKycAPI.getResult().getDob(), "dd-MMM-yyyy")),
                                "Customer DOB is as Expected", "Customer DOB is not as Expected"));
                if (!customerDOB.equals("-"))
                    if (UtilsMethods.isCustomerBirthday(customerDOB.trim(), "dd-MMM-yyyy")) {
                        commonLib.pass("Today is Customer Birthday");
                        final boolean birthday = pages.getDemoGraphicPage().isBirthday();
                        assertCheck.append(actions.assertEqualBoolean(birthday, true, "Today is customer birthday and birthday icon displayed",
                                "Today is customer birthday but birthday Icon NOT displayed"));
                    }
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getIdType().toLowerCase().trim(),
                        pages.getDemoGraphicPage().getKeyValueAPI(gsmKycAPI.getResult().getIdentificationType()), "Customer's ID Type is as Expected",
                        "Customer's ID Type is not as Expected"));
                final String customerIdNumber = pages.getDemoGraphicPage().getIdNumber().replace("*", "");
                assertCheck.append(actions.assertEqualBoolean(StringUtils.contains(gsmKycAPI.getResult().getIdentificationNo(), customerIdNumber), true,
                        "Customer's ID Number is as Expected", "Customer's ID Number is not as Expected and Expected was :" + customerIdNumber));
            }
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getGsmKycStatus(), pages.getDemoGraphicPage().getKeyValueAPI(gsmKycAPI.getResult().getGsm()),
                    "Customer's GSM KYC Status is as Expected", "Customer's GSM KYC Status is not as Expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testCustomerInfo " + e, true);
        }
    }

    @Test(priority = 5, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testGSMStatusAndSIMNumber() {
        try {
            selUtils.addTestcaseDescription(
                    "Validate SIM Number,Validate Activation date after hovering over SIM Number,Validate GSM Status, Validate data after hovering the GSM status",
                    "description");
            KYCProfile kycProfile = api.kycProfileAPITest(customerNumber);
            final Integer statusCode = kycProfile.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "KYC Profile API Status Code Matched and is :" + statusCode, "KYC Profile API Status Code NOT Matched and is :" + statusCode, false));
            final String gsmStatus = pages.getDemoGraphicPage().getGSMStatus();
            assertCheck.append(actions.assertEqualStringType(gsmStatus.toLowerCase().trim(),
                    kycProfile.getResult().getStatus().toLowerCase().trim(), "Customer's SIM Status is as Expected",
                    "Customer's SIM Status is not as Expected"));
            if (!gsmStatus.contains("Unable to fetch data")) {
                pages.getDemoGraphicPage().hoverOnSIMStatusInfoIcon();
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getActivationDate().trim(),
                        UtilsMethods.getDateFromEpoch(Long.parseLong(kycProfile.getResult().getActivationDate()), "dd-MMM-yyy"),
                        "Customer's Activation Date is as Expected", "Customer's Activation Date is not as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getGSMStatusReasonCode().trim().toLowerCase(),
                        pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getReason()), "Customer SIM Status Reason is as Expected",
                        "Customer SIM Status Reason is not as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getGSMStatusModifiedBy().trim().toLowerCase(),
                        pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getModifiedBy().trim().toLowerCase()), "Customer SIM Status Modified By is as Expected",
                        "Customer SIM Status Modified By is not as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getGSMStatusModifiedDate().trim(),
                        UtilsMethods.getDateFromString(kycProfile.getResult().getModifiedDate(), "dd-MMM-yyy HH:mm aa", "dd-MMM-yyyy hh:mm aa").replace("am", "AM").replace("pm", "PM"),
                        "Customer SIM Status, Modified Date is as Expected", "Customer SIM Status, Modified Date is not as Expected"));
            }
            final String simNumber = pages.getDemoGraphicPage().getSimNumber();
            assertCheck.append(actions.assertEqualStringType(simNumber.trim(), pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getSim()),
                    "Customer's SIM Number is as Expected", "Customer's SIM Number is not as Expected"));
            if (!simNumber.equalsIgnoreCase("Unable to Fetch Data")) {
                pages.getDemoGraphicPage().hoverOnSIMNumberIcon();
                assertCheck.append(actions
                        .assertEqualStringType(pages.getDemoGraphicPage().getPIN1(), pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getPin1()), "PIN1 Matched Successfully",
                                "PIN1 NOT Matched"));
                assertCheck.append(actions
                        .assertEqualStringType(pages.getDemoGraphicPage().getPIN2(), pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getPin2()), "PIN2 Matched Successfully",
                                "PIN2 NOT Matched"));
            }

            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testSIMNumberAndGSMStatus " + e, true);
        }
    }

    @Test(priority = 6, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testDeviceCompatible() {
        try {
            selUtils.addTestcaseDescription("Validate Device Compatible", "description");
            KYCProfile kycProfile = api.kycProfileAPITest(customerNumber);
            ProfilePOJO profileAPI = api.profileAPITest(customerNumber);
            final Integer statusCode = kycProfile.getStatusCode();
            final int profileAPIStatusCode = profileAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(profileAPIStatusCode, 200, "Profile API Status Code Matched and is :" + profileAPIStatusCode, "Profile API Status Code NOT Matched and is :" + profileAPIStatusCode, false));
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "KYC Profile API Status Code Matched and is :" + statusCode, "KYC Profile API Status Code NOT Matched and is :" + statusCode, false));
            final String simType = pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getSimType());
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getSimType().trim(), simType.toUpperCase(),
                    "Customer's SIM Type is as Expected and is -" + simType, "Customer's SIM Type is not as Expected"));
            final String deviceType = pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getDeviceType());
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getDeviceCompatible().toLowerCase(), deviceType,
                    "Customer's Device Type is as Expected and is - " + deviceType, "Customer's Device Type is not as Expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testDeviceCompatible " + e, true);
        }
    }

    @Test(priority = 7, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testDeviceInfo() {
        try {
            selUtils.addTestcaseDescription(
                    "Validate Device Info like IMEI Number,Validate data on hover like Device Type Brand Device Model and Device OS", "description");
            ProfilePOJO profileAPI = api.profileAPITest(customerNumber);
            final int profileAPIStatusCode = profileAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(profileAPIStatusCode, 200, "Profile API Status Code Matched and is :" + profileAPIStatusCode, "Profile API Status Code NOT Matched and is :" + profileAPIStatusCode, false));
            pages.getDemoGraphicPage().hoverOnDeviceInfoIcon();
            assertCheck.append(actions
                    .assertEqualStringType(pages.getDemoGraphicPage().getIMEINumber().trim(), pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getImeiNumber()),
                            "Customer device IMEI number as expected", "Customer device IMEI number as not expected"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getDeviceType().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getDeviceType().toLowerCase().trim()), "Customer device type as expected",
                    "Customer device type as not expected"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getBrand().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getBrand().toLowerCase().trim()), "Customer device Brand as expected",
                    "Customer device Brand as not expected"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getDeviceModel().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getModel().toLowerCase().trim()), "Customer device model as expected",
                    "Customer device model as not expected"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getDemoGraphicPage().getDeviceOS(), pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getOs()),
                    "Customer device OS as expected", "Customer device OS as not expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testDeviceInfo " + e, true);
        }
    }

    @Test(priority = 8, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testConnectionTypeServiceCategorySegment() {
        try {
            selUtils.addTestcaseDescription("Validate Connection Type, Validate Service Category,Validate Segment, Validate Service Class",
                    "description");
            KYCProfile kycProfile = api.kycProfileAPITest(customerNumber);
            final Integer statusCode = kycProfile.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "KYC Profile API Status Code Matched and is :" + statusCode, "KYC Profile API Status Code NOT Matched and is :" + statusCode, false));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getConnectionType().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getLineType().toLowerCase().trim()), "Customer Connection Type is as expected",
                    "Customer Connection Type as not expected"));
            final String segment = pages.getDemoGraphicPage().getSegment();
            assertCheck.append(actions.assertEqualStringType(segment.toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getSegment().toLowerCase().trim()), "Customer Segment as expected", "Customer Segment as not expected"));
            if (!segment.contains("Unable to fetch data")) {
                pages.getDemoGraphicPage().hoverOnSegmentInfoIcon();
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getSubSegment(), pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getSubSegment()), "Customer Sub Segment as expected",
                        "Customer Sub Segment as not expected"));
                final String serviceCategory = pages.getDemoGraphicPage().getServiceCategory();
                assertCheck.append(actions.assertEqualStringType(serviceCategory.toLowerCase().trim(),
                        pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getServiceCategory()), "Customer Service Category as expected",
                        "Customer Service Category as not expected and is: " + serviceCategory));
            }
            if (kycProfile.getResult().getVip()) {
                assertCheck.append(actions
                        .assertEqualBoolean(pages.getDemoGraphicPage().isVIP(), true, "Customer is VIP but Icon displayed as expected",
                                "Customer is VIP but Icon does not display as expected"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testConnectionTypeServiceCategorySegment " + e, true);
        }

    }

    @Test(priority = 9, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testServiceClassAndAppStatus() {
        try {
            selUtils.addTestcaseDescription("Validate Service Class, Validate App Status", "description");
            KYCProfile kycProfile = api.kycProfileAPITest(customerNumber);
            ProfilePOJO profileAPI = api.profileAPITest(customerNumber);
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getServiceClass().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(kycProfile.getResult().getServiceClass()), "Customer Service Class as expected",
                    "Customer Service Class as not expected"));
            assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getAppStatus().toLowerCase().trim(),
                    pages.getDemoGraphicPage().getKeyValueAPI(profileAPI.getResult().getAppStatus()), "App Status as expected",
                    "App Status as not expected"));

        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testServiceClassAndAppStatus " + e, true);
        }
    }


    @Test(priority = 10, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testDataManager() {
        try {
            selUtils.addTestcaseDescription("Validate Data Manager,Validate Data Manager Status", "description");
            PlansPOJO plansAPI = api.accountPlansTest(customerNumber);
            final int statusCode = plansAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "Plan API Status Code Matched and is :" + statusCode, "Plan API Status Code NOT Matched and is :" + statusCode, false));
            final String dataManager = pages.getDemoGraphicPage().getKeyValueAPI(plansAPI.getResult().getDataManager());
            if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.DATA_MANAGER_TOGGLEABLE), "True"))
                if (pages.getDemoGraphicPage().getDataManagerStatus().equalsIgnoreCase("true"))
                    assertCheck.append(actions.assertEqualStringType("on", dataManager.toLowerCase().trim(),
                            "Customer's Data Manager Status is as Expected", "Customer's Data Manager Status is not as Expected"));
                else
                    assertCheck.append(actions.assertEqualStringType("off", dataManager.toLowerCase().trim(),
                            "Customer's Data Manager Status is as Expected", "Customer's Data Manager Status is not as Expected"));
            else
                assertCheck.append(actions.assertEqualStringType(pages.getDemoGraphicPage().getDataManagerValue(),
                        dataManager == null || dataManager.equals("") ?
                                "-" :
                                dataManager.toLowerCase().trim(), "Data Manager Value is as Expected",
                        "Data Manager Value is NOT as Expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testDataManager " + e, true);
        }

    }

    @Test(priority = 11, groups = {"RegressionTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void invalidMSISDNTest() {
        try {
            selUtils.addTestcaseDescription("Validating the Demographic Information of User with invalid MSISDN : 123456789", "description");
            pages.getDemoGraphicPage().enterMSISDN(constants.getValue("cs.invalid.msisdn"));
            pages.getDemoGraphicPage().waitTillLoaderGetsRemoved();
            assertCheck.append(actions
                    .assertEqualStringType(pages.getDemoGraphicPage().invalidMSISDNError(), "Entered customer number is Invalid",
                            "Error Message Correctly Displayed", "Error Message NOT Displayed Correctly"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - invalidMSISDNTest" + e.fillInStackTrace(), true);
        }
    }

}
