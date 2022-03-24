# android sdk

## Table of Content

### Integration

* [Add Trackier SDK to your app](#qs-add-trackier-sdk)
    * [Add the SDK using gradle](#qs-add-sdk-gradle)
    * [Add the latest version of Trackier SDK as a dependency](#qs-add-latest-version-sdk)
    * [Adding Android install referrer to your app](#qs-add-install-referre)
    * [Add required permissions](#qs-add-request-permissions)
    * [Getting Google Advertising ID](#qs-getting-gaid)
* [Implement and initialize the SDK](#qs-implement-trackier-sdk)
    * [Retrieve your dev key](#qs-retrieve-dev-key)
    * [Initialize the SDK](#qs-initialize-trackier-sdk)
    * [Associate User Info during initialization of sdk](#qs-assosoiate-user-info)
* [Defer Start Sdk](#qs-defer-start-sdk)
* [Disable Organic Tracking](#qs-disable-orgainic-tracking)
* [Track Events](#qs-track-event)
    * [Retrieve Event Id from dashboard](#qs-retrieve-event-id)
    * [Track Simple Event](#qs-track-simple-event)
    * [Track with Currency & Revenue Event](#qs-track-event-with-currencey)
    * [Add custom params with event](#qs-add-custom-parms-event)
* [Track Uninstall](#qs-track-uninstall)
* [Proguard Settings](#qs-progaurd-trackier-sdk)

## <a id="qs-add-trackier-sdk"></a>Add Trackier SDK to your app

### <a id="qs-add-sdk-gradle"></a>Add the SDK using Gradle
<p>Add the code below to Module level app build.gradle before dependencies</p>

```gradle
  repositories {
    mavenCentral()
  }
```

## <a id="qs-add-latest-version-sdk"></a>Add the latest version of Trackier SDK as a dependency

You can find the latest version [here](https://search.maven.org/artifact/com.trackier/android-sdk)

```gradle 
  implementation 'com.trackier:android-sdk:1.6.18'
```

## <a id="qs-add-install-referre"></a>Adding Android install referrer to your app

Add the Android Install Referrer as a dependency. You can find the latest version [here](https://search.maven.org/artifact/com.trackier/android-sdk)

```gradle 
  dependencies {
    // make sure to use the latest SDK version:
    // https://mvnrepository.com/artifact/com.trackier/android-sdk   
  
    implementation 'com.trackier:android-sdk:1.6.18'
    implementation 'com.android.installreferrer:installreferrer:2.2'
  }
```

Sync the project to retrieve the dependencies – see the following screenshot
<br></br>
<img width="1740" alt="Screenshot 2021-07-29 at 6 28 33 PM" src="https://user-images.githubusercontent.com/34488320/127852271-76cc284b-a257-44a2-ac3a-ad7d3cbe384f.png">
<br></br>

## <a id="qs-add-request-permissions"></a>Add required permissions

```java 
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

  <!-- Optional : -->
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

![pasted image 0](https://user-images.githubusercontent.com/34488320/127852970-62b516d8-9aa1-498a-809c-abb2c4650d2c.png)


## <a id="qs-getting-gaid"></a> Getting Google Advertising ID

- Add the google advertising id dependency in your app **build.gradle**

```gradle
dependencies {
  // This can be added where the SDK dependency has been added
  implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
}
```
- Update your Android Manifest file by adding the following permission. This is required if your app is targeting devices with android version 12+

```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

- Add meta data inside the application tag (If not already added)
```xml
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
```

## <a id="qs-implement-trackier-sdk"></a>Implement and initialize the SDK

### <a id="qs-retrieve-dev-key"></a>Retrieve your dev key

<img width="1210" alt="Screenshot 2021-08-03 at 3 13 00 PM" src="https://user-images.githubusercontent.com/34488320/127995061-b9cd8899-7236-441c-b594-3ce83aa54c0b.png">

### <a id="qs-initialize-trackier-sdk"></a>Initialize the SDK

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
* The second argument is environment which can be either "development", "production" or "testing".
* After that pass the sdkConfig reference to TrackierSDK.initialize method.

<img width="1745" alt="Screenshot 2021-07-29 at 6 30 20 PM" src="https://user-images.githubusercontent.com/34488320/127853995-b6daf9b4-2785-4b0e-890a-55c6492d9182.png">

### Note
 Don’t forgot to register the global application class inside Manifest. You can register your global class in Manifest like this, suppose the name of global class is Trackier Application:
 
```java 
  android:name = "APP_PACKAGE_NAME.TrackierApplication"
```
## <a id="qs-assosoiate-user-info"></a>Assosiate User Info during initialization of sdk

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

## <a id="qs-defer-start-sdk"></a>Defer SDK start
### To defer sdk start
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

Note:- Make sure to take EXTERNAL_STORAGE_READ permission before enabling this feature. For reference you can see [example directory](https://github.com/trackier/android_sdk/tree/master/TrackierSDK/example-app-kotlin)

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
## <a id="qs-disable-orgainic-tracking"></a>Disable Organic Track 
#### To disable organic track pass true boolean to disableOrganicTracking method of sdk config 

---
  #### Java
___
```java 
   TrackierSDKConfig sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production");
   sdkConfig.disableOrganicTracking(true);
   TrackierSDK.initialize(sdkConfig);
```

---
  #### Kotlin
___
```kotlin 
   val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
   sdkConfig.disableOrganicTracking(true)
   TrackierSDK.initialize(sdkConfig)
```

## <a id="qs-track-event"></a>Track Events

<a id="qs-retrieve-event-id"></a>Retrieve Event Id from dashboard
<br></br>
<img width="1725" alt="Screenshot 2021-08-03 at 3 25 55 PM" src="https://user-images.githubusercontent.com/34488320/127996772-5e760e26-addc-499b-a7c6-de3ef2d0288c.png">


### <a id="qs-track-simple-event"></a>Track Simple Event

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
* You can associate inbuilt params with the event , in-built param list are below:-  orderId, revenue, currency, param1, param2, param3 ,param4, param5, param6, param7, param8, param9, param10

### <a id="qs-track-event-with-currencey"></a>Track with Currency & Revenue Event
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

### <a id="qs-add-custom-parms-event"></a>Add custom params with event


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


## <a id="qs-track-uninstall"></a>Track Uninstall 

--- 
  #### Java 
---
```java 
  private FirebaseAnalytics mFirebaseAnalytics; 
  FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this); 
  mFirebaseAnalytics.setUserProperty("ct_objectId", Objects.requireNonNull(TrackierSDK.getTrackierId())); 
``` 
 
* Add the above code to your app to set up a common identifier. 
* Set the `app_remove` event as a conversion event in Firebase. 
* Use the Firebase cloud function to send uninstall information to Trackier MMP. 
* You can find the support article [here](https://help.trackier.com/support/solutions/articles/31000162841-android-uninstall-tracking). 


## <a id="qs-progaurd-trackier-sdk"></a>Proguard Settings 

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


