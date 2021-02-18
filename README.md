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
  implementation 'com.trackier:android-sdk:1.0.1'
```
 
#### Sync the project to retrieve the dependencies – see the following screenshot:

![Screenshot 2021-02-18 at 1 20 16 PM](https://user-images.githubusercontent.com/34488320/108324546-0e3d8500-71ee-11eb-9c8b-2c06c96d2f00.png)
 
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
 
  implementation 'com.trackier:android-sdk:1.0.1'
  implementation 'com.android.installreferrer:installreferrer:2.2'
}
```
 
#### Sync the project to retrieve the dependencies – see the following screenshot:
 
![Screenshot 2021-02-18 at 1 20 51 PM](https://user-images.githubusercontent.com/34488320/108324596-1dbcce00-71ee-11eb-925a-a05b7c4035ef.png)
 
 
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
 
Note:- Don’t forgot to register the global application class inside Manifest. You can register your global class in Manifest like this, suppose the name of global class is TrackeirApplication:

```java 
android:name = "APP_PACAGE_NAME.TrackeirApplication"
```
 
### Track Events :-
 
#### Retrieve Event Id from dashboard:-
 
 
 
 
#### Track Event :-
   
KOTLIN:-
```kotlin
          val event = TrackierEvent(TrackierEvent.UPDATE)
          event.param1 = "Param Name"
          TrackierSDK.trackEvent(event)
```

JAVA :-
```java
  TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
  event.param1 = “Praram Name”;
  TrackierSDK.trackEvent(event);
 
```
 
 
#### Track with Currency & Revenue Event :-
 
KOTLIN :-
```kotlin
          val event = TrackierEvent(TrackierEvent.UPDATE)
          event.param1 = "Praram Name"
          event.revenue = 0.5
          event.currency = "USD"
          TrackierSDK.trackEvent(event)
```
 
JAVA :-
```java
          TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
          event.param1 = “Praram Name”;
          event.revenue = 0.5;
          event.currency = “USD”;
          TrackierSDK.trackEvent(event);
```
