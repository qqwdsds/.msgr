package com.example.application.loginregister

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.example.application.Keys
import com.example.application.mainmessenger.MessagesActivity
import com.example.application.R
import com.example.application.models.User
import com.example.application.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: FirebaseDatabase
    private lateinit var dataBaseStorage: FirebaseStorage

    private lateinit var _username: String
    private lateinit var _email: String
    private lateinit var _password: String
    private var _image: Uri? = null

    private val tag = "Register Activity"
    //tag:"MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }

        // Initialize Firebase Auth
        auth = Firebase.auth
        dataBase = Firebase.database
        dataBaseStorage = Firebase.storage

        // register button
        binding.registerButton.setOnClickListener {
            Log.d(tag, "Trying to register")

            completeRegister()
        }

        // "Already have an account?" functionality
        binding.alreadyHaveAccountTextView.setOnClickListener {
            Log.d(tag, "Trying to login")

            // launch login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // select photo button
        binding.selectPhotoButton.setOnClickListener {
            Log.d(tag, "Try to select photo")

            chooseImage.launch("image/*")
        }
    }

    // choose an image
    private val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            Log.d(tag, "Image has chosen")

            _image = it
            binding.selectPhotoButton.text = " "

            binding.selectPhotoImage.setImageURI(it)
            binding.selectPhotoButton.alpha = 0f

        }
    }

    //register button functionality
    private fun completeRegister() {
        _email = binding.emailEdit.text.toString()
        _password = binding.passwordEdit.text.toString()

        // First check: email and password
        if (_email.isEmpty() || _password.isEmpty()) {
            Toast.makeText(this, "Enter an email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        // Second check: username
        if (binding.usernameEdit.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show()
            return
        }
        _username = binding.usernameEdit.text.toString()

        //Third check: image
        if (_image == null) {
            Toast.makeText(this, "Choose a photo", Toast.LENGTH_SHORT).show()
            return
        }

        // turn on progressbar and turn off elements state
        binding.progressbar.visibility = View.VISIBLE
        elementState(false)

        // Firebase: add user
        auth.createUserWithEmailAndPassword(_email, _password) // reg user (email && password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(tag, "createUserWithEmail:success, uid: ${it.result.user?.uid}")

                uploadImageToDatabaseStorage() // download image to storage
            }
            .addOnFailureListener {
                // turn off progressbar and turn on elements state
                binding.progressbar.visibility = View.GONE
                elementState(true)

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    // change element state
    private fun elementState(state: Boolean){
        val layout = findViewById<ConstraintLayout>(R.id.registerLayout)
        for(view in layout.children){
            view.isEnabled = state
        }
    }

    private fun uploadImageToDatabaseStorage() {
        if (_image == null) return

        val imageName = UUID.randomUUID().toString()
        val ref = dataBaseStorage.getReference("images/$imageName")

        Log.d(tag, "Try to upload image to database storage")

        ref.putFile(_image!!)
            .addOnSuccessListener {
                Log.d(tag, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl
                    .addOnSuccessListener {
                        Log.d(tag, "Image Url $it")
                        
                        saveUserToDataBase(it.toString())
                    }
            }
    }

    private fun saveUserToDataBase(imageRef: String) {
        val uid = auth.uid
        val ref = dataBase.getReference("users/$uid")

        val user = User(uid!!, _username, imageRef)
        ref.setValue(user)
            .addOnSuccessListener{
                Log.d(tag, "User has been added to database")

                // start messages activity
                val i = Intent(this, MessagesActivity::class.java)
                // close previous activity
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.putExtra(Keys.USERNAME, _username)
                startActivity(i)
            }
            .addOnFailureListener{
                Log.e(tag, "$it")
            }
    }
}