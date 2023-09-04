package com.example.letsbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.model.Produto
import com.example.letsbuy.ui.chat.ChatFragment
import com.example.letsbuy.ui.curti.CurtiFragment
import com.example.letsbuy.ui.home.HomeFragment
import com.example.letsbuy.ui.perfil.PerfilFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.math.RoundingMode

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        val recyclerView_produtos = findViewById<RecyclerView>(R.id.recyclerView_produtos)
//        recyclerView_produtos.layoutManager = GridLayoutManager(this, 2, 1, false)
//        recyclerView_produtos.setHasFixedSize(true)
//
//        val listaProdutos: MutableList<Produto> = mutableListOf()
//        val adapterProduto = AdapterProduto(this, listaProdutos)
//        recyclerView_produtos.adapter = adapterProduto

        replaceFragment(HomeFragment())

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> replaceFragment(HomeFragment())
                R.id.page_2 -> replaceFragment(CurtiFragment())
                R.id.page_3 -> replaceFragment(ChatFragment())
                R.id.page_4 -> replaceFragment(PerfilFragment())
            }
            true
        }

//        val produto1 = Produto(
//            R.drawable.img_teste,
//            "Playtation 1",
//            "Yohan Hudson",
//            "R$ 800,00"
//        )
//
//        val produto2 = Produto(
//            R.drawable.img_teste,
//            "Playtation 1",
//            "Yohan Hudson",
//            "R$ 800,00"
//        )
//
//        val produto3 = Produto(
//            R.drawable.img_teste,
//            "Playtation 1",
//            "Yohan Hudson",
//            "R$ 800,00"
//        )
//
//        val produto4 = Produto(
//            R.drawable.img_teste,
//            "Playtation 1",
//            "Yohan Hudson",
//            "R$ 800,00"
//        )
//
//        val produto5 = Produto(
//            R.drawable.img_teste,
//            "Playtation 1",
//            "Yohan Hudson",
//            "R$ 800,00"
//        )

//        listaProdutos.add(produto1)
//        listaProdutos.add(produto2)
//        listaProdutos.add(produto3)
//        listaProdutos.add(produto4)
//        listaProdutos.add(produto5)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transition = fragmentManager.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.commit()
    }
}