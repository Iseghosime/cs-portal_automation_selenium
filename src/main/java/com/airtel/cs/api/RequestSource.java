package com.airtel.cs.api;

import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.URIConstants;
import com.airtel.cs.commonutils.restutils.RestCommonUtils;
import com.airtel.cs.pojo.request.AccountBalanceRequest;
import com.airtel.cs.pojo.request.AccumulatorsRequest;
import com.airtel.cs.pojo.request.GenericRequest;
import com.airtel.cs.pojo.request.LoanRequest;
import com.airtel.cs.pojo.request.MoreTransactionHistoryRequest;
import com.airtel.cs.pojo.request.RechargeHistoryRequest;
import com.airtel.cs.pojo.request.RingtonDetailsRequest;
import com.airtel.cs.pojo.request.SMSHistoryRequest;
import com.airtel.cs.pojo.request.ServiceProfileRequest;
import com.airtel.cs.pojo.request.TransactionHistoryRequest;
import com.airtel.cs.pojo.request.UsageHistoryMenuRequest;
import com.airtel.cs.pojo.request.UsageHistoryRequest;
import com.airtel.cs.pojo.request.VoucherSearchRequest;
import com.airtel.cs.pojo.response.AMProfilePOJO;
import com.airtel.cs.pojo.response.AccountsBalancePOJO;
import com.airtel.cs.pojo.response.GsmKycPOJO;
import com.airtel.cs.pojo.response.LoginPOJO;
import com.airtel.cs.pojo.response.PlansPOJO;
import com.airtel.cs.pojo.response.ProfilePOJO;
import com.airtel.cs.pojo.response.RechargeHistoryPOJO;
import com.airtel.cs.pojo.response.UsageHistoryPOJO;
import com.airtel.cs.pojo.response.accumulators.AccumulatorsPOJO;
import com.airtel.cs.pojo.response.airtelmoney.AirtelMoneyPOJO;
import com.airtel.cs.pojo.response.clearrefillstatus.RefillStatus;
import com.airtel.cs.pojo.response.configuration.ConfigurationPOJO;
import com.airtel.cs.pojo.response.crbt.ActivateRingtone;
import com.airtel.cs.pojo.response.crbt.Top20Ringtone;
import com.airtel.cs.pojo.response.hlrservice.HLRServicePOJO;
import com.airtel.cs.pojo.response.kycprofile.KYCProfile;
import com.airtel.cs.pojo.response.loandetails.Loan;
import com.airtel.cs.pojo.response.loansummary.Summary;
import com.airtel.cs.pojo.response.smshistory.SMSHistoryPOJO;
import com.airtel.cs.pojo.response.tariffplan.AvailablePlanPOJO;
import com.airtel.cs.pojo.response.tariffplan.CurrentPlanPOJO;
import com.airtel.cs.pojo.response.ticketlist.TicketPOJO;
import com.airtel.cs.pojo.response.vendors.VendorNames;
import com.airtel.cs.pojo.response.voucher.VoucherSearchPOJO;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class RequestSource extends RestCommonUtils {

    public static Integer statusCode = null;
    private static final String TARIFF_PLAN_TEST_NUMBER = constants.getValue(ApplicationConstants.TARIFF_PLAN_TEST_NUMBER);
    private static final Map<String, Object> queryParam = new HashMap<>();

    /*
    This Method will hit the Available Plan API and returns the response
     */
    public AvailablePlanPOJO availablePlanPOJO() {
        AvailablePlanPOJO result = null;
        try {
            commonPostMethod(URIConstants.TARIFF_AVAILABLE_PLANS, new GenericRequest(TARIFF_PLAN_TEST_NUMBER));
            result = response.as(AvailablePlanPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - availablePlanPOJO " + e.getMessage(), false);
        }
        return result;
    }

    /*
    This Method will hit the Current Plan API and returns the response
     */
    public CurrentPlanPOJO currentPlanPOJO() {
        CurrentPlanPOJO result = null;
        try {
            commonPostMethod(URIConstants.TARIFF_CURRENT_PLAN, new GenericRequest(TARIFF_PLAN_TEST_NUMBER));
            result = response.as(CurrentPlanPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - currentPlanPOJO " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the Login API "/auth/api/user-mngmnt/v2/login" and return the response
     *
     * @param body body of the API
     * @return response
     */
    public LoginPOJO loginPOJO(String body) {
        LoginPOJO result = null;
        try {
            commonLib.info("Logging in Using Login API for getting TOKEN with user");
            commonPostMethod(URIConstants.V2_LOGIN, body);
            result = response.as(LoginPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - loginPOJO " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/kyc/profile" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public KYCProfile kycProfileAPITest(String msisdn) {
        KYCProfile result = null;
        try {
            commonPostMethod(URIConstants.KYC_PROFILE, new GenericRequest(msisdn));
            result = response.as(KYCProfile.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - kycProfileAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/profile" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public ProfilePOJO profileAPITest(String msisdn) {
        ProfilePOJO result = null;
        try {
            commonPostMethod(URIConstants.GSM_PROFILE, new GenericRequest(msisdn));
            result = response.as(ProfilePOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - profileAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-am-service/v1/profile" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public AMProfilePOJO amServiceProfileAPITest(String msisdn) {
        AMProfilePOJO result = null;
        try {
            queryParam.put("msisdn", msisdn);
            queryParam.put("walletType", "Main");
            commonGetMethodWithQueryParam(URIConstants.AM_PROFILE, queryParam);
            result = response.as(AMProfilePOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - amServiceProfileAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/gsm/kyc" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public GsmKycPOJO gsmKYCAPITest(String msisdn) {
        GsmKycPOJO result = null;
        try {
            commonPostMethod(URIConstants.GSM_KYC, new GenericRequest(msisdn));
            result = response.as(GsmKycPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - gsmKYCAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/account/plans" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public PlansPOJO accountPlansTest(String msisdn) {
        PlansPOJO result = null;
        try {
            commonPostMethod(URIConstants.ACCOUNT_PLAN, new GenericRequest(msisdn));
            result = response.as(PlansPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - accountPlansTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/usage/history" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public UsageHistoryPOJO usageHistoryTest(String msisdn) {
        UsageHistoryPOJO result = null;
        try {
            commonPostMethod(URIConstants.USAGE_HISTORY, new UsageHistoryRequest(msisdn, 5, 1, null, null, null, "More"));
            result = response.as(UsageHistoryPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - usageHistoryTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/usage/history" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public UsageHistoryPOJO usageHistoryMenuTest(String msisdn) {
        UsageHistoryPOJO result = null;
        try {
            commonPostMethod(URIConstants.USAGE_HISTORY, new UsageHistoryMenuRequest(msisdn, 5, 1, null, null, null, "More", "FREE"));
            result = response.as(UsageHistoryPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - usageHistoryTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/recharge/history" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response,"
     */
    public RechargeHistoryPOJO rechargeHistoryAPITest(String msisdn) {
        RechargeHistoryPOJO result = null;
        try {
            commonPostMethod(URIConstants.RECHARGE_HISTORY, new RechargeHistoryRequest(msisdn, 5, 1, null, null, null));
            result = response.as(RechargeHistoryPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - rechargeHistoryAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-am-service/v1/transaction/history" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public AirtelMoneyPOJO transactionHistoryAPITest(String msisdn) {
        AirtelMoneyPOJO result = null;
        try {
            commonPostMethod(URIConstants.TRANSACTION_HISTORY, new TransactionHistoryRequest(msisdn, 5, 1, null, null));
            result = response.as(AirtelMoneyPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - transactionHistoryAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-am-service/v1/transaction/history" and return the response
     *
     * @param msisdn       The msisdn
     * @param currencyType The currencyType
     * @return The Response
     */
    public AirtelMoneyPOJO moreTransactionHistoryAPITest(String msisdn, String currencyType) {
        AirtelMoneyPOJO result = null;
        try {
            commonPostMethod(URIConstants.TRANSACTION_HISTORY, new MoreTransactionHistoryRequest(msisdn, 5, 1, null, null, null, null, currencyType, true));
            result = response.as(AirtelMoneyPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - moreTransactionHistoryAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/accounts/balance" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public AccountsBalancePOJO balanceAPITest(String msisdn) {
        AccountsBalancePOJO result = null;
        try {
            commonPostMethod(URIConstants.ACCOUNT_BALANCE, new AccountBalanceRequest(msisdn, 10, 1));
            result = response.as(AccountsBalancePOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - balanceAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/sr/api/sr-service/v1/fetch/ticket" and return the response
     *
     * @param ticketId The ticketId
     * @return The Response
     */
    public TicketPOJO ticketMetaDataTest(String ticketId) {
        TicketPOJO result = null;
        try {
            queryParam.put("id", ticketId);
            commonGetMethodWithQueryParam(URIConstants.SR_FETCH_HISTORY, queryParam);
            result = response.as(TicketPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - ticketMetaDataTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-notification-service/v1/fetch/history" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public SMSHistoryPOJO smsHistoryTest(String msisdn) {
        SMSHistoryPOJO result = null;
        try {
            commonPostMethod(URIConstants.NOTIFICATION_FETCH_HISTORY, new SMSHistoryRequest(msisdn, 10, 0));
            result = response.as(SMSHistoryPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - smsHistoryTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/voucher/detail" and return the response
     *
     * @param voucherId The voucherId
     * @return The Response
     */
    public VoucherSearchPOJO voucherSearchTest(String voucherId) {
        VoucherSearchPOJO result = null;
        try {
            commonPostMethod(URIConstants.VOUCHER_DETAILS, new VoucherSearchRequest(voucherId));
            result = response.as(VoucherSearchPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - voucherSearchTest " + e.getMessage(), false);
        }
        return result;
    }

    /*
    This Method will hit the API "/cs-vas-service/v1/vendors" and return the response
     */
    public VendorNames vendorsNamesTest() {
        VendorNames result = null;
        try {
            commonGetMethod(URIConstants.VENDORS);
            result = response.as(VendorNames.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - vendorsNamesTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-vas-service/v1/loan/summary" and return the response
     *
     * @param msisdn     The msisdn
     * @param vendorName The vendorName
     * @return The Response
     */
    public Summary loanSummaryTest(String msisdn, String vendorName) {
        Summary result = null;
        try {
            commonPostMethod(URIConstants.LOAN_SUMMARY, new LoanRequest(msisdn, vendorName));
            result = response.as(Summary.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - loanSummaryTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-vas-service/v1/loan/details" and return the response
     *
     * @param msisdn     The msisdn
     * @param vendorName The vendorName
     * @return The Response
     */
    public Loan loanDetailsTest(String msisdn, String vendorName) {
        Loan result = null;
        try {
            commonPostMethod(URIConstants.LOAN_DETAILS, new LoanRequest(msisdn, vendorName));
            result = response.as(Loan.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - loanDetailsTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/refill/status" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public RefillStatus clearRefillTest(String msisdn) {
        RefillStatus result = null;
        try {
            commonPostMethod(URIConstants.REFILL_STATUS, new GenericRequest(msisdn));
            result = response.as(RefillStatus.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - clearRefillTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-vas-service/v1/search/tunes" and return the response
     *
     * @param msisdn     The msisdn
     * @param searchBy   The searchBy
     * @param searchText The searchText
     * @return The Response
     */
    public Top20Ringtone ringtoneDetailTest(String msisdn, String searchBy, String searchText) {
        Top20Ringtone result = null;
        try {
            commonPostMethod(URIConstants.SEARCH_TUNES, new RingtonDetailsRequest(msisdn, searchBy, searchText));
            result = response.as(Top20Ringtone.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - ringtoneDetailTest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-vas-service/v1/fetch/tunes" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public ActivateRingtone activateRingtone(String msisdn) {
        ActivateRingtone result = null;
        try {
            queryParam.put("msisdn", msisdn);
            commonGetMethodWithQueryParam(URIConstants.FETCH_TUNES, queryParam);
            result = response.as(ActivateRingtone.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - activateRingtone " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/accumulators" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public AccumulatorsPOJO accumulatorsAPITest(String msisdn) {
        AccumulatorsPOJO result = null;
        try {
            commonPostMethod(URIConstants.ACCUMULATORS, new AccumulatorsRequest(msisdn, 5, 1));
            result = response.as(AccumulatorsPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - accumulatorsAPITest " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-gsm-service/v1/hlr/service/profiles" and return the response
     *
     * @param msisdn The msisdn
     * @return The Response
     */
    public HLRServicePOJO getServiceProfileWidgetInfo(String msisdn) {
        HLRServicePOJO result = null;
        try {
            commonPostMethod(URIConstants.SERVICE_PROFILE, new ServiceProfileRequest(msisdn, 5, 1));
            result = response.as(HLRServicePOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - getServiceProfileWidgetInfo " + e.getMessage(), false);
        }
        return result;
    }

    /**
     * This Method will hit the API "/cs-service/api/cs-service/v1/configurations" and return the response
     *
     * @param key The key
     * @return The Response
     */
    public ConfigurationPOJO getConfiguration(String key) {
        ConfigurationPOJO result = null;
        try {
            queryParam.put("keys", key);
            queryParam.put("opco", OPCO);
            commonGetMethodWithQueryParam(URIConstants.CONFIGURATIONS, queryParam);
            result = response.as(ConfigurationPOJO.class);
        } catch (Exception e) {
            commonLib.fail("Exception in method - getConfiguration " + e.getMessage(), false);
        }
        return result;
    }
}