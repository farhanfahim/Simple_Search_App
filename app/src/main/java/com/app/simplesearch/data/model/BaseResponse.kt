package com.app.simplesearch.data.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class BaseResponse(

//    @JvmSuppressWildcards
    @field:SerializedName("data")
    val data: JsonElement? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
