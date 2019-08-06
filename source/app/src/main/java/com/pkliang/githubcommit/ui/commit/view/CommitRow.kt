package com.pkliang.githubcommit.ui.commit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.pkliang.githubcommit.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.commit_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CommitRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.commit_row, this)
    }

    @TextProp
    fun setAuthor(autherName: CharSequence) {
        author.text = autherName
    }

    @TextProp
    fun setMessage(msg: CharSequence?) {
        message.visibility = if (msg.isNullOrBlank()) View.GONE else View.VISIBLE
        message.text = msg
    }

    @TextProp
    fun setCommittedDate(date: CharSequence?) {
        committedDate.visibility = if (date.isNullOrBlank()) View.GONE else View.VISIBLE
        committedDate.text = date
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
