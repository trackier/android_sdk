# Detailed Test Case Analysis - TrackierSDK

## Overview
This document provides a comprehensive analysis of how each test case works in the TrackierSDK, including detailed explanations of testing approaches, code examples, and strategies used for each test file.

---

## 1. TrackierSDKUnitTest.kt - Main SDK API Testing

### Purpose
Tests the main `TrackierSDK` object which serves as the primary entry point for all SDK functionality.

### Testing Strategy
- **State Management**: Uses reflection to reset SDK state between tests
- **Initialization Testing**: Tests single and repeated initialization scenarios
- **Method Coverage**: Tests all public `@JvmStatic` methods
- **Async Testing**: Uses `runBlocking` for suspend functions

### Key Test Cases

#### 1.1 SDK Initialization Tests
```kotlin
@Test
fun `test SDK initialization with valid config`() {
    val testConfig = TrackierSDKConfig(context, "test_token", "development")
    TrackierSDK.initialize(testConfig)
    assertTrue(TrackierSDK.isEnabled())
    assertEquals("test_token", TrackierSDK.getAppToken())
}
```
**How it works:**
- Creates a test configuration with mock context
- Initializes the SDK with the config
- Verifies SDK is enabled and app token is stored correctly

#### 1.2 State Reset Mechanism
```kotlin
private fun resetSDKState() {
    try {
        val sdkClass = TrackierSDK::class.java
        val isInitializedField = sdkClass.getDeclaredField("isInitialized")
        isInitializedField.isAccessible = true
        isInitializedField.set(null, false)
        // ... reset other fields
    } catch (e: Exception) {
        println("Warning: Could not reset SDK state via reflection: ${e.message}")
    }
}
```
**How it works:**
- Uses Java reflection to access private fields
- Resets `isInitialized`, `appToken`, and `instance` fields
- Ensures each test starts with a clean SDK state

#### 1.3 Event Tracking Tests
```kotlin
@Test
fun `test trackEvent when SDK enabled and initialized`() {
    val testConfig = TrackierSDKConfig(context, "test_token", "development")
    TrackierSDK.initialize(testConfig)
    TrackierSDK.setEnabled(true)
    val event = TrackierEvent("test_event")
    TrackierSDK.trackEvent(event)
    assertTrue(true) // Should not throw exception
}
```
**How it works:**
- Tests event tracking in enabled state
- Verifies no exceptions are thrown
- Uses `assertTrue(true)` as a placeholder since actual tracking is async

#### 1.4 Suspend Function Testing
```kotlin
@Test
fun `test trackSession suspend function`() {
    val testConfig = TrackierSDKConfig(context, "test_token", "development")
    TrackierSDK.initialize(testConfig)
    
    runBlocking {
        TrackierSDK.trackSession()
        assertTrue(true)
    }
}
```
**How it works:**
- Uses `runBlocking` coroutine builder to test suspend functions
- Executes the suspend function in a blocking context
- Verifies no exceptions are thrown

---

## 2. TrackierEventUnitTest.kt - Event Model Testing

### Purpose
Tests the `TrackierEvent` data class which represents SDK events.

### Testing Strategy
- **Constructor Testing**: Tests all constructor overloads
- **Property Testing**: Verifies all properties are set correctly
- **Data Validation**: Tests with various parameter combinations

### Key Test Cases

#### 2.1 Constructor Testing
```kotlin
@Test
fun `test TrackierEvent with name only`() {
    val event = TrackierEvent("test_event")
    assertEquals("test_event", event.eventName)
    assertNull(event.eventValue)
    assertNull(event.eventCurrency)
    assertNull(event.eventOrderId)
    assertTrue(event.eventParams.isEmpty())
}
```
**How it works:**
- Tests the basic constructor with only event name
- Verifies default values for optional parameters
- Checks that eventParams is initialized as empty map

#### 2.2 Revenue Event Testing
```kotlin
@Test
fun `test TrackierEvent with revenue`() {
    val event = TrackierEvent("purchase", 99.99)
    assertEquals("purchase", event.eventName)
    assertEquals(99.99, event.eventValue)
    assertEquals("USD", event.eventCurrency) // Default currency
    assertNull(event.eventOrderId)
}
```
**How it works:**
- Tests revenue tracking with value and default currency
- Verifies revenue value is stored correctly
- Checks default USD currency is applied

