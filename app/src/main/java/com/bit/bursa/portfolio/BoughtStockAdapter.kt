package com.bit.bursa.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.R

class BoughtStockAdapter(private val boughtStockList: MutableList<BoughtStockList>) : RecyclerView.Adapter<BoughtStockAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockName : TextView = itemView.findViewById(R.id.stockName)
        val stockKlse : TextView = itemView.findViewById(R.id.stockKlse)
        val stockPrice: TextView = itemView.findViewById(R.id.stockPrice)
        val stockquantity: TextView = itemView.findViewById(R.id.priceTarget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = boughtStockList[position]
        holder.stockName.text = stock.name
        holder.stockKlse.text = stock.klse
        holder.stockPrice.text = stock.price
        holder.stockquantity.text = stock.quantity
    }

    override fun getItemCount() = boughtStockList.size

}
