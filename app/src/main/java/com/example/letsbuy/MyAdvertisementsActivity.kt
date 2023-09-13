package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityMyAdvertisementsBinding
import com.example.letsbuy.databinding.ActivityPerfilBinding
import com.example.letsbuy.ui.perfil.PerfilFragment

class MyAdvertisementsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMyAdvertisementsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_advertisements)

        binding =  ActivityMyAdvertisementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        binding.lnAdvertisements.setOnClickListener() {
            val adDetail = Intent(this, AdDetailActivity::class.java)
            startActivity(adDetail)
        }

        binding.lnAdvertisements2.setOnClickListener() {
            val adDetail = Intent(this, AdDetailActivity::class.java)
            startActivity(adDetail)
        }

        binding.lnAdvertisements3.setOnClickListener() {
            val adDetail = Intent(this, AdDetailActivity::class.java)
            startActivity(adDetail)
        }
    }
}