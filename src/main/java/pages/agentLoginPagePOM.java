package pages;

import Utils.ExtentReports.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Log4j2
public class agentLoginPagePOM extends BasePage {

    By loginQueueTitle = By.xpath("//h2[contains(text(),'Login Queue')]");
    By selectQueue = By.xpath("//span[contains(text(),'Select Sub Workgroup (Queue)')]");
    By skipBtn = By.xpath("//span[contains(text(),'Skip > >')]");
    By submitBtn = By.xpath("//span[contains(text(),'Submit')]");
    By allQueueOption = By.xpath("//body/div/div/div/div/div/app-select-all/mat-checkbox/label/span[1]");


    public agentLoginPagePOM(WebDriver driver) {
        super(driver);
    }


    public boolean checkSkipButton() {
        log.info("Checking Skip Button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking Agent Login Page SKIP Button");
        return checkState(skipBtn);
    }

    public boolean checkSubmitButton() {
        log.info("Checking Submit Button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking Agent Login Page Submit Button");
        return checkState(submitBtn);
    }

    public boolean isQueueLoginPage() {
        log.info("Login Page");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Supervisor Login Page");
        return checkState(loginQueueTitle);
    }

    public void clickSelectQueue() {
        log.info("Select Login Queue");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting Login Queue");
        click(selectQueue);
    }

    public void clickSkipBtn() {
        log.info("Clicking on SKIP Button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking on SKIP Button");
        click(skipBtn);
    }

    public void clickSubmitBtn() {
        log.info("Clicking on Submit Button");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking on SUBMIT Button");
        clickOutside();
        click(submitBtn);
    }

    public void byQueueName(String queueName) throws InterruptedException {
        log.info("Selecting Queue : " + queueName);
        selectByText(queueName);
    }

    public void selectAllQueue() {
        log.info("Selecting ALL Queue");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting ALL Queue");
        click(allQueueOption);
    }
}
