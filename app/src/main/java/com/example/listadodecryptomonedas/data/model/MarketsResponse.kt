package com.example.listadodecryptomonedas.data.model

import com.example.listadodecryptomonedas.api.Market
import com.google.gson.annotations.SerializedName

data class MarketsResponse(
    @SerializedName("data") var marketList: List<Market>,
    @SerializedName("timestamp") var timestamp: Long
)
