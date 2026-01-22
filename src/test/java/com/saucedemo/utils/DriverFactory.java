package com.saucedemo.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;

import com.saucedemo.config.ConfigReader;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(String browser) {
        WebDriver webDriver = null;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                // ═══════════════════════════════════════════════════════
                //  NUCLEAR OPTION - Disable ALL password prompts
                // ═══════════════════════════════════════════════════════

                // Use incognito mode (no password saving in incognito)
                chromeOptions.addArguments("--incognito");

                // Window settings
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");

                // Disable password features (multiple flags for safety)
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.addArguments("--disable-password-manager-reauthentication");

                // Remove automation detection
                chromeOptions.setExperimentalOption("excludeSwitches",
                    new String[]{"enable-automation", "enable-logging"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);

                // Disable blink features
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

                // Additional stability flags
                chromeOptions.addArguments("--no-first-run");
                chromeOptions.addArguments("--no-default-browser-check");
                chromeOptions.addArguments("--disable-infobars");

                // Comprehensive preferences to disable password manager
                HashMap<String, Object> chromePrefs = new HashMap<>();
                chromePrefs.put("credentials_enable_service", false);
                chromePrefs.put("profile.password_manager_enabled", false);
                chromePrefs.put("profile.default_content_setting_values.notifications", 2);
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("autofill.profile_enabled", false);
                chromePrefs.put("password_manager_enabled", false);
                chromeOptions.setExperimentalOption("prefs", chromePrefs);

                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");

                // Disable Firefox password manager popup
                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addPreference("signon.autofillForms", false);

                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Browser " + browser + " is not supported");
        }

        webDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        webDriver.manage().window().maximize();

        driver.set(webDriver);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
