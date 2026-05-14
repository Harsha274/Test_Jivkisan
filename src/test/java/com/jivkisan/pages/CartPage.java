package com.jivkisan.pages;
 
import org.openqa.selenium.By;


import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
 
public class CartPage {

    private final WebDriver driver;

    private final WebDriverWait wait;
 
    // Locators

    private final By allProductsLink = By.xpath("/html/body/header/div/nav/a[2]");

    private final By productsGrid = By.id("all-products-grid");

    private final By productCards = By.xpath("//*[@id='all-products-grid']/div");

    // More robust locators for the cart button

    private final By cartIconBtn = By.xpath("//*[@id=\"myCartBtn\"]"); 

    private final By cartItemsInMenu = By.cssSelector("#cartItemsContainer");

    private final By subtotalElement = By.id("cartSubtotal");

    private final By authModal = By.id("authModal");
 
    public CartPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    }
 
    public void navigateToAllProducts() {

        try {

            wait.until(ExpectedConditions.invisibilityOfElementLocated(authModal));

        } catch (Exception e) { }
 
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(allProductsLink));

        try {

            link.click();

        } catch (Exception e) {

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);

        }

        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(productsGrid));

    }
 
    public List<String> addAnyTwoProducts() {

        List<String> addedProductNames = new ArrayList<>();

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));

        List<WebElement> cards = driver.findElements(productCards);
 
        for (int i = 0; i < 2 && i < cards.size(); i++) {

            WebElement card = cards.get(i);

            String name = card.findElement(By.tagName("h3")).getText();

            addedProductNames.add(name);
 
            WebElement addButton = card.findElement(By.xpath(".//button[contains(translate(., 'ADD', 'add'), 'add')]"));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addButton);

            try {

                addButton.click();

            } catch (Exception e) {

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);

            }

            System.out.println("LOG: Added to cart -> " + name);

            try { Thread.sleep(1500); } catch (InterruptedException e) { }

        }

        return addedProductNames;

    }
 
    public void openCartMenu() throws InterruptedException {

        // Scroll to top first to ensure header elements are not intercepted by floating elements

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");

        // Find cart button using a more flexible approach

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cartIconBtn));

        Thread.sleep(2000);

        try {

            btn.click();

        } catch (Exception e) {

            // Fallback to JS click if the icon is physically obstructed

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        }

        // Wait for cart content to be visible

        wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalElement));

    }

 
    public void printCartSummary() {

        System.out.println("\n========== Jivkisan Bill ==========");

        List<WebElement> items = driver.findElements(cartItemsInMenu);

        if (items.isEmpty()) {

            System.out.println("No items found in the cart UI!");

        }
 
        
 
        String totalValue = driver.findElement(subtotalElement).getText();

        System.out.println("----------------------------------");

        System.out.println("FINAL TOTAL VALUE: " + totalValue);

        System.out.println("==================================\n");

    }
    
    // Add these methods inside the CartPage class CART PERSISTENCE CHECKK
    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }
 
    public void logout() {
        // Assuming there is a profile/logout button. If not, we use JS to clear session or click logout.
        // Based on common patterns, clicking the profile and then logout or a specific logout ID.
        try {
            WebElement profile = driver.findElement(By.id("profileName"));
            profile.click();
            driver.findElement(By.id("logoutBtn")).click(); // Adjust ID if different
        } catch (Exception e) {
            // Fallback: trigger logout via script if UI button is tricky
            ((JavascriptExecutor) driver).executeScript("localStorage.clear(); sessionStorage.clear(); window.location.reload();");
        }
    }
    //END
    
    public void updateUnitsRandomlyForSellerProducts() {
        // Wait for products to load
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        
        // Locate all "Manage Stock" inputs (these only exist for products owned by the logged-in seller)
        // Based on your HTML: id^='update-stock-'
        By ownerInputLocator = By.cssSelector("input[id^='update-stock-']");
        
        List<WebElement> stockInputs = driver.findElements(ownerInputLocator);
        Random rand = new Random();

        if (stockInputs.isEmpty()) {
            System.out.println("LOG: No products found belonging to this seller.");
            return;
        }

        for (WebElement input : stockInputs) {
            try {
                // Generate random units between 1 and 149
                int randomUnits = rand.nextInt(149) + 1;

                // Scroll to the input to ensure it's interactable
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
                
                input.clear();
                input.sendKeys(String.valueOf(randomUnits));

                // Find the associated "Update" button next to this specific input
                // Logic: It's the button immediately following the input
                WebElement updateBtn = input.findElement(By.xpath("./following-sibling::button"));
                updateBtn.click();

                System.out.println("LOG: Successfully updated item ID [" + input.getAttribute("id") + "] to " + randomUnits + " units.");
                
                // Brief wait for the toast notification/Firebase update
                Thread.sleep(1000); 
                
            } catch (Exception e) {
                System.out.println("LOG: Failed to update a specific product: " + e.getMessage());
            }
        }
    }
    private final By searchAllProductsInput = By.cssSelector("#searchAllProducts");
    private final By searchOrganicProductsInput = By.cssSelector("#searchOrganicProducts");
    private final By organicOnlyLink = By.xpath("//a[contains(text(),'Organic Only')]"); // Adjust XPath based on your UI
    // Add these methods to CartPage.java
    public void navigateToOrganicOnly() {
   	    WebElement link = wait.until(ExpectedConditions.elementToBeClickable(organicOnlyLink));
   	    link.click();
   	}

   	public void searchProduct(String panelType, String productName) {
   	    By locator = panelType.equalsIgnoreCase("Organic") ? searchOrganicProductsInput : searchAllProductsInput;
   	    WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
   	    searchBox.clear();
   	    searchBox.sendKeys(productName);
   	}

   	public boolean isProductVisible(String productName) {
   	    // 1. Add a small sleep or wait to allow the search filter to process
   	    try { Thread.sleep(1000); } catch (InterruptedException e) { }

   	    // 2. Use a more flexible XPath that looks for the text anywhere in the grid
   	    // This ignores specific div structures that might change between panels
   	    String dynamicXPath = "//*[contains(@id, 'grid')]//h3[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + productName.toLowerCase() + "')]";
   	    
   	    try {
   	        WebElement product = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
   	        return product.isDisplayed();
   	    } catch (Exception e) {
   	        System.out.println("LOG: Product '" + productName + "' not found in DOM.");
   	        return false;
   	    }
   	}
    }



    
    


 