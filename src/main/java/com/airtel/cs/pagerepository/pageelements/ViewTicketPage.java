package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;

public class ViewTicketPage {
    public By ticketIdValue = By.xpath("//span[@class='blueColor ellipsis']");
    public By arrowIcon = By.xpath("//div[@class='mat-form-field-infix']//div[@class='mat-select-arrow-wrapper']");
    public By submitAs = By.className("submit-btn");
    public By stateName = By.xpath("//button[@class='sbmit-colse-btn']//span[2]");
    public By addCommentBox = By.xpath("//textarea[@placeholder='Add Comment...']");
    public By addBtn = By.xpath("//button[@class='add-button']//span");
    public By allComment = By.xpath("//table[@class='ng-star-inserted']/tbody/tr");
    public By allTicketState = By.xpath("//div[@class='cdk-overlay-pane']//mat-option");
    public By continueBtn = By.xpath("//span[contains(text(),'continue')]");
    public By cancelBtn = By.xpath("//button[@class='no-btn mat-button']");
    public By backButton = By.xpath("//button[@class='back mat-button']");
    public By ticketLogTab=By.xpath("//a[contains(text(),'Ticket Log')]");
    public String readLoggedText="//p//span[@class='open-state' and contains(text(),'";
    public String bulkUpdate="')]//following-sibling::span[contains(text(),'Bulk update')]";
    public String stateOptions="//div[@class='cdk-overlay-pane']//mat-option[";
    public String stateText="]//span";
    public String commentSection="//table[@class='ng-star-inserted']//tbody//tr[";
    public String addComment= "]//p";
    public String commentType="]/td/span/span[1]";
    public String editCommentSection="]//td[1]//a[1]//img[1]";
    public String deleteIcon="]//td[1]//a[2]//img[1]";
    public String iconList="//table[@class='ng-star-inserted']/tbody//tr[";
    public By issueComment=By.xpath("//table[@class='ng-star-inserted']//tbody//tr[1]/td/span/span[1]");
}
