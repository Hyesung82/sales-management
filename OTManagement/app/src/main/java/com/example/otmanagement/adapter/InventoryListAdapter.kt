package com.example.otmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otmanagement.R
import com.example.otmanagement.data.Inventory

class InventoryListAdapter(val context: Context, val list: ArrayList<Inventory>)
    : RecyclerView.Adapter<InventoryListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false)
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
        val quantity = itemView.findViewById<TextView>(R.id.tvQuantity)

        fun bind (inventory: Inventory, context: Context) {
            name.text = inventory.name
            quantity.text = inventory.quantity.toString()
        }
    }
}