package com.app.simplesearch.data.remote.retrofit

import com.google.gson.JsonElement
import com.app.simplesearch.data.model.APIError

sealed class Result {
    class Success(val response: JsonElement) : Result()
    class Error(val error: String) : Result()
    class Exception(val exception: java.lang.Exception) : Result()
    class RetrofitError(val apiError: APIError?) : Result()

}