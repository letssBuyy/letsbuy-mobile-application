package com.example.letsbuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentHomeBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        retrieveAdversiment(id!!)
        return binding.root
    }

    private fun initRecyclerView(listProduct: List<AllAdversimentsAndLikeDtoResponse>) {
        binding.recyclerViewProdutos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewProdutos.setHasFixedSize(true)
        binding.recyclerViewProdutos.adapter = AdapterProduto(listProduct)
    }

    private fun retrieveAdversiment(id: Long) {
        val teste: List<AllAdversimentsAndLikeDtoResponse> = listOf()

        val api = Rest.getInstance().create(AdversimentService::class.java)
        api.retrieveAdversiment(id)
            .enqueue(object : Callback<List<AllAdversimentsAndLikeDtoResponse>> {
                override fun onResponse(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    response: Response<List<AllAdversimentsAndLikeDtoResponse>>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val adversiments = response.body()
                        if (adversiments.isNullOrEmpty()) {
                            binding.imgError.visibility = View.VISIBLE
                            binding.errorMessage.visibility = View.VISIBLE
                        } else {
                            initRecyclerView(adversiments)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    t: Throwable
                ) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}