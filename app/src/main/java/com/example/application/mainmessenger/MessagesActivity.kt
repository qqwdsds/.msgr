package com.example.application.mainmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.Keys
import com.example.application.R
import com.example.application.databinding.ActivityMessagesBinding
import com.example.application.loginregister.RegisterActivity
import com.example.application.models.ChatItem
import com.example.application.models.MessageInfo
import com.example.application.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter

class MessagesActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMessagesBinding

    private val tag = "Messages Activity"
    // tag:"Messages Activity"

    private val adapter = GroupieAdapter()

    // save every latest message using userId key
    private val userMessagesMap = HashMap<String, MessageInfo>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        verifyThatUserIsLoggedIn()

        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adjustRecyclerView()

        getLatestMessages()

        binding.buttonNewMessage.setOnClickListener {
            val i = Intent(this, ContactsActivity::class.java)
            startActivity(i)
        }

        // show user info
        val button_info = findViewById<ImageView>(R.id.user_info)
        button_info.setOnClickListener {
            showUserInfo()
        }

        binding.navigationView.setNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.menu_sign_out -> logOut()
            }
            true
        }
    }

    private fun adjustRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener { item, _ ->
            val chatItem = item as ChatItem

            val i = Intent(this@MessagesActivity, ChatLogActivity::class.java)
            i.putExtra(Keys.USER_KEY, chatItem.getUser())
            startActivity(i)
        }
    }

    private fun refreshRecyclerView() {
        adapter.clear()
        userMessagesMap.values.forEach { message ->
            adapter.add(ChatItem(message))
        }
        if(adapter.itemCount < 1){
            binding.noMessagesText.visibility = View.VISIBLE
        }
        else{
            binding.noMessagesText.visibility = View.GONE
        }
    }

    // add all latest messages
    private fun getLatestMessages() {
        val curentUserUid = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$curentUserUid")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val messageInfo = snapshot.getValue(MessageInfo::class.java) ?: return

                userMessagesMap[snapshot.key!!] = messageInfo   // save latest message
                refreshRecyclerView()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val messageInfo = snapshot.getValue(MessageInfo::class.java) ?: return

                userMessagesMap[snapshot.key!!] = messageInfo   // save latest message
                refreshRecyclerView()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }

        })
    }

    // if user not verify, this method return user to register page
    private fun verifyThatUserIsLoggedIn()
    {
        val uid = FirebaseAuth.getInstance().uid
        Log.d(tag, "User Id: $uid")
        if (uid == null)
        {
            val registerActivity = Intent(this, RegisterActivity::class.java)
            registerActivity.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(registerActivity)
        }
    }

    // show user info in navigation drawer
    private fun showUserInfo() {
        findViewById<DrawerLayout>(R.id.drawler_layout).openDrawer(GravityCompat.START)
        val navigation_view_username = findViewById<TextView>(R.id.navigation_view_username)
        val navigation_view_profile_photo =
            findViewById<ImageView>(R.id.navigation_view_user_profile_photo)

        val user_uid = FirebaseAuth.getInstance().uid as String
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(user_uid).get()
            .addOnSuccessListener {
                val user = it.getValue(User::class.java)
                Log.d("MessagesActivity", "retrieving data successful: user: ${user?.username}")
                navigation_view_username.text = user?.username
                Picasso.get().load(user?.profileImageUrl).into(navigation_view_profile_photo)
            }
            .addOnFailureListener{
                Log.d("MessagesActivity", "retrieving data failure: ${it}")
            }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val registerActivity = Intent(this, RegisterActivity::class.java)
        registerActivity.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(registerActivity)
    }
}