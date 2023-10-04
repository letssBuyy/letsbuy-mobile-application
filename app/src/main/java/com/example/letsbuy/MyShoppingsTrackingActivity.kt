package com.example.letsbuy

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.letsbuy.adapter.AdapterMyAdvertisementsBought
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityMyShoppingsBinding
import com.example.letsbuy.databinding.ActivityMyShoppingsTrackingBinding
import com.example.letsbuy.dto.MyBoughtsResponse
import com.example.letsbuy.service.MyBoughtsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyShoppingsTrackingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMyShoppingsTrackingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMyShoppingsTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            val back = Intent(this, MyShoppingsActivity::class.java)
            startActivity(back)
        }

        binding.title.text = intent.extras?.getString("title", "Nome indefinido").toString()
        binding.idAd.text = "ID do anúncio: ${intent.extras?.getLong("idAdvertisement")}"
        binding.sellerName.text = "Vendido por: ${intent.extras?.getString("sellerName")}"
        binding.price.text = "R$ ${intent.extras?.getDouble("price")}"
        binding.saleDate.text = intent.extras?.getString("saleDate")
        binding.sellerCpf.text = "CPF: ${intent.extras?.getString("sellerCpf")?.subSequence(0,3)}.***.***-**"

        val image = intent.extras?.getString("images")
        if (image.isNullOrEmpty()){
            binding.imgAd.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(baseContext).load(image).into(binding.imgAd)
        }

        when(intent.extras?.getInt("trackingSize")){
            2 -> {
                binding.imageView8.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
                binding.imageView9.setImageDrawable(getDrawable(R.drawable.line_pink))
            }
            3,4,5 -> {
                binding.imageView8.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
                binding.imageView9.setImageDrawable(getDrawable(R.drawable.line_pink))
                binding.imageView10.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
                binding.imageView11.setImageDrawable(getDrawable(R.drawable.line_pink))
            }
            6 -> {
                binding.imageView8.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
                binding.imageView9.setImageDrawable(getDrawable(R.drawable.line_pink))
                binding.imageView10.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
                binding.imageView11.setImageDrawable(getDrawable(R.drawable.line_pink))
                binding.imageView12.setImageDrawable(getDrawable(R.drawable.check_pink_svg))
            }
        }

    }
}