package com.saucedemo.base;

import com.saucedemo.config.ConfigReader;
import com.saucedemo.utils.DriverFactory;
import com.saucedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {

    protected WebDriver driver;
    protected String browser;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        this.browser = browser;
        DriverFactory.setDriver(browser);
        this.driver = DriverFactory.getDriver();
        driver.get(ConfigReader.getAppUrl());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String testName = result.getMethod().getMethodName();
            ScreenshotUtil.captureScreenshot(driver, testName, browser);
            System.out.println("Test failed: " + testName);
            System.out.println("Failure reason: " + result.getThrowable().getMessage());
        }

        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return driver;
    }
}
