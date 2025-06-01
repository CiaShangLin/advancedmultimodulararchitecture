package com.shang.data.di

import com.shang.data.BuildConfig
import com.shang.data.OkHttpClientProvider
import com.shang.data.interceptors.HeaderIntercept
import com.shang.data.okhttp.OkhttpClientProviderInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun providerOkhttpClientProvider(): OkhttpClientProviderInterface {
        return OkHttpClientProvider()
    }

    @Provides
    @Singleton
    @Named(OKHTTP_CALL_FACTORY_TAG)
    fun providerOkhttpCallFactory(
        @Named(LOGGING_INTERCEPTOR_TAG) OkhttpLoggerIntercept: HttpLoggingInterceptor,
        @Named(HEADER_INTERCEPTOR_TAG) headerIntercept: HeaderIntercept,
        okhttpClientProvider: OkhttpClientProviderInterface
    ): Call.Factory {
        return okhttpClientProvider.getOkHttpClient(BuildConfig.PIN_CERTIFICATE)
            .addInterceptor(OkhttpLoggerIntercept)
            .addInterceptor(headerIntercept)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}
