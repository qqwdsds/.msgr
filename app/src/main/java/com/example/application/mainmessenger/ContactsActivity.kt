package com.example.application.mainmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.application.Keys
import com.example.application.R
import com.example.application.databinding.ActivityContactsBinding
import com.example.application.models.User
import com.example.application.models.UserItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieAdapter

class ContactsActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityContactsBinding

    private val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back button
        findViewById<ImageView>(R.id.contacts_arrow_back).setOnClickListener {
            finish()
        }

        binding.recycleViewContacts.adapter = adapter

        getUsers()
    }


    // get users from the database and add them to the adapter
    private fun getUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                // get all users from database
                snapshot.children.forEach{
                    val user = it.getValue(User::class.java)

                    if(user != null)
                    {
                        if(user.userId != FirebaseAuth.getInstance().uid)
                        {
                            // add to the adapter
                            adapter.add(UserItem(user))
                        }
                    }
                }

                // start chat log activity
                adapter.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem
                    val i = Intent(this@ContactsActivity, ChatLogActivity::class.java)
                    i.putExtra(Keys.USER_KEY, userItem.user)
                    startActivity(i)

                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }
}