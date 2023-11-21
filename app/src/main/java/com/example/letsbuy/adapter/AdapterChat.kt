package com.example.letsbuy.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letsbuy.ChatMessageActivity
import com.example.letsbuy.R
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.ui.chat.ChatMessageFragment

class AdapterChat(
    private val myList: List<ChatResponseDto>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterChat.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat,parent,false)
        Log.w("Chegou AQUII CREATEVIEW",itemView.toString())
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chats = myList[position]

        Log.w("MY LISTYYYYYYYYY" , myList.toString())
        Log.w("MY LISTYYYYYYYYY POSSS" , myList[position].toString())
        Log.w("CHHHHHHHAAAAAAAATSSS" , chats.toString())


        Log.w("Chegou" , chats.adversiment.title.toString())
        Log.w("CHEGOUUUUUUUUUUUUUUUUUUUUUU" , chats.adversiment.title.toString())
        Log.w("Titulo" , chats.adversiment.title.toString())
        Log.w("Titulo" , chats.adversiment.title.toString())



        holder.textTittle.text = chats.adversiment.title
        holder.textData.text = "Ontem"
        holder.textCategory.text = CategoryEnum.enumCategoryToDescription(chats.adversiment.category)

        if (chats.adversiment.images.isNullOrEmpty()){
            holder.imgadversiment.setImageResource(R.drawable.broke_image)
        } else {
            Glide.with(holder.itemView.context).load(chats.adversiment.images!!.first().url).into(holder.imgadversiment)
        }

        holder.imgadversiment.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatMessageActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = myList.size


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imgadversiment: ImageView = itemView.findViewById(R.id.badgeImageOne)
        val textData : TextView = itemView.findViewById(R.id.brandAdThree)
        val textTittle : TextView = itemView.findViewById(R.id.nameAdOne)
        val textCategory : TextView = itemView.findViewById(R.id.brandAdTwo)
    }
}