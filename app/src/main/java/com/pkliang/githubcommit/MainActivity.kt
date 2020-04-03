package com.pkliang.githubcommit

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.navigation.findNavController
import com.airbnb.mvrx.BaseMvRxActivity
import com.pkliang.githubcommit.ui.user.SearchUserFragmentDirections
import com.pkliang.githubcommit.ui.user.UserSearchSuggestionProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvRxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                title = getString(R.string.search_user, query)
                SearchRecentSuggestions(
                    this,
                    UserSearchSuggestionProvider.AUTHORITY,
                    UserSearchSuggestionProvider.MODE
                )
                    .saveRecentQuery(query, null)
                val action = SearchUserFragmentDirections.actionSearchUserFragmentSelf(query)
                findNavController(R.id.nav_host_fragment).navigate(action)
            }
        }
    }
}
