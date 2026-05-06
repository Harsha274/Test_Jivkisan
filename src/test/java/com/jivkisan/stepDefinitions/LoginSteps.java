package com.jivkisan.stepDefinitions;

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
    public void user_applies_for_raita_membership_with_details() {
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
    
}