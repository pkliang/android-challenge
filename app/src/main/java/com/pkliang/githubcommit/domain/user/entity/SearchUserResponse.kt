package com.pkliang.githubcommit.domain.user.entity

data class SearchUserResponse(
    val users: List<User>?,
    val hasNextPage: Boolean?,
    val nextPage: String?
)
