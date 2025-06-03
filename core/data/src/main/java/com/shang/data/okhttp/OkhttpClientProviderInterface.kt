package com.shang.data.okhttp

import okhttp3.OkHttpClient

interface OkhttpClientProviderInterface {

  fun getOkHttpClient(pin: String): OkHttpClient.Builder

  fun cancelAllRequest()
}
