package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.letsbuy.databinding.ActivityPaymentCompletedBinding
import com.example.letsbuy.databinding.ActivityPaymentFailureBinding

class PaymentFailureActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPaymentFailureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_failure)
        binding = ActivityPaymentFailureBinding.inflate(layoutInflater)

        binding.imageView1.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        binding.btnMinhasCompras.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }
}