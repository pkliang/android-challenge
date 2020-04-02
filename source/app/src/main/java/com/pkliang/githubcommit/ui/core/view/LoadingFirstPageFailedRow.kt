package com.pkliang.githubcommit.ui.core.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.pkliang.githubcommit.R
import kotlinx.android.synthetic.main.text_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class LoadingFirstPageFailedRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.text_row, this)
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
