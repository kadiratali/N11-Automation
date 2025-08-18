# N11 Web Automation

This project presents a robust and scalable web automation framework designed to streamline and accelerate the testing process. Built with the **Page Object Model (POM)** design pattern, the framework ensures a clean, maintainable, and reusable code base.

A key feature of this framework is its support for **parallel test execution**, which significantly reduces the overall test run time, making it ideal for Continuous Integration/Continuous Delivery (CI/CD) environments.

## Features and Technologies
 - Paralell Execution scenarios
 - Page Object Model
 - TestNG
 - Selenium
 - Maven
 - SLF4j
 - Allure
 - Allure Report
 - Allure Server
 - Allure CLI

## Installation
 - Install Java 21
 - Install Maven
 - Install Allure

## Running Tests
```
  - mvn clean install -DskipTests  
  - mvn test -Dcucumber.filter.tags="@Regression"
```

## Creating Report

After running your tests, you can generate and view a detailed test report using Allure. The report is generated from the test results and can be opened in your browser.
```
 allure generate [results_folder]/allure-results -o target/r
 allure open target/r
```

## LOAD TEST JMETER
    Running the JMeter Test
The performance test scenario is located at src/test/resources/SearchJmeter/n11Search.jmx within the project. To run this test from the command line (in non-GUI mode), use the following command:

```
 jmeter -n -t src/test/resources/SearchJmeter/n11Search.jmx -l test-results.jtl
```
-n: Runs JMeter in non-GUI mode.

-t: Specifies the path to the .jmx test file.

-l: Specifies the name of the .jtl (JMeter Test Log) file where the results will be saved.

This command will execute the test scenario and save the results to a file named test-results.jtl. You can then use this log file to generate detailed reports.

    


## License

MIT