package com.example.letsbuy.ui.chat.chatMessage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.letsbuy.R
import java.text.NumberFormat
import java.util.Locale

class ChatMessageFragment(
    private val isUserSessionMessage: Boolean,
    private val datetime: String,
    private val message: String,
    private val messageId: Long,
    private val isProposal: Boolean,
    private val amount: Double,
    private val adversimentTitle: String,
    private val adversimentImage: String,
    private val context: Context,
    private val acceptProposal: (id: Long, isUserSessionMessage: Boolean) -> Unit,
    private val declineProposal: (id: Long, isUserSessionMessage: Boolean) -> Unit
): Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val layoutResId = if (isProposal) {
            R.layout.item_chat_proposal
        } else if (isUserSessionMessage) {
            R.layout.item_chat_message_right
        } else {
            R.layout.item_chat_message_left
        }

        val view = inflater.inflate(layoutResId, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isProposal) {
            val productImageView: ImageView = view.findViewById(R.id.productImageView)
            val titleProductTextView: TextView = view.findViewById(R.id.productTitle)
            val priceProductTextView: TextView = view.findViewById(R.id.productPrice)
            val acceptProposalButton: Button = view.findViewById(R.id.acceptProposalButton)
            val closePoposalButton: Button = view.findViewById(R.id.closeProposalButton)

            titleProductTextView.text = adversimentTitle
            if (adversimentImage.isEmpty()){
                productImageView.setImageResource(R.drawable.broke_image)
            } else {
                Glide.with(context).load(adversimentImage).into(productImageView)
            }
            priceProductTextView.text = currencyFormater(amount)

            if (isUserSessionMessage) {
                acceptProposalButton.visibility = View.INVISIBLE
                closePoposalButton.visibility = View.INVISIBLE

            } else {
                acceptProposalButton.visibility = View.VISIBLE
                closePoposalButton.visibility = View.VISIBLE

                acceptProposalButton.setOnClickListener {
                    acceptProposal(messageId, isUserSessionMessage)
                }

                closePoposalButton.setOnClickListener {
                    declineProposal(messageId, isUserSessionMessage)
                }
            }
        } else {
            val messageTextView: TextView = view.findViewById(R.id.textFieldMessage)
            val datetimeTextView: TextView = view.findViewById(R.id.textFieldHour)

            messageTextView.text = message
            datetimeTextView.text = datetime
        }
    }

    fun currencyFormater(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }
}