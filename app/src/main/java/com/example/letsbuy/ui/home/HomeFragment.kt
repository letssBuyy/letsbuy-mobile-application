package com.example.letsbuy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentHomeBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.service.AdversimentService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapterProduct: AdapterProduto
private lateinit var recyclerView: RecyclerView
private lateinit var listProduct: MutableList<AllAdversimentsAndLikeDtoResponse>
lateinit var imagem: Array<Int>
lateinit var name: Array<String>
lateinit var sell: Array<String>
lateinit var price: Array<String>

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()

        retrieveAdversiment(id)

        Log.w("LISTA", listProduct.toString())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_produtos)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        val adapterProduct = AdapterProduto(view.context, listProduct)
        recyclerView.adapter = adapterProduct
    }

    private fun retrieveAdversiment(id: Long?) {
        listProduct = mutableListOf<AllAdversimentsAndLikeDtoResponse>()

        val api = Rest.getInstance().create(AdversimentService::class.java)
        api.retrieveAdversiment(id).enqueue(object : Callback<List<AllAdversimentsAndLikeDtoResponse>> {
                override fun onResponse(
                    call: Call<List<AllAdversimentsAndLikeDtoResponse>>,
                    response: Response<List<AllAdversimentsAndLikeDtoResponse>>
                ) {
                    if (response.isSuccessful) {
                        val adversiments = response.body()
                        adversiments?.forEach { adversiment ->
                            listProduct.add(adversiment)
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