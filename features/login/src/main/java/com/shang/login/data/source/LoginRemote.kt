package com.shang.login.data.source

import com.shang.domain.model.User
import com.shang.domain.result.OutCome

interface LoginRemote {
    suspend fun login(username: String, password: String): OutCome<User>
}
