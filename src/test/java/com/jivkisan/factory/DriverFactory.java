package com.jivkisan.factory;
 
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import com.jivkisan.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
 
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
 
    private DriverFactory() {}
 
    /**
     * Initializes the driver specifically for Microsoft Edge
     */
    public static void initDriver() {
        String browser = ConfigReader.get("browser");
        if (browser != null && browser.equalsIgnoreCase("edge")) {
            // Points to the driver shown in your screenshot
            String driverPath = System.getProperty("user.dir") + "/drivers/msedgedriver.exe";
            System.setProperty("webdriver.edge.driver", driverPath);
            
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--start-maximized");
            // Use "--headless=new" if you want to run without seeing the browser window
            if (ConfigReader.getBoolean("headless")) {
                options.addArguments("--headless=new");
            }
            driver.set(new EdgeDriver(options));
        }
        // Set wait time from config.properties
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));
    }
    public static WebDriver getDriver() {
        return driver.get();
    }
 
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}