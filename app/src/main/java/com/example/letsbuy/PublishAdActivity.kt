package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityPerfilBinding
import com.example.letsbuy.databinding.ActivityPublishAdBinding

class PublishAdActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPublishAdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_ad)

        binding =  ActivityPublishAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPublish.setOnClickListener() {
            val publish = Intent(this, HomeActivity::class.java)
            startActivity(publish)
        }

        binding.imageBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }
}