package com.example.letsbuy.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import com.example.letsbuy.api.Rest
import com.example.letsbuy.dto.AdTrackingPayload
import com.example.letsbuy.dto.TrackingResponseDto
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.model.Tracking
import com.example.letsbuy.service.MyAdvertisementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterMyAdvertisementsSold(
    private val advertisements: List<AdvertisementResponse>,
    private val context: Context
): RecyclerView.Adapter<AdapterMyAdvertisementsSold.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemMyAdvertisementsView =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_advertisement_tracking_adapter, parent,false)


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

        if (!advertisement.trackings.isNullOrEmpty()){
            Log.w("TRACKING", advertisement.trackings.toString())
            if (advertisement.trackings.size == 2){

                holder.trackingButton.text = "Enviado"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button)
                holder.trackingButton.setOnClickListener {
                    createTracking(idUser = advertisement.userId, advertisement.id, holder.trackingButton)
                }
            } else if (advertisement.trackings.size == 6){

                holder.trackingButton.text = "Entregue"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button_inactive)

            } else {
                holder.trackingButton.text = "Aguarde"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button_inactive)
            }
        }

    }

    private fun createTracking(idUser: Long, idAd: Long, button: TextView){
        val api = Rest.getInstance().create(MyAdvertisementService::class.java)
        api.createTracking(
            userId = idUser,
            idAd = idAd,
            Tracking(
                status = "SENT",
                adversiment = AdTrackingPayload(
                    id = idAd
                ))).enqueue(object: Callback<List<TrackingResponseDto>> {

            override fun onResponse(call: Call<List<TrackingResponseDto>>, response: Response<List<TrackingResponseDto>>) {
                if (response.isSuccessful) {
                    button.text = "Aguarde"
                    button.setBackgroundResource(R.drawable.bg_button_inactive)
                }
            }

            override fun onFailure(call: Call<List<TrackingResponseDto>>, t: Throwable) {
                val alertBuilder = AlertDialog.Builder(context)
                alertBuilder.setTitle("ERRO")
                alertBuilder.setMessage("Ocorreu um erro, tente novamente!")
                alertBuilder.create().show()
            }
        })
    }

    class MyViewHolder(itemAdvertisement: View) : RecyclerView.ViewHolder(itemAdvertisement){
        val imgAdvertisement: ImageView = itemAdvertisement.findViewById(R.id.img_advertisements)
        val titleAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView36)
        val priceAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView30)
        val dateAdvertisement: TextView = itemAdvertisement.findViewById(R.id.textView35)
        val trackingButton: TextView = itemAdvertisement.findViewById(R.id.btn_tracking)
    }
}