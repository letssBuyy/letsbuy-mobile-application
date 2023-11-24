package com.example.letsbuy

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.example.letsbuy.adapter.AdapterChatMessage
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityChatMessageBinding
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.service.ChatService
import com.example.letsbuy.service.MessageService
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)
        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nameSeller = if (intent.getStringExtra("NAME_SELLER").isNullOrEmpty()) "Vendedor" else intent.getStringExtra("NAME_SELLER")
        val imgSeller = if (intent.getStringExtra("IMG_SELLER").isNullOrEmpty()) "quebrou" else intent.getStringExtra("IMG_SELLER")
        val pref = this?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        binding.progressBar.visibility = View.VISIBLE
        getMessages(id!!, nameSeller!!)

    }
    private fun initRecyclerView(myList: List<MapMessage>) {
        binding.recyclerViewChatMessage.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChatMessage.setHasFixedSize(true)
        binding.recyclerViewChatMessage.adapter = AdapterChatMessage(myList, this)
    }

    private fun getMessages(id: Long, nameSeller: String) {
        Log.w("NOMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", nameSeller)

        val api = Rest.getInstance().create(MessageService::class.java)
        api.getMessage(id)
            .enqueue(object : Callback<List<MapMessage>> {
                override fun onResponse(
                    call: Call<List<MapMessage>>,
                    response: Response<List<MapMessage>>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data.isNullOrEmpty()) {
                            //binding.scrollFav.visibility = View.GONE
                            binding.emptyChats.visibility = View.VISIBLE
                        } else {
                            initRecyclerView(data)
                            Log.w("NOMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", nameSeller)
                            Log.w("NOMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", nameSeller)
                            //binding.scrollFav.visibility = View.VISIBLE
                            binding.emptyChats.visibility = View.GONE
                            binding.nome.text = nameSeller
//                            if (imgSeller.equals("quebrou")){
//                                binding.imageProfile.setImageResource(R.drawable.broke_image)
//                            }else{
//                                Glide.with(binding.imageProfile.context).load(imgSeller.toUri()).into(binding.imageProfile)
//                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<MapMessage>>,
                    t: Throwable
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    //binding.scrollFav.visibility = View.GONE
                    binding.emptyChats.visibility = View.VISIBLE
                }
            })
    }
}