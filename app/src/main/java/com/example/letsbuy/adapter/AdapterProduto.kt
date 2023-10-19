package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.AdDetailActivity
import com.example.letsbuy.EditAdActivity
import com.example.letsbuy.R
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.Produto
import com.example.letsbuy.model.enums.CategoryEnum

class AdapterProduto(private val context: Context?, private val produtos: MutableList<AllAdversimentsAndLikeDtoResponse>): RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_produto_item, parent, false)
        val holder = ProdutoViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int = produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val advertisement = produtos[position]

        if (advertisement.adversiments.images.isNullOrEmpty()){
            holder.imagem.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.adversiments.images.first().url).into(holder.imagem)
        }

        holder.name.text = produtos[position].adversiments.title
        holder.category.text = CategoryEnum.enumCategoryToDescription(produtos[position].adversiments.category)
        holder.price.text = "R$ ${produtos[position].adversiments.price.toString()}"

        holder.imagem.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdDetailActivity::class.java)
            intent.putExtra("ID_AD", advertisement.adversiments.id)
            context?.startActivity(intent)
        }
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.img_advertisement)
        val name = itemView.findViewById<TextView>(R.id.tv_name_advertisement)
        val category = itemView.findViewById<TextView>(R.id.tv_category)
        val price = itemView.findViewById<TextView>(R.id.tv_price_advertisement)
    }
}
