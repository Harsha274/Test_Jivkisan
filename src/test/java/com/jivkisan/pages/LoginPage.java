package com.jivkisan.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Locators (Based on your index5.html structure) ---
    private final By loginRegisterBtn = By.id("showAuthModalBtn");
    private final By emailField = By.id("loginEmail");
    private final By passwordField = By.id("loginPassword");
    private final By submitLoginBtn = By.cssSelector("#loginForm button[type='submit']");
    private final By profileName = By.id("profileName");
    private final By adminPanelBtn = By.id("adminPanelBtn");
    private final By adminDashboardHeader = By.xpath("//h2[text()='Admin Panel']");
    
    // --- Raita Membership Application Locators ---
    private final By organicRaitaNavLink = By.xpath("//a[contains(text(),'Organic Raita')]");
    private final By applyRaitaBtn = By.id("applyRaitaBtn");
    private final By raitaFarmerName = By.id("raitaFarmerName");
    private final By raitaFarmDetails = By.id("raitaFarmDetails");
    private final By raitaQ1 = By.id("raitaQ1");
    private final By raitaQ2 = By.id("raitaQ2");
    private final By raitaQ3 = By.id("raitaQ3");
    private final By raitaImage1 = By.id("raitaImage1");
    private final By raitaImage2 = By.id("raitaImage2");
    private final By submitBtnByName = By.xpath("//button[contains(text(),'Submit Application')]");

    // --- New Locators for Discussion Forum Scenario ---
    private final By raitaApprovedContent = By.id("raitaApprovedContent");
    private final By raitaNotApprovedContent = By.id("raitaNotApprovedContent");
    
    // --- Table Column Locators ---
    // Customer Orders Table
    private final By customerOrderIdList = By.cssSelector("#allOrdersTableBody tr td:nth-child(1)"); 
    private final By customerNameList = By.cssSelector("#allOrdersTableBody tr td:nth-child(3)"); 
    //Raita Applicaion
    private final By supplierCompanyNameList1 = By.cssSelector("#supplierAdminTableBody tr td:nth-child(1)");
    private final By raitaApplicantNameList1 = By.cssSelector("#adminTableBody tr td:nth-child(2)");
    
    
    
    // Supplier Applications Table
    private final By supplierCompanyNameList = By.cssSelector("#supplierAdminTableBody tr td:nth-child(1)");
    
    // Raita Applications Table
    private final By raitaApplicantNameList = By.cssSelector("#adminTableBody tr td:nth-child(2)");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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

    public void clickAdminPanel() {
        wait.until(ExpectedConditions.elementToBeClickable(adminPanelBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(adminDashboardHeader));
    }

    public boolean isAdminPanelVisible() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(adminPanelBtn)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    
    
    /**
     * INTERNAL SYNC LOGIC:
     * This method ensures that we wait until the Firebase data has actually rendered 
     * in the table, replacing the "Loading..." placeholder rows.
     */
    private void waitForTableDataToLoad(By locator) {
        wait.until(d -> {
            List<WebElement> elements = d.findElements(locator);
            if (elements.isEmpty()) return false;
            String text = elements.get(0).getText();
            // Wait until the text is not "Loading..." and not empty
            return !text.toLowerCase().contains("loading") && !text.trim().isEmpty();
        });
    }

    // --- Data Extraction Methods ---

    public List<String> getCustomerOrderIds() {
        waitForTableDataToLoad(customerOrderIdList);
        return driver.findElements(customerOrderIdList).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    public List<String> getCustomerNames() {
        waitForTableDataToLoad(customerNameList);
        return driver.findElements(customerNameList).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    public List<String> getSupplierCompanyNames() {
        waitForTableDataToLoad(supplierCompanyNameList1);
        return driver.findElements(supplierCompanyNameList1).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    public List<String> getRaitaApplicantNames() {
        waitForTableDataToLoad(raitaApplicantNameList1);
        return driver.findElements(raitaApplicantNameList1).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }
    
    //Raita Application start
    public void navigateToOrganicRaita() {
        wait.until(ExpectedConditions.elementToBeClickable(organicRaitaNavLink)).click();
    }
    
    

    public void clickApplyRaita() {
        wait.until(ExpectedConditions.elementToBeClickable(applyRaitaBtn)).click();
    }

    // UPDATED: Now takes two separate image paths
    public void fillRaitaApplication(String name, String details, String q1, String q2, String q3, String imgPath1, String imgPath2) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(raitaFarmerName));
        driver.findElement(raitaFarmerName).sendKeys(name);
        driver.findElement(raitaFarmDetails).sendKeys(details);
        driver.findElement(raitaQ1).sendKeys(q1);
        driver.findElement(raitaQ2).sendKeys(q2);
        driver.findElement(raitaQ3).sendKeys(q3);
        
        driver.findElement(raitaImage1).sendKeys(imgPath1);
        driver.findElement(raitaImage2).sendKeys(imgPath2);
    }

    public void submitRaitaForm() {
        // Wait for the button containing the text "Submit Application"
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitBtnByName));
        
        // Scroll to it
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        
        // Use JavaScript click to ensure the 'onsubmit' event triggers properly
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        
        // IMPORTANT: Allow 5 seconds for Firebase to process images and upload data
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    //Raita Application end

    
    
    
    
  //admin forum access
 // Add these methods inside the LoginPage class//approved pGE //
    public boolean isRaitaApprovedPageDisplayed() {
        try {
            // Wait up to 5 seconds for Firebase to sync and UI to toggle
            return wait.until(ExpectedConditions.visibilityOfElementLocated(raitaApprovedContent)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isApplyMembershipPageDisplayed() {
        try {
            return driver.findElement(raitaNotApprovedContent).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

//admin forum access END

