package com.example.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.application.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select User"

    }
}