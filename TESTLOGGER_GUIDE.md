# 🔍 TestLogger - Complete Guide

## 📝 Tổng quan

**TestLogger** là class logging mạnh mẽ được thiết kế để debug và tạo detailed reports trong automation tests.

### Tính năng chính

✅ **Multiple log levels** - DEBUG, INFO, WARN, ERROR, PASS, FAIL
✅ **Variable & Object logging** - Log biến, collections, maps
✅ **Step creation** - Tự động đánh số steps
✅ **Screenshot attachment** - Capture và embed screenshots
✅ **Exception logging** - Full stack trace
✅ **Table logging** - HTML tables trong report
✅ **Timing & Performance** - Track execution time
✅ **Element logging** - Log Selenium element details
✅ **Console + HTML Report** - Log vào cả 2 nơi

---

## 🚀 Quick Start

### Import class

```java
import com.saucedemo.utils.TestLogger;
```

### Basic usage

```java
@Test
public void testLogin() {
    TestLogger.initTest("Login Test");

    TestLogger.step("Navigate to login page");
    TestLogger.info("Opening application");

    TestLogger.variable("username", "standard_user");

    TestLogger.pass("Login successful");

    TestLogger.finalizeTest("Login Test", true);
}
```

---

## 📚 Detailed Features

### 1. Log Levels

#### DEBUG - Detailed debugging info (only in report by default)

```java
TestLogger.debug("Current page state: " + state);
TestLogger.debug("elementCount", elements.size());
```

**Enable debug in console:**
```bash
mvn test -Dtest.debug=true
```

#### INFO - General information

```java
TestLogger.info("Navigating to checkout page");
TestLogger.info("User logged in successfully");
```

#### WARN - Warnings (không fail test)

```java
TestLogger.warn("Page loaded slowly - 5 seconds");
TestLogger.warn("Optional field left empty");
```

#### ERROR - Errors (thường dùng khi handle exceptions)

```java
TestLogger.error("Failed to find element: " + elementId);
TestLogger.error("API returned status 500");
```

#### PASS - Success messages

```java
TestLogger.pass("Item added to cart successfully");
TestLogger.pass("Checkout completed");
```

#### FAIL - Failure messages

```java
TestLogger.fail("Expected count: 3, but got: 0");
TestLogger.fail("Login button not clickable");
```

---

### 2. Step Creation

#### Numbered steps (auto-increment)

```java
@Test
public void testCheckout() {
    TestLogger.initTest("Checkout Test"); // Reset counter

    TestLogger.step("Login to application");
    // ... code

    TestLogger.step("Add items to cart");
    // ... code

    TestLogger.step("Complete checkout");
    // ... code
}
```

**Output:**
```
Step 1: Login to application
Step 2: Add items to cart
Step 3: Complete checkout
```

#### Create step with return value (for nested logging)

```java
ExtentTest step1 = TestLogger.step("Login process");
step1.info("Entering username");
step1.info("Entering password");
step1.pass("Login completed");
```

#### Create step without numbering

```java
ExtentTest customStep = TestLogger.createStep("Verify cart state");
customStep.info("Checking cart items");
```

---

### 3. Variable & Object Logging

#### Single variable

```java
String username = "standard_user";
int itemCount = 3;
boolean isVisible = true;

TestLogger.variable("username", username);
TestLogger.variable("itemCount", itemCount);
TestLogger.variable("isVisible", isVisible);
```

**Output:**
```
📦 username = "standard_user"
📦 itemCount = 3
📦 isVisible = true
```

#### Multiple variables

```java
Map<String, Object> vars = new LinkedHashMap<>();
vars.put("username", "standard_user");
vars.put("password", "***hidden***");
vars.put("rememberMe", true);

TestLogger.variables(vars);
```

#### Log collections

```java
List<String> products = Arrays.asList(
    "Sauce Labs Backpack",
    "Sauce Labs Bike Light",
    "Sauce Labs Bolt T-Shirt"
);

TestLogger.logList("Products in Cart", products);
```

**Output:**
```
📋 Products in Cart (3 items):
    [0] Sauce Labs Backpack
    [1] Sauce Labs Bike Light
    [2] Sauce Labs Bolt T-Shirt
```

#### Log maps

```java
Map<String, String> userInfo = new LinkedHashMap<>();
userInfo.put("Username", "standard_user");
userInfo.put("Role", "User");
userInfo.put("Status", "Active");

TestLogger.logMap("User Information", userInfo);
```

**Output:**
```
📋 User Information (3 entries):
    Username = standard_user
    Role = User
    Status = Active
```

---

