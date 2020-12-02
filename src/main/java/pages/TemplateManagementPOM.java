package pages;

import Utils.ExtentReports.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@Log4j2
public class TemplateManagementPOM extends BasePage {

    By createdTemplateTab = By.xpath("//div[@role=\"tab\"][1]");
    By viewCreatedTemplateTab = By.xpath("//div[@role=\"tab\"][2]");
    By addTemplateBtn = By.xpath("//mat-radio-button[1]");
    By addTemplateCategoryBtn = By.xpath("//mat-radio-button[2]");
    By categoryLabel = By.xpath("//label[contains(text(),'Category')]");
    By messageChannelLabel = By.xpath("//label[contains(text(),'Message Channel')]");
    By templateNameLabel = By.xpath("//label[contains(text(),'Template Name')]");
    By roleLabel = By.xpath("//label[contains(text(),'Role')]");
    By agentChannelLabel = By.xpath("//label[contains(text(),'Agents Channel')]");
    By smsLanguageLabel = By.xpath("//label[contains(text(),'SMS Language')]");
    By smsContentLabel = By.xpath("//label[contains(text(),'SMS Content')]");
    By selectedLanguages = By.xpath("//mat-tab-group[@class='teatarea mat-tab-group mat-primary ng-star-inserted']//div[@class='mat-tab-label-container']//div[@role=\"tab\"]");

    //Add template Category Tab
    By templateCategoryName = By.xpath("//input[@placeholder='Enter Category Name']");
    By addCategoryBtn = By.xpath("//span[contains(text(),'Add Category')]");
    By allCategoryList = By.xpath("//div[@class='sms-managment__card-list--card--content-area--content ng-star-inserted']//div[@class=\"sms-managment__card-list--card--content-area--content--sms-card ng-star-inserted\"]");

    //Add Template
    By openCategoryList = By.xpath("//app-custom-mat-select[@formcontrolname=\"templateCategory\"]//mat-select[@role=\"listbox\"]");
    By templateNameBox = By.xpath("//input[@formcontrolname=\"templateName\"]");
    By openRoleList = By.xpath("//app-custom-mat-select[@formcontrolname=\"roles\"]//mat-select[@role=\"listbox\"]");
    By openChannelList = By.xpath("//app-custom-mat-select[@formcontrolname=\"channels\"]//mat-select[@role=\"listbox\"]");
    By openLangList = By.xpath("//mat-select[@formcontrolname=\"language\"]");
    By smsContent = By.xpath("//textarea[@placeholder='Content']");
    By allOption = By.xpath("//mat-option[@role=\"option\"]");
    By cancelBtn = By.xpath("//button[contains(text(),'Cancel')]");
    By createBtn = By.xpath("//button[contains(text(),'Create')]");

    By message = By.xpath("//mat-dialog-container[@role='dialog']//app-template-modal//div//div//p");

    public TemplateManagementPOM(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        waitVisibility(createdTemplateTab);
        log.info("Checking that is Template Management Page is loaded : " + checkState(createdTemplateTab));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking that is Template Management Page is loaded : " + checkState(createdTemplateTab));
        return checkState(createdTemplateTab);
    }

    public void clickCreatedTemplateTab() {
        log.info("Clicking on created template");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking on created template");
        click(createdTemplateTab);
    }

    public ViewCreatedTemplatePOM clickViewCreatedTemplateTab() {
        log.info("Clicking on view created template");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking on view created template");
        click(viewCreatedTemplateTab);
        return new ViewCreatedTemplatePOM(driver);
    }

    public boolean isAddTemplateAvailable() {
        log.info("Checking create SMS template page have 'Add Template' Option with radio button to select : " + isElementVisible(addTemplateBtn));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking create SMS template page have 'Add Template' Option with radio button to select : " + isElementVisible(addTemplateBtn));
        return isElementVisible(addTemplateBtn);
    }

    public boolean isAddTemplateCategoryAvailable() {
        log.info("Checking create SMS template page have 'Add Template' Option with radio button to select : " + isElementVisible(addTemplateCategoryBtn));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking create SMS template page have 'Add Template Category' Option with radio button to select : " + isElementVisible(addTemplateCategoryBtn));
        return isElementVisible(addTemplateCategoryBtn);
    }

