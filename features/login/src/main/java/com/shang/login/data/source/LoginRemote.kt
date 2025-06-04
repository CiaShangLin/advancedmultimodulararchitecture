package com.shang.login.data.source

import com.shang.data.result.OutCome
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.domain.model.User

interface LoginRemote {
  suspend fun login(loginRequestBody: LoginRequestBody): OutCome<User>
}
