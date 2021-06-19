package com.example.otmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityItemByNameBinding

class ItemByName : AppCompatActivity() {
    private lateinit var binding: ActivityItemByNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_by_name)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.svName.setQuery(intent.getStringExtra(EXTRA_QUERY), true)

        // TODO: 검색 recycler view filter
    }
}