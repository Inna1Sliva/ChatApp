package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.application.adapter.MessageAdapter
import com.example.application.adapter.UserAdapter
import com.example.application.databinding.ActivityHomeBinding
import com.example.application.databinding.ActivitySignupBinding
import com.example.application.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {
    var userList = ArrayList<User>()
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageProfile()

        binding.searchUser.setOnClickListener {
            val enterActivity = Intent(this, UsersActivity::class.java)
            startActivity(enterActivity)
            finish()
        }
        binding.imageProfile.setOnClickListener {
            val profile = Intent(this, ProfileActivity::class.java)
            startActivity(profile)
            finish()

        }
    }
        private fun imageProfile() {
            user= FirebaseAuth.getInstance().currentUser!!
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.uid)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(User::class.java)
                    //binding.Name.text = users!!.userName

                        Glide.with(this@HomeActivity)
                            .load(users!!.profileImage)
                            .into(binding.imageProfile)

                    readDataUserMessage()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HomeActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })
        }

    private fun readDataUserMessage() {
        val firebase:FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference:DatabaseReference = FirebaseDatabase.getInstance().getReference("UserMessage")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
               // val currentUser = snapshot.getValue(User::class.java)
                //  if (currentUser!!.profileImage == ""){
                //   binding.imageProfile.setImageResource(R.drawable.profile_image)
                // }else{
                //Glide.with(this@UsersActivity)
                // .load(currentUser.profileImage)
                // .into(binding.imageProfile)
                //   }
                for (dataSnapshot:DataSnapshot in snapshot.children){
                    val user=dataSnapshot.getValue(User::class.java)
                    if (!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }
                recyclerViewMessage()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recyclerViewMessage() {
        binding.recyclerMessage.layoutManager = LinearLayoutManager(this)
        val messageAdapter = MessageAdapter(this@HomeActivity, userList)
        binding.recyclerMessage.adapter = messageAdapter
    }
}
