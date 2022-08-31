package com.example.application

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.application.databinding.ActivityProfileBinding
import com.example.application.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var uriImage:Uri?= null
    private var REQ_CODE = 2

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageProfile()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        binding.profImage.setOnClickListener {
            setImageUserProfile()
        }


        binding.beforNavigation.setOnClickListener {
            val home=Intent(this,HomeActivity::class.java)
            startActivity(home)
            finish()
        }
        binding.seveImage.setOnClickListener {
            storImageProfile()
            binding.barProgress.visibility = View.VISIBLE
        }
    }

    private fun imageProfile() {
        user=FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.uid)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(User::class.java)
                binding.Name.text = users!!.userName
                if (users.profileImage == ""){
                    binding.profImage.setImageResource(R.drawable.profile_image)
                }else{
                    Glide.with(this@ProfileActivity)
                        .load(users.profileImage)
                        .into(binding.profImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, error.message,Toast.LENGTH_SHORT).show()            }

        })
    }

    private fun storImageProfile() {

        if (uriImage !=null){
         val storage:StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            storage.putFile(uriImage!!)
                .addOnSuccessListener {
                    val hm:HashMap<String, String> = HashMap()
                    hm.put("userName", binding.Name.text.toString())
                    hm.put("profileImage",uriImage.toString())
                    databaseReference.updateChildren(hm as Map<String, Any>)
                    binding.barProgress.visibility = View.GONE
                    Toast.makeText(this,"Uploaded", Toast.LENGTH_SHORT).show()
                    binding.seveImage.visibility = View.GONE
                }
                .addOnFailureListener{
                    binding.barProgress.visibility =View.GONE
                    Toast.makeText(this,"Failed" + it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setImageUserProfile() {
        val intent:Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select Image"),REQ_CODE )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE && resultCode != null){
            uriImage = data!!.data
            try {
                val bitmap:Bitmap =MediaStore.Images.Media.getBitmap(contentResolver, uriImage)
                binding.profImage.setImageBitmap(bitmap)
                binding.seveImage.visibility = View.VISIBLE
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }


}