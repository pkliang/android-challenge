package com.pkliang.githubcommit.domain.commit.usecase

import com.pkliang.githubcommit.domain.commit.entity.CommitRequestParams
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.domain.commit.repository.CommitRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GetRepoCommitsUseCaseShould {
    private val commitRepository = mockk<CommitRepository>()
    private val commitResponse = mockk<CommitResponse>()
    private val commitRequestParams = CommitRequestParams("owner", "repoName")

    private val getRepoCommitsUseCase = GetRepoCommitsUseCase(commitRepository)

    @Test
    fun `call getCommit when invoked`() = runBlocking {
        coEvery {
            commitRepository.getCommit(any(), any(), any(), any())
        } returns commitResponse


        getRepoCommitsUseCase(commitRequestParams)


        coVerify {
            commitRepository.getCommit(
                commitRequestParams.owner,
                commitRequestParams.repoName,
                commitRequestParams.first,
                commitRequestParams.after
            )
        }
        confirmVerified(commitRepository, commitResponse)
    }

    @Test
    fun `populate CommitResponse when invoked`() = runBlocking {
        coEvery {
            commitRepository.getCommit(any(), any(), any(), any())
        } returns commitResponse


        assertThat(getRepoCommitsUseCase(commitRequestParams), equalTo(commitResponse))
    }
}
