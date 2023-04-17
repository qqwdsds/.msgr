package com.example.application.models

import android.view.View
import com.example.application.R
import com.example.application.databinding.ActivityMessagesItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

class ChatItem(val latestMessage: MessageInfo): BindableItem<ActivityMessagesItemBinding>()
{
    private lateinit var user: User
    fun getUser(): User{
        return user
    }
    override fun bind(viewBinding: ActivityMessagesItemBinding, position: Int)
    {
        viewBinding.latestMessagesText.text = latestMessage.text

        // get userId chat element using latest message
        val chatItemUserId = if(latestMessage.fromId == FirebaseAuth.getInstance().uid){
            latestMessage.toId
        } else
        {
            latestMessage.fromId
        }

        // get reference to a user using userId
        val ref = FirebaseDatabase.getInstance().getReference("users/$chatItemUserId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                user = snapshot.getValue(User::class.java) ?: return

                viewBinding.usernameText.text = user.username
                Picasso.get()
                    .load(user.profileImageUrl)
                    .into(viewBinding.userImage)
            }

            override fun onCancelled(error: DatabaseError) { }
        })
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
