package com.trackier.example_app_java;

import android.app.Application;
import android.content.Context;
import com.trackier.sdk.TrackierSDK;
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

        final String TR_DEV_KEY  = "xxxx-xx-4505-bc8b-xx";
        TrackierSDKConfig sdkConfig = new   TrackierSDKConfig(this, TR_DEV_KEY,"production");
        TrackierSDK.initialize(sdkConfig);
    }
    public static synchronized MainApplication getInstance() {
        return mInstance;
    }
}