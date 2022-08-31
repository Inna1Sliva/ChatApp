package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.application.adapter.ChatAdapter
import com.example.application.databinding.ActivityChatBinding
import com.example.application.model.Chat
import com.example.application.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //private var firebaseUser:FirebaseUser?= null
    var chatList = ArrayList<Chat>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        var senderId = auth.currentUser!!.uid

         val intent =getIntent()
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profileImage = intent.getStringExtra("profileImage")
        val name = binding.userName
        //val image=binding.imageProfile
        name.text=userName

        seveMessagaUser(userId!!, userName!!,profileImage!!)

        binding.message.setOnClickListener {
            val message = binding.userMessage.text.toString()
            if (message.isEmpty()){
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
           binding.userMessage.setText("")
            }else{
                sendMessage(senderId, userId!!, message)
                binding.userMessage.setText("")

            }
             }
        readMessage(senderId, userId)


        binding.imageProfile.setOnClickListener { imageProfile() }

        binding.navBefor.setOnClickListener { buttonNavigation() }


    }

    private fun readMessage(senderId: String, receiverId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }
                binding.chatMessage.layoutManager = LinearLayoutManager(this@ChatActivity)
                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                binding.chatMessage.adapter = chatAdapter
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun sendMessage(senderId:String, receiverId:String, message:String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat").child(senderId)
        val hashMap: HashMap<String,String> = HashMap()
        hashMap.put("senderId",senderId)
        hashMap.put("receiverId",receiverId)
        hashMap.put("message",message)
        databaseReference.setValue(hashMap).addOnCompleteListener {
            Toast.makeText(this, "Сообщение отправленно", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "Ошибка записи данных", Toast.LENGTH_SHORT).show()
        }

    }

    private fun seveMessagaUser(userId: String, userName:String,imageProfile: String ) {
        databaseReference = FirebaseDatabase.getInstance().getReference("UserMessage").child(userId)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("userId", userId)
        hashMap.put("userName", userName)
        hashMap.put("profileImage", imageProfile)
        databaseReference.setValue(hashMap).addOnCompleteListener {
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "Ошибка записи данных", Toast.LENGTH_SHORT).show()
        }

    }

    private fun imageProfile() {
        val homeActivity = Intent(this,ProfileActivity::class.java)
        startActivity(homeActivity)
    }

    private fun buttonNavigation() {
        val homeActivity = Intent(this,UsersActivity::class.java)
        startActivity(homeActivity)

    }
}