package com.pkliang.githubcommit

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.pkliang.githubcommit.data.network.AuthInterceptor
import com.pkliang.githubcommit.data.repository.CommitRepositoryGraphqlImpl
import com.pkliang.githubcommit.domain.commit.repository.CommitRepository
import com.pkliang.githubcommit.domain.commit.usecase.GetRepoCommitsUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

const val BASE_URL = "https://api.github.com/graphql"

val appModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(get<Context>().getString(R.string.auth_token)))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    single { ApolloClient.builder().serverUrl(BASE_URL).okHttpClient(get()).build() }

    single<CommitRepository> { CommitRepositoryGraphqlImpl(get()) }

    factory { GetRepoCommitsUseCase(get()) }
}
