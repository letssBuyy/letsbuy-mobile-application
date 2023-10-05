package com.example.letsbuy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterMyAdvertisements
import com.example.letsbuy.adapter.AdapterViewProfile
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityProfileViewBinding
import com.example.letsbuy.databinding.ActivityPublishAdBinding
import com.example.letsbuy.dto.AllAdversimentsAndLikeDtoResponse
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.model.AdvertisementResponse
import com.example.letsbuy.model.enums.AdversimentColorEnum
import com.example.letsbuy.model.enums.CategoryEnum
import com.example.letsbuy.model.enums.QualityEnum
import com.example.letsbuy.service.AdversimentService
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityProfileViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updatePage()

    }


    private fun initRecyclerView(userAndAdversiments: UserAdversimentsDtoResponse){
        binding.recyclerViewAdvertisementsSeller.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAdvertisementsSeller.setHasFixedSize(true)
        binding.recyclerViewAdvertisementsSeller.adapter = AdapterViewProfile(userAndAdversiments,this)
    }

    private fun updatePage() {
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(9, 3).enqueue(object:
            Callback<UserAdversimentsDtoResponse> {

            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                if(response.isSuccessful) {
                    val userAndAdversiments = response.body()
                    binding.tvPersonName.text = userAndAdversiments!!.name
                    binding.tvPostDate.text = "Na LetsBuy desde ${userAndAdversiments.registrationDate.substring(0, 10)}"
                    //binding.profileImage.setImageURI(userAndAdversiments.profileImage.toUri())
                    binding.scroll.visibility = View.VISIBLE
                    initRecyclerView(userAndAdversiments)

                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                binding.scroll.visibility = View.GONE
                Toast.makeText(this@ProfileViewActivity, "Ocorreu um erro ao tentar carregar as informações desse usuário", Toast.LENGTH_SHORT).show()
            }
        })
    }
}