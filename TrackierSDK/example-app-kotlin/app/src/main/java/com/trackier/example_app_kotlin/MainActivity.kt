package com.trackier.example_app_kotlin

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.multidex.BuildConfig
import com.trackier.sdk.TrackierEvent
import com.trackier.sdk.TrackierSDK
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Process
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
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

        //requestPermission()

    }

    @SuppressLint("QueryPermissionsNeeded")
    fun getDeviceInstallApplication(): String {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        return apps.joinToString()
    }

    private fun checkIsEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("sdk_gphone64_arm64")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator"))
    }


    fun getIpv4HostAddress(): String {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }

    fun getTotalMemory(context: Context): String {
        val actManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalMemory = memInfo.totalMem.toDouble() / (1024 * 1024 * 1024)
        return "$totalMemory"
    }
    fun getAvailableMemory(context: Context): String {
        val actManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val availMemory = memInfo.availMem.toDouble() / (1024 * 1024 * 1024)
        return "$availMemory"
    }

    fun getDeviceChargingStatus(): String {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            applicationContext.registerReceiver(null, ifilter)
        }
        // isCharging if true indicates charging is ongoing and vice-versa
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        var chargingStatus: String? = null
        chargingStatus = if (isCharging)
            "Device is Charging"
        else
            "Device is not Charging"
        return chargingStatus
    }

    fun getDeviceBootTime(): String {
        var bootTime =
            java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val resultDate = Date(bootTime)
        return sdf.format(resultDate)
    }

    var strOrientation: String? = null
    fun getDeviceOrientation(context: Context): String {
        var orientation = context.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            strOrientation = "portrait"
        else strOrientation = "landscape"
        return "$strOrientation"

    }

    fun getSystemVolume(): Int {
        val am = getSystemService(AUDIO_SERVICE) as AudioManager
        val volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        return volume_level
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getBatteryLevel(): Int {
        val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
        val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return batLevel

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getAvailableInternalStorage(): String? {
        val iPath: File = Environment.getDataDirectory()
        val iStat = StatFs(iPath.path)
        val iBlockSize = iStat.blockSizeLong
        val iAvailableBlocks = iStat.availableBlocksLong
        val iTotalBlocks = iStat.blockCountLong
        val iAvailableSpace = formatSize(iAvailableBlocks * iBlockSize)
        //val iTotalSpace = formatSize(iTotalBlocks * iBlockSize)
        return iAvailableSpace

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getTotalInternalStorage(): String? {
        val iPath: File = Environment.getDataDirectory()
        val iStat = StatFs(iPath.path)
        val iBlockSize = iStat.blockSizeLong
        val iAvailableBlocks = iStat.availableBlocksLong
        val iTotalBlocks = iStat.blockCountLong
        val iTotalSpace = formatSize(iTotalBlocks * iBlockSize)
        return iTotalSpace

    }

    private fun formatSize(size: Long): String? {
        var size = size
        var suffix: String? = null
        if (size >= 1024) {
            suffix = "KB"
            size /= 1024
            if (size >= 1024) {
                suffix = "MB"
                size /= 1024
            }
        }
        val resultBuffer = StringBuilder(java.lang.Long.toString(size))
        var commaOffset = resultBuffer.length - 3
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',')
            commaOffset -= 3
        }
        if (suffix != null) resultBuffer.append(suffix)
        return resultBuffer.toString()
    }

    fun getCPUDetails(): String? {
        val processBuilder: ProcessBuilder
        var cpuDetails = ""
        val DATA = arrayOf("/system/bin/cat", "/proc/cpuinfo")
        val `is`: InputStream
        val process: Process
        val bArray: ByteArray
        bArray = ByteArray(1024)
        try {
            processBuilder = ProcessBuilder(*DATA)
            process = processBuilder.start()
            `is` = process.inputStream
            while (`is`.read(bArray) !== -1) {
                cpuDetails = cpuDetails + String(bArray) //Stroing all the details in cpuDetails
            }
            `is`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return cpuDetails
    }

    fun cpuD(): String {
        var output = ""
        try {
            val DATA = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            val processBuilder = ProcessBuilder(*DATA)
            val process = processBuilder.start()
            val inputStream = process.inputStream
            val byteArry = ByteArray(1024)
            while (inputStream.read(byteArry) != -1) {
                output = output + String(byteArry)
            }
            inputStream.close()
            Log.d("CPU_INFO", output)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "$output"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getHeadphonesPlugged(): String {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val audioDevices = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        var headPhonePlugged: String? = null
        for (deviceInfo in audioDevices) {
            if (deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                || deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADSET
            ) {
                headPhonePlugged = "headphone plugged"
                return headPhonePlugged
            }
        }
        headPhonePlugged = "headphone not plugged"
        return headPhonePlugged
    }

    fun getEmail(context: Context?): String? {

        val accountManager = AccountManager.get(context)
        val account = getAccount(accountManager)
        return account?.name
    }

    private fun getAccount(accountManager: AccountManager): Account? {
        val accounts = accountManager.getAccountsByType("com.google")
        val account: Account? = if (accounts.isNotEmpty()) {
            accounts[0]
        } else {
            null
        }
        return account
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

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 30) {
            if (hasAllFilesPermission()) {
                Toast.makeText(this, "You have required permission", Toast.LENGTH_LONG).show()
                TrackierSDK.fireInstall()
            } else {
                val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                startActivity(
                    Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )
                )
            }
        } else {
            Toast.makeText(this, "Opps ! Permission Not Granted", Toast.LENGTH_LONG).show()
        }

        return true
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