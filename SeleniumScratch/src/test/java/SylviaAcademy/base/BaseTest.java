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
import SylviaAcademy.pages.LoginPage;

public class BaseTest {

	  protected WebDriver driver;
	  protected LoginPage loginPage;

	    @Parameters("browser")
	    @BeforeMethod(alwaysRun = true)
	    public void setUp(@Optional("chrome") String browser) {
	        driver = DriverFactory.createDriver(browser);
	        driver.get("https://rahulshettyacademy.com/client");
	        loginPage = new LoginPage(driver); 

	    }

	    @AfterMethod(alwaysRun = true)
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
	    
	    public WebDriver getDriver() {
	        return this.driver; // Assurez-vous que 'driver' est bien déclaré dans BaseTest
	    }
	    
	    public String getEmailError() {
	        return driver.findElement(By.xpath("//input[@id='userEmail']/following-sibling::div[@class='invalid-feedback']/div")).getText();
	    }

	    public String getPasswordError() {
	        return driver.findElement(By.xpath("//input[@id='userPassword']/following-sibling::div[@class='invalid-feedback']/div")).getText();
	    }

	    public boolean isErrorMessageDisplayed() {
	        return driver.findElements(By.cssSelector(".toast-message")).size() > 0;
	    }
	    
		public void waitForWebElementToAppear(WebElement findBy) {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOf(findBy));

		}
		
		public void waitForWebElementToLocate(By locBy) {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locBy));

		}
		
		public boolean isLoginErrorMessageDisplayed(String expectedMessage) {
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		    try {
		        WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//*[contains(text(),'" + expectedMessage + "')]")));
		        return errorMsg.isDisplayed();
		    } catch (TimeoutException e) {
		        return false;
		    }
		    
		    
		}
		
		public static void assertRedirectionToDashboard(WebDriver driver) {
	        Assert.assertTrue(driver.getCurrentUrl().contains("/dashboard"), "Redirection échouée après connexion");
	    }

	    public static void assertNoLoginErrorDisplayed(WebDriver driver) {
	        Assert.assertFalse(isErrorMessageDisplayed(driver), "Un message d'erreur est affiché malgré la connexion réussie");
	    }
	    
	    public static void assertLoginErrorDisplayed(WebDriver driver) {
	        Assert.assertTrue(VerifyErrorMessageDisplayed(driver), "le message d'erreur n'est pas affiché");
	    }

	    public static boolean isErrorMessageDisplayed(WebDriver driver) {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
	            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-message")));
	            return driver.findElement(By.cssSelector(".toast-message")).isDisplayed();
	        } catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
	            return false;
	        }
	    }
	    
	    public static boolean VerifyErrorMessageDisplayed(WebDriver driver) {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-message")));
	            return driver.findElement(By.cssSelector(".toast-message")).isDisplayed();
	        } catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
	            return false;
	        }
	    }
	    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
	        // 1. Vérifier et créer le dossier reports si nécessaire
	        File reportsDir = new File(System.getProperty("user.dir") + "//reports//");
	        if (!reportsDir.exists()) {
	            reportsDir.mkdirs();
	        }
	        
	        // 2. Générer un nom de fichier unique avec timestamp
	        String fileName = testCaseName + "_" + System.currentTimeMillis() + ".png";
	        String fullPath = System.getProperty("user.dir") + "//reports//" + fileName;
	        
	        // 3. Prendre la capture d'écran
	        try {
	            TakesScreenshot ts = (TakesScreenshot) driver;
	            File source = ts.getScreenshotAs(OutputType.FILE);
	            
	            // 4. Sauvegarder le fichier
	            FileUtils.copyFile(source, new File(fullPath));
	            
	            // 5. Retourner le chemin complet pour ExtentReports
	            return fullPath;
	        } catch (Exception e) {
	            System.err.println("Échec de la capture d'écran: " + e.getMessage());
	            throw e; // Relancer l'exception pour la gestion dans le Listener
	        }
	    }
}
