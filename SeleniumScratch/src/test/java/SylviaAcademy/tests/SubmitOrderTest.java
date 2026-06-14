package SylviaAcademy.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import SylviaAcademy.base.BaseTest;
import SylviaAcademy.pages.CartPage;
import SylviaAcademy.pages.DashboardPage;
import SylviaAcademy.utils.ConfigReader;

public class SubmitOrderTest extends BaseTest {

    private static final String PRODUCT_NAME = "IPHONE 13 PRO";

    @Test(groups = "smoke")
    public void testAddProductToCart() {
        loginPage.login(ConfigReader.get("username"), ConfigReader.get("password"));

        DashboardPage dashboard = new DashboardPage(getDriver());
        Assert.assertTrue(dashboard.isAt(), "Not redirected to dashboard after login");

        dashboard.addProductToCart(PRODUCT_NAME);
        dashboard.goToCart();

        CartPage cart = new CartPage(getDriver());
        Assert.assertTrue(cart.isProductInCart(PRODUCT_NAME), PRODUCT_NAME + " not found in cart");
    }
}
