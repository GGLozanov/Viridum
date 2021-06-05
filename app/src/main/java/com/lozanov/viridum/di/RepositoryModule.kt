package com.lozanov.viridum.di

import android.content.Context
import com.lozanov.viridum.repo.decl.SketchFabRepository
import com.lozanov.viridum.repo.impl.SketchFabRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideSketchFabRepository(
        @ApplicationContext application: Context
    ): SketchFabRepository {
        return SketchFabRepositoryImpl(
            NetworkModule.provideAPI(application) // should... work?
        )
    }
}