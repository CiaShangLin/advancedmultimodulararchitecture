package com.shang.login.domain.mapper

import com.shang.domain.model.User
import com.shang.login.data.response.UserResponse

interface LoginMapperInterface {
    suspend fun toDomain(userResponse: UserResponse): User
}
