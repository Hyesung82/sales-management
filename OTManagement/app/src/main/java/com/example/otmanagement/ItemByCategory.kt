package com.example.otmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.otmanagement.databinding.ActivityItemByCategoryBinding

class ItemByCategory : AppCompatActivity() {
    private lateinit var binding: ActivityItemByCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_by_category)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvCategoryName.text = intent.getStringExtra(EXTRA_CATEGORY)

        // TODO: 카테고리에 따라 아이템 불러오기
    }


}