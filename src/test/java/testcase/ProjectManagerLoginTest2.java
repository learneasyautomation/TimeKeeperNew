package testcase;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import dataprovider.ConfigDataProvider;
import factory.BrowserFactory1;
import factory.BrowserFactory;
import factory.DataProviderFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Home4;
import pages.Login4;
import utility.*;

import java.io.IOException;

@Listeners(TestNGListener.class)
public class ProjectManagerLoginTest2 extends ExtentReportClass{

    WebDriver driver;
    ExtentReports report;
    ExtentTest logger1;
    Login4 objLogin4;
    Home4 objHome4;
    String browser;
    String Url;
    KeyEvents objKeyEvents;
    CaptureScreenshot1 objCaptureScreenshot1;
    BrowserFactory1 objBrowserFactory1;
    ConfigDataProvider objConfigDataProvider;
    Logger logger;

    @BeforeMethod
    public void setup() {
        String reportpath = System.getProperty("user.dir") + "\\Reports\\TimeKeeper" + Helper.getCurrentDateTime() + ".html";
        report = new ExtentReports(reportpath);
        System.out.println("=====After execution report can be checked at " + reportpath + "========");
        String fileName = new Exception().getStackTrace()[0].getFileName();
        report = new ExtentReports(".\\Reports\\" + fileName + ".html", true);
        DOMConfigurator.configure("log4j.xml");
        logger.info("# # # # # # # # # # # # # # # # # # # # # # # # # # # ");
        logger.info("TEST Has Started");
        logger1 = report.startTest(fileName);
        objBrowserFactory1 = new BrowserFactory1(driver, logger);
        objConfigDataProvider = new ConfigDataProvider();
        driver = objBrowserFactory1.getBrowser();
        logger.info("Application is up and running");
    }

    @Test(enabled = true, priority = 1, groups = {"smoke", "regression"}, description = "Verify that Project Manager is able to login into the application successfully")
    public void testProjectManagerLogin() throws InterruptedException {
        try {
            objLogin4 = new Login4(driver, logger);
            objHome4 = new Home4(driver, logger);
            objCaptureScreenshot1 = new CaptureScreenshot1(driver, logger);
            String screenShotPath = "";
            String screenshotName = new Exception()
                    .getStackTrace()[0]
                    .getMethodName();

            if (driver.getTitle().contains("Time Keeper")) {
                logger.info("Application title: " + driver.getTitle());
                objLogin4.enterUsername(DataProviderFactory.getConfig().getProjectManagerUserName());
                objLogin4.enterPassword(DataProviderFactory.getConfig().getProjectManagerPwd());
                objLogin4.changePasswordStatus();
                objLogin4.enableRememberMe();
                objLogin4.clickLoginBtn();
                screenShotPath = objCaptureScreenshot1.captureScreenshot(screenshotName);
                logger.info("Screenshot is captured");
                Assert.assertEquals(objHome4.getTextMyTimeMenu(), "My Time");
                logger.info("Project Manager Login is successful and Project Manager landed on Home Page");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Exception while logging in with Project Manager");
        }
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        BrowserFactory.closeBrowser(driver);
        report.endTest(logger1);
        report.flush();

    }
}