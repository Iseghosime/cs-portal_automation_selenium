package com.airtel.cs.pagerepository.pagemethods;

import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.pagerepository.pageelements.TariffPlanPage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Log4j2
public class TariffPlan extends BasePage {
    public TariffPlanPage pageElements;
    private static final String TEXT = "Not Clicked on Migrate Button";

    public TariffPlan(WebDriver driver) {
        super(driver);
        pageElements = PageFactory.initElements(driver, TariffPlanPage.class);
    }

    /*
    This Method will verify if Service Class Tab opened or not
     */
    public Boolean isChangeServiceClassTabOpened() {
        boolean result = false;
        try {
            result = elementVisibleWithExplictWait(pageElements.serviceClassTab);
        } catch (Exception e) {
            log.error("Service Class Tab is not Visible", e);
        }
        return result;
    }

    /*
    This Method will return the current plan of the customer from change service class tab
     */
    public String getCurrentPlan() {
        return getText(pageElements.currentPlanName);
    }

    /*
    This Method will return true/false depending upon checkbox under Service Class Tab is checked or unchecked
     */
    public Boolean isCheckBoxChecked() {
        return getAttributeBoolean(pageElements.currentPlanDetailsCheckBox, "aria-checked");
    }

    /*
    This Method will return Name of the current plan showing over Plan Description
     */
    public String getCurrentPlanDetailsHeader() {
        return getText(pageElements.currentPlanDetailsHeaderName);
    }

    /*
    This Method will return us the Plan Description
     */
    public String getPlanDescription() {
        return getText(pageElements.planDescription);
    }

    /*
    This Method will give us the drop down name under Service Class Tab
     */
    public String getDropDownName() {
        return getText(pageElements.dropDownName);
    }

    /*
    This Method will check Current Plan of the Customer Should not be there under Available Plans
     */
    public Boolean checkCurrentPlanNotInDropDownList(String currentPlan) {
        boolean result = true;
        clickAndWaitForLoaderToBeRemoved(pageElements.dropDown);
        final List<WebElement> elementsListfromBy = getElementsListFromBy(pageElements.dropDownList);
        for (WebElement dropDownList : elementsListfromBy) {
            result = dropDownList.getText().equalsIgnoreCase(currentPlan);
        }
        return result;
    }

    /*
    This Method will return us the size of the Drop Down present under Service Class Tab
     */
    public int getDropDownListSize() {
        final List<WebElement> elementsListfromBy = getElementsListFromBy(pageElements.dropDownList);
        return elementsListfromBy.size();
    }

    /*
    This Method will help us to select any plan other than Current Plan
     */
    public String selectPlanOtherThanCurrentPlan(String currentPlan) {
        String selectedValue = null;
        final List<WebElement> elementsListfromBy = getElementsListFromBy(pageElements.dropDownList);
        for (WebElement dropDownList : elementsListfromBy) {
            if (!(dropDownList.getText().equalsIgnoreCase(currentPlan))) {
                clickAndWaitForLoaderToBeRemoved(pageElements.dropDownList);
                selectedValue = getText(pageElements.selectedDropDownValue);
                break;
            }
        }
        return selectedValue;
    }

    /*
    This Method will give us the Note present under Service Class Tab, once select the Plan other than Current Plan
     */
    public String getFootNote() {
        return getText(pageElements.planFootNote);
    }

    /*
    This Method will check Migrate Button is present or not under Service Class Tab
     */
    public Boolean isMigrateButtonPresent() {
        return isVisible(pageElements.migrateButton);
    }

    /*
    This Method will let us know, Migrate button is enabled or not
     */
    public Boolean isMigrateBtnEnabled() {
        return isEnabled(pageElements.migrateButton);
    }

    /*
    This Method will open the issue details popup after click on Migrate btn
     */
    public void openIssueDetailsModal() {
        try {
            if (isVisible(pageElements.migrateButton)) {
                clickAndWaitForLoaderToBeRemoved(pageElements.migrateButton);
            }
        } catch (Exception e) {
            log.error(TEXT, e.getMessage());
        }
    }

