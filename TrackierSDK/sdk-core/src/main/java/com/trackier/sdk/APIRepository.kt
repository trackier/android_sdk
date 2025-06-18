package com.trackier.sdk

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trackier.sdk.dynamic_link.DynamicLinkResponse
import com.trackier.sdk.dynamic_link.ErrorResponse
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object APIRepository {
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val trackierApi: APIService by lazy {
        val region = Factory.getConfig().getRegion()
        val baseUrl = if (region.isNotEmpty()) {
            "${Constants.SCHEME}$region-${Constants.BASE_URL}"
        } else {
            "${Constants.SCHEME}${Constants.BASE_URL}"
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()

        retrofit.create(APIService::class.java)
    }
    
    private val trackierDeeplinksApi: APIService by lazy {
        val region = Factory.getConfig().getRegion()
        val baseUrl = if (region.isNotEmpty()) {
            "${Constants.SCHEME}$region-${Constants.BASE_URL_DL}"
        } else {
            "${Constants.SCHEME}${Constants.BASE_URL_DL}"
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()

        retrofit.create(APIService::class.java)


//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL_DL)
//            .addConverterFactory(MoshiConverterFactory.create().asLenient())
//            .client(client)
//            .build()
//
//        retrofit.create(APIService::class.java)
    }

    private val trackierDynamiclinkApi: APIService by lazy {
        val region = Factory.getConfig().getRegion()
        val baseUrl = if (region.isNotEmpty()) {
            "${Constants.SCHEME}$region-${Constants.BASE_URL_DYNAMIC_LINK}"
        } else {
            "${Constants.SCHEME}${Constants.BASE_URL_DYNAMIC_LINK}"
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()

        retrofit.create(APIService::class.java)

//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL_DYNAMIC_LINK)
//            .addConverterFactory(MoshiConverterFactory.create().asLenient())
//            .client(client)
//            .build()

//        retrofit.create(APIService::class.java)
    }

    private suspend fun sendInstall(body: MutableMap<String, Any>): ResponseData {
        val logger = Factory.logger
        logger.info("Install body is: $body")
        return trackierApi.sendInstallData(body)
    }

    private suspend fun sendEvent(body: MutableMap<String, Any>): ResponseData {
        val logger = Factory.logger
        logger.info("Event body is: $body")
        return trackierApi.sendEventData(body)
    }

    private suspend fun sendSession(body: MutableMap<String, Any>): ResponseData {
        val logger = Factory.logger
        logger.info("Session body is: $body")
        return trackierApi.sendSessionData(body)
    }
    
    private suspend fun sendDeeplinks(body: MutableMap<String, Any>): ResponseData {
        val logger = Factory.logger
        logger.info("Deeplink body is: $body")
        return trackierDeeplinksApi.sendDeeplinksData(body)
    }

    suspend fun sendDynamiclinks(body: MutableMap<String, Any>): DynamicLinkResponse {
        // Convert request body to JSON string for logging
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Map::class.java)
        val requestBodyJson = jsonAdapter.toJson(body)
        Log.i("TrackierSDK", "Dynamic Link Body : $requestBodyJson")
        return try {
            trackierDynamiclinkApi.sendDynamicLinkData(body)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(DynamicLinkResponse::class.java)
            Log.e("TrackierSDK", "Error Response: $errorBody")
            try {
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it) }
                errorResponse ?: DynamicLinkResponse(
                    success = false,
                    message = "Failed to create link. HTTP ${e.code()}",
                    error = ErrorResponse(
                        statusCode = e.code(),
                        errorCode = "UNKNOWN_ERROR",
                        codeMsg = "Unknown error occurred",
                        message = "Could not parse error response"
                    )
                )
            } catch (jsonException: Exception) {
                Log.e("TrackierSDK", "JSON Parsing Error: ${jsonException.message}")
                DynamicLinkResponse(
                    success = false,
                    message = "JSON parsing failed: ${jsonException.message}",
                    error = ErrorResponse(
                        statusCode = e.code(),
                        errorCode = "JSON_PARSE_ERROR",
                        codeMsg = "Failed to parse error response",
                        message = jsonException.localizedMessage ?: "Unknown JSON error"
                    )
                )
            }
        } catch (e: Exception) {
            Log.d("TrackierSDK", "Exception: ${e.message}")
            DynamicLinkResponse(
                success = false,
                message = "Failed to create link. Exception: ${e.message}",
                error = ErrorResponse(
                    statusCode = 500,
                    errorCode = "EXCEPTION",
                    codeMsg = "Internal error",
                    message = e.localizedMessage ?: "Unknown exception"
                )
            )
        }
    }

    // Expose a public wrapper for sendDeeplinks without changing its visibility
    suspend fun publicResolveDeeplinks(body: MutableMap<String, Any>): ResponseData {
        return sendDeeplinks(body)
    }

    suspend fun doWork(workRequest: TrackierWorkRequest): ResponseData? {
        return when(workRequest.kind) {
            TrackierWorkRequest.KIND_INSTALL -> sendInstall(workRequest.getData())
            TrackierWorkRequest.KIND_EVENT -> sendEvent(workRequest.getEventData())
            TrackierWorkRequest.KIND_UNKNOWN -> null
            TrackierWorkRequest.KIND_SESSION_TRACK -> sendSession(workRequest.getSessionData())
            TrackierWorkRequest.KIND_DEEPLINKS -> sendDeeplinks(workRequest.getDeeplinksData())
            else -> null
        }
    }

    suspend fun processWork(workRequest: TrackierWorkRequest): ResponseData? {
        return try {
            when(workRequest.kind) {
                TrackierWorkRequest.KIND_INSTALL -> sendInstall(workRequest.getData())
                TrackierWorkRequest.KIND_EVENT -> sendEvent(workRequest.getEventData())
                TrackierWorkRequest.KIND_UNKNOWN -> null
                TrackierWorkRequest.KIND_SESSION_TRACK -> sendSession(workRequest.getSessionData())
                TrackierWorkRequest.KIND_DEEPLINKS -> sendDeeplinks(workRequest.getDeeplinksData())
                else -> null
            }
        } catch (ex: Exception) {
            null
        }
    }
}
