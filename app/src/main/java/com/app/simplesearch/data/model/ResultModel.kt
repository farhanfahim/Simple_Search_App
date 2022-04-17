package com.app.simplesearch

import com.app.simplesearch.data.model.Items
import com.google.gson.annotations.SerializedName


data class ResultModel (

  @SerializedName("total_count"        )
  var totalCount        : Int?             = null,
  @SerializedName("incomplete_results" )
  var incompleteResults : Boolean?         = null,
  @SerializedName("items"              )
  var items             : ArrayList<Items> = arrayListOf()

)