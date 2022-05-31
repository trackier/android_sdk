package com.trackier.sdk

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.Inet4Address
import java.net.NetworkInterface
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
    var orientation: String? = null
    var bootTime: String? = null
    var availableInternalStorage: String? = null
    var totalInternalStorage: String? = null
    var cpuDetails: String = ""
    var chargingStatus: String? = null
    var headPhoneStatus: Boolean? = null
    var installedApplication: String? = null
    var ipAddress: String? = null
    var availableMemory: String? = null
    var totalMemory: String? = null
    var screenDensityNumber: String? = null

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
            deviceInfo.screenDensityNumber = "${displayMetrics.densityDpi}"
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
            deviceInfo.orientation = getDeviceOrientation(context)
            deviceInfo.bootTime = getDeviceBootTime()
            if (VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val (totalStorage, availableStorage) = getTotalAvailableStorage()
                deviceInfo.availableInternalStorage = availableStorage
                deviceInfo.totalInternalStorage = totalStorage
            }
            deviceInfo.cpuDetails = getCPUDetails()
            deviceInfo.chargingStatus = getDeviceChargingStatus(context)
            if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deviceInfo.headPhoneStatus = getHeadphonesPlugged(context)
            }
            deviceInfo.installedApplication = getDeviceInstallApplication(context)
            deviceInfo.ipAddress = getIpv4HostAddress()
            val (totalMemory, availableMemory) = getTotalAvailableMemory(context)
            deviceInfo.totalMemory = totalMemory
            deviceInfo.availableMemory = availableMemory

        }

        private fun getDeviceBootTime(): String? {
            return try {
                val bootTime =
                    System.currentTimeMillis() - android.os.SystemClock.elapsedRealtime()
                val resultDate = Date(bootTime)
                Util.dateFormatter.format(resultDate)
            } catch (ex: Exception) {
                null
            }
        }

        private fun getIpv4HostAddress(): String {
            NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
                networkInterface.inetAddresses?.toList()?.find {
                    !it.isLoopbackAddress && it is Inet4Address
                }?.let { return it.hostAddress }
            }
            return ""
        }


        private fun getDeviceOrientation(context: Context): String {
            val orientation = context.resources.configuration.orientation
            val screenOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
                "portrait"
            else "landscape"
            return screenOrientation
        }

        private fun getTotalAvailableMemory(context: Context): Pair<String?, String?> {
            return try {
                val actManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val memInfo = ActivityManager.MemoryInfo()
                actManager.getMemoryInfo(memInfo)
                val totalMemory = memInfo.totalMem
                val availMemory = memInfo.availMem
                Pair("$totalMemory", "$availMemory")
            } catch (ex: Exception) {
                Pair(null, null)
            }
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        fun getTotalAvailableStorage(): Pair<String?, String?> {
            val iPath: File = Environment.getDataDirectory()
            val iAvailableSpace =
                StatFs(iPath.path).availableBlocksLong * StatFs(iPath.path).blockSizeLong
            val iTotalSpace = StatFs(iPath.path).blockCountLong * StatFs(iPath.path).blockSizeLong
            return Pair("$iAvailableSpace", "$iTotalSpace")
        }

        @SuppressLint("QueryPermissionsNeeded", "WrongConstant")
        fun getDeviceInstallApplication(context: Context): String? {
            return try {
                val pm = context.packageManager
                val apps = pm.getInstalledApplications(PackageManager.GET_GIDS)
                Util.md5(apps.joinToString())
            } catch (ex: Exception) {
                null
            }
        }

        private fun getDeviceChargingStatus(context: Context): String? {
            return try {
                val batteryStatus: Intent? =
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                        context.registerReceiver(null, ifilter)
                    }
                val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                        || status == BatteryManager.BATTERY_STATUS_FULL
                val chargingStatus: String = if (isCharging) "yes" else "no"
                chargingStatus
            } catch (ex: Exception) {
                null
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun getBatteryLevel(context: Context): String? {
            return try {
                val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
                val batLevel: Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                "$batLevel"
            } catch (ex: Exception) {
                null
            }

        }

        private fun getSystemVolume(context: Context): String? {
            return try {
                val am = context.getSystemService(AUDIO_SERVICE) as AudioManager
                val volumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC)
                "$volumeLevel"
            } catch (ex: Exception) {
                null
            }
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
            val low = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_LOW) / 2
            val high = (DisplayMetrics.DENSITY_MEDIUM + DisplayMetrics.DENSITY_HIGH) / 2
            return when {
                density < low -> "low"
                density > high -> "high"
                density == 0 -> null
                else -> "medium"
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun getHeadphonesPlugged(context: Context): Boolean? {
            return try {
                val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
                val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)
                val headPhonePlugged: Boolean
                for (deviceInfo in audioDevices) {
                    if (deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || deviceInfo.type == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                        headPhonePlugged = true
                        return headPhonePlugged
                    }
                }
                headPhonePlugged = false
                headPhonePlugged
            } catch (ex: Exception) {
                false
            }
        }

        private fun getCPUDetails(): String {
            val processBuilder: ProcessBuilder
            var cpuDetails = ""
            val DATA = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            val `is`: InputStream
            val process: Process
            val bArray = ByteArray(1024)
            try {
                processBuilder = ProcessBuilder(*DATA)
                process = processBuilder.start()
                `is` = process.inputStream
                while (`is`.read(bArray) !== -1) {
                    cpuDetails += String(bArray)
                }
                `is`.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return cpuDetails
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


