package com.pkliang.githubcommit.ui.user.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.pkliang.githubcommit.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commit_row.view.avatar
import kotlinx.android.synthetic.main.user_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class UserRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.user_row, this)
        setPadding(
            context.resources.getDimensionPixelOffset(R.dimen.padding),
            context.resources.getDimensionPixelOffset(R.dimen.padding),
            context.resources.getDimensionPixelOffset(R.dimen.padding),
            context.resources.getDimensionPixelOffset(R.dimen.padding)
        )
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    @TextProp
    fun setUserName(value: CharSequence) {
        user_name.text = value
    }

    @TextProp
    fun setName(value: CharSequence?) {
        name.visibility = if (value.isNullOrBlank()) View.GONE else View.VISIBLE
        name.text = value
    }

    @TextProp
    fun setEmail(value: CharSequence?) {
        email.visibility = if (value.isNullOrBlank()) View.GONE else View.VISIBLE
        email.text = value
    }

    @ModelProp
    fun setAvatar(url: String?) {
        avatar.visibility = View.VISIBLE
        Picasso.get().load(url).fit().placeholder(R.mipmap.ic_launcher).noFade().into(avatar)
    }

    @CallbackProp
    fun setClickListener(clickListener: OnClickListener?) {
        setOnClickListener(clickListener)
    }
}
