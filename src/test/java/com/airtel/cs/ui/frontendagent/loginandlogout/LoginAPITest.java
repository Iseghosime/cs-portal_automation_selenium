package com.airtel.cs.ui.frontendagent.loginandlogout;

import com.airtel.cs.common.actions.BaseActions;
import com.airtel.cs.commonutils.PassUtils;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.pojo.response.LoginPOJO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class LoginAPITest extends Driver {

    private final BaseActions actions = new BaseActions();
    ObjectMapper mapper = new ObjectMapper();

    @Test(priority = 1)
    public void testLoginAPIWithBetaUser() throws JsonProcessingException {
        selUtils.addTestcaseDescription("Validate the Login API with Beta user,Hit the Login API -/auth/api/user-mngmnt/v2/login with valid headers and credentials,Validating Success Message from response", "description");
        final String loginAUUID = constants.getValue(CommonConstants.BETA_USER_AUUID);
        LoginPOJO Req = LoginPOJO.loginBody(loginAUUID, PassUtils.decodePassword(constants.getValue(CommonConstants.BETA_USER_PASSWORD)));
        map.clear();
        pages.getLoginPage().setApiHeader();
        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Req);
        commonLib.info("Validating login api with user : " + loginAUUID);
        try {
            final Response response = pages.getLoginPage().loginAPI(dtoAsString);
            String token = "Bearer " + response.jsonPath().getString("result.accessToken");
            map.add(new Header("Authorization", token));
            commonLib.info("Response Body : " + response.asString());
            commonLib.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
            final String message = response.jsonPath().getString("message");
            assertCheck.append(actions.assertEqual_stringType(message, "User authenticated successfully", "User authenticated successfully", message));
        } catch (Exception e) {
            continueExecutionAPI = false;
            commonLib.fail("Exception in Method :- testLoginAPI " + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }

    @Test(priority = 2)
    public void testLoginApiWithAllUser() throws JsonProcessingException {
        selUtils.addTestcaseDescription("Validate the Login API with User Having all the roles,Hit the Login API -/auth/api/user-mngmnt/v2/login with valid headers and credentials,Validating Success Message from response", "description");
        final String loginAUUID = constants.getValue(CommonConstants.ALL_USER_ROLE_AUUID);
        LoginPOJO Req = LoginPOJO.loginBody(loginAUUID, PassUtils.decodePassword(constants.getValue(CommonConstants.ALL_USER_ROLE_PASSWORD)));
        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Req);
        commonLib.info("Validating login api with user : " + loginAUUID);
        try {
            final Response response = pages.getLoginPage().loginAPI(dtoAsString);
            commonLib.info("Response Body : " + response.asString());
            commonLib.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
            final String message = response.jsonPath().getString("message");
            assertCheck.append(actions.assertEqual_stringType(message, "User authenticated successfully", "User authenticated successfully", message));
        } catch (Exception e) {
            continueExecutionFA = false;
            continueExecutionBS = false;
            commonLib.fail("Exception in Method :- testLoginApiWithAdminUser " + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }

    @Test(priority = 3)
    public void testLoginApiWithBackendAgent() throws JsonProcessingException {
        selUtils.addTestcaseDescription("Validate the Login API with Backend Agent valid credentials,Hit the Login API -/auth/api/user-mngmnt/v2/login with valid headers and credentials,Validating Success Message from response", "description");
        final String loginAUUID = constants.getValue(CommonConstants.BA_USER_AUUID);
        LoginPOJO Req = LoginPOJO.loginBody(loginAUUID, PassUtils.decodePassword(constants.getValue(CommonConstants.BA_USER_PASSWORD)));
        String dtoAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(Req);
        commonLib.info("Validating login api with user : " + loginAUUID);
        try {
            final Response response = pages.getLoginPage().loginAPI(dtoAsString);
            commonLib.info("Response Body : " + response.asString());
            commonLib.info("Response time : " + response.getTimeIn(TimeUnit.SECONDS) + " s");
            final String message = response.jsonPath().getString("message");
            assertCheck.append(actions.assertEqual_stringType(message, "User authenticated successfully", "User authenticated successfully", message));
        } catch (Exception e) {
            continueExecutionBA = false;
            commonLib.fail("Exception in Method :- testLoginApiWithBackendAgent " + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }
}
