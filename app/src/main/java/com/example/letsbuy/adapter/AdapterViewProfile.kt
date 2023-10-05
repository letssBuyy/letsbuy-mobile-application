package com.example.letsbuy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.model.enums.CategoryEnum.Companion.enumCategoryToDescription

class AdapterViewProfile (
    private val userAndAdversiments: UserAdversimentsDtoResponse,
    private val context: Context
): RecyclerView.Adapter<AdapterViewProfile.MyViewHolder>() {
    class MyViewHolder (itemAdversiment: View) : RecyclerView.ViewHolder(itemAdversiment){
        val imgAdversiment: ImageView = itemAdversiment.findViewById(R.id.iv_adversiment)
        val titleAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_title_ad)
        val priceAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_price_ad)
        val categoryAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_category_ad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemMyViewProfileView =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_adversiment_seller_adapter, parent,false)

        return MyViewHolder(itemMyViewProfileView)
    }

    override fun getItemCount(): Int = userAndAdversiments.adversiments.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val adversiment = userAndAdversiments.adversiments[position]

        holder.titleAdversiment.text = adversiment.title
        holder.priceAdversiment.text = "R$ ${adversiment.price}"
        holder.categoryAdversiment.text = enumCategoryToDescription(adversiment.category)

        if (adversiment.images.isNullOrEmpty()) {
            holder.imgAdversiment.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(adversiment.images.first().url).into(holder.imgAdversiment)
        }

    }
}
