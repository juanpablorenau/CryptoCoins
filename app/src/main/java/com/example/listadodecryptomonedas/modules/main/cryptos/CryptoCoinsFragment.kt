package com.example.listadodecryptomonedas.modules.main.cryptos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.adapters.CryptoRecyclerAdapter
import com.example.listadodecryptomonedas.adapters.MarketRecyclerAdapter
import com.example.listadodecryptomonedas.data.model.CryptoCoin
import com.example.listadodecryptomonedas.databinding.DialogMarketsBinding
import com.example.listadodecryptomonedas.databinding.FragmentCryptoCoinsBinding
import com.example.listadodecryptomonedas.modules.main.exchanges.ExchangesActivity

class CryptoCoinsFragment :
    Fragment(),
    CryptoRecyclerAdapter.RecyclerViewInterface,
    MarketRecyclerAdapter.RecyclerViewInterface {

    private lateinit var binding: FragmentCryptoCoinsBinding
    private lateinit var viewModel: CryptoCoinsViewModel

    private lateinit var cryptoRecyclerAdapter: CryptoRecyclerAdapter
    private lateinit var marketRecyclerAdapter: MarketRecyclerAdapter

    private lateinit var dialogBinding: DialogMarketsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crypto_coins, container, false)
        binding = FragmentCryptoCoinsBinding.bind(view)
        viewModel = ViewModelProvider(this)[CryptoCoinsViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragment = this

        initialize()

        return view
    }

    private fun initialize() {
        viewModel.getCryptoCoins()
        initCryptoRecyclerAdapter()
        initObservers()
        refreshListener()
    }

    private fun initObservers() {
        viewModel.cryptoCoins.observe(viewLifecycleOwner) { cryptoCoins ->
            binding.progressBar.isVisible = false
            cryptoCoins.sortBy { it.id }
            cryptoRecyclerAdapter.data = cryptoCoins
            binding.swipe.isVisible = true
        }

        viewModel.markets.observe(viewLifecycleOwner) { markets ->
            dialogBinding.progressBar.isVisible = false
            markets.sortBy { it.exchangeId }
            marketRecyclerAdapter.data = markets.distinctBy { it.exchangeId }
            dialogBinding.recyclerMarkets.isVisible = true
        }

        viewModel.cryptoSelectedIds.observe(viewLifecycleOwner) { cryptoCoinsSelectedIds ->
            binding.floatingButton.isVisible = cryptoCoinsSelectedIds.isNotEmpty()
        }

        viewModel.exchangeUrl.observe(viewLifecycleOwner) { url ->
            goToExchangeActivity(url)
        }

        viewModel.communicationError.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != 0) {
                context?.let {
                    Toast.makeText(it, getString(errorMsg), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initCryptoRecyclerAdapter() {
        cryptoRecyclerAdapter = CryptoRecyclerAdapter(this, this)
        binding.recyclerCoins.adapter = cryptoRecyclerAdapter
        binding.recyclerCoins.layoutManager = LinearLayoutManager(context)
    }

    private fun initDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_markets, null)
        dialogBinding = DialogMarketsBinding.bind(dialogView)
        dialogBinding.viewModel = viewModel
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setView(dialogView)
        val dialog = builder?.create()
        dialog?.show()
        initMarketRecyclerAdapter()
    }

    private fun initMarketRecyclerAdapter() {
        marketRecyclerAdapter = MarketRecyclerAdapter(this)
        marketRecyclerAdapter.data = viewModel.markets.value ?: listOf()
        dialogBinding.recyclerMarkets.adapter = marketRecyclerAdapter
        dialogBinding.recyclerMarkets.layoutManager = LinearLayoutManager(context)
    }

    private fun refreshListener() {
        binding.swipe.setOnRefreshListener {
            if (!binding.floatingButton.isVisible) {
                viewModel.getCryptoCoins()
            }
            binding.swipe.isRefreshing = false
        }
    }

    override fun onCryptoClickListener(id: String?) {
        initDialog()
        id?.let { viewModel.getMarkets(it) }
    }

    override fun onCryptoLongClickListener(cryptoCoin: CryptoCoin, position: Int) {
        if (cryptoCoin.isSelected) {
            cryptoCoin.isSelected = false
            cryptoCoin.id?.let {
                viewModel.deleteCryptoId(it)
                viewModel.deleteCryptoSelected(cryptoCoin)
            }
        } else {
            cryptoCoin.isSelected = true
            cryptoCoin.id?.let {
                viewModel.addCryptoId(it)
                viewModel.addCryptoSelected(cryptoCoin)
            }
        }
        cryptoRecyclerAdapter.notifyItemChanged(position)
    }

    override fun onMarketClickListener(id: String?) {
        id?.let { viewModel.getExchange(it) }
    }

    fun addCryptos() {
        viewModel.selectedCryptos.value?.forEach { crypto ->
            crypto.isSelected = false
        }
        cryptoRecyclerAdapter.notifyDataSetChanged()
        viewModel.saveFavoritesIds()
    }

    private fun goToExchangeActivity(url: String?) {
        val toExchangeActivity = Intent(requireContext(), ExchangesActivity::class.java)
        toExchangeActivity.putExtra(getString(R.string.shared_pref_url), url)
        startActivity(toExchangeActivity)
    }
}
