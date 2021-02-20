package com.bit.bursa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.login.LoginViewModel
import com.bit.bursa.tradelog.TradeLogAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase


class TradeLogFragment : Fragment() {
val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_trade_log, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.TradeLogRecyclerView)
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail

        val tradeLog = mutableListOf<TradeLog>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@TradeLogFragment.context)
                adapter = TradeLogAdapter(tradeLog)
            }
        }

        db.collection("user").document("log").collection(email)
            .get()
            .addOnSuccessListener { result ->
                for (results in result) {
                    val name = results.getField<String>("name").toString()
                    val activity = results.getField<String>("activity").toString()
                    val quantity = results.getField<String>("quantity").toString()
                    val price = results.getField<String>("price").toString()
                    val klse = results.getField<String>("klse").toString()

                    tradeLog.add(TradeLog(name,klse, activity,quantity,price))
                }
                rv()
            }
        return root
    }

}

data class TradeLog (
    val name : String,
    val klse : String,
    val type: String,
    val quantity: String,
    val price : String
        )
