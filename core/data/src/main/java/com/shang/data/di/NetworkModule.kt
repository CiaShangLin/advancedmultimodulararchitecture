package com.shang.data.di

import com.shang.data.interceptors.ACCEPT_HEADER
import com.shang.data.BuildConfig
import com.shang.data.interceptors.CLIENT_ID_HEADER
import com.shang.data.interceptors.HeaderIntercept
import com.shang.data.OkHttpClientProvider
import com.shang.data.okhttp.OkhttpClientProviderInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Named(CLIENT_ID_TAG)
    fun providerClientId(): String {
        return ""
    }

    @Provides
    @Named(ACCESS_TOKEN_TAG)
    fun providerAccessToken(): () -> String? {
        return { "" }
    }

    @Provides
    @Named(LANGUAGE_TAG)
    fun providerLanguage(): () -> Locale {
        return { Locale.ENGLISH }
    }

    @Provides
    @Singleton
    @Named(HEADER_INTERCEPTOR_TAG)
    fun providerHeaderIntercept(
        @Named(CLIENT_ID_TAG) clientId: String,
        @Named(ACCESS_TOKEN_TAG) accessToken: () -> String?,
        @Named(LANGUAGE_TAG) language: () -> Locale,
    ): Interceptor {
        return HeaderIntercept(clientId, accessToken, language)
    }

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR_TAG)
    fun providerOkhttpLoggerIntercept(): okhttp3.logging.HttpLoggingInterceptor {
        val logging = okhttp3.logging.HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.NONE
        }
        if (!BuildConfig.DEBUG) {
            logging.redactHeader(CLIENT_ID_HEADER)
            logging.redactHeader(ACCEPT_HEADER)
        }
        return logging
    }

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
