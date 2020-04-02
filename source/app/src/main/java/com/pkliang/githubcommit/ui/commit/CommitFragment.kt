package com.pkliang.githubcommit.ui.commit

import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.fragmentViewModel
import com.pkliang.githubcommit.R
import com.pkliang.githubcommit.domain.commit.entity.Commit
import com.pkliang.githubcommit.domain.commit.entity.CommitRequestParams
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.ui.core.view.LoadingNextPageRowModel_
import com.pkliang.githubcommit.ui.commit.view.commitRow
import com.pkliang.githubcommit.ui.core.view.emptyResultRow
import com.pkliang.githubcommit.ui.core.view.loadingFirstPageRow
import com.pkliang.githubcommit.ui.core.view.loadingNextPageFailedRow
import com.pkliang.githubcommit.ui.core.BaseFragment
import com.pkliang.githubcommit.ui.core.InitState
import com.pkliang.githubcommit.ui.core.LcerState
import com.pkliang.githubcommit.ui.core.LoadFirstPageFailed
import com.pkliang.githubcommit.ui.core.LoadNextPageFailed
import com.pkliang.githubcommit.ui.core.LoadingFirstPage
import com.pkliang.githubcommit.ui.core.LoadingNextPage
import com.pkliang.githubcommit.ui.core.MvRxEpoxyController
import com.pkliang.githubcommit.ui.core.PageLoaded
import com.pkliang.githubcommit.ui.core.simpleController

class CommitFragment : BaseFragment() {

    private val commitViewModel: CommitViewModel by fragmentViewModel()

    private val firstPageCommitParams = CommitRequestParams(
        "aosp-mirror",
        "platform_build"
    )

    override fun epoxyController(): MvRxEpoxyController =
        simpleController(commitViewModel) { state ->

            when (state.viewState) {
                is InitState -> commitViewModel.fetchFirstPage(firstPageCommitParams)
                is LoadingFirstPage -> renderLoadingFirstPage()
                is LoadFirstPageFailed -> renderLoadFirstPageFailed(state.viewState)
                is LoadNextPageFailed -> renderLoadNextPageFailed(state, state.viewState)
                is LoadingNextPage,
                is PageLoaded -> renderCommits(state)
            }
        }

    private fun EpoxyController.renderLoadingFirstPage() {
        loadingFirstPageRow {
            id("loadingFirstPage")
        }
    }

    private fun EpoxyController.renderLoadFirstPageFailed(
        viewState: LoadFirstPageFailed<CommitResponse>
    ) {
        loadingNextPageFailedRow {
            id("loadFirstPageFailed")
            errorMessage("Error: ${viewState.error.localizedMessage}")
            clickListener { _ ->
                commitViewModel.fetchFirstPage(firstPageCommitParams)
            }
        }
    }

    private fun EpoxyController.renderLoadNextPageFailed(
        state: LcerState<CommitResponse>,
        viewState: LoadNextPageFailed<CommitResponse>
    ) {
        renderListing(state.viewState.data?.commits ?: emptyList())

        loadingNextPageFailedRow {
            id("loadingNextPageFailedRow")
            errorMessage("Error: ${viewState.error.localizedMessage}")
            clickListener { _ -> commitViewModel.fetchNextPage() }
        }
    }

    private fun EpoxyController.renderCommits(state: LcerState<CommitResponse>) {
        val commits = state.viewState.data?.commits
        if (commits.isNullOrEmpty()) {
            emptyResultRow {
                id("emptyResultRow")
                title(getString(R.string.no_commit))
            }
        } else {
            renderListing(commits)
        }

        LoadingNextPageRowModel_()
            .id("LoadingNextPageRowModel${commits?.size}")
            .onBind { _, _, _ -> commitViewModel.fetchNextPage() }
            .addIf(state.viewState.data?.nextPage != null, this)
    }

    private fun EpoxyController.renderListing(items: List<Commit>) {
        items.forEach { commit ->
            commitRow {
                id(commit.id)
                message(commit.message)
                author(commit.author ?: "")
                committedDate(commit.committedDate)
                avatar(commit.avatarUrl)
            }
        }
    }
}
