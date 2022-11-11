package com.example.listadodecryptomonedas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.listadodecryptomonedas.App
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.data.model.CryptoCoin

class CryptoRecyclerAdapter(
    private val itemClickListener: RecyclerViewInterface,
    private val itemLongClickListener: RecyclerViewInterface
) :
    RecyclerView.Adapter<CryptoRecyclerAdapter.ViewHolder>() {

    companion object {
        private const val NUMBER_OF_DIGITS = 7
    }

    var data = listOf<CryptoCoin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onCryptoClickListener(data[position].id)
        }

        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onCryptoLongClickListener(data[position], position)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RecyclerViewInterface {

        fun onCryptoClickListener(id: String?)
        fun onCryptoLongClickListener(cryptoCoin: CryptoCoin, position: Int)
    }

    class ViewHolder private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val textCryptoName: TextView = itemView.findViewById(R.id.text_crypto_name)
        private val textCryptoPrice: TextView = itemView.findViewById(R.id.text_crypto_price)
        private val textCryptoVariation: TextView =
            itemView.findViewById(R.id.text_crypto_variation)
        private val constraintLayout: ConstraintLayout =
            itemView.findViewById(R.id.constraint_crypto)

        fun bind(
            item: CryptoCoin
        ) {
            textCryptoName.text = item.name
            textCryptoPrice.text = item.priceUsd?.substring(0, NUMBER_OF_DIGITS).plus("$")
            textCryptoVariation.text =
                item.changePercent24Hr?.substring(0, NUMBER_OF_DIGITS)

            val variationNumber = item.changePercent24Hr?.toDouble() ?: 0.0
            if (variationNumber < 0.0) {
                textCryptoVariation.setTextColor(App.instance.getColor(R.color.red))
            } else {
                textCryptoVariation.setTextColor(App.instance.getColor(R.color.green))
            }
            if (item.isSelected) {
                constraintLayout.setBackgroundColor(App.instance.getColor(R.color.light_gray))
            } else {
                constraintLayout.setBackgroundColor(App.instance.getColor(R.color.white))
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.item_crypto_coin, parent, false)

                return ViewHolder(view)
            }
        }
    }
}
