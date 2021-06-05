package com.lozanov.viridum.di

import android.content.Context
import android.net.ConnectivityManager
import com.lozanov.viridum.ViridumHiltApplication
import com.lozanov.viridum.api.ViridumAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideAPI(
        @ApplicationContext application: Context
    ): ViridumAPI = ViridumAPI(application.getSystemService(
        Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
}