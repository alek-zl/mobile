package com.example.myapplication7.authorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication7.R
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication7.Costants
import com.example.myapplication7.registration.Registration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Authorization : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)


        val b = findViewById<Button>(R.id.buttonLogin)
        val phone = findViewById<EditText>(R.id.edPhone)



        b.setOnClickListener {
            val intent = Intent(this@Authorization, Kode::class.java)
            intent.putExtra("phone",  phone.text.toString())
            startActivityForResult(intent, Costants.REQUEST_CODE_LOGIN)


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Costants.REQUEST_CODE_LOGIN && resultCode== RESULT_OK && data!=null){
            //Возможно нужно дописать
        }
        if(requestCode== Costants.REQUEST_CODE_REG && resultCode== RESULT_OK && data!=null){
            //Возможно нужно дописать
        }

    }
}