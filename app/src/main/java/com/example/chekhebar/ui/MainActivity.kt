package com.example.chekhebar.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chekhebar.R
import com.example.chekhebar.core.di.ViewModelFactory
import com.example.chekhebar.data.Result
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var googleApiClient: GoogleApiClient

    private val LOCATION_REQUEST = 161

    private lateinit var placeAdapter: PlaceAdapter

    lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = viewModelFactory.create(MapViewModel::class.java)

        rv_places.layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter()
        rv_places.adapter = placeAdapter
        rv_places.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        googleApiClient.registerConnectionCallbacks(this)

    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }

    override fun onConnected(p0: Bundle?) {
        Toast.makeText(this, "Connected :)", Toast.LENGTH_SHORT).show()
        getLocationBasedPlaces()
        viewModel.places.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    placeAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    placeAdapter.submitList(result.data)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this, "Connection was suspended", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOCATION_REQUEST && resultCode == RESULT_OK) {

        }
    }


    private fun getLocationBasedPlaces() {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
        } else {
            fusedLocationProviderClient.lastLocation.result?.let {
                viewModel.getNearbyPlaces(it.latitude, it.longitude)
            }
        }
    }

    private fun checkPermissions() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST && grantResults.size > 1 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.result?.let {
                viewModel.getNearbyPlaces(it.latitude, it.longitude)
            }
        }
    }
}
