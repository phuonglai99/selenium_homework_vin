package com.saucedemo.runner;

import com.saucedemo.utils.JiraTestFilter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleJiraRunner {

    public static void main(String[] args) {

        String[] jiraKeys = {
            "SAUCE-201"     // testAddSingleItemToCart
        };

        String browser = "chrome";  // chrome or firefox

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║       Simple Jira Test Runner - SauceDemo                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🔖 Running tests for Jira keys: " + Arrays.toString(jiraKeys));
        System.out.println("🌐 Browser: " + browser);
        System.out.println();

        // Set filter
        JiraTestFilter.setJiraKeys(jiraKeys);

        // Create and run TestNG suite
        TestNG testNG = new TestNG();
        XmlSuite suite = createTestSuite(browser);
        testNG.setXmlSuites(Arrays.asList(suite));

        System.out.println("▶️  Starting test execution...\n");
        testNG.run();

        System.out.println();
        System.out.println("✅ Test execution completed!");
    }

    /**
     * Create TestNG suite programmatically
     */
    private static XmlSuite createTestSuite(String browser) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Simple Jira Test Suite");
        suite.setParallel(XmlSuite.ParallelMode.NONE);

        // Add listener for filtering
        suite.addListener("com.saucedemo.utils.JiraTestFilter");

        // Add ExtentReports listener
        suite.addListener("com.saucedemo.listeners.ExtentTestListener");

        // Create test
        XmlTest test = new XmlTest(suite);
        test.setName(browser + " Tests");
        test.addParameter("browser", browser);

        // Add test classes
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass("com.saucedemo.tests.LoginTest"));
        classes.add(new XmlClass("com.saucedemo.tests.CartTest"));
        test.setXmlClasses(classes);

        return suite;
    }
}
