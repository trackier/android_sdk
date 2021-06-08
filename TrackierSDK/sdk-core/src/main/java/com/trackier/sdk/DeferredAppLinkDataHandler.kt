package com.trackier.sdk

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class DeferredAppLinkDataHandler {
    companion object {
        private val NATIVE_URL_KEY = "com.facebook.platform.APPLINK_NATIVE_URL"
        fun fetchDeferredAppLinkData(
            context: Context, callback: AppLinkFetchEvents?): Boolean? {
            var isRequestSucceeded = true
            try {
                val FacebookSdkClass = Class.forName("com.facebook.FacebookSdk")
                val initSdkMethod: Method = FacebookSdkClass.getMethod("sdkInitialize", Context::class.java)
                initSdkMethod.invoke(null, context)
                val AppLinkDataClass = Class.forName("com.facebook.applinks.AppLinkData")
                val AppLinkDataCompletionHandlerClass = Class.forName("com.facebook.applinks.AppLinkData\$CompletionHandler")
                val fetchDeferredAppLinkDataMethod: Method = AppLinkDataClass.getMethod("fetchDeferredAppLinkData", Context::class.java, String::class.java, AppLinkDataCompletionHandlerClass)
                val ALDataCompletionHandler = object : InvocationHandler {
                    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>): Any? {
                        if (method.getName().equals("onDeferredAppLinkDataFetched") && args[0] != null) {
                            var appLinkUrl: String? = null
                            val appLinkDataClass = AppLinkDataClass.cast(args[0])
                            val getArgumentBundleMethod: Method = AppLinkDataClass.getMethod("getArgumentBundle")
                            val appLinkDataBundle = Bundle::class.java.cast(getArgumentBundleMethod.invoke(appLinkDataClass))
                            if (appLinkDataBundle != null) {
                                appLinkUrl = appLinkDataBundle.getString(NATIVE_URL_KEY)
                            }
                            callback?.onAppLinkFetchFinished(appLinkUrl)
                        } else {
                            callback?.onAppLinkFetchFinished(null)
                        }
                        return null
                    }
                }
                val completionListenerInterface: Any = Proxy.newProxyInstance(AppLinkDataCompletionHandlerClass.classLoader, arrayOf(AppLinkDataCompletionHandlerClass), ALDataCompletionHandler)
                val fbAppID: String = context.getString(context.getResources().getIdentifier("facebook_app_id", "string", context.getPackageName()))
                if (TextUtils.isEmpty(fbAppID)) {
                    isRequestSucceeded = false
                } else {
                    fetchDeferredAppLinkDataMethod.invoke(null, context, fbAppID, completionListenerInterface)
                }
            } catch (ex: Exception) {
                isRequestSucceeded = false
            }
            return isRequestSucceeded
        }

    }
    interface AppLinkFetchEvents {
        fun onAppLinkFetchFinished(nativeAppLinkUrl: String?)
    }
}