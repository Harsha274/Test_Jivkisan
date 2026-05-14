Feature: JivKisan Authentication
  As a user of JivKisan
  I want to login with my credentials
  So that I can access my dashboard and specific features

  Background: 
    Given user is on the JivKisan homepage
    And user clicks on Login Register button

  @SetA
  Scenario: Successful Admin Login
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    And the Admin Panel should be visible

  @SetB
  Scenario: Successful Normal User Login
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    And the Admin Panel should NOT be visible

  @SetA
  Scenario: Admin panel access validation for Chandini
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    And verify if user is Admin to show Admin Panel

  @SetB
  Scenario: Add any two products to cart and verify details in cart menu
    When user enters email "shleya111@gmail.com" and password "shreya164"
    And user navigates to all products page
    And user adds any two products to the cart and displays their names
    And user navigates to my cart menu
    Then user should see product details and the final total value in the console

  @SetA
  Scenario: Count number of Orders and Applications
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    And the Admin Panel should be visible
    And the user counts the total orders and applications

  @SetB
  Scenario: Submit Raita Membership application
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    When user navigates to Organic Raita section
    And user applies for Raita Membership with details
    Then the application should be submitted successfully

  @SetA
  Scenario: Access member-only discussion forum - Shreya
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    When user navigates to Organic Raita section
    Then verify if member application is approved to access forum

  @SetB
  Scenario: Access member-only discussion forum - Harsha
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    When user navigates to Organic Raita section
    Then verify if member application is approved to access forum

  @SetA
  Scenario: Admin approves pending applications and payments
    When user enters email "harshask274@gmail.com" and password "Harsha123"
    Then user should be logged in as "Harsha"
    And the Admin Panel should be visible
    When user clicks on Admin Panel button
    And Admin approves a pending Supplier application
    And Admin approves a pending Payment confirmation
    And Admin approves a pending Raita application

  @SetB
  Scenario: Cart persistence check after refresh and re-login
    When user enters email "shleya111@gmail.com" and password "shreya164"
    And user navigates to all products page
    And user adds any two products to the cart and displays their names
    And user refreshes the page
    And user navigates to my cart menu
    Then user should see product details and the final total value in the console
    And user logs out and logs back in with email "shleya111@gmail.com" and password "shreya164"
    And user navigates to my cart menu
    Then user should see product details and the final total value in the console

  @SetA
  Scenario: Post message in Organic Raita forum and verify timestamp
    When user enters email "rockboymahi@gmail.com" and password "Mahesh"
    Then user should be logged in as "Mahesh"
    When user navigates to Organic Raita section
    And user posts a forum message "Hello, testing the organic raita panel"
    Then the message should be visible with current username and timestamp

  @SetB
  Scenario: Update item quantity in cart only done by respected seller
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    And user navigates to all products page
    And the seller updates the number of units for their items randomly below 150
    Then the product units should reflect the new changes

  @SetA
  Scenario: Submit Supplier Application with Certificate
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    When user navigates to Become a Supplier section
    And user applies for Supplier Membership with certificate
    Then the supplier application should be submitted successfully

  @SetB
  Scenario: Session logout and back button protection for Organic Raita
    When user enters email "rockboymahi@gmail.com" and password "Mahesh"
    Then user should be logged in as "Mahesh"
    And user navigates to Organic Raita section
    Then verify if member application is approved to access forum
    When user clicks on the profile name
    And user clicks on logout button
    And user refreshes the authentication page
    When user attempts to navigate back in the browser
    Then the user should be restricted from accessing the Organic Raita content

  @SetA
  Scenario: Verify search functionality across All Products and Organic panels
    When user enters email "rockboymahi@gmail.com" and password "Mahesh"
    Then user should be logged in as "Mahesh"
    And user navigates to all products page
    When user searches for "Arka Microbial Consortium (IIHR)" in the "All Products" panel
    Then the product "Arka Microbial Consortium (IIHR)" should be visible in the results
    And user navigates to organic only page
    When user searches for "Organic Mango" in the "Organic" panel
    Then the product "Organic Mango" should be visible in the results

  @SetB
  Scenario: Validate External Mandi API Data Structure
    Given the Mandi Price API is available
    When I send a GET request to the data.gov.in resource
    Then the response status code should be 200
    And each record should contain "commodity", "market", "min_price", and "modal_price"
    And the "modal_price" should be a valid numeric string

  @SetA
  Scenario: Validate Supplier Certificate Base64 Payload Performance
    When user enters email "chandinip72@gmail.com" and password "Chandini123"
    Then user should be logged in as "Chandini"
    When user navigates to Become a Supplier section
    And user uploads a high-resolution certificate for Base64 processing
    Then the supplier application should complete within 10 seconds
    And the system should accept the large Base64 payload without errors

  @SetB
  Scenario: Validate AI Advisory opens in a new tab
    Given user is on the JivKisan homepage
    When user clicks on the AI Advisory menu link
    Then a new tab should open with the AI Advisory page
    
