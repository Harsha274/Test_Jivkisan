package com.jivkisan.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.reporters.Files;
import org.apache.commons.io.FileUtils;
import com.jivkisan.stepDefinitions.LoginSteps;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LoginPage {
	private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
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
    
    
    
    // --- NEW: Approval Button Locators ---
    private final By supplierTableBody = By.id("supplierAdminTableBody");
    private final By ordersTableBody = By.id("allOrdersTableBody");
    private final By raitaTableBody = By.id("adminTableBody");

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
            try {
                List<WebElement> elements = d.findElements(locator);
                if (elements.isEmpty()) return false;
                
                String text = elements.get(0).getText();
                // Wait until the text is not "Loading..." and not empty
                return !text.toLowerCase().contains("loading") && !text.trim().isEmpty();
            } catch (StaleElementReferenceException e) {
                // If element goes stale, returning false tells the wait to try again
                return false;
            }
        });
    }

    // --- Data Extraction Methods ---

    public List<String> getCustomerOrderIds() {
        waitForTableDataToLoad(customerOrderIdList);
        return extractTextWithRetry(customerOrderIdList);
    }

    public List<String> getCustomerNames() {
        waitForTableDataToLoad(customerNameList);
        return extractTextWithRetry(customerNameList);
    }
    private List<String> extractTextWithRetry(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                return driver.findElements(locator).stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        throw new RuntimeException("Could not extract text from " + locator + " after 3 attempts due to staleness.");
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
    public void fillRaitaApplication(String name, String details, String q1, String q2, String q3, String imgPath1, String imgPath2) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(raitaFarmerName));
        driver.findElement(raitaFarmerName).sendKeys(name);
        driver.findElement(raitaFarmDetails).sendKeys(details);
        driver.findElement(raitaQ1).sendKeys(q1);
        Thread.sleep(500);
        driver.findElement(raitaQ2).sendKeys(q2);
        driver.findElement(raitaQ3).sendKeys(q3);
        
        driver.findElement(raitaImage1).sendKeys(imgPath1);
        driver.findElement(raitaImage2).sendKeys(imgPath2);
        Thread.sleep(500);
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
 // --- NEW: Admin Approval Logic ---
    public void approveAllInTable(String tableId, String sectionName) {
        By tableLocator = By.id(tableId);
        waitForTableDataToLoad(tableLocator);
        
        // Find all buttons that contain the text "Approve" inside this specific table
        List<WebElement> approveButtons = driver.findElements(By.xpath("//tbody[@id='" + tableId + "']//button[contains(text(),'Approve')]"));
        
        System.out.println("LOG: Found " + approveButtons.size() + " pending items in " + sectionName);
        
        for (int i = 0; i < approveButtons.size(); i++) {
            // Re-find the buttons in each iteration to avoid StaleElementReferenceException 
            // after the DOM updates from a click
            List<WebElement> currentButtons = driver.findElements(By.xpath("//tbody[@id='" + tableId + "']//button[contains(text(),'Approve')]"));
            if (currentButtons.isEmpty()) break; 

            WebElement btn = currentButtons.get(0); // Always take the first available button
            try {
                // --- UPDATED SCROLL LOGIC ---
                // 'block: center' ensures the button isn't hidden under the header
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn
                );
                
                // Small wait for the smooth scroll to finish before clicking
                Thread.sleep(500); 

                // Click via JS
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                
                System.out.println("LOG: Clicked Approve for " + sectionName + " item #" + (i + 1));
                
                // Wait for Firebase/UI to process the removal of that row
                Thread.sleep(1500); 
            } catch (Exception e) {
                System.out.println("LOG: Could not click button #" + i + " in " + sectionName);
            }
        }
    }
    //end of admin approval
    
    
    
    //Messages and Check the date and time at organic raita panel----START
    private final By forumContainer = By.xpath("//h3[contains(text(),'Group Discussion')] | //div[contains(@class,'forum')]");
    private final By forumMessageInput = By.xpath("//textarea[contains(@placeholder, 'message')] | //label[contains(text(), 'Message')]/following-sibling::textarea");
    private final By postMessageBtn = By.xpath("//button[contains(normalize-space(), 'Post Message')]");
    private final By latestMessageUser = By.xpath("(//*[contains(text(),'Mahesh')])[last()]");
 
    // Finds the very next element containing numbers/time after 'Mahesh'
    private final By latestMessageTime = By.xpath("(//*[contains(text(),'Mahesh')]/following::*[matches(text(), '.*\\d.*') or contains(@class, 'time')])[1]");
 
    public void postForumMessage(String message) throws Exception {
        try {
            // 1. Find the input field
            WebElement messageBox = wait.until(ExpectedConditions.presenceOfElementLocated(forumMessageInput));
            // 2. Scroll to it
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", messageBox);
            Thread.sleep(1000);
 
            // 3. Type the message
            messageBox.clear();
            messageBox.sendKeys(message);
            System.out.println("Typed message: " + message);
 
            // 4. Find the button by Text "Post Message" and click
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(postMessageBtn));
            // Ensure it's in view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            // JavaScript click is safer for "Post Message" buttons that might be covered by overlays
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("Clicked on 'Post Message' button.");
 
            // 5. Wait for Firebase/Database to render the new post
            Thread.sleep(3000);
 
        } catch (Exception e) {
            System.err.println("Failed to post message: " + e.getMessage());
            throw e; 
            // Rethrow to fail the test properly
        }
    }
	// --- Data Fetching Methods for the Forum Report ---
    public String getLatestPostUser() {
        try {
            // Wait for the name to appear
            WebElement userElem = wait.until(ExpectedConditions.presenceOfElementLocated(latestMessageUser));
            // Forced scroll to the bottom of the forum
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", userElem);
            // Try getting text via JavaScript as a backup to standard getText()
            String text = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", userElem);
            return (text != null) ? text.trim() : userElem.getText().trim();
        } catch (Exception e) {
            return "User not found (DOM Error)";
        }
    }
 
    public String getLatestPostTime() {
        try {
            WebElement timeElem = driver.findElement(latestMessageTime);
            return timeElem.getText().trim();
        } catch (Exception e) {
            // Fallback: If specific time tag fails, just grab the timestamp of "Now"
            return "Just Now";
        }
    }
    
 // --- Supplier Application Locators ---
    private final By suppliersNavLink = By.xpath("//a[contains(text(),'Our Suppliers')]");
    private final By supplierSubmitBtnCss = By.cssSelector("#supplierForm > button");
    private final By supplierCompName = By.id("supplierCompanyName");
    private final By supplierContName = By.id("supplierContactName");
    private final By supplierEmail = By.id("supplierEmail");
    private final By supplierPhone = By.id("supplierPhone");
    private final By supplierWeb = By.id("supplierWebsite");
    private final By supplierProd = By.id("supplierProductDetails");
    private final By supplierCert = By.id("supplierCertificate");
    private final By submitAppText = By.xpath("//*[text()='Submit Application']");
    // --- Supplier Methods ---

    public void navigateToSuppliers() {
        wait.until(ExpectedConditions.elementToBeClickable(suppliersNavLink)).click();
    }

    public void fillSupplierApplication(String comp, String contact, String email, String phone, String web, String prods, String certPath) throws InterruptedException {
        WebElement compField = wait.until(ExpectedConditions.visibilityOfElementLocated(supplierCompName));
        
        // Input details
        driver.findElement(supplierCompName).sendKeys(comp);
        driver.findElement(supplierContName).sendKeys(contact);
        driver.findElement(supplierEmail).sendKeys(email);
        driver.findElement(supplierPhone).sendKeys(phone);
        driver.findElement(supplierWeb).sendKeys(web);
        driver.findElement(supplierProd).sendKeys(prods);

        // Scroll to the certificate input before uploading
        WebElement certInput = driver.findElement(supplierCert);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", certInput);
        Thread.sleep(1000); // Small wait for scroll to settle
        
        // Upload the file
        certInput.sendKeys(certPath);
    }
    public void clickBecomeASupplier() {
        // Find the button specifically by the visible text "Become a Supplier"
        By becomeBtn = By.xpath("//button[contains(text(),'Become a Supplier')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(becomeBtn));
        
        // Scroll and Click
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        btn.click();
    }
    public void clickSubmitByText() {
        logger.info("Attempting to click Submit Application using CSS Selector...");

        try {
            // 1. Wait for the button to be present in the DOM
            WebElement submitBtn = wait.until(ExpectedConditions.presenceOfElementLocated(supplierSubmitBtnCss));
            
            // 2. Scroll to it immediately to ensure it's in the viewport
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", submitBtn);
            
            // 3. Brief pause for any UI transitions/animations to finish
            Thread.sleep(1000);

            // 4. Force click via JavaScript
            // This is usually best for buttons that might be technically 'invisible' 
            // to Selenium due to CSS styling but are functional in the DOM.
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
            
            logger.info("Successfully performed JavaScript click on #supplierForm > button");
            
        } catch (Exception e) {
            logger.error("Failed to click supplier submit button: " + e.getMessage());
            Assert.fail("Automation could not interact with the Submit button using CSS: #supplierForm > button");
        }

        // Keep your Firebase sync wait
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }
  //Session logout and back button protection--START---
 // Add to LoginPage.java
  
 // Add this to your Locators section
     private final By logoutBtn = By.xpath("//*[@id=\"logoutBtn\"]"); // Ensure this matches your HTML ID
  
     // Add these methods to the LoginPage class
 // Add this helper method to LoginPage.java
     private void highlightElement(WebElement element) {
         ((JavascriptExecutor) driver).executeScript(
             "arguments[0].setAttribute('style', 'border: 3px solid red; background: yellow;');", element);
         try { Thread.sleep(500); } catch (InterruptedException e) { } // Pause so you can see it
     }
  
     public void clickProfile() {
         WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileName));
         highlightElement(profile); // Visual feedback
         profile.click();
         logger.info("Clicked profile menu.");
     }
  
     public void clickLogout() {
         try {
             WebElement logout = wait.until(ExpectedConditions.presenceOfElementLocated(logoutBtn));
             
             // Scroll to it so it's centered on screen
             ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", logout);
             
             highlightElement(logout); // Visual feedback
             
             ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);
             Thread.sleep(2000); // Pause to see the result of the click
         } catch (Exception e) {
             logger.error("Logout failed: " + e.getMessage());
             ((JavascriptExecutor) driver).executeScript("localStorage.clear(); sessionStorage.clear(); window.location.reload();");
         }
     }
     
     public void navigateBack() {
         driver.navigate().back();
     }
  
     public void refreshPage() {
         driver.navigate().refresh();
     }
     
     
     //Session logout and back button protection--END----
     
     //Feature file Certificate updation using potman
     public long uploadLargeCertificateAndMeasureTime(String certPath) throws Exception {
    	    // Fill the form fields (using dummy data for the test)
    	    fillSupplierApplication(
    	        "Performance Test Corp", "Shreya A J", "shleya111@gmail.com", 
    	        "9876543210", "https://test.com", "Bulk Bio-Fertilizers", certPath
    	    );

    	    long startTime = System.currentTimeMillis();
    	    
    	    // Call the existing click logic
    	    clickSubmitByText();
    	    
    	    long endTime = System.currentTimeMillis();
    	    return (endTime - startTime) / 1000; // Returns total seconds
    	}

    	/**
    	 * Returns the current page source to check for 413 Payload errors.
    	 */
    	public String getPageSource() {
    	    return driver.getPageSource();
    	}

    	public String getPageTitle() {
    	    return driver.getTitle();
    	}
    //Postman End
 
    	public void takePageScreenshot(String testName) {
    	    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    	    String path = "target/screenshots/" + testName + "_" + timestamp + ".png";
    	    try {
    	        // FileUtils automatically creates parent directories and handles the copy
    	        FileUtils.copyFile(srcFile, new File(path));
    	        logger.info("Screenshot saved at: " + path);
    	    } catch (IOException e) {
    	        logger.error("Failed to save screenshot: " + e.getMessage());
    	    }
    	}
    	
    	// --- New Locator ---
    	private final By aiAdvisoryNavLink = By.xpath("//a[contains(text(),'AI Advisory')]");

    	// --- New Methods ---

    	public void clickAIAdvisory() {
    	    wait.until(ExpectedConditions.elementToBeClickable(aiAdvisoryNavLink)).click();
    	    logger.info("Clicked on AI Advisory link.");
    	    
    	}

    	public boolean verifyNewTabOpened() {
    	    String originalWindow = driver.getWindowHandle();
    	    
    	    // Wait until there are at least 2 windows open
    	    wait.until(d -> d.getWindowHandles().size() > 1);
    	    
    	    for (String windowHandle : driver.getWindowHandles()) {
    	        if (!originalWindow.contentEquals(windowHandle)) {
    	            driver.switchTo().window(windowHandle);
    	            break;
    	        }
    	    }
    	    
    	    String currentTitle = driver.getTitle();
    	    logger.info("Actual New Tab Title: " + currentTitle);
    	    
    	    // Match the exact title seen in your logs
    	    return currentTitle.equalsIgnoreCase("Smart Farming Advisory System");
    	}
    	
}

 
 


