package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ //Intent goes from Splash to Main
            val splashIntent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, SPLASH_TIME_OUT.toLong())//Pass in how long it goes for

    }
}