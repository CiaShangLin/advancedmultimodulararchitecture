package com.shang.data.source

import com.google.gson.Gson
import com.shang.data.connectivity.NetworkMonitorInterface
import com.shang.data.di.HEADER_LOCATION
import com.shang.data.error.getDefaultErrorResponse
import com.shang.data.error.getErrorResponse
import com.shang.data.interceptors.NoConnectivityException
import com.shang.data.mapper.toDomain
import com.shang.data.response.ErrorResponse
import com.shang.domain.result.OutCome
import kotlinx.coroutines.isActive
import okhttp3.Headers
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException
import kotlin.coroutines.coroutineContext

class NetworkDataSource<SERVICE>(
    val service: SERVICE,
    val gson: Gson,
    val networkMonitor: NetworkMonitorInterface,
    val userIdProvider: () -> String,
) {

    suspend fun <R, T> performRequest(
        request: suspend (SERVICE).(String) -> Response<R>,
        onSuccess: suspend (R, Headers) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onRedirect: suspend (String, Int) -> OutCome<T> = { _, _ -> OutCome.empty<T>() },
        onEmpty: suspend () -> OutCome<T> = { OutCome.empty<T>() },
        onError: suspend (ErrorResponse, Int) -> OutCome<T> = { errorResponse, code ->
            OutCome.error(
                errorResponse.toDomain(code),
            )
        },
    ): OutCome<T> {
        try {
            val response = service.request(userIdProvider())
            val responseCode = response.code()
            val errorBodyString = response.errorBody()?.string()
            if (response.isSuccessful || responseCode == DataSource.SEE_OTHERS) {
                val body = response.body()
                if (body != null && body != Unit) {
                    if (coroutineContext.isActive) {
                        return onSuccess(body, response.headers())
                    } else {
                        return onEmpty()
                    }
                } else {
                    val location = response.headers()[HEADER_LOCATION]
                    if (location != null) {
                        return onRedirect(location, responseCode)
                    } else {
                        return onEmpty()
                    }
                }
            } else if (errorBodyString.isNullOrEmpty()) {
                return onError(getDefaultErrorResponse(), responseCode)
            } else {
                return onError(getErrorResponse(gson, errorBodyString), responseCode)
            }
        } catch (e: Exception) {
            val code = when (e) {
                is SocketTimeoutException -> DataSource.TIMEOUT
                is UnknownHostException, is NoConnectivityException -> DataSource.UNKNOWN
                is SSLProtocolException, is SSLHandshakeException -> DataSource.SSL_PINNING
                else -> DataSource.UNKNOWN
            }
            return onError(getDefaultErrorResponse(), code)
        }
    }
}
