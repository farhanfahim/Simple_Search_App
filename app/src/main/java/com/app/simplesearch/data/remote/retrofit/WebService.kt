package com.app.simplesearch.data.remote.retrofit

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import retrofit2.http.*

interface WebService {

    @GET
    suspend fun get(
        @Url endpoint: String
    ): retrofit2.Response<JsonElement>




}