package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CheckoutStepTwoPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutStepTwoPage(WebDriver driver, int customWaitTimeInSeconds) {
        super(driver, customWaitTimeInSeconds);
    }

    public boolean isCheckoutStepTwoPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().equals("Checkout: Overview");
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

    public String getSubtotal() {
        wait.until(ExpectedConditions.visibilityOf(subtotalLabel));
        return subtotalLabel.getText();
    }

    public String getTax() {
        wait.until(ExpectedConditions.visibilityOf(taxLabel));
        return taxLabel.getText();
    }

    public String getTotal() {
        wait.until(ExpectedConditions.visibilityOf(totalLabel));
        return totalLabel.getText();
    }

    public void clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        finishButton.click();
    }

    public void clickCancel() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelButton));
        cancelButton.click();
    }
}