    public boolean isCategoryAvailable() {
        log.info("Checking 'Category' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(categoryLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking Category label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(categoryLabel));
        return isElementVisible(categoryLabel);
    }

    public boolean isMessageChannelAvailable() {
        log.info("Checking 'Message Channel' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(messageChannelLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking 'Message Channel' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(messageChannelLabel));
        return isElementVisible(messageChannelLabel);
    }

    public boolean isTemplateNameAvailable() {
        log.info("Checking 'Template name' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(templateNameLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking 'Template name' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(templateNameLabel));
        return isElementVisible(templateNameLabel);
    }

    public boolean isRoleAvailable() {
        log.info("Checking 'Role' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(roleLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking 'Role' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(roleLabel));
        return isElementVisible(roleLabel);
    }

    public boolean isAgentChannelAvailable() {
        log.info("Checking 'Agent Channel' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(agentChannelLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking 'Agent Channel' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(agentChannelLabel));
        return isElementVisible(agentChannelLabel);
    }

    public boolean isSMSLanguageAvailable() {
        log.info("Checking 'SMS Language' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(smsLanguageLabel));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking 'SMS Language' label displayed on the 'Add Template' tab, when the Customer channel is selected as 'SMS'." + isElementVisible(smsLanguageLabel));
        return isElementVisible(smsLanguageLabel);
    }

    public void switchTabToAddTemplateCategory() {
        log.info("Switch Tab to Add template category Tab");
        click(addTemplateCategoryBtn);
    }

    public void switchTabToAddTemplate() {
        log.info("Switch Tab to Add template category Tab");
        click(addTemplateBtn);
    }

    public void writeTemplateCategoryName(String text) {
        log.info("Creating Template Category with name: " + text);
        ExtentTestManager.getTest().log(LogStatus.INFO, "Creating Template Category with name: " + text);
        writeText(templateCategoryName, text);
    }

    public void writeTemplateName(String text) {
        log.info("Creating Template  with name: " + text);
        ExtentTestManager.getTest().log(LogStatus.INFO, "Creating Template  with name: " + text);
        writeText(templateNameBox, text);
    }

    public void clickAddCategoryBtn() {
        log.info("clicking add template category button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "clicking add template category button");
        click(addCategoryBtn);
    }

    public boolean validateAddedCategoryDisplay(String text) {
        List<WebElement> list = returnListOfElement(allCategoryList);
        for (int i = 1; i <= list.size(); i++) {
            By categoryName = By.xpath("//div[@class='sms-managment__card-list--card--content-area--content ng-star-inserted']//div[@class=\"sms-managment__card-list--card--content-area--content--sms-card ng-star-inserted\"][" + i + "]//h6");
            String name = readText(categoryName);
            log.info("Reading Category Name: " + name);
            if (name.trim().equalsIgnoreCase(text)) {
                ExtentTestManager.getTest().log(LogStatus.PASS, "Recent Added Template Category found with name :" + text);
                return true;
            }
        }
        ExtentTestManager.getTest().log(LogStatus.FAIL, "Recent Added Template Category does not found with name :" + text);
        return false;
    }

    public void selectOptionFromList(String text) {
        log.info("Selecting option with name: " + text);
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting option with name: " + text);
        selectByText(text);
    }

    public void clickTemplateCategory() {
        log.info("Opening Template Category name list");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening Template Category name list");
        click(openCategoryList);
    }

    public void clickAgentRole() {
        log.info("Opening Agent Role list");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening Agent Role list");
        click(openRoleList);
    }

    public void clickAgentChannels() {
        log.info("Opening Agent channel list");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening Agent channel list");
        click(openChannelList);
    }

    public void clickSMSLanguage() {
        log.info("Opening SMS language list");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Opening SMS language list");
        click(openLangList);
    }

    public boolean clickCreateTemplateBtn() {
        log.info("Clicking create template button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking create template button");
        if (driver.findElement(createBtn).isEnabled()) {
            click(createBtn);
            return true;
        } else {
            return false;
        }
    }

    public void writeSMSContent(String text) {
        log.info("SMS Content: " + text);
        ExtentTestManager.getTest().log(LogStatus.INFO, "SMS Content: " + text);
        writeText(smsContent, text);
    }

    public String readResponseMessage() {
//        String text = readText(message);
        String text= (String) js.executeScript("return arguments[0].innerHTML", driver.findElement(message));
        ExtentTestManager.getTest().log(LogStatus.INFO, "Response: " + text);
        return text;
    }

}