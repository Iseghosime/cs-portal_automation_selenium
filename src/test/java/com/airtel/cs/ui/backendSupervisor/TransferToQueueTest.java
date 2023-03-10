package com.airtel.cs.ui.backendSupervisor;

import com.airtel.cs.commonutils.utils.UtilsMethods;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstants;
import com.airtel.cs.commonutils.applicationutils.constants.PermissionConstants;
import com.airtel.cs.commonutils.dataproviders.dataproviders.DataProviders;
import com.airtel.cs.commonutils.dataproviders.databeans.TransferQueueDataBean;
import com.airtel.cs.driver.Driver;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TransferToQueueTest extends Driver {


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
            assertCheck.append(actions.assertEqualBoolean(UtilsMethods.isUserHasPermission(workflow_override), true, "Agent has permission of ticket workflow override as expected", "Agent does not have permission of ticket workflow override as expected"));
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
                assertCheck.append(actions.assertEqualStringType(pages.getSupervisorTicketList().getQueueValue().trim().toLowerCase(), data.getFromQueue().toLowerCase().trim(), "Ticket Does found with Selected State", "Ticket Does not found with Selected State", true, true));
                assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().validateQueueFilter(data.getFromQueue()), true, "Queue Filter Does Applied Correctly", "Queue Filter does not applied correctly"));
                assertCheck.append(actions.assertEqualStringNotNull(pages.getSupervisorTicketList().getTicketIdValue(), "Ticket Id Found", "No Ticket id found"));
                ticketId = pages.getSupervisorTicketList().getTicketIdValue();
                pages.getSupervisorTicketList().resetFilter();
                pages.getSupervisorTicketList().searchTicket(ticketId);
                pages.getSupervisorTicketList().clickCheckbox();
                assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isTicketTransferActionPerformed(data.getToQueue()), true, "Ticket transferred action performed successfully ", "Ticket transferred action not performed"));
                if (pages.getSupervisorTicketList().isTransferErrorMessageVisible()) {
                    commonLib.info("Not able to transfer ticket to selected queue . Getting error message : " + pages.getSupervisorTicketList().getTransferErrorMessage());
                    assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isCancelBtn(), true, "Cancel Button display as expected", "Cancel Button does not display."));
                    if (data.getTransferAnyway().equalsIgnoreCase("true")) {
                        assertCheck.append(actions.assertEqualBoolean(pages.getSupervisorTicketList().isTransferAnyWayBtn(), true, "Transfer Anyway button displayed as expected", "Transfer Anyway button not displayed as expected"));
                        try {
                            pages.getSupervisorTicketList().clickTransferAnyWayBtn();
                            pages.getSupervisorTicketList().searchTicket(ticketId);
                            assertCheck.append(actions.assertEqualStringType(pages.getSupervisorTicketList().getQueueValue().toLowerCase().trim(), data.getToQueue().toLowerCase().trim(), "Ticket Transferred to Selected Queue", "Ticket not Transferred to Selected Queue", true, true));
                        } catch (NoSuchElementException | TimeoutException g) {
                            commonLib.fail("Transfer Anyway does not display in case of ticket does not transfer to selected queue.", true);
                            pages.getSupervisorTicketList().clickCancelBtn();
                        }
                    } else {
                        pages.getSupervisorTicketList().clickCancelBtn();
                        commonLib.fail("Transfer to queue does not Perform as per config sheet both queue belong to same workgroup.", false);
                    }
                } else {
                    pages.getSupervisorTicketList().searchTicket(ticketId);
                    if (pages.getSupervisorTicketList().getQueueValue().trim().equalsIgnoreCase(data.getToQueue().trim()))
                        commonLib.pass("Ticket successfully transferred to the selected queue");
                    else
                        commonLib.fail("Ticket not transferred to the selected queue", true);
                    pages.getSupervisorTicketList().clearInputBox();
                }
            } catch (InterruptedException | NoSuchElementException | TimeoutException | ElementClickInterceptedException e) {
                pages.getFilterTabPage().clickOutsideFilter();
                pages.getFilterTabPage().clickCloseFilter();
                commonLib.fail("Not able to apply filter " + e.getMessage(), true);
            }
            pages.getSupervisorTicketList().clearInputBox();
        } catch (Exception e) {
            commonLib.fail("Exception in Method - transferToQueue" + e.fillInStackTrace(), true);
        }
        actions.assertAllFoundFailedAssert(assertCheck);
    }
}
