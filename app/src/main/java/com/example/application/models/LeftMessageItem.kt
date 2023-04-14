package com.example.application.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.databinding.MessageLeftBinding
import com.xwray.groupie.viewbinding.BindableItem

class LeftMessageItem(val message: String): BindableItem<MessageLeftBinding>()
{
    override fun bind(viewBinding: MessageLeftBinding, position: Int)
    {
        viewBinding.chatMessageText.text = message
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