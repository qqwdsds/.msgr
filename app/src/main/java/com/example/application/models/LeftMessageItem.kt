package com.example.application.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.databinding.MessageLeftBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

// class for GroupieAdapter
class LeftMessageItem(val message: String, val user: User): BindableItem<MessageLeftBinding>()
{
    override fun bind(viewBinding: MessageLeftBinding, position: Int)
    {
        viewBinding.chatMessageText.text = message

        Picasso.get()
            .load(user.profileImageUrl)
            .into(viewBinding.chatUserImage)
    }

    override fun getLayout(): Int
    {
        return R.layout.message_left
    }

    override fun initializeViewBinding(view: View): MessageLeftBinding
    {
        return MessageLeftBinding.bind(view)
    }
}