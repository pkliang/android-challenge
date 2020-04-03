package com.pkliang.githubcommit.domain.commit.entity

data class Commit(
    val id: String?,
    val author: String?,
    val avatarUrl: String?,
    val message: String?,
    val committedDate: String?,
    val authoredDate: String?
)
