package com.app.simplesearch.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.simplesearch.ResultModel
import com.app.simplesearch.data.remote.repo.Repository
import com.app.simplesearch.data.remote.retrofit.Result
import com.app.simplesearch.exception.ConnectivityException
import com.app.simplesearch.ui.events.BaseEvent
import com.app.simplesearch.ui.events.ResultEvent
import com.google.gson.Gson
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException


class HomeViewModel @ViewModelInject constructor(
    private val gson: Gson,
    private val repository: Repository,

): ViewModel() {

    private val event = MutableLiveData<BaseEvent<ResultEvent>>()
    val navEvent : LiveData<BaseEvent<ResultEvent>> = event

    fun searchApi( param: Map<String, Any>){
        viewModelScope.launch {
            event.value = BaseEvent(ResultEvent.StartLoading)
            try {
                repository.searchApi(param).let {
                    when(it) {
                        is Result.Success -> {
                            event.value = BaseEvent(ResultEvent.StopLoading)
                            try {
                                val data: ResultModel = gson.fromJson(
                                    it.response.toString() ,
                                    ResultModel::class.java)

                                event.value = BaseEvent(ResultEvent.OnResultData(data))

                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                event.value = BaseEvent(ResultEvent.Error(e.message))
                            }
                        }
                        is Result.Error -> {

                            event.value = BaseEvent(ResultEvent.StopLoading)
                            event.value = BaseEvent(ResultEvent.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            if (it.apiError!!.statusCode == 401) {
                                println("401")
                            }
                            if (it.apiError!!.statusCode == 403) {
                                println("403")
                            }
                            event.value = BaseEvent(ResultEvent.StopLoading)
                        }
                        is Result.Exception -> {
                            event.value = BaseEvent(ResultEvent.StopLoading)
                            if (it.exception is HttpException) {
                                if (it.exception.code() == 401) {
                                    Log.e("TAG", "getDashboardWidgets: tokenExpired ")
                                }
                            } else if (it.exception is ConnectivityException || it.exception is SocketTimeoutException || it.exception is UnknownHostException || it.exception is ConnectException || it.exception is SSLHandshakeException || it.exception is TimeoutException) {
                                event.value =
                                    BaseEvent(ResultEvent.OnNoInternetAvailable(it.exception.message))
                            } else {
                                event.value = BaseEvent(ResultEvent.Error(it.exception.message))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                event.value= BaseEvent(ResultEvent.StopLoading)
                event.value= BaseEvent(ResultEvent.Error(e.message))
            }

        }
    }

}