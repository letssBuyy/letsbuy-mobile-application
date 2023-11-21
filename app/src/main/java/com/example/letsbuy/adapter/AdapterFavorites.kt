package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
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

class AdapterFavorites(
    private val myList: List<AllAdversimentsAndLikeDtoResponse>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterFavorites.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val adversiment = myList[position]

        holder.textPrice.text = "R$ " + adversiment.adversiments.price.toString()
        holder.textTittle.text = adversiment.adversiments.title
        holder.textCategory.text =
            CategoryEnum.enumCategoryToDescription(adversiment.adversiments.category)
        holder.like.setOnClickListener {
            islike(holder, adversiment)
        }

        if (adversiment.adversiments.images.isNullOrEmpty()) {
            holder.imgAdvertisement.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(adversiment.adversiments.images.first().url)
                .into(holder.imgAdvertisement)
        }

        holder.imgAdvertisement.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdDetailActivity::class.java)
            intent.putExtra("ID_AD", adversiment.adversiments.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = myList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAdvertisement: ImageView = itemView.findViewById(R.id.imageViewAdversement)
        val textPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val textTittle: TextView = itemView.findViewById(R.id.textViewTittle)
        val textCategory: TextView = itemView.findViewById(R.id.textViewCategory)
        val like: ImageView = itemView.findViewById(R.id.imageViewLike)
    }


    private fun islike(holder: MyViewHolder, advertisement: AllAdversimentsAndLikeDtoResponse) {
        Log.w("ENTROU NA FUNCTION", "AAAAAAAAAAAAAAAAAAAA")
        val api = Rest.getInstance().create(LikeService::class.java)
        api.unLike(advertisement.adversiments.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.w("respostaApi", "aqui no sucesso")
                } else {
                    Log.e("respostaApi", "Erro na requisição: $response")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("respostaApi", "Ocorreu um erro ao descurtir, tente novamente")
            }
        })
    }
}