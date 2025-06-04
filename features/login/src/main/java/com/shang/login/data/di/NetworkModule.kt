package com.shang.login.data.di

import com.google.gson.Gson
import com.shang.data.connectivity.NetworkMonitorInterface
import com.shang.data.di.DISPATCHER_DEFAULT_TAG
import com.shang.data.di.USER_ID_TAG
import com.shang.data.factory.ServiceFactory
import com.shang.data.source.NetworkDataSource
import com.shang.login.data.response.LoginRemoteImp
import com.shang.login.data.service.LoginService
import com.shang.login.data.source.LoginRemote
import com.shang.login.domain.mapper.LoginMapperImp
import com.shang.login.domain.mapper.LoginMapperInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

  @Provides
  @Singleton
  fun provideLoginServiceFactory(serviceFactory: ServiceFactory): LoginService {
    return serviceFactory.createService(LoginService::class.java)
  }

  @Provides
  @Singleton
  fun provideNetworkDataSource(
    loginService: LoginService,
    gson: Gson,
    networkMonitorInterface: NetworkMonitorInterface,
    @Named(USER_ID_TAG) userIdProvider: () -> String,
  ): NetworkDataSource<LoginService> {
    return NetworkDataSource(
      loginService,
      gson,
      networkMonitorInterface,
      userIdProvider,
    )
  }

  @Provides
  @Singleton
  fun provideLoginMapper(@Named(DISPATCHER_DEFAULT_TAG) dispatcher: CoroutineDispatcher): LoginMapperInterface {
    return LoginMapperImp(dispatcher)
  }

  @Provides
  @Singleton
  fun provideLoginRemoteImplementer(
    networkDataSource: NetworkDataSource<LoginService>,
    loginMapper: LoginMapperInterface,
  ): LoginRemote {
    return LoginRemoteImp(networkDataSource, loginMapper)
  }
}
