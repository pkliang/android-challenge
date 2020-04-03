package com.pkliang.githubcommit.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.pkliang.githubcommit.GetCommitQuery
import com.pkliang.githubcommit.domain.commit.entity.Commit
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.domain.commit.repository.CommitRepository

class CommitRepositoryGraphqlImpl(private val apolloClient: ApolloClient) : CommitRepository {
    override suspend fun getCommit(
        owner: String,
        repoName: String,
        first: Int,
        after: String?
    ): CommitResponse {
        val response = apolloClient.query(
            GetCommitQuery(
                owner,
                repoName,
                Input.fromNullable(first),
                Input.fromNullable(after)
            )
        ).toDeferred().await()

        if (response.hasErrors()) {
            throw Exception(response.errors()[0].message())
        }

        val commitFragment =
            response.data()?.repository?.defaultBranchRef?.target?.inlineFragment as GetCommitQuery.AsCommit?
        val nextPage = commitFragment?.history?.pageInfo?.endCursor
        val commits = commitFragment?.history?.edges?.map {
            it?.node
        }?.map {
            Commit(
                it?.oid,
                it?.author?.name,
                it?.author?.avatarUrl,
                it?.messageHeadline,
                it?.committedDate,
                it?.authoredDate
            )
        }

        return CommitResponse(commits, nextPage)
    }
}
