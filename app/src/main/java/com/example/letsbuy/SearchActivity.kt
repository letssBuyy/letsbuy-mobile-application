package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.letsbuy.adapter.AdapterSearch
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivitySearchBinding
import com.example.letsbuy.dto.AdversimentFilterDto
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum
import com.example.letsbuy.service.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE

        val pref = this.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        var id = pref?.getString("ID", null)?.toLong()

        binding.imgBack.setOnClickListener {
            val home = Intent(baseContext, HomeActivity::class.java)
            startActivity(home)
        }

        binding.editSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewSearch.visibility = View.GONE
                binding.imgError.visibility = View.GONE
                binding.errorMessage.visibility = View.GONE
                filterSearch(id!!)
                return@setOnEditorActionListener true
            }
            false
        }

        val bottomFilterFragment = BottomSheetFilterFragment()
        binding.btnFilter.setOnClickListener {
            bottomFilterFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

        filterSearch(id!!)
    }

    private fun initRecyclerView(listSearch: List<AllAdversimentsAndLikeDtoResponse>) {
        binding.recyclerViewSearch.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewSearch.setHasFixedSize(true)
        binding.recyclerViewSearch.adapter = AdapterSearch(listSearch)
    }

    fun filterSearch(idUser: Long) {
        val api = Rest.getInstance().create(SearchService::class.java)

        val title = binding.editSearch.text.toString()

        val location = if (intent.getStringExtra("LOCATION")
                .isNullOrEmpty()
        ) null else intent.getStringExtra("LOCATION")
        val priceMin = if (intent.getDoubleExtra("PRICEMIN", -1.0)
                .equals(-1.0)
        ) null else intent.getDoubleExtra("PRICEMIN", -1.0)
        val priceMax = if (intent.getDoubleExtra("PRICEMAX", -1.0)
                .equals(-1.0)
        ) null else intent.getDoubleExtra("PRICEMAX", -1.0)
        val quality = if (intent.getStringExtra("QUALITY")
                .isNullOrEmpty()
        ) null else QualityEnum.qualityToEnum(intent.getStringExtra("QUALITY")!!)
        val category = if (intent.getStringExtra("CATEGORY")
                .isNullOrEmpty()
        ) null else CategoryEnum.categoryToEnum(intent.getStringExtra("CATEGORY")!!)
        val color = if (intent.getStringExtra("COLOR")
                .isNullOrEmpty()
        ) null else AdversimentColorEnum.colorToEnum(intent.getStringExtra("COLOR")!!)
        val typeFilter = intent.getIntExtra("FILTER", 1)

        Log.d("PRECOMIN", priceMin.toString())
        Log.d("PRECOMAX", priceMax.toString())
        val filter =
            AdversimentFilterDto(location, priceMin, priceMax, quality, category, color, typeFilter)

        api.search(idUser, title, filter)
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
                            binding.tvQtdAdversiments.text =
                                "0 anúncios encontrados"
                        } else {
                            binding.imgError.visibility = View.GONE
                            binding.errorMessage.visibility = View.GONE
                            binding.recyclerViewSearch.visibility = View.VISIBLE
                            binding.tvQtdAdversiments.text =
                                "${adversiments.size} anúncios encontrados"
                            initRecyclerView(adversiments)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    t: Throwable
                ) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}