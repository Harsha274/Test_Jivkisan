Feature: JivKisan Authentication
  As a user of JivKisan
  I want to login with my credentials
  So that I can access my dashboard and specific features

  Background:
    Given user is on the JivKisan homepage
    And user clicks on Login Register button

  Scenario: Successful Admin Login
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    And the Admin Panel should be visible

  Scenario: Admin panel access validation for Chandini
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    And verify if user is Admin to show Admin Panel

  Scenario: Successful Normal User Login
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    And the Admin Panel should NOT be visible
    
    
   Scenario: Add any two products to cart and verify details in cart menu
When user enters email "shleya111@gmail.com" and password "shreya164"
And user navigates to all products page
And user adds any two products to the cart and displays their names
And user navigates to my cart menu
Then user should see product details and the final total value in the console


  Scenario: Count number of Orders and Applications
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    And the Admin Panel should be visible
    And the user counts the total orders and applications
    
 
  Scenario: Submit Raita Membership application
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    When user navigates to Organic Raita section
    And user applies for Raita Membership with details
    Then the application should be submitted successfully
    
    
    
    
    
    
  Scenario: Access member-only discussion forum
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    When user navigates to Organic Raita section
    Then verify if member application is approved to access forum
    
     Scenario: Access member-only discussion forum
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    When user navigates to Organic Raita section
    Then verify if member application is approved to access forum
    
    