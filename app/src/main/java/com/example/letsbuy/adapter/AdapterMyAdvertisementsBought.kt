package com.example.letsbuy.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.AdDetailActivity
import com.example.letsbuy.MyShoppingsActivity
import com.example.letsbuy.MyShoppingsTrackingActivity
import com.example.letsbuy.R
import com.example.letsbuy.api.Rest
import com.example.letsbuy.dto.AdTrackingPayload
import com.example.letsbuy.dto.TrackingResponseDto
import com.example.letsbuy.dto.MyBoughtsResponse
import com.example.letsbuy.model.Tracking
import com.example.letsbuy.service.MyAdvertisementService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterMyAdvertisementsBought(
    private val advertisements: List<MyBoughtsResponse>,
    private val userId: Long,
    private val context: Context
): RecyclerView.Adapter<AdapterMyAdvertisementsBought.MyViewHolder>(){

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
            if (advertisement.trackings.size == 5){

                holder.trackingButton.text = "Recebido"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button)
                holder.trackingButton.setOnClickListener {
                    createTracking(idUser = userId, advertisement.id, holder.trackingButton)
                }
            } else if (advertisement.trackings.size == 6){

                holder.trackingButton.text = "Entregue"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button_inactive)

            } else {
                holder.trackingButton.text = "Aguarde"
                holder.trackingButton.setBackgroundResource(R.drawable.bg_button_inactive)
            }
        }

        holder.card.setOnClickListener {
            val intent = Intent(holder.itemView.context,MyShoppingsTrackingActivity::class.java)
            intent.putExtra("idAdvertisement", advertisement.id)
            intent.putExtra("title", advertisement.title)
            intent.putExtra("price", advertisement.price)
            intent.putExtra("images", advertisement.images?.first()?.url)
            intent.putExtra("saleDate", advertisement.saleDate)
            intent.putExtra("sellerName", advertisement.user.name)
            intent.putExtra("sellerCpf", advertisement.user.cpf)
            intent.putExtra("trackingSize", advertisement.trackings.size)
            context.startActivity(intent)
        }

    }

    private fun createTracking(idUser: Long, idAd: Long, button: TextView){
        val api = Rest.getInstance().create(MyAdvertisementService::class.java)
        api.createTracking(
            userId = idUser,
            idAd = idAd,
            Tracking(
                status = "DELIVERED",
                adversiment = AdTrackingPayload(
                    id = idAd
                ))).enqueue(object: Callback<List<TrackingResponseDto>> {

            override fun onResponse(call: Call<List<TrackingResponseDto>>, response: Response<List<TrackingResponseDto>>) {
                if (response.isSuccessful) {
                    button.text = "Entregue"
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
        val card: ConstraintLayout = itemAdvertisement.findViewById(R.id.ln_advertisements2)
    }
}