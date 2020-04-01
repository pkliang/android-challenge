package com.pkliang.githubcommit.domain.user.entity

data class SearchUserResponse(
    val users: List<User>?,
    val nextPage: String?
)
