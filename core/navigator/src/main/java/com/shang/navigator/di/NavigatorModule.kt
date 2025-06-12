package com.shang.navigator.di

import com.shang.navigator.core.AppNavigator
import com.shang.navigator.core.AppNavigatorImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NavigatorModule {

    @Provides
    @Singleton
    fun provideNavigator(): AppNavigator {
        return AppNavigatorImp()
    }
}