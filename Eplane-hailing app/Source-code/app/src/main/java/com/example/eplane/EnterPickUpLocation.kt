package com.example.eplane

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.eplane.databinding.ActivityEnterPickUpLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_enter_pick_up_location.*
import java.io.IOException

class EnterPickUpLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityEnterPickUpLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var user_uid: String
    lateinit var latLng: LatLng
    lateinit var location: String
    var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterPickUpLocationBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar?.hide() // hide the title bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(binding.root)

        user_uid= intent.getStringExtra("user_uid").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.pick_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // SearchView Setup
        sc_pick_location.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                location = sc_pick_location.query.toString()
                var addressList: List<Address>? = null

                if (!location.equals("")) {
                    val geocoder = Geocoder(this@EnterPickUpLocation)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address = addressList[0]
                        mapFragment.getMapAsync(object: OnMapReadyCallback {
                            override fun onMapReady(p0: GoogleMap) {
                                marker?.remove()
                                latLng = LatLng(address.latitude, address.longitude)
                                marker = p0.addMarker(MarkerOptions().position(latLng).title(location).icon(BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_CYAN)))
                                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F))
                            }

                        })
                    } else {
                        location ="Current Location"
                        Toast.makeText(this@EnterPickUpLocation, "Location not found.", Toast.LENGTH_LONG).show()
                    }
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        // Submit Button
        choose_drop_location.setOnClickListener {
            val intent = Intent(this, EnterDropOffLocation::class.java)
            intent.putExtra("user_uid", user_uid)
            intent.putExtra("pickup_lat", latLng.latitude)
            intent.putExtra("pickup_lng", latLng.longitude)
            intent.putExtra("pickup_location", location)
            startActivity(intent)
        }

        fetchLocation()
    }

    private fun fetchLocation() {

        val task = fusedLocationClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                mapFragment.getMapAsync { p0 ->
                    latLng = LatLng(it.latitude, it.longitude)
                    location = "Current Location"
                    marker =
                        p0.addMarker(MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_CYAN)))
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F))
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        return
    }
}