# android sdk

## Table of Content

### [Integration]()

* [Add Trackier SDK to your app]()
    * [Add the SDK to your project]()
    * [Add the latest version of Trackier SDK as a dependency]()
    * [Add required permissions]()
    * [Adding Android install referrer to your app]()
    * [Sync the project to retrieve the dependencies]()
* [Implement and initialize the SDK]()
    * [Retrieve your dev key]()
    * [Initialize the SDK]()
    * [Attribution Params]()
    * [Associate User Info during initialization of sdk]()
* [Defer Start Sdk]()
* [Track Events]()
    * [Retrieve Event Id from dashboard]()
    * [Track Event]()
    * [Track with Currency & Revenue Event]()
    * [Add custom params with event]()
* [Proguard Settings]()

## Add Trackier SDK to your app

### Add the SDK using Gradle
<p>Add the code below to Module level app build.gradle before dependencies</p>

```gradle
  repositories {
    mavenCentral()
  }
```

## Add the latest version of Trackier SDK as a dependency

You can find the latest version [here]()

```gradle 
  implementation 'com.trackier:android-sdk:1.6.6'
```

## Adding Android install referrer to your app

Add the Android Install Referrer as a dependency. You can find the latest version [here]()

```gradle 
  dependencies {
    // make sure to use the latest SDK version:
    // https://mvnrepository.com/artifact/com.trackier/android-sdk   
  
    implementation 'com.trackier:android-sdk:1.6.6'
    implementation 'com.android.installreferrer:installreferrer:2.2'
  }
```

Sync the project to retrieve the dependencies – see the following screenshot
<br></br>
<img width="1740" alt="Screenshot 2021-07-29 at 6 28 33 PM" src="https://user-images.githubusercontent.com/34488320/127852271-76cc284b-a257-44a2-ac3a-ad7d3cbe384f.png">
<br></br>

## Add required permissions

```java 
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

  <!-- Optional : -->
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

![pasted image 0](https://user-images.githubusercontent.com/34488320/127852970-62b516d8-9aa1-498a-809c-abb2c4650d2c.png)

## Implement and initialize the SDK

### Retrieve your dev key

### Initialize the SDK

<p>We recommend initializing the SDK inside the app’s global application class. This allows the SDK to
initialize in all scenarios, including deep linking.
The steps listed below take place inside the app’s global application class.
</p>

Inside the app’s global class, import the following libraries

---
  #### Java
___
```java 
  import com.trackier.sdk.TrackierEvent;
  import com.trackier.sdk.TrackierSDK;
  import com.trackier.sdk.TrackierSDKConfig;
```

---
  #### Kotlin
___
```kotlin 
  import com.trackier.sdk.TrackierEvent
  import com.trackier.sdk.TrackierSDK
  import com.trackier.sdk.TrackierSDKConfig
```

Inside the global class, assign your dev key to a variable, preferably name TR_DEV_KEY.
<p>
Important: it is crucial to use the correct dev key when initializing the SDK. Using the wrong dev key or an
incorrect dev key impact all traffic sent from the SDK and cause attribution and reporting issues.
</p>

---
  #### Java
___
```java 
  private static final String TR_DEV_KEY  = "xxxx-xx-4505-bc8b-xx";
```

---
  #### Kotlin
___
```kotlin 
  const val TR_DEV_KEY = "xxxx-xx-4505-bc8b-xx"
```

Inside the global class, after call to super.create(), initialize SDK

---
  #### Java
___
```java 
  TrackierSDKConfig  sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY, "production");
  TrackierSDK.initialize(sdkConfig);

```

---
  #### Kotlin
___
```kotlin 
  val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
  TrackierSDK.initialize(sdkConfig)
```
* The first argument is your Trackier Sdk api key.
* The second argument is environment which can be either        “development” and “production” 
* After that pass the sdkConfig reference to TrackierSDK.initialize method.

<img width="1745" alt="Screenshot 2021-07-29 at 6 30 20 PM" src="https://user-images.githubusercontent.com/34488320/127853995-b6daf9b4-2785-4b0e-890a-55c6492d9182.png">

### Note
 Don’t forgot to register the global application class inside Manifest. You can register your global class in Manifest like this, suppose the name of global class is Trackier Application:
 
```java 
  android:name = "APP_PACKAGE_NAME.TrackierApplication"
