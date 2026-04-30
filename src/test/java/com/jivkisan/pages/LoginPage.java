package com.jivkisan.pages;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
 
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
 
    // Locators based on JivKisan index5.html
    private final By loginRegisterBtn = By.id("showAuthModalBtn");
    private final By emailField = By.id("loginEmail");
    private final By passwordField = By.id("loginPassword");
    private final By submitLoginBtn = By.cssSelector("#loginForm button[type='submit']");
    private final By profileName = By.id("profileName");
    private final By adminPanelBtn = By.id("adminPanelBtn");
 
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
 
    public void openApp(String url) {
        driver.get(url);
    }
 
    public void clickLoginRegister() {
        driver.findElement(loginRegisterBtn).click();
    }
 
    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(submitLoginBtn).click();
    }
 
    public String getLoggedInUserName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(profileName));
        return driver.findElement(profileName).getText();
    }
 
    public boolean isAdminPanelVisible() {
        try {
            // Wait briefly to see if it appears (it's hidden by default in HTML)
            Thread.sleep(1000);
            return driver.findElement(adminPanelBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
 