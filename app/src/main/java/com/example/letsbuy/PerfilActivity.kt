package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityPerfilBinding

class PerfilActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        binding =  ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.texEdtPerfil.setOnClickListener() {
            val edtPerfil = Intent(this, EditProfileActivity::class.java)
            startActivity(edtPerfil)
        }

        binding.texMyAdvertisements.setOnClickListener(){
            val myAdvertisementsActivity = Intent(this, MyAdvertisementsActivity::class.java)
            startActivity(myAdvertisementsActivity)
        }

        binding.texPublishAd.setOnClickListener() {
            val publishAd = Intent(this, PublishAdActivity::class.java)
            startActivity(publishAd)
        }

        binding.texMyShoppings.setOnClickListener() {
            val myShoppings = Intent(this, MyShoppingsActivity::class.java)
            startActivity(myShoppings)
        }

        binding.texMyWallet.setOnClickListener() {
            val myWallet = Intent(this, WalletActivity::class.java)
            startActivity(myWallet)
        }

        binding.texExit.setOnClickListener() {
            val exit = Intent(this, LoginActivity::class.java)
            startActivity(exit)
        }
    }
}