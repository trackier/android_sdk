package com.trackier.example_app_kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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

        // var user: User = UserUtil.setupUser(this, null)

        if (Intent.ACTION_MAIN == action) run {
            // user launches the app from app icon or widget
            // do your normal logic here based on your requirements

        } else if (Intent.ACTION_VIEW == action) {
            // deferred deep link is running the app
            // customise the color of gif
            val uri = getUri()
            if (uri != null) run {
                // user = UserUtil.setupUser(this, getDeepLinkParams(uri))
                Log.d("TAG", "onCreate: "+getDeepLinkParams(uri).toString())
            }
        }


    }
    private fun getUri(): Uri? {
        val uri = intent.data
        return uri ?: if (intent.hasExtra("deferred_deeplink"))
            Uri.parse(intent.extras?.getString("deferred_deeplink"))
        else
            null
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