package com.example.letsbuy

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.BottomsheetDeleteAdFragmentBinding
import com.example.letsbuy.databinding.BottomsheetWithdrawFragmentBinding
import com.example.letsbuy.dto.TransactionPayload
import com.example.letsbuy.listener.BottomSheetDeleteAdListener
import com.example.letsbuy.listener.BottomSheetWithdrawListener
import com.example.letsbuy.service.MyAdvertisementService
import com.example.letsbuy.service.WalletService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetDeleteAdFragment(
    private val idAd: Long
): BottomSheetDialogFragment(){

    private lateinit var binding: BottomsheetDeleteAdFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetDeleteAdFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private var bottomSheetDeleteAdListener: BottomSheetDeleteAdListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomSheetDeleteAdListener) {
            bottomSheetDeleteAdListener = context
        } else {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBack.setOnClickListener {
            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            deleteAd(idAd)
        }

    }

    private fun deleteAd(idAd: Long){
        val api = Rest.getInstance().create(MyAdvertisementService::class.java)

        api.deleteAd(idAd).enqueue(object: Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    binding.textView13.text = "Anúncio Excluido!"
                    binding.textView13.setTextColor(Color.parseColor("#05B501"))
                    binding.btnDelete.visibility = View.GONE
                    bottomSheetDeleteAdListener?.onDeleteCompleted()

                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                binding.textView13.text = "Erro durante a exclusão"
                binding.textView13.setTextColor(Color.parseColor("#F14866"))
                bottomSheetDeleteAdListener?.onDeleteCompleted()

            }
        })
    }
}