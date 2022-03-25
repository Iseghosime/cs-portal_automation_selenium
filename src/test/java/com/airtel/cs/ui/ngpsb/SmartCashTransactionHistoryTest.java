package com.airtel.cs.ui.ngpsb;

import com.airtel.cs.api.PsbRequestSource;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.dataproviders.databeans.HeaderDataBean;
import com.airtel.cs.commonutils.dataproviders.dataproviders.DataProviders;
import com.airtel.cs.commonutils.utils.UtilsMethods;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.airtelmoney.AirtelMoney;
import com.airtel.cs.model.cs.response.psb.cs.clmdetails.CLMDetailsResponse;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SmartCashTransactionHistoryTest extends Driver {
    private static String customerNumber = null;
    PsbRequestSource api = new PsbRequestSource();
    CLMDetailsResponse clmDetails;
    AirtelMoney amTransactionHistoryAPI;

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest", "SmokeTest"})
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest", "SmokeTest"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_TIER1_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(customerNumber);
            pages.getMsisdnSearchPage().clickOnSearch();
            clmDetails = api.getCLMDetails(customerNumber);
            assertCheck.append(actions.assertEqualIntType(clmDetails.getStatusCode(), 200, "CLM Details API Status Code Matched and is :" + clmDetails.getStatusCode(), "CLM Details API Status Code NOT Matched and is :" + clmDetails.getStatusCode(), false));
            if (clmDetails.getStatusCode() == 200) {
                boolean pageLoaded = pages.getDemographicWidget().isPageLoaded(clmDetails);
                if (!pageLoaded)
                    continueExecutionFA = false;
            } else
                commonLib.warning("Clm Details API is not working");
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openCustomerInteraction" + e.fillInStackTrace(), true);
        }
    }

    @DataProviders.Table(name = "More Airtel Money Secondary History")
    @Test(priority = 2, groups = {"ProdTest", "SmokeTest"}, dataProvider = "HeaderData", dataProviderClass = DataProviders.class, dependsOnMethods = {"openCustomerInteraction"})
    public void transactionHistoryWidgetLayoutTest(HeaderDataBean data) {
        try {
            selUtils.addTestcaseDescription("Validating Smart Cash Transaction History's Header Name ,Validating all the filter displayed as per config,Validate search by transaction id box displayed as per config.", "description");
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isSmartCashTransactionHistoryVisible(), true, "AM More Secondary transaction widget display as expected", "AM More Secondary transaction widget does not display as expected"));
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isTodayFilterTabOnSecondWidget(), true, "Today Filter does display on UI.", "Today Filter does not display."));
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isTodayFilterTabOnSecondWidget(), true, "Last Two Days Filter does display on UI.", "Last Two Days Filter does not display."));
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isTodayFilterTabOnSecondWidget(), true, "Last Seven Days Filter does display on UI.", "Last Seven Days Filter does not display."));
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isTodayFilterTabOnSecondWidget(), true, "Date Range Filter does display on UI.", "Date Range Filter does not display."));
            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isSearchTxnIdBoxOnSecondWidget(), true, "TXN ID Box does display on UI.", "TXN ID Box does not display."));
            String nubanId = clmDetails.getResult().getDetails().get(0).getAccounts().get(0).getId();
            amTransactionHistoryAPI = api.getTransactionHistory(customerNumber,nubanId );
            final int statusCode = amTransactionHistoryAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "Transaction History  API success and status code is :" + statusCode, "Transaction History API got failed and status code is :" + statusCode, false));
            if (statusCode != 200) {
                assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isAirtelMoneyErrorVisibleOnSecondWidget(), true, "API is Giving error and Widget is showing error Message on API is " + amTransactionHistoryAPI.getMessage(), "API is Giving error But Widget is not showing error Message on API is " + amTransactionHistoryAPI.getMessage()));
                commonLib.fail("API is Unable to Get Transaction History for Customer", true);
            } else if (amTransactionHistoryAPI.getResult().getTotalCount() == null) {
                assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isAirtelMoneyNoResultFoundVisibleOnSecondWidget(), true, "No Result Found Icon does display on UI.", "No Result Found Icon does not display on UI."));

            } else {
                int count = Math.min(amTransactionHistoryAPI.getResult().getTotalCount(), 10);
                if (count > 0) {
                    for (int i = 0; i < data.getHeaderName().size(); i++) {
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getHeadersOnSecondWidget(i + 1), data.getHeaderName().get(i), "Header Name for Row " + (i + 1) + " is as expected", "Header Name for Row " + (i + 1) + " is not as expected"));
                    }
                }
            }
        } catch (Exception e) {
            commonLib.fail("Exception in Method - transactionHistoryWidgetLayoutTest" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }

    @DataProviders.Table(name = "More Airtel Money History")
    @Test(priority = 3, groups = {"ProdTest"}, dependsOnMethods = {"amDetailedHistorySecondaryWidgetHeaderTest", "openCustomerInteraction"})
    public void transactionHistoryWidgetDataTest() {
        try {
            selUtils.addTestcaseDescription("Validate all the row data display on UI as per api response.", "description");
            final int statusCode = amTransactionHistoryAPI.getStatusCode();
            if (statusCode != 200) {
                assertCheck.append(actions.assertEqualBoolean(pages.getMoreAMTxnTabPage().isAirtelMoneyErrorVisibleOnSecondWidget(), true, "API is Giving and Widget is showing error Message on API is " + amTransactionHistoryAPI.getMessage(), "API is Giving error But Widget is not showing error Message on API is " + amTransactionHistoryAPI.getMessage()));
                commonLib.fail("API is Unable to Get Smart Cash Transaction History for Customer", false);
            } else if (amTransactionHistoryAPI.getResult().getTotalCount() == null) {
                assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isAirtelMoneyNoResultFoundVisibleOnSecondWidget(), true, "No Result Found Icon does display on UI.", "No Result Found Icon does not display on UI."));
            } else {
                int count = Math.min(amTransactionHistoryAPI.getResult().getTotalCount(), 10);
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        if (amTransactionHistoryAPI.getResult().getData().get(i).getAmount().charAt(0) == '+') {
                            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isPosSignDisplayOnSecondWidget(i + 1), true, i + "th Positive Sign does display in case of Amount Credited.", i + "th Positive Sign does not display in case of Amount Credited."));
                        } else {
                            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isNegSignDisplayOnSecondWidget(i + 1), true, i + "th Negative Sign does display in case of Amount Debited.", i + "th Negative Sign does not display in case of Amount Debited."));
                        }
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 2).replaceAll("\\R", " "), UtilsMethods.getDateFromEpoch(new Long(amTransactionHistoryAPI.getResult().getData().get(i).getTransactionDate()), constants.getValue(CommonConstants.AM_HISTORY_TIME_FORMAT)), i + "th Date is expected as API response.", i + "th Date is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 3), amTransactionHistoryAPI.getResult().getData().get(i).getTransactionType(), i + "th Transaction Type is same as expected in API response.", i + "th Transaction Type is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 4), amTransactionHistoryAPI.getResult().getData().get(i).getSource(), i + "th Sender MSISDN is expected as API response.", i + "th Sender MSISDN is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 5), amTransactionHistoryAPI.getResult().getData().get(i).getMsisdn(), i + "th Receiver MSISDN is expected as API response.", i + "th Receiver MSISDN is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 6), amTransactionHistoryAPI.getResult().getData().get(i).getSecondPartyName(), i + "th Beneficiary name is expected as API response.", i + "th Beneficiary name is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 7), amTransactionHistoryAPI.getResult().getData().get(i).getTransactionId(), i + "th Transaction Id is expected as API response.", i + "th Transaction Id is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 8), amTransactionHistoryAPI.getResult().getData().get(i).getTxnChannel(), i + "th Transaction Channel is expected as API response.", i + "th Transaction Channel is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 9), amTransactionHistoryAPI.getResult().getData().get(i).getServiceCharge(), i + "th Service Charge is expected as API response.", i + "th Service Charge is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 10), amTransactionHistoryAPI.getResult().getData().get(i).getBalanceBefore(), i + "th Pre-balance is expected as API response.", i + "th Pre-balance is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 11), amTransactionHistoryAPI.getResult().getData().get(i).getBalanceAfter(), i + "th Post-balance is expected as API response.", i + "th Post-balance is not expected as API response."));
                        assertCheck.append(actions.matchUiAndAPIResponse(pages.getSmartCashTransactionHistory().getValueCorrespondingToHeader(i + 1, 12), amTransactionHistoryAPI.getResult().getData().get(i).getStatus(), i + "th Status is expected as API response.", i + "th Status is not expected as API response."));
                        if (amTransactionHistoryAPI.getResult().getData().get(i).getEnableResendSms()) {
                            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isResendSMSIconVisible(i+1,1), true, "Resend SMS Icon does enable as mentioned in API Response.", "Resend SMS Icon does not enable as mentioned in API Response."));
                        }
                        if (amTransactionHistoryAPI.getResult().getData().get(i).getIsReversal()) {
                            assertCheck.append(actions.assertEqualBoolean(pages.getSmartCashTransactionHistory().isReversalIconVisible(i+1,1), true, "Reversal Icon does enable as mentioned in API Response.", "Reversal SMS Icon does not enable as mentioned in API Response."));
                        }
                    }
                }
            }
        } catch (Exception e) {
            commonLib.fail("Exception in Method - transactionHistoryWidgetDataTest" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }
}
