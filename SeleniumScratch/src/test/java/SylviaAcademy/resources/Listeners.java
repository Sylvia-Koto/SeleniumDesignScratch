package SylviaAcademy.resources;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import SylviaAcademy.base.BaseTest;
import SylviaAcademy.factory.DriverManager;

public class Listeners implements ITestListener {

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
        // 1. Log l'échec dans le rapport
        extentTest.get().fail(result.getThrowable());
        
        try {
            // 2. Récupérer le driver depuis votre DriverManager
            WebDriver driver = DriverManager.getDriver();
            
            if (driver != null) {
                // 3. Capture en base64 (sans fichier temporaire)
                String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                
                // 4. Attacher au rapport Extent
                extentTest.get().fail("Screenshot du failure", 
                    MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                
                // 5. Optionnel: Sauvegarde dans un fichier
                saveScreenshotToFile(result.getMethod().getMethodName(), screenshotBase64);
            } else {
                extentTest.get().warning("Aucun driver disponible dans DriverManager");
                
                // Fallback: Essayer de récupérer depuis l'instance de test
                attemptFallbackScreenshot(result);
            }
        } catch (Exception e) {
            extentTest.get().warning("Échec de la capture: " + e.getMessage());
        }
    }

    private void attemptFallbackScreenshot(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            if (testInstance instanceof BaseTest) {
                WebDriver fallbackDriver = ((BaseTest) testInstance).getDriver();
                if (fallbackDriver != null) {
                    String screenshotBase64 = ((TakesScreenshot) fallbackDriver).getScreenshotAs(OutputType.BASE64);
                    extentTest.get().fail("Screenshot (fallback)", 
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                }
            }
        } catch (Exception e) {
            extentTest.get().warning("Échec du fallback: " + e.getMessage());
        }
    }

    private void saveScreenshotToFile(String methodName, String base64Screenshot) {
        try {
            byte[] decodedImg = Base64.getDecoder().decode(base64Screenshot);
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String fileName = "screenshots/" + methodName + "_" + timestamp + ".png";
            
            Files.createDirectories(Paths.get("screenshots"));
            Files.write(Paths.get(fileName), decodedImg);
        } catch (Exception e) {
            extentTest.get().warning("Échec sauvegarde fichier: " + e.getMessage());
        }
    }
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}