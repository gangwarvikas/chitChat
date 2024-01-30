package com.example.chitchat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chitchat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class profile : AppCompatActivity() {
    private lateinit var continuebtn : Button
    private lateinit var userNameField : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var selectImg : Uri
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var userImage : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //initialise value
        continuebtn = findViewById(R.id.btnContinue)
        userNameField = findViewById(R.id.userName)
        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile..")
            .setCancelable(false)
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        userImage = findViewById(R.id.profileImage)

        //setProfile Image
        userImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        continuebtn.setOnClickListener {
//            Toast.makeText(this,"you clicked",Toast.LENGTH_SHORT).show()
            if(userNameField.text.isEmpty()){
                Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show()
            }else if(selectImg==null){
                Toast.makeText(this,"Please select your image",Toast.LENGTH_SHORT).show()
            }else{
                uploadData()
            }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            if(data.data != null){
                selectImg = data.data!!

                userImage.setImageURI(selectImg)
            }
        }
    }

    private fun uploadData(){
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectImg).addOnCompleteListener {
            if(it.isSuccessful){
                reference.downloadUrl.addOnFailureListener { task->
                    uploadInfo(task.toString())
                }
            }
        }
    }
    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(auth.uid.toString(), userNameField.text.toString(), auth.currentUser!!.phoneNumber.toString(), imgUrl)

        // Adjust the database reference path to match your actual database structure
        val userRef = database.reference.child("user").child(auth.uid.toString())

        userRef.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, UserListActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to insert data ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}