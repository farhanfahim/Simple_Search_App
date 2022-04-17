package com.app.simplesearch.ui.events

import com.app.simplesearch.ResultModel


sealed class ResultEvent {
    object StartLoading : ResultEvent()
    object StopLoading : ResultEvent()
    class OnResultData(val model: ResultModel?) : ResultEvent()
    class OnNoInternetAvailable(val noInternetAvailable: String?) : ResultEvent()
    class Exception(val exception: java.lang.Exception?) : ResultEvent()
    class Error(val error: String?) : ResultEvent()
}