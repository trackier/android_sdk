package com.trackier.sdk


import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import com.trackier.sdk.Factory.logger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.Method
import java.math.BigDecimal
import java.net.URLDecoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object Util {
    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    val dateFormatter = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US)
    init {
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getMapStringVal(data: Map<String, String>, key: String): String {
        return if (data.containsKey(key)) {
            data[key].toString()
        } else {
            ""
        }
    }

    fun getTimeInUnix(date: String): String {
        try {
            val sdf = SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.US)
            val dateObj: Date = sdf.parse(date)
            val time = dateObj.time.toDouble()
            val inUnix: Double = (time / 1000)

            return String.format("%.6f", BigDecimal(inUnix))
        } catch (e: Exception) {
            return  ""
        }
    }

    fun createSignature(data: String, key: String): String {
        val sha256Hmac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        sha256Hmac.init(secretKey)

        return printHexBinary(sha256Hmac.doFinal(data.toByteArray()))
    }
    

    fun getYear(date: String): Int {
        try {
            val dateProvided = dateFormatter.parse(date)
            val cal = Calendar.getInstance()
            cal.time = dateProvided
            return cal[Calendar.YEAR]
        } catch (e: Exception) {
            return 0
        }
    }

    fun getQueryParams(uri: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val decodedUrl = URLDecoder.decode(uri, "UTF-8").toString()
        try {
            val urlParts = decodedUrl.split("?")
            var query = decodedUrl
            if (urlParts.size == 1) {
                query = urlParts[0]
            } else if (urlParts.size >= 2) {
                query = urlParts[1]
            }

            val params = query.split("&")
            for (param in params) {
                val modifiedParam = param.split("#")
                val parts = modifiedParam[0].split("=")
                if (parts.size == 2) {
                    val name = parts[0]
                    val value = parts[1]
                    map[name] = value
                }
            }
        } catch (e: Exception) {}
        return map
    }

    fun getLocale(config: Configuration): Locale? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = config.locales
            return localeList.get(0)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return config.locale
        }
        return null
    }

    fun getSharedPref(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
                Constants.SHARED_PREF_NAME,
                Context.MODE_PRIVATE
        )
    }

    fun getSharedPrefString(context: Context, key: String): String {
        val prefs = getSharedPref(context)
        try {
            val data = prefs.getString(key, "")
            if (data?.isBlank() == false) {
                return data
            }
        } catch (ex: Exception) {}
        return ""
    }

    fun setSharedPrefString(context: Context, key: String, value: String) {
        try {
            val prefs = getSharedPref(context)
            prefs.edit().putString(key, value).apply()
        } catch (ex: Exception) {}
    }

    fun delSharedPrefKey(context: Context, key: String) {
        try {
            val prefs = getSharedPref(context)
            prefs.edit().remove(key).apply()
        } catch (ex: Exception) {}
    }

    fun sha1(input: String) = hashString("SHA-1", input)
    fun md5(input: String) = hashString("MD5", input)

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest.getInstance(type).digest(input.toByteArray())
        return printHexBinary(bytes)
    }

    private fun printHexBinary(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(HEX_CHARS[i shr 4 and 0xF])
            r.append(HEX_CHARS[i and 0xF])
        }
        return r.toString()
    }

    fun loadAddress(interfaceName: String): String? {
        try {
            val filePath = "/sys/class/net/$interfaceName/address"
            val fileData = StringBuilder(1000)
            val reader = BufferedReader(FileReader(filePath), 1024)
            val buf = CharArray(1024)
            var numRead: Int

            var readData: String
            while (reader.read(buf).also { numRead = it } != -1) {
                readData = String(buf, 0, numRead)
                fileData.append(readData)
            }

            reader.close()
            return fileData.toString()
        } catch (ex: Exception) {
            return null
        }
    }
    
    fun campaignData(context: Context, res: ResponseData) {
        res.ad?.let { setSharedPrefString(context, Constants.SHARED_PREF_AD, it) }
        res.adId?.let { setSharedPrefString(context, Constants.SHARED_PREF_ADID, it) }
        res.camp?.let { setSharedPrefString(context, Constants.SHARED_PREF_CAMPAIGN, it) }
        res.campId?.let { setSharedPrefString(context, Constants.SHARED_PREF_CAMPAIGNID, it) }
        res.adSet?.let { setSharedPrefString(context, Constants.SHARED_PREF_ADSET, it) }
        res.adSetId?.let { setSharedPrefString(context, Constants.SHARED_PREF_ADSETID, it) }
        res.channel?.let { setSharedPrefString(context, Constants.SHARED_PREF_CHANNEL, it) }
        res.p1?.let { setSharedPrefString(context, Constants.SHARED_PREF_P1, it) }
        res.p2?.let { setSharedPrefString(context, Constants.SHARED_PREF_P2, it) }
        res.p3?.let { setSharedPrefString(context, Constants.SHARED_PREF_P3, it) }
        res.p4?.let { setSharedPrefString(context, Constants.SHARED_PREF_P4, it) }
        res.p5?.let { setSharedPrefString(context, Constants.SHARED_PREF_P5, it) }
        res.clickId?.let { setSharedPrefString(context, Constants.SHARED_PREF_CLICKID, it) }
        res.dlv?.let { setSharedPrefString(context, Constants.SHARED_PREF_DLV, it) }
        res.pid?.let { setSharedPrefString(context, Constants.SHARED_PREF_PID, it) }
        res.partner?.let { setSharedPrefString(context, Constants.SHARED_PREF_PARTNER, it) }
        setSharedPrefString(context, Constants.SHARED_PREF_ISRETARGETING, res.isRetargeting.toString())
    }
    
    private fun getSysPropertyPath(path: String): String? {
        var value: String? = ""
        try {
            value = Class.forName(Constants.ANDROID_SYSTEM_PROPERTIES_CLASS)
                .getMethod("get", String::class.java)
                .invoke(null, path) as String
            logger.info("Get system property $value")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }
    
    private fun getPreInstallData(path: String): String {
        var bufferedReader: BufferedReader? = null
        var line: String? = ""
        try {
            if (path.isNotEmpty()) {
                bufferedReader = BufferedReader(FileReader(File(path)))
            }
            val jsonFile = StringBuilder()
            while (bufferedReader!!.readLine().also { line = it } != null) {
                jsonFile.append(line)
                return jsonFile.toString()
            }
        } catch (e: Exception) {
            logger.info("Failed to read System Property " + e.message)
        }
        return ""
    }
    
    private fun getPreInstallDataFromDefaultPathWay(): String {
        try {
            if (getPreInstallData(Constants.PRE_DEFINED_PATH1).isNotEmpty()) {
                return Constants.PRE_DEFINED_PATH1
            } else if (getPreInstallData(Constants.PRE_DEFINED_PATH2).isNotEmpty()) {
                return Constants.PRE_DEFINED_PATH2
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
    
    private fun getPreInstallDataFromPreInstallFilePath(context: Context): String {
        var getData = ""
        try {
            getData =
                getPreInstallData(getSysPropertyPath(Constants.SYSTEM_PROPERTIES_PRE_INSTALL_PATH).toString())
            if (getData.isNotEmpty()) {
                return getData
            }
            getData = getPreInstallData(
                getPreInstalllManifestData(
                    context,
                    Constants.PRE_INSTALL_MANIFEST_KEY
                )
            )
            if (getData.isNotEmpty()) {
                return getData
            }
            getData = getPreInstallData(getPreInstallDataFromDefaultPathWay())
            if (getData.isNotEmpty()) {
                return getData
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return getData
    }

    private fun getApplicationInfo(context: Context): ApplicationInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return context.packageManager.getApplicationInfo(context.packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        }
        return context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
    }
    
    private fun getPreInstalllManifestData(context: Context, key: String): String {
        var tempValue = ""
        try {
            if (key == Constants.PRE_INSTALL_MANIFEST_NAME){
                val ai = getApplicationInfo(context)
                val bundle = ai.metaData
                tempValue = bundle.getString(Constants.PRE_INSTALL_MANIFEST_NAME).toString()

            } else if (key == (Constants.PRE_INSTALL_MANIFEST_KEY)) {
                val ai = getApplicationInfo(context)
                val bundle = ai.metaData
                tempValue = bundle.getString(Constants.PRE_INSTALL_MANIFEST_KEY).toString()
            }
//            logger.info( "getManifestData \$propertyName = $tempValue")
        } catch (e: NameNotFoundException) {
            logger.info( "getManifestData NameNotFound = " + e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            logger.info( "getManifestData Exception = " + e.message)
            e.printStackTrace()
        }
        return tempValue
    }
    
    private fun isXioamiPreInstallApp(pkgName: String?): Boolean {
        try {
            val miui = Class.forName("miui.os.MiuiInit")
            val method: Method = miui.getMethod("isPreinstalledPAIPackage", String::class.java)
            return method.invoke(null, pkgName) as Boolean
        } catch (e: Exception) {
            logger.info( "isXioamiPreInstallApp Exception = " + e.message)
        }
        return false
    }
    
    private fun applicationIsSystemApp(mContext: Context): Boolean {
        try {
            val applicationInfo = mContext.packageManager.getApplicationInfo(mContext.packageName, 0)
            val appLocation = applicationInfo.publicSourceDir
            Log.d("xxxx", "applicationIsSystemApp $appLocation")
            if (appLocation != null && appLocation.startsWith(Constants.SYSTEM_PATH)) {
                return true
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace() // TODO Can handle as your logic
        }
        return false
    }
    
    private fun applicationIsSystemAppFlagSystem(mContext: Context): Boolean {
        try {
            val applicationInfo = mContext.packageManager.getApplicationInfo(mContext.packageName, 0)
            // FLAG_SYSTEM is only set to system applications,
            // this will work even if application is installed in external storage
            // Check if package is system app
            if (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                return true
            }
        } catch (e: NameNotFoundException) {
            e.printStackTrace() // TODO Can handle as your logic
        }
        return false
    }
    
    private fun getDeviceActivationDate(): String {
        val manufacturer = Build.MANUFACTURER
        var buildDate = ""
        if (manufacturer.equals(Constants.DEVICE_BRAND)) {
            buildDate = getSysPropertyPath(Constants.SYSTEM_PROPERTIES_DEVICE_BUILD_XIAOMI).toString()
        } else {
            buildDate = getSysPropertyPath(Constants.SYSTEM_PROPERTIES_DEVICE_BUILD_ANDROID).toString()
        }
        if (buildDate.isNotEmpty()) {
            try {
                val activationTimestamp = buildDate.toLong() * 1000L
                val activationDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(activationTimestamp))
                println("Activation Date: $activationDate")
                logger.info("Activation Date = $activationDate")
                return activationDate
            } catch (e: NumberFormatException) {
            }
        } else {
            logger.info("Activation date is empty")
        }
        return ""
    }
    
    private fun getBootTime(): String {
        var activationDate = ""
        try {
            val bootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime()
            val deviceActivationTime = bootTime - SystemClock.uptimeMillis()
            activationDate = Date(deviceActivationTime).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activationDate
    }
    
    private fun osUpdateTime(): String {
        val systemDir = Environment.getRootDirectory()
        val creationTime = systemDir.lastModified()
        return Date(creationTime).toString()
    }
    
    private fun isPreInstallApp(context: Context): MutableMap<String, Boolean> {
        val params = mutableMapOf<String, Boolean>()
        params["isXioamiPreInstallApp"] = isXioamiPreInstallApp(context.packageName)
        params["applicationIsSystemApp"] = applicationIsSystemApp(context)
        params["applicationIsSystemAppFlagSystem"] = applicationIsSystemAppFlagSystem(context)
        return params
    }
    
    private fun preinstallAttribution(context: Context): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        params["preInstallAttribution_Pid"] = getSharedPrefString(context, Constants.PRE_INSTALL_ATTRIBUTION_PID)
        params["preInstallAttribution_Camapign"] = getSharedPrefString(context, Constants.PRE_INSTALL_ATTRIBUTION_CAMPAIGN)
        params["preInstallAttribution_CamapignId"] = getSharedPrefString(context, Constants.PRE_INSTALL_ATTRIBUTION_CAMPAIGNID)
        return params
    }
    
    fun getPreLoadAndPAIdata(context: Context): MutableMap<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["preInstallDataFromPreInstallFilePath"] = getPreInstallDataFromPreInstallFilePath(context)
        params["preInstallManifest"] = getPreInstalllManifestData(context, Constants.PRE_INSTALL_MANIFEST_NAME)
        params["preInstallAttribution"] = preinstallAttribution(context)
        params["isPreInstallApp"] = isPreInstallApp(context)
        params["googleReferrer"] = getSharedPrefString(context, Constants.SHARED_PREF_INSTALL_URL)
        params["miuiReferrer"] = getSharedPrefString(context, Constants.SHARED_PREF_XIAOMI_INSTALL_URL)
        params["deviceActivationDate"] = getDeviceActivationDate()
        params["deviceBootTime"] = getBootTime()
        params["osUpdateTime"] = osUpdateTime()
        return params
    }
}