package com.example.letsbuy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.letsbuy.adapter.AdapterChatMessage
import com.example.letsbuy.api.Rest
import com.example.letsbuy.databinding.ActivityChatMessageBinding
import com.example.letsbuy.dto.AdvertisementResponse
import com.example.letsbuy.dto.ImageDtoResponse
import com.example.letsbuy.dto.MapMessage
import com.example.letsbuy.dto.MessageRequest
import com.example.letsbuy.listener.BottomSheetChatMenuListener
import com.example.letsbuy.service.ChatService
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
import java.text.SimpleDateFormat
import java.util.Locale

class ChatMessageActivity : AppCompatActivity(), BottomSheetChatMenuListener {
    private lateinit var binding: ActivityChatMessageBinding
    private var chatPartnerId: Long? = null
    private var userId: Long? = null
    private var chatId: Long? = null
    private var adId: Long? = null

    private var adversimentImage: String? = null
    private var adversimentTitle: String? = null

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

        adversimentImage = intent.getStringExtra("ADVERSIMENT_IMAGE")
        adversimentTitle = intent.getStringExtra("ADVERSIMENT_TITLE")
        adId = intent.getLongExtra("ADVERSIMENT_ID",-1)

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
            val back = Intent(this, HomeActivity::class.java)
            startActivity(back)
        }

        binding.imageShowMoreOptions.setOnClickListener {
            val bottomSheetFragment = BottomSheetChatMenuFragment(
                userId = userId ?: -1,
                chatId = chatId ?: -1,
                adId = adId ?: -1,
                partnerId = chatPartnerId!!
            )
            bottomSheetFragment.show(supportFragmentManager, "BottomSheetDialog")
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
        val profile = Intent(this, ProfileViewActivity::class.java)
        if (chatPartnerId != null) {
            profile.putExtra("sellerId", chatPartnerId)
            startActivity(profile)
        }
    }

    private fun loadUserData(image: String?, name: String?, id: Long) {
        if (image != null) {
            ImageDtoResponse(url = image).let { userProfileImage ->
               Glide.with(baseContext).load(userProfileImage.url).into(binding.imageProfile)
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

                    var data = response.body()

                    if (response.isSuccessful) {
                        Log.w("chatLog", data.toString())
                        if (data.isNullOrEmpty()) {
                            binding.emptyAdvertisementsLiked.visibility = View.VISIBLE
                            initRecyclerView(emptyList())
                        } else {
                            binding.emptyAdvertisementsLiked.visibility = View.GONE
                            val messageGroup : List<MapMessage> = data
                            val messageGroupSorted = messageGroup.sortedBy {
                                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                sdf.parse(it.date)
                            }
                            initRecyclerView(messageGroupSorted)
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
            adversimentTitle ?: "",
            adversimentImage ?: "",
            this,
            this::acceptProposal,
            this::closeProposal
            )
    }

    private fun acceptProposal(idProposal: Long, isUserSessionMessage: Boolean) {
        if (!isUserSessionMessage) {
            val api = Rest.getInstance().create(ChatService::class.java)

            api.acceptProposal(idProposal).enqueue(object : Callback<AdvertisementResponse> {

                override fun onResponse(call: Call<AdvertisementResponse>, response: Response<AdvertisementResponse>) {
                    Log.w("RESPOSTA", response.toString())
                    if (response.isSuccessful) {
                        closeProposal(idProposal,  false)
                        loadMessages()
                    } else {
                        showToast("Não foi aceitar a proposta")
                    }
                }

                override fun onFailure(call: Call<AdvertisementResponse>, t: Throwable) {
                    showToast("Não foi aceitar a proposta")
                }
            })
        }
    }

    private fun closeProposal(idProposal: Long, isUserSessionMessage: Boolean) {
        if (!isUserSessionMessage) {
            val api = Rest.getInstance().create(ChatService::class.java)
            api.deleteProposal(idProposal).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        loadMessages()
                    } else {
                        showToast("Não foi recusar a proposta")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showToast("Não foi recusar a proposta")
                }
            })
        }
    }

    private fun sendMessage() {
        val api = Rest.getInstance().create(ChatService::class.java)
        val message: String = binding.inputMenssager.text.toString()
        val messageRequest = MessageRequest(chatId ?: -1, message, userId ?: -1)
        api.sendMessage(messageRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    loadMessages()
                    binding.inputMenssager.text.clear()
                    closeKeyboard()
                } else {
                    showToast("Não foi possivel enviar e mensagem")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Não foi possivel enviar e mensagem")
            }
        })
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG).show()
    }

    override fun onSendCompleted() {
        loadMessages()
    }
}