### 4. Assertions with Logging

```java
int expected = 3;
int actual = cartPage.getItemCount();

TestLogger.assertion(
    "Cart should contain 3 items",
    expected == actual,
    expected,
    actual
);
```

**If passed:**
```
✅ Assertion passed: Cart should contain 3 items
🔍 DEBUG: Expected = 3
🔍 DEBUG: Actual = 3
```

**If failed:**
```
❌ Assertion failed: Cart should contain 3 items
❌ Expected: 3
❌ Actual: 0
```

---

### 5. Exception Logging

#### Log exception with message

```java
try {
    element.click();
} catch (Exception e) {
    TestLogger.exception("Failed to click element", e);
}
```

#### Log exception only

```java
try {
    // ... code
} catch (Exception e) {
    TestLogger.exception(e);
}
```

**Output:**
```
❌ [EXCEPTION] Failed to click element
    Message: element not interactable
    Type: org.openqa.selenium.ElementNotInteractableException
    Stack trace: ...
```

---

### 6. Screenshot Logging

#### Capture screenshot with description

```java
TestLogger.screenshot(driver, "Cart page after adding items");
```

#### Screenshot for specific step

```java
ExtentTest step = TestLogger.step("Verify checkout page");
// ... operations
TestLogger.stepScreenshot(driver, "Checkout page verification");
```

**Result:**
- Screenshot embedded trong HTML report
- Backup file saved: `test-output/screenshots/`

---

### 7. Timing & Performance

#### Track operation duration

```java
TestLogger.startTimer("loginOperation");

// ... perform login
loginPage.login(username, password);

long duration = TestLogger.stopTimer("loginOperation");
```

**Output:**
```
🔍 Timer started: loginOperation
⏱️ Timer [loginOperation] = 2.34s
```

#### Log duration manually

```java
long startTime = System.currentTimeMillis();
// ... operation
long elapsed = System.currentTimeMillis() - startTime;

TestLogger.logDuration("Page load", elapsed);
```

**Output:**
```
⏱️ Page load took 3.12s
```

---

### 8. Selenium Element Logging

#### Log element details

```java
WebElement loginButton = driver.findElement(By.id("login-button"));
TestLogger.logElement("Login Button", loginButton);
```

**Output:**
```
ℹ️ Element: Login Button
📦 Tag = "button"
📦 Text = "Login"
📦 Displayed = true
📦 Enabled = true
📦 Selected = false
```

#### Log action on element

```java
TestLogger.action("Click", "Login button");
loginButton.click();

TestLogger.action("Type", "Username field");
usernameField.sendKeys("standard_user");
```

**Output:**
```
🎯 Action: Click on [Login button]
🎯 Action: Type on [Username field]
```

#### Log navigation

```java
String url = "https://www.saucedemo.com";
TestLogger.navigation(url);
driver.get(url);
```

**Output:**
```
🌐 Navigating to: https://www.saucedemo.com
```

---

### 9. Table Logging

#### Simple table

```java
String[][] data = {
    {"Product", "Price", "Quantity"},
    {"Backpack", "$29.99", "1"},
    {"Bike Light", "$9.99", "2"},
    {"T-Shirt", "$15.99", "1"}
};

TestLogger.table("Cart Items", data);
```

**Output (HTML):**
```
📊 Cart Items:
┌────────────┬────────┬──────────┐
│ Product    │ Price  │ Quantity │
├────────────┼────────┼──────────┤
│ Backpack   │ $29.99 │ 1        │
│ Bike Light │ $9.99  │ 2        │
│ T-Shirt    │ $15.99 │ 1        │
└────────────┴────────┴──────────┘
```

#### Comparison table (Expected vs Actual)

```java
Map<String, String[]> comparisons = new LinkedHashMap<>();
comparisons.put("Item Count", new String[]{"3", "3"});
comparisons.put("Total Price", new String[]{"$55.97", "$55.97"});
comparisons.put("Checkout Button", new String[]{"Enabled", "Enabled"});

TestLogger.comparisonTable("Checkout Verification", comparisons);
```

**Output:**
```
Field             | Expected | Actual
------------------|----------|--------
Item Count        | 3        | 3
Total Price       | $55.97   | $55.97
Checkout Button   | Enabled  | Enabled
```

---

### 10. Code & JSON Logging

#### Log JSON

```java
String jsonResponse = "{\n" +
    "  \"status\": \"success\",\n" +
    "  \"items\": [\"item1\", \"item2\"]\n" +
    "}";

TestLogger.json("API Response", jsonResponse);
```

#### Log code snippet

