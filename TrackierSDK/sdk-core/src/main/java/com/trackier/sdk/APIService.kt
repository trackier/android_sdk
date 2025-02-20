package com.trackier.sdk

import com.trackier.sdk.dynamic_link.DynamicLinkResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @POST("install")
    @Headers( "X-Client-SDK: ${Constants.SDK_VERSION}", "User-Agent: ${Constants.USER_AGENT}" )
    suspend fun sendInstallData(@Body data: MutableMap<String, Any>): ResponseData

    @POST("event")
    @Headers( "X-Client-SDK: ${Constants.SDK_VERSION}", "User-Agent: ${Constants.USER_AGENT}" )
    suspend fun sendEventData(@Body data: MutableMap<String, Any>): ResponseData


    @POST("session")
    @Headers( "X-Client-SDK: ${Constants.SDK_VERSION}", "User-Agent: ${Constants.USER_AGENT}" )
    suspend fun sendSessionData(@Body data: MutableMap<String, Any>): ResponseData
    
    @POST("resolver")
    @Headers( "X-Client-SDK: ${Constants.SDK_VERSION}", "User-Agent: ${Constants.USER_AGENT}" )
    suspend fun sendDeeplinksData(@Body data: MutableMap<String, Any>): ResponseData

    @POST("generation")
    @Headers("X-Client-SDK: ${Constants.SDK_VERSION}", "User-Agent: ${Constants.USER_AGENT}")
    suspend fun sendDynamicLinkData(@Body data: MutableMap<String, Any>): DynamicLinkResponse
}