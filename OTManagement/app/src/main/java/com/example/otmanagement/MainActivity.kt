package com.example.otmanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityMainBinding


const val EXTRA_REGION = "region"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var selectedItem: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.ivMyInfo.setOnClickListener {
            startActivity(Intent(this, UserInfo::class.java))
        }

        binding.bProductInfo.setOnClickListener {
            startActivity(Intent(this, ProductInfo::class.java))
        }

        binding.bCustomerInfo.setOnClickListener {
            startActivity(Intent(this, CustomerInfo::class.java))
        }

        binding.bCheck.setOnClickListener {
            val intent = Intent(this, CountriesByRegion::class.java).apply {
                putExtra(EXTRA_REGION, selectedItem)
            }
            Log.d(TAG, "region num: $selectedItem")
            startActivity(intent)
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, REGIONS
        )
        binding.actvRegion.setAdapter(adapter)

        binding.actvRegion.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id->
//            val selectedItem = parent.getItemAtPosition(position).toString()
            selectedItem = position
        }
    }

    private val REGIONS = arrayOf(
        "Europe", "Americas", "Asia", "Middle East and Africa"
    )
}