package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 * Utility class for logging test steps to ExtentReports
 */
public class ExtentLogger {

    /**
     * Log info message
     */
    public static void info(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.info(message);
        }
        System.out.println("  ℹ️  " + message);
    }

    /**
     * Log pass message
     */
    public static void pass(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
        }
        System.out.println("  ✅ " + message);
    }

    /**
     * Log fail message
     */
    public static void fail(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
        }
        System.out.println("  ❌ " + message);
    }

    /**
     * Log warning message
     */
    public static void warning(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.warning(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
        }
        System.out.println("  ⚠️  " + message);
    }

    /**
     * Log skip message
     */
    public static void skip(String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.skip(MarkupHelper.createLabel(message, ExtentColor.ORANGE));
        }
        System.out.println("  ⏭️  " + message);
    }

    /**
     * Log step with custom status
     */
    public static void log(Status status, String message) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(status, message);
        }
        System.out.println("  📝 " + message);
    }

    /**
     * Create a step (node) in the test
     */
    public static ExtentTest createStep(String stepName) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            return test.createNode(stepName);
        }
        return null;
    }
}
