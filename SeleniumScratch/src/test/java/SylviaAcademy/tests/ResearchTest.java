package SylviaAcademy.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import SylviaAcademy.base.BaseTest;
import SylviaAcademy.pages.DashboardPage;
import SylviaAcademy.utils.ConfigReader;

import java.time.Duration;
import java.util.List;

public class ResearchTest extends BaseTest {

    @Test(groups = "smoke", retryAnalyzer = SylviaAcademy.resources.Retry.class)
    public void testProductsDisplayedOnDashboard() {
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        DashboardPage dashboard = new DashboardPage(getDriver());
        Assert.assertTrue(dashboard.isAt(), "Not redirected to dashboard");

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".card h5 b")));

        List<WebElement> products = getDriver().findElements(By.cssSelector(".card h5 b"));
        Assert.assertFalse(products.isEmpty(), "No products displayed on dashboard");
    }
}
