package com.pkliang.githubcommit.ui.commit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.pkliang.githubcommit.R
import kotlinx.android.synthetic.main.text_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class EmptyResultRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.text_row, this)
        retry.visibility = View.GONE
        orientation = VERTICAL
    }

    @TextProp
    fun setTitle(titleChar: CharSequence) {
        title.text = titleChar
    }

    @TextProp
    fun setSubtitle(subtitleChar: CharSequence?) {
        subtitle.visibility = if (subtitleChar.isNullOrBlank()) View.GONE else View.VISIBLE
        subtitle.text = subtitleChar
    }
}
