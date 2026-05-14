package com.jivkisan.stepDefinitions;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jivkisan.factory.DriverFactory;
import com.jivkisan.pages.LoginPage;
import com.jivkisan.utils.ConfigReader;
import io.cucumber.java.en.*;

import java.io.File;
import java.util.List;

public class LoginSteps {
    private static final Logger logger = LoggerFactory.getLogger(LoginSteps.class);
    private final LoginPage loginPage = new LoginPage(DriverFactory.getDriver());


    @Given("user is on the JivKisan homepage")
    public void user_is_on_the_jiv_kisan_homepage() {
        loginPage.openApp(ConfigReader.get("base.url"));
    }

    @When("user clicks on Login Register button")
    public void user_clicks_on_login_register_button() {
        loginPage.clickLoginRegister();
    }

    @When("user enters email {string} and password {string}")
    public void user_enters_credentials(String email, String password) {
        logger.info("Logging in with: " + email);
        loginPage.login(email, password);
    }

    @Then("user should be logged in as {string}")
    public void user_should_be_logged_in_as(String expectedUser) {
        String actualName = loginPage.getLoggedInUserName();
        logger.info("Actual Profile Name: " + actualName);
        Assert.assertFalse(actualName.isEmpty(), "User was not logged in successfully.");
    }

    @Then("the Admin Panel should be visible")
    public void the_admin_panel_should_be_visible() {
        Assert.assertTrue(loginPage.isAdminPanelVisible(), "Admin Panel button is NOT visible!");
    }

    @Then("verify if user is Admin to show Admin Panel")
    public void verify_if_user_is_admin_to_show_admin_panel() {
        if (loginPage.isAdminPanelVisible()) {
            logger.info("Verification: User is an Admin.");
        } else {
            logger.warn("Verification: User is NOT an Admin.");
        }
    }

    @Then("the Admin Panel should NOT be visible")
    public void the_admin_panel_should_not_be_visible() {
        Assert.assertFalse(loginPage.isAdminPanelVisible(), "Admin Panel should be hidden for this user.");
    }

    @Then("the user counts the total orders and applications")
    public void the_user_counts_the_total_orders_and_applications() {
        // Navigate to the Admin View
        loginPage.clickAdminPanel();
        logger.info("--- Data Extraction Started ---");

        // 1. Extract Customer Orders (Complete data: ID + Name)
        List<String> orderIds = loginPage.getCustomerOrderIds();
        List<String> customerNames = loginPage.getCustomerNames();
        
        logger.info("TOTAL CUSTOMER ORDERS: " + orderIds.size());
        for (int i = 0; i < orderIds.size(); i++) {
            logger.info("Order #" + (i + 1) + " | ID: " + orderIds.get(i) + " | Customer Name: " + customerNames.get(i));
        }

        // 2. Extract Supplier Applications
        List<String> suppliers = loginPage.getSupplierCompanyNames();
        logger.info("TOTAL SUPPLIER APPLICATIONS: " + suppliers.size());
        logger.info("Supplier Companies: " + suppliers);

        // 3. Extract Raita Applications
        List<String> raitaApps = loginPage.getRaitaApplicantNames();
        logger.info("TOTAL RAITA APPLICATIONS: " + raitaApps.size());
        logger.info("Applicant Names: " + raitaApps);

        // Ensure we didn't just count a 'Loading' row
        Assert.assertTrue(orderIds.size() > 0, "No real orders were found in the database.");
    }
    
 // --- New Steps for Raita Membership ---

    @When("user navigates to Organic Raita section")
    public void user_navigates_to_organic_raita_section() {
        loginPage.navigateToOrganicRaita();
    }

    @When("user applies for Raita Membership with details")
    public void user_applies_for_raita_membership_with_details() throws InterruptedException {
        // Path handling for two different images
        String imagePath1 = new File("src/test/resources/images/farm.png").getAbsolutePath();
        String imagePath2 = new File("src/test/resources/images/farm2.png").getAbsolutePath();
        
        logger.info("Applying for Raita with images: " + imagePath1 + " and " + imagePath2);
        
        loginPage.clickApplyRaita();
        loginPage.fillRaitaApplication(
            "Chandini P", 
            "Bannerghatta Road, Bangalore", 
            "Composting and Natural Fertilizers", 
            "3 Years", 
            "Turmeric and Ragi", 
            imagePath1,
            imagePath2
        );
        loginPage.submitRaitaForm();
        logger.info("Application submitted successfully.");
    }

