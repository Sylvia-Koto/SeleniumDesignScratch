package SylviaAcademy.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {

    private final WebDriver driver;

    @FindBy(css = ".card h5 b")
    private List<WebElement> productTitles;

    @FindBy(css = ".card")
    private List<WebElement> productCards;

    @FindBy(css = "[routerlink='/dashboard/cart']")
    private WebElement cartLink;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isAt() {
        return driver.getCurrentUrl().contains("/dashboard");
    }

    public void addProductToCart(String productName) {
        for (int i = 0; i < productTitles.size(); i++) {
            if (productTitles.get(i).getText().equalsIgnoreCase(productName)) {
                productCards.get(i).findElement(By.cssSelector("button:last-child")).click();
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    public void goToCart() {
        cartLink.click();
    }
}
