package com.pkliang.githubcommit.domain.user.repository

import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams

interface UserRepository {
    suspend fun searchUser(params: SearchUserRequestParams): SearchUserResponse
}
