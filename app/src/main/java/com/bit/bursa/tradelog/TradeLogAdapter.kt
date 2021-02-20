package com.bit.bursa.tradelog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.R
import com.bit.bursa.TradeLog
import java.util.*

class TradeLogAdapter(private val tradeLog: MutableList<TradeLog>) :
    RecyclerView.Adapter<TradeLogAdapter.ViewHolder>() {
    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tl_name : TextView = itemView.findViewById(R.id.tl_name)
        val tl_klse : TextView = itemView.findViewById(R.id.tl_klse)
        val tl_buyOrSell : TextView = itemView.findViewById(R.id.tl_buyOrSell)
        val tl_quantity : TextView = itemView.findViewById(R.id.tl_quantity)
        val tl_buyPrice : TextView = itemView.findViewById(R.id.tl_buyPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tradelog_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trade = tradeLog[position]
        holder.tl_name.text = trade.name.capitalize(Locale.getDefault())
        holder.tl_klse.text = trade.klse
        holder.tl_buyOrSell.text = trade.type.capitalize(Locale.getDefault())
        holder.tl_quantity.text = trade.quantity
        "${ trade.price } MYR".also { holder.tl_buyPrice.text = it }

    }

    override fun getItemCount() = tradeLog.size

}
