package com.example.letsbuy.ui.curti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CutiViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is curti Fragment"
    }
    val text: LiveData<String> = _text
}