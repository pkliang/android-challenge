package com.pkliang.githubcommit.ui.commit.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.pkliang.githubcommit.R
import kotlinx.android.synthetic.main.text_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingNextPageFailedRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.text_row, this)
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }

    @TextProp
    fun setErrorMessage(errorMessage: CharSequence) {
        title.text = errorMessage
    }

    @CallbackProp
    fun setClickListener(clickListener: OnClickListener?) {
        retry.setOnClickListener(clickListener)
    }
}
