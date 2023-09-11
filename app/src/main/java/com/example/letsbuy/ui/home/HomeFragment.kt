package com.example.letsbuy.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.WalletActivity
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.model.Produto

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapterProduct: AdapterProduto
private lateinit var recyclerView: RecyclerView
private lateinit var listProduct: MutableList<Produto>
lateinit var imagem: Array<Int>
lateinit var name: Array<String>
lateinit var sell: Array<String>
lateinit var price: Array<String>

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
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
        dataInit()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_produtos)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        val adapterProduct = AdapterProduto(view.context, listProduct)
        recyclerView.adapter = adapterProduct
    }

    private fun dataInit() {
        listProduct = mutableListOf<Produto>()

        val produto1 = Produto(
            1,
            R.drawable.badge,
            "Bolsa Marrom",
            "Tommy",
            "R$200,00"
        )

        val produto2 = Produto(
            2,
            R.drawable.badge,
            "Bolsa Marrom",
            "Tommy",
            "R$ 200,00"
        )

        val produto3 = Produto(
            3,
            R.drawable.badge,
            "Bolsa Marrom",
            "Tommy",
            "R$ 200,00"
        )

        val produto4 = Produto(
            4,
            R.drawable.badge,
            "Bolsa Marrom",
            "Tommy",
            "R$ 200,00"
        )

        val produto5 = Produto(
            5,
            R.drawable.badge,
            "Bolsa Marrom",
            "Tommy",
            "R$ 200,00"
        )

        listProduct.add(produto1)
        listProduct.add(produto2)
        listProduct.add(produto3)
        listProduct.add(produto4)
        listProduct.add(produto5)
    }
}