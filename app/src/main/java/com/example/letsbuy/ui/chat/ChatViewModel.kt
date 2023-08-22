package com.example.letsbuy.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ChatViewModel {

    private val _text = MutableLiveData<String>().apply {
        value = "This is chat Fragment"
    }
    val text: LiveData<String> = _text
}