package com.pkliang.githubcommit.domain.commit.entity

data class CommitResponse(
    val commits: List<Commit>?,
    val nextPage: String?
)
