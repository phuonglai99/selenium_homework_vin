package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manager class for ExtentReports HTML reporting
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath;

    /**
     * Initialize ExtentReports
     */
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    /**
     * Create ExtentReports instance
     */
    private static void createInstance() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir = "test-output/extent-reports/";

        // Create directory if not exists
        File directory = new File(reportDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        reportPath = reportDir + "TestReport_" + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure report
        sparkReporter.config().setDocumentTitle("SauceDemo Test Report");
        sparkReporter.config().setReportName("Automation Test Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System info
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Automation Team");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    /**
     * Get current test
     */
    public static ExtentTest getTest() {
        return test.get();
    }

    /**
     * Set current test
     */
    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }

    /**
     * Remove current test
     */
    public static void removeTest() {
        test.remove();
    }

    /**
     * Flush reports
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║  📊 ExtentReports HTML Report Generated Successfully!   ║");
            System.out.println("╚═══════════════════════════════════════════════════════════╝");
            System.out.println("📁 Report Location: " + new File(reportPath).getAbsolutePath());
            System.out.println("🌐 Open in browser to view detailed results");
        }
    }

    /**
     * Get report path
     */
    public static String getReportPath() {
        return reportPath;
    }
}
