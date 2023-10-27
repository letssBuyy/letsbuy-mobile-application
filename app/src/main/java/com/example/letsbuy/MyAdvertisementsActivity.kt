package com.example.letsbuy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterMyAdvertisements
import com.example.letsbuy.adapter.AdapterMyAdvertisementsInactive
import com.example.letsbuy.adapter.AdapterMyAdvertisementsSold
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityMyAdvertisementsBinding
import com.example.letsbuy.listener.BottomSheetDeleteAdListener
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.service.MyAdvertisementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAdvertisementsActivity: AppCompatActivity(), BottomSheetDeleteAdListener {

    private lateinit var binding: ActivityMyAdvertisementsBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_advertisements)

        binding =  ActivityMyAdvertisementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val types = resources.getStringArray(R.array.my_advertisements_type)
        val spinner = binding.spinner
        val arrayAdapter = ArrayAdapter(baseContext, R.layout.dropdown_my_advertisements_item, types)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = if (types[position].equals("Publicados")) {
                    "ACTIVE"
                } else if (types[position].equals("Vendidos")){
                    "SALLED"
                } else {
                    "INACTIVE"
                }

                getAdvertisements(type)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        binding.imgBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

    }

    override fun onDeleteCompleted(){
        getAdvertisements("ACTIVE")
    }

    private fun initRecyclerView(advertisements: List<AdvertisementResponse>, status: String){
        binding.recyclerViewAdvertisements.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdvertisements.setHasFixedSize(true)
        if (status == "SALLED"){
            binding.recyclerViewAdvertisements.adapter = AdapterMyAdvertisementsSold(advertisements, this)
        } else if (status == "INACTIVE"){
            binding.recyclerViewAdvertisements.adapter = AdapterMyAdvertisementsInactive(advertisements)
        } else {
            binding.recyclerViewAdvertisements.adapter = AdapterMyAdvertisements(advertisements,this, supportFragmentManager)
        }

    }

     fun getAdvertisements(status: String){
         val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
         val id = pref?.getString("ID", null)?.toLong()
         val api = Rest.getInstance().create(MyAdvertisementService::class.java)
        api.getAdvertisements(id!!, status).enqueue(object: Callback<List<AdvertisementResponse>> {

            override fun onResponse(call: Call<List<AdvertisementResponse>>, response: Response<List<AdvertisementResponse>>) {
                if (response.isSuccessful) {
                    val advertisements = response.body()
                    if (advertisements.isNullOrEmpty()) {
                        binding.scroll.visibility = View.GONE
                        binding.emptyAdvertisements.visibility = View.VISIBLE
                    } else {
                        binding.scroll.visibility = View.VISIBLE
                        binding.emptyAdvertisements.visibility = View.GONE
                        initRecyclerView(advertisements, status)
                    }
                }
            }

            override fun onFailure(call: Call<List<AdvertisementResponse>>, t: Throwable) {
                binding.scroll.visibility = View.GONE
                binding.emptyAdvertisements.visibility = View.VISIBLE
            }
        })
    }
}