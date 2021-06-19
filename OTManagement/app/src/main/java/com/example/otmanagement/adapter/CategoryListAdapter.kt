package com.example.otmanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otmanagement.R
import com.example.otmanagement.data.Category

class CategoryListAdapter(val context: Context, val list: ArrayList<Category>, val itemClick: (Category) -> Unit)
    : RecyclerView.Adapter<CategoryListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], context)
    }

    inner class Holder(itemView: View, itemClick: (Category) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvCategory)

        fun bind (category: Category, context: Context) {
            name?.text = category.name

            itemView.setOnClickListener { itemClick(category) }
        }
    }
}