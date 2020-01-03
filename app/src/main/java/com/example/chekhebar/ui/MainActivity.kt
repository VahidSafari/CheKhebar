package com.example.chekhebar.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
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

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var settingsClient: SettingsClient

    private val LOCATION_REQUEST = 161
    lateinit var viewModel: MapViewModel
    private val limit = 12
    private var offset = 0
    private val VISIBLE_THRESHOLD = 2
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
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    private val REQUEST_CHECK_SETTINGS = 120

    private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
    private val KEY_LOCATION = "location"
    private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"


    private val pagingScrollListener = object : RecyclerView.OnScrollListener() {
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
                        isLoading = true
                        loadMorPlaces()
                    }
                }
            }
        }
    }

    private fun loadMorPlaces() {
        offset += limit
        currentLocation?.let {
            getPlaces(it.latitude, it.longitude)
        }
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
                    isInitialLoad = false
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

        updateValuesFromBundle(savedInstanceState)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
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
                    savedInstanceState.getParcelable<Location>(KEY_LOCATION)
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                lastUpdateTime =
                    savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING)
            }
            currentLocation?.let {
                getPlaces(it.latitude, it.longitude)
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
                    getPlaces(it.latitude, it.longitude)
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
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )

                currentLocation?.let {
                    getPlaces(it.latitude, it.longitude)
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
                    getPlaces(it.latitude, it.longitude)
                }
            }
    }

    private fun getPlaces(lat: Double, long: Double) {
        viewModel.getNearbyPlaces(lat, long, limit, offset, isInitialLoad)
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
}
