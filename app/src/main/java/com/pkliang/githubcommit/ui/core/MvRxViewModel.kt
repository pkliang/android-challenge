package com.pkliang.githubcommit.ui.core

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Success
import com.pkliang.githubcommit.BuildConfig
import com.pkliang.githubcommit.domain.core.UseCase

abstract class MvRxViewModel<S : MvRxState>(initialState: S) :
    BaseMvRxViewModel<S>(initialState, debugMode = BuildConfig.DEBUG) {
    suspend fun <T, P> UseCase<T, P>.execute(params: P, reducer: S.(Async<T>) -> S) {
        setState { reducer(Loading()) }
        runCatching {
            this(params)
        }.onSuccess {
            setState { reducer(Success(it)) }
        }.onFailure {
            if (it.stackTrace.isNullOrEmpty()) {
                val exception = RuntimeException(it.message, it)
                setState { reducer(Fail(exception)) }
            } else {
                setState { reducer(Fail(it)) }
            }
        }
    }
}
