package API;

import POJO.*;
import POJO.Accumulators.AccumulatorsPOJO;
import POJO.CRBT.ActivateRingtone;
import POJO.CRBT.Top20Ringtone;
import POJO.ClearRefillStatus.RefillStatus;
import POJO.KYCProfile.KYCProfile;
import POJO.LoanDetails.Loan;
import POJO.LoanSummary.Summary;
import POJO.SMSHistory.SMSHistoryPOJO;
import POJO.TicketList.TicketPOJO;
import POJO.Vendors.VendorNames;
import POJO.Voucher.VoucherSearchPOJO;
import Utils.DataProviders.DataProviders;
import Utils.DataProviders.TestDatabean;
import Utils.PassUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static Utils.ExtentReports.ExtentTestManager.getTest;
import static Utils.ExtentReports.ExtentTestManager.startTest;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@Log4j2
public class APIEndPoints extends tests.BaseTest {
    @DataProviders.User(UserType = "API")
    @Test(dataProvider = "loginData", dataProviderClass = DataProviders.class, priority = 1)
    public void loginAPI(TestDatabean Data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LoginPOJO Req = LoginPOJO.loginBody(PassUtils.decodePassword(Data.getPassword()), Data.getLoginAUUID());
        map.clear();
        map.add(new Header("x-app-name", config.getProperty(Env + "-x-app-name")));
        map.add(new Header("x-service-id", config.getProperty(Env + "-x-service-id")));
        //map.add(new Header("x-bsy-bn", config.getProperty(Env + "-x-bsy-bn"))); //Comment this line this header removed from MG Opco.
        map.add(new Header("x-app-type", config.getProperty(Env + "-x-app-type")));
        map.add(new Header("x-client-id", config.getProperty(Env + "-x-client-id")));
        map.add(new Header("x-api-key", config.getProperty(Env + "-x-api-key")));
        map.add(new Header("x-login-module", config.getProperty(Env + "-x-login-module")));
        map.add(new Header("x-channel", config.getProperty(Env + "-x-channel")));
        map.add(new Header("x-app-version", config.getProperty(Env + "-x-app-version")));
        map.add(new Header("Opco", Opco));

        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Req);
        startTest("LOGIN API TEST ", "Logging in Using Login API for getting TOKEN with user : " + Data.getLoginAUUID());
        getTest().log(LogStatus.INFO, "Logging in Using Login API for getting TOKEN with user : " + Data.getLoginAUUID());
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body(dtoAsString)
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        Response response = request.post("/auth/api/user-mngmnt/v2/login");
        Token = "Bearer " + response.jsonPath().getString("result.accessToken");
        map.add(new Header("Authorization", Token));
        getTest().log(LogStatus.INFO, "Response : " + response.asString());
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");

    }

    public PlansPOJO accountPlansTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Account Plans API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/account/plans");
        getTest().log(LogStatus.INFO, "Response : " + response.asString());
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(PlansPOJO.class);
    }

    public UsageHistoryPOJO usageHistoryTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Usage History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"type\":null,\"startDate\":null,\"endDate\":null}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/usage/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(UsageHistoryPOJO.class);
    }

    public UsageHistoryPOJO usageHistoryMenuTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Usage History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"type\":null,\"startDate\":null,\"endDate\":null,\"action\":\"More\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/usage/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(UsageHistoryPOJO.class);
    }

    public AMHandSetProfilePOJO amProfileTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using AM Profile API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"extTxnId\":1234,\"msisdn\":\"" + msisdn + "\",\"walletType\":\"Main\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/am/profile");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(AMHandSetProfilePOJO.class);

    }

    public GsmKycPOJO gsmKYCAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using GSM KYC API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
