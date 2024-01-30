package com.example.chitchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chitchat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class messageAdapter(var context : Context, var list : ArrayList<MessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var ItemSent = 1
    var ItemReceive = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == ItemSent)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.send_item_layout, parent,false)
                )
                else ReceiverViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.receive_item_layout,parent,false)
                )
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid == list[position].senderId) ItemSent else ItemReceive
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = list[position]
        if(holder.itemViewType==ItemSent){
            val viewHolder = holder as SentViewHolder
            viewHolder.sendMessage.text = message.message
        }else{
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.receiveMessage.text = message.message
        }
    }

    class SentViewHolder(view : View)  : RecyclerView.ViewHolder(view){
        var sendMessage = view.findViewById<TextView>(R.id.sendText)
    }

    inner class ReceiverViewHolder(view : View)  : RecyclerView.ViewHolder(view){
        var receiveMessage = view.findViewById<TextView>(R.id.receiveText)
    }
}