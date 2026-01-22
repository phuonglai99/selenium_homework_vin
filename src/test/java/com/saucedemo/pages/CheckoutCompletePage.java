package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage(WebDriver driver, int customWaitTimeInSeconds) {
        super(driver, customWaitTimeInSeconds);
    }

    public boolean isCheckoutCompletePageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().equals("Checkout: Complete!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getCompleteHeader() {
        wait.until(ExpectedConditions.visibilityOf(completeHeader));
        return completeHeader.getText();
    }

    public String getCompleteText() {
        wait.until(ExpectedConditions.visibilityOf(completeText));
        return completeText.getText();
    }

    public void clickBackToProducts() {
        wait.until(ExpectedConditions.elementToBeClickable(backToProductsButton));
        backToProductsButton.click();
    }

    public boolean isOrderComplete() {
        return isCheckoutCompletePageDisplayed() &&
               getCompleteHeader().contains("Thank you for your order");
    }
}
