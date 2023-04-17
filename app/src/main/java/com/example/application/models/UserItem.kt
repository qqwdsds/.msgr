package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.ActivityContactsItemBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

// class for GroupieAdapter
class UserItem(val user: User): BindableItem<ActivityContactsItemBinding>()
{
    override fun bind(viewBinding: ActivityContactsItemBinding, position: Int)
    {
        viewBinding.usernameText.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewBinding.userImage)
    }

    override fun getLayout(): Int
    {
        return R.layout.activity_contacts_item
    }

    override fun initializeViewBinding(view: View): ActivityContactsItemBinding
    {
        return ActivityContactsItemBinding.bind(view)
    }

}