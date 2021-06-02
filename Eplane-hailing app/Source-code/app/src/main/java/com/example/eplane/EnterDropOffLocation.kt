package com.example.eplane

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.eplane.databinding.ActivityEnterDropOffLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_enter_drop_off_location.*
import kotlinx.android.synthetic.main.activity_enter_pick_up_location.*
import java.io.IOException

class EnterDropOffLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityEnterDropOffLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var user_uid: String
    var pickup_lat: Double = 0.0
    var pickup_lng: Double = 0.0
    lateinit var pickUpLatLng: LatLng
    lateinit var pickup_location: String
    var pickup_marker: Marker? = null
    lateinit var latLng: LatLng
    lateinit var location: String
    var marker: Marker? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnterDropOffLocationBinding.inflate(layoutInflater)

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

        user_uid = intent.getStringExtra("user_uid").toString()
        pickup_lat = intent.getDoubleExtra("pickup_lat", 0.0)
        pickup_lng = intent.getDoubleExtra("pickup_lng", 0.0)
        pickUpLatLng = LatLng(pickup_lat, pickup_lng)
        pickup_location = intent.getStringExtra("pickup_location").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.drop_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getMapAsync { p0 ->
            Log.e("USER", pickup_lat.toString() + pickup_lng.toString())
            pickup_marker =
                p0.addMarker(MarkerOptions().position(pickUpLatLng).title(pickup_location).icon(
                    BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_CYAN)))
            p0.moveCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLng, 18.0F))
        }

        sc_pickup_location.setQuery(pickup_location, false)
        enabledSearchView(sc_pickup_location, false)


        // SearchView Setup
        sc_drop_location.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                location = sc_drop_location.query.toString()
                var addressList: List<Address>? = null

                if (location != "") {
                    val geocoder = Geocoder(this@EnterDropOffLocation)
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
                                marker = p0.addMarker(MarkerOptions().position(latLng).title(location).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_ROSE)))
                                p0.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0F))
                            }

                        })
                    } else {
                        location ="Current Location"
                        Toast.makeText(this@EnterDropOffLocation, "Location not found.", Toast.LENGTH_LONG).show()
                    }
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        // Submit Button
        book_eplane.setOnClickListener {
            val intent = Intent(this, GetEplaneInfo::class.java)
            intent.putExtra("user_uid", user_uid)
            intent.putExtra("pickup_lat", pickup_lat)
            intent.putExtra("pickup_lng", pickup_lng)
            intent.putExtra("pickup_location", pickup_location)
            intent.putExtra("drop_lat", latLng.latitude)
            intent.putExtra("drop_lng", latLng.longitude)
            intent.putExtra("drop_location", location)
            startActivity(intent)
        }

        fetchLocation()

    }

    private fun enabledSearchView(view: View, enable: Boolean) {
        view.isEnabled = enable
        if (view is ViewGroup) {
            val viewGroup: ViewGroup = view
            for (i in 0..viewGroup.childCount-1) {
                val child = viewGroup.getChildAt(i)
                enabledSearchView(child, enable)
            }
        }
    }

    private fun fetchLocation() {

        val task = fusedLocationClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {

            if (it != null) {
                mapFragment.getMapAsync { p0 ->
                    latLng = LatLng(pickup_lat - 0.00002, pickup_lng - 0.00002)
                    location = "Current Location"
                    marker =
                        p0.addMarker(MarkerOptions().position(latLng).title("Current Location").icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ROSE)))
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