package com.example.letsbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.letsbuy.databinding.ActivityHomeBinding
import com.example.letsbuy.ui.chat.ChatFragment
import com.example.letsbuy.ui.favorites.FavoritesFragment
import com.example.letsbuy.ui.home.HomeFragment
import com.example.letsbuy.ui.perfil.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                R.id.page_2 -> replaceFragment(FavoritesFragment())
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