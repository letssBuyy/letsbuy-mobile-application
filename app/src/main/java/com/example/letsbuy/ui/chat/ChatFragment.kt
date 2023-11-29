package com.example.letsbuy.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterChat
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentChatBinding
import com.example.letsbuy.dto.ChatRequest
import com.example.letsbuy.dto.ChatResponseDto
import com.example.letsbuy.service.ChatService
import com.example.letsbuy.service.LikeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        binding.progressBar.visibility = View.VISIBLE
        getChats(id)
    }

    private fun initRecyclerView(myList: List<ChatResponseDto>) {
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewChat.setHasFixedSize(true)
        binding.recyclerViewChat.adapter = AdapterChat(myList, requireContext())
    }

    private fun getChats(id: Long?) {
        val api = Rest.getInstance().create(ChatService::class.java)
        api.getChats(id)
            .enqueue(object : Callback<List<ChatResponseDto>> {
                override fun onResponse(
                    call: retrofit2.Call<List<ChatResponseDto>>,
                    response: Response<List<ChatResponseDto>>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data.isNullOrEmpty()) {
                            binding.scrollFav.visibility = View.GONE
                            binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                        }else{
                            initRecyclerView(data)
                            binding.scrollFav.visibility = View.VISIBLE
                            binding.emptyAdvertisementsLiked.visibility = View.GONE
                        }
                    }
                }
                override fun onFailure(
                    call: Call<List<ChatResponseDto>>,
                    t: Throwable
                ) {
                     binding.progressBar.visibility = View.INVISIBLE
                     binding.scrollFav.visibility = View.GONE
                     binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                }
            })
    }
}