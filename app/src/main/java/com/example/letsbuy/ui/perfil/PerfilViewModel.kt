package com.example.letsbuy.ui.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PerfilViewModel {

    private val _text = MutableLiveData<String>().apply {
        value = "This is perfil Fragment"
    }
    val text: LiveData<String> = _text
}