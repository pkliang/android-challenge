package com.pkliang.githubcommit.ui.core

/**
 * This class models Loading-content-error-refresh states of a pagination list
 */
sealed class LcerViewState<T> {
    abstract val data: T?
}

data class InitState<T>(override val data: T? = null) : LcerViewState<T>()

data class LoadingFirstPage<T>(override val data: T?) : LcerViewState<T>()

data class LoadFirstPageFailed<T>(override val data: T?, val error: Throwable) :
    LcerViewState<T>()

data class LoadingNextPage<T>(override val data: T?) : LcerViewState<T>()
data class LoadNextPageFailed<T>(override val data: T?, val error: Throwable) : LcerViewState<T>()

data class Refreshing<T>(override val data: T?) : LcerViewState<T>()
data class RefreshFailed<T>(override val data: T?, val error: Throwable) : LcerViewState<T>()

data class PageLoaded<T>(override val data: T?) : LcerViewState<T>()
