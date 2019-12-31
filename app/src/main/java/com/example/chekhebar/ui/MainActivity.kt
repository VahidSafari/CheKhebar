package com.example.chekhebar.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chekhebar.R
import com.example.chekhebar.core.di.ViewModelFactory
import com.example.chekhebar.data.Result
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_REQUEST = 161
    private lateinit var placeAdapter: PlaceAdapter
    lateinit var viewModel: MapViewModel
    private val limit = 20
    private var offset = 0

    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swl_places.isRefreshing = true
        viewModel = viewModelFactory.create(MapViewModel::class.java)

        rv_places.layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter()
        rv_places.adapter = placeAdapter
        rv_places.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        rv_places.addOnScrollListener(object : PaginationScrollListener(LinearLayoutManager(this)){
            override var isLastPage: Boolean
                get() = this@MainActivity.isLastPage
                set(value) {}
            override var isLoading: Boolean
                get() = this@MainActivity.isLoading
                set(value) {}

            override fun loadMoreItems() {
                isLoading = true
                offset += limit
                getLocation()
            }

        })

        viewModel.places.observe(this, Observer { result ->
            isLoading = false
            when (result) {
                is Result.Success -> {
                    val totalPlaceList = mutableListOf<PlaceView>().apply {
                        addAll(placeAdapter.currentList)
                        addAll(result.data)
                        offset += limit
                    }
                    placeAdapter.submitList(totalPlaceList)
                    isLastPage = true
                }
                is Result.Error -> {
                    isLoading = false
                    placeAdapter.submitList(result.data)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
            swl_places.isRefreshing = false
        })

        if (!checkPermissions()) startLocationPermissionRequest() else getLocation()
        swl_places.setOnRefreshListener {
            if (!checkPermissions()) startLocationPermissionRequest() else getLocation()
        }
    }

    private fun checkPermissions() =
        (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) == PERMISSION_GRANTED)

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
            LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST &&
            grantResults[0] == PERMISSION_GRANTED &&
            grantResults[1] == PERMISSION_GRANTED
        ) {
            getLocation()
        }
    }

    private fun getLocation() {
        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    taskLocation.result?.let {
                        getPlaces(it.latitude, it.longitude)
                    }
                }
            }
    }

    private fun getPlaces(lat: Double, long: Double) {
        viewModel.getNearbyPlaces(lat, long, limit, offset)
    }
}
