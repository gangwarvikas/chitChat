package com.example.chitchat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserListActivity : AppCompatActivity() {

    lateinit var recyclerView : RecyclerView
    lateinit var database : FirebaseDatabase
    lateinit var userList : ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        database = FirebaseDatabase.getInstance()
        userList = ArrayList()
        recyclerView = findViewById(R.id.userListRecyclerView)

        database.reference.child("user")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for(snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModel::class.java)
                        if(user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
                    recyclerView.adapter = ChatAdapter(this@UserListActivity,userList)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}