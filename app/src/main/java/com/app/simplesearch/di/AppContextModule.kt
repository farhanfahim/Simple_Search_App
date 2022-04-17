package com.app.simplesearch.di


import android.content.Context
import com.app.simplesearch.app.SimpleSearch
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppContextModule {

    @Singleton
    @Provides
    fun getApplicationContext(): Context = SimpleSearch.getAppContext().applicationContext

}