package com.example.letsbuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.dto.AdvertisementResponse

class AdapterMyAdvertisementsInactive(
    private val advertisements: List<AdvertisementResponse>
): RecyclerView.Adapter<AdapterMyAdvertisementsInactive.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemMyAdvertisementsView =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_advertisement_inactive_adapter, parent,false)


        return MyViewHolder(itemMyAdvertisementsView)

    }

    override fun getItemCount(): Int = advertisements.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val advertisement = advertisements[position]


        holder.dateAdvertisement.text = advertisement.postDate

        if (advertisement.images.isNullOrEmpty()){
            holder.imgAdvertisement.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.images.first().url).into(holder.imgAdvertisement)
        }

        holder.titleAdvertisement.text = advertisement.title

        holder.priceAdvertisement.text = "R$ ${advertisement.price}"

    }

    class MyViewHolder(itemAdvertisement: View) : RecyclerView.ViewHolder(itemAdvertisement){
        val imgAdvertisement: ImageView = itemAdvertisement.findViewById(R.id.img_advertisements)
        val titleAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView36)
        val priceAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView30)
        val dateAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView35)
    }
}