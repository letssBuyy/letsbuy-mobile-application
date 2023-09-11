package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityEditProfileBinding
import com.example.letsbuy.databinding.ActivityPerfilBinding

class EditProfileActivity: AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        binding =  ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener() {
            val back = Intent(this, PerfilActivity::class.java)
            startActivity(back)
        }
    }
}