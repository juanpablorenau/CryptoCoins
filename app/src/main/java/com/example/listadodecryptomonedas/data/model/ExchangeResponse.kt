package com.example.listadodecryptomonedas.data.model

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    @SerializedName("data") var exchange: Exchange,
    @SerializedName("timestamp") var timestamp: Long
)
