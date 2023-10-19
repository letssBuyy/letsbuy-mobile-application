package com.example.letsbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityHomeBinding
import com.example.letsbuy.databinding.ActivityRegisterBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.dto.UserDtoResponse
import com.example.letsbuy.service.AdversimentService
import com.example.letsbuy.service.UserService
import com.example.letsbuy.ui.chat.ChatFragment
import com.example.letsbuy.ui.curti.CurtiFragment
import com.example.letsbuy.ui.home.HomeFragment
import com.example.letsbuy.ui.perfil.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> replaceFragment(HomeFragment())
                R.id.page_2 -> replaceFragment(CurtiFragment())
                R.id.page_3 -> replaceFragment(ChatFragment())
                R.id.page_4 -> replaceFragment(PerfilFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transition = fragmentManager.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.commit()
    }
}