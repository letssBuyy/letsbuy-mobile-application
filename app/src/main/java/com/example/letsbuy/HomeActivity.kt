package com.example.letsbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.adapter.AdapterProduto
import com.example.letsbuy.model.Produto

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val recyclerView_produtos = findViewById<RecyclerView>(R.id.recyclerView_produtos)
        recyclerView_produtos.layoutManager = GridLayoutManager(this, 2, 1, false)
        recyclerView_produtos.setHasFixedSize(true)

        val listaProdutos: MutableList<Produto> = mutableListOf()
        val adapterProduto = AdapterProduto(this, listaProdutos)
        recyclerView_produtos.adapter = adapterProduto

        val produto1 = Produto(
            R.drawable.img_teste,
            "Playtation 1",
            "PlayStation 1 Classic para quem curti os jogos clássicos do Ps1, ele está em excelente estado de conservação, com um controle funcionando perfeitamente é só ligar e jogar. Já vai com o cabo USB de energia e cabo HDMI incluído. ACEITO",
            "R$ 800,00"
        )

        val produto2 = Produto(
            R.drawable.img_teste,
            "Playtation 1",
            "PlayStation 1 Classic para quem curti os jogos clássicos do Ps1, ele está em excelente estado de conservação, com um controle funcionando perfeitamente é só ligar e jogar. Já vai com o cabo USB de energia e cabo HDMI incluído. ACEITO",
            "R$ 800,00"
        )

        val produto3 = Produto(
            R.drawable.img_teste,
            "Playtation 1",
            "PlayStation 1 Classic para quem curti os jogos clássicos do Ps1, ele está em excelente estado de conservação, com um controle funcionando perfeitamente é só ligar e jogar. Já vai com o cabo USB de energia e cabo HDMI incluído. ACEITO",
            "R$ 800,00"
        )

        val produto4 = Produto(
            R.drawable.img_teste,
            "Playtation 1",
            "PlayStation 1 Classic para quem curti os jogos clássicos do Ps1, ele está em excelente estado de conservação, com um controle funcionando perfeitamente é só ligar e jogar. Já vai com o cabo USB de energia e cabo HDMI incluído. ACEITO",
            "R$ 800,00"
        )

        listaProdutos.add(produto1)
        listaProdutos.add(produto2)
        listaProdutos.add(produto3)
        listaProdutos.add(produto4)
    }
}