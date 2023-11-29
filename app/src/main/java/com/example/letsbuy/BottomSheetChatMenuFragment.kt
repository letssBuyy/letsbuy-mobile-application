package com.example.letsbuy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.BottomsheetChatMenuFragmentBinding
import com.example.letsbuy.databinding.BottomsheetWithdrawFragmentBinding
import com.example.letsbuy.dto.TransactionPayload
import com.example.letsbuy.listener.BottomSheetChatMenuListener
import com.example.letsbuy.listener.BottomSheetWithdrawListener
import com.example.letsbuy.service.WalletService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetChatMenuFragment(userId: Long, adId: Long) : BottomSheetDialogFragment(){

    private lateinit var binding: BottomsheetChatMenuFragmentBinding
    private val idUser = userId
    private val adId = adId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetChatMenuFragmentBinding.inflate(layoutInflater)
        return binding.root

        binding.btnRedirect.setOnClickListener {

            binding.btnAd.visibility = View.GONE
            binding.btnRedirect.visibility = View.GONE
            binding.tvMenu.visibility = View.GONE


            binding.title.visibility = View.VISIBLE
            binding.value.visibility = View.VISIBLE
            binding.btnSend.visibility = View.VISIBLE
            binding.tvProposal.visibility = View.INVISIBLE
        }

        binding.btnAd.setOnClickListener {
            val intent = Intent(context, AdDetailActivity::class.java)
            intent.putExtra("ID_AD", adId)
            startActivity(intent)
        }
    }

    private var bottomSheetChatMenuListener: BottomSheetChatMenuListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomSheetChatMenuListener) {
            bottomSheetChatMenuListener = context
        } else {
            throw ClassCastException("$context must implement BottomSheetChatMenuListener")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSend.setOnClickListener {
            if(
                binding.value.text.isNotBlank() &&
                binding.value.text.toString().toDouble() > 0.0
                ){
                binding.progressBar.visibility = View.VISIBLE
                sendProposal(idUser, binding.value.text.toString().toDouble(), "WITHDRAW")
            } else {
                binding.title.text = "Valor Inválido!"
                binding.title.setTextColor(Color.parseColor("#F14866"))
            }
        }

    }

    private fun sendProposal(userId: Long, amount: Double, transactionType: String){
        val api = Rest.getInstance().create(ChatService::class.java)

        api.createProposal(
            ProposalRequest(
                userId,
                amount,
                transactionType
            )
        ).enqueue(object: Callback<TransactionPayload> {

            override fun onResponse(call: Call<TransactionPayload>, response: Response<TransactionPayload>) {
                binding.progressBar.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    binding.title.text = "Proposta enviada!"
                    binding.title.setTextColor(Color.parseColor("#05B501"))
                    bottomSheetChatMenuListener?.onSendCompleted()
                }
            }

            override fun onFailure(call: Call<TransactionPayload>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.title.text = "Erro durante o envio!"
                binding.title.setTextColor(Color.parseColor("#F14866"))
                bottomSheetChatMenuListener?.onSendCompleted()

            }
        })
    }
}