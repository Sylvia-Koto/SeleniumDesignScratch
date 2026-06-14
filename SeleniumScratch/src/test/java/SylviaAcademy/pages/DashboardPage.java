package SylviaAcademy.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.urlContains("/dashboard"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addProductToCart(String productName) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfAllElements(productTitles));

        for (int i = 0; i < productTitles.size(); i++) {
            if (productTitles.get(i).getText().equalsIgnoreCase(productName)) {
                productCards.get(i).findElement(By.cssSelector("button.btn.w-10")).click();
                // wait for spinner after adding to cart
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.invisibilityOfElementLocated(
                                By.cssSelector(".ngx-spinner-overlay")));
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    public void goToCart() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(cartLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);
    }
}
