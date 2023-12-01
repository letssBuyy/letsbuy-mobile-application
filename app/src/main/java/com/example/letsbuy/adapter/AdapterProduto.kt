package com.example.letsbuy.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.AdDetailActivity
import com.example.letsbuy.R
import com.example.letsbuy.api.Rest
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.service.LikeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterProduto(private val produtos: List<AllAdversimentsAndLikeDtoResponse>) :
    RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_produto_item, parent, false)
        val holder = ProdutoViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int = produtos.size

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val advertisement = produtos[position]

        if (advertisement.adversiments.images.isNullOrEmpty()) {
            holder.imagem.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.adversiments.images.first().url)
                .into(holder.imagem)
        }

        holder.name.text = produtos[position].adversiments.title
        holder.category.text =
            CategoryEnum.enumCategoryToDescription(produtos[position].adversiments.category)
        holder.price.text = "R$ ${produtos[position].adversiments.price.toString()}"
        holder.like.setImageResource(if (advertisement.isLike) R.drawable.icon_heart_selected else R.drawable.heart)

        holder.like.setOnClickListener {
            islike(holder, advertisement)
        }

        holder.imagem.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdDetailActivity::class.java)
            intent.putExtra("ID_AD", advertisement.adversiments.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.img_advertisement)
        val name = itemView.findViewById<TextView>(R.id.tv_name_advertisement)
        val category = itemView.findViewById<TextView>(R.id.tv_category)
        val price = itemView.findViewById<TextView>(R.id.tv_price_advertisement)
        val like = itemView.findViewById<ImageView>(R.id.img_like)
    }

    private fun islike(holder: ProdutoViewHolder, advertisement: AllAdversimentsAndLikeDtoResponse) {
        if (advertisement.isLike) {
            holder.like.setImageResource(R.drawable.heart)
            val api = Rest.getInstance().create(LikeService::class.java)
            api.unLike(advertisement.likeId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        holder.like.setImageResource(R.drawable.heart)
                    } else {
                        holder.like.setImageResource(R.drawable.icon_heart_selected)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    holder.imagem.setImageResource(R.drawable.icon_heart_selected)
                }
            })
        } else {
            holder.like.setImageResource(R.drawable.icon_heart_selected)

            val api = Rest.getInstance().create(LikeService::class.java)
            api.like(advertisement.adversiments.id, advertisement.userId!!)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            holder.like.setImageResource(R.drawable.icon_heart_selected)
                        } else {
                            holder.like.setImageResource(R.drawable.heart)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        holder.like.setImageResource(R.drawable.heart)
                    }
                })
        }
    }
}