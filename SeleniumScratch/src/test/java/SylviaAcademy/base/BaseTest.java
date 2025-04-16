package SylviaAcademy.base;

import java.time.Duration;

import org.openqa.selenium.By;
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

public class BaseTest {

	  protected WebDriver driver;

	    @Parameters("browser")
	    @BeforeMethod
	    public void setUp(@Optional("chrome") String browser) {
	        driver = DriverFactory.createDriver(browser);
	    }

	    @AfterMethod
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
	    
	    public String getEmailError() {
	        return driver.findElement(By.id("error-email")).getText();
	    }

	    public String getPasswordError() {
	        return driver.findElement(By.id("error-password")).getText();
	    }

	    public boolean isErrorMessageDisplayed() {
	        return driver.findElements(By.cssSelector(".error-message")).size() > 0;
	    }
	    
		public void waitForWebElementToAppear(WebElement findBy) {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(findBy));

		}
		
		public boolean isLoginErrorMessageDisplayed(String expectedMessage) {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		    try {
		        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//*[contains(text(),'" + expectedMessage + "')]")));
		        return errorMsg.isDisplayed();
		    } catch (TimeoutException e) {
		        return false;
		    }
		}
}
