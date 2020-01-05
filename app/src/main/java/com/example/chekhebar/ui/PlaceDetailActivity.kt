package com.example.chekhebar.ui

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.chekhebar.R
import com.example.chekhebar.core.di.ViewModelFactory
import com.example.chekhebar.data.Result
import com.example.chekhebar.databinding.ActivityPlaceDetailBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class PlaceDetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: ActivityPlaceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPlaceDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_place_detail)
        binding.placeDetailView = PlaceDetailView(
            "",
            "در حال بارگیری",
            0,
            "در حال بارگیری",
            0.0,
            "در حال بارگیری")
        val pieceId = intent.getStringExtra("placeId")
        val distance = intent.getIntExtra("distance", 1)
        val viewModel = viewModelFactory.create(MapViewModel::class.java)
        pieceId?.let {
            viewModel.getPlaceDetail(pieceId)
        }
        viewModel.placeDetail.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    val detail = result.data
                    detail?.distance = distance
                    binding.placeDetailView = detail
                }
                is Result.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}