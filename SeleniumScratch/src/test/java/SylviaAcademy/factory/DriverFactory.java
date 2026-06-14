package SylviaAcademy.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        boolean headless = "true".equalsIgnoreCase(System.getProperty("headless"));
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                driver = new ChromeDriver(options);
                break;
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("--headless");
                driver = new FirefoxDriver(options);
                break;
            }
            case "edge":
                driver = new EdgeDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        DriverManager.setDriver(driver);
        return driver;
    }
}
