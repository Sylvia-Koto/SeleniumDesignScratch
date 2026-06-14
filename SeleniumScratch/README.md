# SeleniumScratch — E-Commerce Test Automation

Selenium Java test automation framework for the [Rahul Shetty Academy e-commerce app](https://rahulshettyacademy.com/client).  
Built with TestNG, Cucumber, Page Object Model, and ExtentReports.

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 17 |
| Selenium | 4.33.0 |
| TestNG | 7.11.0 |
| Cucumber | 7.22.0 |
| ExtentReports | 5.1.2 |
| Maven | 3.x |

---

## Project Structure

```
SeleniumScratch/
├── src/test/java/SylviaAcademy/
│   ├── base/           # BaseTest — driver lifecycle + generic waits
│   ├── factory/        # DriverFactory + DriverManager (ThreadLocal)
│   ├── pages/          # Page Object Model (LoginPage, DashboardPage, CartPage)
│   ├── resources/      # Listeners, Retry analyzer, ExtentReports config
│   ├── runner/         # CucumberRunner
│   ├── steps/          # Cucumber step definitions
│   ├── tests/          # TestNG test classes
│   └── utils/          # ConfigReader
├── src/test/resources/
│   ├── config.properties
│   └── features/       # Cucumber feature files
└── testng.xml
```

---

## Features

- **Page Object Model** with PageFactory (`@FindBy`)
- **Multi-browser support** — Chrome, Firefox, Edge, Safari
- **Headless mode** via `-Dheadless=true`
- **Data-driven testing** with TestNG `@DataProvider`
- **Cucumber BDD** — feature files + step definitions
- **Retry analyzer** — retries flaky tests once on failure
- **ExtentReports** — HTML report generated in `reports/`
- **Screenshot on failure** — captured and embedded in report
- **ThreadLocal driver** — safe for parallel execution

---

## Test Scenarios

### TestNG (`testng.xml`)

| Test | Description |
|---|---|
| `LoginTest.testLoginSuccess` | Login with valid credentials → redirect to dashboard |
| `LoginTest.testLoginFailure` | Login with invalid inputs → validation error messages |
| `ResearchTest.testProductsDisplayedOnDashboard` | Login → verify products load on dashboard |
| `SubmitOrderTest.testAddProductToCart` | Login → add IPHONE 13 PRO → verify it appears in cart |

### Cucumber (`features/SubmitOrder.feature`)

```gherkin
Scenario: Add IPHONE 13 PRO to cart
  Given I am logged in with valid credentials
  When I add the product "IPHONE 13 PRO" to my cart
  And I navigate to the cart
  Then the product "IPHONE 13 PRO" should be visible in the cart
```

---

## Configuration

Edit `src/test/resources/config.properties`:

```properties
browser=chrome
url=https://rahulshettyacademy.com/client
username=your@email.com
password=yourPassword
```

---

## How to Run

**All tests (TestNG):**
```bash
mvn test
```

**Headless mode:**
```bash
mvn test -Dheadless=true
```

**Specific browser:**
```bash
mvn test -Dbrowser=firefox
```

**Cucumber only:**
```bash
mvn test -Dtest=CucumberRunner
```

**Specific test class:**
```bash
mvn test -Dtest=LoginTest
```

Reports are generated in `reports/index.html` after each run.

---

## Author

**Sylvia N'Guessan** — QA Automation Engineer
