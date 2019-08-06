package com.pkliang.githubcommit.ui.commit

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.pkliang.githubcommit.domain.commit.entity.CommitRequestParams
import com.pkliang.githubcommit.domain.commit.entity.CommitResponse
import com.pkliang.githubcommit.domain.commit.usecase.GetRepoCommitsUseCase
import com.pkliang.githubcommit.ui.core.LcerState
import com.pkliang.githubcommit.ui.core.MvRxViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CommitViewModel(
    private val getRepoCommitsUseCase: GetRepoCommitsUseCase,
    initialState: LcerState<CommitResponse>
) : MvRxViewModel<LcerState<CommitResponse>>(initialState) {

    private lateinit var commitRequestParams: CommitRequestParams

    fun fetchFirstPage(params: CommitRequestParams) = withState { state ->
        if (state.request is Loading) return@withState

        commitRequestParams = params
        viewModelScope.launch {
            getRepoCommitsUseCase.execute(commitRequestParams) { firstPageReducer(it) }
        }
    }

    fun fetchNextPage() = withState { state ->
        if (state.request is Loading) return@withState

        viewModelScope.launch {
            getRepoCommitsUseCase.execute(commitRequestParams.copy(after = state.viewState.data?.nextPage)) {
                nextPageReducer(it) {
                    viewState.data?.copy(
                        nextPage = it()?.nextPage,
                        commits = viewState.data?.commits?.plus(it()?.commits ?: emptyList())
                    )
                }
            }
        }
    }

    fun refresh() = withState { state ->
        if (state.request is Loading) return@withState

        viewModelScope.launch {
            getRepoCommitsUseCase.execute(commitRequestParams) { refreshReducer(it) }
        }
    }

    companion object : MvRxViewModelFactory<CommitViewModel, LcerState<CommitResponse>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: LcerState<CommitResponse>
        ): CommitViewModel {
            val getRepoCommitsUseCase: GetRepoCommitsUseCase by viewModelContext.activity.inject()
            return CommitViewModel(
                getRepoCommitsUseCase,
                state
            )
        }
    }
}
