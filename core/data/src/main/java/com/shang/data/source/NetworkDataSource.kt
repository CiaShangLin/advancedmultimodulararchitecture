package com.shang.data.source

import com.google.gson.Gson
import com.shang.data.connectivity.NetworkMonitorInterface
import com.shang.data.error.toDomain
import com.shang.data.response.ErrorResponse
import com.shang.data.result.OutCome
import okhttp3.Headers
import retrofit2.Response

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
    onError: suspend (ErrorResponse, Int) -> OutCome<T> = { errorResponse, code -> OutCome.error(errorResponse.toDomain(code)) },
  ): OutCome<T> {
  }
}
