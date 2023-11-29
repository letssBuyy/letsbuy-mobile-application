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
            val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
            val userId = pref?.getString("ID", null)?.toLong()
            val login = pref?.getBoolean("LOGADO", false)
            val redirecionar = when {
                login == true -> Intent(this, HomeActivity::class.java)
                login == false && userId != null -> Intent(this, LoginActivity::class.java)
                login == false && userId == null -> Intent(this, SplashActivity::class.java)
                else -> Intent(this, LoginActivity::class.java)
            }

            startActivity(redirecionar)
            finishActivity(1)
        }, 2000)
    }
}