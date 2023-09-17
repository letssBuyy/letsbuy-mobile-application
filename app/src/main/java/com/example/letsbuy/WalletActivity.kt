package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityPerfilBinding
import com.example.letsbuy.databinding.ActivityWalletBinding

class WalletActivity: AppCompatActivity() {

    private lateinit var binding: ActivityWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        binding =  ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }
}