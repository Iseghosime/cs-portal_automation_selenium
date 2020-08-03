package pages;

import Utils.ExtentReports.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@Log4j2
public class MoreUsageHistoryPOM extends BasePage {
    By bundleName = By.xpath("div[@class=\"ng-star-inserted\"][1]/span");
    By transactionNumber = By.xpath("div[@class=\"ng-star-inserted\"][2]/span");
    By dateTime = By.xpath("div[@class=\"ng-star-inserted\"][3]/span");
    By smsTo = By.xpath("div[@class=\"ng-star-inserted\"][4]/span");
    By usedData = By.xpath("div[@class=\"ng-star-inserted\"][4]/span");
    By dataCharges = By.xpath("div[@class=\"ng-star-inserted\"][5]/span");
    By callDuration = By.xpath("div[@class=\"ng-star-inserted\"][4]/span");
    By callCharges = By.xpath("div[@class=\"ng-star-inserted\"][6]/span");
    By callTo = By.xpath("div[@class=\"ng-star-inserted\"][5]/span");
    By smsCharges = By.xpath("div[@class=\"ng-star-inserted\"][5]/span");
    By smsHistoryRows = By.xpath("//span[contains(text(),\"SMS History \")]//parent::div//following-sibling::div[@class=\"card__content restricted ng-star-inserted\"]//div[@class=\"card__card-header--card-body--table--data-list ng-star-inserted\"]");
    By callHistoryRows = By.xpath("//span[contains(text(),\"Call History \")]//parent::div//following-sibling::div[@class=\"card__content restricted ng-star-inserted\"]//div[@class=\"card__card-header--card-body--table--data-list ng-star-inserted\"]");
    By dataHistoryRows = By.xpath("//span[contains(text(),\"Data History \")]//parent::div//following-sibling::div[@class=\"card__content restricted ng-star-inserted\"]//div[@class=\"card__card-header--card-body--table--data-list ng-star-inserted\"]");
    List<WebElement> smsHistoryRowsElements = driver.findElements(smsHistoryRows);
    List<WebElement> callHistoryRowsElements = driver.findElements(callHistoryRows);
    List<WebElement> dataHistoryRowsElements = driver.findElements(dataHistoryRows);
    By smsDatePicker = By.xpath("//span[contains(text(),\"SMS History \")]//following-sibling::form/span[@class=\"datepicker-transaction ng-star-inserted\"]");
    By dataDatePicker = By.xpath("//span[contains(text(),\"Data History \")]//following-sibling::form/span[@class=\"datepicker-transaction ng-star-inserted\"]");
    By callDatePicker = By.xpath("//span[contains(text(),\"Call History \")]//following-sibling::form/span[@class=\"datepicker-transaction ng-star-inserted\"]");

    public MoreUsageHistoryPOM(WebDriver driver) {
        super(driver);
    }

    public String getSmsBundleName(int RowNumber) {
        WebElement rowElement = smsHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        return rowElement.findElement(bundleName).getText();
    }

    public String getCallBundleName(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting Call Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Call Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        return rowElement.findElement(bundleName).getText();
    }

    public String getDataBundleName(int RowNumber) {
        WebElement rowElement = dataHistoryRowsElements.get(RowNumber);
        log.info("Getting Data Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Data Bundle Name from Row Number " + RowNumber + " : " + rowElement.findElement(bundleName).getText());
        return rowElement.findElement(bundleName).getText();
    }

