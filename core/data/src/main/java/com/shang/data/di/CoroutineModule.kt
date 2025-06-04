package com.shang.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Provides
    @Singleton
    @Named(DISPATCHER_MAIN_TAG)
    fun provideDispatcherMain(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Singleton
    @Named(DISPATCHER_DEFAULT_TAG)
    fun provideDispatcherDefault(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Singleton
    @Named(DISPATCHER_IO_TAG)
    fun provideDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
