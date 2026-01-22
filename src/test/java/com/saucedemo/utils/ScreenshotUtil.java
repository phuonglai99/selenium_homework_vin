package com.saucedemo.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.saucedemo.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String testName) {
        if (!ConfigReader.isScreenshotEnabled()) {
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        String screenshotDir = ConfigReader.getScreenshotDir();

        // Create screenshots directory if it doesn't exist
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String screenshotPath = screenshotDir + screenshotName;

        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File source = screenshot.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    public static String captureScreenshot(WebDriver driver, String testName, String browser) {
        if (!ConfigReader.isScreenshotEnabled()) {
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + browser + "_" + timestamp + ".png";
        String screenshotDir = ConfigReader.getScreenshotDir();

        // Create screenshots directory if it doesn't exist
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String screenshotPath = screenshotDir + screenshotName;

        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File source = screenshot.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot captured: " + screenshotPath);
            return screenshotPath;
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot as Base64 string for embedding in HTML reports
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            String base64 = screenshot.getScreenshotAs(OutputType.BASE64);
            System.out.println("📸 Screenshot captured as Base64 (embedded in report)");
            return base64;
        } catch (Exception e) {
            System.err.println("Failed to capture Base64 screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot as Base64 and also save to file
     * @param driver WebDriver instance
     * @param testName Test name
     * @param browser Browser name
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64AndSave(WebDriver driver, String testName, String browser) {
        // Save to file first
        captureScreenshot(driver, testName, browser);

        // Return base64 for embedding
        return captureScreenshotAsBase64(driver);
    }
}
