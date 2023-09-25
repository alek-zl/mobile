package com.example.myapplication7.shop

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.Camera.Camera
import com.example.myapplication7.Costants
import com.example.myapplication7.R
import java.math.RoundingMode

class ShopListActivity : AppCompatActivity() {
    private val adapter = ProductListAdapter()
    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productArrayList: ArrayList<Product>
    lateinit var title: Array<String>
    lateinit var price: Array<Float>
    lateinit var description: Array<String>
    lateinit var imageId: Array<Int>
    lateinit var productId: Array<Int>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        var check: TextView = findViewById(R.id.check)
        var change: Float
        title = arrayOf(
            "Кефир", "Йогурт DANONE", "Чипсы Lays", "Сосиськи Тавр", "Молоко"
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

        //начало фигни
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

        //конец фигни

        adapter.setOnItemClickListener(object: ProductListAdapter.onItemClickListener {
            override fun onItemClick(position: Int, idClick: Int) {

                /*Toast.makeText(this@ShopListActivity,
                    "Вы нажали на $position элемент списка", Toast.LENGTH_SHORT).show()*/
                if(idClick==0) {
                    val intent = Intent(this@ShopListActivity, ProductInfoActivity::class.java)
                    intent.putExtra("title", productArrayList[position].title)
                    intent.putExtra("price", productArrayList[position].price)
                    intent.putExtra("imageId", productArrayList[position].imageId)
                    intent.putExtra("description", productArrayList[position].description)
                    intent.putExtra("counter", productArrayList[position].counter)
                    intent.putExtra("position", position)
                    startActivityForResult(intent, Costants.REQUEST_CODE_DESCRIPTION)
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

            }



        })

        val buttonAdd: Button = findViewById(R.id.buttonAdd)
        buttonAdd.setOnClickListener {
            val intent = Intent(this@ShopListActivity, Camera::class.java)
            startActivityForResult(intent, Costants.REQUEST_CODE_CAMERA)

            /*val product = Product(title[0], price[0], imageId[0], description[0], 1)
            adapter.productAdd(product)
            productArrayList.add(product)
            check.text=(check.text.toString().toFloat()+productArrayList[0].price)
                .toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()*/
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var check: TextView = findViewById(R.id.check)
        if(requestCode== Costants.REQUEST_CODE_DESCRIPTION && resultCode== RESULT_OK && data!=null){
            Toast.makeText(this@ShopListActivity,
                "Изменения были приняты", Toast.LENGTH_SHORT).show()

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
        if(requestCode== Costants.REQUEST_CODE_CAMERA && resultCode== RESULT_OK){
            val product = Product(title[0], price[0], imageId[0], description[0], 1)
            adapter.productAdd(product)
            productArrayList.add(product)
            check.text=(check.text.toString().toFloat()+productArrayList[0].price)
                .toBigDecimal().setScale(2, RoundingMode.UP).toFloat().toString()
        }


    }



    /*private fun getUserdata() {

        for(i in title.indices){
            val product = Product(title[i], price[i])
            productArrayList.add(product)
        }

        productRecyclerView.adapter = ProductListAdapter(productArrayList)

    }*/


}