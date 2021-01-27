package com.trackier.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Build.VERSION
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import androidx.annotation.Keep
import java.lang.ref.WeakReference
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
    var macMd5: String? = null
    var androidId: String? = null
    var isEmulator = false

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
            deviceInfo.displayWidth = "${displayMetrics.widthPixels}"
            deviceInfo.displayHeight = "${displayMetrics.heightPixels}"

            deviceInfo.connectionType = getConnectionType(context)
            setCarrierInfo(deviceInfo, context)
            deviceInfo.macMd5 = getMacAddress(context)
            deviceInfo.isEmulator = checkIsEmulator()
            deviceInfo.androidId = getAndroidID(context)
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
                val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
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

        @SuppressLint("HardwareIds")
        private fun getMacAddress(context: Context): String? {
            return try {
                // for devices above android v6
                val wlanAddress = Util.loadAddress("wlan0")
                if (wlanAddress?.isBlank() == false) {
                    return Util.md5(wlanAddress.trim().toUpperCase(Locale.US))
                }
                // this works below android v6
                val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
                val info = manager?.connectionInfo
                val mac = info?.macAddress?.trim()?.toUpperCase(Locale.US)
                if (mac == "02:00:00:00:00:00" || mac?.length == 0) null else Util.md5(mac!!)
            } catch (ex: Exception) {
                null
            }
        }

        @SuppressLint("HardwareIds")
        private fun getAndroidID(context: Context): String {
            return Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)
        }

        private fun setCarrierInfo(deviceInfo: DeviceInfo, context: Context) {
            try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                deviceInfo.countryCode = tm?.networkCountryIso
                deviceInfo.carrier = tm?.networkOperatorName
            } catch (ex: Exception) {}
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
    }
}
