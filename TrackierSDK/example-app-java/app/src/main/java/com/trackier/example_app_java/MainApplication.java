package com.trackier.example_app_java;

import android.app.Application;
import android.content.Context;

import com.trackier.sdk.TrackierEvent;
import com.trackier.sdk.TrackierSDK;
import com.trackier.sdk.AttributionParams;
import com.trackier.sdk.TrackierSDKConfig;

public class MainApplication extends Application {

    private static Context mContext;
    private static MainApplication mInstance;
    public static Context getmContext() {
        return mContext;
    }
    public static void setmContext(Context mContext) {
        MainApplication.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setmContext(getApplicationContext());
        mInstance = this;

        final String TR_DEV_KEY  = "f4fjfjgjg-33c5-4c9f-xxxxxxx";
        TrackierSDKConfig sdkConfig = new TrackierSDKConfig(this, TR_DEV_KEY,"development");
//        AttributionParams params = new AttributionParams();
//        params.setParterId("ADD_PARTNER_ID_HERE");
//        params.setChannel("ADD_CHANNEL_HERE"); // optional
//        sdkConfig.setAttributionParams(params);
//        TrackierSDK.setEnabled(false);
        TrackierSDK.initialize(sdkConfig);
    }
    public static synchronized MainApplication getInstance() {
        return mInstance;
    }
}