package com.cloudstuff.trackiersdk

import android.net.Uri
import android.util.Log
import java.net.URLDecoder

data class RefererDetails(
        val url: String,
        val clickTime: String,
        val installTime: String,
) {
    val isOrganic: Boolean get() = clickId.isEmpty()

    var clickId :String = ""

        get() {
            var clickIdNew:String =""
            val afterDecode = URLDecoder.decode(url, "UTF-8").toString()
            Log.d("prak24 afterDecode", ":$afterDecode")
            val params = getQueryKeyValueMap(afterDecode);
            if (params != null) {
                clickIdNew= params["tr_clickid"].toString()
            }
            else clickIdNew= ""

            Log.d("prak24 clickId new", params["tr_clickid"].toString())
           // params.map { (key, value) -> println("prak24 map data $key : $value") }

            return clickIdNew
        }

    fun getQueryKeyValueMap(url: String): Map<String, String> {

        val map = url.split("?").associate {
            val (left, right) = url.split("=")
            left to right
        }
        return map
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }


}