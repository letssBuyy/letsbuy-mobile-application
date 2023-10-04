package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.BottomSheetDeleteAdFragment
import com.example.letsbuy.EditAdActivity
import com.example.letsbuy.R
import com.example.letsbuy.dto.AdvertisementResponse

class AdapterMyAdvertisements(
    private val advertisements: List<AdvertisementResponse>,
    private val context: Context,
    private val supportFragmentManager: FragmentManager
): RecyclerView.Adapter<AdapterMyAdvertisements.MyViewHolder>()
{

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
            holder.imgAdvertisement.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(advertisement.images.first().url).into(holder.imgAdvertisement)
        }

        holder.titleAdvertisement.text = advertisement.title

        holder.priceAdvertisement.text = "R$ ${advertisement.price}"

        holder.iconEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditAdActivity::class.java)
            intent.putExtra("idAdvertisement", advertisement.id)
            context.startActivity(intent)
        }

        val bottomSheetDeleteAdFragment = BottomSheetDeleteAdFragment(advertisement.id)
        holder.iconLixeira.setOnClickListener {
            bottomSheetDeleteAdFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

    }

    class MyViewHolder(itemAdvertisement: View) : RecyclerView.ViewHolder(itemAdvertisement){
        val imgAdvertisement: ImageView = itemAdvertisement.findViewById(R.id.img_advertisements)
        val titleAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView36)
        val priceAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView30)
        val dateAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView35)
        val iconEdit: ImageView = itemAdvertisement.findViewById(R.id.imageView36)
        val iconLixeira: ImageView = itemAdvertisement.findViewById(R.id.trash)
    }
}