package com.airtel.cs.ui.backendSupervisor;

import com.airtel.cs.commonutils.actions.BaseActions;
import com.airtel.cs.commonutils.UtilsMethods;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.applicationutils.constants.PermissionConstants;
import com.airtel.cs.commonutils.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.databeans.TransferQueueDataBean;
import com.airtel.cs.driver.Driver;
import io.restassured.http.Headers;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransferToQueueTest extends Driver {

    private final BaseActions actions = new BaseActions();

    @BeforeMethod(groups = {"SanityTest", "RegressionTest"})
    public void checkExecution() {
        if (!(continueExecutionBS && continueExecutionFA)) {
            commonLib.skip("Skipping tests because user NOT able to login Over Portal");
            throw new SkipException("Skipping tests because user NOT able to login Over Portal");
        }
    }

    @Test(priority = 1, groups = {"SanityTest", "RegressionTest"})
    public void openSupervisorDashboard() {
        try {
            selUtils.addTestcaseDescription("Open Supervisor Dashboard , Validate agent redirect to ticket List Page", "description");
            pages.getSideMenuPage().clickOnSideMenu();
            pages.getSideMenuPage().clickOnUserName();
            pages.getSideMenuPage().openSupervisorDashboard();
            assertCheck.append(actions.assertEqualStringType(driver.getTitle(), constants.getValue(CommonConstants.SUPERVISOR_TICKET_LIST_PAGE_TITLE), "Agent redirect to ticket list page as expected", "Agent does not redirect to ticket list page as expected"));
        } catch (Exception e) {
            commonLib.fail("Exception in Method - openSupervisorDashboard" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }

    @Test(priority = 2, groups = {"SanityTest", "RegressionTest"}, dependsOnMethods = "openSupervisorDashboard")
    public void isUserHasWorkFlowOverRidePermission() {
        try {
            selUtils.addTestcaseDescription("Verify that Service Profile widget should be visible to the logged in agent if HLR permission is enabled in UM, Check User has permission to view HLR Widget Permission", "description");
            String workflow_override = constants.getValue(PermissionConstants.WORKFLOW_OVERRIDE_PERMISSION);
            assertCheck.append(actions.assertEqualBoolean(UtilsMethods.isUserHasPermission(new Headers(map), workflow_override), true, "Agent has permission of ticket workflow override as expected", "Agent does not have permission of ticket workflow override as expected"));
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - isUserHasHLRPermission" + e.fillInStackTrace(), true);
        }
    }

    @Test(priority = 3, dataProvider = "TransferQueue", groups = {"SanityTest", "RegressionTest"}, dataProviderClass = DataProviders.class, dependsOnMethods = "openSupervisorDashboard")
    public void transferToQueue(TransferQueueDataBean data) {
        try {
            selUtils.addTestcaseDescription("Backend Supervisor Transfer to queue,Apply filter with queue name " + data.getFromQueue() +
                    ",Validate filter applied correctly,Select a ticket and click on transfer to queue button,validate transfer to queue tab display correctly," +
                    "Click on transfer to queue button with queue name " + data.getToQueue() + ",Validate ticket transfer to new queue successfully. If Transfer anyway flag is true in config sheet then click on transfer anyway otherwise ticket must transfer without fail.", "description");
            String ticketId = null;
            try {
                pages.getSupervisorTicketList().clickFilter();
                pages.getFilterTabPage().scrollToQueueFilter();
                pages.getFilterTabPage().clickQueueFilter();
                pages.getFilterTabPage().selectQueueByName(data.getFromQueue());
                pages.getFilterTabPage().clickOutsideFilter();
                pages.getFilterTabPage().clickApplyFilter();
                Assert.assertEquals(pages.getSupervisorTicketList().getQueueValue().trim().toLowerCase(), data.getFromQueue().toLowerCase().trim(), "Ticket Does not found with Selected State");
                assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().validateQueueFilter(data.getFromQueue()), true, "Queue Filter Does Applied Correctly", "Queue Filter does not applied correctly"));
                try {
                    ticketId = pages.getSupervisorTicketList().getTicketIdValue();
                    pages.getSupervisorTicketList().resetFilter();
                    pages.getSupervisorTicketList().writeTicketId(ticketId);
                    pages.getSupervisorTicketList().clickSearchBtn();
                    pages.getSupervisorTicketList().clickCheckbox();
                    try {
                        assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isAssignToAgent(), true, "Assign to Agent Button Does Available after selecting ticket", "Assign to Agent Button Does Not Available after selecting ticket"));
                        assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isTransferToQueue(), true, "Transfer to Queue Button Does Available after selecting ticket", "Transfer to Queue Button Does Not Available after selecting ticket"));
                        pages.getSupervisorTicketList().clickTransfertoQueue();
                        assertCheck.append(actions.assertEqualBoolean(pages.getTransferToQueue().validatePageTitle(), true, "Transfer to Queue Pop up open as expected", "Transfer to Queue Page Title Does not Display"));
                        pages.getTransferToQueue().clickTransferQueue(data.getToQueue());
                    } catch (NoSuchElementException | TimeoutException e) {
                        commonLib.fail("Not able to perform Transfer to Queue: " + e.fillInStackTrace(), true);
                        pages.getTransferToQueue().clickCloseTab();
                    }
                } catch (NoSuchElementException | TimeoutException e) {
                    commonLib.warning("No Ticket Found in Selected Queue to perform transfer to queue action" + e.fillInStackTrace(), true);
                }
                try {
                    Assert.assertEquals(pages.getSupervisorTicketList().getQueueValue().toLowerCase().trim(), data.getToQueue().toLowerCase().trim(), "Ticket Does not Transfer to Selected Queue");
                    pages.getSupervisorTicketList().clearInputBox();
                } catch (AssertionError f) {
                    f.printStackTrace();
                    commonLib.info("Not able to perform transfer to Queue action: " + pages.getSupervisorTicketList().getTransferErrorMessage());
                    assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isCancelBtn(), true, "Cancel Button display as expected", "Cancel Button does not display."));
                    if (data.getTransferAnyway().equalsIgnoreCase("true")) {
                        assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isTransferAnyWayBtn(), true, "Transfer Anyway button does not display as expected", "Transfer Anyway button does not displayed."));
                        try {
                            pages.getSupervisorTicketList().clickTransferAnyWayBtn();
                            pages.getSupervisorTicketList().writeTicketId(ticketId);
                            pages.getSupervisorTicketList().clickSearchBtn();
                            Assert.assertEquals(pages.getSupervisorTicketList().getQueueValue().toLowerCase().trim(), data.getToQueue().toLowerCase().trim(), "Ticket Does not Transfer to Selected Queue");
                        } catch (NoSuchElementException | TimeoutException g) {
                            commonLib.fail("Transfer Anyway does not display in case of ticket does not transfer to selected queue.", true);
                            pages.getSupervisorTicketList().clickCancelBtn();
                        }
                    } else {
                        pages.getSupervisorTicketList().clickCancelBtn();
                        commonLib.fail("Transfer to queue does not Perform as per config sheet both queue belong to same workgroup.", false);
                    }
                }
            } catch (InterruptedException | NoSuchElementException | TimeoutException | ElementClickInterceptedException e) {
                pages.getFilterTabPage().clickOutsideFilter();
                pages.getFilterTabPage().clickCloseFilter();
                commonLib.fail("Not able to apply filter " + e.getMessage(), true);
            }
            pages.getSupervisorTicketList().clearInputBox();
            actions.assertAllFoundFailedAssert(assertCheck);
        } catch (Exception e) {
            commonLib.fail("Exception in Method - transferToQueue" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }
}
