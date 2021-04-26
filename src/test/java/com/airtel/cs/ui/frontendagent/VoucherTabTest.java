package com.airtel.cs.ui.frontendagent;

import com.airtel.cs.api.APIEndPoints;
import com.airtel.cs.commonutils.PassUtils;
import com.airtel.cs.commonutils.UtilsMethods;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.TestDatabean;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.LoginPOJO;
import com.airtel.cs.pojo.voucher.VoucherDetail;
import com.airtel.cs.pojo.voucher.VoucherSearchPOJO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class VoucherTabTest extends Driver {

    APIEndPoints api = new APIEndPoints();

    @BeforeMethod
    public void checkExecution() {
        SoftAssert softAssert = new SoftAssert();
        if (!continueExecutionFA || !continueExecutionAPI) {
            softAssert.fail("Terminate Execution as user not able to login into portal or Role does not assign to user. Please do needful.");
        }
        softAssert.assertAll();
    }

    @DataProviders.User(userType = "API")
    @Test(dataProvider = "loginData", dataProviderClass = DataProviders.class, priority = 0)
    public void loginAPI(TestDatabean data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SoftAssert softAssert = new SoftAssert();
        final String loginAUUID = data.getLoginAUUID();
        LoginPOJO req = LoginPOJO.loginBody(PassUtils.decodePassword(data.getPassword()), loginAUUID);

        map.clear();
        UtilsMethods.addHeaders("x-app-name", config.getProperty(evnName + "-x-app-name"));
        UtilsMethods.addHeaders("x-service-id", config.getProperty(evnName + "-x-service-id"));
        //map.add(new Header("x-bsy-bn", config.getProperty(Env + "-x-bsy-bn"))); //Comment this line this header removed from MG Opco.
        UtilsMethods.addHeaders("x-app-type", config.getProperty(evnName + "-x-app-type"));
        UtilsMethods.addHeaders("x-client-id", config.getProperty(evnName + "-x-client-id"));
        UtilsMethods.addHeaders("x-api-key", config.getProperty(evnName + "-x-api-key"));
        UtilsMethods.addHeaders("x-login-module", config.getProperty(evnName + "-x-login-module"));
        UtilsMethods.addHeaders("x-channel", config.getProperty(evnName + "-x-channel"));
        UtilsMethods.addHeaders("x-app-version", config.getProperty(evnName + "-x-app-version"));
        UtilsMethods.addHeaders("Opco", OPCO);

        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
        selUtils.addTestcaseDescription("LOGIN API TEST,Logging in Using Login com.airtel.cs.API for getting TOKEN with user : " + loginAUUID, "description");
        commonLib.info("Logging in Using Login com.airtel.cs.API for getting TOKEN with user : " + loginAUUID);
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body(dtoAsString)
                .contentType("application/json");
        try {
            QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
            commonLib.info("Request Headers are  : " + queryable.getHeaders());
            Response response = request.post("/auth/api/user-mngmnt/v2/login");
            String token = "Bearer " + response.jsonPath().getString("result.accessToken");
            map.add(new Header("Authorization", token));
            commonLib.info("Request URL : " + queryable.getURI());
            commonLib.info("Response Body : " + response.asString());
            commonLib.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
            if (response.jsonPath().getString("message").equalsIgnoreCase("Failed to authenticate user.")) {
                continueExecutionAPI = false;
                softAssert.fail("Not able to generate Token. Please Update Password As soon as possible if required.\ncom.airtel.cs.API Response Message: " + response.jsonPath().getString("message"));
            } else if (response.jsonPath().getString("message").toLowerCase().contains("something went wrong")) {
                continueExecutionAPI = false;
                softAssert.fail("Not able to generate Token. Login com.airtel.cs.API Failed(Marked Build As Failed).\ncom.airtel.cs.API Response Message: " + response.jsonPath().getString("message"));
            } else if (!response.jsonPath().getString("message").equalsIgnoreCase("User authenticated successfully")) {
                continueExecutionAPI = false;
                softAssert.fail("Not able to generate Token. Please Check the com.airtel.cs.API error Message and make changes if required.\ncom.airtel.cs.API Response Message: " + response.jsonPath().getString("message"));
            }
        } catch (Exception e) {
            continueExecutionAPI = false;
            softAssert.fail("Connectivity issue occurred, Not able to connect with server : " + e.fillInStackTrace());
        }
        softAssert.assertAll();
    }

    @DataProviders.User(userType = "NFTR")
    @Test(priority = 1, description = "Validate Customer Interaction Page", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void openCustomerInteraction(TestDatabean data) {
        final String customerNumber = data.getCustomerNumber();
        selUtils.addTestcaseDescription("Validating the Search for Customer Interactions :" + customerNumber, "description");
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenuPage().clickOnSideMenu();
        pages.getSideMenuPage().clickOnName();
        pages.getSideMenuPage().openCustomerInteractionPage();
        pages.getMsisdnSearchPage().enterNumber(customerNumber);
        pages.getMsisdnSearchPage().clickOnSearch();
        softAssert.assertTrue(pages.getCustomerProfilePage().isPageLoaded());
        pages.getCustomerProfilePage().waitTillLoaderGetsRemoved();
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Validate Voucher Search Test", dependsOnMethods = "openCustomerInteraction")
    public void voucherSearchTest() throws InterruptedException {
        selUtils.addTestcaseDescription("Validate Voucher Search Test", "description");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        String voucherId = data.getVoucherId();
        if (voucherId != null && !voucherId.equals(" ")) {
            pages.getRechargeHistoryWidget().writeVoucherId(voucherId);
            pages.getRechargeHistoryWidget().clickSearchBtn();
            try {
                pages.getVoucherTab().waitTillTimeLineGetsRemoved();
                Assert.assertTrue(pages.getVoucherTab().isVoucherTabOpen(), "Voucher Id does not found");
                VoucherSearchPOJO voucher = api.voucherSearchTest(voucherId);
                VoucherDetail voucherDetail = voucher.getResult();
                if (voucher.getStatusCode() == 200) {
                    softAssert.assertEquals(pages.getVoucherTab().getSerialValue(), voucherDetail.getVoucherId(), "Voucher Serial number is not same as search voucher id");
                    softAssert.assertEquals(pages.getVoucherTab().getStatusValue().toLowerCase().trim(), voucherDetail.getStatus().toLowerCase().trim(), "Voucher Status is not same as voucher status received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getSubStatus().toLowerCase().trim(), voucherDetail.getSubStatus().toLowerCase().trim(), "Voucher Sub Status is not same as voucher Sub Status received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getRechargeAmt().toLowerCase().trim(), voucherDetail.getRechargeAmount().toLowerCase().trim(), "Voucher Recharge amount is not same as voucher Recharge amount received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getTimeStamp().toLowerCase().trim(), voucherDetail.getTimestamp().toLowerCase().trim(), "Voucher Time Stamp is not same as voucher Time Stamp received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getExpiryDate().toLowerCase().trim(), voucherDetail.getExpiryDate().toLowerCase().trim(), "Voucher Expiry date is not same as voucher Expiry date received by api");
                    if (voucherDetail.getSubscriberId() != null)
                        softAssert.assertEquals(pages.getVoucherTab().getSubscriberId().toLowerCase().trim(), voucherDetail.getSubscriberId().toLowerCase().trim(), "Voucher Subscriber Id is not same as voucher Subscriber Id received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getAgent().toLowerCase().trim(), voucherDetail.getAgent().toLowerCase().trim(), "Voucher Agent not same as voucher Agent received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getBatchID().toLowerCase().trim(), voucherDetail.getBatchId().toLowerCase().trim(), "Voucher Batch Id not same as voucher Batch Id received by api");
                    softAssert.assertEquals(pages.getVoucherTab().getVoucherGroup().toLowerCase().trim(), voucherDetail.getVoucherGroup().toLowerCase().trim(), "Voucher group not same as voucher group received by api");
                    pages.getVoucherTab().clickDoneBtn();
                } else {
                    softAssert.fail("com.airtel.cs.API Response is not 200.");
                }
            } catch (TimeoutException | NoSuchElementException e) {
                Thread.sleep(500);
                pages.getVoucherTab().clickDoneBtn();
                softAssert.fail("Not able to validate Voucher tab: " + e.getMessage());
            }
        } else {
            softAssert.fail("Voucher Id does not found in config sheet. Please add voucher in sheet.");
        }
        softAssert.assertAll();
    }
}