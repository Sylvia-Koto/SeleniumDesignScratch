package SylviaAcademy.base;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import SylviaAcademy.factory.DriverFactory;
import SylviaAcademy.factory.DriverManager;
import SylviaAcademy.pages.LoginPage;
import SylviaAcademy.utils.ConfigReader;

public class BaseTest {

	protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = ConfigReader.get("browser");
        String url = ConfigReader.get("url");

        DriverFactory.createDriver(browser);
        getDriver().get(url);
        loginPage = new LoginPage(getDriver());
    }
 

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
	    
	    public String getEmailError() {
	        return getDriver().findElement(By.xpath("//input[@id='userEmail']/following-sibling::div[@class='invalid-feedback']/div")).getText();
	    }

	    public String getPasswordError() {
	        return getDriver().findElement(By.xpath("//input[@id='userPassword']/following-sibling::div[@class='invalid-feedback']/div")).getText();
	    }

	    public boolean isErrorMessageDisplayedold() {
	        return getDriver().findElements(By.cssSelector(".toast-message")).size() > 0;
	    }
	    
		public void waitForWebElementToAppear(WebElement findBy) {

			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(findBy));

		}
		
		public void waitForWebElementToLocate(By locBy) {

			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locBy));

		}
		
		public boolean isLoginErrorMessageDisplayed(String expectedMessage) {
		    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
		    try {
		        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//*[contains(text(),'" + expectedMessage + "')]")));
		        return errorMsg.isDisplayed();
		    } catch (TimeoutException e) {
		        return false;
		    }
		    
		    
		}
		
		public static void assertRedirectionToDashboard() {
		    Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/dashboard"), "Redirection échouée après connexion");
		}

		public static void assertNoLoginErrorDisplayed() {
		    Assert.assertFalse(isErrorMessageDisplayed(), "Un message d'erreur est affiché malgré la connexion réussie");
		}

		public static void assertLoginErrorDisplayed() {
		    Assert.assertTrue(VerifyErrorMessageDisplayed(), "Le message d'erreur n'est pas affiché");
		}

		public static boolean isErrorMessageDisplayed() {
		    try {
		        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(2));
		        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-message")));
		        return DriverManager.getDriver().findElement(By.cssSelector(".toast-message")).isDisplayed();
		    } catch (NoSuchElementException | TimeoutException e) {
		        return false;
		    }
		}

		public static boolean VerifyErrorMessageDisplayed() {
		    try {
		        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
		        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-message")));
		        return DriverManager.getDriver().findElement(By.cssSelector(".toast-message")).isDisplayed();
		    } catch (NoSuchElementException | TimeoutException e) {
		        return false;
		    }
		}

		public static String getScreenshot(String testCaseName) throws IOException {
		    File reportsDir = new File(System.getProperty("user.dir") + "//reports//");
		    if (!reportsDir.exists()) {
		        reportsDir.mkdirs();
		    }
		    
		    String fileName = testCaseName + "_" + System.currentTimeMillis() + ".png";
		    String fullPath = System.getProperty("user.dir") + "//reports//" + fileName;
		    
		    try {
		        TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
		        File source = ts.getScreenshotAs(OutputType.FILE);
		        FileUtils.copyFile(source, new File(fullPath));
		        return fullPath;
		    } catch (Exception e) {
		        System.err.println("Échec de la capture d'écran: " + e.getMessage());
		        throw e;
		    }
		}
}
