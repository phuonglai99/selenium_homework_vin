package com.saucedemo.runner;

import com.saucedemo.utils.JiraTestFilter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runner class to execute tests based on Jira keys
 *
 * Usage examples:
 * 1. Run tests from file:
 *    java -cp ... JiraTestRunner --file jira-keys.txt
 *
 * 2. Run specific Jira keys:
 *    java -cp ... JiraTestRunner --keys PROJ-123,PROJ-456
 *
 * 3. Run with browser:
 *    java -cp ... JiraTestRunner --keys PROJ-123 --browser chrome
 */
public class JiraTestRunner {

    public static void main(String[] args) {
        String filePath = null;
        String[] jiraKeys = null;
        String browser = "chrome";
        boolean showHelp = false;

        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--file":
                case "-f":
                    if (i + 1 < args.length) {
                        filePath = args[++i];
                    }
                    break;
                case "--keys":
                case "-k":
                    if (i + 1 < args.length) {
                        jiraKeys = args[++i].split(",");
                    }
                    break;
                case "--browser":
                case "-b":
                    if (i + 1 < args.length) {
                        browser = args[++i];
                    }
                    break;
                case "--help":
                case "-h":
                    showHelp = true;
                    break;
            }
        }

        if (showHelp || (filePath == null && jiraKeys == null)) {
            printHelp();
            return;
        }

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         Jira Test Runner - SauceDemo          ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();

        // Load Jira keys
        if (filePath != null) {
            System.out.println("📁 Loading Jira keys from file: " + filePath);
            JiraTestFilter.loadJiraKeysFromFile(filePath);
        } else if (jiraKeys != null) {
            System.out.println("🔖 Running tests for Jira keys: " + Arrays.toString(jiraKeys));
            JiraTestFilter.setJiraKeys(jiraKeys);
        }

        System.out.println("🌐 Browser: " + browser);
        System.out.println();

        // Create TestNG suite programmatically
        TestNG testNG = new TestNG();
        XmlSuite suite = createTestSuite(browser);

        testNG.setXmlSuites(Arrays.asList(suite));
        testNG.run();

        System.out.println();
        System.out.println("✅ Test execution completed!");
    }

    private static XmlSuite createTestSuite(String browser) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Jira Test Suite");
        suite.setParallel(XmlSuite.ParallelMode.NONE);

        // Add interceptor for filtering
        suite.addListener("com.saucedemo.utils.JiraTestFilter");

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

    private static void printHelp() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Jira Test Runner - Usage Guide                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java -cp ... com.saucedemo.runner.JiraTestRunner [OPTIONS]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --file, -f <path>       Path to file containing Jira keys (one per line)");
        System.out.println("  --keys, -k <keys>       Comma-separated Jira keys (e.g., PROJ-123,PROJ-456)");
        System.out.println("  --browser, -b <name>    Browser to use (chrome|firefox) [default: chrome]");
        System.out.println("  --help, -h              Show this help message");
        System.out.println();
        System.out.println("Examples:");
        System.out.println();
        System.out.println("  1. Run tests from file:");
        System.out.println("     mvn exec:java -Dexec.mainClass=\"com.saucedemo.runner.JiraTestRunner\" \\");
        System.out.println("                   -Dexec.args=\"--file jira-keys.txt\"");
        System.out.println();
        System.out.println("  2. Run specific Jira keys:");
        System.out.println("     mvn exec:java -Dexec.mainClass=\"com.saucedemo.runner.JiraTestRunner\" \\");
        System.out.println("                   -Dexec.args=\"--keys SAUCE-101,SAUCE-102\"");
        System.out.println();
        System.out.println("  3. Run with Firefox:");
        System.out.println("     mvn exec:java -Dexec.mainClass=\"com.saucedemo.runner.JiraTestRunner\" \\");
        System.out.println("                   -Dexec.args=\"--keys SAUCE-101 --browser firefox\"");
        System.out.println();
        System.out.println("File Format (jira-keys.txt):");
        System.out.println("  # Comments start with #");
        System.out.println("  SAUCE-101");
        System.out.println("  SAUCE-102");
        System.out.println("  SAUCE-103");
        System.out.println();
    }
}
