package SylviaAcademy.base;

import org.openqa.selenium.WebDriver;
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
}
