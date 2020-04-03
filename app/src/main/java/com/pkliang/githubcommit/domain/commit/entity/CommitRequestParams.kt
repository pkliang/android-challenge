package com.pkliang.githubcommit.domain.commit.entity

data class CommitRequestParams(
    val owner: String,
    val repoName: String,
    val first: Int = 25,
    val after: String? = null
)
