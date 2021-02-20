package com.bit.bursa

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class StockAdapter(private val itemWatchlist: MutableList<ItemWatchlist>) : RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockName : TextView = itemView.findViewById(R.id.stockName)
        val stockKlse : TextView = itemView.findViewById(R.id.stockKlse)
        val stockPrice: TextView = itemView.findViewById(R.id.stockPrice)
        val stockTarget: TextView = itemView.findViewById(R.id.priceTarget)
        val layoutStock: ConstraintLayout = itemView.findViewById(R.id.layoutStock)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val watchlist = itemWatchlist[position]
        holder.stockName.text = watchlist.name
        holder.stockKlse.text = watchlist.klse
        holder.stockPrice.text = "${ watchlist.price } MYR"
        holder.stockTarget.text = watchlist.target

        holder.layoutStock.setOnClickListener{
            val bundle = bundleOf("stock_name" to watchlist.name)
            try {
                holder.itemView.findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
            } catch (e: IllegalArgumentException) {
                d("bomoh", "Detail Illegal Assumption : Stock Adapter")
                holder.itemView.findNavController().navigate(R.id.action_searchFragment2_to_detailsFragment2, bundle)
            }
        }
    }

    override fun getItemCount() = itemWatchlist.size

}
