package com.example.letsbuy.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.model.Transaction

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val transaction = transactions[position]

        if (transaction.transactionType == "DEPOSIT"){
            holder.transactionType.text = "DEPÓSITO"
            holder.transactionValue.setTextColor(Color.parseColor("#05B501"))
            holder.transactionValue.text = "+ R\$ ${transaction.amount}"
        } else {
            holder.transactionType.text = "SAQUE"
            holder.transactionValue.setTextColor(Color.parseColor("#F14866"))
            holder.transactionValue.text = "- R\$ ${transaction.amount}"
        }

        holder.transactionDate.text = transaction.createdAt

    }

    class MyViewHolder(itemTransactionView: View) : RecyclerView.ViewHolder(itemTransactionView){
        val transactionType: TextView = itemTransactionView.findViewById(R.id.transactionType)
        val transactionDate: TextView = itemTransactionView.findViewById(R.id.transactionDate)
        val transactionValue: TextView = itemTransactionView.findViewById(R.id.transactionValue)
    }
}