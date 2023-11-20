package com.example.letsbuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.api.Rest
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.service.LikeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterSearch(private val listSearch: List<AllAdversimentsAndLikeDtoResponse>) :
    RecyclerView.Adapter<AdapterSearch.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int = listSearch.size
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val advertisement = listSearch[position]

        if (advertisement.adversiments.images.isNullOrEmpty()) {
            holder.imagem.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.adversiments.images.first().url)
                .into(holder.imagem)
        }

        holder.tittle.text = advertisement.adversiments.title
        holder.category.text = CategoryEnum.enumCategoryToDescription(advertisement.adversiments.category)
        holder.price.text = "R$ ${advertisement.adversiments.price.toString()}"
        holder.like.setImageResource(if (advertisement.isLike) R.drawable.icon_heart_selected else R.drawable.heart)

        holder.like.setOnClickListener {
            islike(holder, advertisement)
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.img_adversement)
        val tittle = itemView.findViewById<TextView>(R.id.tv_title)
        val price = itemView.findViewById<TextView>(R.id.tv_price)
        val category = itemView.findViewById<TextView>(R.id.tv_category)
        val like = itemView.findViewById<ImageView>(R.id.img_like)
    }

    private fun islike(holder: AdapterSearch.SearchViewHolder, advertisement: AllAdversimentsAndLikeDtoResponse) {
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