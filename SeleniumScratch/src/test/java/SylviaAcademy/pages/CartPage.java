package SylviaAcademy.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    @FindBy(css = "h3")
    private List<WebElement> cartItems;

    @FindBy(css = ".btn.btn-primary")
    private WebElement checkoutButton;

    private final WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isProductInCart(String productName) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/cart"));
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> d.findElements(By.tagName("h3")).stream()
                        .anyMatch(e -> e.getText().equalsIgnoreCase(productName)));
        return true;
    }

    public void checkout() {
        checkoutButton.click();
    }
}
