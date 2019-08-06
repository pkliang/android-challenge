package com.pkliang.githubcommit

import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import com.pkliang.githubcommit.ui.commit.CommitFragment

class MainActivity : BaseMvRxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CommitFragment())
                .commitNow()
        }
    }
}
