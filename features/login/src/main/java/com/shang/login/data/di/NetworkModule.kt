package com.shang.login.data.di

import com.shang.data.factory.ServiceFactory
import com.shang.login.data.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoginServiceFactory(serviceFactory: ServiceFactory): LoginService {
        return serviceFactory.createService(LoginService::class.java)
    }
}