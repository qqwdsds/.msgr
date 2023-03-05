package com.example.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.ActivityMessagesBinding

class NewMessageAdapter : RecyclerView.Adapter<NewMessageAdapter.MessageHolder>()
{
    private val userList = ArrayList<User>()

    class MessageHolder(item: View) : RecyclerView.ViewHolder(item)
    {
        fun bind(user: User)
        {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_user, parent, false)
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