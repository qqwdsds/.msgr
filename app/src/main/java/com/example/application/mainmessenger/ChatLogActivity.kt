package com.example.application.mainmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.databinding.ActivityChatLogBinding
import com.example.application.models.LeftMessageItem
import com.example.application.models.RightMessageItem
import com.xwray.groupie.GroupieAdapter

class ChatLogActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityChatLogBinding

    private val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }

        // back button
        findViewById<ImageView>(R.id.chat_log_arrow_back).setOnClickListener {
            finish()
        }

        binding.chatLogRecyclerView.adapter = adapter
        binding.chatLogRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}