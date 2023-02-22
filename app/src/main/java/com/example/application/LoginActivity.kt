package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.application.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var _email: String
    private lateinit var _password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signInButton.setOnClickListener(::signInButton_OnClick)
    }

    private fun signInButton_OnClick(view: View)
    {
        _email = binding.emailEdit.text.toString()
        _password = binding.passwordEdit.text.toString()

        if(_email.isEmpty() || _password.isEmpty())
        {
            Toast.makeText(this, "Enter an email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(_email, _password)
            .addOnCompleteListener{
                if(!it.isSuccessful) {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }

                Toast.makeText(this, "Successful sign in!", Toast.LENGTH_SHORT).show()

                // start messages activity
                val i = Intent(this, MessagesActivity::class.java)
                // close previous activity
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            }
    }
}