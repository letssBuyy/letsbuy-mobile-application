package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterTransaction
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityWalletBinding
import com.example.letsbuy.model.Transaction
import com.example.letsbuy.model.Wallet
import com.example.letsbuy.listener.BottomSheetWithdrawListener
import com.example.letsbuy.service.WalletService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletActivity: AppCompatActivity(), BottomSheetWithdrawListener {
    private lateinit var binding: ActivityWalletBinding
    private var amount = "R$ 0.00"
    private lateinit var idUser: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = getSharedPreferences("AUTH", MODE_PRIVATE)
        val userId = pref?.getString("ID", null)?.toLong()
        idUser = userId.toString()
        getWalletData(userId!!)

        binding.imgBack.setOnClickListener() {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        binding.eye.setOnClickListener{
            binding.textView2.text = amount
            binding.eye.visibility = View.GONE
            binding.eyeClosed.visibility = View.VISIBLE
        }

        binding.eyeClosed.setOnClickListener {
            binding.textView2.text = "••••••••"
            binding.eye.visibility = View.VISIBLE
            binding.eyeClosed.visibility = View.GONE
        }

        val bottomSheetWithdrawFragment = BottomSheetWithdrawFragment(userId)
        binding.tvWithdraw.setOnClickListener {
            bottomSheetWithdrawFragment.show(supportFragmentManager, "BottomSheetDialog")
        }

    }

    override fun onWithdrawCompleted() {
        getWalletData(idUser.toLong())
    }

    private fun initRecyclerView(transactions: List<Transaction>){
        binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTransactions.setHasFixedSize(true)
        binding.recyclerViewTransactions.adapter = AdapterTransaction(transactions)
    }

    private fun getWalletData(userId: Long){
        val api = Rest.getInstance().create(WalletService::class.java)
        api.getWallet(userId).enqueue(object: Callback<Wallet> {

            override fun onResponse(call: Call<Wallet>, response: Response<Wallet>) {
                if (response.isSuccessful) {
                    val wallet = response.body()
                    amount = "R$ ${wallet?.balance}"
                    val transactions = wallet?.transactions
                    if (transactions.isNullOrEmpty()) {
                        binding.scroll.visibility = View.GONE
                        binding.emptyTransactions.visibility = View.VISIBLE
                    } else {
                        binding.scroll.visibility = View.VISIBLE
                        binding.emptyTransactions.visibility = View.GONE
                        initRecyclerView(transactions)
                    }
                }
            }

            override fun onFailure(call: Call<Wallet>, t: Throwable) {
                binding.scroll.visibility = View.GONE
                binding.emptyTransactions.visibility = View.VISIBLE
            }
        })
    }

}