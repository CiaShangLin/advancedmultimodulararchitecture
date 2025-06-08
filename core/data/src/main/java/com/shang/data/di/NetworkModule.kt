package com.shang.data.di

import android.content.Context
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.shang.data.BuildConfig
import com.shang.data.OkHttpClientProvider
import com.shang.data.connectivity.NetworkMonitorImp
import com.shang.data.connectivity.NetworkMonitorInterface
import com.shang.data.factory.ServiceFactory
import com.shang.data.okhttp.OkhttpClientProviderInterface
import com.shang.data.service.BASE_URL
import com.shang.data.service.SessionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(context: Context): NetworkMonitorInterface {
        return NetworkMonitorImp(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClientProvider(): OkhttpClientProviderInterface {
        return OkHttpClientProvider()
    }

    // ok http factory
    @Provides
    @Singleton
    fun provideOkHttpCallFactory(
        @Named(LOGGING_INTERCEPTOR_TAG) okHttpLoggingInterceptor: Interceptor,
        @Named(HEADER_INTERCEPTOR_TAG) headerInterceptor: Interceptor,
        @Named(CHUCKER_INTERCEPTOR_TAG) chuckerInterceptor: Interceptor,
        @Named(CONNECTIVITY_INTERCEPTOR_TAG) connectivityInterceptor: Interceptor,
        @Named(AUTHENTICATION_INTERCEPTOR_TAG) authenticationInterceptor: Interceptor,
        okHttpClientProvider: OkhttpClientProviderInterface,
    ): OkHttpClient {
        return okHttpClientProvider.getOkHttpClient(BuildConfig.PIN_CERTIFICATE)
            .addInterceptor(okHttpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(authenticationInterceptor)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideServiceFactory(retrofit: Retrofit): ServiceFactory {
        return ServiceFactory(retrofit)
    }

    @Provides
    @Singleton
    fun providesSessionService(serviceFactory: ServiceFactory): SessionService {
        return serviceFactory.create(SessionService::class.java)
    }
}
