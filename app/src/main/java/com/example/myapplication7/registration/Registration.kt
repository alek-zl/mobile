package com.example.myapplication7.registration

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication7.MainActivity
import com.example.myapplication7.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.myapplication7.Constants.Companion.BASE_URL
class Registration : AppCompatActivity() {
    private lateinit var slist: ArrayList<String>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val bundle: Bundle? = intent.extras
        val phone = bundle!!.getString("phone")
        var gender: String= "Мужской"

        val buttonReturn: Button = findViewById(R.id.buttonReturnReg)
        buttonReturn.setOnClickListener {
            intent.putExtra("phone",  phone)
            setResult(RESULT_OK, intent)
            finish()
        }

        slist = arrayListOf()
        slist.add("Пол")
        slist.add("Мужской")
        slist.add("Женский")
        val spinnerAdapter = ArrayAdapter(this, androidx.camera.view.R.layout.support_simple_spinner_dropdown_item, slist)
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!=null){
                    gender = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(RegApi::class.java)

        val buttonContinue = findViewById<Button>(R.id.buttonContinueReg)
        val edData = findViewById<EditText>(R.id.edDate)
        val edName = findViewById<EditText>(R.id.edName)

        buttonContinue.setOnClickListener {
            if(gender=="Мужской" || gender=="Женский") {

                CoroutineScope(Dispatchers.IO).launch {
                    val userReg = mainApi.auth(
                        RegRequest(
                            phone.toString(),
                            edName.text.toString(),
                            gender,
                            edData.text.toString()
                        )
                    )
                    runOnUiThread {
                        val intent2 = Intent(this@Registration, MainActivity::class.java)
                        startActivity(intent2)
                        finish()
                    }
                }
            }
            else{
                Toast.makeText(this@Registration,
                    "Выберите пол!!!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}