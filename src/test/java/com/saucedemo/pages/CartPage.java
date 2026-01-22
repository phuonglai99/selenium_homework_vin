package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "cart_quantity")
    private List<WebElement> cartQuantities;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> itemNames;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage(WebDriver driver, int customWaitTimeInSeconds) {
        super(driver, customWaitTimeInSeconds);
    }

    public boolean isCartPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText();
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkoutButton.click();
    }

    public void clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton));
        continueShoppingButton.click();
    }

    public boolean isCheckoutButtonDisplayed() {
        try {
            return checkoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getCartItemNames() {
        return itemNames.stream()
            .map(WebElement::getText)
            .collect(java.util.stream.Collectors.toList());
    }
}