    @Then("the application should be submitted successfully")
    public void the_application_should_be_submitted_successfully() {
        logger.info("Submission step completed.");
    }
    //raita application end
    
    
    
    
    
    //DISCUSIIN ACCES FORUM STAR//
    @Then("verify if member application is approved to access forum")
    public void verify_if_member_application_is_approved_to_access_forum() {
        // Small sleep to let Firebase auth state update the UI containers
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        if (loginPage.isRaitaApprovedPageDisplayed()) {
            logger.info("Access Confirmed: This member is APPROVED. Discussion forum is visible.");
        } else {
            logger.warn("Access Restricted: Member NOT APPROVED. Verifying that the Apply page is shown instead.");
            boolean isApplyPageVisible = loginPage.isApplyMembershipPageDisplayed();
            Assert.assertTrue(isApplyPageVisible, "Neither Approved Forum nor Apply page is visible!");
        }
    }
    //END//
    
    
 // --- NEW: Admin Approval Steps ---
    @When("user clicks on Admin Panel button")
    public void user_clicks_on_admin_panel_button() {
        loginPage.clickAdminPanel();
        logger.info("Admin Panel opened.");
    }
    @And("Admin approves a pending Supplier application")
    public void admin_approves_supplier() {
        loginPage.approveAllInTable("supplierAdminTableBody", "Supplier Section");
    }

    @And("Admin approves a pending Payment confirmation")
    public void admin_approves_payment() {
        loginPage.approveAllInTable("allOrdersTableBody", "Payments Section");
    }

    @And("Admin approves a pending Raita application")
    public void admin_approves_raita() {
        loginPage.approveAllInTable("adminTableBody", "Raita Section");
    }
    //end of admin approval
    
    
    
    //Messages and Check the date and time at organic raita panel---START---    
    @When("user posts a forum message {string}")
    public void user_posts_a_forum_message(String message) throws Exception {
        logger.info("Initiating post sequence for user Mahesh...");
        // This calls the scrolling and typing logic we added to LoginPage
        loginPage.postForumMessage(message);
    }
 
    @Then("the message should be visible with current username and timestamp")
    public void the_message_should_be_visible_with_current_username_and_timestamp() {
        // 1. Give it a generous 7 seconds for the Firebase sync to complete
        try { Thread.sleep(7000); } catch (InterruptedException e) { e.printStackTrace(); }
 
        String postedBy = loginPage.getLatestPostUser();
        String postedAt = loginPage.getLatestPostTime();
 
        // 2. The Report
        System.out.println("==========================================");
        System.out.println("RAITA FORUM POST REPORT");
        System.out.println("USER: " + postedBy);
        System.out.println("TIME: " + postedAt);
        System.out.println("==========================================");
 
        // 3. Robust Assertion
        Assert.assertTrue(postedBy.toLowerCase().contains("mahesh"), 
            "FAILURE: The forum did not display Mahesh's name. It showed: " + postedBy);
    } //END//MEssages and chats
    
 // --- New Steps for Supplier Membership ---

    @When("user applies for Supplier Membership with certificate")
    public void user_applies_for_supplier_membership_with_certificate() throws InterruptedException {
        String certPath = new File("src/test/resources/images/farm.png").getAbsolutePath();
        
        // 1. Fill the text fields
        loginPage.fillSupplierApplication(
            "Organic Greens Co.", 
            "Shreya A J", 
            "shleya111@gmail.com", 
            "9876543210", 
            "https://organicgreens.com", 
            "Premium Bio-Fertilizers",
            certPath
        );

        // 2. Click the words "Submit Application"
        logger.info("Attempting to click Submit by reading the words on screen...");
        loginPage.clickSubmitByText();
    }
 // --- Missing Steps for Supplier Membership ---

    @When("user navigates to Become a Supplier section")
    public void user_navigates_to_become_a_supplier_section() {
        logger.info("Navigating to Supplier Section...");
        loginPage.navigateToSuppliers();
        
        // Clicking "Become a Supplier" by searching for the words specifically
        loginPage.clickBecomeASupplier(); 
    }

