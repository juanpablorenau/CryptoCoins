package com.example.listadodecryptomonedas.modules.main.cryptos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listadodecryptomonedas.App
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.api.*
import com.example.listadodecryptomonedas.api.Market
import com.example.listadodecryptomonedas.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoCoinsViewModel : ViewModel() {

    private val preferences = App.preferences

    val cryptoCoins: MutableLiveData<MutableList<CryptoCoin>> = MutableLiveData()
    val markets: MutableLiveData<MutableList<Market>> = MutableLiveData()
    val exchangeUrl = MutableLiveData<String>()

    val cryptoSelectedIds: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val selectedCryptos: MutableLiveData<MutableList<CryptoCoin>> = MutableLiveData(mutableListOf())

    val communicationError: MutableLiveData<Int> = MutableLiveData<Int>()

    fun addCryptoId(id: String) {
        cryptoSelectedIds.value?.add(id)
        cryptoSelectedIds.value = cryptoSelectedIds.value
    }

    fun deleteCryptoId(id: String) {
        cryptoSelectedIds.value?.remove(id)
        cryptoSelectedIds.value = cryptoSelectedIds.value
    }

    fun addCryptoSelected(cryptoCoin: CryptoCoin) {
        selectedCryptos.value?.add(cryptoCoin)
        selectedCryptos.value = selectedCryptos.value
    }

    fun deleteCryptoSelected(cryptoCoin: CryptoCoin) {
        selectedCryptos.value?.remove(cryptoCoin)
        selectedCryptos.value = selectedCryptos.value
    }

    fun saveFavoritesIds() {
        val list = getFavoritesIds().list
        cryptoSelectedIds.value?.let { list?.addAll(it) }
        preferences.setObject(
            App.instance.getString(R.string.shared_pref_object),
            SharedPrefObject(list)
        )
        cryptoSelectedIds.value?.clear()
        cryptoSelectedIds.value = cryptoSelectedIds.value
    }

    private fun getFavoritesIds(): SharedPrefObject {
        return preferences.getObject(App.instance.getString(R.string.shared_pref_object)) as SharedPrefObject
    }

    fun getCryptoCoins() {
        if (Utils.isNetworkAvailable(App.instance)) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient().apiCall(
                    {
                        RetrofitClient().getCryptoCoins()
                    },
                    object : RetrofitClient.RemoteEmitter {
                        override fun onResponse(response: Response<Any>) {
                            val body = response.body() as? CryptoCoinsResponse
                            val list = body?.cryptoCoinList as MutableList<CryptoCoin>
                            cryptoCoins.postValue(list)
                        }

                        override fun onError(errorType: RetrofitClient.ErrorType, msg: String) {
                            Log.e("Api errortype", errorType.toString())
                            Log.e("Api message", msg)

                            communicationError.value = R.string.generic_error_backend
                        }
                    }
                )
            }
        } else {
            // TODO Aquí has de implementar lo que ocurre si no tienes conexión a Internet
        }
    }

    fun getMarkets(id: String) {
        if (Utils.isNetworkAvailable(App.instance)) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient().apiCall(
                    {
                        RetrofitClient().getMarkets(id)
                    },
                    object : RetrofitClient.RemoteEmitter {
                        override fun onResponse(response: Response<Any>) {
                            val body = response.body() as? MarketsResponse
                            val list = body?.marketList as MutableList<Market>
                            markets.postValue(list)
                        }

                        override fun onError(errorType: RetrofitClient.ErrorType, msg: String) {
                            Log.e("Api errortype", errorType.toString())
                            Log.e("Api message", msg)

                            communicationError.value = R.string.generic_error_backend
                        }
                    }
                )
            }
        } else {
            // TODO Aquí has de implementar lo que ocurre si no tienes conexión a Internet
        }
    }

    fun getExchange(id: String) {
        if (Utils.isNetworkAvailable(App.instance)) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient().apiCall(
                    {
                        RetrofitClient().getExchange(id)
                    },
                    object : RetrofitClient.RemoteEmitter {
                        override fun onResponse(response: Response<Any>) {
                            val body = response.body() as? ExchangeResponse
                            val exchange = body?.exchange as Exchange
                            exchangeUrl.postValue(exchange.exchangeUrl.toString())
                        }

                        override fun onError(errorType: RetrofitClient.ErrorType, msg: String) {
                            Log.e("Api errortype", errorType.toString())
                            Log.e("Api message", msg)

                            communicationError.value = R.string.generic_error_backend
                        }
                    }
                )
            }
        } else {
            // TODO Aquí has de implementar lo que ocurre si no tienes conexión a Internet
        }
    }
}
