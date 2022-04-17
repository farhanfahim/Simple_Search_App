package com.app.simplesearch.utils

class AppConstants {

    companion object {

        const val BASE_URL = "https://api.github.com/search/"

        // parameters
        const val PARAM_QUERY = "keyword"
        const val PARAM_PER_PAGE = "per_page"
        const val PARAM_CURRENT_PAGE = "page"
        const val PARAM_USER = "users"

        const val ErrorMessageNoConnectivity = "Connectivity lost. Please check " +
                "network connectivity."

    }

}