---

## 3. TrackierSDKConfigUnitTest.kt - Configuration Testing

### Purpose
Tests the `TrackierSDKConfig` class which handles SDK configuration.

### Testing Strategy
- **Required Parameters**: Tests constructor with minimal required params
- **Optional Parameters**: Tests with all optional parameters
- **Property Access**: Tests getter methods for all properties

### Key Test Cases

#### 3.1 Required Configuration Testing
```kotlin
@Test
fun `test config creation with required parameters`() {
    val config = TrackierSDKConfig(context, "test_token", "development")
    assertEquals("test_token", config.appToken)
    assertEquals("development", config.environment)
    assertEquals("", config.region) // Default empty region
}
```
**How it works:**
- Tests constructor with required context, appToken, and environment
- Verifies required properties are set correctly
- Checks default values for optional properties

#### 3.2 Region Configuration Testing
```kotlin
@Test
fun `test config with region setting`() {
    val config = TrackierSDKConfig(context, "test_token", "development")
    config.setRegion("in")
    assertEquals("in", config.getRegion())
}
```
**How it works:**
- Tests region setting and retrieval
- Verifies region is stored and returned correctly

---

## 4. APIRepositoryUnitTest.kt - API Communication Testing

### Purpose
Tests the `APIRepository` class which handles all network communication.

### Testing Strategy
- **Method Testing**: Tests all public API methods
- **Error Handling**: Tests various error scenarios
- **Data Validation**: Tests request/response data structure

### Key Test Cases

#### 4.1 Install Data Testing
```kotlin
@Test
fun testSendInstallData() {
    val testData = mutableMapOf<String, Any>("test" to "value")
    val response = APIRepository.sendInstallData(testData)
    assertNotNull(response)
    assertTrue(response is ResponseData)
}
```
**How it works:**
- Creates test data map
- Calls the API method
- Verifies response is not null and correct type

#### 4.2 URL Building Logic Testing
```kotlin
@Test
fun testURLBuildingLogic() {
    val expectedBaseUrl = "${Constants.SCHEME}in-${Constants.BASE_URL}"
    val expectedDeeplinksUrl = "${Constants.SCHEME}in-${Constants.BASE_URL_DL}"
    
    assertEquals("https://in-events.trackier.io/v1/", expectedBaseUrl)
    assertEquals("https://in-sdkr.apptracking.io/dl/", expectedDeeplinksUrl)
}
```
**How it works:**
- Tests URL construction logic
- Verifies correct URL format for different regions
- Tests both base URL and deeplinks URL patterns

---

## 5. DeviceInfoUnitTest.kt - Device Information Testing

### Purpose
Tests the `DeviceInfo` class which collects device-specific information.

### Testing Strategy
- **Initialization Testing**: Tests device info initialization
- **Property Testing**: Tests all device properties
- **Platform Testing**: Tests Android-specific information

### Key Test Cases

#### 5.1 Device Info Initialization
```kotlin
@Test
fun testDeviceInfoInitialization() {
    val deviceInfo = DeviceInfo()
    DeviceInfo.init(deviceInfo, context)
    
    assertNotNull(deviceInfo.osName)
    assertNotNull(deviceInfo.osVersion)
    assertNotNull(deviceInfo.deviceModel)
    assertNotNull(deviceInfo.appVersion)
}
```
**How it works:**
- Creates DeviceInfo instance
- Initializes it with context
- Verifies all required properties are set

#### 5.2 OS Information Testing
```kotlin
@Test
fun testOSInformation() {
    val deviceInfo = DeviceInfo()
    DeviceInfo.init(deviceInfo, context)
    
    assertEquals("Android", deviceInfo.osName)
    assertTrue(deviceInfo.osVersion.isNotEmpty())
}
```
**How it works:**
- Tests OS name and version detection
- Verifies Android OS is correctly identified
- Checks OS version is not empty

---

## 6. UtilUnitTest.kt - Utility Function Testing

### Purpose
Tests the `Util` class which provides helper functions.

