package com.example.listadodecryptomonedas.modules.main.exchanges

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.databinding.ActivityExchangesBinding

class ExchangesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExchangesBinding
    private lateinit var viewModel: ExchangesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangesBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ExchangesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel.url.value = intent.getStringExtra(getString(R.string.shared_pref_url))

        initWebView()
    }

    private fun initWebView() {
        binding.webView.webViewClient = WebViewClient()
        viewModel.url.value?.let { binding.webView.loadUrl(it) }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
    }
}
