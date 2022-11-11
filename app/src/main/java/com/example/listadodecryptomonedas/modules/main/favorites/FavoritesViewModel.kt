package com.example.listadodecryptomonedas.modules.main.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listadodecryptomonedas.App
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.api.RetrofitClient
import com.example.listadodecryptomonedas.api.Utils
import com.example.listadodecryptomonedas.data.model.CryptoCoin
import com.example.listadodecryptomonedas.data.model.CryptoResponse
import com.example.listadodecryptomonedas.data.model.SharedPrefObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class FavoritesViewModel : ViewModel() {

    private val preferences = App.preferences

    val favoriteCryptoCoins: MutableLiveData<MutableList<CryptoCoin>> =
        MutableLiveData(mutableListOf())
    private val favoriteCryptoCoinIds: MutableLiveData<MutableList<String>> = MutableLiveData()
    val communicationError: MutableLiveData<Int> = MutableLiveData<Int>()

    val cryptoList = mutableListOf<CryptoCoin>()

    fun getFavorites() {
        val sharedPrefObject =
            preferences.getObject(App.instance.getString(R.string.shared_pref_object)) as SharedPrefObject
        favoriteCryptoCoinIds.value = sharedPrefObject.list
        favoriteCryptoCoinIds.value?.forEach { id ->
            getCryptoCoinById(id)
        }
    }

    fun deleteId(id: String) {
        favoriteCryptoCoinIds.value?.remove(id)
        favoriteCryptoCoinIds.value = favoriteCryptoCoinIds.value
        preferences.setObject(
            App.instance.getString(R.string.shared_pref_object),
            SharedPrefObject(favoriteCryptoCoinIds.value)
        )
    }

    private fun getCryptoCoinById(id: String) {
        if (Utils.isNetworkAvailable(App.instance)) {
            CoroutineScope(Dispatchers.IO).launch {
                RetrofitClient().apiCall(
                    {
                        RetrofitClient().getCryptoCoinById(id)
                    },
                    object : RetrofitClient.RemoteEmitter {
                        override fun onResponse(response: Response<Any>) {
                            val body = response.body() as? CryptoResponse
                            val crypto = body?.cryptoCoin as CryptoCoin
                            cryptoList.add(crypto)
                            favoriteCryptoCoins.postValue(cryptoList)
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
