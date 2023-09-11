package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityMyAdvertisementsBinding
import com.example.letsbuy.databinding.ActivityMyShoppingsBinding

class MyShoppingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMyShoppingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_shoppings)

        binding =  ActivityMyShoppingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }
}