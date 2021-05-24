package com.lozanov.viridum.di

import android.content.Context
import com.lozanov.viridum.ViridumHiltApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext application: Context
    ): ViridumHiltApplication = application as ViridumHiltApplication
}