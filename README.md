# 📚 SauceDemo Selenium Test Automation - Complete Guide

**Hướng dẫn đầy đủ để chạy Selenium automation tests cho SauceDemo**

---

## 📑 Mục lục

1. [Giới thiệu](#-giới-thiệu)
2. [Cài đặt](#-cài-đặt)
3. [Chạy Tests](#-chạy-tests)
4. [Jira Test Runner](#-jira-test-runner)
5. [Parallel Testing](#-parallel-testing)
6. [ExtentReports](#-extentreports)
7. [Custom Wait Times](#-custom-wait-times)
8. [Troubleshooting](#-troubleshooting)

---

## 🎯 Giới thiệu

### Tính năng chính

- ✅ Automation testing cho Login flow với tất cả edge cases
- ✅ Automation testing cho Add to Cart và Checkout flow
- ✅ Chạy parallel trên Chrome và Firefox
- ✅ Tự động chụp screenshot khi test fail (embedded trong HTML)
- ✅ Jira integration - chạy tests theo Jira keys
- ✅ ExtentReports - HTML reports đẹp với charts
- ✅ GitHub Actions CI/CD
- ✅ Chrome incognito mode - Không bị popup "Save password"

### Test Cases

**LoginTest (12 tests):**
- ✅ Valid login
- ✅ Invalid username/password
- ✅ Empty fields
- ✅ Locked user
- ✅ Problem user
- ✅ Performance glitch user
- ✅ Logout functionality

**CartTest (10 tests):**
- ✅ Add single/multiple items
- ✅ Remove items
- ✅ Cart badge counter
- ✅ Checkout flow
- ✅ Form validation
- ✅ Complete purchase

---

## 🛠️ Cài đặt

### Yêu cầu hệ thống

- Java JDK 11+
- Maven 3.6+
- Chrome browser (latest)
- Firefox browser (latest)

### Bước cài đặt

1. **Clone repository:**
```bash
git clone <repository-url>
cd AutoHomeworkSeleniumVin
```

2. **Cài đặt dependencies:**
```bash
mvn clean install
```

3. **Verify cài đặt:**
```bash
mvn -version
java -version
```

**Lưu ý:** Không cần download ChromeDriver/GeckoDriver thủ công! WebDriverManager tự động làm điều này.

---

## 🚀 Chạy Tests

### 1. Chạy TẤT CẢ tests (Parallel Chrome + Firefox)

```bash
mvn clean test
```

**Kết quả:**
- 44 tests (22 tests x 2 browsers)
- Thời gian: ~5-7 phút
- Report tự động generate

### 2. Chạy trên 1 browser

**Chrome only:**
```bash
mvn clean test -Dbrowser=chrome
```

**Firefox only:**
```bash
mvn clean test -Dbrowser=firefox
```

### 3. Chạy 1 test class

```bash
mvn test -Dtest=LoginTest
```

### 4. Chạy 1 test method

```bash
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

### 5. Chạy từ IDE

**IntelliJ IDEA / Eclipse:**
1. Right-click vào file `testng.xml`
2. Chọn "Run testng.xml"
3. Xem results trong console

---

## 🎯 Jira Test Runner

### Tổng quan

Chạy tests dựa trên **Jira issue keys** để:
- Test specific Jira tickets
- Filter tests theo sprint/epic
- Run regression suite
- Bug verification

### Jira Keys Mapping

| Jira Key | Test Method | Description |
|----------|-------------|-------------|
| **LoginTest** |||
| SAUCE-101 | testValidLogin | Valid login with standard_user |
| SAUCE-102 | testInvalidUsername | Invalid username |
| SAUCE-103 | testInvalidPassword | Invalid password |
| SAUCE-104 | testEmptyUsername | Empty username field |
| SAUCE-105 | testEmptyPassword | Empty password field |
| SAUCE-106 | testBothFieldsEmpty | Both fields empty |
| SAUCE-107 | testLockedOutUser | Locked out user |
| SAUCE-108 | testProblemUser | Problem user login |
| SAUCE-109 | testPerformanceGlitchUser | Performance glitch user |
| SAUCE-110 | testErrorUser | Error user login |
| SAUCE-111 | testVisualUser | Visual user login |
| SAUCE-112 | testLogoutFunctionality | Logout functionality |
| **CartTest** |||
| SAUCE-201 | testAddSingleItemToCart | Add 1 item to cart |
| SAUCE-202 | testAddMultipleItemsToCart | Add 3 items to cart |
| SAUCE-203 | testRemoveItemFromInventory | Remove item from inventory |
| SAUCE-204 | testRemoveItemFromCart | Remove item from cart |
| SAUCE-205 | testCartBadgeCounter | Verify cart badge updates |
| SAUCE-206 | testNavigateToCartPage | Navigate to cart |
| SAUCE-207 | testCheckoutWithSingleItem | Checkout with 1 item |
| SAUCE-208 | testCheckoutWithMultipleItems | Checkout with 3 items |
| SAUCE-209 | testCheckoutValidationErrors | Checkout form validation |
| SAUCE-210 | testCompleteCheckoutFlow | Full checkout flow |

---

### Cách 1: Chạy từ File (RECOMMENDED)

#### **Bước 1: Tạo file Jira keys**

Tạo file `my-tests.txt`:

```
# My Test Cases - Jira Keys
# Lines starting with # are comments

# Login Tests
SAUCE-101
SAUCE-102
SAUCE-103

# Cart Tests
SAUCE-201
SAUCE-204
```

**Format:**
- Mỗi dòng = 1 Jira key
- Lines bắt đầu với `#` = comment
- Empty lines = bỏ qua
- Keys tự động uppercase

#### **Bước 2: Chạy tests**

**Maven:**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--file my-tests.txt --browser chrome"
```

**Firefox:**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--file my-tests.txt --browser firefox"
```

---

### Cách 2: Chạy từ Command Line (Ad-hoc)

**Single test:**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--keys SAUCE-101 --browser chrome"
```

**Multiple tests (comma-separated):**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--keys SAUCE-101,SAUCE-102,SAUCE-201 --browser chrome"
```

---

### Cách 3: SimpleJiraRunner (Easiest for IDE)

#### **Bước 1: Open SimpleJiraRunner.java**

File: `src/test/java/com/saucedemo/runner/SimpleJiraRunner.java`

#### **Bước 2: Edit Jira keys array**

```java
public static void main(String[] args) {
    String[] jiraKeys = {
        "SAUCE-101",    // testValidLogin
        "SAUCE-201"     // testAddSingleItemToCart
    };

    String browser = "chrome";  // or "firefox"

    JiraTestFilter.setJiraKeys(jiraKeys);
    // ... rest of code
}
```

#### **Bước 3: Run từ IDE**

**IntelliJ IDEA:**
- Right-click → Run 'SimpleJiraRunner.main()'

**Eclipse:**
- Right-click → Run As → Java Application

**VS Code:**
- Click "Run" button above `main()`

---

### Use Cases

**Scenario 1: Smoke Tests**
```
# smoke-tests.txt
SAUCE-101  # Valid login
SAUCE-201  # Add to cart
SAUCE-210  # Complete checkout
```

**Scenario 2: Sprint Tests**
```
# sprint-5-tests.txt
SAUCE-102
SAUCE-103
SAUCE-201
SAUCE-202
```

**Scenario 3: Bug Verification**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--keys SAUCE-201,SAUCE-204 --browser chrome"
```

---

## ⚡ Parallel Testing

### Tổng quan

Tests chạy **PARALLEL** (đồng thời) trên Chrome và Firefox:
- ✅ Thread-safe với ThreadLocal WebDriver
- ✅ Tiết kiệm 50% thời gian
- ✅ Tự động quản lý drivers
- ✅ Independent test execution

### TestNG Configuration

File: `testng.xml`

```xml
<suite name="SauceDemo Test Suite" parallel="tests" thread-count="2">
    <listeners>
        <listener class-name="com.saucedemo.listeners.ExtentTestListener"/>
    </listeners>

    <!-- Thread 1: Chrome Tests -->
    <test name="Chrome Tests" parallel="classes">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="com.saucedemo.tests.LoginTest"/>
            <class name="com.saucedemo.tests.CartTest"/>
        </classes>
    </test>

    <!-- Thread 2: Firefox Tests -->
    <test name="Firefox Tests" parallel="classes">
        <parameter name="browser" value="firefox"/>
        <classes>
            <class name="com.saucedemo.tests.LoginTest"/>
            <class name="com.saucedemo.tests.CartTest"/>
        </classes>
    </test>
</suite>
```

### Timeline thực tế

```
┌─── Time: 0s ──────────────────────────────┐
│                                            │
│  Thread 1 (Chrome)    Thread 2 (Firefox)  │
│  ═══════════════════  ═══════════════════ │
│                                            │
│  [START] Chrome       [START] Firefox     │
│  ↓                    ↓                    │
│  LoginTest (12)       LoginTest (12)      │
│  CartTest (10)        CartTest (10)       │
│  ↓                    ↓                    │
│  [FINISH]             [FINISH]            │
│                                            │
└─── Time: ~7 minutes ──────────────────────┘

✅ Parallel: ~7 phút
❌ Sequential: ~14 phút
⚡ Tiết kiệm: 50%!
```

### Performance Comparison

| Cách chạy | Thời gian | Tests executed |
|-----------|-----------|----------------|
| **Parallel (Chrome + Firefox)** | ~7 phút | 44 tests |
| Sequential (Chrome → Firefox) | ~14 phút | 44 tests |
| Chrome only | ~7 phút | 22 tests |
| Firefox only | ~7 phút | 22 tests |

---

## 📊 ExtentReports

### Tổng quan

ExtentReports tạo **HTML reports đẹp** với:
- ✅ Thống kê pass/fail/skip
- ✅ Charts và graphs
- ✅ Step-by-step logs
- ✅ Screenshots embedded (Base64)
- ✅ Jira keys tracking
- ✅ Timeline view
- ✅ Category grouping

### Report Location

Sau khi chạy tests:

```
test-output/extent-reports/TestReport_YYYYMMDD_HHMMSS.html
```

**Ví dụ:**
```
test-output/extent-reports/TestReport_20260123_120530.html
```

### Mở Report

1. Navigate đến `test-output/extent-reports/`
2. Double-click file `.html` mới nhất
3. Report mở trong browser

### Report Features

**Dashboard View:**
- Total tests run
- Pass/Fail/Skip statistics
- Pass percentage
- Test duration
- Pie charts & bar graphs

**Test Details:**
- Test name and description
- Jira key (if tagged)
- Priority level
- Browser used
- Execution time
- Step-by-step logs

**Failed Tests:**
- Failure reason
- Stack trace
- Screenshot attached (EMBEDDED in HTML, không cần external files!)
- Quick filter

### Screenshots

Screenshots tự động:
- ✅ Captured when test fails
- ✅ Embedded as Base64 trong HTML
- ✅ Không cần external files
- ✅ Backup file cũng được save

**Location:**
```
test-output/screenshots/testName_browser_timestamp.png
```

### Expected Output

**Console:**
```
╔═══════════════════════════════════════════════════════════╗
║       Simple Jira Test Runner - SauceDemo                ║
╚═══════════════════════════════════════════════════════════╝

🔖 Running tests for Jira keys: [SAUCE-101, SAUCE-201]
🌐 Browser: chrome

▶️  Starting test: testValidLogin
  🔧 Attempted to dismiss Chrome password popup with ESC key
  ✅ No blocking overlays detected
✅ Test PASSED: testValidLogin

▶️  Starting test: testAddSingleItemToCart
  📦 Adding product: Sauce Labs Backpack
✅ Test PASSED: testAddSingleItemToCart

╔═══════════════════════════════════════════════════════════╗
║  📊 ExtentReports HTML Report Generated Successfully!    ║
╚═══════════════════════════════════════════════════════════╝
📂 Location: test-output/extent-reports/
📸 Screenshots: test-output/screenshots/
```

---

## ⏱️ Custom Wait Times

### Tổng quan

Tất cả Page Objects kế thừa `BasePage` với:
- ✅ Default wait time: 20 giây (từ `config.properties`)
- ✅ Có thể custom wait cho từng page
- ✅ Giảm code duplication

### Sử dụng Default Wait (99% trường hợp)

```java
@Test
public void testNormalLogin() {
    // Dùng wait time mặc định (20 giây)
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login("standard_user", "secret_sauce");

    InventoryPage inventoryPage = new InventoryPage(driver);
    Assert.assertTrue(inventoryPage.isInventoryPageDisplayed());
}
```

### Custom Wait cho Page cụ thể

**Ví dụ 1: Page cần wait lâu hơn (30s)**

```java
@Test
public void testSlowPage() {
    // Page này cần wait 30 giây
    LoginPage loginPage = new LoginPage(driver, 30);
    loginPage.login("standard_user", "secret_sauce");

    // Inventory page vẫn dùng default (20s)
    InventoryPage inventoryPage = new InventoryPage(driver);
}
```

**Ví dụ 2: Page cần wait ngắn (5s)**

```java
@Test
public void testFastCheckout() {
    LoginPage loginPage = new LoginPage(driver);
    // ... login and add to cart

    // Checkout page nhanh, chỉ cần 5 giây
    CheckoutStepOnePage checkoutPage = new CheckoutStepOnePage(driver, 5);
    checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
}
```

### Khi nào nên dùng Custom Wait?

✅ **Nên dùng:**
- Server/Application chậm trong môi trường cụ thể
- Một page cụ thể load lâu hơn
- Testing trên staging/production chậm
- Page có AJAX calls phức tạp

❌ **Không nên dùng:**
- Application chạy bình thường
- Wait mặc định (20s) đã đủ
- Không có lý do rõ ràng

### Tóm tắt

| Cách sử dụng | Code | Wait Time |
|-------------|------|-----------|
| **Mặc định** | `new LoginPage(driver)` | 20 giây |
| **Custom** | `new LoginPage(driver, 30)` | 30 giây |
| **Global change** | Sửa `config.properties` | Áp dụng tất cả |

---

## 🔧 Troubleshooting

### Chrome Password Popup (ĐÃ FIX!)

**Problem:** Chrome shows "Save password?" popup blocking interactions

**Solution:** ✅ FIXED with `--incognito` mode

File: `DriverFactory.java`

```java
case "chrome":
    ChromeOptions chromeOptions = new ChromeOptions();

    // NUCLEAR OPTION - Incognito mode
    chromeOptions.addArguments("--incognito");
    chromeOptions.addArguments("--disable-save-password-bubble");

    // Comprehensive preferences
    HashMap<String, Object> chromePrefs = new HashMap<>();
    chromePrefs.put("credentials_enable_service", false);
    chromePrefs.put("profile.password_manager_enabled", false);
    chromeOptions.setExperimentalOption("prefs", chromePrefs);
```

**Result:**
- ✅ Chrome opens in incognito mode
- ✅ NO password save prompts
- ✅ Tests run smoothly

---

### Screenshots không captured

**Problem:** Screenshots không được chụp khi test fail

**Solution:** ✅ FIXED - Using `DriverFactory.getDriver()`

File: `ExtentTestListener.java`

```java
@Override
public void onTestFailure(ITestResult result) {
    // Use DriverFactory to get current thread's driver
    WebDriver driver = DriverFactory.getDriver();

    if (driver != null) {
        String base64 = ScreenshotUtil.captureScreenshotAsBase64(driver);
        test.addScreenCaptureFromBase64String(base64, "Failure Screenshot");
    }
}
```

---

### Screenshots không embedded trong HTML

**Problem:** Screenshots là external files, không embedded

**Solution:** ✅ FIXED - Using Base64 encoding

File: `ScreenshotUtil.java`

```java
public static String captureScreenshotAsBase64(WebDriver driver) {
    try {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        String base64 = screenshot.getScreenshotAs(OutputType.BASE64);
        return base64;
    } catch (Exception e) {
        return null;
    }
}
```

**Result:**
- ✅ Screenshots embedded directly in HTML
- ✅ Không cần external files
- ✅ Report self-contained

---

### Browser không tìm thấy

**Chrome not found:**
```bash
# Download Chrome
https://www.google.com/chrome/
```

**Firefox not found:**
```bash
# Download Firefox
https://www.mozilla.org/firefox/
```

WebDriverManager sẽ tự động tải driver.

---

### Tests chạy tuần tự thay vì parallel

**Kiểm tra:**
- ✅ `testng.xml` có `parallel="tests"` và `thread-count="2"`
- ✅ Chạy đúng command: `mvn clean test`
- ❌ KHÔNG phải: `mvn test -Dbrowser=chrome`

---

### Report không generated

**Solution:**
1. Check listener trong `testng.xml`:
```xml
<listeners>
    <listener class-name="com.saucedemo.listeners.ExtentTestListener"/>
</listeners>
```

2. Verify dependency trong `pom.xml`:
```xml
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.1.1</version>
</dependency>
```

3. Run clean compile:
```bash
mvn clean compile test-compile
```

---

### Tests chạy quá lâu

**Solution:** Giảm wait time trong `config.properties`:

```properties
# From:
implicit.wait=10
explicit.wait=20

# To:
implicit.wait=5
explicit.wait=10
```

---

## 📂 Project Structure

```
AutoHomeworkSeleniumVin/
├── src/
│   ├── main/java/com/saucedemo/
│   │   └── config/
│   │       └── ConfigReader.java
│   └── test/java/com/saucedemo/
│       ├── annotations/
│       │   └── JiraTest.java
│       ├── base/
│       │   ├── BasePage.java
│       │   └── BaseTest.java
│       ├── listeners/
│       │   └── ExtentTestListener.java
│       ├── pages/
│       │   ├── LoginPage.java
│       │   ├── InventoryPage.java
│       │   ├── CartPage.java
│       │   ├── CheckoutStepOnePage.java
│       │   ├── CheckoutStepTwoPage.java
│       │   └── CheckoutCompletePage.java
│       ├── runner/
│       │   ├── JiraTestRunner.java
│       │   └── SimpleJiraRunner.java
│       ├── tests/
│       │   ├── LoginTest.java
│       │   └── CartTest.java
│       └── utils/
│           ├── DriverFactory.java
│           ├── ScreenshotUtil.java
│           ├── JiraTestFilter.java
│           ├── ExtentReportManager.java
│           └── ExtentLogger.java
├── .github/workflows/
│   └── selenium-tests.yml
├── test-output/
│   ├── extent-reports/
│   │   └── TestReport_*.html
│   └── screenshots/
│       └── *.png
├── testng.xml
├── pom.xml
├── my-tests.txt
└── COMPLETE_GUIDE.md (THIS FILE)
```

---

## 🎓 Quick Commands Reference

### Basic Commands

```bash
# Compile project
mvn clean compile

# Run all tests (parallel)
mvn clean test

# Run on Chrome only
mvn clean test -Dbrowser=chrome

# Run on Firefox only
mvn clean test -Dbrowser=firefox

# Run specific test class
mvn test -Dtest=LoginTest

# Run specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

### Jira Test Runner Commands

```bash
# Run from file
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--file my-tests.txt --browser chrome"

# Run specific keys
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--keys SAUCE-101,SAUCE-201 --browser chrome"

# Run with Firefox
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--file my-tests.txt --browser firefox"
```

---

## ✅ Pre-flight Checklist

**Trước khi chạy tests:**

- [ ] Chrome browser đã cài đặt
- [ ] Firefox browser đã cài đặt (optional)
- [ ] Maven đã cài đặt: `mvn -version`
- [ ] Java JDK 11+ đã cài đặt: `java -version`
- [ ] Project đã compile: `mvn clean compile`

**Sẵn sàng? Chạy ngay:**
```bash
mvn clean test
```

---

## 🎯 Best Practices

### 1. Luôn chạy full parallel test suite trước khi commit
```bash
mvn clean test
```

### 2. Kiểm tra test results
Mở `test-output/extent-reports/TestReport_*.html`

### 3. Review screenshots khi có failures
Check `test-output/screenshots/`

### 4. Đảm bảo tests độc lập
- Mỗi test chạy được riêng lẻ
- Không phụ thuộc vào test khác
- Sử dụng `@BeforeMethod` setup fresh state

### 5. Organize Jira key files
```
jira-keys/
├── smoke-tests.txt
├── sprint-1-tests.txt
├── sprint-2-tests.txt
└── regression-tests.txt
```

---

## 📋 Configuration

### config.properties

Location: `src/test/resources/config.properties`

```properties
# Application URL
app.url=https://www.saucedemo.com/

# Wait times (seconds)
implicit.wait=10
explicit.wait=20

# Screenshot settings
screenshot.dir=test-output/screenshots/
screenshot.enabled=true

# Test credentials
valid.username=standard_user
valid.password=secret_sauce
```

---

## 🔗 Related Documentation

- **README.md** - Project overview
- **USAGE_EXAMPLES.md** - Custom wait examples
- **PARALLEL_TESTING_GUIDE.md** - Parallel execution details
- **JIRA_TEST_RUNNER_GUIDE.md** - Jira integration details
- **EXTENT_REPORTS_GUIDE.md** - Reporting details
- **QUICK_TEST.md** - Quick verification guide

---

## 🎉 Summary

### What You Get:

✅ **Complete test automation framework**
- Page Object Model
- 22 comprehensive test cases
- Parallel execution on 2 browsers

✅ **Jira integration**
- Tag tests with Jira keys
- Run tests by file or command line
- Filter by sprint/epic/feature

✅ **Beautiful HTML reports**
- ExtentReports with charts
- Embedded screenshots (Base64)
- Timeline and category views

✅ **No popup issues**
- Chrome incognito mode
- No password save prompts
- Smooth test execution

✅ **Fast execution**
- Parallel testing saves 50% time
- ThreadLocal WebDriver
- Independent test runs

---

## 📞 Quick Start

**1. Clone & Install:**
```bash
git clone <repo>
cd AutoHomeworkSeleniumVin
mvn clean install
```

**2. Run All Tests:**
```bash
mvn clean test
```

**3. Run Specific Jira Tests:**
```bash
# Create my-tests.txt with:
SAUCE-101
SAUCE-201

# Run:
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.JiraTestRunner" -Dexec.args="--file my-tests.txt --browser chrome"
```

**4. View Reports:**
```
Open: test-output/extent-reports/TestReport_*.html
```

---

## 🚀 Ready to Test!

**Tất cả đã được setup sẵn sàng!**

- ✅ Chrome incognito mode → Không popup
- ✅ Parallel execution → Nhanh 2x
- ✅ ExtentReports → Reports đẹp
- ✅ Jira integration → Flexible testing
- ✅ Screenshots embedded → Self-contained reports

**Chạy ngay:**
```bash
mvn clean test
```

**Hoặc với Jira keys:**
```bash
mvn exec:java -Dexec.mainClass="com.saucedemo.runner.SimpleJiraRunner"
```

---

## 📧 Support

Nếu gặp vấn đề:
1. Check Troubleshooting section
2. Review configuration files
3. Verify all prerequisites installed
4. Check console output for errors

---

**🎊 HAPPY TESTING! 🎊**

---

*Last updated: 2026-01-23*
*Framework version: 1.0.0*
*Author: Automation Team*