```java
String code = "driver.findElement(By.id(\"login-button\")).click();";
TestLogger.code("Selenium Command", code, "java");
```

---

### 11. Highlighting & Emphasis

#### Highlight important message

```java
TestLogger.highlight("CRITICAL: Starting payment process");
```

**Output:**
```
⭐⭐⭐ CRITICAL: STARTING PAYMENT PROCESS ⭐⭐⭐
```

#### Separator line

```java
TestLogger.separator();
TestLogger.info("Starting new section");
TestLogger.separator();
```

**Output:**
```
─────────────────────────────────────────────────────────
ℹ️ Starting new section
─────────────────────────────────────────────────────────
```

---

## 🎯 Complete Test Example

```java
@Test
@JiraTest(key = "SAUCE-301", description = "Complete checkout flow")
public void testCompleteCheckoutFlow() {
    // Initialize test
    TestLogger.initTest("Complete Checkout Flow");

    // ═══════════════════════════════════════════════════════
    //  STEP 1: Login
    // ═══════════════════════════════════════════════════════
    ExtentTest step1 = TestLogger.step("Login to application");

    TestLogger.navigation("https://www.saucedemo.com");
    driver.get("https://www.saucedemo.com");

    LoginPage loginPage = new LoginPage(driver);

    String username = ConfigReader.getValidUsername();
    TestLogger.variable("username", username);
    TestLogger.variable("password", "***hidden***");

    TestLogger.startTimer("loginDuration");

    TestLogger.action("Type", "username field");
    loginPage.enterUsername(username);

    TestLogger.action("Type", "password field");
    loginPage.enterPassword(ConfigReader.getValidPassword());

    TestLogger.action("Click", "login button");
    loginPage.clickLoginButton();

    long loginTime = TestLogger.stopTimer("loginDuration");

    InventoryPage inventoryPage = new InventoryPage(driver);
    boolean isDisplayed = inventoryPage.isInventoryPageDisplayed();

    TestLogger.assertion(
        "Inventory page should be displayed",
        isDisplayed,
        true,
        isDisplayed
    );

    Assert.assertTrue(isDisplayed);
    TestLogger.stepScreenshot(driver, "Logged in successfully");
    step1.pass("Login completed in " + loginTime + "ms");

    // ═══════════════════════════════════════════════════════
    //  STEP 2: Add items to cart
    // ═══════════════════════════════════════════════════════
    ExtentTest step2 = TestLogger.step("Add items to cart");

    List<String> items = Arrays.asList(
        "Sauce Labs Backpack",
        "Sauce Labs Bike Light",
        "Sauce Labs Bolt T-Shirt"
    );

    TestLogger.logList("Items to add", items);

    for (int i = 0; i < items.size(); i++) {
        String item = items.get(i);
        TestLogger.info("Adding item " + (i + 1) + ": " + item);

        TestLogger.action("Click Add to Cart", item);
        inventoryPage.addItemToCart(item);

        int cartCount = inventoryPage.getCartItemCount();
        TestLogger.variable("cartCount", cartCount);

        TestLogger.assertion(
            "Cart should have " + (i + 1) + " items",
            cartCount == (i + 1),
            i + 1,
            cartCount
        );
    }

    step2.pass("All items added to cart");

    // ═══════════════════════════════════════════════════════
    //  STEP 3: Navigate to cart
    // ═══════════════════════════════════════════════════════
    ExtentTest step3 = TestLogger.step("Navigate to cart");

    TestLogger.action("Click", "shopping cart icon");
    inventoryPage.clickShoppingCart();

    CartPage cartPage = new CartPage(driver);
    TestLogger.debug("Current URL", driver.getCurrentUrl());

    TestLogger.screenshot(driver, "Cart page with 3 items");
    step3.pass("Navigated to cart");

    // ═══════════════════════════════════════════════════════
    //  STEP 4: Checkout
    // ═══════════════════════════════════════════════════════
    ExtentTest step4 = TestLogger.step("Complete checkout");

    TestLogger.action("Click", "checkout button");
    cartPage.clickCheckout();

    CheckoutStepOnePage checkoutPage = new CheckoutStepOnePage(driver);

    Map<String, Object> checkoutInfo = new LinkedHashMap<>();
    checkoutInfo.put("firstName", "John");
    checkoutInfo.put("lastName", "Doe");
    checkoutInfo.put("postalCode", "12345");

    TestLogger.variables(checkoutInfo);

    checkoutPage.fillCheckoutInformation("John", "Doe", "12345");
    checkoutPage.clickContinue();

    TestLogger.screenshot(driver, "Checkout overview");
    step4.pass("Checkout information entered");

    // ═══════════════════════════════════════════════════════
    //  STEP 5: Verify and complete
    // ═══════════════════════════════════════════════════════
    ExtentTest step5 = TestLogger.step("Verify and complete order");

    CheckoutStepTwoPage overviewPage = new CheckoutStepTwoPage(driver);

    // Create verification table
    String[][] verificationData = {
        {"Field", "Status"},
        {"Items in cart", "✓ 3 items"},
        {"Payment info", "✓ Valid"},
        {"Shipping info", "✓ Valid"},
        {"Total price", "✓ Calculated"}
    };

    TestLogger.table("Order Verification", verificationData);

    TestLogger.action("Click", "finish button");
    overviewPage.clickFinish();

    CheckoutCompletePage completePage = new CheckoutCompletePage(driver);
    boolean isComplete = completePage.isOrderComplete();

    TestLogger.assertion(
        "Order should be completed",
        isComplete,
        true,
        isComplete
    );

    Assert.assertTrue(isComplete);

    TestLogger.screenshot(driver, "Order completed successfully");
    step5.pass("Order completed successfully");

    // ═══════════════════════════════════════════════════════
    //  Summary
    // ═══════════════════════════════════════════════════════
    TestLogger.separator();
    TestLogger.highlight("TEST COMPLETED SUCCESSFULLY");
    TestLogger.separator();

    Map<String, Object> summary = new LinkedHashMap<>();
    summary.put("Items ordered", items.size());
    summary.put("Login time", loginTime + "ms");
    summary.put("Test status", "PASSED");

    TestLogger.logMap("Test Summary", summary);

    TestLogger.finalizeTest("Complete Checkout Flow", true);
}
```

