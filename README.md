# android_sdk
Trackier MMP Android SDK

Android SDK integration for developers
 
## Integration
 
### Add TrackierSDK to your app
 
#### Add the SDK to your project
 
  Add the SDK using Gradle:- Add the code below to Module-level/app/build.gradle before
  dependencies:
```
  repositories {
    mavenCentral()
  }
```

#### Add the latest version of Trackier SDK as a dependency.

You can find the latest version [here](https://mvnrepository.com/artifact/com.trackier/android-sdk).
```
  implementation 'com.trackier:android-sdk:1.6.5'
```
 
#### Sync the project to retrieve the dependencies – see the following screenshot:

![Screenshot 2021-03-08 at 3 33 09 PM](https://user-images.githubusercontent.com/34488320/110306675-1423ca80-8024-11eb-9117-07ed04c5072c.jpg)
 
### Add required permissions

#### Add the following permissions to Androidmanifest.xml:
  
```
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 
  <!-- Optional : -->
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```
 
### Adding Android install referrer to your app
 
#### Add the Android Install Referrer as a dependency. You can find the latest version here

```
dependencies {
  // make sure to use the latest SDK version:
  // https://mvnrepository.com/artifact/com.trackier/android-sdk   
 
  implementation 'com.trackier:android-sdk:1.6.5'
  implementation 'com.android.installreferrer:installreferrer:2.2'
}
```
 
#### Sync the project to retrieve the dependencies – see the following screenshot:
 
![Screenshot 2021-03-08 at 3 30 10 PM](https://user-images.githubusercontent.com/34488320/110306113-76300000-8023-11eb-9732-94598bae53ce.jpg)
 
 
## Implement and initialize the SDK
 
### Retrieve your dev key
 
### Initialize the SDK
  
   We recommend initializing the SDK inside the app’s global application class. This allows the SDK to  
   initialize in all scenarios, including deep linking.
 
  The steps listed below take place inside the app’s global application class.
 
#### Inside the app’s global class, import the following libraries
 
    import com.trackier.sdk.TrackierEvent
    import com.trackier.sdk.TrackierSDK
    import com.trackier.sdk.TrackierSDKConfig
 
#### Inside the global class, assign your dev key to a variable, preferably name TR_DEV_KEY.
 
   Important: it is crucial to use the correct dev key when initializing the SDK. Using the wrong dev key or an     
   incorrect dev key impact all traffic sent from the SDK and cause attribution and reporting issues.
 
 KOTLIN:-
 
      const val TR_DEV_KEY = "xxxx-xx-4505-bc8b-xx";
 
JAVA :-
 
      private static final String TR_DEV_KEY  = "xxxx-xx-4505-bc8b-xx";
 
#### Inside the global class, after call to super.create(), initialize SDK like this:-

KOTLIN
```kotlin
       val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
       TrackierSDK.initialize(sdkConfig)
```
 
JAVA:
```java
      TrackierSDKConfig  sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY, "production");
      TrackierSDK.initialize(sdkConfig);
```
 
Note:- Don’t forgot to register the global application class inside Manifest. You can register your global class in Manifest like this, suppose the name of global class is TrackierApplication:

```java 
      android:name = "APP_PACAGE_NAME.TrackierApplication"
```

### Attribution Params:-

You can set attribution params like  ParterId, SiteId, SubSiteId, ChannelId, Ad & AdId during the initialization of trackier sdk.

Kotlin:-

    val attributionParams = AttributionParams( parterId = "xxxx",siteId = "xxx",subSiteID = "xxx",channel = "xxxx",ad = "xxx",adId = "xxx")
    sdkConfig.setAttributionParams(apkAttributes) 
    TrackierSDK.initialize(sdkConfig);

 Java :- 

    AttributionParams attributionParams = new AttributionParams("parterId","siteId","subSiteID","channel","ad","adId");
    sdkConfig.setAttributionParams(apkAttributes)
    TrackierSDK.initialize(sdkConfig);
    
    
### Assosiate User Info during initialization of sdk

To assosiate Customer Id , Customer Email and Customer additional params during initializing sdk:-

    TrackierSDK.setUserId("XXXXXXXX")
    TrackierSDK.setUserEmail("abc@gmail.com")
    TrackierSDK.initialize(sdkConfig)

Note :- For additional user details , make a mutable map and pass it in setUserAdditionalDetails function. Eg:

    val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
    userAdditionalDetails.put("userMobile",99XXXXXXXX)
    TrackierSDK.setUserAdditionalDetails(userAdditionalDetails)    
    
### Tracking Via APK :-

To enable tracking via APK,

KOTLIN :-

	 sdkConfig.setManualMode(true)
	 TrackierSDK.setLocalRefTrack(true,"_")
	 TrackierSDK.initialize(sdkConfig)

JAVA :-

	 sdkConfig.setManualMode(true);
	 TrackierSDK.setLocalRefTrack(true,"_");
	 TrackierSDK.initialize(sdkConfig);

 Note:- Make sure to take EXTERNAL_STORAGE_READ permission before enabling this feature. For reference you can see [example directory](https://github.com/trackier/android_sdk/blob/master/TrackierSDK/example-app-kotlin/app/src/main/java/com/trackier/example_app_kotlin/MainActivity.kt) .


Now you can  explicitly fire Install whenever desired,

KOTLIN :-
 
            TrackierSDK.fireInstall()
	    
JAVA :- 

            TrackierSDK.fireInstall();


### Track Events :-
 
#### Retrieve Event Id from dashboard:-
 
 
 
 
#### Track Event :-
   
KOTLIN:-
```kotlin
      val event = TrackierEvent(TrackierEvent.LEVEL_ACHIEVED)
      event.param1 = "Level 10"
      TrackierSDK.trackEvent(event)
```

JAVA :-
```java
      TrackierEvent event = new TrackierEvent(TrackierEvent.LEVEL_ACHIEVED);
      event.param1 = "Level 10";
      TrackierSDK.trackEvent(event);
 
```
 
 
#### Track with Currency & Revenue Event :-
 
KOTLIN :-
```kotlin
       val event = TrackierEvent(TrackierEvent.PURCHASE)
       event.param1 = "Praram Name"
       event.revenue = 2.5
       event.currency = "USD"
       TrackierSDK.trackEvent(event)
```
 
JAVA :-
```java
       TrackierEvent event = new TrackierEvent(TrackierEvent.PURCHASE);
       event.param1 = "Praram Name";
       event.revenue = 2.5;
       event.currency = "USD";
       TrackierSDK.trackEvent(event);
```
### Proguard Settings

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
