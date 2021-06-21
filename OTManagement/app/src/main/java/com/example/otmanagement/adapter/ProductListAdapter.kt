package com.example.otmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otmanagement.R
import com.example.otmanagement.data.Product

class ProductListAdapter(val context: Context, val list: ArrayList<Product>)
    : RecyclerView.Adapter<ProductListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvProductName)
        val desc = itemView.findViewById<TextView>(R.id.tvProductDesc)
        val cost = itemView.findViewById<TextView>(R.id.tvCost)
        val price = itemView.findViewById<TextView>(R.id.tvPrice)

        fun bind (product: Product, context: Context) {
            name?.text = product.name
            desc.text = product.desc
            cost.text = product.cost.toString()
            price.text = product.price.toString()
        }
    }
}