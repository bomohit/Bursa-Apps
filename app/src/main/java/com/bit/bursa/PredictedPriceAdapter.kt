package com.bit.bursa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PredictedPriceAdapter(private val predictedList: MutableList<PredictedList>) :
    RecyclerView.Adapter<PredictedPriceAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stockName : TextView = itemView.findViewById(R.id.p_name)
        val stockKlse : TextView = itemView.findViewById(R.id.p_klse)
        val stockPrice: TextView = itemView.findViewById(R.id.p_price)
        val stockPredicted: TextView = itemView.findViewById(R.id.p_predicted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.predicted_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val predicted = predictedList[position]
        holder.stockName.text = predicted.name
        holder.stockKlse.text = predicted.klse
        holder.stockPrice.text = predicted.price
        holder.stockPredicted.text = predicted.predicted

    }

    override fun getItemCount() = predictedList.size

}
