package com.example.otmanagement

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityWarehouseDetailBinding

class WarehouseDetail : AppCompatActivity() {
    lateinit var binding: ActivityWarehouseDetailBinding
    val TAG = "WarehouseDetail"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_warehouse_detail)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        Log.d(TAG, "extra_warehouse: ${intent.getStringExtra(EXTRA_WAREHOUSE)}")
        binding.tvWarehouseTitle.text = intent.getStringExtra(EXTRA_WAREHOUSE)

        // TODO: 공장 정보
    }
}