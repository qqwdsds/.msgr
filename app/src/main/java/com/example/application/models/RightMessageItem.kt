package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.MessageRightBinding
import com.xwray.groupie.viewbinding.BindableItem

class RightMessageItem: BindableItem<MessageRightBinding>()
{
    override fun bind(viewBinding: MessageRightBinding, position: Int)
    {
        //TODO("Not yet implemented")
    }

    override fun getLayout(): Int {
        return R.layout.message_right
    }

    override fun initializeViewBinding(view: View): MessageRightBinding {
        return MessageRightBinding.bind(view)
    }
}