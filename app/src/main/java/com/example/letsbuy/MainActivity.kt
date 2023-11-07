package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val redirecionar = Intent(this, SplashActivity::class.java)
            startActivity(redirecionar)
            finishActivity(1)
        }, 2000)
    }
}