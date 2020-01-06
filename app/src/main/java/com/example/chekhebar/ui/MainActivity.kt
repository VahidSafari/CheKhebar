package com.example.chekhebar.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.os.Looper
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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@Suppress("PrivatePropertyName")
class MainActivity : DaggerAppCompatActivity(), PlaceAdapter.IOpenDetailActivity {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var settingsClient: SettingsClient

    private val LOCATION_REQUEST = 161
    lateinit var viewModel: MapViewModel
    private val REQUEST_LIMIT = 15
    private var REQUEST_OFFSET = 0
    private var isLoading = false
    private var isInitialLoad = true

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var placeAdapter: PlaceAdapter

    private var currentLocation: Location? = null
    private var requestingLocationUpdate = true
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback
    private var lastUpdateTime: String? = null
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long =
        UPDATE_INTERVAL_IN_MILLISECONDS * 3 / 2
    private val REQUEST_CHECK_SETTINGS = 120

    private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
    private val KEY_LOCATION = "location"
    private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"

    private val VISIBLE_THRESHOLD = 1

    private val pagingScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading && dy > 0) {
                val visibleItemCount = layoutManager.childCount
                Log.e(":| VISIBLE ITEMS:     ", visibleItemCount.toString())
                val totalItemCount = layoutManager.itemCount
                Log.e(":| TOTAL ITEMS:       ", totalItemCount.toString())
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                Log.e(":| LAST VISIBLE ITEM: ", lastVisibleItem.toString())
                if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
                    isLoading = true
                    loadMorePlaces()
                }
            }
        }
    }

    private fun loadMorePlaces() {
        REQUEST_OFFSET += REQUEST_LIMIT
        currentLocation?.let {
            getPlaces()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swl_places.isRefreshing = true
        viewModel = viewModelFactory.create(MapViewModel::class.java)

        recyclerView = findViewById(R.id.rv_places)
        layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter(this)

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
                    isInitialLoad = false
                    val totalPlaceList = mutableListOf<PlaceView>().apply {
                        addAll(placeAdapter.currentList)
                        addAll(result.data)
                        REQUEST_OFFSET += REQUEST_LIMIT + 1
                    }
                    placeAdapter.submitList(totalPlaceList)
                    Toast.makeText(
                        this,
                        "لیست با موفقیت بروزرسانی شد",
                        Toast.LENGTH_LONG
                    )
                }
                is Result.Error -> {
                    placeAdapter.submitList(result.data)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
            swl_places.isRefreshing = false
            isLoading = false
        })

        updateValuesFromBundle(savedInstanceState)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        swl_places.setOnRefreshListener(::getPlaces)
        fab_get_current_location.setOnClickListener {
            startLocationUpdates()
        }
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
// the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                requestingLocationUpdate = savedInstanceState.getBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES
                )
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                currentLocation =
                    savedInstanceState.getParcelable(KEY_LOCATION)
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                lastUpdateTime =
                    savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING)
            }
            currentLocation?.let {
                getPlaces()
            }
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
            startLocationUpdates()
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation
                lastUpdateTime = DateFormat.getTimeInstance().format(Date())
                currentLocation?.let {
                    if (!isLoading)
                        getPlaces()
                }
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
    }

    private fun buildLocationSettingsRequest() {
        locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
    }

    private fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(this) {
                Toast.makeText(
                    this,
                    "موقعیت مکانی بروزرسانی شد",
                    Toast.LENGTH_LONG
                ).show()
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )

                currentLocation?.let {
                    if (!isLoading)
                        getPlaces()
                }
            }.addOnFailureListener { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "MainActivity",
                            "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings "
                        )
                        try {
                            val rae = e as ResolvableApiException

                            rae.startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sie: SendIntentException) {
                            Log.i(
                                "MainActivity",
                                "PendingIntent unable to execute request."
                            )
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings."
                        Log.e("MainActivity", errorMessage)
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                        requestingLocationUpdate = false
                    }
                }
                currentLocation?.let {
                    if (!isLoading)
                        getPlaces()
                }
            }
    }

    private fun getPlaces() {
        currentLocation?.let {
            viewModel.getNearbyPlaces(
                it.latitude,
                it.longitude,
                REQUEST_LIMIT,
                REQUEST_OFFSET,
                isInitialLoad
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdate && checkPermissions()) {
            startLocationUpdates()
        } else if (!checkPermissions()) {
            startLocationPermissionRequest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        rv_places.removeOnScrollListener(pagingScrollListener)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            .addOnCompleteListener(this) {
                requestingLocationUpdate = false
            }
    }

    override fun openDetailActivity(
        placeId: String,
        name: String,
        distance: Int,
        address: String,
        category: String
    ) {
        startActivity(
            Intent(this, PlaceDetailActivity::class.java).apply {
                putExtra("placeId", placeId)
                putExtra("name", name)
                putExtra("distance", distance)
                putExtra("address", address)
                putExtra("category", category)
            }
        )
    }


}