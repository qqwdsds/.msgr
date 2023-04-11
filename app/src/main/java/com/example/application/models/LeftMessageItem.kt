package com.example.application.models

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.application.R
import com.example.application.databinding.MessageLeftBinding
import com.xwray.groupie.viewbinding.BindableItem

class LeftMessageItem: BindableItem<MessageLeftBinding>()
{
    override fun bind(viewBinding: MessageLeftBinding, position: Int)
    {
        //TODO("Not yet implemented")
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