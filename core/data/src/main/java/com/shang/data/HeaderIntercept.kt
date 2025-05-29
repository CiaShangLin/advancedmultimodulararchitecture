package com.shang.data

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

class HeaderIntercept(
    private val clientId: String,
    private val accessTokenProvider: () -> String?,
    private val languageProvider: () -> Locale
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

    }
}