package tests;

import Utils.DataProviders.DataProvider;
import Utils.DataProviders.TestDatabean;
import Utils.DataProviders.nftrDataBeans;
import Utils.ExtentReports.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.InteractionsPOM;
import pages.SideMenuPOM;
import pages.customerInteractionPagePOM;
import pages.customerInteractionsSearchPOM;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InteractionCommentTest extends BaseTest {

    @DataProvider.User(UserType = "ALL")
    @Test(priority = 1, description = "Validate Customer Interaction Page", dataProvider = "loginData", dataProviderClass = DataProvider.class)
    public void openCustomerInteraction(Method method, TestDatabean Data) throws IOException {
        ExtentTestManager.startTest("Validating the Search forCustomer Interactions :" + Data.getCustomerNumber(), "Validating the Customer Interaction Search Page By Searching Customer number : " + Data.getCustomerNumber());
        SoftAssert softAssert = new SoftAssert();
        SideMenuPOM SideMenuPOM = new SideMenuPOM(driver);
        SideMenuPOM.clickOnSideMenu();
        SideMenuPOM.clickOnName();
        customerInteractionsSearchPOM customerInteractionsSearchPOM = SideMenuPOM.openCustomerInteractionPage();
        customerInteractionsSearchPOM.enterNumber(Data.getCustomerNumber());
        customerInteractionPagePOM customerInteractionPagePOM = customerInteractionsSearchPOM.clickOnSearch();
        softAssert.assertTrue(customerInteractionPagePOM.isPageLoaded());
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Create Interaction ", dataProvider = "interactionComment", dataProviderClass = DataProvider.class)
    public void addInteractionComment(nftrDataBeans Data) throws InterruptedException, IOException {
        ExtentTestManager.startTest("Add Interaction Ticket Comment", "Add Interaction Ticket Comment on Ticket" + Data.getIssueCode());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        customerInteractionPagePOM customerInteractionPagePOM = new customerInteractionPagePOM(driver);
        InteractionsPOM interactionsPOM = customerInteractionPagePOM.clickOnInteractionIcon();
        SoftAssert softAssert = new SoftAssert();
        interactionsPOM.clickOnCode();
//        if (!interactionsPOM.isSearchVisible()) {
//            interactionsPOM.clickOnCode();
//        }
        try {
            interactionsPOM.searchCode(Data.getIssueCode());
        } catch (NoSuchElementException e) {
            interactionsPOM.clickOnCode();
            interactionsPOM.searchCode(Data.getIssueCode());

        }
        interactionsPOM.selectCode(Data.getIssueCode());
        ExtentTestManager.getTest().log(LogStatus.INFO, "Creating ticket with issue code -" + Data.getIssueCode());
        System.out.println(interactionsPOM.getIssue());
        softAssert.assertEquals(interactionsPOM.getIssue().trim().toLowerCase().replace(" ", ""), Data.getIssue().trim().toLowerCase().replace(" ", ""), "Issue is not as expected ");
        System.out.println(interactionsPOM.getIssueSubSubType());
        softAssert.assertEquals(interactionsPOM.getIssueSubSubType().trim().toLowerCase().replace(" ", ""), Data.getIssueSubSubType().trim().toLowerCase().replace(" ", ""), "Issue sub sub type is not as expected ");
        System.out.println(interactionsPOM.getIssueType());
        softAssert.assertEquals(interactionsPOM.getIssueType().trim().toLowerCase().replace(" ", ""), Data.getIssueType().trim().toLowerCase().replace(" ", ""), "Issue type is not as expected ");
        System.out.println(interactionsPOM.getIssueSubType());
        softAssert.assertEquals(interactionsPOM.getIssueSubType().trim().toLowerCase().replace(" ", ""), Data.getIssueSubType().trim().toLowerCase().replace(" ", ""), "Issue sub type is not as expected ");
        String ticket_number = null;
        try {
            if (Data.getIssueFieldType1().equalsIgnoreCase("Text Box")) {
                System.out.println(interactionsPOM.getIssueDetailLabel("1"));
                softAssert.assertEquals(interactionsPOM.getIssueDetailLabel("1").replace("*", "").trim(), (Data.getIssueFieldLabel1().replace("*", "").trim()));
                if (Data.getIssueFieldMandatory1().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.getIssueDetailLabel("1").contains("*"), Data.getIssueFieldLabel1() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setIssueDetailInput("1", "012345");
            } else if (Data.getIssueFieldType1().equalsIgnoreCase("Date")) {
                System.out.println(interactionsPOM.isDateFieldAvailable());
                softAssert.assertEquals(interactionsPOM.isDateFieldAvailable(), (Data.getIssueFieldLabel1()));
                if (Data.getIssueFieldMandatory1().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.isDateFieldAvailable().contains("*"), Data.getIssueFieldLabel1() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setDateFieldAvailable(dtf.format(now));

            }

            if (Data.getIssueFieldType2().equalsIgnoreCase("Text Box")) {
                System.out.println(interactionsPOM.getIssueDetailLabel("2"));
                softAssert.assertEquals(interactionsPOM.getIssueDetailLabel("2").replace("*", "").trim(), (Data.getIssueFieldLabel2().replace("*", "").trim()));
                if (Data.getIssueFieldMandatory2().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.getIssueDetailLabel("2").contains("*"), Data.getIssueFieldLabel2() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setIssueDetailInput("2", "012345");
            } else if (Data.getIssueFieldType2().equalsIgnoreCase("Date")) {
                System.out.println(interactionsPOM.isDateFieldAvailable());
                softAssert.assertEquals(interactionsPOM.isDateFieldAvailable(), (Data.getIssueFieldLabel2()));
                if (Data.getIssueFieldMandatory2().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.isDateFieldAvailable().contains("*"), Data.getIssueFieldLabel2() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setDateFieldAvailable(dtf.format(now));
            }

            if (Data.getIssueFieldType3().equalsIgnoreCase("Text Box")) {
                System.out.println(interactionsPOM.getIssueDetailLabel("3"));
                softAssert.assertEquals(interactionsPOM.getIssueDetailLabel("3").replace("*", "").trim(), (Data.getIssueFieldLabel3().replace("*", "").trim()));
                if (Data.getIssueFieldMandatory3().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.getIssueDetailLabel("3").contains("*"), Data.getIssueFieldLabel3() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setIssueDetailInput("3", "012345");
            } else if (Data.getIssueFieldType3().equalsIgnoreCase("Date")) {
                System.out.println(interactionsPOM.isDateFieldAvailable());
                softAssert.assertEquals(interactionsPOM.isDateFieldAvailable(), (Data.getIssueFieldLabel3()));
                if (Data.getIssueFieldMandatory3().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.isDateFieldAvailable().contains("*"), Data.getIssueFieldLabel3() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setDateFieldAvailable(dtf.format(now));

            }


            if (Data.getIssueFieldType4().equalsIgnoreCase("Text Box")) {
                System.out.println(interactionsPOM.getIssueDetailLabel("4"));
                softAssert.assertEquals(interactionsPOM.getIssueDetailLabel("4").replace("*", "").trim(), (Data.getIssueFieldLabel4().replace("*", "").trim()));
                if (Data.getIssueFieldMandatory4().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.getIssueDetailLabel("4").contains("*"), Data.getIssueFieldLabel4() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setIssueDetailInput("4", "012345");
            } else if (Data.getIssueFieldType4().equalsIgnoreCase("Date")) {
                System.out.println(interactionsPOM.isDateFieldAvailable());
                softAssert.assertEquals(interactionsPOM.isDateFieldAvailable(), (Data.getIssueFieldLabel4()));
                if (Data.getIssueFieldMandatory4().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.isDateFieldAvailable().contains("*"), Data.getIssueFieldLabel4() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setDateFieldAvailable(dtf.format(now));
            }

            if (Data.getIssueFieldType5().equalsIgnoreCase("Text Box")) {
                System.out.println(interactionsPOM.getIssueDetailLabel("5"));
                softAssert.assertEquals(interactionsPOM.getIssueDetailLabel("5").replace("*", "").trim(), (Data.getIssueFieldLabel5().replace("*", "").trim()));
                if (Data.getIssueFieldMandatory5().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.getIssueDetailLabel("5").contains("*"), Data.getIssueFieldLabel5() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setIssueDetailInput("5", "012345");
            } else if (Data.getIssueFieldType5().equalsIgnoreCase("Date")) {
                System.out.println(interactionsPOM.isDateFieldAvailable());
                softAssert.assertEquals(interactionsPOM.isDateFieldAvailable(), (Data.getIssueFieldLabel5()));
                if (Data.getIssueFieldMandatory5().equalsIgnoreCase("Yes")) {
                    softAssert.assertTrue(interactionsPOM.isDateFieldAvailable().contains("*"), Data.getIssueFieldLabel5() + "Label is mandatory but doesn't contain '*' ");
                }
                interactionsPOM.setDateFieldAvailable(dtf.format(now));
            }
            interactionsPOM.sendComment("Automation Suite");
            Assert.assertTrue(interactionsPOM.isSaveEnable());
            interactionsPOM.clickOnSave();
            softAssert.assertTrue(interactionsPOM.isResolvedFTRDisplayed());
            System.out.println(interactionsPOM.getResolvedFTRDisplayed());
            String[] valueToWrite = {""};
            if (!interactionsPOM.getResolvedFTRDisplayed().contains("Resolved FTR")) {
                ticket_number = interactionsPOM.getResolvedFTRDisplayed();
                System.out.println(ticket_number);
            } else {
                softAssert.fail("It's FTR not NFTR");
            }
            String comment="Adding Interaction Comment Using Automation";
            interactionsPOM.clickCommentIcon();
            interactionsPOM.addInteractionComment(comment);
            interactionsPOM.saveInteractionComment();
            interactionsPOM.waitTillLoaderGetsRemoved();
            interactionsPOM.openAddedComment();
            softAssert.assertEquals(interactionsPOM.getAddedComment().toLowerCase().trim(),comment.toLowerCase().trim(),"Agent Added Interaction Ticket comment does not match");

        } catch (NoSuchElementException e) {
            System.out.println("in catch");
            interactionsPOM.closeInteractions();
            interactionsPOM.clickOnContinueButton();
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        String base64Screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).
                getScreenshotAs(OutputType.BASE64);
        ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
        interactionsPOM.closeTicketCommentBox();
        softAssert.assertAll();
    }
}