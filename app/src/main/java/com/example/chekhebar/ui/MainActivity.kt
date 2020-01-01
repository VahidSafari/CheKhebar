package com.example.chekhebar.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var viewModel: MapViewModel
    private val limit = 12
    private var offset = 0
    private val VISIBLE_THRESHOLD = 2
    private var isLoading = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var placeAdapter: PlaceAdapter

    private val pagingScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
//            if (!isLoading) {
//                val visibleItemCount = layoutManager.childCount
//                Log.e("++++", visibleItemCount.toString())
//                val totalItemCount = layoutManager.itemCount
//                Log.e("++++", totalItemCount.toString())
//                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//                Log.e("++++", lastVisibleItem.toString())
//                if (visibleItemCount != 0 &&
//                    visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount
//                ) {
//                    loadMorPlaces()
//                    isLoading = true
//                }
//            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!isLoading) {
                    val visibleItemCount = layoutManager.childCount
                    Log.e("++++ visible items: ", visibleItemCount.toString())
                    val totalItemCount = layoutManager.itemCount
                    Log.e("++++ total items: ", totalItemCount.toString())
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    Log.e("++++ last visible item", lastVisibleItem.toString())
                    if (visibleItemCount != 0 &&
                        visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount
                    ) {
                        loadMorPlaces()
                        isLoading = true
                    }
                }
            }

        }
    }

    private fun loadMorPlaces() {
        offset += limit
        getLocation()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swl_places.isRefreshing = true
        viewModel = viewModelFactory.create(MapViewModel::class.java)

        recyclerView = findViewById(R.id.rv_places)
        layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter()

        recyclerView.apply {
            layoutManager = this@MainActivity.layoutManager
            adapter = placeAdapter
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            addOnScrollListener(pagingScrollListener)
        }

        viewModel.places.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    val totalPlaceList = mutableListOf<PlaceView>().apply {
                        addAll(placeAdapter.currentList)
                        addAll(result.data)
                        offset += limit
                    }
                    placeAdapter.submitList(totalPlaceList)
                }
                is Result.Error -> {
                    placeAdapter.submitList(result.data)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
            swl_places.isRefreshing = false
            isLoading = false
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

    override fun onDestroy() {
        super.onDestroy()
        rv_places.removeOnScrollListener(pagingScrollListener)
    }
}
