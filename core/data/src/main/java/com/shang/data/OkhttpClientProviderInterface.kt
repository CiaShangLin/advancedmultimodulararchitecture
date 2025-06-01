package com.shang.data

import okhttp3.OkHttpClient

interface OkhttpClientProviderInterface {

    fun getOkHttpClient(pin: String): OkHttpClient.Builder

    fun cancelAllRequest()
}
