package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.ActivityMessagesBinding
import com.example.application.databinding.MessageUserBinding

class NewMessageAdapter : RecyclerView.Adapter<NewMessageAdapter.MessageHolder>()
{
    private val userList = ArrayList<User>()

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val binding = MessageUserBinding.bind(view)

        fun bind(user: User)
        {
            binding.usernameText.text = user.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.message_user, parent, false)
        return MessageHolder(view)
    }

    override fun getItemCount(): Int
    {
        return userList.size
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int)
    {
        holder.bind(userList[position])
    }

    fun addUser(user: User)
    {
        userList.add(user)
        notifyDataSetChanged()
    }
}