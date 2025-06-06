package com.shang.login.data.source

import com.shang.domain.result.OutCome
import com.shang.login.domain.model.User

interface LoginRemote {
    suspend fun login(username: String, password: String): OutCome<User>
}
