package com.shang.login.data.response

import com.shang.data.result.OutCome
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.source.LoginRemote

class LoginRemoteImp: LoginRemote {
    override fun login(loginRequestBody: LoginRequestBody): OutCome<UserResponse> {
        TODO("Not yet implemented")
    }
}