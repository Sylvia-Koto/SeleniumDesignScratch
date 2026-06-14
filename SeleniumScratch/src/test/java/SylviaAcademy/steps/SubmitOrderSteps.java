package SylviaAcademy.steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import SylviaAcademy.factory.DriverFactory;
import SylviaAcademy.factory.DriverManager;
import SylviaAcademy.pages.CartPage;
import SylviaAcademy.pages.DashboardPage;
import SylviaAcademy.pages.LoginPage;
import SylviaAcademy.utils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SubmitOrderSteps {

    private WebDriver driver;
    private DashboardPage dashboardPage;
    private CartPage cartPage;

    @Given("I am logged in with valid credentials")
    public void iAmLoggedInWithValidCredentials() {
        DriverFactory.createDriver(ConfigReader.get("browser"));
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.get("url"));
        new LoginPage(driver).login(ConfigReader.get("username"), ConfigReader.get("password"));
        dashboardPage = new DashboardPage(driver);
    }

    @When("I add the product {string} to my cart")
    public void iAddTheProductToMyCart(String productName) {
        dashboardPage.addProductToCart(productName);
    }

    @And("I navigate to the cart")
    public void iNavigateToTheCart() {
        dashboardPage.goToCart();
        cartPage = new CartPage(driver);
    }

    @Then("the product {string} should be visible in the cart")
    public void theProductShouldBeVisibleInTheCart(String productName) {
        Assert.assertTrue(cartPage.isProductInCart(productName),
                productName + " not found in cart");
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
