package com.example.myapplication7.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication7.R

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductHolder>(){
    val productList = ArrayList<Product>()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int, idClick: Int)

        /*fun onMinusClick(position: Int)
        fun onPlusClick(position: Int)*/

    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ProductHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvPlus: TextView = itemView.findViewById(R.id.Plus)
        val tvMinus: TextView = itemView.findViewById(R.id.tvCounter)

        init{

            tvTitle.setOnClickListener{
                listener.onItemClick(adapterPosition, 0)
            }

            tvPlus.setOnClickListener{
                listener.onItemClick(adapterPosition, 1)
            }

            tvMinus.setOnClickListener{
                listener.onItemClick(adapterPosition, 2)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
        parent, false)
        return  ProductHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val currentItem = productList[position]
        holder.tvTitle.text = currentItem.title
        holder.tvPrice.text = "${currentItem.price}"
    }

    fun productAdd(product: Product){
        productList.add(product)
        notifyDataSetChanged()
    }

    fun deleteItem(i: Int){

        productList.removeAt(i)
        notifyDataSetChanged()

    }
}