package com.shang.data.factory

import retrofit2.Retrofit
import retrofit2.create

class ServiceFactory(private val retrofit: Retrofit) {
    fun <T> createService(service: Class<T>): T {
        return retrofit.create<T>(service)
    }
}
