package com.example.letsbuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import android.content.Context
import com.example.letsbuy.model.enums.CategoryEnum

class AdapterFavorites(
    private val myList: List<AllAdversimentsAndLikeDtoResponse>
) : RecyclerView.Adapter<AdapterFavorites.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_favorites,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val adversiment = myList[position]

        holder.textPrice.text ="R$ " + adversiment.adversiments.price.toString()
        holder.textTittle.text = adversiment.adversiments.title
        holder.textCategory.text = CategoryEnum.enumCategoryToDescription(adversiment.adversiments.category)

        if (adversiment.adversiments.images.isNullOrEmpty()){
            holder.imgAdvertisement.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(adversiment.adversiments.images.first().url).into(holder.imgAdvertisement)
        }


    }

    override fun getItemCount() = myList.size


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgAdvertisement: ImageView = itemView.findViewById(R.id.imageViewAdversement)
        val textPrice : TextView = itemView.findViewById(R.id.textViewPrice)
        val textTittle : TextView = itemView.findViewById(R.id.textViewTittle)
        val textCategory : TextView = itemView.findViewById(R.id.textViewCategory)
    }

}