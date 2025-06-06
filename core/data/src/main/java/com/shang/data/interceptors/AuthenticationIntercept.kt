package com.shang.data.interceptors

import com.shang.data.di.BEARER
import com.shang.data.response.TokenResponse
import com.shang.data.service.SessionService
import com.shang.data.source.DataSource
import com.shang.protodatastore.manager.session.SessionDataStoreInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationIntercept @Inject constructor(
    private val sessionDataInterface: SessionDataStoreInterface,
    private val sessionService: SessionService,
    private val coroutineDispatcher: CoroutineDispatcher,
) :
    Interceptor {

    private val mutex = Mutex()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking(coroutineDispatcher) {
            sessionDataInterface.getAccessToken()
        }
        val authenticationRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER $accessToken")
            .build()
        val response = chain.proceed(authenticationRequest)
        if (response.code != DataSource.UNAUTHORISED) {
            return response
        }

        val tokenResponse = runBlocking {
            mutex.withLock {
                val tokenResponse = getUpdatedToken().await()
                tokenResponse.body().also {
                    sessionDataInterface.setAccessToken(it?.accessToken ?: "")
                    sessionDataInterface.setRefreshToken(it?.refreshToken ?: "")
                }
            }
        }

        return if (tokenResponse?.accessToken != null) {
            response.close()

            // retry the original request with the new token
            val authenticatedRequest =
                request.newBuilder()
                    .header(AUTHORIZATION_HEADER, "$BEARER ${tokenResponse.accessToken}").build()

            val response = chain.proceed(authenticatedRequest)

            response
        } else {
            response
        }
    }

    private suspend fun getUpdatedToken(): Deferred<retrofit2.Response<TokenResponse>> {
        val refreshToken = sessionDataInterface.getRefreshToken()
        return withContext(coroutineDispatcher) {
            sessionService.getToken(refreshToken)
        }
    }
}
