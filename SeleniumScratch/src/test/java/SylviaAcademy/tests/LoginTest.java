package SylviaAcademy.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
            {"queentester@gmail.com", "invalidPass", null, "Incorrect password"}, // ❌ Mot de passe incorrect
            {"", "Test@@1234", "Email is required", null},                        // ❌ Email vide
            {"invalidUser@gmail.com", "invalidPass", "Incorrect email", "Incorrect password"}, // ❌ Email + mot de passe incorrects
            {"", "", "Email is required", "Password is required"},                // ❌ Champs vides
            {"queentester@gmail.com", "", null, "Password is required"}           // ❌ Mot de passe vide
        };
    }

    // === 2. Test de succès (uniquement pour les credentials valides) ===
    @Test(dataProvider = "validCredentials", groups = "smoke")
    public void testLoginSuccess(String username, String password) {
        loginPage.login(username, password);
        waitForWebElementToLocate(By.cssSelector(".btn.btn-custom[routerlink='/dashboard/']"));
        assertRedirectionToDashboard(driver);
        assertNoLoginErrorDisplayed(driver);
        

    }

    // === 3. Test d'échec (pour les cas invalides) ===
    @Test(dataProvider = "invalidCredentials")
    public void testLoginFailure(String username, String password, String expectedEmailError, String expectedPasswordError) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        
        // Vérifications dynamiques des erreurs
        if (expectedEmailError != null) {
            Assert.assertEquals(getEmailError(), expectedEmailError, "Message d'erreur email incorrect");
        }
        if (expectedPasswordError != null) {
            Assert.assertEquals(getPasswordError(), expectedPasswordError, "Message d'erreur mot de passe incorrect");
        }
    }
}