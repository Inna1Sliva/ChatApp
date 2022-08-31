package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.application.databinding.ActivityEnterBinding
import com.google.firebase.auth.FirebaseAuth
import java.net.Authenticator

class EnterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private  lateinit var binding: ActivityEnterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.butEnter.setOnClickListener {
            AuthenticationUser()
        }

    }

    private fun AuthenticationUser() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        if (email.isEmpty()){
            Toast.makeText(this, "Email entered incorrectly",Toast.LENGTH_SHORT).show()
                //binding.email.requestFocus()
        }else{
            binding.email.requestFocus()
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Password entered incorrectly",Toast.LENGTH_SHORT).show()
          //  binding.password.requestFocus()
        }else{
            binding.password.requestFocus()
        }

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            Toast.makeText(this, "Email and password are required",Toast.LENGTH_SHORT).show()

        }else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        val homeActivity = Intent(this, HomeActivity::class.java)
                        startActivity(homeActivity)
                        finish()
                    }else{
                        Toast.makeText(this, "Email or password invalid",Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
}