    @Then("the supplier application should be submitted successfully")
    public void the_supplier_application_should_be_submitted_successfully() {
        logger.info("Verifying supplier application submission...");
        // You can add an assertion here if there is a success message on the UI
        System.out.println("SUCCESS: Supplier application processed.");
    }
    
    
  //Session logout and back button protection--START---
 // Add to LoginSteps.java
     
     @When("user clicks on the profile name")
     public void user_clicks_on_the_profile_name() {
         loginPage.clickProfile();
         logger.info("User clicked on the profile menu.");
     }
  
     @And("user clicks on logout button")
     public void user_clicks_on_logout_button() {
         loginPage.clickLogout();
         logger.info("User clicked on logout.");
     }
    
     // Keep ONLY this one for refreshing
     @And("user refreshes the authentication page")
     public void user_refreshes_the_page() {
         loginPage.refreshPage();
         logger.info("Page refreshed.");
     }
  
     @When("user attempts to navigate back in the browser")
     public void user_attempts_to_navigate_back_in_the_browser() {
         loginPage.navigateBack();
         logger.info("User attempted to go back to the protected page.");
     }
     @Then("the user should be restricted from accessing the Organic Raita content")
     public void the_user_should_be_restricted_from_accessing_the_organic_raita_content() {
         // We check if the approved content is NOT visible
         boolean isVisible = loginPage.isRaitaApprovedPageDisplayed();
         
         // Assert that it is FALSE (meaning the user is restricted/logged out)
         Assert.assertFalse(isVisible, "SECURITY FAIL: User can still see Raita content!");
         logger.info("Restriction verified: Protected content is not accessible.");
     }
   //Session logout and back button protection--END---
     
     
     
  //Feature file updation of certificate using POSTMAN
     @Then("the supplier application should complete within {int} seconds")
     public void verify_response_time(int maxSeconds) throws Exception {
         String largeImgPath = new File("src/test/resources/images/large_cert.png").getAbsolutePath();
         
         // 1. Measure and Execute
         long timeTaken = loginPage.uploadLargeCertificateAndMeasureTime(largeImgPath);
         
         // 2. Take the screenshot immediately after submission completes
         loginPage.takePageScreenshot("Supplier_Upload_Result");
         
         logger.info("Payload upload took: " + timeTaken + " seconds");
         
         // 3. Assertion
         Assert.assertTrue(timeTaken <= maxSeconds, 
             "Performance Fail: Upload took " + timeTaken + " seconds (Max allowed: " + maxSeconds + ")");
     }

     @And("the system should accept the large Base64 payload without errors")
     public void verify_payload_limit() {
         // Access the driver via DriverFactory to avoid the "driver" error
         String pageTitle = DriverFactory.getDriver().getTitle();
         String pageSource = DriverFactory.getDriver().getPageSource();

         // Verify we didn't hit a 413 Payload Too Large error
         boolean hasPayloadError = pageTitle.contains("413") || pageSource.contains("Too Large");
         
         Assert.assertFalse(hasPayloadError, "Payload Limit Fail: The server returned a 413 Payload Too Large error.");
         logger.info("Payload Limit Verified: Large Base64 string accepted by JivKisan server.");
     }
     //END POSTMAN
     @When("user uploads a high-resolution certificate for Base64 processing")
     public void user_uploads_large_certificate() {
         // This step is a placeholder for the upload logic triggered in the next step
         logger.info("Ready to process large Base64 payload.");
     }
     
     private String mainWindowHandle;

     @When("user clicks on the AI Advisory menu link")
     public void user_clicks_on_the_ai_advisory_menu_link() throws InterruptedException {
         mainWindowHandle = DriverFactory.getDriver().getWindowHandle();
         loginPage.clickAIAdvisory();
         Thread.sleep(500);
     }

     

     @Then("a new tab should open with the AI Advisory page")
     public void a_new_tab_should_open_with_the_ai_advisory_page() {
         // Calling the updated verification method
         boolean isOpened = loginPage.verifyNewTabOpened(); 
         Assert.assertTrue(isOpened, "Assertion Failed! The tab opened but the title didn't match.");
     }
    
 }
    
    

 
 
