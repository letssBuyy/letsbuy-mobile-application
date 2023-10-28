package com.example.letsbuy

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.BottomsheetWithdrawFragmentBinding
import com.example.letsbuy.dto.TransactionPayload
import com.example.letsbuy.listener.BottomSheetWithdrawListener
import com.example.letsbuy.service.WalletService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetWithdrawFragment(userId: Long) : BottomSheetDialogFragment(){

    private lateinit var binding: BottomsheetWithdrawFragmentBinding
    private val idUser = userId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetWithdrawFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private var bottomSheetWithdrawListener: BottomSheetWithdrawListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomSheetWithdrawListener) {
            bottomSheetWithdrawListener = context
        } else {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnWithdraw.setOnClickListener {
            if(binding.editText2.text.isNotBlank() && binding.editText2.text.toString().toDouble() > 0.0){
                binding.progressBar.visibility = View.VISIBLE
                createTransaction(idUser, binding.editText2.text.toString().toDouble(), "WITHDRAW")
            } else {
                binding.textView13.text = "Valor Inválido!"
                binding.textView13.setTextColor(Color.parseColor("#F14866"))
            }
        }

    }

    private fun createTransaction(userId: Long, amount: Double, transactionType: String){
        val api = Rest.getInstance().create(WalletService::class.java)

        api.createTransaction(
            TransactionPayload(
                userId,
                amount,
                transactionType
            )
        ).enqueue(object: Callback<TransactionPayload> {

            override fun onResponse(call: Call<TransactionPayload>, response: Response<TransactionPayload>) {

                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.textView13.text = "Valor Sacado!"
                    binding.textView13.setTextColor(Color.parseColor("#05B501"))
                    bottomSheetWithdrawListener?.onWithdrawCompleted()

                }
            }

            override fun onFailure(call: Call<TransactionPayload>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.textView13.text = "Erro durante o saque!"
                binding.textView13.setTextColor(Color.parseColor("#F14866"))
                bottomSheetWithdrawListener?.onWithdrawCompleted()

            }
        })
    }
}