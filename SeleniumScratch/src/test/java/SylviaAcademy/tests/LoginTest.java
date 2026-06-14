package SylviaAcademy.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import SylviaAcademy.base.BaseTest;
import SylviaAcademy.pages.DashboardPage;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][]{
            {"queentester@gmail.com", "Test@@1234"}
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
            // email, password, expectedEmailError, expectedPasswordError
            {"queentester@gmail.com", "invalidPass", null, null},
            {"", "Test@@1234", "*Test fails", null},
            {"invalidUser@gmail.com", "invalidPass", null, null},
            {"", "", "*Email is required", "*Password is required"},
            {"queentester@gmail.com", "", null, "*Password is required"}
        };
    }

    @Test(dataProvider = "validCredentials", groups = "smoke", retryAnalyzer = SylviaAcademy.resources.Retry.class)
    public void testLoginSuccess(String email, String password) {
        loginPage.login(email, password);

        DashboardPage dashboard = new DashboardPage(getDriver());
        waitFor(By.cssSelector(".btn.btn-custom[routerlink='/dashboard/']"));

        Assert.assertTrue(dashboard.isAt(), "Redirect to dashboard failed");
        Assert.assertFalse(isToastVisible(), "Unexpected error toast after successful login");
    }

    @Test(dataProvider = "invalidCredentials", retryAnalyzer = SylviaAcademy.resources.Retry.class)
    public void testLoginFailure(String email, String password, String expectedEmailError, String expectedPasswordError) {
        loginPage.login(email, password);

        if (expectedEmailError == null && expectedPasswordError == null) {
            Assert.assertTrue(isToastVisible(), "Expected error toast not displayed");
        }
        if (expectedEmailError != null) {
            Assert.assertEquals(loginPage.getEmailError(), expectedEmailError, "Wrong email error message");
        }
        if (expectedPasswordError != null) {
            Assert.assertEquals(loginPage.getPasswordError(), expectedPasswordError, "Wrong password error message");
        }
    }

    private boolean isToastVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            WebElement toast = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".toast-message")));
            return toast.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }
}
