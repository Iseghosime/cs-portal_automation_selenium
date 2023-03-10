package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;
import org.openqa.selenium.support.CacheLookup;

public class SupervisorTicketListPage {
    @CacheLookup
    public By searchTicketBox = By.xpath("//input[@type='search'][1]");
    public By searchTicketBox2 = By.xpath("//span[@class='search-box small-search' or @class='search-box small-search search-box-container']//input");
    @CacheLookup
    public By searchTicketBtn = By.xpath("//app-ticket-search-box//button");
    public By ticketIdLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[1]//span[@class='data-title' or @class='data-title highlight']");
    public By ticketIdvalue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area' or @class='data-area-full']//ul[1]//li[1]//span[@class='blue-clr']");
    public By workGroupName = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[2]//span[@class='data-title ellipsis value-clr' or @class='data-title value-clr']");
    public By workgroupSLA = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[2]//span[2]");
    public By prioritylabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[3]//span[1]");
    public By priorityValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[3]//span[2]");
    public By stateLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[4]//span[1]");
    public By statevalue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[4]//span[2]");
    public By creationdateLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[5]//span[1]");
    public By creationdatevalue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[5]//span[2]");
    public By createdbyLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[6]//span[1]");
    public By createdbyvalue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[6]//span[2]");
    public By queueLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[7]//span[1]");
    public By listQueueValue = By.xpath("//ul/li[7]/span[2]");
    public By queueValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'or @class='container-fluid table-card light-red ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[1]//li[7]//span[2]");
    public By issueLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[1]//span[1]");
    public By issueValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[1]//span[2]");
    public By issueTypeLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[2]//span[1]");
    public By issueTypeValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[2]//span[2]");
    public By subTypeLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[3]//span[1]");
    public By subTypeValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[3]//span[2]");
    public By subSubTypeLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[4]//span[1]");
    public By subSubTypeValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[4]//span[2]");
    public By codeLabel = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[5]//span[1]");
    public By codeValue = By.xpath("//app-ticket-list//div[@class='container-fluid table-card ng-star-inserted'][1]//div[@class='data-area-full' or @class='data-area']//ul[2]//li[5]//span[2]");
    public By assignedto = By.xpath("//body//p//span[1]");
    public By checkBox = By.xpath("//app-ticket-list//div[1]//div[1]//div[1]//input[@class='supercheck' or @class='supercheck ng-star-inserted']");
    public By assignToagentBtn = By.xpath("//li[1]//button[1]");
    public By transfertoQueueBtn = By.xpath("//button[contains(text(),'Transfer')]");
    public By loggedInQueue = By.xpath("//span[contains(text(),'Login with Ticket Pool')]");
    public By selectTicketType = By.xpath("//*[@name='state']");
    public By openTicketType = By.xpath("//span[contains(text(),' Open ') and @class='mat-option-text']");
    public By closedTicketType = By.xpath("//span[contains(text(),' Closed ') and @class='mat-option-text']");
    public By selectFilterBtn = By.xpath("//span[contains(text(),'Select Filter')]");
    public By pageRefreshBtn = By.xpath("//img[@class='refresh-image']");
    public By noResultFound = By.xpath("//body//mat-error//p[1]");
    public By resetFilterButton = By.xpath("//div[@class='clear-filter-btn']//button");
    public By reOpenBtn = By.xpath("//li[1]//button[1]");
    public By reOpenBox = By.xpath("//*[@placeholder='Reopen comment']");
    public By submitReopenComment = By.className("sbt-btn");
    public By closeReopenCommentBox = By.xpath("//button[@class='close-btn']");
    public By redDot = By.xpath("//span[@class='reddot ng-star-inserted']");
    public By greenDot = By.xpath("//span[@class='greendot ng-star-inserted']");
    public By assigneeAUUID = By.xpath("//div[contains(@class,'assignee-agent')]/p/span[3]");
    public By assigneeName = By.xpath("//div[@class='service-request']//div[1]//div[1]//div[2]//div[2]//p[1]//span[2]");
    public By allTicket = By.xpath("//div[@class='container-fluid table-card ng-star-inserted']");
    public By searchOptionBtn = By.xpath("//div[@class='options']");
    public By allSearchOption = By.xpath("//ul[@class='ng-star-inserted']//li");
    public By msisdn = By.xpath("//span[@class='td-msisdn auuid-clr']");
    public By allTicketTab = By.xpath("//div[@class='mat-tab-list']//div[contains(text(),'All Tickets')]");
    public By myTicketTab = By.xpath("//div[@class='mat-tab-list']//div[contains(text(),'My Assigned Tickets')]");
    public By transferErrorMessage = By.xpath("//div[@class='transferQueueError bk-light-red ng-star-inserted']//span");
    public By transferSuccessMessage=By.xpath("//div[@class='tranfered-ticket transferQueueError bk-light-green ng-star-inserted']//span");
    public By cancelBtn = By.xpath("//span[contains(text(),'Cancel')]");
    public By transferAnywayBtn = By.xpath("//span[contains(text(),'Transfer Anyway')]");
    public By notAssigned = By.xpath("//div[@class='service-request']//div[1]//div[1]//div[2]//div[2]//p[1]/span[contains(text(),'Not Assigned')]");
    public By sourceTitleTicketRowTicketListing = By.xpath("//span[contains(text(),'Source')] | //b[contains(text(),'Source')]");
    public By sourceTitleText = By.xpath("//*[@title='Customer Service']");
    public By openTicketDetailPage = By.xpath("//*[contains(@class,'data-area-full')]");
    public By backButtonDetailPage = By.xpath("//span[text()=' BACK ']");
    public By selectAll=By.xpath("//div[@class='filter-section']//input[@class='supercheck' or @class='supercheck ng-star-inserted']");
    public By closeTabBtn=By.className("close-button");
    public String ticketRow="//div[@class='container-fluid table-card ng-star-inserted'][";
    public String queueName="]//ul/li[7]/span[2]";
    public String optionText="//span[contains(text(),'";
    public String ticketIdText="]//ul[1]//li[1]//span[2]";
    public String ticketEscalation="]//span[@class='escalation']";
    public String selectByOption="//ul[@class='ng-star-inserted']//li[normalize-space()='";
    public By ticketAvailable = By.xpath("//div[@class='super-ticket-list ng-star-inserted']");
    public By ticketOnInteractionHistory = By.xpath("//div[@class='agent-list-container ng-star-inserted']");
    public By loginIntoQueue=By.xpath("//span[contains(text(),'Login as Agent')]");
    public By popUpTitle=By.xpath("//h1[contains(text(),' Select Queue ')]");
    public By loginQueueName=By.xpath("//label[@formarrayname='queues' and contains(text(),'CS Test Automation')]");
    public By agentLoggedInQueue=By.xpath("//span[contains(text(),'CS Test Automation')]");
    public By continueBtn=By.xpath("//span[contains(text(),'Continue')]");

    public By commentBox=By.xpath("//textarea[contains(@placeholder,'Closure Comment')]");
    public By submitButton=By.xpath("//button[contains(@class,'sbt-btn')]");

    /*
    public By successTicketTransferMessage=By.xpath("//span[contains(text(),'1 out of 1 ticket transferred to new queue successfully.')]");
    public By successTicketTransferIcon=By.xpath("//img[contains(@src,'successfully.svg')]");
     */
}
