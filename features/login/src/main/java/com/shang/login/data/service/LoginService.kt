package com.shang.login.data.service

import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.response.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "https://mydomain.com"
const val EMAIL = "email"

interface LoginService {

    @POST("$BASE_URL/Auth/login")
    fun login(
        @Body body: LoginRequestBody,
    ): Deferred<Response<LoginResponse>>

    @POST("$BASE_URL/Auth/ForgetPassword")
    fun forgetPassword(
        @Query(EMAIL) email: String,
    ): Deferred<Response<Unit>>
}
