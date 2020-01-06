package com.example.chekhebar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chekhebar.R
import com.example.chekhebar.databinding.ActivityPlaceDetailBinding
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlaceDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        binding.placeView = PlaceView(
            intent.getStringExtra("placeId") ?: "",
            intent.getStringExtra("name") ?: "",
            intent.getIntExtra("distance", 1),
            intent.getStringExtra("address") ?: "",
            intent.getStringExtra("category") ?: ""
        )
        iv_back.setOnClickListener {
            onBackPressed()
        }
    }
}