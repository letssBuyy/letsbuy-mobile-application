package com.example.letsbuy.ui.chat

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterChatMessage
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.FragmentChatMessageBinding
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.UserAdversimentsDtoResponse
import com.example.letsbuy.service.MessageService
import com.example.letsbuy.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMessageFragment : Fragment() {
    private lateinit var binding: FragmentChatMessageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatMessageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = context?.getSharedPreferences("AUTH", AppCompatActivity.MODE_PRIVATE)
        val id = pref?.getString("ID", null)?.toLong()
        binding.progressBar.visibility = View.VISIBLE
        getMessages(id!!)
        getUserById(id)

    }

    private fun initRecyclerView(myList: List<MapMessage>) {
        binding.recyclerViewChatMenssage.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewChatMenssage.setHasFixedSize(true)
        binding.recyclerViewChatMenssage.adapter = AdapterChatMessage(myList, requireContext())
    }

    private fun getMessages(id: Long) {
        val api = Rest.getInstance().create(MessageService::class.java)
        api.getMessage(id)
            .enqueue(object : Callback<List<MapMessage>> {
                override fun onResponse(
                    call: retrofit2.Call<List<MapMessage>>,
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
                            //binding.scrollFav.visibility = View.VISIBLE
                            binding.emptyChats.visibility = View.GONE
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

    private fun getUserById(id: Long) {
        val api = Rest.getInstance().create(UserService::class.java)
        api.getAdversimentsByUser(id, null).enqueue(object : Callback<UserAdversimentsDtoResponse> {
            override fun onResponse(
                call: Call<UserAdversimentsDtoResponse>,
                response: Response<UserAdversimentsDtoResponse>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        binding.nome.text = user.name
                    }
                }
            }

            override fun onFailure(call: Call<UserAdversimentsDtoResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}



