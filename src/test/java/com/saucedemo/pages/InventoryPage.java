package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement shoppingCartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage(WebDriver driver, int customWaitTimeInSeconds) {
        super(driver, customWaitTimeInSeconds);
    }

    public boolean isInventoryPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return pageTitle.getText();
    }

    public void addItemToCart(String itemName) {
        WebElement addButton = driver.findElement(
            By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button")
        );
        wait.until(ExpectedConditions.elementToBeClickable(addButton));
        addButton.click();
    }

    public void addItemToCartByIndex(int index) {
        if (index >= 0 && index < inventoryItems.size()) {
            WebElement item = inventoryItems.get(index);
            WebElement addButton = item.findElement(By.tagName("button"));
            wait.until(ExpectedConditions.elementToBeClickable(addButton));
            addButton.click();
        }
    }

    public void removeItemFromCart(String itemName) {
        WebElement removeButton = driver.findElement(
            By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button")
        );
        wait.until(ExpectedConditions.elementToBeClickable(removeButton));
        removeButton.click();
    }

    public int getCartItemCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(shoppingCartBadge));
            return Integer.parseInt(shoppingCartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickShoppingCart() {
        wait.until(ExpectedConditions.elementToBeClickable(shoppingCartLink));
        shoppingCartLink.click();
    }

    public int getInventoryItemCount() {
        return inventoryItems.size();
    }

    public boolean isItemAddedToCart(String itemName) {
        try {
            WebElement removeButton = driver.findElement(
                By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button[text()='Remove']")
            );
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
