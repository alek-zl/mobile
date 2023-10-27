package com.example.myapplication7.shop

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.Camera.Camera
import com.example.myapplication7.Constants
import com.example.myapplication7.Pay
import com.example.myapplication7.R
import com.example.myapplication7.scanner.Scan
import com.example.myapplication7.scanner.ScanApi
import com.example.myapplication7.scanner.ScanRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode

class ShopListActivity : AppCompatActivity() {
    private val adapter = ProductListAdapter()
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    private var requestCamera: ActivityResultLauncher<String>?=null
    lateinit var title: Array<String>
    lateinit var price: Array<Float>
    lateinit var description: Array<String>
    lateinit var imageId: Array<Int>
    lateinit var productId: Array<Int>
    var couterProduct: Int = 0
    var change: Float = 0f

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        var check: TextView = findViewById(R.id.check)

        title = arrayOf(
            "Кефир", "Йогурт", "Чипсы Lays", "Сосиськи", "Молоко"
        )
        price = arrayOf(78.55f, 50.99f, 159.90f, 86.90f, 96.99f)

        imageId = arrayOf(
            R.drawable.picture1,
            R.drawable.picture2,
            R.drawable.picture3,
            R.drawable.picture4,
            R.drawable.picture5
        )

        description = arrayOf(
            getString(R.string.text_picture1),
            getString(R.string.text_picture2),
            getString(R.string.text_picture3),
            getString(R.string.text_picture4),
            getString(R.string.text_picture5)

        )


        productRecyclerView = findViewById(R.id.rcView)
        productRecyclerView.layoutManager = LinearLayoutManager(this)
        productRecyclerView.setHasFixedSize(true)

        productArrayList = arrayListOf<Product>()
        productRecyclerView.adapter = adapter


        val swipegesture = object : SwipeGesture(this){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction){
                    ItemTouchHelper.LEFT ->{
                        adapter.deleteItem(viewHolder.adapterPosition)
                    }
                }

            }

        }


        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(productRecyclerView)


        adapter.setOnItemClickListener(object: ProductListAdapter.onItemClickListener {
            override fun onItemClick(position: Int, idClick: Int) {

                /*Toast.makeText(this@ShopListActivity,
                    "Вы нажали на $position элемент списка", Toast.LENGTH_SHORT).show()*/
                if(idClick==0) {
                    val intent = Intent(this@ShopListActivity, ProductInfoActivity::class.java)
                    intent.putExtra("title", productArrayList[position].title)
                    intent.putExtra("price", productArrayList[position].price)
                    intent.putExtra("image", productArrayList[position].image)
                    intent.putExtra("description", productArrayList[position].description)
                    intent.putExtra("counter", productArrayList[position].counter)
                    intent.putExtra("position", position)
                    startActivityForResult(intent, Constants.REQUEST_CODE_DESCRIPTION)
                }
                if(idClick==1){

                    change= (productArrayList[position].price/productArrayList[position].counter)
                    productArrayList[position].counter++
                    productArrayList[position].price = (productArrayList[position].price+change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

                    productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.tvPrice).text =
                        productArrayList[position].price.toString()

                    productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.Counter).text =
                        productArrayList[position].counter.toString()

                    check.text=(check.text.toString().toFloat()+change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()
                }
                if(idClick==2 && productArrayList[position].counter!=1){

                    change= (productArrayList[position].price/productArrayList[position].counter).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()
                    productArrayList[position].counter--
                    productArrayList[position].price = (productArrayList[position].price-change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

                    productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.tvPrice).text =
                        productArrayList[position].price.toString()

                    productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.Counter).text =
                        productArrayList[position].counter.toString()

                    check.text=(check.text.toString().toFloat()-change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()
                }
                /*if(idClick==2 && productArrayList[position].counter==1){

                        adapter.deleteItem(position)
                        productArrayList.removeAt(position)

                }*/
            }
        })


        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            if(allPermissionGranted()) {
                val intent = Intent(this, Scan::class.java)
                startActivityForResult(intent, Constants.REQUEST_CODE_SCANNER)
            }
            else{
                ActivityCompat.requestPermissions(this,
                    Constants.REQUEST_PERMISSIONS,
                    Constants.REQUEST_CODE_PERMISSIONS
                )
            }
        }

        val buttonPay: Button = findViewById(R.id.buttonPay)
        buttonPay.setOnClickListener {
            val intent4 = Intent(this@ShopListActivity, Pay::class.java)
            startActivity(intent4)
            finish()
        }

    }
    private fun allPermissionGranted() =
        Constants.REQUEST_PERMISSIONS.all{
            ContextCompat.checkSelfPermission(baseContext, it)== PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.REQUEST_CODE_PERMISSIONS){
            if(allPermissionGranted()){
                val intent = Intent(this, Scan::class.java)
                startActivityForResult(intent, 100)
            }else{
                Toast.makeText(this, "Permission not granted by user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val scanApi = retrofit.create(ScanApi::class.java)


        var check: TextView = findViewById(R.id.check)
        if(requestCode== Constants.REQUEST_CODE_DESCRIPTION && resultCode== RESULT_OK && data!=null){
            /*Toast.makeText(this@ShopListActivity,
                "Изменения были приняты", Toast.LENGTH_SHORT).show()*/

            var priceDescription: Float = data.getFloatExtra("price", 0f)
            val position: Int = data.getIntExtra("position", 0)
            val counter: Int = data.getIntExtra("counter", 0)
            if(priceDescription!=0f && counter!=0){
                var counterTMP =productArrayList[position].counter
                var priceTMP = productArrayList[position].price
                productArrayList[position].counter = counter
                productArrayList[position].price = (productArrayList[position].price/counterTMP*productArrayList[position].counter)
                    .toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

                productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.tvPrice).text =
                    productArrayList[position].price.toString()

                productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.Counter).text =
                    productArrayList[position].counter.toString()

                check.text=(check.text.toString().toFloat()-priceTMP+productArrayList[position].price)
                    .toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()

            }

        }
        if(requestCode== Constants.REQUEST_CODE_SCANNER && resultCode== 555 && data!=null){

            var intentData: String = data.getStringExtra("barcode").toString()
            var title: String =""
            var price: Float = 0f
            var image : String =""
            var description: String =""


            CoroutineScope(Dispatchers.IO).launch {
                val scanAnswer = scanApi.auth(
                    ScanRequest(
                        intentData
                    )
                )
                runOnUiThread {

                    var id = scanAnswer.id
                     title = scanAnswer.title
                     price = scanAnswer.price
                     image = scanAnswer.image
                     description = scanAnswer.description
                    var position = isProductInArray(productArrayList,title)

                    if(position==100) {

                        val product = Product(title, price, image, description, 1)

                        adapter.productAdd(product)
                        productArrayList.add(product)
                        check.text = (check.text.toString().toFloat() + productArrayList[couterProduct].price)
                            .toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()
                        couterProduct++

                    }
                    if(position!=100){
                        change= (productArrayList[position].price/productArrayList[position].counter)
                        productArrayList[position].counter++
                        productArrayList[position].price = (productArrayList[position].price+change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat()

                        productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.tvPrice).text =
                            productArrayList[position].price.toString()

                        productRecyclerView.getChildAt(position).findViewById<TextView>(R.id.Counter).text =
                            productArrayList[position].counter.toString()
                        check.text=(check.text.toString().toFloat()+change).toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()
                    }



                }
            }




        }


    }





    private fun isProductInArray(array: ArrayList<Product>, titleStr: String): Int {
        var index: Int = 100
        for(i in array.indices){
            if(array[i].title == titleStr){
                index = i
            }
        }
        return index
    }


}