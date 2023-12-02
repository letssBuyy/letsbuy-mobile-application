package com.example.letsbuy.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.SearchActivity
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentHomeBinding
import com.example.letsbuy.dto.AdversimentFilterDto
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.service.SearchService
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
        var id = pref?.getString("ID", null)?.toLong()

        binding.editMobileNo.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                if (binding.editMobileNo.text.isNullOrEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.imgError.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                    initFilter(id!!)
                    false
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewProdutos.visibility = View.GONE
                    val title = binding.editMobileNo.text.toString()
                    search(id!!, title)
                    return@setOnEditorActionListener true
                }
            }
            false
        }

        binding.btnFilter.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "")
            startActivity(filter)
        }

        binding.imgFashionAcessories.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Acessórios")
            startActivity(filter)
        }

        binding.imgElectronis.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Eletrônicos")
            startActivity(filter)
        }

        binding.imgHouseDeoratioNn.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Casa e decoração")
            startActivity(filter)
        }

        binding.imgBooks.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Livros")
            startActivity(filter)
        }

        binding.imgVehicles.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Veículos")
            startActivity(filter)
        }

        binding.imgSportsLeisure.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Esporte e lazer")
            startActivity(filter)
        }

        binding.imgPets.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Pets")
            startActivity(filter)
        }

        binding.imgChildren.setOnClickListener {
            val filter = Intent(context, SearchActivity::class.java)
            filter.putExtra("CATEGORY", "Bebês e crianças")
            startActivity(filter)
        }

        initFilter(id!!)

        return binding.root
    }

    private fun initRecyclerView(listProduct: List<AllAdversimentsAndLikeDtoResponse>) {
        binding.recyclerViewProdutos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewProdutos.setHasFixedSize(true)
        binding.recyclerViewProdutos.adapter = AdapterProduto(listProduct)
    }

    fun search(id: Long, title: String) {
        val api = Rest.getInstance().create(SearchService::class.java)
        api.searchHome(id, title)
            .enqueue(object : Callback<List<AllAdversimentsAndLikeDtoResponse>> {
                override fun onResponse(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    response: Response<List<AllAdversimentsAndLikeDtoResponse>>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val adversiment = response.body()
                        if (adversiment.isNullOrEmpty()) {
                            binding.imgError.visibility = View.VISIBLE
                            binding.errorMessage.visibility = View.VISIBLE
                            binding.tvFilter.text = "0 anúncios encontrados"
                        } else {
                            binding.imgError.visibility = View.GONE
                            binding.errorMessage.visibility = View.GONE
                            binding.recyclerViewProdutos.visibility = View.VISIBLE
                            binding.tvFilter.text = "${adversiment.size} anúncios encontrados"
                            initRecyclerView(adversiment)
                        }
                    } else {
                        val message = "Requisição nao aceita"
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun initFilter(idUser: Long) {
        val api = Rest.getInstance().create(SearchService::class.java)

        val filter =
            AdversimentFilterDto(null, null, null, null, null, null, 1)

        api.search(idUser, null, filter)
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
                            binding.recyclerViewProdutos.visibility = View.GONE
                        } else {
                            binding.imgError.visibility = View.GONE
                            binding.errorMessage.visibility = View.GONE
                            binding.recyclerViewProdutos.visibility = View.VISIBLE
                            initRecyclerView(adversiments)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}