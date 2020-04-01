package com.pkliang.githubcommit.domain.user.entity

data class SearchUserRequestParams(
    val queryString: String,
    val first: Int,
    val after: String? = null
)
