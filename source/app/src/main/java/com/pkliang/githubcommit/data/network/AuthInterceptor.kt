package com.pkliang.githubcommit.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().header(
                "Authorization",
                "Bearer 12b8eaab100f425c450e568db42650eabad9a0c9"
            ).build()
        )
    }
}
