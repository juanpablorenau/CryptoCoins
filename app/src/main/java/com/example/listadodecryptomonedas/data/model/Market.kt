package com.example.listadodecryptomonedas.data.model

data class Market(
    var exchangeId: String,
    var baseId: String,
    var quoteId: String,
    var baseSymbol: String,
    var quoteSymbol: String,
    var volumeUsd24Hr: String,
    var priceUsd: String,
    var volumePercent: String
)
