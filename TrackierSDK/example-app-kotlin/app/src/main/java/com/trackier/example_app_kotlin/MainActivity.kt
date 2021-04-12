package com.trackier.example_app_kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.trackier.sdk.DeeplinkEvent
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_event_track = findViewById(R.id.event_track) as Button
        val btn_event_curr_track = findViewById(R.id.event_curr_track) as Button

        btn_event_track.setOnClickListener {
            val event = TrackierEvent(TrackierEvent.UPDATE)
            event.param1 = "Param_Name"
            TrackierSDK.trackEvent(event)
            Log.d("TAG", "onClick: event_track ")
        }

        btn_event_curr_track.setOnClickListener {
            val event = TrackierEvent(TrackierEvent.UPDATE)
            event.param1 = "Praram Name";
            event.revenue = 0.5
            event.currency = "USD"
            TrackierSDK.trackEvent(event)
            Log.d("TAG", "onClick: event_curr_track ")
        }

        val action: String? = intent?.action
        val data: Uri? = intent?.data
        if(data!=null){
            Log.d("TAG", "onCreate: "+data.toString())
            TrackierSDK.appWillOpenUrl(data)
        }

    }

    private fun getDeepLinkParams(uri: Uri?): HashMap<String, String> {
        val deepLinkingParams = HashMap<String, String>()
        if (uri != null) {
            val paramNames = uri.queryParameterNames
            for (name in paramNames) {
                deepLinkingParams[name] = uri.getQueryParameter(name)!!
            }
        }
        return deepLinkingParams
    }



}