package com.pkliang.githubcommit.domain.user.entity

data class User(
    val userName: String,
    val avatarUrl: String,
    val createdAt: String,
    val email: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val followersCount: Int,
    val followingsCount: Int,
    val pullRequestsCount: Int
)
