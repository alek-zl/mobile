package com.example.myapplication7

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.myapplication7.shop.ShopListActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonBegin = findViewById<Button>(R.id.buttonBegin)
        buttonBegin.setOnClickListener {
            val intent3 = Intent(this@MainActivity, ShopListActivity::class.java)
            startActivity(intent3)
            finish()
        }
        val buttonFinish: Button = findViewById(R.id.buttomFinish)
        buttonFinish.setOnClickListener {
            finish()
        }
    }
}