package com.pkliang.githubcommit.domain.user.usecase

import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SearchUserUseCaseShould {
    private val userRepository = mockk<UserRepository>()
    private val searchUserRequestParams = mockk<SearchUserRequestParams>()
    private val searchUserResponse = mockk<SearchUserResponse>()

    private val searchUserUseCase = SearchUserUseCase(userRepository)

    @Test
    fun `call searchUser when invoked`() = runBlockingTest {
        coEvery {
            userRepository.searchUser(searchUserRequestParams)
        } returns searchUserResponse


        searchUserUseCase(searchUserRequestParams)


        coVerify {
            userRepository.searchUser(searchUserRequestParams)
        }
        confirmVerified(userRepository, searchUserResponse, searchUserRequestParams)
    }

    @Test
    fun `populate searchUserResponse when invoked`() = runBlockingTest {
        coEvery {
            userRepository.searchUser(searchUserRequestParams)
        } returns searchUserResponse


        assertThat(searchUserUseCase(searchUserRequestParams), equalTo(searchUserResponse))
    }
}
