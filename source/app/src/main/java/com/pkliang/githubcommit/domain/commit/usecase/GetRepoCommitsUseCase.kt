package com.pkliang.githubcommit.domain.commit.usecase

import com.pkliang.githubcommit.domain.commit.entity.CommitRequestParams
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.domain.commit.repository.CommitRepository
import com.pkliang.githubcommit.domain.core.UseCase

class GetRepoCommitsUseCase(private val commitRepository: CommitRepository) :
    UseCase<CommitResponse, CommitRequestParams>() {
    override suspend fun run(params: CommitRequestParams): CommitResponse =
        commitRepository.getCommit(
            params.owner,
            params.repoName,
            params.first,
            params.after
        )
}
