package com.example.otmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityProductInfoBinding

class ProductInfo : AppCompatActivity() {
    private lateinit var binding: ActivityProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_info)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}