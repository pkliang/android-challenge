package com.pkliang.githubcommit.ui.commit

import androidx.lifecycle.LifecycleOwner
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.test.MvRxTestRule
import com.pkliang.githubcommit.CoroutinesTestRule
import com.pkliang.githubcommit.domain.commit.entity.Commit
import com.pkliang.githubcommit.domain.commit.entity.CommitRequestParams
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.domain.commit.usecase.GetRepoCommitsUseCase
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

class CommitViewModelShould {
    @Rule
    @JvmField
    var testSchedulerRule = MvRxTestRule()

    @Rule
    @JvmField
    var coroutineTestRule = CoroutinesTestRule()

    private val lifecycleOwner = spyk<LifecycleOwner>()
    private val subscriber = spyk<(LcerState<CommitResponse>) -> Unit>()
    private val getRepoCommitsUseCase = mockk<GetRepoCommitsUseCase>()

    private val secondPage = "1"
    private val thirdPage = "2"
    private val exception = Exception("exception")

    @Test
    fun `emit correct sequence of states when fetchFirstPage called and the first page returned`() {
        givenFirstPageCommitResponse()
        every { subscriber(any()) } just runs
        val commitViewModel = CommitViewModel(
            getRepoCommitsUseCase,
            LcerState()
        )
        commitViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


        commitViewModel.fetchFirstPage(givenFirstPageCommitParams())


        coVerifySequence { getRepoCommitsUseCase(givenFirstPageCommitParams()) }
        coVerifySequence { fetchFirstPageStatesSequence() }
        confirmVerified(lifecycleOwner, subscriber, getRepoCommitsUseCase)
    }

    @Test
    fun `emit correct sequence of states when fetchFirstPage called and an exception returned`() {
        givenException()
        every { subscriber(any()) } just runs
        val commitViewModel = CommitViewModel(
            getRepoCommitsUseCase,
            LcerState()
        )
        commitViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


        commitViewModel.fetchFirstPage(givenFirstPageCommitParams())


        coVerifySequence { getRepoCommitsUseCase(givenFirstPageCommitParams()) }
        coVerifySequence {
            subscriber(LcerState(InitState(), Uninitialized))
            subscriber(LcerState(LoadingFirstPage<CommitResponse>(null), Loading()))
            subscriber(
                LcerState(
                    LoadFirstPageFailed<CommitResponse>(null, exception),
                    Fail(exception)
                )
            )
        }
        confirmVerified(lifecycleOwner, subscriber, getRepoCommitsUseCase)
    }

    @Test
    fun `emit correct sequence of states when fetchNextPage called and next pages returned`() {
            givenFirstPageCommitResponse()
            every { subscriber(any()) } just runs
            val commitViewModel = CommitViewModel(
                getRepoCommitsUseCase,
                LcerState()
            )
            commitViewModel.subscribe(owner = lifecycleOwner, subscriber = subscriber)


            commitViewModel.fetchFirstPage(givenFirstPageCommitParams())
            givenSecondPageCommitResponse()
            commitViewModel.fetchNextPage()


            coVerifySequence {
                getRepoCommitsUseCase(givenFirstPageCommitParams())
                getRepoCommitsUseCase(givenSecondPageCommitParams())
            }
            coVerifySequence {
                fetchFirstPageStatesSequence()
                fetchSecondPageStatesSequence()
            }
            confirmVerified(lifecycleOwner, subscriber, getRepoCommitsUseCase)
        }

    private fun givenFirstPageCommitParams(): CommitRequestParams =
        CommitRequestParams("owner", "repoName")

    private fun givenSecondPageCommitParams() = givenFirstPageCommitParams().copy(
        after = secondPage
    )

    private fun givenFirstPageCommitResponse() =
        coEvery { getRepoCommitsUseCase(any()) } returns CommitResponse(
            listOf(
                Commit(
                    "1",
                    null,
                    null,
                    null,
                    null,
                    null
                )
            ), secondPage
        )

    private fun givenSecondPageCommitResponse() =
        coEvery { getRepoCommitsUseCase(any()) } returns CommitResponse(
            listOf(
                Commit(
                    "2",
                    null,
                    null,
                    null,
                    null,
                    null
                )
            ), thirdPage
        )


    private fun givenException() =
        coEvery { getRepoCommitsUseCase(any()) } throws exception

    private fun expectedFirstPageResponse() = CommitResponse(
        listOf(
            Commit(
                "1",
                null,
                null,
                null,
                null,
                null
            )
        ), secondPage
    )

    private fun expectedSecondPageResponse() = CommitResponse(
        listOf(
            Commit(
                "1",
                null,
                null,
                null,
                null,
                null
            ),
            Commit(
                "2",
                null,
                null,
                null,
                null,
                null
            )
        ), thirdPage
    )

    private fun fetchFirstPageStatesSequence() {
        subscriber(LcerState(InitState(), Uninitialized))
        subscriber(LcerState(LoadingFirstPage<CommitResponse>(null), Loading()))
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
                Success(
                    CommitResponse(
                        listOf(Commit("2", null, null, null, null, null)),
                        thirdPage
                    )
                )
            )
        )
    }
}
