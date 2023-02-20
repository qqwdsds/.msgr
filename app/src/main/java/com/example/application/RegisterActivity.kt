package com.example.application

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.application.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: FirebaseDatabase

    private var _username = "username"
    private lateinit var _email: String
    private lateinit var _password: String

    private val TAG = "MainActivity"
    //tag:"MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        dataBase = Firebase.database

        binding.registerButton.setOnClickListener(::registerButton_OnClick)

        binding.alreadyHaveAccountTextView.setOnClickListener {
            Log.d("Main Activity", "Trying to login")
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerButton_OnClick(view: View)
    {
        _email = binding.emailEdit.text.toString()
        _password = binding.passwordEdit.text.toString()

        if(_email.isEmpty() || _password.isEmpty())
        {
            Toast.makeText(this, "Enter an email and password.",Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.usernameEdit.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show()
            return
        }
        _username = binding.usernameEdit.text.toString()

        // Firebase: add user
        auth.createUserWithEmailAndPassword(_email, _password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "createUserWithEmail:success, uid: ${it.result.user?.uid}")


                addUserToDatabase()

                // launch new activity
                val activityMessages = Intent(this, MessagesActivity::class.java)
                activityMessages.putExtra(Keys.USERNAME, _username)
                activityMessages.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(activityMessages)
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun addUserToDatabase()
    {
        val userid = auth.uid ?: ""
        val ref = dataBase.getReference("user/$userid")

        val user = User(userid, binding.usernameEdit.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "added user to storage")
            }
            .addOnFailureListener{
                Log.d(TAG,"user not added to storage. uid: $userid")
            }
    }
}