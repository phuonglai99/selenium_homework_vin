package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Enhanced Test Logger for debugging and detailed test reporting
 *
 * Features:
 * - Multiple log levels (DEBUG, INFO, WARN, ERROR, PASS, FAIL)
 * - Variable/Object logging
 * - Step creation with auto-numbering
 * - Screenshot attachment
 * - Exception logging
 * - Table logging
 * - Collection pretty printing
 * - Time tracking
 * - Console + HTML Report logging
 *
 * Usage:
 * TestLogger.step("Login to application");
 * TestLogger.info("Entering username: " + username);
 * TestLogger.debug("Current URL", driver.getCurrentUrl());
 * TestLogger.variable("cartCount", cartCount);
 * TestLogger.pass("Login successful");
 */
public class TestLogger {

    private static int stepCounter = 0;
    private static Map<String, Long> timers = new HashMap<>();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    /**
     * Log debug message (only visible in report, not in console by default)
     */
    public static void debug(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.INFO, "🔍 DEBUG: " + message);
        }
        if (isDebugEnabled()) {
            System.out.println("  🔍 [DEBUG] " + getCurrentTime() + " " + message);
        }
    }

    /**
     * Log debug with key-value pair
     */
    public static void debug(String key, Object value) {
        debug(key + " = " + formatValue(value));
    }

    /**
     * Log info message
     */
    public static void info(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info("ℹ️ " + message);
        }
        System.out.println("  ℹ️  [INFO] " + getCurrentTime() + " " + message);
    }

    /**
     * Log warning message
     */
    public static void warn(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.warning(MarkupHelper.createLabel("⚠️ " + message, ExtentColor.YELLOW));
        }
        System.out.println("  ⚠️  [WARN] " + getCurrentTime() + " " + message);
    }

    /**
     * Log error message
     */
    public static void error(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.fail(MarkupHelper.createLabel("❌ ERROR: " + message, ExtentColor.RED));
        }
        System.err.println("  ❌ [ERROR] " + getCurrentTime() + " " + message);
    }

    /**
     * Log pass message
     */
    public static void pass(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.pass(MarkupHelper.createLabel("✅ " + message, ExtentColor.GREEN));
        }
        System.out.println("  ✅ [PASS] " + getCurrentTime() + " " + message);
    }

    /**
     * Log fail message
     */
    public static void fail(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.fail(MarkupHelper.createLabel("❌ " + message, ExtentColor.RED));
        }
        System.err.println("  ❌ [FAIL] " + getCurrentTime() + " " + message);
    }

    // ════════════════════════════════════════════════════════
    //  STEP CREATION
    // ════════════════════════════════════════════════════════

    /**
     * Create a numbered step
     * Example: "Step 1: Login to application"
     */
    public static ExtentTest step(String description) {
        stepCounter++;
        String stepName = "Step " + stepCounter + ": " + description;

        ExtentTest test = ExtentReportManager.getTest();
        ExtentTest stepNode = null;

        if (test != null) {
            stepNode = test.createNode(stepName);
            stepNode.info("🚀 Starting: " + description);
        }

        System.out.println("\n  🚀 " + stepName);
        return stepNode;
    }

    /**
     * Create a step without numbering
     */
    public static ExtentTest createStep(String description) {
        ExtentTest test = ExtentReportManager.getTest();
        ExtentTest stepNode = null;

        if (test != null) {
            stepNode = test.createNode(description);
        }

        System.out.println("\n  📌 " + description);
        return stepNode;
    }

    /**
     * Reset step counter (call in @BeforeMethod)
     */
    public static void resetStepCounter() {
        stepCounter = 0;
    }

    // ════════════════════════════════════════════════════════
    //  VARIABLE & OBJECT LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log a variable with its value
     * Example: TestLogger.variable("username", "standard_user");
     */
    public static void variable(String name, Object value) {
        String formattedValue = formatValue(value);
        String message = "📦 Variable [" + name + "] = " + formattedValue;

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info("<b>" + name + "</b> = <code>" + formattedValue + "</code>");
        }
        System.out.println("  📦 " + name + " = " + formattedValue);
    }

    /**
     * Log multiple variables at once
     */
    public static void variables(Map<String, Object> variables) {
        info("Variables:");
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            variable(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Log an assertion check
     */
    public static void assertion(String description, boolean condition, Object expected, Object actual) {
        if (condition) {
            pass("Assertion passed: " + description);
            debug("Expected", expected);
            debug("Actual", actual);
        } else {
            fail("Assertion failed: " + description);
            error("Expected: " + formatValue(expected));
            error("Actual: " + formatValue(actual));
        }
    }

    // ════════════════════════════════════════════════════════
    //  COLLECTION LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log a list with pretty formatting
     */
    public static void logList(String name, List<?> list) {
        if (list == null || list.isEmpty()) {
            info(name + " is empty");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(list.size()).append(" items):");

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            sb.append("<ul>");
            for (int i = 0; i < list.size(); i++) {
                sb.append("<li>").append(i).append(": ").append(list.get(i)).append("</li>");
            }
            sb.append("</ul>");
            test.info(sb.toString());
        }

        System.out.println("  📋 " + name + " (" + list.size() + " items):");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("      [" + i + "] " + list.get(i));
        }
    }

    /**
     * Log a map with pretty formatting
     */
    public static void logMap(String name, Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            info(name + " is empty");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(map.size()).append(" entries):");

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            sb.append("<ul>");
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append("<li><b>").append(entry.getKey()).append("</b>: ")
                  .append(entry.getValue()).append("</li>");
            }
            sb.append("</ul>");
            test.info(sb.toString());
        }

        System.out.println("  📋 " + name + " (" + map.size() + " entries):");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println("      " + entry.getKey() + " = " + entry.getValue());
        }
    }

    // ════════════════════════════════════════════════════════
    //  EXCEPTION LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log an exception with full stack trace
     */
    public static void exception(Throwable throwable) {
        exception("Exception occurred", throwable);
    }

    /**
     * Log an exception with custom message
     */
    public static void exception(String message, Throwable throwable) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.fail(throwable);
            test.fail("<b>❌ " + message + "</b>: " + throwable.getMessage());
        }

        System.err.println("  ❌ [EXCEPTION] " + message);
        System.err.println("      Message: " + throwable.getMessage());
        System.err.println("      Type: " + throwable.getClass().getName());
        throwable.printStackTrace();
    }

    // ════════════════════════════════════════════════════════
    //  SCREENSHOT LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Capture and log screenshot
     */
    public static void screenshot(WebDriver driver, String description) {
        try {
            String base64 = ScreenshotUtil.captureScreenshotAsBase64(driver);

            ExtentTest test = ExtentReportManager.getTest();
            if (test != null && base64 != null) {
                test.info("📸 " + description);
                test.addScreenCaptureFromBase64String(base64, description);
            }

            info("Screenshot captured: " + description);
        } catch (Exception e) {
            warn("Failed to capture screenshot: " + e.getMessage());
        }
    }

    /**
     * Capture screenshot of a specific step
     */
    public static void stepScreenshot(WebDriver driver, String stepDescription) {
        screenshot(driver, "Screenshot for: " + stepDescription);
    }

    // ════════════════════════════════════════════════════════
    //  TABLE LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log data as HTML table
     */
    public static void table(String title, String[][] data) {
        if (data == null || data.length == 0) {
            info(title + " - No data");
            return;
        }

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            String markup = MarkupHelper.createTable(data).getMarkup();
            test.info("<b>" + title + "</b>" + markup);
        }

        // Console output
        System.out.println("  📊 " + title + ":");
        for (String[] row : data) {
            System.out.println("      " + String.join(" | ", row));
        }
    }

    /**
     * Log comparison table (Expected vs Actual)
     */
    public static void comparisonTable(String title, Map<String, String[]> comparisons) {
        int rows = comparisons.size() + 1;
        String[][] data = new String[rows][3];

        data[0] = new String[]{"Field", "Expected", "Actual"};

        int i = 1;
        for (Map.Entry<String, String[]> entry : comparisons.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue()[0]; // Expected
            data[i][2] = entry.getValue()[1]; // Actual
            i++;
        }

        table(title, data);
    }

    // ════════════════════════════════════════════════════════
    //  TIMING & PERFORMANCE
    // ════════════════════════════════════════════════════════

    /**
     * Start a timer
     */
    public static void startTimer(String timerName) {
        timers.put(timerName, System.currentTimeMillis());
        debug("Timer started: " + timerName);
    }

    /**
     * Stop a timer and log elapsed time
     */
    public static long stopTimer(String timerName) {
        Long startTime = timers.get(timerName);
        if (startTime == null) {
            warn("Timer '" + timerName + "' was not started");
            return 0;
        }

        long elapsed = System.currentTimeMillis() - startTime;
        timers.remove(timerName);

        info("⏱️ Timer [" + timerName + "] = " + formatDuration(elapsed));
        return elapsed;
    }

    /**
     * Log elapsed time for an operation
     */
    public static void logDuration(String operation, long milliseconds) {
        String formatted = formatDuration(milliseconds);
        info("⏱️ " + operation + " took " + formatted);
    }

    // ════════════════════════════════════════════════════════
    //  SELENIUM ELEMENT LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log element details
     */
    public static void logElement(String name, WebElement element) {
        if (element == null) {
            warn("Element '" + name + "' is null");
            return;
        }

        try {
            Map<String, Object> details = new LinkedHashMap<>();
            details.put("Tag", element.getTagName());
            details.put("Text", element.getText());
            details.put("Displayed", element.isDisplayed());
            details.put("Enabled", element.isEnabled());
            details.put("Selected", element.isSelected());

            info("Element: " + name);
            variables(details);
        } catch (Exception e) {
            warn("Could not retrieve element details: " + e.getMessage());
        }
    }

    /**
     * Log action on element
     */
    public static void action(String action, String elementDescription) {
        info("🎯 Action: " + action + " on [" + elementDescription + "]");
    }

    /**
     * Log navigation
     */
    public static void navigation(String url) {
        info("🌐 Navigating to: " + url);
    }

    // ════════════════════════════════════════════════════════
    //  HIGHLIGHTING & EMPHASIS
    // ════════════════════════════════════════════════════════

    /**
     * Log highlighted/important message
     */
    public static void highlight(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info(MarkupHelper.createLabel("⭐ " + message, ExtentColor.BLUE));
        }
        System.out.println("\n  ⭐⭐⭐ " + message.toUpperCase() + " ⭐⭐⭐");
    }

    /**
     * Log separator line
     */
    public static void separator() {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info("<hr>");
        }
        System.out.println("  ─────────────────────────────────────────────────────────");
    }

    // ════════════════════════════════════════════════════════
    //  CODE BLOCK LOGGING
    // ════════════════════════════════════════════════════════

    /**
     * Log JSON string with pretty formatting
     */
    public static void json(String title, String jsonString) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info("<b>" + title + "</b><pre><code class='language-json'>"
                    + jsonString + "</code></pre>");
        }
        System.out.println("  📄 " + title + ":");
        System.out.println(jsonString);
    }

    /**
     * Log code block
     */
    public static void code(String title, String code, String language) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info("<b>" + title + "</b><pre><code class='language-" + language + "'>"
                    + code + "</code></pre>");
        }
        System.out.println("  💻 " + title + ":");
        System.out.println(code);
    }

    // ════════════════════════════════════════════════════════
    //  HELPER METHODS
    // ════════════════════════════════════════════════════════

    /**
     * Format value for display
     */
    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        if (value instanceof Collection) {
            return value.toString() + " (size: " + ((Collection<?>) value).size() + ")";
        }
        if (value instanceof Map) {
            return value.toString() + " (size: " + ((Map<?, ?>) value).size() + ")";
        }
        return value.toString();
    }

    /**
     * Get current time as string
     */
    private static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }

    /**
     * Format duration in milliseconds to readable string
     */
    private static String formatDuration(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + "ms";
        } else if (milliseconds < 60000) {
            return String.format("%.2fs", milliseconds / 1000.0);
        } else {
            long minutes = milliseconds / 60000;
            long seconds = (milliseconds % 60000) / 1000;
            return minutes + "m " + seconds + "s";
        }
    }

    /**
     * Check if debug logging is enabled
     */
    private static boolean isDebugEnabled() {
        // Can be controlled via config or system property
        String debugProperty = System.getProperty("test.debug", "false");
        return Boolean.parseBoolean(debugProperty);
    }

    /**
     * Clear all timers
     */
    public static void clearTimers() {
        timers.clear();
    }

    // ════════════════════════════════════════════════════════
    //  CONVENIENCE METHODS
    // ════════════════════════════════════════════════════════

    /**
     * Initialize logger for a test (call in @BeforeMethod)
     */
    public static void initTest(String testName) {
        resetStepCounter();
        clearTimers();
        separator();
        highlight("Starting Test: " + testName);
        separator();
    }

    /**
     * Finalize logger for a test (call in @AfterMethod)
     */
    public static void finalizeTest(String testName, boolean passed) {
        separator();
        if (passed) {
            pass("Test Completed: " + testName);
        } else {
            fail("Test Failed: " + testName);
        }
        separator();
    }
}
