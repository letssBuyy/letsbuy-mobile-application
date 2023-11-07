package com.example.letsbuy

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.letsbuy.databinding.ActivityLoginBinding
import com.example.letsbuy.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindLayoutEvents()
    }

    fun bindLayoutEvents() {
        val firstScreen = findViewById<View>(R.id.first_screen)
        val secondScreen = findViewById<View>(R.id.second_screen)
        val thirdScreen = findViewById<View>(R.id.third_screen)


        binding.firstScreen.skipButton.setOnClickListener {
            sendLogin()
        }

        binding.firstScreen.nextButton.setOnClickListener {
            firstScreen.visibility = View.INVISIBLE
            secondScreen.visibility = View.VISIBLE
            thirdScreen.visibility = View.INVISIBLE
        }

        binding.secondScreen.skipButton.setOnClickListener {
            sendLogin()
        }

        binding.secondScreen.nextButton.setOnClickListener {
            firstScreen.visibility = View.INVISIBLE
            secondScreen.visibility = View.INVISIBLE
            thirdScreen.visibility = View.VISIBLE
        }

        binding.thirdScreen.loginButton.setOnClickListener {
            sendLogin()
        }

        binding.thirdScreen.registerButton.setOnClickListener {
            sendRegister()
        }
    }

    fun sendLogin() {
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }

    fun sendRegister() {
        val cadastrar = Intent(this, RegisterActivity::class.java)
        startActivity(cadastrar)
    }
}