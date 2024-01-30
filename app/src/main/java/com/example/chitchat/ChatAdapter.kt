package com.example.chitchat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chitchat.model.UserModel

class ChatAdapter(private var context : Context, private var list : ArrayList<UserModel>) : RecyclerView.Adapter<ChatAdapter
    .ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.eachitem, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.image)
        holder.name.text = user.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("uid",user.uid)
            context.startActivity(intent)
        }
    }

    class ChatViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var image = itemView.findViewById<ImageView>(R.id.userImage)
        var name = itemView.findViewById<TextView>(R.id.userName)
    }
}