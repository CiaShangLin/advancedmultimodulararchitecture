package com.shang.login.domain.mapper

import com.shang.domain.model.User
import com.shang.login.data.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoginMapperImp(private val defaultCoroutineDispatcher: CoroutineDispatcher) :
    LoginMapperInterface {
    override suspend fun toDomain(userResponse: UserResponse): User {
        return withContext(defaultCoroutineDispatcher) {
            User(
                id = userResponse.id.orEmpty(),
                fullName = userResponse.fullName.orEmpty(),
                email = userResponse.email.orEmpty(),
                photo = userResponse.photo.orEmpty(),
            )
        }
    }
}
