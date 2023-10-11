package com.example.letsbuy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.model.Produto

class AdapterProduto(private val context: Context, private val produtos: MutableList<Produto>): RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_produto_item, parent, false)
        val holder = ProdutoViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int = produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.imagem.setImageResource(produtos[position].imagem)
        holder.name.text = produtos[position].name
        holder.sell.text = produtos[position].sell
        holder.preco.text = produtos[position].price
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.img_advertisement)
        val name = itemView.findViewById<TextView>(R.id.tv_name_advertisement)
        val sell = itemView.findViewById<TextView>(R.id.tv_seller)
        val preco = itemView.findViewById<TextView>(R.id.tv_price_advertisement)
    }
}
