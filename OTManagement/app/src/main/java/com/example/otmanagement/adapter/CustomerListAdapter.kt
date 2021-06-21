package com.example.otmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otmanagement.R

class CustomerListAdapter(val context: Context, val list: ArrayList<String>, val itemClick: (String) -> Unit)
    : RecyclerView.Adapter<CustomerListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.customer_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], context)
    }

    inner class Holder(itemView: View, itemClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvCustomer)

        fun bind (customer: String, context: Context) {
            name?.text = customer

            itemView.setOnClickListener { itemClick(customer) }
        }
    }
}