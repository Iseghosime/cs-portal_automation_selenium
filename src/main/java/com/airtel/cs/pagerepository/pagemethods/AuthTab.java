package com.airtel.cs.pagerepository.pagemethods;


import com.airtel.cs.commonutils.applicationutils.enums.JavaColors;
import com.airtel.cs.pagerepository.pageelements.AuthTabPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthTab extends BasePage {

    AuthTabPage pageElements;

    public AuthTab(WebDriver driver) {
        super(driver);
        pageElements = PageFactory.initElements(driver, AuthTabPage.class);
    }

    /**
     * This method is use to check auth tab display or not
     * @return true/false
     */
    public boolean isAuthTabLoad() {
        commonLib.info("Checking Authentication tab loaded");
        return isEnabled(pageElements.authTabTitle);
    }

    /**
     * This method is use to click close tab button
     */
    public void clickCloseBtn() {
        commonLib.info("Clicking on close button");
        clickAndWaitForLoaderToBeRemoved(pageElements.authCloseBtn);
    }

    /**
     * This method is use to get auth tab policy message
     * @return true/false
     */
    public String getAuthInstruction() {
        final String text = getText(pageElements.authInstruction);
        commonLib.info("Reading auth instruction: " + text);
        return text;
    }

    /**
     * This method is use to click on non-authenticate button
     */
    public void clickNonAuthBtn() {
        commonLib.info("Clicking on Non-Authenticate button");
        if (driver.findElement(pageElements.notAuthBtn).isEnabled())
            clickAndWaitForLoaderToBeRemoved(pageElements.notAuthBtn);
    }

    /**
     * This method is use to click authorise button
     */
    public void clickAuthBtn() {
        if (driver.findElement(pageElements.authBtn).isEnabled()) {
            commonLib.info("Clicking on Authenticate button");
            clickWithoutLoader(pageElements.authBtn);
        } else
            clickAndWaitForLoaderToBeRemoved(pageElements.authCloseBtn);
    }

    /*
    This Method will get the widget unlock message
     */
    public String getWidgetUnlockMessage() {
        return getText(pageElements.widgetUnlockMsg);
    }

    /**
     * This method is use to check non-authenticate button enable or not
     * @return true/false
     */
    public boolean isNonAuthBtnEnable() {
        commonLib.info("Checking Non-Authenticate button is enable");
        return driver.findElement(pageElements.notAuthBtn).isEnabled();
    }

    /**
     * This method is use to check authenticate button enable or not
     * @return true/false
     */
    public boolean isAuthBtnEnable() {
        commonLib.info("Checking Authenticate button is enable");
        return driver.findElement(pageElements.authBtn).isEnabled();
    }

    /**
     * This method is use to get policy question and answer
     * @return MAP The Question & Answer pair
     */
    public Map<String, String> getQuestionAnswer() {
        List<WebElement> list = returnListOfElement(pageElements.listOfQuestions);
        Map<String, String> questionList = new HashMap<>();
        for (int i = 1; i <= list.size(); i++) {
            By question = By.xpath( pageElements.authTabQuestion+ i + pageElements.question);
            By answer = By.xpath(pageElements.authTabQuestion+ i + pageElements.answer );
            commonLib.info("Question: " + getText(question) + " :" + getText(answer));
            questionList.put(getText(question).replaceAll("[^a-zA-Z]+", "").toLowerCase().trim(), getText(answer).trim());
        }
        return questionList;
    }

    /**
     * This method is use to check checkbox of policy question based on row number
     * @param i The row number
     * @throws InterruptedException throw exception when scroll to element interrupt
     */
    public void clickCheckBox(int i) throws InterruptedException {
        commonLib.info("Clicking " + i + "Ques Checkbox");
        By checkBox = By.xpath(pageElements.authTabCheckBox + i + pageElements.checkBox);
        scrollToViewElement(checkBox);
        clickWithoutLoader(checkBox);
    }

    /**
     * This method use to check sim bar pop up open or not
     * @return true/false
     */
    public boolean isSIMBarPopup() {
        final boolean state = isElementVisible(pageElements.simBarTitle);
        commonLib.info("Is popup open: " + state);
        return state;
    }

    /**
     * This method use to close sim bar pop up tab
     */
    public void closeSIMBarPopup() {
        commonLib.info("Closing SIM bar/unbar popup");
        clickAndWaitForLoaderToBeRemoved(pageElements.simCloseBtn);
    }

    /**
     * This method use to check issue detail configure or not
     * @return true/false
     */
    public boolean isIssueDetailTitleVisible() {
        boolean result = false;
        if (isVisible(pageElements.issueDetails)) {
            final boolean state = isEnabled(pageElements.issueDetails);
            commonLib.info("Is Issue Detail Configured: " + state);
            result = state;
        } else {
            commonLib.fail("Exception in method - isIssueDetailTitleVisible", true);
        }
        return result;
    }

    /**
     * This method use to click select reason drop down
     */
    public void clickSelectReasonDropDown() {
        if (isVisible(pageElements.listOfIssue)) {
            clickWithoutLoader(pageElements.listOfIssue);
        } else {
            commonLib.fail("Exception in method - clickSelectReasonDropDown", true);
        }
    }

    /**
     * This method use to get total number of input issue fields configured while performing sim bar/unbar action
     */
    public Integer getNumberOfInputFieldDisplay(){
        return returnListOfElement(pageElements.listOfFields).size();
    }

    public void fillAllInputField(String text){
        try {
            int size = getNumberOfInputFieldDisplay();
            for (int i = 1; i <= size; i++) {
                By inputField = By.xpath(pageElements.questionField + i + "']");
                enterText(inputField, text);
            }
        }catch (NoSuchElementException | TimeoutException e){
            commonLib.infoColored("No Issue Field found with input type"+e.getMessage(), JavaColors.BLUE,true);
        }
    }

    /**
     * This method use to get all the reason which is configure
     */
    public List<String> getReasonConfig() {
        List<WebElement> list = returnListOfElement(pageElements.options);
        List<String> reason = new ArrayList<>();
        for (int i = 1; i <= list.size(); i++) {
            String text = getText(By.xpath("//mat-option[" + i + "]//span"));
            commonLib.info("Reading Reason: " + text);
            reason.add(text.trim());
        }
        return reason;
    }

    /**
     * This method is use to choose first reason from dropdown
     */
    public void chooseReason() {
        commonLib.info("Selecting Reason: " + getText(pageElements.code));
        clickWithoutLoader(pageElements.code);
    }

    /**
     * This method is use to get first reason from dropdown
     * @return String The Value
     */
    public String getReason() {
        final String text = getText(pageElements.code);
        commonLib.info("Visible Reason: " + text);
        return text;
    }

    /**
     * This method is use to write comment into comment box
     * @param text The comment
     */
    public void enterComment(String text) {
        commonLib.info("Writing comment into comment box: " + text);
        enterText(pageElements.commentBox, text);
    }

    /**
     * This method use to check cancel button enable or not
     * @return true/false
     */
    public boolean isCancelBtnEnable() {
        commonLib.info("Checking SIM Bar/unbar cancel button is enable");
        return driver.findElement(pageElements.cancelBtn).isEnabled();
    }

    /**
     * This method use to check submit button enable or not
     * @return true/false
     */
    public boolean isSubmitBtnEnable() {
        commonLib.info("Checking SIM Bar/unbar submit button is enable");
        return driver.findElement(pageElements.submitBtn).isEnabled();
    }

    /**
     * This method use to click cancel button
     */
    public void clickCancelBtn() {
        commonLib.info("Clicking Cancel Button");
        clickAndWaitForLoaderToBeRemoved(pageElements.cancelBtn);
    }

    /**
     * This method use to click submit button
     */
    public void clickSubmitBtn() {
        if (isClickable(pageElements.submitBtn)) {
            commonLib.info("Clicking Submit Button");
            clickWithoutLoader(pageElements.submitBtn);
        } else {
            commonLib.fail("Exception in Method - clickSubmitBtn", true);
        }
    }

    /**
     * This method use to get toast message
     * @return String The Value
     */
    public String getToastText() {
        String result = null;
        if (isVisible(pageElements.toastModal)) {
            result = getText(pageElements.toastModal);
            clickWithoutLoader(pageElements.closeBtn);
        } else {
            commonLib.fail("Exception in method - getToastText", true);
            commonLib.info("Going to Close Modal through close Button");
            clickWithoutLoader(pageElements.closeBtn);
        }
        return result;
    }

    /**
     * This method use to get error message
     * @return String The String
     */
    public String getErrorMessage(){
        String text=getText(pageElements.errorMessage);
        commonLib.info("Reading Error Message Display over Pop up: "+text);
        return text;
    }

}
