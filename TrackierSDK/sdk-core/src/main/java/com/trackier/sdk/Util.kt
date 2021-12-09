package com.trackier.sdk

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.io.BufferedReader
import java.io.FileReader
import java.math.BigDecimal
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

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

    fun getQueryParams(query: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        try {
            val params = query.split("&")
            for (param in params) {
                val parts = param.split("=")
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
}