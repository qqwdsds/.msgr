package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.ActivityMessagesItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class ChatItem(val latestMessage: MessageInfo): BindableItem<ActivityMessagesItemBinding>()
{
    override fun bind(viewBinding: ActivityMessagesItemBinding, position: Int)
    {
        viewBinding.latestMessagesText.text = latestMessage.text
    }

    override fun getLayout(): Int
    {
        return R.layout.activity_messages_item
    }

    override fun initializeViewBinding(view: View): ActivityMessagesItemBinding
    {
        return ActivityMessagesItemBinding.bind(view)
    }
}
