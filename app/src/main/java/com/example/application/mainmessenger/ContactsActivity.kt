package com.example.application.mainmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.application.R
import com.example.application.databinding.ActivityContactsBinding
import com.example.application.models.User
import com.example.application.models.UserItem
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

        findViewById<ImageView>(R.id.arrow_back).setOnClickListener {
            finish()
        }

        binding.recycleViewContacts.adapter = adapter

        getUsers()
    }

    private fun getUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                // get all items from database
                snapshot.children.forEach{
                    val user = it.getValue(User::class.java)

                    if(user != null)
                    {
                        adapter.add(UserItem(user))
                    }

                }
            }

            override fun onCancelled(error: DatabaseError)
            {

            }

        })
    }
}