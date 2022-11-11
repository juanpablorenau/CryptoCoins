package com.example.listadodecryptomonedas.api

data class Market(
    var exchangeId: String? = null,
    var baseId: String? = null,
    var quoteId: String? = null,
    var baseSymbol: String? = null,
    var quoteSymbol: String? = null,
    var volumeUsd24Hr: String? = null,
    var priceUsd: String? = null,
    var volumePercent: String? = null
)
