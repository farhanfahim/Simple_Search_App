package com.app.simplesearch.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SimpleSearch : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    companion object {
        val TAG: String = SimpleSearch::class.java
            .simpleName
        lateinit var ctx: SimpleSearch

        fun getAppContext(): SimpleSearch {
            return ctx
        }
    }
}