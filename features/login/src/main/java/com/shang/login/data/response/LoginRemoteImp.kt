package com.shang.login.data.response

import com.shang.data.error.toDomain
import com.shang.data.result.OutCome
import com.shang.data.source.NetworkDataSource
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.service.LoginService
import com.shang.login.data.source.LoginRemote

class LoginRemoteImp(private val networkDataService: NetworkDataSource<LoginService>) : LoginRemote {
  override suspend fun login(loginRequestBody: LoginRequestBody): OutCome<UserResponse> {
    return networkDataService.performRequest(
      request = { login(loginRequestBody).await() },
      onSuccess = { response, headers -> OutCome.success(response) },
      onError = { errorResponse, code -> OutCome.error(errorResponse.toDomain(code)) },
    )
  }
}
