package com.airtel.cs.driver;

import com.airtel.cs.commonutils.applicationutils.constants.ApplicationConstants;
import com.airtel.cs.commonutils.applicationutils.constants.CommonConstantsUtils;
import com.airtel.cs.commonutils.applicationutils.constants.ConstantsUtils;
import com.airtel.cs.commonutils.commonlib.CommonLib;
import com.airtel.cs.commonutils.extentreports.ExtentReport;
import com.airtel.cs.commonutils.seleniumutils.SeleniumCommonUtils;
import com.airtel.cs.pagerepository.pagemethods.PageCollection;
import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.http.Header;
import lombok.extern.log4j.Log4j2;
import org.joda.time.LocalDateTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class Driver {


    private static final String USER_DIR = "user.dir";
    public static final String OPCO = System.getProperty("Opco").toUpperCase();
    public static WebDriver driver;
    public static WebDriver default_Driver = null;
    public static WebDriver temp_Driver = null;
    public static String loginURL = null;
    public static String parentWindowHandle;
    public static String tempWindowHandle;
    public static PageCollection pages;
    public static final CommonLib commonLib = new CommonLib();
    public static Properties config;
    public static String Env;
    public static final StringBuilder TESTCASE_DESCRIPTION_BUILDER = new StringBuilder(); // FOR ADDING TESTCASE DESCRIPTION IN EXTNT REPORTS
    public static String excelPath;
    public static List<Header> map = new ArrayList<>();
    public static String Token;
    public static String baseUrl;
    public static String umBaseUrl;
    public static String suiteType;
    public StringBuilder assertCheck = null;
    public static final ExtentReport reporter = new ExtentReport();
    public static final SeleniumCommonUtils selUtils = new SeleniumCommonUtils();
    public static final LocalDateTime dateTime = new LocalDateTime();
    public static ExtentTest test;
    public static ExtentReports reports;
    public static ConstantsUtils constants = ConstantsUtils.getInstance();
    public static CommonConstantsUtils commonConstants =CommonConstantsUtils.getInstance();
    private static final String EVN_NAME = System.getProperty("Env").toUpperCase();
    public static Recordset recordset = null;
    public static boolean continueExecutionAPI = true;
    public static boolean continueExecutionBA = true;
    public static boolean continueExecutionBS = true;
    public static boolean continueExecutionFA = true;
    public static String ElementName = ""; // FOR PASSING ELEMENT NAMES TO LOGS
    public static String Message = null;
    public static String token =null;



    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite
    public void classLevelSetup() throws IOException {
        if (System.getProperty("suiteType").equalsIgnoreCase("Regression")) {
            suiteType = "Regression";
        } else if (System.getProperty("suiteType").equalsIgnoreCase("Sanity")) {
            suiteType = "Sanity";
        } else if (System.getProperty("suiteType").equalsIgnoreCase("Prod")) {
            suiteType = "Prod";
        }


        Env = System.getProperty("Env").toUpperCase();
        if (Env.equalsIgnoreCase("TEST")) Env = "SIT";
        excelPath = System.getProperty(USER_DIR) + "/resources/excels/" + OPCO + ".xlsx";
        config = new Properties();
        FileInputStream fis;
        fis = new FileInputStream(System.getProperty(USER_DIR) + "/resources/properties/" + OPCO + "-config.properties");
        config.load(fis);
        System.out.println(config.getProperty(suiteType + "-NftrSheet"));
        System.out.println("OPCO Chosen :" + OPCO);
        System.out.println("Environment Chosen : " + Env);
        System.out.println("Suite Type : " + suiteType);

        baseUrl = config.getProperty(Env + "-APIBase");
        umBaseUrl = constants.getValue(ApplicationConstants.UM_API_BASE);
        loginURL = constants.getValue(ApplicationConstants.UM_LOGIN_URL);
        String browser = config.getProperty("browser");
        System.out.println(baseUrl);
        if (browser.equals("chrome")) {
            WebDriverManager.chromedriver().proxy("172.23.12.116:4145").setup(); //Always use this on server
            //WebDriverManager.chromedriver().setup(); //Use this for local for proxy issue
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1792,1120");
            options.setHeadless(false);
            Map<String, Object> prefs = new HashMap<String, Object>();
            //prefs.put("download.prompt_for_download", false);
            prefs.put("download.default_directory", System.getProperty("user.dir") + "\\Excels");
            prefs.put("intl.accept_languages", "nl");
            prefs.put("disable-popup-blocking", "true");
            options.setExperimentalOption("prefs", prefs);
            //Using with Options will start in Headless Browser
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        } else if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
        }
        default_Driver = driver;
        parentWindowHandle = default_Driver.getWindowHandle();
        assertCheck = new StringBuilder(); // @ THIS WILL EMPTY ASSERT STRING-BUILDER BEFORE EACH TEST
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        try {
            initializePages();
        } catch (Exception e) {
            commonLib.error(e.getMessage());
        }
    }

    @BeforeMethod
    public void methodLevelSetup() {
        assertCheck = new StringBuilder(); // @ THIS WILL EMPTY ASSERT STRING-BUILDER BEFORE EACH TEST
    }

    @AfterSuite
    public void teardown() {
        driver.quit();
    }

    /**
     * Initialize pages.
     */
    public static void initializePages() {
        pages = new PageCollection(driver);
    }
    /**
     * Goto URL.l
     *
     * @param url the url
     */
    // open url method
    public void gotoURL(String url) {
        try {
            commonLib.info("OPENING URL -" + url);
            driver.get(url);
            try {
                driver.switchTo().alert().accept();
            } catch (Exception e) {
            }
            selUtils.waitForPageLoad();
        } catch (Exception e) {
            e.getStackTrace();
            commonLib.warning("Exception in method - | gotoURL | " + "</br>" + e.getMessage());
        }
    }
}