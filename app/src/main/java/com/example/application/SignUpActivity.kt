package com.example.application

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.application.databinding.ActivitySignupBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

   private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginAccount.setOnClickListener {
            intentEnterActyvity()
        }
        binding.butSignUp.setOnClickListener {
            val userName = binding.yourName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            registUser(userName, email, password)
        }
    }
    private fun intentEnterActyvity() {
        val enterActivity = Intent(this,EnterActivity::class.java)
        startActivity(enterActivity)
        finish()
    }
    private fun registUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        val userId: String = user!!.uid
                        databaseUser( userId, userName)
                        intentHomeActivity()



                       // databaseReference = FirebaseDatabase.getInstance().getReference("User")
                        //  val userInformation = UserList(userId = userId, userName = name, profileImage = "")

                       // val hashMap: HashMap<String, String> = HashMap()
                       // hashMap.put("userId", userId)
                       // hashMap.put("userName", userName)
                        //hashMap.put("profileImage", "")
                       // databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                          //  Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()


                            //   } .addOnFailureListener {
                            // Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

                            // }

                       // }
                    }
                }

        }

    private fun intentHomeActivity() {
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun databaseUser( userId: String, userName: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId)
      //  val profileImage = imageUserRegistretion()
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("userId", userId)
        hashMap.put("userName", userName)
        hashMap.put("profileImage", "")
        databaseReference.setValue(hashMap).addOnCompleteListener {
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "Ошибка записи данных", Toast.LENGTH_SHORT).show()
        }

    }

   // private fun imageUserRegistretion(): Any {

   // }

}