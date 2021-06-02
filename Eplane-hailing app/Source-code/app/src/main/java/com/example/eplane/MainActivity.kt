package com.example.eplane

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        supportActionBar?.hide(); // hide the title bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val user_uid = sharedPreferences.getString("USER_UID", null).toString()

        if (user_uid != "null") {
            val intent = Intent(this@MainActivity, EnterPickUpLocation::class.java)
            intent.putExtra("user_uid", user_uid)
            startActivity(intent)
        }

        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                number_progress.visibility = View.GONE
                get_otp.visibility = View.VISIBLE
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                number_progress.visibility = View.GONE
                get_otp.visibility = View.VISIBLE
                Toast.makeText(this@MainActivity, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                number_progress.visibility = View.GONE
                get_otp.visibility = View.VISIBLE
                val intent = Intent(this@MainActivity, EnterOtp::class.java)
                intent.putExtra("mobile", input_mobile_number.text.toString())
                intent.putExtra("otp", p0)
                startActivity(intent)
            }
        }

        get_otp.setOnClickListener {

            if (input_mobile_number.text.toString().trim().isNotEmpty()) {
                if ((input_mobile_number.text.toString().trim()).length == 10) {

                    number_progress.visibility = View.VISIBLE
                    get_otp.visibility = View.INVISIBLE

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91" + input_mobile_number.text.toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()

                    PhoneAuthProvider.verifyPhoneNumber(options)



                } else {
                    Toast.makeText(this, "Please enter a valid Mobile Number", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter your Mobile Number", Toast.LENGTH_LONG).show()
            }

        }

    }
}