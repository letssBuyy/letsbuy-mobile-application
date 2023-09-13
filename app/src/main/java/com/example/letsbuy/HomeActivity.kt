package com.example.letsbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.letsbuy.ui.chat.ChatFragment
import com.example.letsbuy.ui.curti.CurtiFragment
import com.example.letsbuy.ui.home.HomeFragment
import com.example.letsbuy.ui.perfil.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        replaceFragment(HomeFragment())

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> replaceFragment(HomeFragment())
//                R.id.page_2 -> startActivity(Intent(this, FavoritesActivity::class.java))
                R.id.page_2 -> replaceFragment(CurtiFragment())
//                R.id.page_3 -> startActivity(Intent(this, ChatActivity::class.java))
                R.id.page_3 -> replaceFragment(ChatFragment())
                R.id.page_4 -> replaceFragment(PerfilFragment())
//                R.id.page_4 -> startActivity(Intent(this, PerfilActivity::class.java))
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