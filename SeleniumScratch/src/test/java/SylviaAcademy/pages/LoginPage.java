package SylviaAcademy.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "userPassword")
    private WebElement passwordInput;

    @FindBy(id = "login")
    private WebElement loginButton;

    @FindBy(xpath = "//input[@id='userEmail']/following-sibling::div[@class='invalid-feedback']/div")
    private WebElement emailError;

    @FindBy(xpath = "//input[@id='userPassword']/following-sibling::div[@class='invalid-feedback']/div")
    private WebElement passwordError;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    public String getEmailError() {
        return emailError.getText();
    }

    public String getPasswordError() {
        return passwordError.getText();
    }
}
