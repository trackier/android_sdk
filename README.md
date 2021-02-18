# android_sdk
Trackier MMP Android SDK

Android SDK integration for developers
 
Integration
 
1)Add TrackierSDK to your app
 
1.1)Add the SDK to your project
 
	Add the SDK using Gradle:- Add the code below to Module-level/app/build.gradle before
	dependencies:
 
	repositories {
     	mavenCentral()
   	}
     
1.2)Add the latest version of Trackier SDK as a dependency. You can find the latest version    
   here.
 
   implementation 'com.trackier:android-sdk:1.0.1'
 
1.3)Sync the project to retrieve the dependencies – see the following screenshot:
 
<Image>
 
2)Add required permissions
 
 
2.1)Add the following permissions to Androidmanifest.xml:
  
   <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 
  <!-- Optional : -->
 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 
 
3) Adding Android install referrer to your app
 
3.1)Add the Android Install Referrer as a dependency. You can find the latest version here
 
dependencies {
// make sure to use the latest SDK version:
// https://mvnrepository.com/artifact/com.trackier/android-sdk   
 
implementation 'com.trackier:android-sdk:1.0.1'
implementation 'com.android.installreferrer:installreferrer:1.1'
}
 
3.2)Sync the project to retrieve the dependencies – see the following screenshot:
 
<Image>
 
 
Implement and initialize the SDK
 
1)Retrieve your dev key
 
2)Initialize the SDK
  
   We recommend initializing the SDK inside the app’s global application class. This allows the SDK to  
   initialize in all scenarios, including deep linking.
 
  The steps listed below take place inside the app’s global application class.
 
  2.1) Inside the app’s global class, import the following libraries
 
   	import com.trackier.sdk.TrackierEvent
   	import com.trackier.sdk.TrackierSDK
   	import com.trackier.sdk.TrackierSDKConfig
 
2.2) Inside the global class, assign your dev key to a variable, preferably name TR_DEV_KEY.
 
   Important: it is crucial to use the correct dev key when initializing the SDK. Using the wrong dev key or an     
   incorrect dev key impact all traffic sent from the SDK and cause attribution and reporting issues.
 
 KOTLIN:-
 
    	const val TR_DEV_KEY = “c814db62-c196-4505-bc8b-46fa8e37f688”;
 
JAVA :-
 
     	private static final String TR_DEV_KEY  = "c814db62-c196-4505-bc8b-46fa8e37f688";
 
2.3)Inside the global class, after call to super.create(), initialize SDK like this:-
 
KOTLIN :-
 
    	val sdkConfig = TrackierSDKConfig(this,  TR_DEV_KEY,  "put environment here you working on")
    	TrackierSDK.initialize(sdkConfig)
 
 
 
JAVA :-
 
  	TrackierSDKConfig  sdkConfig = new   TrackierSDKConfig(this, TR_DEV_KEY,"test");
  	TrackierSDK.initialize(sdkConfig);
 
Note:- Don’t forgot to register the global application class inside Manifest. You can register your global class in Manifest like this, suppose the name of global class is TrackeirApplication:
 
android:name = “APP_PACAGE_NAME. TrackeirApplication”
 
 
 
3)Track Events :-
 
3.1) Retrieve Event Id from dashboard:-
 
 
 
 
 
 
 
 
 
 
 
3.2)Track Event :-
   
KOTLIN:-
 
        	val event = TrackierEvent(TrackierEvent.UPDATE)
        	event.param1 = “Param Name”
        	TrackierSDK.trackEvent(event)
 
JAVA :-
 
TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
                     	event.param1 = “Praram Name”;
                     	TrackierSDK.trackEvent(event);
 
 
 
 
3.3)Track with Currency & Revenue Event :-
 
KOTLIN:-
 
        	val event = TrackierEvent(TrackierEvent.UPDATE)
        	event.param1 = “Praram Name”;
        	event.revenue = 0.5
        	event.currency = “USD”
        	TrackierSDK.trackEvent(event)
 
JAVA :-
 
          TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
                                event.param1 = “Praram Name”;
        	                      event.revenue = 0.5;
        	                      event.currency = “USD”;
                                TrackierSDK.trackEvent(event);
 
 
 
 
 
 
 
                              
 

