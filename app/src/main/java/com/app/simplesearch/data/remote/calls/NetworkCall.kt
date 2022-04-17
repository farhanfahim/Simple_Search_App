package com.app.simplesearch.data.remote.calls

import android.util.Log
import android.util.MalformedJsonException
import com.google.gson.JsonElement
import com.app.simplesearch.data.remote.retrofit.Result
import com.app.simplesearch.exception.ConnectivityException
import com.app.simplesearch.data.remote.Connectivity
import com.app.simplesearch.data.remote.GetApiError
import com.app.simplesearch.data.remote.retrofit.WebService
import java.sql.DriverManager.println
import javax.inject.Inject

class NetworkCall @Inject constructor(
    val webService: WebService,
    val connectivity: Connectivity,
    val getApiError: GetApiError
) {

    inline fun <reified T : Any> generalRequest(
        request: () -> retrofit2.Response<JsonElement>
    ): Result =
        try {
            val response = request()
            Log.d("GENERAL_REQ", "Headers---> ${response.headers()}")
            Log.d("GENERAL_REQ", "RawResponse ---> ${response.raw()}")
            if (response.isSuccessful) {
                Log.d("GENERAL_REQ", "RawResponse ---> ${response.body()}")
                if (response.body() != null) {
                    Result.Success(
                        response.body()!!
                    )
                } else {
                    println("Error")
                    Result.Error(
                        response.message()
                    )
                }
            } else {
                println("RetrofitError")
//              to handle response codes other than 200
                Result.RetrofitError(getApiError.parseError(response))
//                Result.RetrofitError(getApiError.parseError(response.errorBody()!!))
            }
        } catch (exception: Exception) {
//              this catch is like onFailure when we used callbacks
            Log.d("GENERAL_REQ", "Exception ${exception.message!!}")


            if (exception is MalformedJsonException){
                val response = request()

            Log.d("file", "Exception ${response.errorBody()?.byteStream()}")
            }
//               this is custom exception to show network error
            Result.Exception(exception)
        }


    suspend inline fun <reified T : Any> get(
        endpoint: String, //end point//query params
    ): Result {
        Log.d("GENERAL_REQ", "endpoint : $endpoint")
        //        if no internet connection available throw exception and return
        if (!connectivity.isNetworkConnected()) {
            return Result.Exception(ConnectivityException())
        }
        return generalRequest<T> { webService.get(endpoint) }
    }


}

