package com.example.boersenapp.ui.favoriten

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritenViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favoriten Fragment"
    }
    val text: LiveData<String> = _text
}