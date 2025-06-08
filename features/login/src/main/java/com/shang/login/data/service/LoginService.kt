package com.shang.login.data.service

import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.response.UserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

const val EMAIL = "email"

interface LoginService {

    @POST("Auth/login")
    fun login(
        @Body body: LoginRequestBody,
    ): Deferred<Response<UserResponse>>

    @POST("Auth/ForgetPassword")
    fun forgetPassword(
        @Query(EMAIL) email: String,
    ): Deferred<Response<Unit>>
}
