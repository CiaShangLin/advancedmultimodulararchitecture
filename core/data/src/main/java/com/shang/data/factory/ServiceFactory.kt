package com.shang.data.factory

import retrofit2.Retrofit

class ServiceFactory(private val retrofit: Retrofit) {
    fun <T> create(service: Class<T>): T {
        return retrofit.create<T>(service)
    }
}
