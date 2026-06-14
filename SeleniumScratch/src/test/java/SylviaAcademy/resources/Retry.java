package SylviaAcademy.resources;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import SylviaAcademy.factory.DriverManager;

public class Retry implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT && shouldRetry(result)) {
            retryCount++;
            // Déplacer ces appels avant le return
            logRetryAttempt(result);
            captureScreenshotOnRetry(result);
            return true;
        }
        return false;
    }

    // Les autres méthodes restent inchangées
    private boolean shouldRetry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            return !throwable.getClass().getName().contains("NoSuchElementException");
        }
        return true;
    }

    private void logRetryAttempt(ITestResult result) {
        String message = String.format("Retry #%d for test %s", 
                retryCount, result.getName());
        System.out.println(message);
        Listeners.getCurrentTest().log(Status.WARNING, message);
    }

    private void captureScreenshotOnRetry(ITestResult result) {
        try {
            String screenshotBase64 = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
            Listeners.getCurrentTest().log(Status.INFO, "Screenshot before retry", 
                MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}