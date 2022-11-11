package com.example.listadodecryptomonedas.data.model

import com.google.gson.annotations.SerializedName

data class AnyResponse(
    @SerializedName("data") var list: List<Any>,
    @SerializedName("timestamp") var timestamp: Long
)