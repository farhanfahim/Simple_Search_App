package com.app.simplesearch.data.remote.repo

import com.app.simplesearch.data.remote.calls.NetworkCall
import com.app.simplesearch.data.remote.retrofit.Result
import com.app.simplesearch.utils.AppConstants
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository @Inject constructor(
    private val networkCall: NetworkCall
) {

    suspend fun searchApi(
        param: Map<String, Any>,
    ): Result =
        networkCall.get<String>(
            AppConstants.PARAM_USER+"?q=${param[AppConstants.PARAM_QUERY]}&per_page=${param[AppConstants.PARAM_PER_PAGE]}&page=${param[AppConstants.PARAM_CURRENT_PAGE]}",
        )



}