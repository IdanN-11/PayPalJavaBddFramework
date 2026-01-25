# PayPal BDD Selenium Excel Test Automation Framework

A comprehensive **Behavior-Driven Development (BDD)** test automation framework for testing PayPal checkout flows using Selenium, Cucumber, and Excel data-driven testing.

## 📋 Project Overview

This project implements an end-to-end automated testing solution for PayPal payment checkout scenarios. It combines:

- **BDD Framework**: Cucumber for readable, business-facing test scenarios
- **UI Automation**: Selenium WebDriver for browser interactions
- **API Testing**: REST Assured for PayPal API validations
- **Data-Driven Testing**: Apache POI for Excel-based test data management
- **Parallel Execution**: Support for running multiple tests concurrently
- **Reporting**: Allure Reports for detailed test execution reports

### Key Features

✅ **PayPal Sandbox Testing** - Complete checkout and order authorization flow  
✅ **Excel Data-Driven** - Manage test data in Excel sheets  
✅ **API + UI Testing** - Combined API and browser automation  
✅ **JWT Authentication** - Secure token generation for API calls  
✅ **Parallel Execution** - Run tests concurrently using JUnit 5  
✅ **Comprehensive Logging** - Logback configuration for detailed logs  
✅ **Allure Reporting** - Visual test reports with screenshots  

## 🏗️ Project Structure

```
paypal-bdd-selenium-excel/
├── src/test/java/com/example/paypal/
│   ├── config/              # Configuration management
│   │   └── PayPalConfig.java
│   ├── hooks/               # Cucumber hooks (setup/teardown)
│   │   └── Hooks.java
│   ├── pageobject/          # Page Object Model
│   │   ├── LoginPage.java
│   │   ├── AccountsPage.java
│   │   └── *PageOR.java     # Page Object Repositories
│   ├── POJO/                # Data transfer objects
│   │   ├── PayPal.java
│   │   ├── PurchaseUnits.java
│   │   ├── Items.java
│   │   └── ...
│   ├── steps/               # Cucumber step definitions
│   │   └── ApiSteps.java
│   ├── runners/             # Test runners
│   │   └── ParallelRunner.java
│   └── util/                # Utility classes
│       ├── DriverFactory.java
│       ├── ExcelUtil.java
│       ├── JwtGeneratorRS256.java
│       └── RestHelper.java
├── src/test/resources/
│   ├── features/            # Cucumber feature files
│   │   └── paypal_checkout.feature
│   ├── config/              # Configuration files
│   │   ├── config.properties
│   │   └── env.properties
│   ├── testdata/            # Test data files
│   │   └── payloads/
│   └── logback.xml          # Logging configuration
└── pom.xml                  # Maven build configuration
```

## 🚀 Getting Started

### Prerequisites

- **Java 21 LTS** (or Java 17+)
- **Maven 3.8.0+**
- **Chrome/Firefox** browser
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd paypal-bdd-selenium-excel
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure environment**
   - Update `src/test/resources/config/config.properties` with your settings
   - Update `src/test/resources/config/env.properties` with PayPal credentials

### Running Tests

**Run all tests:**
```bash
mvn clean test
```

**Run specific feature file:**
```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
```

**Generate Allure Report:**
```bash
mvn clean test allure:report
allure serve target/allure-results
```

## 📊 Test Scenarios

The framework validates:

- ✓ PayPal access token generation
- ✓ Order creation with product details
- ✓ Order confirmation
- ✓ User login and authentication
- ✓ Payment account selection
- ✓ Order authorization
- ✓ Order status validation

## 🔧 Key Technologies

| Component | Version | Purpose |
|-----------|---------|---------|
| Cucumber | 7.20.0 | BDD framework |
| Selenium | 4.27.0 | Web automation |
| REST Assured | 5.5.0 | API testing |
| JUnit 5 | 5.11.0 | Test execution |
| Apache POI | 5.3.0 | Excel handling |
| Allure | 2.29.0 | Reporting |
| JJWT | 0.12.3 | JWT token generation |
| Logback | 1.5.6 | Logging |

##  Configuration

Update these files before running tests:

**config.properties:**
```properties
browser=chrome
implicit.wait=10
explicit.wait=10
headless=false
base.url=https://www.sandbox.paypal.com
```

**env.properties:**
```properties
client_id=YOUR_PAYPAL_CLIENT_ID
client_secret=YOUR_PAYPAL_CLIENT_SECRET
api.base.url=https://api-m.sandbox.paypal.com
```

## 📝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m 'Add new feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a Pull Request

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

## 👤 Contact & Support

For issues or questions:
- Open an issue on GitHub
- Contact the development team
- Check existing documentation

## 🎯 Roadmap

- [ ] Upgrade to Java 21 LTS
- [ ] Add GraphQL API testing
- [ ] Implement visual regression testing
- [ ] Add mobile testing support
- [ ] Enhance CI/CD pipeline
- [ ] Create custom reporting dashboard

---

**Last Updated:** January 22, 2026  
**Status:** Active Development