---

## 💡 Best Practices

### 1. Initialize and finalize tests

```java
@Test
public void myTest() {
    TestLogger.initTest("My Test Name");
    // ... test logic
    TestLogger.finalizeTest("My Test Name", true); // or false if failed
}
```

**Benefits:**
- Resets step counter
- Clears timers
- Adds clear boundaries in logs

### 2. Use steps for logical grouping

```java
// ✅ Good - Organized with steps
ExtentTest step1 = TestLogger.step("Login");
// login code

ExtentTest step2 = TestLogger.step("Add to cart");
// add to cart code

// ❌ Bad - No structure
TestLogger.info("Login");
// all code mixed together
```

### 3. Log variables before assertions

```java
// ✅ Good - Clear debugging
int expected = 3;
int actual = cartPage.getItemCount();
TestLogger.variable("expected", expected);
TestLogger.variable("actual", actual);
Assert.assertEquals(actual, expected);

// ❌ Bad - Hard to debug failures
Assert.assertEquals(cartPage.getItemCount(), 3);
```

### 4. Use appropriate log levels

```java
// ✅ Good - Correct log levels
TestLogger.debug("Internal state: " + state);        // Verbose debugging
TestLogger.info("Navigating to page");               // General info
TestLogger.warn("Slow page load detected");          // Warning
TestLogger.error("Element not found");               // Error
TestLogger.pass("Test passed");                      // Success
TestLogger.fail("Assertion failed");                 // Failure

// ❌ Bad - Everything is info
TestLogger.info("Debug: state = " + state);
TestLogger.info("Warning: slow load");
TestLogger.info("Error: not found");
```

### 5. Capture screenshots at key points

```java
// ✅ Good - Screenshots at important steps
TestLogger.stepScreenshot(driver, "After login");
TestLogger.stepScreenshot(driver, "Cart with items");
TestLogger.stepScreenshot(driver, "Checkout complete");

// ❌ Bad - Too many screenshots (clutters report)
TestLogger.screenshot(driver, "Click 1");
TestLogger.screenshot(driver, "Click 2");
TestLogger.screenshot(driver, "Click 3");
```

### 6. Track performance for slow operations

```java
// ✅ Good - Track time for important operations
TestLogger.startTimer("pageLoad");
driver.get(url);
wait.until(ExpectedConditions.titleContains("Inventory"));
TestLogger.stopTimer("pageLoad");

// ❌ Bad - No timing info
driver.get(url);
wait.until(ExpectedConditions.titleContains("Inventory"));
```

### 7. Use tables for complex comparisons

```java
// ✅ Good - Clear comparison table
Map<String, String[]> comparisons = new LinkedHashMap<>();
comparisons.put("Field1", new String[]{expected1, actual1});
comparisons.put("Field2", new String[]{expected2, actual2});
TestLogger.comparisonTable("Verification", comparisons);

// ❌ Bad - Hard to read multiple assertions
TestLogger.info("Expected: " + expected1 + ", Actual: " + actual1);
TestLogger.info("Expected: " + expected2 + ", Actual: " + actual2);
```

