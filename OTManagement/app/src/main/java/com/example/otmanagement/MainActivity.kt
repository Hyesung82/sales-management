package com.example.otmanagement

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
            startActivity(Intent(this, CountriesByRegion::class.java))
        }
    }
}