package com.trackier.sdk

import okhttp3.OkHttpClient
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
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()

        retrofit.create(APIService::class.java)
    }
    
    private val trackierDeeplinksApi: APIService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_DL)
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()
        
        retrofit.create(APIService::class.java)
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
    
    private suspend fun sendDeeplinks(body: MutableMap<String, Any>): DeeplinkResponse {
        val logger = Factory.logger
        logger.info("Deeplinks body is: $body")
        return trackierDeeplinksApi.sendDeeplinksData(body)
    }

    suspend fun doWork(workRequest: TrackierWorkRequest): ResponseData? {
        return when(workRequest.kind) {
            TrackierWorkRequest.KIND_INSTALL -> sendInstall(workRequest.getData())
            TrackierWorkRequest.KIND_EVENT -> sendEvent(workRequest.getEventData())
            TrackierWorkRequest.KIND_UNKNOWN -> null
            TrackierWorkRequest.KIND_SESSION_TRACK -> sendSession(workRequest.getSessionData())
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
                else -> null
            }
        } catch (ex: Exception) {
            null
        }
    }
    
    suspend fun doWorkDeeplinks(workRequest: TrackierWorkRequest): DeeplinkResponse? {
        return when(workRequest.kind) {
            TrackierWorkRequest.KIND_DEEPLINKS -> sendDeeplinks(workRequest.getDeeplinksData())
            else -> null
        }
    }
    
    suspend fun processWorkDeeplinks(workRequest: TrackierWorkRequest): DeeplinkResponse? {
        return try {
            when(workRequest.kind) {
                TrackierWorkRequest.KIND_DEEPLINKS -> sendDeeplinks(workRequest.getDeeplinksData())
                else -> null
            }
        } catch (ex: Exception) {
            null
        }
    }

}
