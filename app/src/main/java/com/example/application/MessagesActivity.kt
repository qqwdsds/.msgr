package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.application.databinding.ActivityMessagesBinding
import com.google.firebase.auth.FirebaseAuth

class MessagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessagesBinding

    private val tag = "Messages Activity"
    // tag:"Messages Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyThatUserIsLoggedIn()

        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(Keys.USERNAME)
        //binding.usernameTextView.text = username
    }

    // if user not verify, this method return user to register page
    private fun verifyThatUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        Log.d(tag, "User Id: $uid")
        if (uid == null) {
            val registerActivity = Intent(this, RegisterActivity::class.java)
            registerActivity.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(registerActivity)
        }
    }

    // create options menu (top)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // option menu functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.new_message -> {
                val i = Intent(this, ContactsActivity::class.java)
                startActivity(i)
            }
            R.id.sing_out -> { // sign out and return to register activity
                FirebaseAuth.getInstance().signOut()
                val registerActivity = Intent(this, RegisterActivity::class.java)
                registerActivity.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(registerActivity)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}