package com.shang.data.service

import com.shang.data.response.TokenResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

const val BASE_URL = "https://api.mockfly.dev/mocks/6d4f2c90-a7f9-427a-9bf5-cec724f26a45/"
const val REFRESH_TOKEN = "refreshToken"
interface SessionService {

    @GET("Auth/GetSession")
    fun getToken(
        @Header(REFRESH_TOKEN) refreshToken: String,
    ): Deferred<Response<TokenResponse>>

    @GET("Auth/DeleteSession")
    fun logout(
        @Header(REFRESH_TOKEN) refreshToken: String,
    ): Deferred<Response<Unit>>
}
