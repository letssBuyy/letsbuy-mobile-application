package com.example.letsbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsbuy.adapter.AdapterChatMessage
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityChatMessageBinding
import com.example.letsbuy.dto.ImageDtoResponse
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageRequest
import com.example.letsbuy.service.ChatService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatMessageBinding
    private var chatPartnerId: Long? = null
    private var userId: Long? = null
    private var chatId: Long? = null

    private var AdversimentImage: String? = null
    private var AdversimentTitle: String? = null

    private var messagesJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_message)
        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        val prefs = getSharedPreferences("AUTH", MODE_PRIVATE)
        userId = prefs.getString("ID", "")?.toLong() ?: -1

        chatId = intent.getLongExtra("CHAT_ID", -1)

        val partnerName = intent.getStringExtra("PARTNER_NAME")
        val partnerImage = intent.getStringExtra("PARTNER_IMAGE")
        val partnerId = intent.getLongExtra("PARTNER_ID", -1)

        AdversimentImage = intent.getStringExtra("ADVERSIMENT_IMAGE")
        AdversimentTitle = intent.getStringExtra("ADVERSIMENT_TITLE")

        loadUserData(partnerImage, partnerName, partnerId)
        bindLayoutEvents()
        loadMessages()

        scheduleMessageLoading()
    }

    private fun scheduleMessageLoading() {
        messagesJob?.cancel()
        messagesJob = coroutineScope.launch {
            while (isActive) {
                delay(60000) // 1 minutes delay
                withContext(Dispatchers.Main) {
                    loadMessages()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesJob?.cancel()
    }

    private fun bindLayoutEvents() {
        binding.imageBack.setOnClickListener {
            // TODO: implements back to chat
        }

        binding.imageShowMoreOptions.setOnClickListener {
           // TODO: Open actionsheet
        }

        binding.buttonEnviar.setOnClickListener {
            sendMessage()
        }

        binding.editTextName.setOnClickListener {
            sendToUserProfile()
        }

        binding.imageProfile.setOnClickListener {
            sendToUserProfile()
        }
    }

    private fun sendToUserProfile() {
        val profile = Intent(this, ChatActivity::class.java)
        if (chatPartnerId != null) {
            profile.putExtra("sellerId", chatPartnerId)
            startActivity(profile)
        }
    }

    private fun loadUserData(image: String?, name: String?, id: Long) {
        if (image != null) {
            ImageDtoResponse(url = image)?.let { userProfileImage ->
                Picasso.get().load(userProfileImage.url).into(binding.imageProfile)
            }
        }
        binding.editTextName.text = name ?: ""
        chatPartnerId = id
    }

    private fun loadMessages() {
        val api = Rest.getInstance().create(ChatService::class.java)
        chatId?.let {chatId ->
            api.getMessage(chatId).enqueue(object : Callback<List<MapMessage>> {
                override fun onResponse(
                    call: Call<List<MapMessage>>,
                    response: Response<List<MapMessage>>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE

                    val data = response.body()

                    Log.d("chatLOG", data.toString())

                    if (response.isSuccessful) {
                        if (data.isNullOrEmpty()) {
                            binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                        } else {
                            binding.emptyAdvertisementsLiked.visibility = View.GONE
                            initRecyclerView(data)
                        }
                    } else {
                        showToast("Não foi possivel carregar as mensagens")
                    }
                }

                override fun onFailure(call: Call<List<MapMessage>>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    showToast("Não foi possivel carregar as mensagens")
                }
            })
        }
    }

    private fun initRecyclerView(myList: List<MapMessage>) {
        binding.containerMessages.layoutManager = LinearLayoutManager(this)
        binding.containerMessages.setHasFixedSize(true)
        binding.containerMessages.adapter = AdapterChatMessage(myList,
            userId ?: -1,
            AdversimentImage ?: "",
            AdversimentTitle ?: "",
            this)
    }

    private fun sendMessage() {
        val api = Rest.getInstance().create(ChatService::class.java)
        val message: String = binding.inputMenssager.text.toString()
        val messageRequest = MessageRequest(chatId ?: -1, message, userId ?: -1)
        api.sendMessage(messageRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("chatLOG", response.toString())
                if (response.isSuccessful) {
                    loadMessages()
                } else {
                    showToast("Não foi possivel enviar e mensagem")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Não foi possivel enviar e mensagem")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG).show()
    }
}