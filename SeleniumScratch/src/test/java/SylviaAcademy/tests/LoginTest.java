package SylviaAcademy.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import SylviaAcademy.base.BaseTest;
import SylviaAcademy.pages.LoginPage;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginCredentials")
    public Object[][] getLoginData() {
        return new Object[][]{
            {"queentester@gmail.com", "Test@@1234", true},  // ✅ Valide
            {"invalidUser", "invalidPass", false},          // ❌ Faux identifiants
            {"", "", false},                                // ❌ Vide
            {"queentester@gmail.com", "", false},           // ❌ Mot de passe manquant
            {"", "Test1234", false}                          // ❌ Email manquant
        };
    }

    @Test(dataProvider = "loginCredentials", groups = "smoke")
    public void testLogin(String username, String password, boolean isSuccessExpected) {
        driver.get("https://rahulshettyacademy.com/client");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        boolean loginSuccess;
        try {
            // 🟢 Attendre un élément spécifique du dashboard pour confirmer la connexion
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn.btn-custom[routerlink='/dashboard/']")));
            loginSuccess = true;
        } catch (TimeoutException e) {
            loginSuccess = false;
        }

        if (isSuccessExpected) {
            Assert.assertTrue(loginSuccess, "Login should succeed with valid credentials.");
        } else {
            Assert.assertFalse(loginSuccess, "Login should fail with invalid credentials.");
        }
    }
}