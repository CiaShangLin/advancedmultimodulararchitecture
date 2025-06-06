package com.shang.data.interceptors

import com.shang.data.connectivity.NetworkMonitorInterface
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ConnectivityInterceptor @Inject constructor(private val networkMonitorInterface: NetworkMonitorInterface) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (networkMonitorInterface.hasConnectivity()) {
            return chain.proceed(chain.request())
        } else {
            throw NoConnectivityException
        }
    }
}

object NoConnectivityException : IOException()