### Testing Strategy
- **Function Testing**: Tests each utility function individually
- **Edge Cases**: Tests with null, empty, and invalid inputs
- **Format Testing**: Tests date formatting and string utilities

### Key Test Cases

#### 6.1 Shared Preferences Testing
```kotlin
@Test
fun testSharedPreferencesOperations() {
    val testKey = "test_key"
    val testValue = "test_value"
    
    Util.setSharedPrefString(context, testKey, testValue)
    val retrievedValue = Util.getSharedPrefString(context, testKey)
    
    assertEquals(testValue, retrievedValue)
}
```
**How it works:**
- Tests shared preferences setter and getter
- Verifies data persistence and retrieval
- Tests the complete read-write cycle

#### 6.2 Date Formatting Testing
```kotlin
@Test
fun testDateFormatterPattern() {
    val pattern = Util.dateFormatter.toPattern()
    assertEquals(Constants.DATE_TIME_FORMAT, pattern)
}
```
**How it works:**
- Tests date formatter configuration
- Verifies correct date format pattern
- Ensures consistency with constants

---

## 7. FactoryUnitTest.kt - Factory Pattern Testing

### Purpose
Tests the `Factory` class which creates and manages logger instances.

### Testing Strategy
- **Singleton Testing**: Tests factory pattern implementation
- **Logger Testing**: Tests logger creation and configuration
- **State Testing**: Tests logger state management

### Key Test Cases

#### 7.1 Logger Creation Testing
```kotlin
@Test
fun testLoggerCreation() {
    val logger = Factory.logger
    assertNotNull(logger)
    assertTrue(logger is java.util.logging.Logger)
}
```
**How it works:**
- Tests logger creation through factory
- Verifies logger is not null
- Checks correct logger type

#### 7.2 Log Level Testing
```kotlin
@Test
fun testSetLogLevel() {
    val originalLevel = Factory.logger.level
    Factory.setLogLevel(Level.FINEST)
    assertEquals(Level.FINEST, Factory.logger.level)
    
    Factory.setLogLevel(originalLevel ?: Level.INFO)
}
```
**How it works:**
- Tests log level setting functionality
- Verifies level changes are applied
- Restores original level after test

---

## 8. ConstantsUnitTest.kt - Constants Testing

### Purpose
Tests the `Constants` object which defines SDK constants.

### Testing Strategy
- **Value Testing**: Tests all constant values
- **Format Testing**: Tests URL and format constants
- **Version Testing**: Tests SDK version constants

### Key Test Cases

#### 8.1 SDK Version Testing
```kotlin
@Test
fun testSDKVersion() {
    assertNotNull(Constants.SDK_VERSION)
    assertTrue(Constants.SDK_VERSION.isNotEmpty())
    assertTrue(Constants.SDK_VERSION.matches(Regex("\\d+\\.\\d+\\.\\d+")))
}
```
**How it works:**
- Tests SDK version is not null
- Verifies version is not empty
- Validates version format (x.y.z)

#### 8.2 URL Constants Testing
```kotlin
@Test
fun testURLConstants() {
    assertTrue(Constants.SCHEME.startsWith("https://"))
    assertTrue(Constants.BASE_URL.contains("trackier.io"))
    assertTrue(Constants.BASE_URL_DL.contains("apptracking.io"))
}
```
**How it works:**
- Tests URL scheme is HTTPS
- Verifies base URLs contain expected domains
- Ensures correct URL structure

---

## 9. ResponseDataUnitTest.kt - API Response Testing

### Purpose
Tests the `ResponseData` class which handles API responses.

### Testing Strategy
- **Success Testing**: Tests successful response handling
- **Error Testing**: Tests error response handling
- **Data Testing**: Tests response data extraction

### Key Test Cases

#### 9.1 Success Response Testing
```kotlin
@Test
fun testSuccessResponse() {
    val responseData = ResponseData(true, "Success message", null)
    
    assertTrue(responseData.success)
    assertEquals("Success message", responseData.message)
    assertNull(responseData.error)
}
```
**How it works:**
- Tests successful response creation
- Verifies success flag is true
- Checks message is set correctly
- Ensures error is null

