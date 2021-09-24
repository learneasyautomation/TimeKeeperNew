package testcase_Excel;

import com.relevantcodes.extentreports.LogStatus;
import dataprovider.ConfigDataProvider;
import factory.BrowserFactory;
import factory.DataProviderFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Home;
import pages.Login;
import utility.CaptureScreenshot;
import utility.ExtentReportClass;
import utility.KeyEvents;
import utility.TestNGListener;

import java.io.IOException;

@Listeners(TestNGListener.class)
public class ProjectManagerLoginTest extends ExtentReportClass {

    WebDriver driver;
    Login objLogin;
    Home objHome;
    String browser;
    String Url;
    KeyEvents objKeyEvents;
    CaptureScreenshot objCaptureScreenshot;
    BrowserFactory objBrowserFactory;
    ConfigDataProvider objConfigDataProvider;

    @BeforeMethod
    public void setup() {
        String fileName = new Exception().getStackTrace()[0].getFileName();
        logger = report.startTest(fileName);
        objBrowserFactory = new BrowserFactory(driver, logger);
        objConfigDataProvider = new ConfigDataProvider();
        driver = objBrowserFactory.getBrowser();
    }

    @Test(enabled = true, priority = 1, groups = {"smoke", "regression"}, description = "Verify that Project Manager is able to login into the application successfully")
    public void testProjectManagerLogin() throws InterruptedException {
        String screenShotPath = "";
        try {
            objLogin = new Login(driver, logger);
            objHome = new Home(driver, logger);
            objCaptureScreenshot = new CaptureScreenshot(driver, logger);

            String screenshotName = new Exception()
                    .getStackTrace()[0]
                    .getMethodName();

            if (driver.getTitle().contains("Time Keeper")) {
                logger.log(LogStatus.INFO, "Application title: " + driver.getTitle());
                objLogin.enterUsername(DataProviderFactory.getExcel().getData(0,7,1));
                objLogin.enterPassword(DataProviderFactory.getExcel().getData(0,8,1));
                objLogin.changePasswordStatus();
                objLogin.enableRememberMe();
                objLogin.clickLoginBtn();
                screenShotPath = objCaptureScreenshot.captureScreenshot(screenshotName);
                logger.log(LogStatus.INFO, "Screenshot is captured");
                Assert.assertEquals(objHome.getTextMyTimeMenu(), "My Time");
                logger.log(LogStatus.PASS, "Project Manager Login is successful and Project Manager landed on Home Page" + logger.addScreenCapture(screenShotPath));
            }
        } catch (Exception e) {
            logger.log(LogStatus.ERROR, e.getMessage());
            logger.log(LogStatus.ERROR, "Exception while logging in with Project Manager" + logger.addScreenCapture(screenShotPath));
        }
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        BrowserFactory.closeBrowser(driver);

    }
}