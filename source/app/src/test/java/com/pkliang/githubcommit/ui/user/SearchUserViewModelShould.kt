package com.pkliang.githubcommit.ui.user

import androidx.lifecycle.LifecycleOwner
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.test.MvRxTestRule
import com.pkliang.githubcommit.CoroutinesTestRule
import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.entity.User
import com.pkliang.githubcommit.domain.user.usecase.SearchUserUseCase
import com.pkliang.githubcommit.ui.core.InitState
import com.pkliang.githubcommit.ui.core.LcerState
import com.pkliang.githubcommit.ui.core.LoadFirstPageFailed
import com.pkliang.githubcommit.ui.core.LoadingFirstPage
import com.pkliang.githubcommit.ui.core.LoadingNextPage
import com.pkliang.githubcommit.ui.core.PageLoaded
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.spyk
import org.junit.Rule
import org.junit.Test


class SearchUserViewModelShould {
    @Rule
    @JvmField
    var testSchedulerRule = MvRxTestRule()

    @Rule
    @JvmField
    var coroutineTestRule = CoroutinesTestRule()

    private val lifecycleOwner = spyk<LifecycleOwner>()
    private val subscriber = spyk<(LcerState<SearchUserResponse>) -> Unit>()
    private val searchUserUseCase = mockk<SearchUserUseCase>()
    private val secondPage = "2"
    private val thirdPage = "3"
    private val exception = Exception("test")
    private val user1 = mockk<User>()
    private val user2 = mockk<User>()
    private val firstPageRequest = mockk<SearchUserRequestParams>()


    @Test
    fun `emit correct sequence of states when fetchFirstPage called and the first page returned`() {
        givenFirstPageSearchUserResponse()
        every { subscriber(any()) } just runs
        val searchUserViewModel = SearchUserViewModel(
            searchUserUseCase,
            LcerState()
        )
        searchUserViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


        searchUserViewModel.fetchFirstPage(firstPageRequest)


        coVerifySequence { searchUserUseCase(firstPageRequest) }
        coVerifySequence { fetchFirstPageStatesSequence() }
        confirmVerified(lifecycleOwner, subscriber, searchUserUseCase)
    }

    @Test
    fun `emit correct sequence of states when fetchFirstPage called and an exception returned`() {
        givenException()
        every { subscriber(any()) } just runs
        val searchUserViewModel = SearchUserViewModel(
            searchUserUseCase,
            LcerState()
        )
        searchUserViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


        searchUserViewModel.fetchFirstPage(firstPageRequest)


        coVerifySequence { searchUserUseCase(firstPageRequest) }
        coVerifySequence {
            subscriber(LcerState(InitState(), Uninitialized))
            subscriber(LcerState(LoadingFirstPage<SearchUserResponse>(null), Loading()))
            subscriber(
                LcerState(
                    LoadFirstPageFailed<SearchUserResponse>(null, exception),
                    Fail(exception)
                )
            )
        }
        confirmVerified(lifecycleOwner, subscriber, searchUserUseCase)
    }

    @Test
    fun `emit correct sequence of states when fetchNextPage called and next pages returned`() {
        givenFirstPageSearchUserResponse()
        every { subscriber(any()) } just runs
        val searchUserViewModel = SearchUserViewModel(
            searchUserUseCase,
            LcerState()
        )
        searchUserViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


        searchUserViewModel.fetchFirstPage(firstPageRequest)
        givenSecondPageSearchUserResponse()
        searchUserViewModel.fetchNextPage()


        coVerifySequence {
            searchUserUseCase(firstPageRequest)
            searchUserUseCase(firstPageRequest.copy(after = secondPage))
        }
        coVerifySequence {
            fetchFirstPageStatesSequence()
            fetchSecondPageStatesSequence()
        }
        confirmVerified(lifecycleOwner, subscriber, searchUserUseCase)
    }

    private fun fetchFirstPageStatesSequence() {
        subscriber(LcerState(InitState(), Uninitialized))
        subscriber(LcerState(LoadingFirstPage<SearchUserResponse>(null), Loading()))
        subscriber(
            LcerState(
                PageLoaded(expectedFirstPageResponse()),
                Success(expectedFirstPageResponse())
            )
        )
    }

    private fun fetchSecondPageStatesSequence() {
        subscriber(LcerState(LoadingNextPage(expectedFirstPageResponse()), Loading()))
        subscriber(
            LcerState(
                PageLoaded(expectedSecondPageResponse()),
                Success(SearchUserResponse(listOf(user2), true, thirdPage))
            )
        )
    }

    private fun givenFirstPageSearchUserResponse() =
        coEvery { searchUserUseCase(firstPageRequest) } returns SearchUserResponse(
            listOf(user1),
            true,
            secondPage
        )

    private fun givenSecondPageSearchUserResponse() =
        coEvery { searchUserUseCase(firstPageRequest.copy(after = secondPage)) } returns SearchUserResponse(
            listOf(user2),
            true,
            thirdPage
        )

    private fun givenException() =
        coEvery { searchUserUseCase(firstPageRequest) } throws exception

    private fun expectedFirstPageResponse() = SearchUserResponse(
        listOf(user1),
        true,
        secondPage
    )

    private fun expectedSecondPageResponse() = SearchUserResponse(
        listOf(user1, user2),
        true,
        thirdPage
    )
}