#### 9.2 Error Response Testing
```kotlin
@Test
fun testErrorResponse() {
    val error = ResponseData.Error(400, "BAD_REQUEST", "Invalid request")
    val responseData = ResponseData(false, "Error occurred", error)
    
    assertFalse(responseData.success)
    assertEquals("Error occurred", responseData.message)
    assertNotNull(responseData.error)
    assertEquals(400, responseData.error?.statusCode)
}
```
**How it works:**
- Tests error response creation
- Verifies success flag is false
- Checks error object is properly set
- Validates error details

---

## 10. DynamicLinkUnitTest.kt - Dynamic Link Testing

### Purpose
Tests the `DynamicLink` class which handles dynamic link creation.

### Testing Strategy
- **Builder Testing**: Tests builder pattern implementation
- **Parameter Testing**: Tests all builder parameters
- **Validation Testing**: Tests link validation

### Key Test Cases

#### 10.1 Builder Pattern Testing
```kotlin
@Test
fun testDynamicLinkBuilder() {
    val dynamicLink = DynamicLink.Builder()
        .setTemplateId("test_template")
        .setLink(android.net.Uri.parse("https://example.com"))
        .build()
    
    assertNotNull(dynamicLink)
    assertEquals("test_template", dynamicLink.templateId)
    assertEquals("https://example.com", dynamicLink.link.toString())
}
```
**How it works:**
- Tests builder pattern for dynamic link creation
- Verifies all parameters are set correctly
- Checks final object is properly constructed

---

## 11. ReferrerDetailsUnitTest.kt - Referrer Testing

### Purpose
Tests the `ReferrerDetails` class which handles referrer information.

### Testing Strategy
- **Property Testing**: Tests all referrer properties
- **Organic Testing**: Tests organic referrer detection
- **Deep Link Testing**: Tests deep link detection

### Key Test Cases

#### 11.1 Referrer Details Creation
```kotlin
@Test
fun testReferrerDetailsCreation() {
    val referrerDetails = ReferrerDetails()
    
    assertNotNull(referrerDetails)
    assertFalse(referrerDetails.isOrganic)
    assertNull(referrerDetails.clickId)
    assertNull(referrerDetails.referrerClickTimestamp)
}
```
**How it works:**
- Tests referrer details object creation
- Verifies default values are set correctly
- Checks all properties are accessible

---

## 12. BackgroundWorkerUnitTest.kt - Background Processing Testing

### Purpose
Tests the `BackgroundWorker` class which handles background tasks.

### Testing Strategy
- **Worker Testing**: Tests worker initialization and execution
- **Error Testing**: Tests error handling scenarios
- **Context Testing**: Tests context handling

### Key Test Cases

#### 12.1 Worker Execution Testing
```kotlin
@Test
fun testBackgroundWorkerExecution() {
    val worker = BackgroundWorker(context, workParams)
    val result = worker.doWork()
    
    assertNotNull(result)
    assertTrue(result is androidx.work.ListenableWorker.Result)
}
```
**How it works:**
- Tests worker creation and execution
- Verifies worker returns a result
- Checks result type is correct

---

## 13. TrackierWorkRequestUnitTest.kt - Work Request Testing

### Purpose
Tests the `TrackierWorkRequest` class which manages background work requests.

### Testing Strategy
- **Request Testing**: Tests work request creation
- **Configuration Testing**: Tests request configuration
- **Scheduling Testing**: Tests work scheduling

### Key Test Cases

#### 13.1 Work Request Creation
```kotlin
@Test
fun testWorkRequestCreation() {
    val workRequest = TrackierWorkRequest.createWorkRequest(context, "test_data")
    
    assertNotNull(workRequest)
    assertTrue(workRequest is androidx.work.OneTimeWorkRequest)
}
```
**How it works:**
- Tests work request creation with data
- Verifies request is properly constructed
- Checks correct work request type

---

## 14. Interface Testing - APIService & DeepLinkListener

### Purpose
Tests interfaces that define contracts for SDK functionality.

### Testing Strategy
- **Structure Testing**: Tests interface structure and methods
- **Implementation Testing**: Tests interface implementations
- **Callback Testing**: Tests callback mechanisms

### Key Test Cases

