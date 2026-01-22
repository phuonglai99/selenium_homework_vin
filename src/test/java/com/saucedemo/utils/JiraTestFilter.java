package com.saucedemo.utils;

import com.saucedemo.annotations.JiraTest;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JiraTestFilter implements IMethodInterceptor {

    private static Set<String> jiraKeys = new HashSet<>();
    private static boolean filterEnabled = false;

    public static void loadJiraKeysFromFile(String filePath) {
        jiraKeys.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    jiraKeys.add(line.toUpperCase());
                }
            }
            filterEnabled = true;
            System.out.println("Loaded " + jiraKeys.size() + " Jira keys from file: " + filePath);
            System.out.println("Keys: " + jiraKeys);
        } catch (IOException e) {
            System.err.println("Error reading Jira keys file: " + e.getMessage());
            filterEnabled = false;
        }
    }

    public static void setJiraKeys(String... keys) {
        jiraKeys.clear();
        for (String key : keys) {
            if (key != null && !key.trim().isEmpty()) {
                jiraKeys.add(key.trim().toUpperCase());
            }
        }
        filterEnabled = !jiraKeys.isEmpty();
        System.out.println("Set Jira keys filter: " + jiraKeys);
    }

    public static void clearFilter() {
        jiraKeys.clear();
        filterEnabled = false;
        System.out.println("Jira filter cleared - all tests will run");
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        if (!filterEnabled || jiraKeys.isEmpty()) {
            System.out.println("No Jira filter applied - running all tests");
            return methods;
        }

        List<IMethodInstance> filteredMethods = new ArrayList<>();

        for (IMethodInstance method : methods) {
            Method testMethod = method.getMethod().getConstructorOrMethod().getMethod();
            JiraTest annotation = testMethod.getAnnotation(JiraTest.class);

            if (annotation != null) {
                String testKey = annotation.key().toUpperCase();
                if (jiraKeys.contains(testKey)) {
                    filteredMethods.add(method);
                    System.out.println("✓ Including test: " + testMethod.getName() + " [" + annotation.key() + "]");
                } else {
                    System.out.println("✗ Excluding test: " + testMethod.getName() + " [" + annotation.key() + "]");
                }
            } else {
                System.out.println("✗ Excluding test without @JiraTest: " + testMethod.getName());
            }
        }

        System.out.println("\nFiltered: " + filteredMethods.size() + " tests out of " + methods.size());
        return filteredMethods;
    }
}
