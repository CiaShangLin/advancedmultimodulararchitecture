package com.shang.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

const val AUTHORIZATION_HEADER = "Authorization"
const val ACCEPT_HEADER = "Accept"
const val CONTENT_TYPE_HEADER = "Content-Type"
const val ACCEPT_LANGUAGE_HEADER = "Accept-Language"
const val CLIENT_ID_HEADER = "Client-Id"

const val JSON = "application/json"
const val ARABIC_LANGUAGE = "ar-SA"
const val ENGLISH_LANGUAGE = "en-US"

class HeaderIntercept(
    private val clientId: String,
    private val languageProvider: () -> Locale,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val language = if (languageProvider() == Locale.ENGLISH) {
            ENGLISH_LANGUAGE
        } else {
            ARABIC_LANGUAGE
        }
        requestBuilder.addHeader(ACCEPT_HEADER, JSON)
        requestBuilder.addHeader(CONTENT_TYPE_HEADER, JSON)
        requestBuilder.addHeader(ACCEPT_LANGUAGE_HEADER, language)
        return chain.proceed(requestBuilder.build())
    }
}
