package com.bit.bursa

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.stockScreener.StockScreenerAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase


class StockScreenerFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_stock_screener, container, false)
        val recyclerView : RecyclerView = root.findViewById(R.id.ScreenerRecyclerView)

        val stockScreenerList = mutableListOf<StockScreenerList>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@StockScreenerFragment.context)
                adapter = StockScreenerAdapter(stockScreenerList)
            }
        }

        fun screenerupdate() {
            db.collection("stock")
                .get()
                .addOnSuccessListener { result ->
                    for (results in result ) {
                        val name = results.id
                        val klse = results.getField<String>("stock_klse").toString()
                        val price = results.getField<String>("stock_price").toString()
                        val target = results.getField<String>("price_target").toString()
                        db.collection("screener").document(name)
                            .get()
                            .addOnSuccessListener {
                                val screenerStat = it.getField<String>("status").toString()
                                stockScreenerList.add(StockScreenerList(name,klse,price, target, screenerStat))
                                rv()
                            }
                    }

                }
        }

        val screenerList = db.collection("screener")
        screenerList.addSnapshotListener { snapshot, error ->
            if (error != null ) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                screenerupdate()
            }
        }

        return root
    }


}

data class StockScreenerList (
    val name: String,
    val klse: String,
    val price: String,
    val target: String,
    val screener: String

)

