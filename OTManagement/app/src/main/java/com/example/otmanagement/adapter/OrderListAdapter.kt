package com.example.otmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otmanagement.R
import com.example.otmanagement.data.Order

class OrderListAdapter(val context: Context, val list: ArrayList<Order>, val itemClick: (Order) -> Unit)
    : RecyclerView.Adapter<OrderListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], context)
    }

    inner class Holder(itemView: View, itemClick: (Order) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.tvOrderId)
        val status = itemView.findViewById<TextView>(R.id.tvStatus)
        val salesman = itemView.findViewById<TextView>(R.id.tvSalesMan)
        val date = itemView.findViewById<TextView>(R.id.tvOrderDate)

        fun bind (order: Order, context: Context) {
            id.text = order.id.toString()
            status.text = order.status
            salesman.text = order.salesman
            date.text = order.date

            itemView.setOnClickListener { itemClick(order) }
        }
    }
}