    /*
    This Method will check issue details pop up opened or not
     */
    public Boolean isIssueDetailModalOpened() {
        commonLib.hardWait();
        return isVisible(pageElements.issueDetailPopUp);
    }

    /*
    This Method will let us know, Comment box is visible or not over Issue details pop up
     */
    public Boolean isCommentBoxVisible() {
        try {
            return isVisibleContinueExecution(pageElements.commentBox);
        }catch (NoSuchElementException | TimeoutException e){
            commonLib.info(constants.getValue("element.not.visible")+" "+e.fillInStackTrace());
            return false;
        }
    }

    /*
    This Method will select the reason over Issue details pop up
     */
    public Boolean isSelectReasonVisible() {
        return isVisible(pageElements.issueDetailsReason);
    }

    /*
    This Method will let us know, Cancel Button is visible or not over Issue Details Popup
     */
    public Boolean isCancelBtnVisisble() {
        return isVisible(pageElements.cancelBtn);
    }

    /*
        This Method will let us know, Submit Button is visible or not over Issue Details Popup
     */
    public Boolean isSubmitBtnVisible() {
        return isVisible(pageElements.submitBtn);
    }

    /*
    This Method will click on the Cancel Btn over issue details pop up
     */
    public void clickCancelBtn() {
        clickAndWaitForLoaderToBeRemoved(pageElements.cancelBtn);
    }

    /*
    This Method will click on the Submit Btn over issue details pop up
     */
    public void clickSubmitBtn() {
        clickAndWaitForLoaderToBeRemoved(pageElements.submitBtn);
    }

    /*
    This Method will click over issue details Popup
     */
    public void clickIssueDetails() {
        clickAndWaitForLoaderToBeRemoved(pageElements.issueDetailsReason);
    }

    /*
    This Method will select the Reason over issue details pop up
     */
    public void selectReason() {
        final List<WebElement> elementsListfromBy = getElementsListFromBy(pageElements.selectReason);
        for (WebElement dropDownList : elementsListfromBy) {
            if (dropDownList.getSize() != null) {
                clickAndWaitForLoaderToBeRemoved(pageElements.selectReason);
                break;
            }
        }
    }

    public void enterCommentIssuePopUp() {
        setTextWithTimeStamp(pageElements.commentBox, "Comment By Automation");
    }

    /*
    This Method will let us know, submit Btn is enabled or not over issue details pop up
     */
    public Boolean isSubmitEnabled() {
        return isEnabled(pageElements.submitBtn);
    }

    /*
    This Method will return the Note Present over the issue details pop up
     */
    public String getNoteTextIssueDetailsPopUp() {
        return getAttribute(pageElements.noteText, "title", false);
    }

    /*
    With this Method will open issue details modal,select reason, enter comment
     */
    public void enterDetailsIssuePopup() {
        openIssueDetailsModal();
        clickIssueDetails();
        selectReason();
        enterCommentIssuePopUp();
    }

    /*
    This Method will click on submit btn and plan will get changed
     */
    public Boolean changePlan() {
        clickAndWaitForLoaderToBeRemoved(pageElements.submitBtn);
        waitTillLoaderGetsRemoved();
        return isVisible(pageElements.successMsg);
    }

    /*
    This Method will provide us the text present over the Success or Failure modal
     */
    public String getModalText() {
        return getText(pageElements.modalSuccessFailureMsg);
    }

    /*
   This Method will login in CS Portal and will check the Tariff Plan Option visible or not
    */
    public void goAndCheckServiceClassOptionVisible() {
        try {
            pages.getLoginPage().doLoginInCSPortal();
            pages.getSideMenuPage().openCustomerInteractionPage();
            pages.getMsisdnSearchPage().enterNumber(constants.getValue(ApplicationConstants.TARIFF_PLAN_TEST_NUMBER));
            pages.getMsisdnSearchPage().clickOnSearch();
            pages.getCustomerProfilePage().clickOnAction();
        } catch (Exception e) {
            commonLib.error("Exception in Method - goAndCheckServiceClassOptionVisible " + e.getMessage());
        }
    }
}
