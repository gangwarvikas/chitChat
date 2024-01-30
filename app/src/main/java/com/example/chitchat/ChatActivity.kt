package com.example.chitchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var send : ImageView
    private lateinit var message : EditText
    private lateinit var database : FirebaseDatabase
    private lateinit var senderUid : String
    private lateinit var receiverUid : String

    private lateinit var senderRoom : String
    private lateinit var receiverRoom : String

    private lateinit var recycle : RecyclerView

    private lateinit var list : ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //initialise variable by id
        send = findViewById(R.id.sendicon)
        message = findViewById(R.id.messageBox)
        database = FirebaseDatabase.getInstance()
        list=ArrayList()
        recycle = findViewById(R.id.recyclerView)

        //getting user id
        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!

        senderRoom = senderUid+receiverUid
        receiverRoom = receiverUid+senderUid

        send.setOnClickListener {
            if(message.text.isEmpty()){
                Toast.makeText(this,"please Enter Your Message",Toast.LENGTH_LONG).show()
            } else{
                val currentMessage = MessageModel(message.text.toString(), senderUid, Date().time)
                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(currentMessage)
                    .addOnSuccessListener {

                        database.reference.child("chats")
                            .child(receiverRoom)
                            .child("message").child(randomKey!!)
                            .setValue(currentMessage).addOnSuccessListener {
                                message.text = null
                                Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for(snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    recycle.adapter = messageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"error : $error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}