package com.example.myapplication7.authorization

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication7.R

class Preview : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val b = findViewById<Button>(R.id.btnStart)

        b.setOnClickListener {
            val intent = Intent(this@Preview, Authorization::class.java)
            startActivity(intent)
            finish()
        }
    }
}