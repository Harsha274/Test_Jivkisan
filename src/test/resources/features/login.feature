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
 
  Scenario: Successful Normal User Login
    When user enters email "shleya111@gmail.com" and password "shreya164"
    Then user should be logged in as "Shreya A J"
    And the Admin Panel should NOT be visible