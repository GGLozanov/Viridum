package com.lozanov.viridum.di


import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Singleton
    @Provides
    fun provideNavigator(): Navigator = Navigator(listOf(
            NavDestination.ModelSelection, NavDestination.Login))
}