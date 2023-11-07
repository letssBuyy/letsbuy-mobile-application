package com.example.letsbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.letsbuy.adapter.AdapterViewProfile
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityProfileViewBinding
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sellerId = intent.getLongExtra("sellerId", 0)
        binding.progressBar.visibility = View.VISIBLE
        updatePage(sellerId)

        binding.ivImageBack.setOnClickListener {
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }
    }

    private fun initRecyclerView(userAndAdversiments: UserAdversimentsDtoResponse) {
        binding.recyclerViewAdvertisementsSeller.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdvertisementsSeller.setHasFixedSize(true)
        binding.recyclerViewAdvertisementsSeller.adapter =
            AdapterViewProfile(userAndAdversiments, this)
    }

    private fun updatePage(sellerId: Long) {
        Log.d("SELLERID", sellerId.toString())
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(sellerId, null).enqueue(object :
            Callback<UserAdversimentsDtoResponse> {

            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val userAndAdversiments = response.body()

                    Glide.with(this@ProfileViewActivity)
                        .load(userAndAdversiments!!.profileImage)
                        .error(R.drawable.broke_image)
                        .into(binding.profileImage)

                    binding.tvPersonName.text = userAndAdversiments!!.name
                    binding.tvPostDate.text =
                        "Na LetsBuy desde ${userAndAdversiments.registrationDate.substring(0, 10)}"
                    binding.scroll.visibility = View.VISIBLE
                    initRecyclerView(userAndAdversiments)
                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.scroll.visibility = View.GONE
                Toast.makeText(
                    this@ProfileViewActivity,
                    "Ocorreu um erro ao tentar carregar as informações desse usuário",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}