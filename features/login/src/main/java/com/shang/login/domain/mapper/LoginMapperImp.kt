package com.shang.login.domain.mapper

import com.shang.login.data.response.UserResponse
import com.shang.login.domain.model.User
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
