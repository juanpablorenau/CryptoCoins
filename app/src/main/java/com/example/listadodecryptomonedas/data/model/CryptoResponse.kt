package com.example.listadodecryptomonedas.data.model

import com.google.gson.annotations.SerializedName

data class CryptoResponse(
    @SerializedName("data") var cryptoCoin: CryptoCoin,
    @SerializedName("timestamp") var timestamp: Long
)
