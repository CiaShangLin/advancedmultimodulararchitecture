package com.shang.login.domain.mapper

import com.shang.login.data.response.UserResponse
import com.shang.login.domain.model.User

interface LoginMapperInterface {
    suspend fun toDomain(userResponse: UserResponse): User
}
