package com.pkliang.githubcommit.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.pkliang.githubcommit.SearchUserQuery
import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.entity.User
import com.pkliang.githubcommit.domain.user.repository.UserRepository

class UserRepositoryGraphqlImpl(private val apolloClient: ApolloClient) :
    UserRepository {
    override suspend fun searchUser(params: SearchUserRequestParams): SearchUserResponse {
        val response = apolloClient.query(
            SearchUserQuery(
                params.queryString,
                Input.fromNullable(params.first),
                Input.fromNullable(params.after)
            )
        ).toDeferred().await()

        if (response.hasErrors()) {
            throw Exception(response.errors()[0].message())
        }

        val nextPage = response.data()?.search?.pageInfo?.endCursor
        val hasNextPage = response.data()?.search?.pageInfo?.hasNextPage

        val users = response.data()?.search?.edges?.mapNotNull {
            it?.node?.inlineFragment as SearchUserQuery.AsUser?
        }?.map {
            User(
                userName = it.login,
                avatarUrl = it.avatarUrl,
                createdAt = it.createdAt,
                email = it.email,
                name = it.name,
                company = it.company,
                location = it.location,
                followersCount = it.followers.totalCount,
                followingsCount = it.following.totalCount,
                pullRequestsCount = it.pullRequests.totalCount,
                repositoriesCount = it.repositories.totalCount
            )
        }

        return SearchUserResponse(users, hasNextPage, nextPage)
    }
}
