package com.example.letsbuy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.dto.MapMessage

class AdapterChatMessage(
    private val myList: List<MapMessage>,
    private val context: Context,
) : RecyclerView.Adapter<AdapterChatMessage.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterChatMessage.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterChatMessage.MyViewHolder, position: Int) {
        val message = myList[0].messages[position]
        holder.textMessage.text = message.message
    }

    override fun getItemCount() = myList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textMessage : TextView = itemView.findViewById(R.id.textMsg)
        val textNome : TextView = itemView.findViewById(R.id.nome)
    }
}