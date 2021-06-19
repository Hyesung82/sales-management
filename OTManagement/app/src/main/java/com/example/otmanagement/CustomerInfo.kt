package com.example.otmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityCustomerInfoBinding

class CustomerInfo : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_info)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        // TODO: 고객 정보 filter
    }
}