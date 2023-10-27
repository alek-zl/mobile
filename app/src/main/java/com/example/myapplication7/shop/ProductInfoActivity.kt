package com.example.myapplication7.shop

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication7.R
import java.math.RoundingMode

class ProductInfoActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        val title: TextView = findViewById(R.id.tvTitle)
        val description: TextView = findViewById(R.id.tvInfo)
        val price: TextView = findViewById(R.id.tvPrice)
        val counter: TextView = findViewById(R.id.tvCounter)
        val image: ImageView = findViewById(R.id.Img)
        var priceChange: Float = 0f
        var counterChange: Int = 0
        var priceDifference: Float =0f

        val bundle: Bundle? = intent.extras
        val titleIntent = bundle!!.getString("title")
        val descriptionIntent = bundle.getString("description")
        val priceIntent = bundle.getFloat("price", 0f)
        val counterIntent = bundle.getInt("counter", 0)
        val imageStrBase64 =  bundle.getString("image").toString()
        val position: Int = bundle.getInt("position", 0)



        title.text = titleIntent
        description.text = descriptionIntent
        price.text = priceIntent.toString()
        priceChange = priceIntent
        counter.text = counterIntent.toString()
        counterChange = counterIntent
        image.setImageBitmap(decodePicString(imageStrBase64))
        priceDifference = priceIntent/counterIntent

        val tvPlus: TextView = findViewById(R.id.tvPlus)
        tvPlus.setOnClickListener {
            counterChange++
            priceChange = (priceChange+priceDifference).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
            price.text = priceChange.toString()
            counter.text = counterChange.toString()
        }
        val tvMinus: TextView = findViewById(R.id.tvMinus)
        tvMinus.setOnClickListener {
            if(counterChange!=1){
                counterChange--
                priceChange = (priceChange-priceDifference).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
                price.text = priceChange.toString()
                counter.text = counterChange.toString()
            }
        }

        val buttonAnswer: Button = findViewById(R.id.buttonAnswer)
        buttonAnswer.setOnClickListener {
            intent.putExtra("price", priceChange)
            intent.putExtra("counter", counterChange)
            intent.putExtra("position", position)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    fun decodePicString (encodedString: String): Bitmap {
        val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodedImage
    }

}