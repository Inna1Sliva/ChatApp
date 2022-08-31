package com.example.application.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.ChatActivity
import com.example.application.ChatTwoActivity
import com.example.application.R
import com.example.application.model.User
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(val context: Context, val userList:ArrayList<User>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text=user.userName
        holder.layoutUser.setOnClickListener {
            val intent= Intent(context, ChatTwoActivity::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("userName", user.userName)
            intent.putExtra("profileImage", user.profileImage)
            context.startActivity(intent)
        }

        Glide.with(context)
            .load(user.profileImage)
            .placeholder(R.drawable.profile_image)
            .into(holder.imgUser)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MessageViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.userName)
        val imgUser: CircleImageView = view.findViewById(R.id.imagePerson)
        val layoutUser: LinearLayout = view.findViewById(R.id.linearLayout)

    }
}