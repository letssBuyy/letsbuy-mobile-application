package com.example.letsbuy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.letsbuy.R
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageResponseDto
import com.example.letsbuy.ui.chat.chatMessage.ChatMessageFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdapterChatMessage(
    private val myList: List<MapMessage>,
    private val currentUserId: Long,
    private val adversimentTitle: String,
    private val adversimentImage: String,
    private val context: Context,
    private val acceptProposal: (id: Long, isUserSessionMessage: Boolean) -> Unit,
    private val declineProposal: (id: Long, isUserSessionMessage: Boolean) -> Unit
) : RecyclerView.Adapter<AdapterChatMessage.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messages = myList[position]
        holder.textHour.text = formatDate(messages.date)
        populateMessagesFragment(holder.containerMessages, messages.messages, position)
    }

    private fun populateMessagesFragment(container: LinearLayout, messages: List<MessageResponseDto>, position: Int) {
        val fragmentManager = (context as AppCompatActivity).supportFragmentManager

        val containerId = View.generateViewId()
        container.id = containerId

        val fragmentTransaction = fragmentManager.beginTransaction()

        for (message in messages) {
            val chatFragment = ChatMessageFragment(
                currentUserId == message.idUser,
                message = message.message,
                messageId = message.id,
                datetime = formatDateTime(message.postedAt),
                isProposal = message.isProposal,
                amount = message.amount,
                adversimentTitle = adversimentTitle,
                adversimentImage = adversimentImage,
                context = context,
                acceptProposal = acceptProposal,
                declineProposal = declineProposal
            )

            fragmentTransaction.add(containerId, chatFragment)
        }

        fragmentTransaction.commit()
    }

    fun formatDateTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        try {
            val date = inputFormat.parse(dateTimeString)
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "Formato de data inválido"
        }
    }

    fun formatDate(dateString: String): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
        val date: Date
        try {
            date = dateFormatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "Data Inválida"
        }

        val messageDate = Calendar.getInstance()
        messageDate.time = date
        val currentDate = Calendar.getInstance()
        val formattedDate: String

        if (currentDate.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR)
            && currentDate.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR)
        ) {
            formattedDate = "Hoje"
        } else {
            currentDate.add(Calendar.DAY_OF_YEAR, -1)
            if (currentDate.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR)
                && currentDate.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR)
            ) {
                formattedDate = "Ontem"
            } else if (currentDate.get(Calendar.DAY_OF_WEEK) == messageDate.get(Calendar.DAY_OF_WEEK)) {
                val dayOfWeek = SimpleDateFormat("EEEE", Locale("pt", "BR")).format(date)
                formattedDate = dayOfWeek
            } else {
                formattedDate = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("pt", "BR")).format(date)
            }
        }

        return formattedDate
    }

    override fun getItemCount() = myList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textHour: TextView = itemView.findViewById(R.id.textFieldHour)
        val containerMessages: LinearLayout = itemView.findViewById(R.id.containerFragmentMessages)
    }
}