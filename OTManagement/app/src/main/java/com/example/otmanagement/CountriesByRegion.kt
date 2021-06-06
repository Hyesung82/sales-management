package com.example.otmanagement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityCountriesByRegionBinding

class CountriesByRegion : AppCompatActivity() {
    private lateinit var binding: ActivityCountriesByRegionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_countries_by_region)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}