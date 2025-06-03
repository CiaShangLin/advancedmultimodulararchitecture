package com.shang.login.data.response

import com.shang.data.result.OutCome
import com.shang.data.source.NetworkDataSource
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.service.LoginService
import com.shang.login.data.source.LoginRemote

class LoginRemoteImp(private val networkDataService: NetworkDataSource<LoginService>) : LoginRemote {
  override fun login(loginRequestBody: LoginRequestBody): OutCome<UserResponse> {
    TODO("Not yet implemented")
  }
}
