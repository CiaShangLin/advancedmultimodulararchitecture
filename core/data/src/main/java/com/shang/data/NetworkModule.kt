package com.shang.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.math.log

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

//    @Provides
//    @Singleton
//    fun providerHeaderIntercept():Interceptor{
//        return HeaderIntercept()
//    }

  @Provides
  @Singleton
  fun providerOkhttpLoggerIntercept(): okhttp3.logging.HttpLoggingInterceptor {
    val logging = okhttp3.logging.HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
      logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
    } else {
      logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.NONE
    }
    if (!BuildConfig.DEBUG){
      logging.redactHeader(CLIENT_ID_HEADER)
      logging.redactHeader(ACCEPT_HEADER)
    }
    return logging
  }


  @Provides
  @Singleton
  fun providerOkhttpCallFactory(interceptor: Interceptor): Call.Factory {
    return OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .retryOnConnectionFailure(true)
      .followRedirects(false)
      .followSslRedirects(false)
      .readTimeout(60,TimeUnit.SECONDS)
      .writeTimeout(60,TimeUnit.SECONDS)
      .connectTimeout(60,TimeUnit.SECONDS)
      .build()
  }
}
