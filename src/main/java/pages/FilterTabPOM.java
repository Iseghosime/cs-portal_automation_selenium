package pages;

import Utils.ExtentReports.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FilterTabPOM extends BasePage {

    By applyFilter = By.xpath("//button[@class=\"filter-button mat-button\"]");

    //Filter By Created date
    By filterCreatedByLabel=By.xpath("//div[@class=\"mat-drawer-inner-container\"]//span[contains(text(),'Filter By Created Date')]");
    By last7DaysCD=By.xpath("//mat-radio-group[@formcontrolname='days']/mat-radio-button[1]/label/div[2]/span[2]");
    By last30DaysCD=By.xpath("//mat-radio-group[@formcontrolname='days']/mat-radio-button[2]/label/div[2]/span[2]");
    By dateDurationCD=By.xpath("//mat-radio-group[@formcontrolname='days']/mat-radio-button[3]/label/div[2]/span[2]");
    By startDateCD=By.xpath("//input[@formcontrolname=\"startDate\"]");
    By endDateCD=By.xpath("//input[@formcontrolname=\"endDate\"]");

    //Filter By SLA Date
    By sLADueDateLabel= By.xpath("//div[@class=\"mat-drawer-inner-container\"]//span[contains(text(),'Filter By SLA Due Date')]");
    By last7DaysSD=By.xpath("//mat-radio-group[@formcontrolname='slaDays']/mat-radio-button[1]/label/div[2]/span[2]");
    By last30DaysSD=By.xpath("//mat-radio-group[@formcontrolname='slaDays']/mat-radio-button[2]/label/div[2]/span[2]");
    By dateDurationSD=By.xpath("//mat-radio-group[@formcontrolname='slaDays']/mat-radio-button[3]/label/div[2]/span[2]");
    By slaStartDate=By.xpath("//input[@formcontrolname=\"slaStartDate\"]");
    By slaEndDate=By.xpath("//input[@formcontrolname=\"slaEndDate\"]");

    //Filter By Category
    By categoryLabel=By.xpath("//div[@class=\"mat-drawer-inner-container\"]//span[contains(text(),'Filter By Category')]");
    By byCode=By.xpath("//mat-label[contains(text(),'Code')]");
    By byIssue=By.xpath("//label[@class='mat-form-field-label ng-tns-c6-98 mat-empty mat-form-field-empty ng-star-inserted']//mat-label[@class='ng-star-inserted'][contains(text(),'Issue')]");
    By byIssueType=By.xpath("//mat-label[contains(text(),'Issue Type')]");
    By byIssueSubType=By.xpath("//mat-label[contains(text(),'Issue sub type')]");
    By byIssueSubSubType=By.xpath("//mat-label[contains(text(),'Issue sub sub type')]");

    //Filter By Queue
    By queueLabel=By.xpath("//span[contains(text(),'Filter By Queue')]");
    By showQueueFilter = By.xpath("//mat-label[contains(text(),'Select Queue')]");
    By openQueueList = By.xpath("//div[5]//div[2]//div[1]//mat-form-field[1]//div[1]//div[1]//div[1]//mat-select[1]//div[1]//div[2]//div[1]");

    //Filter By Ticket Assignee
    By ticketAssigneeLabel=By.xpath("//span[contains(text(),'Filter By Ticket Assignee')]");
    By unAssigned=By.xpath("//span[contains(text(),'Unassigned')]");
    By assigned=By.xpath("//span[contains(text(),'Assigned')]");
    By assigneeList=By.xpath("//mat-select[@formcontrolname=\"selectedAssigneeName\"]");

    //Filter By Ticket Escalated level
    By escalatedLevelLabel=By.xpath("//span[contains(text(),'Filter By Escalated Level')]");
    By openEscalationFilter=By.xpath("//*[@formcontrolname=\"selectedEscalatedLevel\"]/div/div[2]");
    By level1Escalation= By.xpath("//span[contains(text(),' L1 ')]");
    By level2Escalation= By.xpath("//span[contains(text(),' L2 ')]");
    By level3Escalation= By.xpath("//span[contains(text(),' L3 ')]");

    //Filter By State
    By stateLabel=By.xpath("//span[contains(text(),'Filter By State')]");

    //Filter By Priority
    By priorityLabel=By.xpath("//span[contains(text(),'By Priority')]");



    public FilterTabPOM(WebDriver driver) {
        super(driver);
    }

    public void clickQueueFilter() {
        click(openQueueList);
    }

    public void scrollToQueueFilter() throws InterruptedException {
        scrollToViewElement(showQueueFilter);
    }


    public void selectQueueByName(String queueName) throws InterruptedException {
        ExtentTestManager.getTest().log(LogStatus.INFO, "Select Queue Filter Name: "+queueName);
        click(By.xpath("//span[contains(text(),' " + queueName + " ')]"));
    }

    public void clickApplyFilter() {
        ExtentTestManager.getTest().log(LogStatus.INFO, "Clicking on APPLY Filter Button");
        click(applyFilter);
    }

    public void clickOutsideFilter() {
        clickOutside();
    }

    public void clickUnAssignedFilter() throws InterruptedException {
        ExtentTestManager.getTest().log(LogStatus.INFO, "Apply Filter By Ticket Assignee");
        scrollToViewElement(unAssigned);
        click(unAssigned);
    }

    public void OpenEscalationFilter() throws InterruptedException {
        ExtentTestManager.getTest().log(LogStatus.INFO, "Apply filter by ticket escalation level");
        scrollToViewElement(openEscalationFilter);
        Thread.sleep(1000);
        click(openEscalationFilter);
    }

    public void selectAllLevel1(){
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting escalation level 1");
        try{
        List<WebElement> level1=driver.findElements(level1Escalation);
        for(WebElement level:level1){
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level);
            Thread.sleep(500);
            level.click();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectAllLevel2(){
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting escalation level 1");
        try{
            List<WebElement> level1=driver.findElements(level2Escalation);
            for(WebElement level:level1){
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level);
                Thread.sleep(500);
                level.click();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectAllLevel3(){
        ExtentTestManager.getTest().log(LogStatus.INFO, "Selecting escalation level 1");
        try{
            List<WebElement> level1=driver.findElements(level3Escalation);
            for(WebElement level:level1){
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", level);
                Thread.sleep(500);
                level.click();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
