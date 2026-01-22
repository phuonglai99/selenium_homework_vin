package com.saucedemo.tests;

import com.saucedemo.annotations.JiraTest;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    @JiraTest(key = "SAUCE-101", description = "Valid login test", priority = "High")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        // Verify login page elements are displayed
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(),
            "Username field should be displayed");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(),
            "Password field should be displayed");
        Assert.assertTrue(loginPage.isLoginButtonEnabled(),
            "Login button should be enabled");

        // Perform login
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // Verify successful navigation to inventory page
        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
            "User should be redirected to inventory page after successful login");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products",
            "Page title should be 'Products'");
    }

    @Test(priority = 2, description = "Verify login fails with locked out user")
    @JiraTest(key = "SAUCE-102", description = "Locked user test", priority = "High")
    public void testLoginWithLockedUser() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with locked user
        loginPage.login(ConfigReader.getLockedUsername(), ConfigReader.getLockedPassword());

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for locked user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
            "Error message should indicate user is locked out");
    }

    @Test(priority = 3, description = "Verify login fails with invalid username")
    @JiraTest(key = "SAUCE-103", description = "Invalid username test", priority = "Medium")
    public void testLoginWithInvalidUsername() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with invalid username
        loginPage.login(ConfigReader.getInvalidUsername(), ConfigReader.getValidPassword());

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error message should indicate invalid credentials");
    }

    @Test(priority = 4, description = "Verify login fails with invalid password")
    public void testLoginWithInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with invalid password
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getInvalidPassword());

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
            "Error message should indicate invalid credentials");
    }

    @Test(priority = 5, description = "Verify login fails with empty username")
    public void testLoginWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with empty username
        loginPage.enterUsername("");
        loginPage.enterPassword(ConfigReader.getValidPassword());
        loginPage.clickLoginButton();

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for empty username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
            "Error message should indicate username is required");
    }

    @Test(priority = 6, description = "Verify login fails with empty password")
    public void testLoginWithEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with empty password
        loginPage.enterUsername(ConfigReader.getValidUsername());
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for empty password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
            "Error message should indicate password is required");
    }

    @Test(priority = 7, description = "Verify login fails with empty username and password")
    public void testLoginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with empty credentials
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for empty credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
            "Error message should indicate username is required");
    }

    @Test(priority = 8, description = "Verify login with special characters in username")
    public void testLoginWithSpecialCharacters() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with special characters
        loginPage.login("user@#$%", ConfigReader.getValidPassword());

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid username with special characters");
    }

    @Test(priority = 9, description = "Verify login with SQL injection attempt")
    public void testLoginWithSQLInjection() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt SQL injection
        loginPage.login("' OR '1'='1", "' OR '1'='1");

        // Verify error message is displayed (should not bypass authentication)
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed - SQL injection should not work");
    }

    @Test(priority = 10, description = "Verify login with XSS script attempt")
    public void testLoginWithXSSAttempt() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt XSS injection
        loginPage.login("<script>alert('XSS')</script>", ConfigReader.getValidPassword());

        // Verify error message is displayed (should handle XSS safely)
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed - XSS should be handled safely");
    }

    @Test(priority = 11, description = "Verify case sensitivity of username")
    public void testLoginCaseSensitivity() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with uppercase username
        loginPage.login(ConfigReader.getValidUsername().toUpperCase(),
                       ConfigReader.getValidPassword());

        // Verify error message is displayed (username should be case-sensitive)
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed - username should be case-sensitive");
    }

    @Test(priority = 12, description = "Verify login with whitespace in credentials")
    public void testLoginWithWhitespace() {
        LoginPage loginPage = new LoginPage(driver);

        // Attempt login with whitespace
        loginPage.login(" " + ConfigReader.getValidUsername() + " ",
                       " " + ConfigReader.getValidPassword() + " ");

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for credentials with whitespace");
    }
}
