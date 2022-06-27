# Android SDK

## Table of Content

### Trackier Android SDK Integration

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
* [Events Tracking](#qs-track-event)
    * [Retrieve Event Id from dashboard](#qs-retrieve-event-id)
    * [Track with Currency & Revenue Event](#qs-track-event-with-currencey)
    * [Add custom params with event](#qs-add-custom-parms-event)
* [Defer Start Sdk](#qs-defer-start-sdk)
* [Disable Organic Tracking](#qs-disable-orgainic-tracking)
* [Uninstall Tracking](#qs-track-uninstall)
* [Proguard Settings](#qs-progaurd-trackier-sdk)

## <a id="qs-add-trackier-sdk"></a>Integrate Trackier SDK to your app

### <a id="qs-add-sdk-gradle"></a>Add the SDK using Gradle
<p>Add the code below to Module level app build.gradle before dependencies</p>

```gradle
  repositories {
    mavenCentral() //Please add this code to your module level build.gradle
  }
```
Below are the for screenshot reference.

Screenshot[1]

<img width="1000" alt="Screenshot 2022-06-08 at 12 52 39 PM" src="https://user-images.githubusercontent.com/16884982/172559795-3aa499b7-71e6-4f6f-bf31-eba69c477276.png">


## <a id="qs-add-latest-version-sdk"></a>Add the latest version of Trackier SDK as a dependency

You can find the latest version [here](https://search.maven.org/artifact/com.trackier/android-sdk)

```gradle 
  implementation 'com.trackier:android-sdk:1.6.22' //Add this Trackier SDK dependency in the build.gradle
```
Also, please check the screenshot[2] in the below section for reference.

## <a id="qs-add-install-referre"></a>Adding Android install referrer to your app

Add the Android Install Referrer as a dependency. You can find the latest version [here](https://search.maven.org/artifact/com.trackier/android-sdk)

```gradle 
  dependencies {
    // make sure to use the latest SDK version:
    // https://mvnrepository.com/artifact/com.trackier/android-sdk   
  
    implementation 'com.trackier:android-sdk:1.6.22' //Add this Trackier SDK dependency in the build.gradle
    implementation 'com.android.installreferrer:installreferrer:2.2' //Add this install referrer dependency in the build.gradle
  }
```

Sync the project to retrieve the dependencies – see the following screenshot.

Screenshot[2]

<img width="1000" alt="Screenshot 2022-06-22 at 11 52 52 AM" src="https://user-images.githubusercontent.com/16884982/174958114-441c58a7-7db4-4973-ae62-00c3cac763fc.png">



## <a id="qs-add-request-permissions"></a>Add required permissions

Trackier SDK need the following below permission in the manifest.xml. 

Please add the below permission in your app project manifest.xml. if they are not added.

```java 
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

  <!-- Optional : -->
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

Below are the Screenshot for the reference

Screenshot[3]

<img width="1000" alt="Screenshot 2022-06-08 at 2 47 52 PM" src="https://user-images.githubusercontent.com/16884982/172580625-ca43dd96-15bf-4240-a539-2b594463101a.png">


## <a id="qs-getting-gaid"></a> Getting Google Advertising ID

Trackier SDK need the advertising id from the application. 

For achieving this, you need to add some line of code in the build.gradle and also in Manifest.xml for read the Advertising id from the application which is mentioned below

- Add the google advertising id dependency in your app **build.gradle**

```gradle
dependencies {
  // This can be added where the SDK dependency has been added
  implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
}
```
Below are the Screenshot for the reference

ScreenShot[4]

<img width="1000" alt="Screenshot 2022-06-22 at 12 01 26 PM" src="https://user-images.githubusercontent.com/16884982/174959194-d3c2e05f-7b0a-48cc-9058-e70ef7a04a4d.png">


- Update your Android Manifest file by adding the following permission. This is required if your app is targeting devices with android version 12+

```xml
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

- Add meta data inside the application tag (If not already added)
```xml
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" /> //Add this meta-data in the manifest.xml under Application tag.
```
Below are the screenshot of application tag in manifest.xml for the reference

Screenshot[5]

<img width="1000" alt="Screenshot 2022-06-08 at 4 16 40 PM" src="https://user-images.githubusercontent.com/16884982/172598171-2f16460b-9376-40ad-b13d-6be226754aa9.png">


## <a id="qs-implement-trackier-sdk"></a>Integrate and Initialize the Trackier SDK

### <a id="qs-retrieve-dev-key"></a>Retrieve your dev key

For initialising the Trackier SDk. First, We need to generate the Sdk key from the Trackier MMP panel.

Following below are the steps to retrieve the development key:-

- Login your Trackier Panel
- Select your application and click on Action button and login as
- In the Dashboard, Click on the` SDK Integration` option on the left side of panel. 
- under on the SDK Integration, You will be get the SDK Key.

After follow all steps, Your SDK key look like the below screenshot

Screenshot[6]

<img width="1000" alt="Screenshot 2022-06-10 at 3 46 48 PM" src="https://user-images.githubusercontent.com/16884982/173044860-a540706c-ad10-4174-aaf0-7cf9290fc949.png">



### <a id="qs-initialize-trackier-sdk"></a>Integrate the Trackier SDK in the Native Application

<p>We recommend initializing the SDK inside the app’s global application class. This allows the SDK to
initialize in all scenarios, including deep linking.

For initializing the sdk in your app project. Add the initialize function code in the global application class. 

If your project app does not have application class in your project. Follow the below steps

- Create a new class that extends the `Application`
- Open the `AndroidManifest.xml` file of your project app and find the `<application>` elements
- Add the attribute `android:name` and set it to the name of your new application class.

In our Trackier example app, we use `Application` class named as `MainApplication`.

We configured the manifest file as like mentioned in below:-

```xml
	
  <application
        android:name=".MainApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Exampleappjava">

        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />

        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

``` 

After following the above steps, you need to add the following code in the MainApplication class under `onCreate()` method.

Check the below example code for the SDK initializations.


  #### Java

```java 

import com.trackier.sdk.TrackierSDK;
import com.trackier.sdk.TrackierSDKConfig;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final String TR_DEV_KEY  = "XXXXXXX-XXXX-XXXX-80e3-5938fadff"; //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
         * In First argument, you need to pass context of the application
         * In second argument, you need to pass the Trackier SDK api key
         * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        TrackierSDKConfig sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY,"development");
        TrackierSDK.initialize(sdkConfig);
    }

}
  
```
Screenshot for the above initializing code:-

Screenshot[7]

<img width="1000" alt="Screenshot 2022-06-09 at 1 04 05 AM" src="https://user-images.githubusercontent.com/16884982/172702379-45d55946-bb5f-491d-a0c2-277000a3b7c4.png">

<p>
Important: it is crucial to use the correct dev key when initializing the SDK. Using the wrong dev key or an
incorrect dev key impact all traffic sent from the SDK and cause attribution and reporting issues.
</p>

  #### Kotlin

<p>We recommend initializing the SDK inside the app’s global application class. This allows the SDK to
initialize in all scenarios, including deep linking.

For initializing the sdk in your app project. Add the initialize function code in the global application class. 

If your project app does not have application class in your project. Follow the below steps

- Create a new class that extends the `Application`
- Open the `AndroidManifest.xml` file of your project app and find the `<application>` elements
- Add the attribute `android:name` and set it to the name of your new application class.

In our Trackier example app, we use `Application` class named as `MainApplication`.

We configured the manifest file as like mentioned in below:-

```xml
	
  <application
        android:name=".MainApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Exampleappkotlin">

        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />

        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

``` 
After following the above steps, you need to add the following code in the MainApplication class under `onCreate()` method.

Check the below example code for the SDK initializations.

```kotlin 

import android.app.Application
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig

class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val TR_DEV_KEY: String = "xxxx-xx-4505-bc8b-xx" //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
            * In First argument, you need to pass context of the application
            * In second argument, you need to pass the Trackier SDK api key
            * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        TrackierSDK.initialize(sdkConfig)
    }
}

```
Screenshot for the above initializing code of Trackier example app:-

Screenshot[8]
<img width="1440" alt="Screenshot 2022-06-09 at 12 43 24 PM" src="https://user-images.githubusercontent.com/16884982/172787564-365384c5-ca11-4894-8953-63c6195e67ed.png">


<p>
Important: it is crucial to use the correct dev key when initializing the SDK. Using the wrong dev key or an
incorrect dev key impact all traffic sent from the SDK and cause attribution and reporting issues.
</p>

## <a id="qs-track-event"></a>Events Tracking

<a id="qs-retrieve-event-id"></a>Trackier events trackings enable to provides the insights into how to user interacts with your app. 
Trackier sdk easily get that insights data from the app. Just follow with the simple events integration process

Trackier provides the `Built-in events` and `Customs events` on the Trackier panel.

Built-in Events - Built-in events are the list of constants events which already been created on the dashboard. You can use directly to track
those events. Just need to implements events in the app projects.

Screenshot[9]

<img width="1000" alt="Screenshot 2022-06-10 at 1 23 01 PM" src="https://user-images.githubusercontent.com/16884982/173018185-3356c117-8b9f-46d1-bfd5-c41653f9ac9e.png">

### Example code for calling Built-in events

#### Java

```java

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trackier.sdk.TrackierEvent;
import com.trackier.sdk.TrackierSDK;

public class MainActivity extends AppCompatActivity {

    Button eventTrack, eventTrackWithCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventTrack =(Button) findViewById(R.id.event_track);
        eventTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsTracking();
            }
        });

    }

    private void eventsTracking(){
        // Below are the example of built-in events function calling
        //The arguments - "TrackierEvent.LOGIN" passed in the Trackier event class is Events id

        TrackierEvent event = new TrackierEvent(TrackierEvent.LOGIN);

        /*Below are the function for the adding the extra data,
        You can add the extra data like login details of user or anything you need.
        We have 10 params to add data, Below 5 are mentioned*/

        event.param1 = "Param 1";
        event.param2 = "Param 2";
        event.param3 = "Param 3";
        event.param4 = "Param 4";
        event.param5 = "Param 5";
        TrackierSDK.trackEvent(event);
        Log.d("TAG", "onClick: event_track ");

    }
}

```

Note:- Argument in Trackier event class is event Id.

You can integrate inbuilt params with the event. In-built param list are mentioned below:-  

orderId, revenue, currency, param1, param2, param3 ,param4, param5, param6, param7, param8, param9, param10.


#### Kotlin

```kotlin
	
import android.os.*
import android.util.Log
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_event_track = findViewById(R.id.event_track) as Button
        val btn_event_curr_track = findViewById<Button>(R.id.event_curr_track)

        btn_event_track.setOnClickListener {
            eventsTracking()
        }

    }

    fun eventsTracking(){
        
        // Below are the example of built-in events function calling
        //The arguments - "TrackierEvent.LOGIN" passed in the Trackier event class is Events id.

        val event = TrackierEvent(TrackierEvent.LOGIN)

        /*Below are the function for the adding the extra data,
       You can add the extra data like login details of user or anything you need.
       We have 10 params to add data, Below 5 are mentioned*/

        event.param1 = "Param 1";
        event.param2 = "Param 2";
        event.param3 = "Param 3";
        event.param4 = "Param 4";
        event.param5 = "Param 5";
        TrackierSDK.trackEvent(event)
        Log.d("TAG", "onClick: event_track ")

    }
}

```
Note:- Argument in Trackier event class is event Id.

You can integrate inbuilt params with the event. In-built param list are mentioned below:-  

orderId, revenue, currency, param1, param2, param3 ,param4, param5, param6, param7, param8, param9, param10.


Customs Events - Customs events are created by user as per their required business logic. You can create the events in the Trackier dashboard and integrate those events in the app project.

Screenshot[10]

<img width="1000" alt="Screenshot 2022-06-10 at 3 49 22 PM" src="https://user-images.githubusercontent.com/16884982/173045367-28cf3ead-9123-4bf3-ab86-54f38c9149db.png">

### Example code for calling Customs Events

#### Java 

```java

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trackier.sdk.TrackierEvent;
import com.trackier.sdk.TrackierSDK;

public class MainActivity extends AppCompatActivity {

    Button eventTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventTrack =(Button) findViewById(R.id.event_track);
        eventTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customEventsTracking();
            }
        });

    }

    private void customEventsTracking(){

        // Below are the example of customs events function calling
        //The arguments - "sEMWSCTXeu" passed in the event class is Events id

        TrackierEvent event = new TrackierEvent("sEMWSCTXeu");

        /*Below are the function for the adding the extra data,
        You can add the extra data like login details of user or anything you need.
        We have 10 params to add data, Below 5 are mentioned*/
        event.param1 = "Param 1";
        event.param2 = "Param 2";
        event.param3 = "Param 3";
        event.param4 = "Param 4";
        event.param5 = "Param 5";
        TrackierSDK.trackEvent(event);
        Log.d("TAG", "onClick: event_track ");

    }
}

```

#### Kotlin

```kotlin
	
mport android.os.*
import android.util.Log
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_event_track = findViewById(R.id.event_track) as Button
        val btn_event_curr_track = findViewById<Button>(R.id.event_curr_track)

        btn_event_curr_track.setOnClickListener {
            customEventsTrcking()
        }

    }

    fun customEventsTrcking() {
        // Below are the example of customs events function calling
        //The arguments - "sEMWSCTXeu" passed in the event class is Events id
        val event = TrackierEvent("sEMWSCTXeu")

        /*Below are the function for the adding the extra data,
         You can add the extra data like login details of user or anything you need.
         We have 10 params to add data, Below 5 are mentioned*/
        event.param1 = "Praram Name";
        event.param2 = "Param 2";
        event.param3 = "Param 3";
        event.param4 = "Param 4";
        event.param5 = "Param 5";
        TrackierSDK.trackEvent(event)
        Log.d("TAG", "onClick: event_curr_track ")

    }
}


```

### <a id="qs-track-event-with-currencey"></a>Revenue Event Tracking

Trackier allow user to pass the revenue data which is generated from the app through `Revenue events`. 
It is mainly used to keeping record of generating revenue from the app and also you can pass currency as well.

  #### Java

```java
	
 public class MainActivity extends AppCompatActivity {

    Button eventTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventTrack =(Button) findViewById(R.id.event_track);
        eventTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revenueEventsTracking();
            }
        });

    }

    private void revenueEventsTracking(){
        // Below are the example of inbuilt events function calling
        //The arguments - "TrackierEvent.LOGIN" passed in the event class is Events id
        TrackierEvent event = new TrackierEvent("sEMWSCTXeu");
        
        //Passing the revenue events be like below example
        event.revenue = 2.5; //Pass your generated revenue here.
        event.currency = "USD"; //Pass your currency here.
        
        TrackierSDK.trackEvent(event);
        Log.d("TAG", "onClick: event_track ");
        


    }
}

```

#### Kotlin

```kotlin
	
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_event_track = findViewById(R.id.event_track) as Button
        val btn_event_curr_track = findViewById<Button>(R.id.event_curr_track)

        btn_event_curr_track.setOnClickListener {
            revenueEventsTrcking()
        }

    }

    fun revenueEventsTrcking() {
        // Below are the example of customs events function calling
        //The arguments - "sEMWSCTXeu" passed in the event class is Events id
        val event = TrackierEvent("sEMWSCTXeu")

        //Passing the revenue events be like below example
        event.revenue = 2.5 //Pass your generateed revenue here.
        event.currency = "USD" //Pass your currency here.

        TrackierSDK.trackEvent(event)
        Log.d("TAG", "onClick: event_curr_track ")

    }
}

 
```

### <a id="qs-add-custom-parms-event"></a>Pass the custom params in events



  #### Java

```kotlin 

   private void customEventsTracking(){
        // Below are the example of inbuilt events function calling
        //The arguments - "TrackierEvent.LOGIN" passed in the event class is Events id
        TrackierEvent event = new TrackierEvent("sEMWSCTXeu");

        //Passing the extra data through customs params
        HashMap<String,Object>  eventCustomParams= new HashMap<>();
        eventCustomParams.put("customParam1","xxxxxx");
        eventCustomParams.put("customParam2","xxxxxx");
        event.ev = eventCustomParams; //Pass the reference to the ev
        TrackierSDK.trackEvent(event);
        Log.d("TAG", "onClick: event_track ");

    }
```

* First create a mutable map
* Pass its reference to event.ev param of event
* Pass event reference to trackEvent method of TrackerSDK


## <a id="qs-assosoiate-user-info"></a>Passing User Data to SDK

Trackier allows to pass additional data like Userid, Email to SDK so that same can be correlated to the Trackier Data and logs.

Just need to pass the data of `User Id`, `Email Id` and other additional data to Trackier sdk function which is mentioned below:- 


  #### Java

```java 

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.trackier.sdk.TrackierSDK;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button eventTrack, eventTrackWithCurrency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDetails();
    }

    private void userDetails(){
        
        /*Passing the UserId and User EmailId Data */
        TrackierSDK.setUserId("XXXXXXXX"); //Pass the UserId values here
        TrackierSDK.setUserEmail("abc@gmail.com"); //Pass the user email id in the argument.
        
        /*Passing the additional data */
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put("Name","Sanu"); //You can pass the Username data.
        userDetails.put("UserMobile","873287XXXX"); // You can pass user mobile number
        TrackierSDK.setUserAdditionalDetails(userDetails);
    }
}

```


  #### Kotlin

```kotlin 
  
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.trackier.sdk.TrackierSDK


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDetails()
        }

    fun userDetails(){
        
        /*Passing the UserId and UserEmail Data*/
        TrackierSDK.setUserId("XXXXXXXX") //Pass the UserId values here
        TrackierSDK.setUserEmail("abc@gmail.com") //Pass the user email id in the argument.
        
        /*Passing the Additional Data*/
        val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
        userAdditionalDetails.put("UserName","ABC") //You can pass the Username data.
        userAdditionalDetails.put("MobileNumber","8878328XXX") // You can pass user mobile number
        TrackierSDK.setUserAdditionalDetails(userAdditionalDetails) //Pass the userAdditionalDetails reference in this method.
    }

}

```

### For Passing Additional Data
 
Trackier allow for passing the additional user details like `UserName`, `Mobile Number`, `UserAge`, `UserGender` etc. . You need to first make a hashmap and pass it in setUserAdditionalDetails function. The example are in mentioned below 


  #### Java

```java 
	
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.trackier.sdk.TrackierSDK;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button eventTrack, eventTrackWithCurrency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDetails();
    }

    private void userDetails(){
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put("Name","Sanu"); //You can pass the Username data.
        userDetails.put("UserMobile","873287XXXX"); // You can pass user mobile number
        TrackierSDK.setUserAdditionalDetails(userDetails);
    }
}

``` 

  #### Kotlin

```kotlin 

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.trackier.sdk.TrackierSDK


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDetails()
        }

    fun userDetails(){
        val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
        userAdditionalDetails.put("UserName","ABC") //You can pass the Username data.
        userAdditionalDetails.put("MobileNumber","8878328XXX") // You can pass user mobile number.
        TrackierSDK.setUserAdditionalDetails(userAdditionalDetails) //Pass the userAdditionalDetails reference in this method.
    }

}

	
```

## <a id="qs-track-uninstall"></a>Uninstall Tracking

Trackier uninstall functionality is used to track the uninstall of the application from the devices. It is very useful for tracking the quality of the user.
 
  #### Java 

```java 
  private FirebaseAnalytics mFirebaseAnalytics; 
  FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this); 
  mFirebaseAnalytics.setUserProperty("ct_objectId", Objects.requireNonNull(TrackierSDK.getTrackierId())); 
``` 
 
* Add the above code to your app to set up a common identifier. 
* Set the `app_remove` event as a conversion event in Firebase. 
* Use the Firebase cloud function to send uninstall information to Trackier MMP. 
* You can find the support article [here](https://help.trackier.com/support/solutions/articles/31000162841-android-uninstall-tracking). 


## <a id="qs-defer-start-sdk"></a>Defer SDK start
### To defer sdk start

It is customized way to initialized the sdk. It is required when you need to fire the install in specific page. 
After implementing this feature, SDK will not initialize on the time of application open.

  #### Java

```java 

 import android.app.Application;
 import com.trackier.sdk.TrackierSDK;
 import com.trackier.sdk.TrackierSDKConfig;

import java.util.HashMap;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final String TR_DEV_KEY = "XXXXXXX-XXXX-XXXX-80e3-5938fadff"; //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
         * In First argument, you need to pass context of the application
         * In second argument, you need to pass the Trackier SDK api key
         * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        TrackierSDKConfig sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY, "development");

        //Passing User Information on time of initialization     
        sdkConfig.setManualMode(true);
        TrackierSDK.setLocalRefTrack(true, "_");
        TrackierSDK.setUserId("XXXXXXXX"); //Pass the UserId values here
        TrackierSDK.setUserEmail("abc@gmail.com"); //Pass the user email id in the argument.
        TrackierSDK.initialize(sdkConfig);
     
    }

}
```


  #### Kotlin

```kotlin 
  
import android.app.Application
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val TR_DEV_KEY: String = "xxxx-xx-4505-bc8b-xx" //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
            * In First argument, you need to pass context of the application
            * In second argument, you need to pass the Trackier SDK api key
            * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")

        sdkConfig.setManualMode(true)
        TrackierSDK.setLocalRefTrack(true,"_")
        TrackierSDK.initialize(sdkConfig)
        //Passing User Information on time of initialization
        TrackierSDK.setUserId("XXXXXXXX")//Pass the UserId values here
        TrackierSDK.setUserEmail("abc@gmail.com")//Pass the user email id in the argument.
        TrackierSDK.initialize(sdkConfig)
    }
}


```

* Set manual mode to true by passing true boolean value to setManualMode.
* The second method  is to associate user email in which .

Note:- Make sure to take EXTERNAL_STORAGE_READ permission before enabling this feature. For reference you can see [example directory](https://github.com/trackier/android_sdk/tree/master/TrackierSDK/example-app-kotlin)

Now you can explicitly fire Install whenever desired,


  #### Java

```java 
  TrackierSDK.fireInstall(); // Call this function on the specific Activity, wherever you want to fire the install.
```


  #### Kotlin

```kotlin 
  TrackierSDK.fireInstall() // Call this function on the specific Activity, wherever you want to fire the install.

```

## <a id="qs-disable-orgainic-tracking"></a>Disable Organic Track 

This features is used to disable the Organic traffic. Just need to call the `sdkConfig.disableOrganicTracking(true);` function and pass boolean `true` value in it. 

Please follow the below example:-


  #### Java

```java 
	
import android.app.Application;
import com.trackier.sdk.TrackierSDK;
import com.trackier.sdk.TrackierSDKConfig;

import java.util.HashMap;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final String TR_DEV_KEY = "XXXXXXX-XXXX-XXXX-80e3-5938fadff"; //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
         * In First argument, you need to pass context of the application
         * In second argument, you need to pass the Trackier SDK api key
         * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        TrackierSDKConfig sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY, "development");

        sdkConfig.disableOrganicTracking(true); // Pass true value for disable organic tracking.
        TrackierSDK.initialize(sdkConfig);
    }

}



```


  #### Kotlin

```kotlin 

import android.app.Application
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val TR_DEV_KEY: String = "xxxx-xx-4505-bc8b-xx" //Please pass your Development key here.

        /*While Initializing the Sdk, You need to pass the three parameter in the TrackierSDKConfig.
            * In First argument, you need to pass context of the application
            * In second argument, you need to pass the Trackier SDK api key
            * In third argument, you need to pass the environment which can be either "development", "production" or "testing". */
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        sdkConfig.disableOrganicTracking(true); // Pass true value for disable organic tracking/
        TrackierSDK.initialize(sdkConfig)
    }
}


```



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


