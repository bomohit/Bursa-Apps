package com.bit.bursa

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.bursa.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_first, container, false)
        val email = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).myEmail
        val recyclerView : RecyclerView = root.findViewById(R.id.watchListRecyclerView)
        val intro: TextView = root.findViewById(R.id.f_intro)
        val ly: ConstraintLayout = root.findViewById(R.id.layoutFirst)

        ly.setOnClickListener {
            closeKeyBoard(it)
        }

        val itemWatchlist = mutableListOf<ItemWatchlist>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@FirstFragment.context)
                adapter = StockAdapter(itemWatchlist)
            }
        }


        fun stockupdate() {
            db.collection("stock")
                .get()
                .addOnSuccessListener { result ->
                    for (results in result ) {
                        val name = results.id
                        val klse = results.getField<String>("stock_klse").toString()
                        val price = results.getField<String>("stock_price").toString()
                        val target = results.getField<String>("price_target").toString()
                        itemWatchlist.add(ItemWatchlist(name,klse,price, target))
                    }
                    rv()
                }
        }


        val stocklist = db.collection("stock")
        stocklist.addSnapshotListener { snapshot, error ->
            if (error != null ) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                stockupdate()
            }
        }

        db.collection("login").document(email)
            .get()
            .addOnSuccessListener { it ->
                val uname = it.getField<String>("username").toString()
                "Welcome, $uname ".also { intro.text = it }
            }

        return root
    }

    private fun closeKeyBoard(v : View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchKey : EditText = view.findViewById(R.id.seachKey)

        view.findViewById<Button>(R.id.button_portfolio).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<Button>(R.id.buttonSearch).setOnClickListener {
            if (searchKey.text.toString().isNullOrEmpty()) {
                Snackbar.make(view,"Enter Search", Snackbar.LENGTH_SHORT).show()
            } else {
                // Show loading here if needed
                // send argument to searchFragment
                val bundle = bundleOf("search_input" to searchKey.text.toString())
                try {
                    findNavController().navigate(R.id.action_FirstFragment_to_searchFragment, bundle)
                } catch (e: IllegalArgumentException) {
                    d("bomoh", "Search Illegal Assumption")
                }
            }

        }

        view.findViewById<Button>(R.id.button_tradelog).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_tradeLogFragment)
        }

        view.findViewById<Button>(R.id.button_predictedPrice).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_predictedPriceFragment)
        }

        view.findViewById<Button>(R.id.button_screener).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_stockScreenerFragment)
        }
    }
}

data class ItemWatchlist (
        val name: String,
        val klse: String,
        val price: String,
        val target: String
        )
