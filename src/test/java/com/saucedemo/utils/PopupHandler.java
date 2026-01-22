package com.saucedemo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility to handle browser popups that may appear during test execution
 */
public class PopupHandler {

    /**
     * Attempt to dismiss any Chrome password save popup
     * This handles the native Chrome popup that appears after login
     */
    public static void dismissChromePasswordPopup(WebDriver driver) {
        try {
            // Wait a bit for popup to appear (if it will)
            Thread.sleep(500);

            // Try to press ESC key to dismiss
            driver.switchTo().activeElement().sendKeys(org.openqa.selenium.Keys.ESCAPE);

            System.out.println("  🔧 Attempted to dismiss Chrome password popup with ESC key");
        } catch (Exception e) {
            // Silently ignore - popup may not appear
        }
    }

    /**
     * Wait for any overlays/modals to disappear before continuing
     */
    public static void waitForOverlaysToDisappear(WebDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

            // Wait for common overlay elements to disappear
            String[] overlaySelectors = {
                ".modal",
                ".overlay",
                ".popup",
                "[role='dialog']",
                ".MuiDialog-root"
            };

            for (String selector : overlaySelectors) {
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
                } catch (Exception e) {
                    // Element not present or already invisible - continue
                }
            }

            System.out.println("  ✅ No blocking overlays detected");
        } catch (Exception e) {
            System.out.println("  ⚠️  Could not verify overlay state: " + e.getMessage());
        }
    }

    /**
     * Force click through any potential blocking elements
     */
    public static void ensureElementClickable(WebDriver driver, WebElement element) {
        try {
            // Try ESC first to dismiss any popups
            driver.switchTo().activeElement().sendKeys(org.openqa.selenium.Keys.ESCAPE);
            Thread.sleep(300);

            // Now try to click
            element.click();
        } catch (Exception e) {
            // If regular click fails, try JavaScript click
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", element);
                System.out.println("  🔧 Used JavaScript click as fallback");
            } catch (Exception je) {
                System.out.println("  ❌ Could not click element: " + je.getMessage());
                throw je;
            }
        }
    }
}
