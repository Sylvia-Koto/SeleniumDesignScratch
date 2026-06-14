package SylviaAcademy.base;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import SylviaAcademy.factory.DriverFactory;
import SylviaAcademy.factory.DriverManager;
import SylviaAcademy.pages.LoginPage;
import SylviaAcademy.utils.ConfigReader;

public class BaseTest {

    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.createDriver(ConfigReader.get("browser"));
        getDriver().get(ConfigReader.get("url"));
        loginPage = new LoginPage(getDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    public void waitFor(WebElement element) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitFor(By locator) {
        new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
