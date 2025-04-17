package SylviaAcademy.resources;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import SylviaAcademy.base.BaseTest;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Listeners extends BaseTest implements ITestListener {

    ExtentReports extent = ExtentReporterNG.getReportObject();
    ExtentTest test;
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());
        
        try {
            // Méthode 1: Utiliser directement le driver de BaseTest
            if (this.driver != null) {
                captureAndAttachScreenshot();
                return;
            }
            
            // Méthode 2: Récupérer le driver depuis l'instance de test
            Object testInstance = result.getInstance();
            if (testInstance instanceof BaseTest) {
                WebDriver testDriver = ((BaseTest) testInstance).getDriver();
                if (testDriver != null) {
                    String screenshotPath = ((BaseTest) testInstance).getScreenshot(result.getMethod().getMethodName(), testDriver);
                    extentTest.get().addScreenCaptureFromPath(screenshotPath);
                    return;
                }
            }
            
            extentTest.get().warning("Aucun driver disponible pour capture d'écran");
            
        } catch (Exception e) {
            extentTest.get().warning("Échec capture: " + e.getMessage());
        }
    }

    private void captureAndAttachScreenshot() throws IOException {
        String screenshotBase64 = ((TakesScreenshot)this.driver).getScreenshotAs(OutputType.BASE64);
        extentTest.get().fail("Screenshot du failure", 
            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}