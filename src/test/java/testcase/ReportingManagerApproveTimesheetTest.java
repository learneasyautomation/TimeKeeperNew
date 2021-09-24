package testcase;

import com.relevantcodes.extentreports.LogStatus;
import dataprovider.ConfigDataProvider;
import factory.BrowserFactory;
import factory.DataProviderFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.Home;
import pages.Login;
import pages.TimeSheets;
import utility.*;

import java.io.IOException;

import org.testng.annotations.Listeners;

@Listeners(TestNGListener.class)
public class ReportingManagerApproveTimesheetTest extends ExtentReportClass {

    WebDriver driver;
    Login objLogin;
    Home objHome;
    String browser;
    String Url;
    KeyEvents objKeyEvents;
    CaptureScreenshot objCaptureScreenshot;
    BrowserFactory objBrowserFactory;
    ConfigDataProvider objConfigDataProvider;
    TimeSheets objTimeSheets;

    @BeforeMethod
    public void setup() {
        String fileName = new Exception().getStackTrace()[0].getFileName();
        logger = report.startTest(fileName);
        objBrowserFactory = new BrowserFactory(driver, logger);
        objConfigDataProvider = new ConfigDataProvider();
        driver = objBrowserFactory.getBrowser();
    }

    @Test(enabled = true, priority = 1, groups = {"smoke", "regression"}, description = "Verify that timesheet is approved by Reporting Manager which was approved by Project Manager already")
    public void testReportingManagerApproveTimesheet() throws InterruptedException {
        String screenShotPath = "";
        try {
            objLogin = new Login(driver, logger);
            objHome = new Home(driver, logger);
            objTimeSheets = new TimeSheets(driver, logger);
            objCaptureScreenshot = new CaptureScreenshot(driver, logger);

            String screenshotName = new Exception()
                    .getStackTrace()[0]
                    .getMethodName();

            if (driver.getTitle().contains("Time Keeper")) {
                logger.log(LogStatus.INFO, "Application title: " + driver.getTitle());
                objLogin.enterUsername(DataProviderFactory.getConfig().getReportingManagerUserName());
                objLogin.enterPassword(DataProviderFactory.getConfig().getReportingManagerPwd());
                objLogin.changePasswordStatus();
                objLogin.enableRememberMe();
                objLogin.clickLoginBtn();

                if (objHome.getTextMyTimeMenu().equalsIgnoreCase("My Time")) {
                    objHome.clickTimesheetsMenu();
                    objTimeSheets.clickActionByReportingManager();
                    objTimeSheets.approveByReportingManager();
                }
                screenShotPath = objCaptureScreenshot.captureScreenshot(screenshotName);
                logger.log(LogStatus.PASS, "Reporting Manager Timesheet approval is successful" + logger.addScreenCapture(screenShotPath));
            }
        } catch (Exception e) {
            logger.log(LogStatus.ERROR, e.getMessage());
            logger.log(LogStatus.ERROR, "Exception while Reporting Manager approves timesheet" +logger.addScreenCapture(screenShotPath));
        }
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        BrowserFactory.closeBrowser(driver);
    }
}