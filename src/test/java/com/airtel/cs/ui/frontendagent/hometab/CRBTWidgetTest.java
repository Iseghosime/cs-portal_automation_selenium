package com.airtel.cs.ui.frontendagent.hometab;

import com.airtel.cs.api.RequestSource;
import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.dataproviders.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.databeans.HeaderDataBean;
import com.airtel.cs.driver.Driver;
import com.airtel.cs.model.cs.response.crbt.ActivateRingtone;
import com.airtel.cs.model.cs.response.crbt.Top20Ringtone;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Objects;

public class CRBTWidgetTest extends Driver {

    private static String customerNumber = null;
    RequestSource api = new RequestSource();

    @BeforeMethod(groups = {"SanityTest", "RegressionTest", "ProdTest"})
    public void isCRBTFeatureEnabled() {
        if (StringUtils.equalsIgnoreCase(constants.getValue(ApplicationConstants.CRBT_WIDGET), "false")) {
            commonLib.skip("CRBT Widget is NOT Enabled for this Opco=" + OPCO);
            throw new SkipException("CRBT Widget is NOT Enabled for this Opco=" + OPCO);
        }
    }

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
            selUtils.addTestcaseDescription("Open Customer Profile Page with valid MSISDN, Validate Customer Profile Page Loaded or not", "description");
            customerNumber = constants.getValue(ApplicationConstants.CUSTOMER_MSISDN);
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

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = "openCustomerInteraction")
    public void testHeaderAndAuuid() {
        try {
            selUtils.addTestcaseDescription("Validate is CRBT Widget Visible,Validate is CRBT Widget Loaded?,Validate Footer and Middle Auuid", "description");
            String crbtWidgetId = pages.getCrbtWidgetPage().getCRBTWidgetId();
            assertCheck.append(actions.assertEqualBoolean(widgetMethods.isWidgetVisible(crbtWidgetId), true, "CRBT Widget is visible", "CRBT Widget is not visible"));
            assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getFooterAuuidCRBT(), loginAUUID, "Auuid shown at the footer of the CRBT widget and is correct", "Auuid NOT shown at the footer of CRBT widget"));
            assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getMiddleAuuidCRBT(), loginAUUID, "Auuid shown at the middle of the CRBT widget and is correct", "Auuid NOT shown at the middle of CRBT widget"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - testHeaderAndAuuid" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 3, groups = {"SanityTest", "RegressionTest", "ProdTest"}, dependsOnMethods = "openCustomerInteraction")
    public void validateMyTuneTab() {
        try {
            selUtils.addTestcaseDescription("Validate My Tunes tab under CRBT Widget", "description");
            ActivateRingtone ringtoneAPI = api.activateRingtone(customerNumber);
            final String statusCode = ringtoneAPI.getStatusCode();
            assertCheck.append(actions.assertEqualStringType(statusCode, "200", "Ringtone API status code matched and is :" + statusCode, "Ringtone API status code NOT matched and is :" + statusCode, false));
            String message = ringtoneAPI.getResult().getMessage();
            if (Integer.parseInt(statusCode) == 200) {
                if (Objects.isNull(ringtoneAPI.getApiErrors())) {
                    if (Objects.nonNull(ringtoneAPI.getResult())) {
                        if (StringUtils.equalsIgnoreCase(message,"Customer haven't subscribed for hello tunes"))
                            assertCheck.append(actions.assertEqualStringType(message, "Customer haven't subscribed for hello tunes", "Hello Tune Not Subscribed message matched successfully and is :" + message, "Hello Tune Not Subscribed message NOT matched and is :" + message));
                        if (StringUtils.equalsIgnoreCase(message,"Subscriber activation is pending."))
                            assertCheck.append(actions.assertEqualStringType(message, "Subscriber activation is pending.", "Subscriber activation pending message matched successfully and is :" + message, "Subscriber activation pending message NOT Matched and is :" + message));
                        //ToDO Pending as Test msisdn is not available
                    }
                } else {
                    final String actual = pages.getCrbtWidgetPage().noResultMessage();
                    assertCheck.append(actions.assertEqualStringType(actual, "Customer haven't subscribed for hello tunes.", "Hello Tune Not Subscribed Message Matched Successfully and is :" + actual, "Hello Tune Not Subscribed Message NOT Matched and is :" + actual));
                }
            } else {
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultMessage(), true, "Error Message is Visible", "Error Message is not Visible"));
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultImg(), true, "Error Message is as expected", "Error Message is not as expected"));
            }
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (NoSuchElementException | TimeoutException e) {
            commonLib.fail("Exception in Method - validateMyTuneTab" + e.fillInStackTrace(), true);
        }
    }

    @DataProviders.Table(name = "Search Tune Tab")
    @Test(priority = 4, groups = {"SanityTest", "RegressionTest"}, dataProvider = "HeaderData", dataProviderClass = DataProviders.class, dependsOnMethods = "openCustomerInteraction")
    public void testTop20Tunes(HeaderDataBean data) {
        try {
            selUtils.addTestcaseDescription("Validate top 20 tunes tab, Validate top 20 tune CS API,", "description");
            pages.getCrbtWidgetPage().clickTop20Tab();
            Top20Ringtone top20Tune = api.ringtoneDetailTest(customerNumber, "topTwenty", null);
            if (!top20Tune.getStatusCode().equalsIgnoreCase("200")) {
                commonLib.fail("com.airtel.cs.API Response " + top20Tune.getMessage(), true);
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isWidgetError(), true, "Widget Error displayed as api response is not 200", "Widget Error does not display as api response is not 200."));
            } else if (top20Tune.getTotalCount() > 0) {
                for (int i = 0; i < data.getHeaderName().size(); i++) {
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getCrbtWidgetPage().getTop20Header(i + 1), data.getHeaderName().get(i), "Header Name for Search tab is On Column " + (i + 1) + " is as expected", "Header Name for Search tab is On Column " + (i + 1) + " is not as expected"));
                }
                int size = Math.min(top20Tune.getTotalCount(), 5);
                for (int i = 0; i < size; i++) {
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueTop20(i + 1, 1).trim().toLowerCase(), (top20Tune.getResult().get(i).getName() + top20Tune.getResult().get(i).getSinger()).toLowerCase().trim(), "Name is same as expected in CS API response", "Name is not same as expected in CS API response"));
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueTop20(i + 1, 2).trim(), top20Tune.getResult().get(i).getRenewalAmount().trim(), "Renewal Amount is same as expected in CS API response", "Renewal Amount is not same as expected in CS API response"));
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueTop20(i + 1, 3).trim(), top20Tune.getResult().get(i).getPeriod().trim(), "Period is same as expected in CS API response", "Period is not same as expected in CS API response"));
                }
            } else {
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultMessage(), true, "No Result message found in case of no ringtone found with search keyword", "No Result message does not in case of no ringtone found with search keyword"));
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultImg(), true, "No Result Image found in case of no ringtone found with search keyword", "No Result Image does not in case of no ringtone found with search keyword"));
            }
        } catch (NoSuchElementException | TimeoutException e) {
            commonLib.fail("Exception in Method - testTop20Tunes" + e.fillInStackTrace(), true);
        }
    }

    @DataProviders.Table(name = "Search Tune Tab")
    @Test(priority = 5, groups = {"SanityTest", "RegressionTest"}, dataProvider = "HeaderData", dataProviderClass = DataProviders.class, dependsOnMethods = "openCustomerInteraction")
    public void testSearchTune(HeaderDataBean data) {
        try {
            selUtils.addTestcaseDescription("Validate Search Tune Tab, Validate Search With Keyword and result, Validate Search By Option,Validate Headers and there values after search", "description");
            pages.getCrbtWidgetPage().clickSearchTuneTab();
            pages.getCrbtWidgetPage().searchKeyword("h");
            pages.getCrbtWidgetPage().clickSearchBtn();
            pages.getCrbtWidgetPage().clickSearchByOption();
            final String option1 = pages.getCrbtWidgetPage().getOption1();
            assertCheck.append(actions.assertEqualStringType(option1, constants.getValue(CommonConstants.SEARCH_BY_NAME), "Search Option is same as expected and is :" + option1, "Search By option does not same as expectedand is :" + option1));
            final String option2 = pages.getCrbtWidgetPage().getOption2();
            assertCheck.append(actions.assertEqualStringType(option2, constants.getValue(CommonConstants.SEARCH_BY_TITLE), "Search By Title same as expected and is :" + option2, "Search By option does not same as expectedand is :" + option2));
            pages.getCrbtWidgetPage().clickOutside();
            Top20Ringtone searchTune = api.ringtoneDetailTest(customerNumber, "namedTune", "h");
            final String statusCode = searchTune.getStatusCode();
            assertCheck.append(actions.assertEqualStringType(statusCode, "200", "Ringtone Detail API success and status code is :" + statusCode, "Ringtone Detail API is NOT success and status code is :" + statusCode, false));
            if (!StringUtils.equalsIgnoreCase(statusCode, "200")) {
                commonLib.fail("API Response " + searchTune.getMessage(), true);
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isWidgetError(), true, "Widget Error displayed as API response is not 200", "Widget Error does not display as api response is not 200."));
            } else if (searchTune.getTotalCount() > 0) {
                for (int i = 0; i < data.getHeaderName().size()-1; i++) {
                    assertCheck.append(actions.matchUiAndAPIResponse(pages.getCrbtWidgetPage().getSearchHeader(i + 1), data.getHeaderName().get(i), "Header Name for Search tab is On Column " + (i + 1) + " is as expected", "Header Name for Search tab is On Column " + (i + 1) + " is not as expected"));
                }
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isActionTabVisible(), true, "Action tab is visible in the header", "Action tab is visible in the header "));
                int size = Math.min(searchTune.getTotalCount(), 5);
                for (int i = 0; i < size; i++) {
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueSearch(i + 1, 1).replaceAll("\n", " "), (searchTune.getResult().get(i).getName() + " " + searchTune.getResult().get(i).getSinger()), "Name is same as expected in CS API response", "Name is not same as expected in CS API response"));
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueSearch(i + 1, 2).replaceAll("\n", " "), searchTune.getResult().get(i).getRenewalAmount().trim(), "Renewal Amount is same as expected CS API response", "Renewal Amount is not same as expected in CS API response"));
                    assertCheck.append(actions.assertEqualStringType(pages.getCrbtWidgetPage().getValueSearch(i + 1, 3).replaceAll("\n", " "), searchTune.getResult().get(i).getPeriod().trim(), "Period is same as expected CS API response", "Period is not same as expected in CS API response"));
                }
            } else {
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultMessage(), true, "No Result message found is case of no ringtone found with search keyword", "No Result message does not in case of no ringtone found with search keyword"));
                assertCheck.append(actions.assertEqualBoolean(pages.getCrbtWidgetPage().isNoResultImg(), true, "No Result Image found is case of no ringtone found with search keyword", "No Result Image does not in case of no ringtone found with search keyword"));
            }
        } catch (Exception e) {
            commonLib.fail("Exception in Method - testSearchTune" + e.fillInStackTrace(), true);
        }
    }
}
