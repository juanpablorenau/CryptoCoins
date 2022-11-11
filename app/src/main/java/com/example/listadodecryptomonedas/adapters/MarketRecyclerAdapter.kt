package com.example.listadodecryptomonedas.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadodecryptomonedas.R
import com.example.listadodecryptomonedas.api.Market

class MarketRecyclerAdapter(private val itemClickListener: MarketRecyclerAdapter.RecyclerViewInterface) :
    RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder>() {

    var data = listOf<Market>()
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
            itemClickListener.onMarketClickListener(data[position].exchangeId)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RecyclerViewInterface {
        fun onMarketClickListener(id: String?)
    }

    class ViewHolder private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val textMarketName: TextView = itemView.findViewById(R.id.text_market_name)

        fun bind(
            item: Market
        ) {
            textMarketName.text = item.exchangeId
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.item_market, parent, false)

                return ViewHolder(view)
            }
        }
    }
}
