package com.pkliang.githubcommit.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().header(
                "Authorization",
                "Bearer 88cf261dc748743e435c19e8c6403ffdd28896b1"
            ).build()
        )
    }
}
