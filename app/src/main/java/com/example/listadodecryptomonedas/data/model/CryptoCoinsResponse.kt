package com.example.listadodecryptomonedas.data.model

import com.google.gson.annotations.SerializedName

data class CryptoCoinsResponse(
    @SerializedName("data") var cryptoCoinList: List<CryptoCoin>,
    @SerializedName("timestamp") var timestamp: Long
)
