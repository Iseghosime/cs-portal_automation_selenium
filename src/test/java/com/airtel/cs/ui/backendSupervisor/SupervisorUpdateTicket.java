package com.airtel.cs.ui.backendSupervisor;

import com.airtel.cs.api.APIEndPoints;
import com.airtel.cs.commonutils.PassUtils;
import com.airtel.cs.commonutils.UtilsMethods;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.NftrDataBeans;
import com.airtel.cs.commonutils.dataproviders.TestDatabean;
import com.airtel.cs.commonutils.extentreports.ExtentTestManager;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.LoginPOJO;
import com.airtel.cs.pojo.smshistory.SMSHistoryList;
import com.airtel.cs.pojo.smshistory.SMSHistoryPOJO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static com.airtel.cs.commonutils.extentreports.ExtentTestManager.startTest;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class SupervisorUpdateTicket extends Driver {

    static String ticketId=null;
    String customerNumber = null;
    APIEndPoints api = new APIEndPoints();

    @BeforeMethod
    public void checkExecution() {
        SoftAssert softAssert = new SoftAssert();
        if (!continueExecutionBS | !continueExecutionAPI) {
            softAssert.fail("Terminate Execution as Backend Supervisor user not able to login into portal or Role does not assign to user. Please do needful.");
        }
        softAssert.assertAll();
    }

    @DataProviders.User(UserType = "API")
    @Test(dataProvider = "loginData", dataProviderClass = DataProviders.class, priority = 0)
    public void loginAPI(TestDatabean data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SoftAssert softAssert = new SoftAssert();
        LoginPOJO Req = LoginPOJO.loginBody(PassUtils.decodePassword(data.getPassword()), data.getLoginAUUID());

        map.clear();
        UtilsMethods.addHeaders("x-app-name", config.getProperty(Env + "-x-app-name"));
        UtilsMethods.addHeaders("x-service-id", config.getProperty(Env + "-x-service-id"));
        //map.add(new Header("x-bsy-bn", config.getProperty(Env + "-x-bsy-bn"))); //Comment this line this header removed from MG Opco.
        UtilsMethods.addHeaders("x-app-type", config.getProperty(Env + "-x-app-type"));
        UtilsMethods.addHeaders("x-client-id", config.getProperty(Env + "-x-client-id"));
        UtilsMethods.addHeaders("x-api-key", config.getProperty(Env + "-x-api-key"));
        UtilsMethods.addHeaders("x-login-module", config.getProperty(Env + "-x-login-module"));
        UtilsMethods.addHeaders("x-channel", config.getProperty(Env + "-x-channel"));
        UtilsMethods.addHeaders("x-app-version", config.getProperty(Env + "-x-app-version"));
        UtilsMethods.addHeaders("Opco", OPCO);

        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Req);
        startTest("LOGIN com.airtel.cs.API TEST ", "Logging in Using Login com.airtel.cs.API for getting TOKEN with user : " + data.getLoginAUUID());
        UtilsMethods.printInfoLog("Logging in Using Login com.airtel.cs.API for getting TOKEN with user : " + data.getLoginAUUID());
        baseURI = baseUrl;
        Headers headers = new Headers(map);
        RequestSpecification request = given()
                .headers(headers)
                .body(dtoAsString)
                .contentType("application/json");
        try {
            QueryableRequestSpecification queryable = SpecificationQuerier.query(request);
            UtilsMethods.printInfoLog("Request Headers are  : " + queryable.getHeaders());
            Response response = request.post("/auth/api/user-mngmnt/v2/login");
            String token = "Bearer " + response.jsonPath().getString("result.accessToken");
            map.add(new Header("Authorization", token));
            UtilsMethods.printInfoLog("Request URL : " + queryable.getURI());
            UtilsMethods.printInfoLog("Response Body : " + response.asString());
            UtilsMethods.printInfoLog("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
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

    @DataProviders.User(UserType = "NFTR")
    @Test(priority = 1, dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void setCustomerNumber(TestDatabean Data) {
        customerNumber = Data.getCustomerNumber();
    }

    @Test(priority = 2, description = "Open Supervisor Dashboard")
    public void openSupervisorDashboard() {
        ExtentTestManager.startTest("Open Supervisor Dashboard", "Open Supervisor Dashboard");
        pages.getSideMenu().clickOnSideMenu();
        pages.getSideMenu().clickOnName();
        pages.getSideMenu().openSupervisorDashboard();
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenu().waitTillLoaderGetsRemoved();
        Assert.assertEquals(driver.getTitle(), config.getProperty("supervisorTicketListPage"));
        softAssert.assertAll();
    }

    @Test(priority = 3, description = "Update Ticket", dataProvider = "ticketId", dataProviderClass = DataProviders.class)
    public void updateTicket(Method method, NftrDataBeans Data) throws InterruptedException {
        ExtentTestManager.startTest("Update Ticket: " + Data.getIssueCode(), "Update Ticket");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening URL");
        SoftAssert softAssert = new SoftAssert();
        DataProviders data = new DataProviders();
        String selectedState = null;
        try {
            pages.getSupervisorTicketList().writeTicketId(Data.getTicketNumber());
            pages.getSupervisorTicketList().clickSearchBtn();
            pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
            Assert.assertEquals(pages.getSupervisorTicketList().getTicketIdvalue(), Data.getTicketNumber(), "Search Ticket does not found");
            try {
                pages.getSupervisorTicketList().viewTicket();
                pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
                try {
                    selectedState = pages.getViewTicket().selectState(data.ticketStateClosed());
                    if (selectedState.equalsIgnoreCase(data.ticketStateClosed())) {
                        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
                        pages.getSupervisorTicketList().changeTicketTypeToClosed();
                        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
                        pages.getSupervisorTicketList().writeTicketId(Data.getTicketNumber());
                        pages.getSupervisorTicketList().clickSearchBtn();
                        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
                        softAssert.assertEquals(pages.getSupervisorTicketList().getTicketIdvalue(), Data.getTicketNumber(), "Search Ticket Does not Fetched Correctly");
                        Assert.assertEquals(pages.getSupervisorTicketList().getStatevalue(), selectedState, "Ticket Does not Updated to Selected State");
                        if (ticketId == null) {
                            ticketId = Data.getTicketNumber();
                        }
                        SMSHistoryPOJO smsHistory = api.smsHistoryTest(customerNumber);
                        SMSHistoryList list = smsHistory.getResult().get(0);
                        ExtentTestManager.getTest().log(LogStatus.INFO, "Message Sent after closure: " + list.getMessageText());
                        softAssert.assertTrue(list.getMessageText().contains(Data.getTicketNumber()), "Message Sent does not send for same ticket id which has been closed");
                        softAssert.assertEquals(list.getSmsType().toLowerCase().trim(), config.getProperty("systemSMSType").toLowerCase().trim(), "Message type is not system");
                        softAssert.assertFalse(list.isAction(), "Action button is not disabled");
                        softAssert.assertEquals(list.getTemplateName().toLowerCase().trim(), config.getProperty("ticketCreateEvent").toLowerCase().trim(), "Template event not same as defined.");
                    } else {
                        pages.getViewTicket().clickBackButton();
                    }
                } catch (TimeoutException | NoSuchElementException | ElementClickInterceptedException e) {
                    softAssert.fail("Update Ticket does not complete due to error :" + e.fillInStackTrace());
                    pages.getViewTicket().clickBackButton();
                }
            } catch (TimeoutException | NoSuchElementException | AssertionError e) {
                e.printStackTrace();
                softAssert.fail("Update Ticket does not complete due to error :" + e.fillInStackTrace());
            }
        } catch (TimeoutException | NoSuchElementException | AssertionError e) {
            e.printStackTrace();
            softAssert.fail("Ticket id search not done due to following error: " + e.getMessage());
        }
        pages.getSupervisorTicketList().clearInputBox();
        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
        pages.getSupervisorTicketList().changeTicketTypeToOpen();
        pages.getSupervisorTicketList().waitTillLoaderGetsRemoved();
        softAssert.assertAll();
    }

    @DataProviders.User(UserType = "NFTR")
    @Test(priority = 4, description = "Validate Customer Interaction Page", dataProvider = "loginData", dataProviderClass = DataProviders.class)
    public void openCustomerInteraction(Method method, TestDatabean Data) throws IOException {
        ExtentTestManager.startTest("Validating the Search forCustomer Interactions :" + Data.getCustomerNumber(), "Validating the Customer Interaction Search Page By Searching Customer number : " + Data.getCustomerNumber());
        SoftAssert softAssert = new SoftAssert();
        pages.getSideMenu().clickOnSideMenu();
        pages.getSideMenu().clickOnName();
        if (ticketId != null) {
            pages.getSideMenu().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(Data.getCustomerNumber());
            pages.getMsisdnSearchPage().clickOnSearch();
            softAssert.assertTrue(pages.getCustomerProfilePage().isPageLoaded());
        } else {
            UtilsMethods.printWarningLog("No Ticket Id Closed. SKIP Validate Re-open Icon on Closed Ticket");
        }
        softAssert.assertAll();
    }

    @Test(priority = 5, dependsOnMethods = "openCustomerInteraction", description = "Validate Re-open Icon on Closed Ticket")
    public void validateReopenIcon() throws InterruptedException, IOException {
        SoftAssert softAssert = new SoftAssert();
        if (ticketId != null) {
            ExtentTestManager.startTest("Validate Re-open Icon on Closed Ticket: " + ticketId, "Validate Re-open Icon on Closed Ticket: " + ticketId);
            pages.getCustomerProfilePage().clickOnViewHistory();
            pages.getViewHistory().clickOnTicketHistory();
            pages.getFrontendTicketHistoryPage().waitTillLoaderGetsRemoved();
            pages.getFrontendTicketHistoryPage().writeTicketId(ticketId);
            pages.getFrontendTicketHistoryPage().clickSearchBtn();
            pages.getFrontendTicketHistoryPage().waitTillLoaderGetsRemoved();
            Thread.sleep(3000);
            softAssert.assertEquals(pages.getFrontendTicketHistoryPage().getTicketId(1), ticketId, "Ticket Id does not same as search ticket id.");
            pages.getFrontendTicketHistoryPage().getTicketState(1);
            softAssert.assertTrue(pages.getFrontendTicketHistoryPage().checkReopen(1), "Reopen icon does not found on ticket");
        } else {
            UtilsMethods.printWarningLog("No Ticket Id Closed. SKIP Validate Re-open Icon on Closed Ticket");
        }
        softAssert.assertAll();
    }
}
