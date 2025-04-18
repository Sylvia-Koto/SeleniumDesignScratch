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

	  private static ExtentReports extent = ExtentReporterNG.getReportObject();
	    private ExtentTest test;
	    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>(); // Rendue static

  
    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }
    
    public static ExtentTest getCurrentTest() {
        return extentTest.get();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());

        // Génère un ID unique basé sur la méthode + paramètres
        String uniqueId = getUniqueTestId(result);

        // Vérifie s’il y a un autre test échoué avec exactement les mêmes paramètres
        boolean alreadyHandled = result.getTestContext().getFailedTests().getAllResults().stream()
            .filter(r -> r != result)
            .anyMatch(r -> getUniqueTestId(r).equals(uniqueId));

        if (!alreadyHandled) {
            try {
                WebDriver driver = DriverManager.getDriver();
                if (driver != null) {
                    String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    extentTest.get().fail("Test failed after retries",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                    saveScreenshotToFile(uniqueId, screenshotBase64); // utilise uniqueId pour nom de fichier
                } else {
                    attemptFallbackScreenshot(result);
                }
            } catch (Exception e) {
                extentTest.get().warning("Échec de la capture: " + e.getMessage());
            }
        } else {
            System.out.println("[DEBUG] Screenshot déjà pris pour : " + uniqueId);
        }
    }
    
    private String getUniqueTestId(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        String params = "";
        if (result.getParameters() != null && result.getParameters().length > 0) {
            params = Arrays.stream(result.getParameters())
                    .map(p -> p == null ? "null" : p.toString().replaceAll("[^a-zA-Z0-9]", "_"))
                    .reduce((a, b) -> a + "_" + b).orElse("");
        }

        return className + "_" + methodName + (params.isEmpty() ? "" : "_" + params);
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