```
## Assosiate User Info during initialization of sdk

To assosiate Customer Id , Customer Email and Customer additional params during initializing sdk 

---
  #### Java
___
```java 
  TrackierSDK.setUserId("XXXXXXXX");
  TrackierSDK.setUserEmail("abc@gmail.com");
  TrackierSDK.initialize(sdkConfig);
```

---
  #### Kotlin
___
```kotlin 
  TrackierSDK.setUserId("XXXXXXXX")
  TrackierSDK.setUserEmail("abc@gmail.com")
  TrackierSDK.initialize(sdkConfig)
```

### Note 
For additional user details , make a mutable map and pass it in setUserAdditionalDetails function.


<!-- --- -->
  <!-- #### Java
___
```java 
val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
userAdditionalDetails.put("userMobile",99XXXXXXXX)
TrackierSDK.setUserAdditionalDetails(userAdditionalDetails)	
``` -->
---
  #### Kotlin
___
```kotlin 
  val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
  userAdditionalDetails.put("userMobile",99XXXXXXXX)
  TrackierSDK.setUserAdditionalDetails(userAdditionalDetails)	
```

* The first method is to associate  user Id in which .
* The second method  is to associate user email in which .


You can also associate additional details by making a mutable map where . and the pass reference in  TrackierSDK’s setUserAdditionalDetails method.

## Defer SDK start

To defer sdk start,
---
  #### Java
___
```java 
  sdkConfig.setManualMode(true);
  TrackierSDK.setLocalRefTrack(true,"_");
  TrackierSDK.initialize(sdkConfig);
```

---
  #### Kotlin
___
```kotlin 
  sdkConfig.setManualMode(true)
  TrackierSDK.setLocalRefTrack(true,"_")
  TrackierSDK.initialize(sdkConfig)
```

* Set manual mode to true by passing true boolean value to setManualMode.
* The second method  is to associate user email in which .

Note:- Make sure to take EXTERNAL_STORAGE_READ permission before enabling this feature. For reference you can see [example directory]()

Now you can explicitly fire Install whenever desired,

---
  #### Java
___
```java 
  TrackierSDK.fireInstall();
```

---
  #### Kotlin
___
```kotlin 
  TrackierSDK.fireInstall()
```

## Track Events

Retrieve Event Id from dashboard
<br></br>
[SCREENSHOT]()

### Track Event

---
  #### Java
___
```java 
  TrackierEvent event = new TrackierEvent(TrackierEvent.LEVEL_ACHIEVED);
  event.param1 = "Level 10";
  TrackierSDK.trackEvent(event);
```

---
  #### Kotlin
___
```kotlin 
  val event = TrackierEvent(TrackierEvent.LEVEL_ACHIEVED)
  event.param1 = "Level 10"
  TrackierSDK.trackEvent(event)
```

* Argument in Trackier event class is event Id which you want to call event for.
* You can associate inbuilt params with the event , in-built param list are below:-  orderId, currency, currency, param1, param2, param3 ,param4, param5, param6, param7, param8, param9, param10

### Track with Currency & Revenue Event
---
  #### Java
___
```java 
  TrackierEvent event = new TrackierEvent(TrackierEvent.PURCHASE);
  event.param1 = "Praram Name";
  event.revenue = 2.5;
  event.currency = "USD";
  TrackierSDK.trackEvent(event);

```

---
  #### Kotlin
___
```kotlin 
  val event = TrackierEvent(TrackierEvent.PURCHASE)
  event.param1 = "Praram Name"
  event.revenue = 2.5
  event.currency = "USD"
  TrackierSDK.trackEvent(event)
```

### Add custom params with event


---
  #### Kotlin
___
```kotlin 
  val eventCustomParams: MutableMap<String,Any> = mutableMapOf()
  eventCustomParams.put("customParam1",XXXXX)
  eventCustomParams.put("customParam2",XXXXX)
  event.ev = eventCustomParams  
  TrackierSDK.trackEvent(event)
```

* First create a mutable map
* Pass its reference to event.ev param of event
* Pass event reference to trackEvent method of TrackerSDK

## Proguard Settings 

If your app is using proguard then add these lines to the proguard config file 

``` 
  -keep class com.trackier.sdk.** { *; }
  -keep class com.google.android.gms.common.ConnectionResult {
      int SUCCESS;
  }
  -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
      com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
  }
  -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
      java.lang.String getId();
      boolean isLimitAdTrackingEnabled();
  }
  -keep public class com.android.installreferrer.** { *; }
```













