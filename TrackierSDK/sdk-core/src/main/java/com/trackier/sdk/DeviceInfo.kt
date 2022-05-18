package com.trackier.sdk

import android.content.ContentResolver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class DeviceInfo(
    val osName: String = "android",
    val name: String = Build.DEVICE,
    val buildName: String = Build.ID,
    val osVersion: String = VERSION.RELEASE,
    val manufacturer: String = Build.MANUFACTURER,
    val hardwareName: String = Build.DISPLAY,
    val model: String = Build.MODEL,
    val apiLevel: String = "${VERSION.SDK_INT}",
    val brand: String = Build.BRAND
) {
    lateinit var packageName: String
    var appVersion: String? = null
    var appInstallTime: String? = null
    var appUpdateTime: String? = null
    val sdkVersion = Constants.SDK_VERSION

    var language: String? = null
    var country: String? = null
    val timezone = TimeZone.getDefault().id

    val board = Build.BOARD
    val bootloader = Build.BOOTLOADER
    val cpuCores = Runtime.getRuntime().availableProcessors()
    val radioVersion = Build.getRadioVersion()

    var deviceType: String? = null
    var screenSize: String? = null
    var screenFormat: String? = null
    var screenDensity: String? = null
    lateinit var displayWidth: String
    lateinit var displayHeight: String

    var connectionType: String? = null
    var countryCode: String? = null
    var carrier: String? = null
    var macMd5: String? = null  // removed
    var androidId: String? = null   // removed
    var isEmulator = false
    var locale: String? = ""

    var fbAttributionId: String? = null

    var batteryLevel: String? = null
    var systemVolume: String? = null
    var deviceOrientation: String? = null
    var deviceBootTime: String? = null
    var availableInternalStorage: String? = null
    var cpuDetail: String? = null
    var deviceChargingStatus: String? = null

    companion object {
        fun init(deviceInfo: DeviceInfo, context: Context) {
            deviceInfo.packageName = context.packageName
            deviceInfo.appVersion = appVersion(context)
            deviceInfo.appInstallTime = appInstallTime(context)
            deviceInfo.appUpdateTime = getAppUpdateTime(context)
            val config = context.resources.configuration
            val locale = Util.getLocale(config)
            deviceInfo.language = locale?.language
            deviceInfo.country = locale?.country

            val screenLayout = context.resources.configuration.screenLayout
            val displayMetrics = context.resources.displayMetrics
            deviceInfo.deviceType = getDeviceType(screenLayout)
            deviceInfo.screenSize = screenSize(screenLayout)
            deviceInfo.screenFormat = screenFormat(screenLayout)
            deviceInfo.screenDensity = screenDensity(displayMetrics.densityDpi)
            // deviceInfo.screenDensityNumber = displayMetrics.densityDpi
            deviceInfo.displayWidth = "${displayMetrics.widthPixels}"
            deviceInfo.displayHeight = "${displayMetrics.heightPixels}"

            deviceInfo.connectionType = getConnectionType(context)
            setCarrierInfo(deviceInfo, context)
            deviceInfo.isEmulator = checkIsEmulator()

            deviceInfo.fbAttributionId = getFBAttributionId(context.contentResolver)
            deviceInfo.locale = Locale.getDefault().toString()


            if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                deviceInfo.batteryLevel = getBatteryLevel(context)
            }
            deviceInfo.systemVolume = getSystemVolume(context)
            deviceInfo.deviceOrientation = getDeviceOrientation(context)
            deviceInfo.deviceBootTime = getDeviceBootTime();
            if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                deviceInfo.availableInternalStorage = getAvailableInternalStorage()
            }
            deviceInfo.cpuDetail = getCPUDetails()
            deviceInfo.deviceChargingStatus = getDeviceChargingStatus(context)
        }

        fun getDeviceBootTime(): String {
            var bootTime =
                java.lang.System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime();
            //   val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
            val resultDate = Date(bootTime)
            return Util.dateFormatter.format(resultDate)
            //return sdf.format(resultDate)
        }

        var strOrientation: String? = null
        fun getDeviceOrientation(context: Context): String {
            var orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                strOrientation = "portrait"
            else strOrientation = "landscape"
            return "$strOrientation"
        }

        fun getDeviceChargingStatus(context: Context): String {
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context.registerReceiver(null, ifilter)
                }
            // isCharging if true indicates charging is ongoing and vice-versa
            val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                    || status == BatteryManager.BATTERY_STATUS_FULL
            var chargingStatus: String? = null
            chargingStatus = if (isCharging)
                "Device is charging"
            else
                "Device is not charging"
            return chargingStatus
        }


        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        fun getAvailableInternalStorage(): String? {
            val iPath: File = Environment.getDataDirectory()
            val iStat = StatFs(iPath.path)
            val iBlockSize = iStat.blockSizeLong
            val iAvailableBlocks = iStat.availableBlocksLong
            val iTotalBlocks = iStat.blockCountLong
            val iAvailableSpace = formatSize(iAvailableBlocks * iBlockSize)
            return iAvailableSpace

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
            return "$resultBuffer"
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getBatteryLevel(context: Context): String {
            val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
            val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            return "$batLevel"
            //  return batLevel as String

        }

        fun getSystemVolume(context: Context): String {
            val am = context.getSystemService(AUDIO_SERVICE) as AudioManager
            val volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC)
            return "$volume_level"
        }

        private fun appVersion(context: Context): String? {
            return try {
                val pm = context.packageManager
                val info = pm.getPackageInfo(context.packageName, 0)
                info.versionName
            } catch (ex: Exception) {
                null
            }
        }

        private fun getDeviceType(screenLayout: Int): String? {
            return when (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_SMALL, Configuration.SCREENLAYOUT_SIZE_NORMAL -> "phone"
                Configuration.SCREENLAYOUT_SIZE_LARGE, 4 -> "tablet"
                else -> null
            }
        }

        private fun screenSize(screenLayout: Int): String? {
            return when (screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_SMALL -> "small"
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> "normal"
                Configuration.SCREENLAYOUT_SIZE_LARGE -> "large"
                4 -> "xlarge"
                else -> null
            }
        }

        private fun screenFormat(screenLayout: Int): String? {
            return when (screenLayout and Configuration.SCREENLAYOUT_LONG_MASK) {
                Configuration.SCREENLAYOUT_LONG_YES -> "long"
                Configuration.SCREENLAYOUT_LONG_NO -> "normal"
                else -> null
            }
        }

        private fun screenDensity(density: Int): String? {
            val low = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_LOW) / 2;
            val high = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_HIGH) / 2;
            return when {
                density < low -> "low"
                density > high -> "high"
                density == 0 -> null
                else -> "medium"
            }
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

        private fun screenDensityNumer(density: Int): String? {
            val low = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_LOW) / 2;
            val high = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_HIGH) / 2;
            return when {
                density < low -> "low"
                density > high -> "high"
                density == 0 -> null
                else -> "medium"
            }
        }

        private fun appInstallTime(context: Context): String? {
            return try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
                )
                Util.dateFormatter.format(Date(packageInfo.firstInstallTime))
            } catch (ex: Exception) {
                null
            }
        }

        private fun getAppUpdateTime(context: Context): String? {
            return try {
                val pm = context.packageManager
                val packageInfo = pm.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
                )
                Util.dateFormatter.format(Date(packageInfo.lastUpdateTime))
            } catch (ex: Exception) {
                null
            }
        }

        private fun getConnectionType(context: Context): String? {
            return try {
                val conn =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                if (VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val mwifi = conn?.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    return if (mwifi?.isConnected == true) "wifi" else "mobile"
                }
                val networks = conn?.allNetworks
                if (networks?.isNullOrEmpty() == true) {
                    return null
                }
                val n = conn.activeNetwork
                var connType: String? = null
                for (item: Network in networks) {
                    val result = conn.getNetworkCapabilities(item)
                    if (item == n && result?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
                        connType = "mobile"
                        break
                    }
                    if (item == n && result?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                        connType = "wifi"
                        break
                    }
                }
                connType
            } catch (ex: Exception) {
                null
            }
        }

        private fun setCarrierInfo(deviceInfo: DeviceInfo, context: Context) {
            try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                deviceInfo.countryCode = tm?.networkCountryIso?.toUpperCase()
                deviceInfo.carrier = tm?.networkOperatorName
            } catch (ex: Exception) {
            }
        }

        private fun checkIsEmulator(): Boolean {
            return (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || "google_sdk" == Build.PRODUCT)
        }

        suspend fun getGAID(context: Context): Pair<String?, Boolean> {
            try {
                val weakContext = WeakReference(context)
                val adIdMethod =
                    Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient")
                        .getDeclaredMethod("getAdvertisingIdInfo", Context::class.java)
                val adInfo = adIdMethod.invoke(null, *arrayOf<Any?>(weakContext.get()))

                val getIdMethod =
                    Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient\$Info")
                        .getDeclaredMethod("getId")
                var deviceId = getIdMethod.invoke(adInfo) as String?
                // Don't save advertising id if it's all zeroes
                if (deviceId.equals(Constants.UUID_EMPTY)) {
                    deviceId = null
                }

                val getLATMethod =
                    Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient\$Info")
                        .getDeclaredMethod("isLimitAdTrackingEnabled")
                val isLAT = getLATMethod.invoke(adInfo) as Boolean
                return Pair(deviceId, isLAT)
            } catch (ex: Exception) {
                return Pair(null, false)
            }
        }

        fun getFBAttributionId(contentResolver: ContentResolver): String {
            try {
                val attributionIdContentUri =
                    Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider")
                val attributionIdColumnName = "aid"

                if (contentResolver == null) return ""

                val projection = arrayOf(attributionIdColumnName)
                val c: Cursor? =
                    contentResolver.query(attributionIdContentUri, projection, null, null, null)
                if (c == null || !c.moveToFirst()) {
                    return ""
                }
                val attributionId: String = c.getString(c.getColumnIndex(attributionIdColumnName))
                c.close()

                return attributionId
            } catch (e: Exception) {
                return ""
            }
        }
    }
}


