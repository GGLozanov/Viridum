package com.lozanov.viridum.di


import android.content.Context
import com.lozanov.viridum.shared.NavDestination
import com.lozanov.viridum.shared.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Singleton
    @Provides
    fun provideNavigator(
        @ApplicationContext application: Context
    ): Navigator = Navigator(listOf(
            NavDestination.MainDestination.ModelSelection, NavDestination.Login))
}