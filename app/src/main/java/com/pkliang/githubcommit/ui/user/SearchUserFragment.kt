package com.pkliang.githubcommit.ui.user

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.fragmentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pkliang.githubcommit.R
import com.pkliang.githubcommit.domain.user.entity.SearchUserRequestParams
import com.pkliang.githubcommit.domain.user.entity.SearchUserResponse
import com.pkliang.githubcommit.domain.user.entity.User
import com.pkliang.githubcommit.ui.core.view.LoadingNextPageRowModel_
import com.pkliang.githubcommit.ui.core.view.emptyResultRow
import com.pkliang.githubcommit.ui.core.view.loadingFirstPageFailedRow
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
import com.pkliang.githubcommit.ui.user.view.userRow
import kotlinx.android.synthetic.main.fragment_search_user.*
import kotlinx.android.synthetic.main.user_detail_view.*

class SearchUserFragment : BaseFragment() {

    val args: SearchUserFragmentArgs by navArgs()
    private val searchUserViewModel: SearchUserViewModel by fragmentViewModel()

    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun epoxyController(): MvRxEpoxyController =
        simpleController(searchUserViewModel) { state ->
            when (state.viewState) {
                is InitState -> if (args.queryString.isNotEmpty()) {
                    searchUserViewModel.fetchFirstPage(SearchUserRequestParams(args.queryString))
                } else {
                    renderIntroPage()
                }
                is LoadingFirstPage -> renderLoadingFirstPage()
                is LoadFirstPageFailed -> renderLoadFirstPageFailed(state.viewState)
                is LoadNextPageFailed -> renderLoadNextPageFailed(state, state.viewState)
                is LoadingNextPage,
                is PageLoaded -> renderUsers(state)
            }
        }

    private fun EpoxyController.renderIntroPage() {
        emptyResultRow {
            id("emptyResultRow")
            title(getString(R.string.search_intro))
        }
    }

    private fun EpoxyController.renderLoadingFirstPage() {
        loadingFirstPageRow {
            id("loadingFirstPage")
        }
    }

    private fun EpoxyController.renderLoadFirstPageFailed(
        viewState: LoadFirstPageFailed<SearchUserResponse>
    ) {
        loadingFirstPageFailedRow {
            id("loadFirstPageFailed")
            errorMessage("Error: ${viewState.error.localizedMessage}")
            clickListener { _ ->
                args.queryString.let {
                    searchUserViewModel.fetchFirstPage(SearchUserRequestParams(it))
                }
            }
        }
    }

    private fun EpoxyController.renderLoadNextPageFailed(
        state: LcerState<SearchUserResponse>,
        viewState: LoadNextPageFailed<SearchUserResponse>
    ) {
        renderListing(state.viewState.data?.users ?: emptyList())

        loadingNextPageFailedRow {
            id("loadingNextPageFailedRow")
            errorMessage("Error: ${viewState.error.localizedMessage}")
            clickListener { _ -> searchUserViewModel.fetchNextPage() }
        }
    }

    private fun EpoxyController.renderUsers(state: LcerState<SearchUserResponse>) {
        val users = state.viewState.data?.users
        if (users.isNullOrEmpty()) {
            emptyResultRow {
                id("emptyResultRow")
                title(getString(R.string.no_user_found))
            }
        } else {
            renderListing(users)
        }

        LoadingNextPageRowModel_()
            .id("LoadingNextPageRowModel${users?.size}")
            .onBind { _, _, _ -> searchUserViewModel.fetchNextPage() }
            .addIf(state.viewState.data?.hasNextPage == true, this)
    }

    private fun EpoxyController.renderListing(items: List<User>) {
        items.forEachIndexed { index, user ->
            userRow {
                id(index)
                name(user.name)
                userName(user.userName ?: "")
                email(user.email)
                avatar(user.avatarUrl)
                clickListener { _ -> renderUserDetails(user) }
            }
        }
    }

    private fun renderUserDetails(user: User) {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        name_text_view.text = user.name ?: user.userName
        if (user.company != null) {
            company.visibility = View.VISIBLE
            company.text = user.company
        } else {
            company.visibility = View.GONE
            company.text = ""
        }
        if (user.location != null) {
            location.visibility = View.VISIBLE
            location.text = user.location
        } else {
            location.visibility = View.GONE
            location.text = ""
        }
        number_of_followers.text = getString(R.string.followers, user.followersCount)
        number_of_repos.text = getString(R.string.repositories, user.repositoriesCount)
        number_of_following.text = getString(R.string.following, user.followingsCount)
        number_of_pull_request.text = getString(R.string.pull_requests, user.pullRequestsCount)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(bottom_sheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_user, menu)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}
