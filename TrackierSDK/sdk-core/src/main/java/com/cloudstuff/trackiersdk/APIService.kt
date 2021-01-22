package com.cloudstuff.trackiersdk

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
}