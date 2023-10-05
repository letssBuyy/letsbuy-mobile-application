package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.EditAdActivity
import com.example.letsbuy.R
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.model.AdvertisementResponse

class AdapterViewProfile (
    private val userAndAdversiments: UserAdversimentsDtoResponse,
    private val context: Context
): RecyclerView.Adapter<AdapterViewProfile.MyViewHolder>(){

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
        holder.priceAdversiment.text = adversiment.price.toString()
        holder.categoryAdversiment.text = adversiment.category

        //if (advertisement.images.isNullOrEmpty()){
            //holder.imgAdvetisement.setImageResource(R.drawable.broke_image)
        //} else {
            //Glide.with(holder.itemView.context).load(advertisement.images.first().url).into(holder.imgAdvetisement)
       // }

        //holder.iconEdit.setOnClickListener {
           // val intent = Intent(holder.itemView.context, EditAdActivity::class.java)
           // intent.putExtra("idAdvertisement", advertisement.id)
           // context.startActivity(intent)
       // }
    }

    class MyViewHolder(itemAdversiment: View) : RecyclerView.ViewHolder(itemAdversiment){
        val imgAdvetisement: ImageView = itemAdversiment.findViewById(R.id.iv_adversiment)
        val titleAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_title_ad)
        val priceAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_price_ad)
        val categoryAdversiment: TextView = itemAdversiment.findViewById(R.id.tv_category_ad)
    }
}