//                .header("Opco", Opco)
//                .header("Host", "172.23.36.206:30050")
                .body("{\"msisdn\":\"" + msisdn + "\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/gsm/kyc");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(GsmKycPOJO.class);

    }

    public ProfilePOJO profileAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using /cs-gsm-service/v1/profile api for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/profile");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(ProfilePOJO.class);
    }

    public KYCProfile KYCProfileAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using /cs-gsm-service/v1/profile api for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/kyc/profile");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(KYCProfile.class);
    }

    public AMProfilePOJO amServiceProfileAPITest(String Msisdn) {
        getTest().log(LogStatus.INFO, "Using AM Service Profile API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .contentType("application/json")
                .queryParam("msisdn", Msisdn)
                .queryParam("walletType", "Main");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getQueryParams().toString());
        log.info("Request Body is  : " + queryable.getQueryParams().toString());
        Response response = request.get("/cs-am-service/v1/profile");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(AMProfilePOJO.class);
    }

    public RechargeHistoryPOJO rechargeHistoryAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Recharge History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"startDate\":null,\"endDate\":null,\"rechargeHistoryVoucherSearch\":null}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/recharge/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(RechargeHistoryPOJO.class);
    }

    public BundleRechargeHistoryPOJO bundleRechargeHistoryAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Recharge History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"startDate\":null,\"endDate\":null}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/bundle/recharge/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(BundleRechargeHistoryPOJO.class);
    }

    public AMTransactionHistoryPOJO transactionHistoryAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Transaction History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"startDate\":null,\"endDate\":null}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-am-service/v1/transaction/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(AMTransactionHistoryPOJO.class);
    }

    public AccountsBalancePOJO balanceAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using Balance (accounts) API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\": \"" + msisdn + "\",\"pageSize\": 10,\"pageNumber\": 1}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/accounts/balance");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");

        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(AccountsBalancePOJO.class);

    }

    public UsageHistoryPOJO usageHistoryTest(String msisdn, String Type) {
        getTest().log(LogStatus.INFO, "Using Usage History API for Getting expected data for UI");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body("{\"msisdn\":\"" + msisdn + "\",\"pageSize\":5,\"pageNumber\":1,\"startDate\":null,\"endDate\":null,\"type\":\"" + Type + "\"}")
                .contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        getTest().log(LogStatus.INFO, "Request Body is  : " + queryable.getBody().toString());
        log.info("Request Body is  : " + queryable.getBody().toString());
        Response response = request.post("/cs-gsm-service/v1/usage/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(UsageHistoryPOJO.class);
    }

    public TicketPOJO ticketMetaDataTest(String ticketId) {
        getTest().log(LogStatus.INFO, "Using fetch ticket details using ticket Id to validate ticket meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).param("id", ticketId).contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        Response response = request.get("/sr/api/sr-service/v1/fetch/ticket");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(TicketPOJO.class);
    }

    public SMSHistoryPOJO smsHistoryTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using fetch ticket details using ticket Id to validate ticket meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"receiverId\":" + msisdn + ",\"pageNumber\":0,\"pageSize\":10}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        Response response = request.post("/cs-notification-service/v1/fetch/history");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(SMSHistoryPOJO.class);
    }

    public VoucherSearchPOJO voucherSearchTest(String voucherId) {
        getTest().log(LogStatus.INFO, "Using fetch Voucher details using voucher Id to validate Voucher meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"voucherId\":\"" + voucherId + "\"}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-gsm-service/v1/voucher/detail");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(VoucherSearchPOJO.class);
    }

    public VendorNames  vendorsNamesTest() {
        getTest().log(LogStatus.INFO, "Using fetch vendor name");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers);
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        Response response = request.get("/cs-vas-service/v1/vendors");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(VendorNames.class);
    }

    public Summary loanSummaryTest(String msisdn, String vendorName) {
        getTest().log(LogStatus.INFO, "Using fetch Voucher details using voucher Id to validate Voucher meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"msisdn\":\""+msisdn+"\",\"vendorName\":\""+vendorName+"\"}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-vas-service/v1/loan/summary");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(Summary.class);
    }

    public Loan loanDetailsTest(String msisdn, String vendorName) {
        getTest().log(LogStatus.INFO, "Using Loan details API to validate Loan Detail and Loan history widget");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"msisdn\":\""+msisdn+"\",\"vendorName\":\""+vendorName+"\"}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-vas-service/v1/loan/details");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(Loan.class);
    }

    public RefillStatus clearRefillTest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using fetch Voucher details using voucher Id to validate Voucher meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"msisdn\":\""+msisdn+"\"}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-gsm-service/v1/refill/status");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(RefillStatus.class);
    }

    public Top20Ringtone ringtoneDetailTest(String msisdn,String searchBy,String searchText) {
        getTest().log(LogStatus.INFO, "Using fetch Ringtone details using MSISDN");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"searchBy\":\""+searchBy+"\",\"searchText\":\""+searchText+"\",\"msisdn\":\""+msisdn+"\"}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-vas-service/v1/search/tunes");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(Top20Ringtone.class);
    }

    public ActivateRingtone activateRingtone(String msisdn){
        getTest().log(LogStatus.INFO, "Using fetch ticket details using ticket Id to validate ticket meta data");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).param("msisdn", msisdn).contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        Response response = request.get("/cs-vas-service/v1/fetch/tunes");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(ActivateRingtone.class);
    }

    public AccumulatorsPOJO accumulatorsAPITest(String msisdn) {
        getTest().log(LogStatus.INFO, "Using fetch Ringtone details using MSISDN");
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers).body("{\"msisdn\":\""+msisdn+"\",\"pageSize\":5,\"pageNumber\":1}").contentType("application/json");
        QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
        getTest().log(LogStatus.INFO, "Request Headers are  : " + queryable.getHeaders());
        log.info("Request Headers are  : " + queryable.getHeaders());
        log.info("Request Body are  : " + queryable.getBody());
        Response response = request.post("/cs-gsm-service/v1/accumulators");
        log.info("Response : " + response.asString());
        log.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        getTest().log(LogStatus.INFO, "Response Body is  : " + response.asString());
        getTest().log(LogStatus.INFO, "Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
        return response.as(AccumulatorsPOJO.class);
    }

}