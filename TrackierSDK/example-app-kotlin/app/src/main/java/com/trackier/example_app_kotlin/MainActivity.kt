package com.trackier.example_app_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK
import java.util.*


class MainActivity : AppCompatActivity() {

    private val PERMS_STORAGE = 1337

    @SuppressLint("WrongConstant", "QueryPermissionsNeeded")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_event_track = findViewById(R.id.event_track) as Button
        val btn_event_curr_track = findViewById<Button>(R.id.event_curr_track)
        btn_event_track.setOnClickListener {
            val event = TrackierEvent(TrackierEvent.UPDATE)
            event.param1 = "Param_Name"
            event.couponCode = "TEST_COUPON"
            event.discount = 10.5f
//            event.c_code = "tes"
//            event.discount = 3f
//            TrackierSDK.setUserName("abcd")
//            TrackierSDK.setUserPhone("1234456545")
            TrackierSDK.trackEvent(event)
            Log.d("TAG", "onClick: event_track ")
        }

        btn_event_curr_track.setOnClickListener {
            val event = TrackierEvent(TrackierEvent.UPDATE)
            event.param1 = "Param Name"
            event.revenue = 0.5
            event.currency = "USD"
            TrackierSDK.trackEvent(event)
            Log.d("TAG", "onClick: event_curr_track ")

        }

        val action: String? = intent?.action
        if (Intent.ACTION_MAIN == action) run {
            // user launches the app from app icon or widget
            // do your normal logic here based on your requirements

        } else if (Intent.ACTION_VIEW == action) {
            // deferred deep link is running the app
            // customise the color of gif
            val uri = getUri()
            if (uri != null) run {
                Log.d("TAG", "onCreate: " + getDeepLinkParams(uri).toString())
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMS_STORAGE) {
            loadRoot()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private fun loadRoot() {
        if (hasStoragePermission()) {
            Toast.makeText(this, "You have required permission", Toast.LENGTH_LONG).show()
            TrackierSDK.fireInstall()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.GET_ACCOUNTS
                ),
                PERMS_STORAGE
            )
        }
    }

    private fun hasStoragePermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            TODO("VERSION.SDK_INT < M")
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun hasAllFilesPermission() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        TODO("VERSION.SDK_INT < R")
    }

}