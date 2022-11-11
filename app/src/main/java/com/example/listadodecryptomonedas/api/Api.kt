package com.example.listadodecryptomonedas.api

import com.example.listadodecryptomonedas.data.model.CryptoCoinsResponse
import com.example.listadodecryptomonedas.data.model.MarketsResponse
import com.example.listadodecryptomonedas.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("v2/assets/")
    suspend fun getCryptoCoins(): Response<CryptoCoinsResponse>

    @GET("v2/assets/{id}")
    suspend fun getCryptoCoinById(@Path(value = "id") id: String): Response<CryptoResponse>

    @GET("v2/assets/{id}/markets")
    suspend fun getMarkets(@Path(value = "id") id: String): Response<MarketsResponse>

    @GET("v2/exchanges/{id}")
    suspend fun getExchange(@Path(value = "id") id: String): Response<ExchangeResponse>
}
