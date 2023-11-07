package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterMyAdvertisementsBought
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityMyShoppingsBinding
import com.example.letsbuy.dto.MyBoughtsResponse
import com.example.letsbuy.service.MyBoughtsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyShoppingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMyShoppingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_shoppings)

        binding =  ActivityMyShoppingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE

        binding.imgBack.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        getBoughts(8)
    }

    private fun initRecyclerView(advertisements: List<MyBoughtsResponse>, userId: Long) {
        binding.recyclerViewAdvertisements.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdvertisements.setHasFixedSize(true)
        binding.recyclerViewAdvertisements.adapter = AdapterMyAdvertisementsBought(
            advertisements = advertisements,
            context = this,
            userId = userId
        )
    }

    private fun getBoughts(userId: Long){
        val api = Rest.getInstance().create(MyBoughtsService::class.java)
        api.getBoughts(userId).enqueue(object: Callback<List<MyBoughtsResponse>> {

            override fun onResponse(call: Call<List<MyBoughtsResponse>>, response: Response<List<MyBoughtsResponse>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val advertisements = response.body()
                    if (advertisements.isNullOrEmpty()) {
                        binding.scroll.visibility = View.GONE
                        binding.emptyAdvertisements.visibility = View.VISIBLE
                    } else {
                        binding.scroll.visibility = View.VISIBLE
                        binding.emptyAdvertisements.visibility = View.GONE
                        initRecyclerView(advertisements, userId)
                    }
                }
            }

            override fun onFailure(call: Call<List<MyBoughtsResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.scroll.visibility = View.GONE
                binding.emptyAdvertisements.visibility = View.VISIBLE
            }
        })
    }
}