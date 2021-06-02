package com.example.eplane

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_get_eplane_info.*

class GetEplaneInfo : AppCompatActivity() {

    private lateinit var user_uid: String
    private var pickup_lat: Double = 0.0
    private var pickup_lng: Double = 0.0
    private lateinit var pickUpLatLng: LatLng
    private lateinit var pickup_location: String
    private var drop_lat: Double = 0.0
    private var drop_lng: Double = 0.0
    private lateinit var dropLatLng: LatLng
    private lateinit var drop_location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setContentView(R.layout.activity_get_eplane_info)

        user_uid = intent.getStringExtra("user_uid").toString()
        pickup_lat = intent.getDoubleExtra("pickup_lat", 0.0)
        pickup_lng = intent.getDoubleExtra("pickup_lng", 0.0)
        pickUpLatLng = LatLng(pickup_lat, pickup_lng)
        pickup_location = intent.getStringExtra("pickup_location").toString()
        drop_lat = intent.getDoubleExtra("drop_lat", 0.0)
        drop_lng = intent.getDoubleExtra("drop_lng", 0.0)
        dropLatLng = LatLng(pickup_lat, pickup_lng)
        drop_location = intent.getStringExtra("drop_location").toString()

        tv_source.text = pickup_location
        tv_destination.text = drop_location
    }
}