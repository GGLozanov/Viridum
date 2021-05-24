package com.lozanov.viridum.di

import com.lozanov.viridum.repo.decl.SketchFabRepository
import com.lozanov.viridum.repo.impl.SketchFabRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideSketchFabRepository(): SketchFabRepository {
        // TODO: Provide DAO through arg later on
        return SketchFabRepositoryImpl(

        )
    }
}