package com.example.application.mainmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.Keys
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
import com.squareup.picasso.Picasso
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

        setupStateBar()

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
        val fromId = FirebaseAuth.getInstance().uid
        val toId = intent.getParcelableExtra<User>(Keys.USER_KEY)?.userId

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        //listen all messages from database
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
            {
                val messageInfo = snapshot.getValue(MessageInfo::class.java)

                // add chat log RecyclerView
                if (messageInfo != null)
                {
                    if (messageInfo.fromId == FirebaseAuth.getInstance().uid)
                    {
                        adapter.add(RightMessageItem(messageInfo.text))
                    }
                    else{
                        val user = intent.getParcelableExtra<User>(Keys.USER_KEY)
                        if(user != null){
                            adapter.add(LeftMessageItem(messageInfo.text, user))
                        }
                        else {
                            Log.d(chatLogActivity, "Load user picture is failed")
                        }
                    }
                }

                // scroll RecyclerView to new messages
                binding.chatLogRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }

        })
    }

    private fun sendMessage()
    {
        // get sender id
        val fromId = FirebaseAuth.getInstance().uid
        if (fromId == null)
        {
            Log.d(chatLogActivity, "fromId == null")

            return
        }

        // get recipient id
        val user = intent.getParcelableExtra<User>(Keys.USER_KEY)
        val toId = user?.userId
        if (toId == null)
        {
            Log.d(chatLogActivity, "toId == null")

            return
        }

        // database folders reference for save user messages data
        val currentUserRef = FirebaseDatabase.getInstance().getReference("user-messages/$fromId/$toId").push()
        val toUserRef = FirebaseDatabase.getInstance().getReference("user-messages/$toId/$fromId").push()

        // message
        val message = binding.messageEditText.text.toString()

        if(message.isEmpty()) return

        // create MessageInfo object to load message to database
        val messageInfo = MessageInfo(currentUserRef.key!!, message, System.currentTimeMillis(), toId, fromId)

        // load message to database
        currentUserRef.setValue(messageInfo)
            .addOnSuccessListener {
                Log.d(chatLogActivity, "Message has been saved to database. Key: ${currentUserRef.key}")

                binding.messageEditText.text.clear()                                                  // clear EditText text
                binding.chatLogRecyclerView.scrollToPosition(adapter.itemCount - 1)          // scroll RecyclerView to the last message
            }

        toUserRef.setValue(messageInfo)

        // add latest message to a database
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(messageInfo)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(messageInfo)
    }

    private fun backButton()
    {
        // back button
        findViewById<ImageView>(R.id.chat_log_arrow_back).setOnClickListener {
            finish()
        }
    }

    private fun setupStateBar()
    {
        val user = intent.getParcelableExtra<User>(Keys.USER_KEY)

        findViewById<TextView>(R.id.chat_log_username).text = user!!.username

        val imageView = findViewById<ImageView>(R.id.chat_log_image)

        Picasso.get()
            .load(user.profileImageUrl)
            .fit()
            .centerInside()
            .into(imageView)
    }

    private fun initAdapter()
    {
        binding.chatLogRecyclerView.adapter = adapter
        binding.chatLogRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}