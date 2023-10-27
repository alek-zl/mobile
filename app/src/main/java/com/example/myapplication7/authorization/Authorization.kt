package com.example.myapplication7.authorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication7.R
import android.annotation.SuppressLint
import android.content.Intent

import android.widget.Button
import android.widget.EditText
import com.example.myapplication7.Constants

class Authorization : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)


        val b = findViewById<Button>(R.id.buttonLogin)
        val phone = findViewById<EditText>(R.id.edPhone)



        b.setOnClickListener {
            val intent = Intent(this@Authorization, Code::class.java)
            intent.putExtra("phone",  phone.text.toString())
            startActivityForResult(intent, Constants.REQUEST_CODE_LOGIN)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Constants.REQUEST_CODE_LOGIN && resultCode== RESULT_OK && data!=null){
            //Возможно нужно дописать
        }
        if(requestCode== Constants.REQUEST_CODE_REG && resultCode== RESULT_OK && data!=null){
            //Возможно нужно дописать
        }

    }
}