    public String getDataTransactionNumber(int RowNumber) {
        WebElement rowElement = dataHistoryRowsElements.get(RowNumber);
        log.info("Getting Data Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Data Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        return rowElement.findElement(transactionNumber).getText();
    }

    public String getCallTransactionNumber(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting Call Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Call Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        return rowElement.findElement(transactionNumber).getText();
    }

    public String getSMSTransactionNumber(int RowNumber) {
        WebElement rowElement = smsHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS Transaction Number from Row Number " + RowNumber + " : " + rowElement.findElement(transactionNumber).getText());
        return rowElement.findElement(transactionNumber).getText();
    }

    public String getSMSDateTime(int RowNumber) {
        WebElement rowElement = smsHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        return rowElement.findElement(dateTime).getText();
    }

    public String getCallDateTime(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        return rowElement.findElement(dateTime).getText();
    }

    public String getDataDateTime(int RowNumber) {
        WebElement rowElement = dataHistoryRowsElements.get(RowNumber);
        log.info("Getting Data Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Data Date and Time from Row Number " + RowNumber + " : " + rowElement.findElement(dateTime).getText());
        return rowElement.findElement(dateTime).getText();
    }

    public String getSMSTo(int RowNumber) {
        WebElement rowElement = smsHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS To from Row Number " + RowNumber + " : " + rowElement.findElement(smsTo).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS To from Row Number " + RowNumber + " : " + rowElement.findElement(smsTo).getText());
        return rowElement.findElement(smsTo).getText();
    }

    public String getSMSCharges(int RowNumber) {
        WebElement rowElement = smsHistoryRowsElements.get(RowNumber);
        log.info("Getting SMS Charges from Row Number " + RowNumber + " : " + rowElement.findElement(smsCharges).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting SMS Charges from Row Number " + RowNumber + " : " + rowElement.findElement(smsCharges).getText());
        return rowElement.findElement(smsCharges).getText();
    }

    public String getCallCharges(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting Call Charges from Row Number " + RowNumber + " : " + rowElement.findElement(callCharges).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Call Charges from Row Number " + RowNumber + " : " + rowElement.findElement(callCharges).getText());
        return rowElement.findElement(callCharges).getText();
    }

    public String getDataCharges(int RowNumber) {
        WebElement rowElement = dataHistoryRowsElements.get(RowNumber);
        log.info("Getting Data Charges from Row Number " + RowNumber + " : " + rowElement.findElement(dataCharges).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Data Charges from Row Number " + RowNumber + " : " + rowElement.findElement(dataCharges).getText());
        return rowElement.findElement(dataCharges).getText();
    }

    public String getCallTo(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting Call To from Row Number " + RowNumber + " : " + rowElement.findElement(callTo).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Call To from Row Number " + RowNumber + " : " + rowElement.findElement(callTo).getText());
        return rowElement.findElement(callTo).getText();
    }

    public String getUsedData(int RowNumber) {
        WebElement rowElement = dataHistoryRowsElements.get(RowNumber);
        log.info("Getting Used Data To from Row Number " + RowNumber + " : " + rowElement.findElement(usedData).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Used Data To from Row Number " + RowNumber + " : " + rowElement.findElement(usedData).getText());
        return rowElement.findElement(usedData).getText();
    }

    public String getCallDuration(int RowNumber) {
        WebElement rowElement = callHistoryRowsElements.get(RowNumber);
        log.info("Getting Used Data To from Row Number " + RowNumber + " : " + rowElement.findElement(callDuration).getText());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Getting Used Data To from Row Number " + RowNumber + " : " + rowElement.findElement(callDuration).getText());
        return rowElement.findElement(callDuration).getText();
    }

    public boolean isSMSDatePickerVisible() {
        log.info("Checking is SMS DatePicker is Enabled");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking is SMS DatePicker is Enabled");
        return checkState(smsDatePicker);
    }

    public boolean isDataDatePickerVisible() {
        log.info("Checking is Data DatePicker is Enabled");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking is Data DatePicker is Enabled");
        return checkState(dataDatePicker);
    }

    public boolean isCallDatePickerVisible() {
        log.info("Checking is Call DatePicker is Enabled");
        ExtentTestManager.getTest().log(LogStatus.INFO, "Checking is Call DatePicker is Enabled");
        return checkState(callDatePicker);
    }

    public int getNumbersOfSMSRows() {
        return smsHistoryRowsElements.size();
    }

    public int getNumbersOfDataRows() {
        return dataHistoryRowsElements.size();
    }

    public int getNumbersOfCallRows() {
        return callHistoryRowsElements.size();
    }

}