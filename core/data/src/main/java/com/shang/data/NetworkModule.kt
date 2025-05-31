package com.shang.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.log

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    @Named("clientId")
    fun providerClientId(): String {
        return ""
    }

    @Provides
    @Named("accessToken")
    fun providerAccessToken(): () -> String? {
        return { "" }
    }

    @Provides
    @Named("language")
    fun providerLanguage(): () -> Locale {
        return { Locale.ENGLISH }
    }


    @Provides
    @Singleton
    @Named("HeaderIntercept")
    fun providerHeaderIntercept(
        @Named("clientId") clientId: String,
        @Named("accessToken") accessToken: () -> String?,
        @Named("language") language: () -> Locale
    ): Interceptor {
        return HeaderIntercept(clientId, accessToken, language)
    }

    @Provides
    @Singleton
    @Named("OkhttpLoggerIntercept")
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
    @Named("OkhttpCallFactory")
    fun providerOkhttpCallFactory(interceptor: Interceptor): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}
