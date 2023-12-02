package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityPaymentCompletedBinding
import com.example.letsbuy.databinding.ActivityPublishAdBinding

class PaymentCompletedActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPaymentCompletedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMinhasCompras.setOnClickListener {
            val back = Intent(this, MyShoppingsActivity::class.java)
            startActivity(back)
        }

        binding.imageView1.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }
}