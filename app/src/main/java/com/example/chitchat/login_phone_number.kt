package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login_phone_number : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var phoneNumber : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_number)

        auth = FirebaseAuth.getInstance()
        phoneNumber = findViewById(R.id.userPhoneNumber)

        if(auth.currentUser != null){
            startActivity(Intent(this,UserListActivity::class.java))
            finish()
        }
        val sendOtp = findViewById<Button>(R.id.btnSendOtp)
        sendOtp.setOnClickListener {
            if(phoneNumber.text.isEmpty()){
                Toast.makeText(this, "Enter your Number", Toast.LENGTH_SHORT).show()
            }
            else{
                var intent = Intent(this,LoginOtp::class.java)
                intent.putExtra("number",phoneNumber.text.toString())
                startActivity(intent)
            }
        }
    }
}