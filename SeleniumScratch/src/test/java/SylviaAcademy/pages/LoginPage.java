package SylviaAcademy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
	
	 private WebDriver driver;

	    // Locators
	    private By usernameInput = By.id("userEmail");
	    private By passwordInput = By.id("userPassword");
	    private By loginButton = By.id("login");

	    public LoginPage(WebDriver driver) {
	        this.driver = driver;
	    }

	    // Actions
	    public void login(String username, String password) {
	        driver.findElement(usernameInput).sendKeys(username);
	        driver.findElement(passwordInput).sendKeys(password);
	        driver.findElement(loginButton).click();
	        
	    }    

}
