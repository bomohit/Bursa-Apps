package com.bit.bursa.stockScreener

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.R
import com.bit.bursa.StockScreenerList
import java.lang.IllegalArgumentException

class StockScreenerAdapter(private val stockScreenerList: MutableList<StockScreenerList>) :
    RecyclerView.Adapter<StockScreenerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockName : TextView = itemView.findViewById(R.id.stockName)
        val stockKlse : TextView = itemView.findViewById(R.id.stockKlse)
        val stockPrice: TextView = itemView.findViewById(R.id.stockPrice)
        val stockTarget: TextView = itemView.findViewById(R.id.priceTarget)
        val layoutStock: ConstraintLayout = itemView.findViewById(R.id.layoutStock)
        val screening: TextView = itemView.findViewById(R.id.screening)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.screening.isVisible = true
        val watchlist = stockScreenerList[position]
        holder.stockName.text = watchlist.name
        holder.stockKlse.text = watchlist.klse
        holder.stockPrice.text = "${ watchlist.price } MYR"
        holder.stockTarget.text = watchlist.target
        holder.screening.text = watchlist.screener.toUpperCase()

        holder.layoutStock.setOnClickListener{
            val bundle = bundleOf("stock_name" to watchlist.name)
            try {
                holder.itemView.findNavController().navigate(R.id.action_stockScreenerFragment_to_detailsFragment, bundle)
            } catch (e: IllegalArgumentException) {
                Log.d("bomoh", "Detail Illegal Assumption : Stock Adapter")

            }
        }
    }

    override fun getItemCount() = stockScreenerList.size

}
