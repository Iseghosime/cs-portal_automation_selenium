package com.airtel.cs.ui.ngpsb.demographic;

import com.airtel.cs.api.PsbRequestSource;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.PermissionConstants;
import com.airtel.cs.commonutils.utils.UtilsMethods;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.psb.cs.clmdetails.CLMDetailsResponse;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;


@Log4j2
public class PsbDemoGraphicWidgetTest extends Driver {
    private static String customerNumber, invalidCustomerNumber = null;
    PsbRequestSource api = new PsbRequestSource();
    CLMDetailsResponse clmDetails;
    String space = " ";
    String className = this.getClass().getName();
    boolean isPermissionEnable = false;


    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void isUserHasPermission() {
        try {
            selUtils.addTestcaseDescription("Validate whether user has AM Customer Details Permission", "description");
            String permission = constants.getValue(PermissionConstants.AM_CUSTOMER_DETAILS_PERMISSION);
            isPermissionEnable = UtilsMethods.isUserHasPermission(permission);
            assertCheck.append(actions.assertEqualBoolean(isPermissionEnable, true, "Logged in user has Am Customer details permission to view the customer details", "Logged in user doesn't has Am Customer details permission to view the customer details"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - isUserHasPermission " + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"isUserHasPermission"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_TIER3_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(customerNumber);
            pages.getMsisdnSearchPage().clickOnSearch();
            clmDetails = api.getCLMDetails(customerNumber);
            assertCheck.append(actions.assertEqualIntType(clmDetails.getStatusCode(), 200, "CLM Details API Status Code Matched and is :" + clmDetails.getStatusCode(), "CLM Details API Status Code NOT Matched and is :" + clmDetails.getStatusCode(), false));
            if (clmDetails.getStatusCode() == 200) {
                if (isPermissionEnable) {
                    boolean pageLoaded = pages.getPsbDemographicWidget().isPageLoaded(clmDetails, className);
                    if (!pageLoaded) continueExecutionFA = false;
                } else commonLib.warning("Logged in user doesn't has permission to view customer details");

            } else commonLib.warning("Clm Details API is not working");
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openCustomerInteraction" + e.fillInStackTrace(), true);
        }
    }


    @Test(priority = 3, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testCustomerDetails() {
        try {
            selUtils.addTestcaseDescription("Validate Customer Name, Validate ", "description");
            assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getMiddleAuuid(), loginAUUID, "Auuid is visible at the middle of the Demo Graphic Widget and is correct", "Auuid is NOT visible at the middle of the Demo Graphic Widget"));
            assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getFooterAuuid(), loginAUUID, "Auuid is visible at the footer of the Demo Graphic Widget and is correct", "Auuid is NOT visible at the footer of the Demo Graphic Widget"));
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isSmartCashLogoVisible(), true, "Smart Cash Logo is visible", "Smart Cash Logo is NOT visible"));
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isCustomerNameHeaderVisible(), true, "Customer Name header is visible", "Customer Name header is NOT visible"));
            final String customerName = pages.getPsbDemographicWidget().getCustomerName();
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getCustomerName(), clmDetails.getResult().getFirstName() + space + clmDetails.getResult().getLastName(), "Customer Name is as Expected", "Customer Name is not as Expected"));
            if (customerName.equalsIgnoreCase("-")) {
                commonLib.warning("Customer Name is not available so unable to display customer details");
            } else {
                pages.getPsbDemographicWidget().hoverOnCustomerInfoIcon();
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isGenderHeaderVisible(), true, "Gender header is visible", "Customer Name header is NOT visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isDobHeaderVisible(), true, "DOB header is visible", "DOB header is NOT visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isPobHeaderVisible(), true, "Place of Birth header is visible", "Place of Birth header is NOT visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isNationalityHeaderVisible(), true, "Nationality header is visible", "Nationality header is NOT visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isMaidenNameHeaderVisible(), true, "Mothers Maiden Name header is visible", "Mothers Maiden Name is NOT visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isCustomerTypeHeaderVisible(), true, "Customer Type header is visible", "Customer Type header is NOT visible"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getGender(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getGender()), "Gender is as Expected", "Gender is not as Expected"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getDob(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getDob()), "DOB is as Expected", "DOB is not as Expected"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getPob(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getPlaceOfBirth()), "Place of birth is as Expected", "Place of birth is not as Expected"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getNationality(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getNationality()), "Nationality is as Expected", "Nationality is not as Expected"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getMothersMaidenName(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getMothersMaidenName()), "Mothers maiden name is as Expected", "Mothers maiden name is not as Expected"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getCustomerType(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getCustomerType()), "Customer Type is as Expected", "Customer Type is not as Expected"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testCustomerDetails " + e, true);
        }
    }

    @Test(priority = 4, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testPrimaryAndSecondaryIdType() {
        try {
            selUtils.addTestcaseDescription("Validate Primary Id Type & Number, Validate Secondary Id Type & Number", "description");
           /*
           Validating Primary Id Type and Number
            */
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isPrimaryIdTypeHeaderVisible(), true, "Primary Id Type header is visible", "Primary Id Type header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getPrimaryIdType(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getPrimaryIdType()), "Primary Id Type is same as Expected", "Primary Id Type is not same as Expected"));
            if (pages.getPsbDemographicWidget().getPrimaryIdType().equalsIgnoreCase("-")) {
                commonLib.warning("Primary Id Type is not available so unable to display Primary Id Number");
            } else {
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isPrimaryIdNumberHeaderVisible(), true, "Primary Id Number header is visible", "Primary Id Number header is NOT visible"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getPrimaryIdNumber(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getPrimaryIdNumber()), "Primary Id Number is same as Expected", "Primary Id Number is not same as Expected"));
            }
            /*
           Validating Secondary Id Type and Number
            */
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isSecondaryIdTypeHeaderVisible(), true, "Secondary Id Type header is visible", "Primary Id Type header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getSecondaryIdType(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getSecondaryIdType()), "Secondary Id Type is same as Expected", "Secondary Id Type is not same as Expected"));
            if (pages.getPsbDemographicWidget().getPrimaryIdType().equalsIgnoreCase("-")) {
                commonLib.warning("Secondary Id Type is not available so unable to display Secondary Id Number is");
            } else {
                assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isSecondaryIdNumberHeaderVisible(), true, "Secondary Id Number header is visible", "Secondary Id Number header is NOT visible"));
                assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getSecondaryIdNumber(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getSecondaryIdNumber()), "Secondary Id Number is same as Expected", "Secondary Id Number is not same as Expected"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in method - testEmailIdAndPrimarySecondaryIdType" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 5, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testEmailIdAndAgent() {
        try {
            selUtils.addTestcaseDescription("Validate Email Id , Validate is User an Agent", "description");
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isEmailIdHeaderVisible(), true, "Email Id header is visible", "Email Id header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getEmailId(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getEmailId()), "Email Id is same as Expected", "Email id is not same as Expected"));
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isUserAgentHeaderVisible(), true, "Is user An Agent header is visible", "Is user An Agent  header is NOT visible"));
            String isUserAnAgent = pages.getPsbDemographicWidget().getIsUserAgent();
            assertCheck.append(actions.matchUiAndAPIResponse(isUserAnAgent, clmDetails.getResult().getDetails().get(0).getIsUser(), "Is user An Agent  is same as Expected", "Is user An Agent is not same as Expected"));
            if (isUserAnAgent.equalsIgnoreCase("YES"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getIsUserAgentColour(), "#33a833", "Colour of Is User An Agent is same as expected", "Colour of Is User An Agent is NOT same as expected"));
            else if (isUserAnAgent.toLowerCase().equalsIgnoreCase("NO"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getIsUserAgentColour(), "#e4000e", "Colour of Is User An Agent is same as expected", "Colour of Is User An Agent is NOT same as expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testEmailIdAndAgent " + e, true);
        }
    }

    @Test(priority = 6, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testAddress() {
        try {
            selUtils.addTestcaseDescription("Validate Address", "description");
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isAddressHeaderVisible(), true, "Address header is visible", "Address header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getAddress(), clmDetails.getResult().getAddressLine1(), "Address is same as Expected", "Address is not same as Expected"));
            if (pages.getPsbDemographicWidget().getAddress().equalsIgnoreCase("-")) {
                commonLib.warning("Address is not available so unable to display address line 2 ,3 and zone");
            } else {
                pages.getPsbDemographicWidget().hoverOnAddressInfoIcon();
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getAddressLine2().toLowerCase(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getAddressLine2()), "Address Line 2 is same as Expected", "Address Line 2 is not same as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getAddressLine3().toLowerCase(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getAddressLine3()), "Address Line 3 is same as Expected", "Address Line 3 is not same as Expected"));
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getAddressZone().toLowerCase(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getZone()), "Address's Zone is same as Expected", "Address's Zone is not same as Expected"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testAddress " + e, true);
        }
    }

    @Test(priority = 7, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testCustomerIdAndCategory() {
        try {
            selUtils.addTestcaseDescription("Validate Customer Id, Validate Customer Category", "description");
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isCustomerIdHeaderVisible(), true, "Customer Id header is visible", "Customer Id header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getCustomerId(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getCustomerId()), "Customer Id is same as Expected", "Customer Id is not same as Expected"));

            if (pages.getPsbDemographicWidget().getCustomerId().equalsIgnoreCase("-")) {
                commonLib.warning("Customer Id is not visible, unable to display KIN details");
            } else {
                final boolean customerIdInfoIconVisible = pages.getPsbDemographicWidget().isCustomerIdInfoIconVisible();
                assertCheck.append(actions.assertEqualBoolean(customerIdInfoIconVisible, true, "Customer Id Icon is visible", "Customer Id Icon is NOT visible"));
                if (customerIdInfoIconVisible) {
                    pages.getPsbDemographicWidget().hoverOnCustomerInfoIdIcon();
                    assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getFirstName().toLowerCase(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getKinDetails().getFirstName()), "Customer FirstName is as Expected", "Customer FirstName is not same as Expected"));
                    assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getLastName().toLowerCase(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getKinDetails().getLastName()), "Customer LastName is as Expected", "Customer LastName is not same as Expected"));
                    assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getMSISDN().toLowerCase(Locale.ROOT), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getKinDetails().getMsisdn()), "Customer MSISDN is as Expected", "Customer MSISDN is not same as Expected"));
                }
            }
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isCustomerCategoryHeaderVisible(), true, "Customer Category header is visible", "Customer Category  header is NOT visible"));
            assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getCustomerCategory(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getCustomerCategory()), "Customer Category  is same as Expected", "Customer Category  is not same as Expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testCustomerIdAndCategory " + e, true);
        }
    }


    @Test(priority = 8, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void testPinSetAndReset() {
        try {
            selUtils.addTestcaseDescription("Validate Pin Set, Validate Pin Reset", "description");
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isPinResetHeaderVisible(), true, "Pin Reset header is visible", "Pin Reset header is NOT visible"));
            String pinReset = pages.getPsbDemographicWidget().getPinReset();
            assertCheck.append(actions.matchUiAndAPIResponse(pinReset, pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getDetails().get(0).getIsPinReset()), "Pin Reset is same as Expected", "Pin Reset is not same as Expected"));
            if (pinReset.equalsIgnoreCase("YES"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getPinResetSetColour(), "#33a833", "Colour of Pin Reset is same as expected", "Colour of Pin Reset is NOT same as expected"));
            else if (pinReset.toLowerCase().equalsIgnoreCase("NO"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getPinResetSetColour(), "#e4000e", "Colour of Pin Reset is same as expected", "Colour of Pin Reset is NOT same as expected"));
            assertCheck.append(actions.assertEqualBoolean(pages.getPsbDemographicWidget().isPinSetHeaderVisible(), true, "Pin Set header is visible", "Pin Set header is NOT visible"));
            assertCheck.append(actions.matchUiAndAPIResponse(pages.getPsbDemographicWidget().getPinSet(), pages.getDemoGraphicPage().getKeyValueAPI(clmDetails.getResult().getDetails().get(0).getIsPinSet()), "Pin Set is same as Expected", "Pin Set is not same as Expected"));
            String pinSet = pages.getPsbDemographicWidget().getPinSet();
            if (pinSet.equalsIgnoreCase("YES"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getPinSetColour(), "#33a833", "Colour of Pin Set is same as expected", "Colour of Pin Set is NOT same as expected"));
            else if (pinSet.equalsIgnoreCase("NO"))
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getPinSetColour(), "#e4000e", "Colour of Pin Set is same as expected", "Colour of Pin Set is NOT same as expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
            commonLib.fail("Exception in method - testPinSetAndReset " + e, true);
        }
    }

    @Test(priority = 9, groups = {"RegressionTest"},dependsOnMethods = {"openCustomerInteraction"})
    public void invalidMsisdnTest() {
        try {
            selUtils.addTestcaseDescription("Search invalid MSISDN, Validate error message", "description");
            invalidCustomerNumber = constants.getValue(ApplicationConstants.INVALID_CUSTOMER_MSISDN);
            pages.getMsisdnSearchPage().enterNumberOnDashboardSearch(invalidCustomerNumber);
            pages.getDemoGraphicPage().clickOnDashboardSearch();
                String errorMessage = "Invalid Nuban ID/MSISDN. Please correct Nuban ID/MSISDN to proceed forward";
                assertCheck.append(actions.assertEqualStringType(pages.getPsbDemographicWidget().getNubanIdErrorMessage(), errorMessage, "Error message is same as Expected when invalid msisdn is searched", "Error message is not same as Expected when invalid msisdn is searched"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - invalidMsisdnTest" + e.fillInStackTrace(), true);
        }
    }
}
