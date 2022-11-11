package com.example.listadodecryptomonedas.data.model

data class Exchange(
    var id: String? = null,
    var name: String? = null,
    var rank: String? = null,
    var percentTotalVolume: String? = null,
    var volumeUsd: String? = null,
    var tradingPairs: String? = null,
    var socket: String? = null,
    var exchangeUrl: String? = null,
    var updated: Long? = null
)
