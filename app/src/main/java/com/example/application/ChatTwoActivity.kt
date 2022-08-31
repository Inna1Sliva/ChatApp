package com.example.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.adapter.ChatAdapter
import com.example.application.databinding.ActivityChatTwoBinding
import com.example.application.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatTwoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser?=null
    var chatList = ArrayList<Chat>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding : ActivityChatTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        var senderId = auth.currentUser!!.uid

        val intent =getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")
        val profileImage = intent.getStringExtra("profileImage")
        var name = binding.userName
        name.text=userName

        binding.navBefor.setOnClickListener { navigationBefor() }
        binding.message.setOnClickListener {
            val message = binding.userMessage.text.toString()
            if (message.isEmpty()){
                Toast.makeText(this, "Пуст", Toast.LENGTH_SHORT).show()
                binding.userMessage.setText("")
            }else {
                messegeSend(senderId,userId!!,message)
                binding.userMessage.setText("")

            }
        }
        readMessage(senderId,userId!!)
    }

    private fun readMessage(senderId: String,receiverId: String) {
        databaseReference=FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot:DataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ){
                        chatList.add(chat)
                    }
                }
                viewRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatTwoActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewRecyclerView() {
        binding.chatRecycler.layoutManager=LinearLayoutManager(this)
        val chatAdapter = ChatAdapter(this,chatList)
        binding.chatRecycler.adapter =chatAdapter
    }


    private fun messegeSend(senderId: String, receiverId:String,message: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference()
        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId",receiverId)
        hashMap.put("message",message)
        databaseReference.child("Chat").push().setValue(hashMap)

    }

    private fun navigationBefor() {
        val homeActivity = Intent(this,HomeActivity::class.java)
        startActivity(homeActivity)
        finish()
    }
}