package com.bit.bursa.portfolio

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.ItemWatchlist
import com.bit.bursa.R
import com.bit.bursa.login.LoginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        d("bomoh", "SecondFragment: $email")
        val root = inflater.inflate(R.layout.fragment_second, container, false)
        val po_intro = root.findViewById<TextView>(R.id.po_intro)
        val recyclerView : RecyclerView = root.findViewById(R.id.portfolioRecyclerView)
        val boughtStockList = mutableListOf<BoughtStockList>()

        db.collection("login").document(email)
            .get()
            .addOnSuccessListener {
                po_intro.text = "${ it.getField<String>("username") }'s Portfolio"
            }


        val balance = db.collection("login").document(email)
        balance.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val bal  = snapshot.getField<String>("balance").toString()
                "$bal MYR ".also { root.findViewById<TextView>(R.id.cashBalance).text = it }
            }
        }

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@SecondFragment.context)
                adapter = BoughtStockAdapter(boughtStockList)
            }
        }

        fun showBoughtStock() {
            d("bomoh", "showboughStock")
            db.collection("user").document("portfolio").collection(email)
                    .get()
                    .addOnSuccessListener { result->
                        for (results in result) {
                            val quan = results.getField<String>("quantity").toString()
                            val uid = results.id

                            if (quan != "0") {
                                db.collection("stock").document(uid)
                                        .get()
                                        .addOnSuccessListener {
                                            val klse = it.getField<String>("stock_klse").toString()
                                            val price = it.getField<String>("stock_price").toString()
                                            boughtStockList.add(BoughtStockList(uid,klse,price,quan))
                                            rv()
                                        }
                            }
                        }
                    }
        }

        showBoughtStock()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<Button>(R.id.buttonAddCapital).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_addCapitalFragment)
        }
    }
}

data class BoughtStockList (
        val name : String,
        val klse : String,
        val price : String,
        val quantity : String
)
