package SylviaAcademy.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {

    @FindBy(css = "div.cart h3")
    private List<WebElement> cartItems;

    @FindBy(css = ".btn.btn-primary")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public boolean isProductInCart(String productName) {
        return cartItems.stream()
                .anyMatch(item -> item.getText().equalsIgnoreCase(productName));
    }

    public void checkout() {
        checkoutButton.click();
    }
}
