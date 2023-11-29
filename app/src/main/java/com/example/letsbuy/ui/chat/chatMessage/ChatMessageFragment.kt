package com.example.letsbuy.ui.chat.chatMessage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.example.letsbuy.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class ChatMessageFragment(
    private val isUserSessionMessage: Boolean,
    private val datetime: String,
    private val message: String,
    private val isProposal: Boolean,
    private val amount: Double,
    private val adversimentTitle: String,
    private val adversimentImage: String,
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
            Picasso.get().load(adversimentImage).into(productImageView)
            priceProductTextView.text = currencyFormater(amount)

            if (isUserSessionMessage) {
                acceptProposalButton.visibility = View.INVISIBLE
                closePoposalButton.visibility = View.INVISIBLE

            } else {
                acceptProposalButton.visibility = View.VISIBLE
                closePoposalButton.visibility = View.VISIBLE

                acceptProposalButton.setOnClickListener {
                    acceptProposal()
                }

                closePoposalButton.setOnClickListener {
                    closeProposal()
                }
            }
        } else {
            val messageTextView: TextView = view.findViewById(R.id.textFieldHour)
            val datetimeTextView: TextView = view.findViewById(R.id.textFieldMessage)

            messageTextView.text = message
            datetimeTextView.text = datetime
        }
    }

    fun currencyFormater(valor: Double): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatoMoeda.format(valor)
    }

    fun acceptProposal() {
        if (!isUserSessionMessage) {
            //TODO: implements accept proposal
        }
    }

    fun closeProposal() {
        if (!isUserSessionMessage) {
            //TODO: implements cancel proposal
        }
    }
}