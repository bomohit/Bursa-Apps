package com.bit.bursa.tradelog


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
        val lay : ConstraintLayout = itemView.findViewById(R.id.tradelog_layout)
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

        if (trade.type.capitalize(Locale.getDefault()) == "Sell") {
            holder.lay.background.setTint(Color.parseColor("#B2022F"))
        }
        if (trade.type.capitalize(Locale.getDefault()) == "Buy") {
            holder.lay.background.setTint(Color.parseColor("#82D305"))
        }

    }

    override fun getItemCount() = tradeLog.size

}
