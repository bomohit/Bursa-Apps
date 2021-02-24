package com.bit.bursa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.searchRecyclerView)
        val search_input = arguments?.getString("search_input").toString()

        val searchList = mutableListOf<ItemWatchlist>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@SearchFragment.context)
                adapter = StockAdapter(searchList)
            }
        }

        db.collection("stock")
            .get()
            .addOnSuccessListener { result ->
                for (results in result) {
                    if (results.id.contains(search_input)) {
                        val stockName = results.id
                        val stockPrice = results.getField<String>("stock_price").toString()
                        val stockKlse = results.getField<String>("stock_klse").toString()
                        val stockTarget = results.getField<String>("price_target").toString()
                        searchList.add(ItemWatchlist(stockName, stockKlse, stockPrice, stockTarget))
                    }
                }
                rv()
            }



        return root
    }


}