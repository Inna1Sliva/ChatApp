package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.application.adapter.UserAdapter
import com.example.application.databinding.ActivityUsersBinding
import com.example.application.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UsersActivity : AppCompatActivity() {
    var userList = ArrayList<User>()
    private lateinit var binding: ActivityUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUsersList()
        binding.beforNavigation.setOnClickListener {
         navigationBefor()
        }
       // binding.imageProfile.setOnClickListener{
         //   profileList()
        //}

    }

    private fun profileList() {
        val profile = Intent(this,ProfileActivity::class.java)
        startActivity(profile)
        finish()
    }
    private fun navigationBefor() {
      val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
        finish()
    }
    fun getUsersList(){
        val firebase:FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference:DatabaseReference = FirebaseDatabase.getInstance().getReference("User")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
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
                recyclerVisibil()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UsersActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recyclerVisibil() {
        binding.recycler.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(this@UsersActivity, userList)
        binding.recycler.adapter = userAdapter

    }
}