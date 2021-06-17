package com.airtel.cs.ui.frontendagent.hometab;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.common.actions.BaseActions;
import com.airtel.cs.commonutils.UtilsMethods;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.HeaderDataBean;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.response.RechargeHistoryPOJO;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RechargeHistoryMenuWidgetTest extends Driver {
    private static String customerNumber = null;
    private final BaseActions actions = new BaseActions();
    RequestSource api = new RequestSource();

    @BeforeMethod
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void openCustomerInteractionAPI() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.RECHARGE_HISTORY_MSISDN);
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

    @DataProviders.Table(name = "More Recharge History")
    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dataProvider = "HeaderData", dataProviderClass = DataProviders.class, dependsOnMethods = "openCustomerInteractionAPI")
    public void rechargeHistoryMenuTest(HeaderDataBean data) {
        try {
            selUtils.addTestcaseDescription("Validating Recharge History's  Menu of User :" + customerNumber + "validate recharge menu widget display all header display as per config,Validate all the data rows must be display as per api response.", "description");
            assertCheck.append(actions.assertEqual_boolean(pages.getRechargeHistoryWidget().isRechargeHistoryWidgetMenuVisible(), true, "Recharge History's MENU is visible ", "Recharge History's MENU is not visible "));
            pages.getRechargeHistoryWidget().openingRechargeHistoryDetails();
            assertCheck.append(actions.assertEqual_boolean(pages.getMoreRechargeHistoryPage().isDatePickerVisible(), true, "Date Picker is visible as expected", "Date picker is not visible "));
            RechargeHistoryPOJO rechargeHistoryAPI = api.rechargeHistoryAPITest(customerNumber);
            int size = pages.getMoreRechargeHistoryPage().getNumbersOfRows();
            try {
                if (rechargeHistoryAPI.getResult().size() == 0 || rechargeHistoryAPI.getResult() == null) {
                    commonLib.warning("Unable to get DATA History Details from com.airtel.cs.API");
                    assertCheck.append(actions.assertEqual_boolean(pages.getMoreRechargeHistoryPage().getNoResultFound(), true, "No Result icon displayed as expected.", "No Result Message is not Visible"));
                } else {
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(1), data.getRow1(), "Header Name for Row 1 is as expected", "Header Name for Row 1 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(2), data.getRow2(), "Header Name for Row 2 is as expected", "Header Name for Row 2 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(3), data.getRow3(), "Header Name for Row 3 is as expected", "Header Name for Row 3 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(4) + pages.getMoreRechargeHistoryPage().getSubHeaders(4).replace("|", ""), data.getRow4().replace("|", ""), "Header Name for Row 4 is as expected", "Header Name for Row 4 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(5), data.getRow5(), "Header Name for Row 5 is as expected", "Header Name for Row 5 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(6), data.getRow6(), "Header Name for Row 6 is as expected", "Header Name for Row 6 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(7), data.getRow7(), "Header Name for Row 7 is as expected", "Header Name for Row 7 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(8), data.getRow8(), "Header Name for Row 8 is as expected", "Header Name for Row 8 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(9), data.getRow9(), "Header Name for Row 9 is as expected", "Header Name for Row 9 is not as expected"));
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getHeaders(10), data.getRow10(), "Header Name for Row 10 is as expected", "Header Name for Row 10 is not as expected"));
                    for (int i = 0; i < size; i++) {
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 1).trim(), rechargeHistoryAPI.getResult().get(i).getCharges().trim(), " Charges received is as expected on row " + i, " Charges received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 2), UtilsMethods.getDateFromString(rechargeHistoryAPI.getResult().get(i).getDateTime(), constants.getValue(CommonConstants.UI_RECHARGE_HISTORY_PATTERN), constants.getValue(CommonConstants.API_RECHARGE_HISTORY_PATTERN)), "Date & Time received is as expected on row " + i, "Date & Time received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 3).replaceAll("[^a-zA-Z]+", ""), rechargeHistoryAPI.getResult().get(i).getBundleName().replaceAll("[^a-zA-Z]+", ""), "Bundle Name received is as expected on row " + i, "Bundle Name received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 4).replaceAll("[^a-zA-Z0-9]+", ""), ((rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getVOICE() == null) ? "" : rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getVOICE()) + ((rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getDATA() == null) ? "" : rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getDATA()) + ((rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getSMS() == null) ? "" : rechargeHistoryAPI.getResult().get(i).getRechargeBenefit().getSMS()), "Recharge Benefits received is as expected on row " + i, "Recharge Benefits received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 5), rechargeHistoryAPI.getResult().get(i).getStatus().trim().toLowerCase(), "Status received is as expected on row " + i, "Status received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 6), rechargeHistoryAPI.getResult().get(i).getMode(), "Mode of recharge received is as expected on row " + i, "Mode of recharge received is not as expected on row " + i));
                        if (rechargeHistoryAPI.getResult().get(i).getMode().equalsIgnoreCase("Voucher")) {
                            assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 7).trim(), rechargeHistoryAPI.getResult().get(i).getSerialNumber().trim(), "Serial Number received is as expected on row " + i, "Serial Number received is not as expected on row " + i));
                        } else {
                            assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 7), "-", "Serial Number received is as expected on row " + i, "Serial Number received is not as expected on row " + i));
                        }
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 8), ((rechargeHistoryAPI.getResult().get(i).getExpiryDate() == null) ? "-" : UtilsMethods.getDateFromString(rechargeHistoryAPI.getResult().get(i).getExpiryDate(), constants.getValue(CommonConstants.API_RECHARGE_HISTORY_PATTERN), constants.getValue(CommonConstants.API_RECHARGE_HISTORY_PATTERN))), "Expiry date received is as expected on row " + i, "Expiry date received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 9), "-", "Old Expiry date received is as expected on row " + i, "Old Expiry date received is not as expected on row " + i));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 10).trim(), ((rechargeHistoryAPI.getResult().get(i).getValidity() == null) ? "-" : rechargeHistoryAPI.getResult().get(i).getValidity()), "Validity received is as expected on row " + i, "Validity received is not as expected on row " + i));
                        if (i != 0) {
                            assertCheck.append(actions.assertEqual_boolean(UtilsMethods.isSortOrderDisplay(pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 2), pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i, 2), constants.getValue(CommonConstants.UI_RECHARGE_HISTORY_PATTERN)), true, "In Sort order display on ui as expected", pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i + 1, 2) + "should not display before " + pages.getMoreRechargeHistoryPage().getValueCorrespondingToRechargeHeader(i, 2)));
                        }
                    }
                }
            } catch (NoSuchElementException | TimeoutException | NullPointerException e) {
                e.printStackTrace();
                commonLib.fail("Not able to validate Correctly more recharge history widget: " + e.fillInStackTrace(), true);
            }
            pages.getMoreRechargeHistoryPage().openingCustomerInteractionDashboard();
        } catch (Exception e) {
            commonLib.fail("Exception in Method - rechargeHistoryMenuTest" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }
}