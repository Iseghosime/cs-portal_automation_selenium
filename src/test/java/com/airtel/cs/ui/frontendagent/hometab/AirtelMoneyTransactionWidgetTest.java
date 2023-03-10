package com.airtel.cs.ui.frontendagent.hometab;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.commonutils.utils.UtilsMethods;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.dataproviders.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.databeans.HeaderDataBean;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.actionconfig.ActionConfigResult;
import com.airtel.cs.model.cs.response.actionconfig.Condition;
import com.airtel.cs.model.cs.response.agents.RoleDetails;
import com.airtel.cs.model.cs.response.airtelmoney.AirtelMoney;
import com.airtel.cs.model.cs.response.amprofile.AMProfile;
import com.airtel.cs.pagerepository.pagemethods.AMTransactionsWidget;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

@Log4j2
public class AirtelMoneyTransactionWidgetTest extends Driver {

    public static final String RUN_AIRTEL_MONEY_WIDGET_TEST_CASE = constants.getValue(ApplicationConstants.RUN_AIRTEL_MONEY_WIDGET_TESTCASE);
    private static String customerNumber = null;
    RequestSource api = new RequestSource();

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void checkExecution() {
        if (!continueExecutionFA) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void checkAirtelMoneyFlag() {
        if (!StringUtils.equals(RUN_AIRTEL_MONEY_WIDGET_TEST_CASE, "true")) {
            commonLib.skip("Airtel Money Widget is NOT enabled for this Opco = " + OPCO);
            throw new SkipException("Skipping because this functionality does not applicable for current Opco");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void openCustomerInteraction() {
        try {
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.AM_CUSTOMER_MSISDN);
            pages.getSideMenuPage().clickOnSideMenu();
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

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void airtelMoneyHeaderTest() {
        try {
            String amWidgetId = pages.getAmTxnWidgetPage().getAMWidgetId();
            selUtils.addTestcaseDescription("Validate Airtel Money Header is Visible,Validate Airtel Money Header Text,Validate Date Picker is visible,Validate Transaction Id Search Box is visible", "description");
            assertCheck.append(actions.assertEqualBoolean(widgetMethods.isWidgetVisible(amWidgetId), true, "Airtel Money Transaction Widget is visible", "Airtel Money Transaction Widget is not visible"));
            assertCheck.append(actions.assertEqualStringType(widgetMethods.getBottomAuuidText(amWidgetId), loginAUUID, "Auuid shown at the footer of the Airtel Money Txn widget and is correct", "Auuid NOT shown at the footer of Airtel Money Txn widget"));
            assertCheck.append(actions.assertEqualStringType(widgetMethods.getMiddleAuuidText(amWidgetId), loginAUUID, "Auuid shown at the middle of the Airtel Money Txn widget and is correct", "Auuid NOT shown at the middle of Airtel Money Txn widget"));
            assertCheck.append(actions.assertEqualStringType(widgetMethods.getWidgetTitle(amWidgetId), constants.getValue(CommonConstants.AIRTEL_MONEY_WIDGET_TITLE), "Airtel Money Widget Header Text Matched", "Airtel Money Widget Header Text NOT Matched"));
            assertCheck.append(actions.assertEqualBoolean(widgetMethods.checkDatePickerVisible(amWidgetId), true, "Airtel Money Transaction Widget's Date Picker is visible", "Airtel Money Transaction Widget's Date Picker is not visible"));
            //Removed Search functionality in Airtel money
            //assertCheck.append(actions.assertEqualBoolean(widgetMethods.checkSearchBoxVisible(amWidgetId), true, "Transaction Id Search Box displayed on UI", "Transaction Id Search Box does not displayed on UI"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - airtelMoneyHeaderTest" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 3, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = "openCustomerInteraction")
    public void airtelMoneyBalanceTest() {
        try {
            selUtils.addTestcaseDescription("Validating Airtel Money Balance", "description");
            final AMTransactionsWidget amTxnWidgetPage = pages.getAmTxnWidgetPage();
            AMProfile amProfileAPI = api.amServiceProfileAPITest(customerNumber);
            final int statusCode = amProfileAPI.getStatusCode();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "AM Profile API success and status code is :" + statusCode, "AM Profile API got failed and status code is :" + statusCode, false));
            if (statusCode == 200) {
                assertCheck.append(actions.assertEqualStringType(amTxnWidgetPage.gettingAirtelMoneyBalance(), amProfileAPI.getResult().getWallets().get(0).getBalance(), "Customer's Airtel Wallet Balance as Expected", "Customer's Airtel Wallet Balance not same not as Expected"));
                assertCheck.append(actions.assertEqualStringType(amTxnWidgetPage.gettingAirtelMoneyCurrency(), amProfileAPI.getResult().getWallets().get(0).getCurrency(), "Customer's Currency code as Expected", "Customer's Currency code not same not as Expected"));
                if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.MULTI_WALLET_BALANCE), "true")) {
                    assertCheck.append(actions.assertEqualStringType(amTxnWidgetPage.gettingAirtelMoneyBalance2(), amProfileAPI.getResult().getWallets().get(1).getBalance(), "Customer's Airtel Wallet2 Balance & Currency code as Expected", "Customer's Airtel Wallet2 Balance & Currency code not same not as Expected"));
                }
            } else {
                commonLib.fail("API is Unable to Get AM Profile for Customer", false);
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - airtelMoneyTransactionWidgetTest" + e.fillInStackTrace(), true);
        }
    }

    @DataProviders.Table(name = "Airtel Money")
    @Test(priority = 4, dataProvider = "HeaderData", dataProviderClass = DataProviders.class, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = {"openCustomerInteraction"})
    public void airtelMoneyTransactionHistoryAPITest(HeaderDataBean data) throws IOException, UnsupportedFlavorException {
        try {
            selUtils.addTestcaseDescription("Validate Airtel Money Transaction History API", "description");
            final AMTransactionsWidget amTxnWidgetPage = pages.getAmTxnWidgetPage();
            AirtelMoney amTransactionHistoryAPI = api.transactionHistoryAPITest(customerNumber);
            final Integer statusCode = amTransactionHistoryAPI.getStatusCode();
            String amWidgetId = pages.getAmTxnWidgetPage().getAMWidgetId();
            assertCheck.append(actions.assertEqualIntType(statusCode, 200, "AM Txn API success and status code is :" + statusCode, "AM Txn API got failed and status code is :" + statusCode, false));
            if (statusCode != 200) {
                assertCheck.append(actions.assertEqualBoolean(widgetMethods.isWidgetErrorIconDisplay(amWidgetId), true, "API and Widget both are showing error message", "API is Giving error But Widget is not showing error Message on API is " + amTransactionHistoryAPI.getMessage()));
            } else if (amTransactionHistoryAPI.getResult().getTotalCount() == null) {
                assertCheck.append(actions.assertEqualBoolean(widgetMethods.isNoResultFoundIconDisplay(amWidgetId), true, "'No Result Found' Icon displayed", "'No Result Found' Icon NOT displayed"));
            } else {
                int count = Math.min(amTransactionHistoryAPI.getResult().getTotalCount(), 5);
                for (int i = 0; i < data.getHeaderName().size(); i++) {
                    assertCheck.append(actions.matchUiAndAPIResponse(widgetMethods.getHeaderName(amWidgetId, i), data.getHeaderName().get(i), "Header Name for Row " + (i + 1) + " is as expected", "Header Name for Row " + (i + 1) + " is not as expected"));
                }
                ActionConfigResult actionConfigResult = api.getActionConfig("reverseTransaction");
                List<String> actionConfigRoles = actionConfigResult.getRoles();
                List<RoleDetails> agentRoles = UtilsMethods.getAgentDetail().getUserDetails().getUserDetails().getRole();
                for (int i = 0; i < count; i++) {
                    boolean hasRole = agentRoles.stream().anyMatch(roleName -> actionConfigRoles.contains(roleName.getRoleName()));
                    if (ObjectUtils.isNotEmpty(actionConfigResult.getConditions())) {
                        Condition condition = actionConfigResult.getConditions().get(0);
                        String amountString = amTxnWidgetPage.getHeaderValue(i, 0).replaceAll("[^\\d.]", "");
                        String operator = condition.getOperator();
                        Integer thresholdValue = Integer.valueOf(condition.getThresholdValue());
                        int amount = StringUtils.isEmpty(amountString) ? 0 : Integer.parseInt(amountString);
                        if (hasRole && (">=".equals(operator) && amount >= thresholdValue
                                || "<".equals(operator) && amount < thresholdValue || "=".equals(operator) && amount == thresholdValue
                                || "<=".equals(operator) && amount <= thresholdValue || ">".equals(operator) && amount > thresholdValue)) {
                            assertCheck.append(actions.assertEqualBoolean(amTxnWidgetPage.isReverseIconEnable(i + 1), false, "Reverse Transaction Icon is disabled as mentioned in CS API Response", "Reverse Transaction  Icon  does not disable as mentioned in CS API Response"));
                        }
                    } else {
                        assertCheck.append(actions.assertEqualBoolean(amTxnWidgetPage.isReverseIconEnable(i + 1), true, "Reverse Transaction Icon is enabled as mentioned in CS API Response", "Reverse Transaction  Icon  does not enable as mentioned in CS API Response"));
                        assertCheck.append(actions.matchUiAndAPIResponse(amTxnWidgetPage.getHeaderValue(i, 0), amTransactionHistoryAPI.getResult().getData().get(i).getAmount(), "Amount is as expected as of CS API response", "Amount is not expected as of CS API response"));
                    }
                    assertCheck.append(actions.matchUiAndAPIResponse(amTxnWidgetPage.getHeaderValue(i, 1), amTransactionHistoryAPI.getResult().getData().get(i).getMsisdn(), "Receiver MSISDN is as expected as of CS API response", "Receiver MSISDN is not expected as of CS API response"));
                    assertCheck.append(actions.matchUiAndAPIResponse(amTxnWidgetPage.getHeaderValue(i, 2), UtilsMethods.getDateFromEpochInUTC(Long.parseLong(amTransactionHistoryAPI.getResult().getData().get(i).getTransactionDate()), constants.getValue(CommonConstants.AM_HISTORY_TIME_FORMAT)), "Date is as expected as of CS API response", "Date is not expected as of CS API response"));
                    assertCheck.append(actions.matchUiAndAPIResponse(amTxnWidgetPage.getHeaderValue(i, 3), amTransactionHistoryAPI.getResult().getData().get(i).getTransactionId(), "Transaction Id is as expected as of CS API response", "Transaction Id is not expected as of CS API response"));
                    assertCheck.append(actions.matchUiAndAPIResponse(amTxnWidgetPage.getHeaderValue(i, 4), amTransactionHistoryAPI.getResult().getData().get(i).getStatus(), "Status is as expected as of CS API response", "Status is not expected as of CS API response"));
                    assertCheck.append(actions.assertEqualBoolean(amTxnWidgetPage.isResendSMS(i + 1), amTransactionHistoryAPI.getResult().getData().get(i).getEnableResendSms(), "Resend SMS Icon is enabled/disabled as mentioned in CS API Response", "Resend SMS Icon does not enable/disable as mentioned in CS API Response"));
                    String id = amTxnWidgetPage.doubleClickOnTransactionId(i + 1);
                    String message = amTxnWidgetPage.getToastMessage();
                    assertCheck.append(actions.assertEqualBoolean(message.contains(id), true, "Transaction id copied to clipboard successfully", "Transaction id does not copy to clipboard"));
                }
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - airtelMoneyTransactionHistoryAPITest" + e.fillInStackTrace(), true);
        }
    }
}
