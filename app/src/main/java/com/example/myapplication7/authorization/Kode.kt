package com.example.myapplication7.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication7.Constants
import com.example.myapplication7.MainActivity
import com.example.myapplication7.R
import com.example.myapplication7.registration.Registration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Kode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kode)

        val edCode = findViewById<EditText>(R.id.edCode)
        val bundle: Bundle? = intent.extras
        val phone = bundle!!.getString("phone")

        var token: String
        var isUserInBD: Boolean = true
        var verificationCode: Int = 1234



        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val userLogin = mainApi.auth(
                AuthRequest(
                    phone.toString()
                )
            )
            runOnUiThread {
                token = userLogin.accessToken
                isUserInBD = userLogin.isUserInBD
                verificationCode = userLogin.verificationCode
                Toast.makeText(this@Kode,
                    "Введите код! КОД: ${verificationCode.toString()}", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonReturn: Button = findViewById(R.id.buttonReturn)
        buttonReturn.setOnClickListener {
            intent.putExtra("phone",  phone)
            setResult(RESULT_OK, intent)
            finish()
        }


        val buttonContinue: Button = findViewById(R.id.buttonContinue)
        buttonContinue.setOnClickListener {
            if(verificationCode.toString() == edCode.text.toString()){
                if(isUserInBD){

                    val intent1 = Intent(this@Kode, MainActivity::class.java)
                    startActivity(intent1)
                    finish()
                }
                else{
                    val intent = Intent(this@Kode, Registration::class.java)
                    intent.putExtra("phone",  phone.toString())
                    startActivity(intent)
                    finish()
                }

            }
            else{
                Toast.makeText(this@Kode,
                    "Введите код! КОД: ${verificationCode.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}