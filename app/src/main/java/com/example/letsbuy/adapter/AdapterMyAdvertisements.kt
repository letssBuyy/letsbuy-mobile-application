package com.example.letsbuy.adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.model.AdvertisementResponse

class AdapterMyAdvertisements (
    private val advertisements: List<AdvertisementResponse>
): RecyclerView.Adapter<AdapterMyAdvertisements.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemMyAdvertisementsView =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_advertisement_adapter, parent,false)


        return MyViewHolder(itemMyAdvertisementsView)

    }

    override fun getItemCount(): Int = advertisements.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val advertisement = advertisements[position]


        holder.dateAdvertisement.text = advertisement.postDate

        if (advertisement.images.isNullOrEmpty()){
            holder.imgAdvetisement.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.images.first().url).into(holder.imgAdvetisement)
        }

        holder.titleAdvertisement.text = advertisement.title

        holder.priceAdvertisement.text = advertisement.price.toString()

    }

    class MyViewHolder(itemAdvertisement: View) : RecyclerView.ViewHolder(itemAdvertisement){
        val imgAdvetisement: ImageView = itemAdvertisement.findViewById(R.id.img_advertisements)
        val titleAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView36)
        val priceAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView30)
        val dateAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView35)
    }
}