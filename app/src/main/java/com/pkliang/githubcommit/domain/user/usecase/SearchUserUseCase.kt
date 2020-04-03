package com.pkliang.githubcommit.domain.user.usecase

import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.repository.UserRepository
import com.pkliang.githubcommit.domain.core.UseCase

class SearchUserUseCase(private val repository: UserRepository) :
    UseCase<SearchUserResponse, SearchUserRequestParams>() {
    override suspend fun run(params: SearchUserRequestParams): SearchUserResponse {
        return repository.searchUser(params)
    }
}
