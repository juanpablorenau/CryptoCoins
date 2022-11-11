package com.example.listadodecryptomonedas.modules.main.exchanges

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExchangesViewModel : ViewModel() {

    val url = MutableLiveData<String>()
}
