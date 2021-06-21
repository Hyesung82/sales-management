package com.example.otmanagement

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otmanagement.adapter.CategoryListAdapter
import com.example.otmanagement.data.Category
import com.example.otmanagement.databinding.ActivityProductInfoBinding

const val EXTRA_CATEGORY = "category"
const val EXTRA_QUERY = "category"

class ProductInfo : AppCompatActivity() {
    private lateinit var binding: ActivityProductInfoBinding
    var categoryList = arrayListOf<Category>(
        Category("CPU"), Category("Video Card"), Category("RAM"),
        Category("Mother Board"), Category("Storage")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_info)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.svProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(applicationContext, ItemByName::class.java).apply {
                    putExtra(EXTRA_QUERY, query)
                }
                startActivity(Intent(intent))
                return false
            }

        })

        val mAdapter = CategoryListAdapter(this, categoryList) {
            val intent = Intent(this, ItemByCategory::class.java).apply {
                putExtra(EXTRA_CATEGORY, it.name)
            }
            startActivity(Intent(intent))
        }
        binding.rvCategory.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.rvCategory.layoutManager = lm
        binding.rvCategory.setHasFixedSize(true)
    }
}