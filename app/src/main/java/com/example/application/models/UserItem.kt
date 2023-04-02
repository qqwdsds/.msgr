package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.MessageUserBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

class UserItem(val user: User): BindableItem<MessageUserBinding>()
{
    override fun bind(viewBinding: MessageUserBinding, position: Int)
    {
        viewBinding.usernameText.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewBinding.userImage)
    }

    override fun getLayout(): Int
    {
        return R.layout.message_user
    }

    override fun initializeViewBinding(view: View): MessageUserBinding
    {
        return MessageUserBinding.bind(view)
    }

}