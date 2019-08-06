package com.pkliang.githubcommit.domain.commit.repository

import com.pkliang.githubcommit.domain.commit.entity.CommitResponse

interface CommitRepository {
    suspend fun getCommit(
        owner: String,
        repoName: String,
        first: Int,
        after: String?
    ): CommitResponse
}
