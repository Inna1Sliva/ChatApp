package com.example.application.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.ChatActivity
import com.example.application.R
import com.example.application.model.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val context: Context, val userList:ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text=user.userName
      holder.layoutUser.setOnClickListener {
          val intent=Intent(context,ChatActivity::class.java)
          intent.putExtra("userId", user.userId)
          intent.putExtra("userName", user.userName)
          intent.putExtra("profileImage", user.profileImage)
          context.startActivity(intent)
      }
     // holder.imgUser.setImageResource(R.drawable.ic_person)

        Glide.with(context)
            .load(user.profileImage)
            .placeholder(R.drawable.profile_image)
            .into(holder.imgUser)

    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val userName: TextView = view.findViewById(R.id.userName)
        val imgUser:CircleImageView = view.findViewById(R.id.imagePerson)
        val layoutUser: LinearLayout = view.findViewById(R.id.linearLayout)
    }
}