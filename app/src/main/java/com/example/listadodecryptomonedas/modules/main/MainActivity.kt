package com.example.listadodecryptomonedas.modules.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.databinding.ActivityMainBinding
import com.example.listadodecryptomonedas.modules.main.cryptos.CryptoCoinsFragment
import com.example.listadodecryptomonedas.modules.main.favorites.FavouritesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var cryptoCoinsListFragment: CryptoCoinsFragment
    private lateinit var favoritesListFragment: FavouritesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cryptoCoinsListFragment = CryptoCoinsFragment()
        favoritesListFragment = FavouritesFragment()

        loadFragment(cryptoCoinsListFragment)
        initBottomListener()
    }

    private fun initBottomListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_cryptos -> loadFragment(cryptoCoinsListFragment)
                R.id.item_favorites -> loadFragment(favoritesListFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }
}