### 8. Enable debug mode for detailed logs

```bash
# Development - verbose logging
mvn test -Dtest.debug=true -Dtest=MyTest

# CI/CD - minimal logging
mvn test -Dtest.debug=false
```

---

## 🔧 Configuration

### Enable debug logging

**Option 1: System property**
```bash
mvn test -Dtest.debug=true
```

**Option 2: In test code**
```java
System.setProperty("test.debug", "true");
```

**Option 3: Config file**

Add to `config.properties`:
```properties
test.debug=true
```

Update `ConfigReader.java`:
```java
public static boolean isDebugEnabled() {
    return Boolean.parseBoolean(properties.getProperty("test.debug", "false"));
}
```

---

## 📊 Output Examples

### Console Output

```
  ─────────────────────────────────────────────────────────
  ⭐⭐⭐ STARTING TEST: LOGIN WITH DETAILED LOGGING ⭐⭐⭐
  ─────────────────────────────────────────────────────────

  🚀 Step 1: Navigate to SauceDemo
  ℹ️  [INFO] 14:23:15.123 Opening application URL
  🌐 Navigating to: https://www.saucedemo.com/

  🚀 Step 2: Enter login credentials
  📦 username = "standard_user"
  📦 password = "***hidden***"
  🎯 Action: Type on [username field]
  🎯 Action: Type on [password field]

  🚀 Step 3: Click login button
  🔍 Timer started: loginTime
  🎯 Action: Click on [login button]
  ⏱️ Timer [loginTime] = 1.84s
  ⏱️ Login operation took 1.84s

  🚀 Step 4: Verify inventory page displayed
  ✅ [PASS] 14:23:17.456 Assertion passed: Inventory page should be displayed
  📸 Screenshot captured: Screenshot for: Inventory page after login

  ─────────────────────────────────────────────────────────
  ✅ [PASS] 14:23:18.123 Test Completed: Login with Detailed Logging
  ─────────────────────────────────────────────────────────
```

### HTML Report

Report sẽ hiển thị:
- ✅ Step-by-step execution với colors
- 📊 Tables với proper formatting
- 📸 Embedded screenshots
- 🔍 Debug info (expandable)
- ⏱️ Timing information
- 📦 Variable values với syntax highlighting
- ❌ Exception stack traces (if any)

---

## 🎓 Summary

### When to use TestLogger:

✅ **Always use for:**
- Complex tests với nhiều steps
- Tests cần detailed debugging
- Performance-critical operations
- Tests hay fail và cần investigate

✅ **Optional for:**
- Simple tests (1-2 assertions)
- Smoke tests
- Tests đã stable

### Key takeaways:

1. **Use steps** để organize test logic
2. **Log variables** trước assertions
3. **Track timing** cho slow operations
4. **Capture screenshots** ở key points
5. **Use appropriate log levels**
6. **Initialize/finalize** tests properly
7. **Enable debug mode** khi develop

---

## 📚 API Reference

| Method | Description | Example |
|--------|-------------|---------|
| `debug(msg)` | Debug info (verbose) | `TestLogger.debug("State: " + state)` |
| `info(msg)` | General information | `TestLogger.info("Navigating to page")` |
| `warn(msg)` | Warning message | `TestLogger.warn("Slow load detected")` |
| `error(msg)` | Error message | `TestLogger.error("Element not found")` |
| `pass(msg)` | Success message | `TestLogger.pass("Test passed")` |
| `fail(msg)` | Failure message | `TestLogger.fail("Assertion failed")` |
| `step(desc)` | Create numbered step | `TestLogger.step("Login")` |
| `variable(name, val)` | Log variable | `TestLogger.variable("count", 5)` |
| `assertion(...)` | Log assertion | `TestLogger.assertion("X==Y", true, x, y)` |
| `screenshot(...)` | Capture screenshot | `TestLogger.screenshot(driver, "Cart")` |
| `startTimer(name)` | Start timer | `TestLogger.startTimer("login")` |
| `stopTimer(name)` | Stop timer | `TestLogger.stopTimer("login")` |
| `logList(...)` | Log list | `TestLogger.logList("Items", list)` |
| `logMap(...)` | Log map | `TestLogger.logMap("Data", map)` |
| `table(...)` | Log table | `TestLogger.table("Data", array)` |
| `exception(e)` | Log exception | `TestLogger.exception(e)` |

---

**🎉 Happy Testing with Detailed Logs!**
