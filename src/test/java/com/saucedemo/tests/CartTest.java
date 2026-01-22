package com.saucedemo.tests;

import com.saucedemo.annotations.JiraTest;
import com.saucedemo.base.BaseTest;
import com.saucedemo.config.ConfigReader;
import com.saucedemo.pages.*;
import com.saucedemo.utils.PopupHandler;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutStepOnePage checkoutStepOnePage;
    private CheckoutStepTwoPage checkoutStepTwoPage;
    private CheckoutCompletePage checkoutCompletePage;

    @BeforeMethod
    public void loginBeforeTest() {
        // Login before each test
        loginPage = new LoginPage(driver);
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // Dismiss any Chrome password save popup
        PopupHandler.dismissChromePasswordPopup(driver);

        // Initialize page objects
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutStepOnePage = new CheckoutStepOnePage(driver);
        checkoutStepTwoPage = new CheckoutStepTwoPage(driver);
        checkoutCompletePage = new CheckoutCompletePage(driver);

        // Verify login successful
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
            "User should be logged in and on inventory page");
    }

    @Test(priority = 1, description = "Verify adding a single item to cart")
    @JiraTest(key = "SAUCE-201", description = "Add single item to cart", priority = "High")
    public void testAddSingleItemToCart() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Verify cart badge shows 1
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1,
            "Cart should show 1 item");

        // Verify item is marked as added
        Assert.assertTrue(inventoryPage.isItemAddedToCart("Sauce Labs Backpack"),
            "Item should show Remove button indicating it's in cart");

        // Go to cart
        inventoryPage.clickShoppingCart();

        // Verify cart page
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should be displayed");
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
            "Fail Cart should contain 1 item");
    }

    @Test(priority = 2, description = "Verify adding multiple items to cart")
    public void testAddMultipleItemsToCart() {
        // Add multiple items
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");
        inventoryPage.addItemToCart("Sauce Labs Bolt T-Shirt");

        // Verify cart badge shows correct count
        Assert.assertEquals(inventoryPage.getCartItemCount(), 3,
            "Cart should show 3 items");

        // Go to cart
        inventoryPage.clickShoppingCart();

        // Verify cart page
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should be displayed");
        Assert.assertEquals(cartPage.getCartItemCount(), 3,
            "Cart should contain 3 items");

        // Verify all items are in cart
        Assert.assertTrue(cartPage.getCartItemNames().contains("Sauce Labs Backpack"),
            "Cart should contain Sauce Labs Backpack");
        Assert.assertTrue(cartPage.getCartItemNames().contains("Sauce Labs Bike Light"),
            "Cart should contain Sauce Labs Bike Light");
        Assert.assertTrue(cartPage.getCartItemNames().contains("Sauce Labs Bolt T-Shirt"),
            "Cart should contain Sauce Labs Bolt T-Shirt");
    }

    @Test(priority = 3, description = "Verify removing item from cart")
    public void testRemoveItemFromCart() {
        // Add items
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");

        // Verify 2 items added
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2,
            "Cart should show 2 items");

        // Remove one item
        inventoryPage.removeItemFromCart("Sauce Labs Backpack");

        // Verify cart badge shows 1
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1,
            "Cart should show 1 item after removal");
    }

    @Test(priority = 4, description = "Verify complete checkout flow with valid information")
    @JiraTest(key = "SAUCE-204", description = "Complete checkout flow", priority = "High")
    public void testCompleteCheckoutFlow() {
        // Add items to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");
        inventoryPage.addItemToCart("Sauce Labs Bike Light");

        // Verify items added
        Assert.assertEquals(inventoryPage.getCartItemCount(), 2,
            "Cart should show 2 items");

        // Go to cart
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should be displayed");

        // Proceed to checkout
        cartPage.clickCheckout();
        Assert.assertTrue(checkoutStepOnePage.isCheckoutStepOnePageDisplayed(),
            "Checkout step one page should be displayed");

        // Fill checkout information
        checkoutStepOnePage.fillCheckoutInformation(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestPostalCode()
        );

        // Continue to overview
        checkoutStepOnePage.clickContinue();
        Assert.assertTrue(checkoutStepTwoPage.isCheckoutStepTwoPageDisplayed(),
            "Checkout overview page should be displayed");

        // Verify items in overview
        Assert.assertEquals(checkoutStepTwoPage.getCartItemCount(), 2,
            "Overview should show 2 items");

        // Verify pricing information is displayed
        Assert.assertFalse(checkoutStepTwoPage.getSubtotal().isEmpty(),
            "Subtotal should be displayed");
        Assert.assertFalse(checkoutStepTwoPage.getTax().isEmpty(),
            "Tax should be displayed");
        Assert.assertFalse(checkoutStepTwoPage.getTotal().isEmpty(),
            "Total should be displayed");

        // Complete checkout
        checkoutStepTwoPage.clickFinish();
        Assert.assertTrue(checkoutCompletePage.isCheckoutCompletePageDisplayed(),
            "Checkout complete page should be displayed");

        // Verify order completion
        Assert.assertTrue(checkoutCompletePage.isOrderComplete(),
            "Order should be completed successfully");
        Assert.assertTrue(checkoutCompletePage.getCompleteHeader().contains("Thank you"),
            "Completion message should be displayed");
    }

    @Test(priority = 5, description = "Verify checkout fails with missing first name")
    public void testCheckoutWithMissingFirstName() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Go to cart and checkout
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        // Leave first name empty
        checkoutStepOnePage.enterFirstName("");
        checkoutStepOnePage.enterLastName(ConfigReader.getTestLastName());
        checkoutStepOnePage.enterPostalCode(ConfigReader.getTestPostalCode());
        checkoutStepOnePage.clickContinue();

        // Verify error message
        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(),
            "Error message should be displayed for missing first name");
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("First Name is required"),
            "Error should indicate first name is required");
    }

    @Test(priority = 6, description = "Verify checkout fails with missing last name")
    public void testCheckoutWithMissingLastName() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Go to cart and checkout
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        // Leave last name empty
        checkoutStepOnePage.enterFirstName(ConfigReader.getTestFirstName());
        checkoutStepOnePage.enterLastName("");
        checkoutStepOnePage.enterPostalCode(ConfigReader.getTestPostalCode());
        checkoutStepOnePage.clickContinue();

        // Verify error message
        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(),
            "Error message should be displayed for missing last name");
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("Last Name is required"),
            "Error should indicate last name is required");
    }

    @Test(priority = 7, description = "Verify checkout fails with missing postal code")
    public void testCheckoutWithMissingPostalCode() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Go to cart and checkout
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        // Leave postal code empty
        checkoutStepOnePage.enterFirstName(ConfigReader.getTestFirstName());
        checkoutStepOnePage.enterLastName(ConfigReader.getTestLastName());
        checkoutStepOnePage.enterPostalCode("");
        checkoutStepOnePage.clickContinue();

        // Verify error message
        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(),
            "Error message should be displayed for missing postal code");
        Assert.assertTrue(checkoutStepOnePage.getErrorMessage().contains("Postal Code is required"),
            "Error should indicate postal code is required");
    }

    @Test(priority = 8, description = "Verify checkout with all information fields empty")
    public void testCheckoutWithAllFieldsEmpty() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Go to cart and checkout
        inventoryPage.clickShoppingCart();
        cartPage.clickCheckout();

        // Leave all fields empty and continue
        checkoutStepOnePage.clickContinue();

        // Verify error message
        Assert.assertTrue(checkoutStepOnePage.isErrorMessageDisplayed(),
            "Error message should be displayed for missing information");
    }

    @Test(priority = 9, description = "Verify adding all items to cart and checkout")
    public void testAddAllItemsAndCheckout() {
        int totalItems = inventoryPage.getInventoryItemCount();

        // Add all items using index
        for (int i = 0; i < totalItems; i++) {
            inventoryPage.addItemToCartByIndex(i);
        }

        // Verify all items added
        Assert.assertEquals(inventoryPage.getCartItemCount(), totalItems,
            "Cart should contain all items");

        // Go to cart
        inventoryPage.clickShoppingCart();
        Assert.assertEquals(cartPage.getCartItemCount(), totalItems,
            "Cart should display all items");

        // Complete checkout
        cartPage.clickCheckout();
        checkoutStepOnePage.fillCheckoutInformation(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestPostalCode()
        );
        checkoutStepOnePage.clickContinue();

        Assert.assertEquals(checkoutStepTwoPage.getCartItemCount(), totalItems,
            "Overview should show all items");

        checkoutStepTwoPage.clickFinish();
        Assert.assertTrue(checkoutCompletePage.isOrderComplete(),
            "Order should be completed with all items");
    }

    @Test(priority = 10, description = "Verify continue shopping from cart")
    public void testContinueShoppingFromCart() {
        // Add item to cart
        inventoryPage.addItemToCart("Sauce Labs Backpack");

        // Go to cart
        inventoryPage.clickShoppingCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should be displayed");

        // Click continue shopping
        cartPage.clickContinueShopping();

        // Verify back on inventory page
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(),
            "Should be back on inventory page");

        // Verify cart still has item
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1,
            "Cart should still contain the item");
    }
}