#### 14.1 APIService Interface Testing
```kotlin
@Test
fun `test APIService interface structure`() {
    assertTrue(APIService::class.java.isInterface)
    
    val methods = APIService::class.java.methods
    val methodNames = methods.map { it.name }
    
    assertTrue(methodNames.contains("sendInstallData"))
    assertTrue(methodNames.contains("sendEventData"))
    // ... other methods
}
```
**How it works:**
- Tests interface is properly defined
- Verifies all required methods exist
- Checks method signatures are correct

#### 14.2 DeepLinkListener Callback Testing
```kotlin
@Test
fun `test DeepLinkListener callback functionality`() {
    var callbackCalled = false
    var receivedDeepLink: DeepLink? = null
    
    val listener = object : DeepLinkListener {
        override fun onDeepLinking(result: DeepLink) {
            callbackCalled = true
            receivedDeepLink = result
        }
    }
    
    val testDeepLink = DeepLink("https://example.com/deeplink", false)
    listener.onDeepLinking(testDeepLink)
    
    assertTrue(callbackCalled)
    assertNotNull(receivedDeepLink)
    assertEquals(testDeepLink, receivedDeepLink)
}
```
**How it works:**
- Creates anonymous implementation of interface
- Tests callback mechanism
- Verifies callback is called with correct data
- Checks data is properly passed through

---

## 15. Exception Testing - InstallReferrerException

### Purpose
Tests custom exception classes for proper exception handling.

### Testing Strategy
- **Creation Testing**: Tests exception creation
- **Inheritance Testing**: Tests exception inheritance
- **Throwing Testing**: Tests exception throwing and catching

### Key Test Cases

#### 15.1 Exception Creation Testing
```kotlin
@Test
fun `test InstallReferrerException with message`() {
    val message = "Test exception message"
    val exception = InstallReferrerException(message)
    
    assertEquals(message, exception.message)
    assertTrue(exception is Exception)
}
```
**How it works:**
- Tests exception creation with message
- Verifies message is stored correctly
- Checks proper inheritance from Exception

#### 15.2 Exception Throwing Testing
```kotlin
@Test
fun `test InstallReferrerException can be thrown and caught`() {
    val message = "Custom error message"
    
    try {
        throw InstallReferrerException(message)
        fail("Exception should have been thrown")
    } catch (e: InstallReferrerException) {
        assertEquals(message, e.message)
        assertTrue(e is Exception)
    }
}
```
**How it works:**
- Tests exception can be thrown
- Verifies exception can be caught
- Checks exception type and message

---

## 16. Test Execution Flow

### How Tests Are Executed
1. **Setup Phase**: `@Before` methods run to set up test environment
2. **Test Execution**: Individual test methods run with specific scenarios
3. **Assertion Phase**: Results are verified using assertions
4. **Cleanup Phase**: State is reset for next test

### Test Independence
- Each test runs in isolation
- SDK state is reset between tests
- No test depends on another test's outcome
- Tests can run in any order

### Error Handling
- Tests catch and verify exceptions
- Edge cases are tested (null inputs, empty values, etc.)
- Both success and failure scenarios are covered

---

## 17. Best Practices Implemented

### Test Organization
- One test file per main class
- Descriptive test method names using backticks
- Clear test structure with setup, execution, and verification

### Assertion Strategy
- Use specific assertions (`assertEquals`, `assertNotNull`, etc.)
- Test both positive and negative cases
- Verify return types and values
- Check for exceptions where appropriate

### Mocking Strategy
- Mock external dependencies when needed
- Use real objects for internal SDK components
- Test interfaces through implementations

### Coverage Strategy
- Test all public methods and properties
- Test edge cases and error conditions
- Test async/suspend functions
- Test interface contracts

---

## 18. Test Maintenance

### Adding New Tests
1. Create test file following naming convention: `{ClassName}UnitTest.kt`
2. Use descriptive test method names
3. Include both positive and negative test cases
4. Mock external dependencies appropriately
5. Reset state between tests

### Updating Tests
- Update tests when SDK API changes
- Maintain test independence
- Keep tests focused and concise
- Ensure all new features are tested

### Test Quality Metrics
- All public methods covered
- Edge cases tested
- Error conditions handled
- Async functions tested
- Interface contracts verified

---

This comprehensive testing approach ensures the TrackierSDK is reliable, maintainable, and thoroughly tested across all functionality. 