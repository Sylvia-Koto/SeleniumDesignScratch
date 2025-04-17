package SylviaAcademy.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import SylviaAcademy.base.BaseTest;
import SylviaAcademy.pages.LoginPage;

public class LoginTest extends BaseTest {

    // === 1. DataProviders séparés pour succès/échec ===
    @DataProvider(name = "validCredentials")
    public Object[][] provideValidCredentials() {
        return new Object[][]{
            {"queentester@gmail.com", "Test@@1234"}  // ✅ Cas de succès
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] provideInvalidCredentials() {
        return new Object[][]{
            // Format: username, password, expectedEmailError, expectedPasswordError
            {"queentester@gmail.com", "invalidPass", null, null}, // ❌ Incorrect Password
            {"", "Test@@1234", "*Test fails", null},       // ❌ Email vide
            {"invalidUser@gmail.com", "invalidPass", null, null}, // ❌ Incorrect Password and email
            {"", "", "*Email is required", "*Password is required"}, // ❌ Champs vides
            {"queentester@gmail.com", "", null, "*Password is required"} // ❌ Mot de passe vide
        };
    }

    // === 2. Test de succès (uniquement pour les credentials valides) ===
    @Test(dataProvider = "validCredentials", groups = "smoke")
    public void testLoginSuccess(String username, String password) {
        loginPage.login(username, password);
        waitForWebElementToLocate(By.cssSelector(".btn.btn-custom[routerlink='/dashboard/']"));
        assertRedirectionToDashboard();
        assertNoLoginErrorDisplayed();
    }

    // === 3. Test d'échec (pour les cas invalides) ===
    @Test(dataProvider = "invalidCredentials")
    public void testLoginFailure(String username, String password, String expectedEmailError, String expectedPasswordError) {
        loginPage.login(username, password);
        
        if (expectedEmailError == null && expectedPasswordError == null) {
            assertLoginErrorDisplayed();
        }
        
        // Vérifications dynamiques des erreurs
        if (expectedEmailError != null) {
            Assert.assertEquals(getEmailError(), expectedEmailError, "Message d'erreur email incorrect");
        }
        if (expectedPasswordError != null) {
            Assert.assertEquals(getPasswordError(), expectedPasswordError, "Message d'erreur mot de passe incorrect");
        }
    }
    
}