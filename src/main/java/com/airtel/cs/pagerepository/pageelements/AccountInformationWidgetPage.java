package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;

public class AccountInformationWidgetPage {

    /**
     * Account Information Widget
     */
    public By getTitle=By.xpath("//div[@id='POSTPAID_ACCOUNT_INFORMATION']//span[@class='card__card-header--label']");
    public By footerAuuid = By.xpath("//div[@id='POSTPAID_ACCOUNT_INFORMATION']//*[@class='auuid-container']");
    public By middleAuuid = By.xpath("//div[@id='POSTPAID_ACCOUNT_INFORMATION']");
    public By progressBarForCreditLimit = By.xpath("");
    public By progressBarRedPortionValue = By.xpath("");
    public By accountInfoDetailed = By.xpath("//div[@id='POSTPAID_ACCOUNT_INFORMATION']//img[@class='header-action-icon ng-star-inserted']");
    public By currentMonthUnbillAmount = By.xpath("");
    public By lastMonthBillAmount = By.xpath("");
    public By currentCycle = By.xpath("");
    public By dueDate = By.xpath("");
    public By lastPaymentMode = By.xpath("");
    public By securityDeposit = By.xpath("");
    public By totalOutstanding = By.xpath("");
    public By accountNumber = By.xpath("");
    public By currentCycleFrom = By.xpath("");
    public By currentCycleTo = By.xpath("");
    public By currentCycleBlackBold = By.xpath("");
    public By dueDateInBlackBold = By.xpath("");
    public By currencyBoldRedText = By.xpath("");

}