package com.example.eplane

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_enter_otp.*

class EnterOtp : AppCompatActivity() {

    var input1 = ""
    var input2 = ""
    var input3 = ""
    var input4 = ""
    var input5 = ""
    var input6 = ""
    lateinit var otp_backend: String
    lateinit var sharedPreferences: SharedPreferences

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

        setContentView(R.layout.activity_enter_otp)

        otp_backend = intent.getStringExtra("otp").toString()

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        verify_otp.setOnClickListener{

            input1 = input_otp1.text.toString().trim()
            input2 = input_otp2.text.toString().trim()
            input3 = input_otp3.text.toString().trim()
            input4 = input_otp4.text.toString().trim()
            input5 = input_otp5.text.toString().trim()
            input6 = input_otp6.text.toString().trim()

            if (input1.isNotEmpty() && input2.isNotEmpty() && input3.isNotEmpty() && input4.isNotEmpty() && input5.isNotEmpty() && input6.isNotEmpty()) {

                val entered_otp = input1 + input2 + input3 + input4 + input5 + input6

                otp_progress.visibility = View.VISIBLE
                verify_otp.visibility = View.INVISIBLE

                val phoneAuthCredential = PhoneAuthProvider.getCredential(otp_backend, entered_otp)

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener (this) { task ->

                        otp_progress.visibility = View.GONE
                        verify_otp.visibility = View.VISIBLE

                        if (task.isSuccessful) {
                            val user = task.result?.user
                            if (user != null) {

                                val editor = sharedPreferences.edit()
                                editor.putString("USER_UID", user.uid)
                                editor.apply()

                                val intent = Intent(this, EnterPickUpLocation::class.java)
                                intent.putExtra("user_uid", user.uid)
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this, "Enter the correct OTP", Toast.LENGTH_LONG).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Incomplete OTP", Toast.LENGTH_LONG).show()
            }
        }

        moveToOtpBox()
    }

    private fun moveToOtpBox() {
        input_otp1.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    input_otp2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        } )
        input_otp2.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    input_otp3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        } )
        input_otp3.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    input_otp4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        } )
        input_otp4.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    input_otp5.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        } )
        input_otp5.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    input_otp6.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        } )
    }
}