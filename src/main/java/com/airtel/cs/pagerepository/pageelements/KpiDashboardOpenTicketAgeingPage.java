package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;

public class KpiDashboardOpenTicketAgeingPage {
    public By lifetimeTillDateLabel = By.xpath("//span[contains(text(),'Lifetime Till Date')]");
    public By openTicketAgeingLabel = By.xpath("//span[contains(text(),'Open Ticket Ageing')]");
    public By monthLabel = By.xpath("//*[contains(text(),'Open Ticket Ageing')]/..//*[contains(text(),'Month')]");
    public By connectionLabel = By.xpath("//*[contains(text(),'Open Ticket Ageing')]/..//*[contains(text(),'Connection')]");
    public By issueSubTypeLabel = By.xpath("//*[contains(text(),'Open Ticket Ageing')]/..//*[contains(text(),'Issue Sub Type')]");
    public By downloadIcon = By.xpath("//img[contains(@src,'./assets/kpi-dashboard/download.svg')]");
    public By openTicketAgeingDetailsIcon = By.xpath("//span[contains(text(),'Open Ticket Ageing')]/..//img");
    public By issueCodeLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'Issue Code')]");
    public By queueNameLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'Queue Name')]");
    public By beyond30DaysLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'Beyond 30 Days')]");
    public By threeToTenDaysLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'3-10 Days')]");
    public By lessThanADayLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'Less Than A Day')]");
    public By twoToThreeDaysLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'2-3 Days')]");
    public By tenToThirtyDaysLabel = By.xpath("//*[contains(text(),'Issue Type & Queue')]/../..//span[contains(text(),'10-30 Days')]");
    public By slabreachTicketAgeingIssueTypeAndQueueLabel = By.xpath("//span[contains(text(),'Sla Breach Ticket Ageing-Issue Type & Queue ')]");
    public By slaBreachTicketAgeingAgentLabel = By.xpath("//span[contains(text(),'Sla Breach Ticket Ageing-Agent ')]");
    public By agentIdLabel = By.xpath("//span[contains(text(),'Agent Id')]");
    public By agentNameLabel = By.xpath("//span[contains(text(),'Agent Name')]");
    public By backIcon = By.xpath("//img[contains(@src,'assets/service-request/images/icon/back.svg')]");
}
