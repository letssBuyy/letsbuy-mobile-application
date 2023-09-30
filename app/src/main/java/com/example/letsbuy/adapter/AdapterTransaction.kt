package com.example.letsbuy.adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.model.Transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class AdapterTransaction (
    private val transactions: List<Transaction>
): RecyclerView.Adapter<AdapterTransaction.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemTransactionView =
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_transaction_adapter, parent,false)

        return MyViewHolder(itemTransactionView)

    }

    override fun getItemCount(): Int = transactions.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactions.asReversed()[position]

        if (transaction.transactionType == "DEPOSIT"){
            holder.transactionType.text = "DEPÓSITO"
            holder.transactionValue.setTextColor(Color.parseColor("#05B501"))
            holder.transactionValue.text = "+ R\$ ${transaction.amount}"
        } else {
            holder.transactionType.text = "SAQUE"
            holder.transactionValue.setTextColor(Color.parseColor("#F14866"))
            holder.transactionValue.text = "- R\$ ${transaction.amount}"
        }

        holder.transactionDate.text = try {
            val formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
            LocalDateTime
                .parse(transaction.createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"))
                .format(formatter)
        } catch (e: Exception){
            e.message
        }



    }

    class MyViewHolder(itemTransactionView: View) : RecyclerView.ViewHolder(itemTransactionView){
        val transactionType: TextView = itemTransactionView.findViewById(R.id.transactionType)
        val transactionDate: TextView = itemTransactionView.findViewById(R.id.transactionDate)
        val transactionValue: TextView = itemTransactionView.findViewById(R.id.transactionValue)
    }
}