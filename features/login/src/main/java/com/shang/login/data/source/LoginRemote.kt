package com.shang.login.data.source

import com.shang.data.result.OutCome
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.response.UserResponse

interface LoginRemote {
    fun login(loginRequestBody: LoginRequestBody): OutCome<UserResponse>
}