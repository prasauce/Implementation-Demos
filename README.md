# Implementation Demo Scripts

This repository is used for implementation demo purposes and provides a few basic examples of how to run different types tests on Sauce Labs. For more advanced examples and best practices, please check out https://github.com/saucelabs-training/demo-java.

## 🖥 Desktop Examples

* [Accessibility Test](./DesktopExamples/src/test/java/AccessibilityTest.java)
    ```
  $ mvn test -pl DesktopExamples -Dtest=AccessibilityTest
    ```
* [Basic Login Test](./DesktopExamples/src/test/java/BasicLoginTest.java) 
    ```
  $ mvn test -pl DesktopExamples -Dtest=BasicLoginTest
    ```
* [Flaky Test](./DesktopExamples/src/test/java/FlakyTest.java) 
    ```
  $ mvn test -pl DesktopExamples -Dtest=FlakyTest
    ```
* [Parallel Cross Browser Tests](./DesktopExamples/src/test/java/ParallelCrossBrowserTest.java) 
    ```
  $ mvn test -pl DesktopExamples -Dtest=ParallelCrossBrowserTest
    ```
* [Performance Test](./DesktopExamples/src/test/java/PerformanceTest.java)
    ```
  $ mvn test -pl DesktopExamples -Dtest=PerformanceTest
    ```

## 📱 Mobile automation

### Emulators/Simulators

* [Emulator Native App Test](./MobileExamples/src/test/java/EmuSims/EmuNativeAppTest.java)
    ```
    $ mvn test -pl MobileExamples -Dtest=EmuNativeAppTest
    ```
* [Emulator Web App Test](./MobileExamples/src/test/java/EmuSims/EmuWebAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=EmuWebAppTest
  ```
* [Simulator Native App Test](./MobileExamples/src/test/java/EmuSims/SimNativeAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=SimNativeAppTest
  ```
* [Simulator Web App Test](./MobileExamples/src/test/java/EmuSims/SimWebAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=SimWebAppTest
  ```
  


### Real Devices
* [Android Native App Test](./MobileExamples/src/test/java/RealDevices/AndroidNativeAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=AndroidNativeAppTest
  ```
* [Android Web App Test](./MobileExamples/src/test/java/RealDevices/AndroidWebAppTest.java) 
  ```
  $ mvn test -pl MobileExamples -Dtest=AndroidWebAppTest
  ```
* [App Crash Test](./MobileExamples/src/test/java/RealDevices/AppCrashTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=AppCrashTest
  ```
* [iOS Native App Test](./MobileExamples/src/test/java/RealDevices/iOSNativeAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=iOSNativeAppTest
  ```
* [iOS Web App Test](./MobileExamples/src/test/java/RealDevices/iOSWebAppTest.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=iOSWebAppTest
  ```
* [Parallel Real Device Tests](./MobileExamples/src/test/java/RealDevices/ParallelRealDeviceTests.java)
  ```
  $ mvn test -pl MobileExamples -Dtest=ParallelRealDeviceTests
  ```



## 👁 Visual Example

* [Visual Desktop Test](./DesktopExamples/src/test/java/VisualDesktopTest.java)

  - Create Baseline Test
  ```
  $ mvn test -pl DesktopExamples -Dtest=VisualDesktopTest
   ```
  - Cause UI Changes
  ```
  $ mvn test -pl DesktopExamples -Dtest=VisualDesktopTest -DmodifyPage=true
  ```

## ⚙️ Setup 

*  Install [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
*  Install [IntelliJ (or another IDE)](https://www.jetbrains.com/idea/download/)
*  Install [JDK](https://www.oracle.com/java/technologies/downloads/)
*  Install [Maven](https://maven.apache.org/download.cgi)

### Import the Project

1. Create a directory on your machine.

2. Clone this repository into said directory.
    ```
    $ git clone https://github.com/prasauce/Implementation-Demos.git
    ```

3. Import the project into your IntelliJ (or IDE of your choice) as a **Maven Project**.

4. Click through the prompts, and confirm when it asks to **Import from Sources**.

5. Choose the **Implementation-Demos** directory as the **root** directory of the project.

### Set Your Sauce Labs Credentials
1. Copy your Sauce Labs **username** and **accessKey** in the [User Settings](https://app.saucelabs.com/user-settings) section of the [Sauce Labs Dashboard](https://app.saucelabs.com/dashboard/builds).
2. Open a Terminal window (command prompt for Windows) and set your Sauce Labs Environment variables:   
   **Mac OSX:**
   ```
   $ export SAUCE_USERNAME="your username"
   $ export SAUCE_ACCESS_KEY="your accessKey"
   ```
   **Windows:**
   ```
   > set SAUCE_USERNAME="username"
   > set SAUCE_ACCESS_KEY="accessKey"
   ```
   > To set an environment variables permanently in Windows, you must append it to the `PATH` variable.

   > Go to **Control Panel > System > Windows version > Advanced System Settings > Environment Variables > System Variables > Edit > New**

   > Then set the "Name" and "Value" for each variable

3. Test the environment variables

   **Mac OSX:**
    ```
    $ echo $SAUCE_USERNAME
    $ echo $SAUCE_ACCESS_KEY
    ```

   ***WARNING FOR UNIX USERS!***

   *If you have problems setting your environment variables, run the following commands in your terminal:*

    ```
    $ launchctl setenv SAUCE_USERNAME $SAUCE_USERNAME
    $ launchctl setenv SAUCE_ACCESS_KEY $SAUCE_ACCESS_KEY
    ```

   **Windows:**
    ```
    > echo %SAUCE_USERNAME%
    > echo %SAUCE_ACCESS_KEY%
    ```

### Run a Maven Test

1. Run the following command to update any package dependencies:
    ```
    $ mvn dependency:resolve
    ```
2. Then run the following command to compile your test code:
    ```
    $ mvn test-compile
    ```
3. Finally, run any of the tests to see if you've properly configured the test environment.
Example: 
    ```
    $ mvn test -pl DesktopExamples -Dtest=BasicLoginTest
   ```