package com.saucedemo.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.saucedemo.annotations.JiraTest;
import com.saucedemo.utils.ExtentReportManager;
import com.saucedemo.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;
import java.lang.reflect.Method;

/**
 * TestNG Listener for ExtentReports integration
 */
public class ExtentTestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;

    @Override
    public void onStart(ISuite suite) {
        extent = ExtentReportManager.getExtentReports();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║          Starting Test Suite: " + suite.getName());
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = extent.createTest(testName, description);

        // Add categories
        String className = result.getTestClass().getName();
        test.assignCategory(className.substring(className.lastIndexOf(".") + 1));

        // Add Jira key if available
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        JiraTest jiraAnnotation = method.getAnnotation(JiraTest.class);
        if (jiraAnnotation != null) {
            test.assignCategory("Jira: " + jiraAnnotation.key());
            test.info("🔖 Jira Key: " + jiraAnnotation.key());
            test.info("📝 Description: " + jiraAnnotation.description());
            test.info("⚡ Priority: " + jiraAnnotation.priority());
        }

        // Add browser info
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            test.info("🌐 Browser: " + parameters[0]);
        }

        ExtentReportManager.setTest(test);

        System.out.println("\n▶️  Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel(
                "Test PASSED: " + result.getMethod().getMethodName(),
                ExtentColor.GREEN
            ));
        }
        System.out.println("✅ Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel(
                "Test FAILED: " + result.getMethod().getMethodName(),
                ExtentColor.RED
            ));

            // Log failure reason
            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                test.fail("❌ Failure Reason: " + throwable.getMessage());
                test.fail(throwable);
            }

            // Attach screenshot (EMBEDDED as Base64 in HTML)
            try {
                // Use DriverFactory to get the current thread's driver
                WebDriver driver = com.saucedemo.utils.DriverFactory.getDriver();

                if (driver != null) {
                    // Capture as Base64 for embedding in HTML
                    String base64Screenshot = ScreenshotUtil.captureScreenshotAsBase64(driver);

                    if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                        // Embed screenshot directly in HTML (no external file needed!)
                        test.addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
                        test.info("📸 Screenshot embedded in report (Base64)");

                        // Optional: Also save to file for backup
                        String screenshotPath = ScreenshotUtil.captureScreenshot(
                            driver,
                            result.getMethod().getMethodName(),
                            getBrowserName(result)
                        );
                        if (screenshotPath != null) {
                            test.info("💾 Backup saved at: " + screenshotPath);
                        }
                    } else {
                        test.warning("⚠️ Could not capture Base64 screenshot");
                    }
                } else {
                    test.warning("⚠️ WebDriver is null - cannot capture screenshot");
                }
            } catch (Exception e) {
                test.warning("⚠️ Could not capture screenshot: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("❌ Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("   Reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel(
                "Test SKIPPED: " + result.getMethod().getMethodName(),
                ExtentColor.YELLOW
            ));

            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            }
        }
        System.out.println("⏭️  Test SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        onTestFailure(result);
    }

    /**
     * Get browser name from test parameters
     */
    private String getBrowserName(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            return parameters[0].toString();
        }
        return "chrome";
    }
}
