package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;

public class RechargeHistoryWidgetPageElements {
    public By rechargeHistoryDatePicker = By.xpath("//div[@id='RECHARGE_HISTORY']//input[@name='dateRange']");
    public By rechargeHistoryHeader = By.xpath("//div[@id='RECHARGE_HISTORY']//span[@class='card__card-header--label'");
    public By rows = By.xpath("//div[@id='RECHARGE_HISTORY']//parent::div//following-sibling::div[@class=\"card__content restricted ng-star-inserted\"]//div[@class=\"table-data-wrapper ng-star-inserted\"]//div[@class=\"card__card-header--card-body--table--data-list row-border\"]");
    public By menu = By.xpath("//div[@id='RECHARGE_HISTORY']//span/img[@class='header-action-icon ng-star-inserted']");
    public By more = By.xpath("//button[text()=\"Recharge History\"]");
    public By rechargeHistoryNoResultFound = By.xpath("//div[@id='RECHARGE_HISTORY']/div[@class=\"card__content restricted ng-star-inserted\"]/descendant::div[@class=\"no-result-found ng-star-inserted\"]/img");
    public By rechargeHistoryNoResultFoundMessage = By.xpath("//div[@id='RECHARGE_HISTORY']/div[@class=\"card__content restricted ng-star-inserted\"]/descendant::div[@class=\"no-result-found ng-star-inserted\"]/span/span");
    public By rechargeHistoryError = By.xpath("//span[contains(text(),\"Recharge History\")]/ancestor::div[@class=\"card ng-star-inserted\"]/div[@class=\"card__content restricted ng-star-inserted\"]/descendant::div[@class=\"widget-error apiMsgBlock ng-star-inserted\"][1]");
    public By ticketIcon = By.xpath("//div[@id='RECHARGE_HISTORY']//span/img[@class='interaction-ticket']");
    public By getTitle = By.xpath("//div[@id='RECHARGE_HISTORY']//span[@class='card__card-header--label'");
    public By voucherBox = By.xpath("//input[@placeholder=\"Voucher ID\"]");
    public By voucherBtn = By.xpath("//input[@placeholder=\"Voucher ID\"]//parent::span//button");
    public By refillIconDisable = By.xpath("//div[@id='RECHARGE_HISTORY']/parent::div//span[2]/span/img[@class=\"header-action-icon disabled ng-star-inserted\"]");
    public By refillIconClickable = By.xpath("//div[@id='RECHARGE_HISTORY']//parent::div//span[2]/span/img[@class=\"header-action-icon ng-star-inserted\"]");
    public By popUpMessage = By.xpath("//div[@class=\"confirm-block ng-star-inserted\"]/p");
    public By noActionBtn = By.xpath("//div[@class=\"confirm-block ng-star-inserted\"]//button[@class=\"no-btn\"]");
}