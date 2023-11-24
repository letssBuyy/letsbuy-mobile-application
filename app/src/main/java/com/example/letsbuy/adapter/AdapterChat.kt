package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.example.letsbuy.ChatMessageActivity
import com.example.letsbuy.R
import com.example.letsbuy.dto.ChatResponseDto
import com.bumptech.glide.request.target.Target;
import java.util.concurrent.ExecutionException;


class AdapterChat(
    private val myList: List<ChatResponseDto>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterChat.MyViewHolder>() {

    private lateinit var nameSeller: String
    private lateinit var imageProf: ViewTarget<ImageView, Drawable>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chats = myList[position]
        holder.textTittle.text = chats.adversiment.title
        holder.textData.text = chats.adversiment.postDate.toString()
        holder.textName.text = chats.seller.name
        nameSeller = chats.seller.name
        imageProf = Glide.with(holder.itemView.context).load(chats.adversiment.images!!.first().url).into(holder.imgadversiment)


        if (chats.adversiment.images.isNullOrEmpty()){
            holder.imgadversiment.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(chats.adversiment.images!!.first().url).into(holder.imgadversiment)
        }

        holder.imgadversiment.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatMessageActivity::class.java)
            intent.putExtra("NAME_SELLER", nameSeller.toString()!!)
//            intent.putExtra("IMG_SELLER", imageProf)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = myList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgadversiment: ImageView = itemView.findViewById(R.id.badgeImageOne)
        val textData : TextView = itemView.findViewById(R.id.brandAdThree)
        val textTittle : TextView = itemView.findViewById(R.id.nameAdOne)
        val textName : TextView = itemView.findViewById(R.id.brandAdTwo)
    }
}