package com.example.application.mainmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.application.R
import com.example.application.databinding.ActivityMessagesBinding
import com.example.application.loginregister.RegisterActivity
import com.example.application.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MessagesActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMessagesBinding

    private val tag = "Messages Activity"
    // tag:"Messages Activity"


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        verifyThatUserIsLoggedIn()

        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button_logout = findViewById<ImageView>(R.id.button_logout)
        val button_info = findViewById<ImageView>(R.id.user_info)

        binding.buttonNewMessage.setOnClickListener {
            val i = Intent(this, ContactsActivity::class.java)
            startActivity(i)
        }

        button_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val registerActivity = Intent(this, RegisterActivity::class.java)
            registerActivity.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(registerActivity)
        }

        // show user info
        button_info.setOnClickListener {
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
}