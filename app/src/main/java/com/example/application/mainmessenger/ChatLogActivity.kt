package com.example.application.mainmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.ActivityChatLogBinding
import com.example.application.models.LeftMessageItem
import com.example.application.models.MessageInfo
import com.example.application.models.RightMessageItem
import com.example.application.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieAdapter

class ChatLogActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityChatLogBinding

    private val adapter = GroupieAdapter()

    private val chatLogActivity = "ChatLogActivity"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setupActionBar()

        backButton()

        initAdapter()

        getMessages()

        binding.buttonSendMessage.setOnClickListener {
            Log.d(chatLogActivity, "Try to send message")

            sendMessage()
        }
    }

    private fun getMessages()
    {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        Log.d(chatLogActivity, "\"messages\" path: $ref")

        //listen all messages from database
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
            {
                val messageInfo = snapshot.getValue(MessageInfo::class.java)
                Log.d(chatLogActivity, "MessageInfo: ${messageInfo}")

                // add chat log RecyclerView
                if (messageInfo != null)
                {
                    if (messageInfo.fromId == FirebaseAuth.getInstance().uid)
                    {
                        adapter.add(RightMessageItem(messageInfo.text))
                    }
                    else{
                        adapter.add(LeftMessageItem(messageInfo.text))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }

        })
    }

    private fun sendMessage()
    {
        // database folder reference for save message data
        val ref = FirebaseDatabase.getInstance().getReference("messages").push()

        // message
        val message = binding.messageEditText.text.toString()

        // get sender id
        val fromId = FirebaseAuth.getInstance().uid
        if (fromId == null)
        {
            Log.d(chatLogActivity, "fromId == null")

            return
        }

        // get recipient id
        val user = intent.getParcelableExtra<User>(ContactsActivity.USER_KEY)
        val toId = user?.userId
        if (toId == null)
        {
            Log.d(chatLogActivity, "toId == null")

            return
        }

        // create MessageInfo object to load message to database
        val messageInfo = MessageInfo(ref.key!!, message, System.currentTimeMillis(), toId, fromId)

        // load message to database
        ref.setValue(messageInfo)
            .addOnSuccessListener {
                Log.d(chatLogActivity, "Message has been saved to database. Key: ${ref.key}")
            }
    }

    private fun backButton()
    {
        // back button
        findViewById<ImageView>(R.id.chat_log_arrow_back).setOnClickListener {
            finish()
        }
    }

    private fun setupActionBar()
    {
        val user = intent.getParcelableExtra<User>(ContactsActivity.USER_KEY)

        findViewById<TextView>(R.id.chat_log_username).text = user!!.username
    }

    private fun initAdapter()
    {
        binding.chatLogRecyclerView.adapter = adapter
        binding.chatLogRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}