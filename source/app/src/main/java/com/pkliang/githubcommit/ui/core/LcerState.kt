package com.pkliang.githubcommit.ui.core

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized

data class LcerState<T>(
    /** We use this to store the view state. */
    val viewState: LcerViewState<T> = InitState(),
    /** We use this Async to keep track of the state of the current network request. */
    val request: Async<T> = Uninitialized
) : MvRxState {

    fun firstPageReducer(async: Async<T>): LcerState<T> = copy(
        request = async,
        viewState = when (async) {
            is Success -> PageLoaded(async())
            is Fail -> LoadFirstPageFailed(viewState.data, async.error)
            is Loading -> LoadingFirstPage(viewState.data)
            is Uninitialized ->
                LoadFirstPageFailed(viewState.data, IllegalStateException("firstPageReducer: $Uninitialized"))
        }
    )

    fun refreshReducer(async: Async<T>): LcerState<T> = copy(
        request = async,
        viewState = when (async) {
            is Success -> PageLoaded(async())
            is Fail -> RefreshFailed(viewState.data, async.error)
            is Loading -> Refreshing(viewState.data)
            is Uninitialized -> RefreshFailed(viewState.data, IllegalStateException("refreshReducer: $Uninitialized"))
        }
    )

    fun nextPageReducer(async: Async<T>, successReducer: () -> T?): LcerState<T> {
        return copy(
            request = async,
            viewState = when (async) {
                is Success -> PageLoaded(successReducer())
                is Fail -> LoadNextPageFailed(viewState.data, async.error)
                is Loading -> LoadingNextPage(viewState.data)
                is Uninitialized ->
                    LoadNextPageFailed(viewState.data, IllegalStateException("nextPageReducer: $Uninitialized"))
            }
        )
    }
}
