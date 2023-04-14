package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.MessageRightBinding
import com.xwray.groupie.viewbinding.BindableItem

class RightMessageItem(val message: String): BindableItem<MessageRightBinding>()
{
    override fun bind(viewBinding: MessageRightBinding, position: Int)
    {
        viewBinding.chatMessageText.text = message
    }

    override fun getLayout(): Int {
        return R.layout.message_right
    }

    override fun initializeViewBinding(view: View): MessageRightBinding {
        return MessageRightBinding.bind(view)
    }
}