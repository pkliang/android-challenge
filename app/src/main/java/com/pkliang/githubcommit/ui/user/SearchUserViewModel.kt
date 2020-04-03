package com.pkliang.githubcommit.ui.user

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.usecase.SearchUserUseCase
import com.pkliang.githubcommit.ui.core.LcerState
import com.pkliang.githubcommit.ui.core.MvRxViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SearchUserViewModel(
    private val searchUserUseCase: SearchUserUseCase,
    initialState: LcerState<SearchUserResponse>
) : MvRxViewModel<LcerState<SearchUserResponse>>(initialState) {

    private lateinit var searchUserRequestParams: SearchUserRequestParams

    fun fetchFirstPage(params: SearchUserRequestParams) = withState { state ->
        if (state.request is Loading) return@withState

        searchUserRequestParams = params
        viewModelScope.launch {
            searchUserUseCase.execute(searchUserRequestParams) { firstPageReducer(it) }
        }
    }

    fun fetchNextPage() = withState { state ->
        if (state.request is Loading) return@withState

        viewModelScope.launch {
            searchUserUseCase.execute(searchUserRequestParams.copy(after = state.viewState.data?.nextPage)) {
                nextPageReducer(it) {
                    viewState.data?.copy(
                        hasNextPage = it()?.hasNextPage,
                        nextPage = it()?.nextPage,
                        users = viewState.data?.users?.plus(it()?.users ?: emptyList())
                    )
                }
            }
        }
    }

    companion object : MvRxViewModelFactory<SearchUserViewModel, LcerState<SearchUserResponse>> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: LcerState<SearchUserResponse>
        ): SearchUserViewModel {
            val searchUserUseCase: SearchUserUseCase by viewModelContext.activity.inject()
            return SearchUserViewModel(
                searchUserUseCase,
                state
            )
        }
    }
}
